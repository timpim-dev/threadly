package com.threadly.felixx.dev.mail

import com.threadly.felixx.dev.data.*

class ClubMatcher(private val clubs: ClubDao) {
    suspend fun match(mail: IncomingMail, candidates: List<ClubEntity>): ClubEntity? {
        val subject = mail.subject.lowercase()
        val sender = mail.senderEmail.trim().lowercase()
        return candidates.firstOrNull { club ->
            if (club.isUnsorted || club.deletedAt != null) return@firstOrNull false
            val keywordMatch = clubs.keywordValues(club.id).any { subject.contains(it.lowercase()) }
            val contactMatch = clubs.contactValues(club.id).any { it.lowercase() == sender }
            when (club.matchMode) {
                ClubMatchMode.KEYWORD_ONLY -> keywordMatch
                ClubMatchMode.CONTACT_ONLY -> contactMatch
                ClubMatchMode.KEYWORD_OR_CONTACT -> keywordMatch || contactMatch
            }
        }
    }
}
