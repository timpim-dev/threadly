package com.threadly.felixx.dev.mail

import android.content.Context
import com.threadly.felixx.dev.data.AccountEntity
import com.threadly.felixx.dev.data.MailTypes
import com.threadly.felixx.dev.security.SecretsStore
import com.google.android.gms.auth.GoogleAuthUtil
import jakarta.mail.Folder
import jakarta.mail.Message
import jakarta.mail.Session
import jakarta.mail.Store
import jakarta.mail.internet.MimeMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties

class GmailProvider(private val context: Context) : MailProvider {
    companion object { private const val TAG = "GmailProvider" }

    private fun getToken(account: AccountEntity): String {
        android.util.Log.d(TAG, "getToken: ${account.email}")
        val androidAccount = android.accounts.Account(account.email, "com.google")
        val token = GoogleAuthUtil.getToken(context, androidAccount, "oauth2:https://mail.google.com/")
        android.util.Log.d(TAG, "getToken: OK len=${token.length}")
        return token
    }

    override suspend fun receive(account: AccountEntity, cursor: String?): ProviderPage = withContext(Dispatchers.IO) {
        android.util.Log.d(TAG, "receive: start")
        val token = getToken(account)
        android.util.Log.d(TAG, "receive: got token, connecting IMAP")
        val props = Properties().apply {
            put("mail.store.protocol", "imaps")
            put("mail.imaps.host", "imap.gmail.com")
            put("mail.imaps.port", "993")
            put("mail.imaps.connectiontimeout", "10000")
            put("mail.imaps.timeout", "10000")
            put("mail.imaps.auth.mechanisms", "XOAUTH2")
        }
        val session = Session.getInstance(props)
        var store: Store? = null
        var folder: Folder? = null
        try {
            store = session.getStore("imaps")
            store.connect("imap.gmail.com", account.email, token)
            android.util.Log.d(TAG, "receive: connected")
            folder = store.getFolder("INBOX")
            folder.open(Folder.READ_ONLY)
            val messageCount = folder.messageCount
            android.util.Log.d(TAG, "receive: inbox has $messageCount messages")
            if (messageCount == 0) return@withContext ProviderPage(emptyList(), cursor)

            val start = Math.max(1, messageCount - 50)
            val messages = folder.getMessages(start, messageCount)
            // Pre-fetch envelopes + flags in bulk to avoid per-message RTT for headers
            val profile = jakarta.mail.FetchProfile().apply {
                add(jakarta.mail.FetchProfile.Item.ENVELOPE)
                add(jakarta.mail.FetchProfile.Item.FLAGS)
                add(jakarta.mail.FetchProfile.Item.CONTENT_INFO)
                add("In-Reply-To")
                add("References")
                add("Thread-Topic")
                add("Thread-Index")
                add("Message-ID")
            }
            folder.fetch(messages, profile)
            android.util.Log.d(TAG, "receive: fetch profile done, parsing...")
            val parsed = messages.mapNotNull { msg ->
                runCatching { MailParser.parse(msg, account.email) }
                    .onFailure { android.util.Log.w(TAG, "parse failed: ${it.message}") }
                    .getOrNull()
            }.reversed()
            android.util.Log.d(TAG, "receive: parsed ${parsed.size} messages")
            ProviderPage(parsed, cursor)
        } finally {
            runCatching { folder?.close(false) }
            runCatching { store?.close() }
        }
    }
    
    override suspend fun send(account: AccountEntity, mail: OutgoingMail): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val token = getToken(account)
            val props = Properties().apply {
                put("mail.transport.protocol", "smtps")
                put("mail.smtps.host", "smtp.gmail.com")
                put("mail.smtps.port", "465")
                put("mail.smtps.auth", "true")
                put("mail.smtps.auth.mechanisms", "XOAUTH2")
            }
            val session = Session.getInstance(props)
            val msg = MimeMessage(session).apply {
                setFrom(account.email)
                setRecipients(Message.RecipientType.TO, mail.to.joinToString(","))
                if (mail.cc.isNotEmpty()) setRecipients(Message.RecipientType.CC, mail.cc.joinToString(","))
                subject = mail.subject
                setText(mail.body)
                if (mail.inReplyTo != null) setHeader("In-Reply-To", mail.inReplyTo)
            }
            val transport = session.getTransport("smtps")
            try {
                transport.connect("smtp.gmail.com", account.email, token)
                transport.sendMessage(msg, msg.allRecipients)
            } finally {
                runCatching { transport.close() }
            }
            msg.messageID ?: ""
        }
    }
    
    override suspend fun testConnection(account: AccountEntity): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val token = getToken(account)
            val props = Properties().apply {
                put("mail.store.protocol", "imaps")
                put("mail.imaps.host", "imap.gmail.com")
                put("mail.imaps.port", "993")
                put("mail.imaps.connectiontimeout", "15000")
                put("mail.imaps.timeout", "15000")
                put("mail.imaps.auth.mechanisms", "XOAUTH2")
            }
            val session = Session.getInstance(props)
            val store = session.getStore("imaps")
            try {
                store.connect("imap.gmail.com", account.email, token)
            } finally {
                runCatching { store.close() }
            }
        }
    }
}

class ImapSmtpProvider(private val secrets: SecretsStore) : MailProvider {
    override suspend fun receive(account: AccountEntity, cursor: String?): ProviderPage = withContext(Dispatchers.IO) {
        val password = secrets.accountSecret(account.id) ?: throw IllegalArgumentException("No password found")
        val props = Properties().apply {
            put("mail.store.protocol", "imaps")
            put("mail.imaps.host", account.imapHost ?: return@withContext ProviderPage(emptyList(), cursor))
            put("mail.imaps.port", "993")
            put("mail.imaps.connectiontimeout", "15000")
            put("mail.imaps.timeout", "15000")
        }
        val session = Session.getInstance(props)
        var store: Store? = null
        var folder: Folder? = null
        try {
            store = session.getStore("imaps")
            store.connect(account.email, password)
            folder = store.getFolder("INBOX")
            folder.open(Folder.READ_ONLY)
            val messageCount = folder.messageCount
            if (messageCount == 0) return@withContext ProviderPage(emptyList(), cursor)
            
            val start = Math.max(1, messageCount - 50) // Fetch last 50 for V1
            val messages = folder.getMessages(start, messageCount)
            val parsed = messages.map { MailParser.parse(it, account.email) }.reversed()
            ProviderPage(parsed, cursor)
        } finally {
            runCatching { folder?.close(false) }
            runCatching { store?.close() }
        }
    }
    
    override suspend fun send(account: AccountEntity, mail: OutgoingMail): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val password = secrets.accountSecret(account.id) ?: throw IllegalArgumentException("No password found")
            val props = Properties().apply {
                put("mail.transport.protocol", "smtps")
                put("mail.smtps.host", account.smtpHost ?: account.imapHost ?: throw IllegalArgumentException("No SMTP host"))
                put("mail.smtps.port", "465")
                put("mail.smtps.auth", "true")
            }
            val session = Session.getInstance(props)
            val msg = MimeMessage(session).apply {
                setFrom(account.email)
                setRecipients(Message.RecipientType.TO, mail.to.joinToString(","))
                if (mail.cc.isNotEmpty()) setRecipients(Message.RecipientType.CC, mail.cc.joinToString(","))
                subject = mail.subject
                setText(mail.body)
                if (mail.inReplyTo != null) setHeader("In-Reply-To", mail.inReplyTo)
            }
            val transport = session.getTransport("smtps")
            try {
                transport.connect(account.email, password)
                transport.sendMessage(msg, msg.allRecipients)
            } finally {
                runCatching { transport.close() }
            }
            msg.messageID ?: ""
        }
    }
    
    override suspend fun testConnection(account: AccountEntity): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val password = secrets.accountSecret(account.id) ?: throw IllegalArgumentException("No password found")
            val props = Properties().apply {
                put("mail.store.protocol", "imaps")
                put("mail.imaps.host", account.imapHost ?: throw IllegalArgumentException("No IMAP host"))
                put("mail.imaps.port", "993")
                put("mail.imaps.connectiontimeout", "15000")
                put("mail.imaps.timeout", "15000")
            }
            val session = Session.getInstance(props)
            val store = session.getStore("imaps")
            try {
                store.connect(account.email, password)
            } finally {
                runCatching { store.close() }
            }
        }
    }
}

class ProviderRegistry(context: Context, secrets: SecretsStore) {
    private val gmail = GmailProvider(context)
    private val imap = ImapSmtpProvider(secrets)
    fun forAccount(account: AccountEntity): MailProvider = if (account.provider == MailTypes.GMAIL) gmail else imap
}
