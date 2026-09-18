package com.threadly.felixx.dev.mail

import com.threadly.felixx.dev.data.*

class ClubMatcher(private val clubs: ClubDao) {
    suspend fun match(mail: IncomingMail, candidates: List<ClubEntity>): ClubEntity? {
        return match(mail.subject, mail.senderEmail, candidates, mail.body)
    }

    suspend fun match(subjectStr: String, senderEmail: String, candidates: List<ClubEntity>, body: String = ""): ClubEntity? {
        val subject = subjectStr.lowercase()
        val bodyLower = body.lowercase()
        val sender = senderEmail.trim().lowercase()
        return candidates.firstOrNull { club ->
            if (club.isUnsorted || club.deletedAt != null) return@firstOrNull false
            val keywords = clubs.keywordValues(club.id)
            val keywordMatch = keywords.any { kw ->
                val k = kw.lowercase()
                subject.contains(k) || bodyLower.contains(k)
            }
            val contactMatch = clubs.contactValues(club.id).any { it.lowercase() == sender }
            when (club.matchMode) {
                ClubMatchMode.KEYWORD_ONLY -> keywordMatch
                ClubMatchMode.CONTACT_ONLY -> contactMatch
                ClubMatchMode.KEYWORD_OR_CONTACT -> keywordMatch || contactMatch
            }
        }
    }
}
