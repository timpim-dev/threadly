package com.threadly.felixx.dev.mail

import com.threadly.felixx.dev.data.AccountEntity

 data class IncomingAttachment(val providerPartId: String, val fileName: String, val mimeType: String, val sizeBytes: Long)

data class IncomingMail(
    val providerMessageId: String,
    val providerThreadId: String?,
    val subject: String,
    val senderName: String,
    val senderEmail: String,
    val recipients: List<String>,
    val cc: List<String> = emptyList(),
    val body: String,
    val sentAt: Long,
    val isFromUser: Boolean,
    val isRead: Boolean,
    val messageIdHeader: String? = null,
    val inReplyTo: String? = null,
    val references: List<String> = emptyList(),
    val attachments: List<IncomingAttachment> = emptyList()
)

data class OutgoingMail(
    val to: List<String>,
    val cc: List<String> = emptyList(),
    val subject: String,
    val body: String,
    val inReplyTo: String? = null,
    val attachments: List<OutgoingAttachment> = emptyList()
)

data class OutgoingAttachment(val fileName: String, val mimeType: String, val uri: String)

data class ProviderPage(val messages: List<IncomingMail>, val cursor: String? = null)

interface MailProvider {
    suspend fun receive(account: AccountEntity, cursor: String? = null): ProviderPage
    suspend fun send(account: AccountEntity, mail: OutgoingMail): Result<String>
    suspend fun testConnection(account: AccountEntity): Result<Unit>
}

fun normalizeSubject(subject: String): String {
    var s = subject
    var changed = true
    while (changed) {
        val old = s
        s = s.replace(Regex("^\\s*\\[.*?\\]\\s*"), "")
             .replace(Regex("^\\s*(re|fw|fwd|aw)\\s*:\\s*", RegexOption.IGNORE_CASE), "")
        changed = (old != s)
    }
    return s.trim().lowercase()
}

fun cleanMessageBody(body: String): String {
    // Decode any leftover HTML entities that might have slipped into plain text
    var cleaned = android.text.Html.fromHtml(body, android.text.Html.FROM_HTML_MODE_LEGACY).toString()

    cleaned = cleaned.replace(Regex("(?ms)^>.*?(\\n|$)"), "")
    cleaned = cleaned.replace(Regex("(?ims)^[- _]{2,30}\\s*(original|forwarded) message.*$"), "")
    
    // Strip Gmail/Outlook/Orange reply headers and signatures
    val replyPattern = Regex("(?ims)(_{3,}\\s*De\\s?:|[-_]{3,}\\s*De\\s?:|On\\s.+?wrote:|Le\\s.+?a\\s[eé]crit\\s?:|Envoy[eé]\\s+depuis\\s+(mon\\s+iPhone|l'application\\s+Mail\\s+Orange|mon\\s+appareil|mon\\s+mobile|mon\\s+smartphone).*?$).*")
    cleaned = cleaned.replace(replyPattern, "")
    
    // Strip Google Groups footers
    val groupsFooterPattern = Regex("(?ims)---\\s*(Vous recevez ce message|You received this message).*")
    cleaned = cleaned.replace(groupsFooterPattern, "")
    
    return cleaned.trim()
}
