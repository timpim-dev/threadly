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

fun normalizeSubject(subject: String): String = subject
    .replace(Regex("^\\s*((re|fw|fwd)\\s*:\\s*)+", RegexOption.IGNORE_CASE), "")
    .trim()
    .lowercase()

fun cleanMessageBody(body: String): String = body
    .replace(Regex("(?ms)^>.*?(\\n|$)"), "")
    .replace(Regex("(?ims)^[- ]{2,3}\\s*original message.*$"), "")
    .replace(Regex("(?ims)^[- ]{2,3}\\s*forwarded message.*$"), "")
    .trim()
