package com.threadly.felixx.dev.mail

import jakarta.mail.Message
import jakarta.mail.Multipart
import jakarta.mail.Part
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.UUID

object MailParser {

    fun parse(msg: Message, accountEmail: String): IncomingMail {
        val mime = msg as? MimeMessage
        val messageId = mime?.messageID ?: UUID.randomUUID().toString()
        
        val fromAddr = msg.from?.firstOrNull() as? InternetAddress
        val senderName = fromAddr?.personal ?: fromAddr?.address ?: "Unknown"
        val senderEmail = fromAddr?.address ?: ""
        
        val recipients = msg.getRecipients(Message.RecipientType.TO)
            ?.mapNotNull { (it as? InternetAddress)?.address } ?: emptyList()
        val cc = msg.getRecipients(Message.RecipientType.CC)
            ?.mapNotNull { (it as? InternetAddress)?.address } ?: emptyList()
            
        val sentAt = msg.sentDate?.time ?: System.currentTimeMillis()
        val isRead = msg.flags?.contains(jakarta.mail.Flags.Flag.SEEN) ?: false
        val isFromUser = senderEmail.equals(accountEmail, ignoreCase = true)
        
        val inReplyTo = mime?.getHeader("In-Reply-To")?.firstOrNull()
        val references = mime?.getHeader("References")?.firstOrNull()?.split(Regex("\\s+"))?.filter { it.isNotBlank() } ?: emptyList()
        
        val threadIdHeader = mime?.getHeader("Thread-Topic")?.firstOrNull()
            ?: mime?.getHeader("Thread-Index")?.firstOrNull()
            ?: normalizeSubject(msg.subject ?: "")
            
        val bodyText = extractText(msg)

        return IncomingMail(
            providerMessageId = messageId,
            providerThreadId = threadIdHeader.ifBlank { null },
            subject = msg.subject ?: "",
            senderName = senderName,
            senderEmail = senderEmail,
            recipients = recipients,
            cc = cc,
            body = bodyText,
            sentAt = sentAt,
            isFromUser = isFromUser,
            isRead = isRead,
            messageIdHeader = messageId,
            inReplyTo = inReplyTo,
            references = references
        )
    }

    private fun extractText(part: Part): String {
        if (part.isMimeType("text/plain")) {
            return part.content?.toString() ?: ""
        }
        if (part.isMimeType("text/html")) {
            val html = part.content?.toString() ?: ""
            return html.replace(Regex("<[^>]*>"), " ").replace(Regex("\\s+"), " ").trim()
        }
        if (part.isMimeType("multipart/alternative")) {
            val mp = part.content as Multipart
            var htmlText = ""
            for (i in 0 until mp.count) {
                val bodyPart = mp.getBodyPart(i)
                if (bodyPart.isMimeType("text/plain")) {
                    return extractText(bodyPart)
                }
                if (bodyPart.isMimeType("text/html")) {
                    htmlText = extractText(bodyPart)
                }
            }
            return htmlText
        }
        if (part.isMimeType("multipart/*")) {
            val mp = part.content as Multipart
            val sb = StringBuilder()
            for (i in 0 until mp.count) {
                sb.append(extractText(mp.getBodyPart(i)))
            }
            return sb.toString()
        }
        return ""
    }
}
