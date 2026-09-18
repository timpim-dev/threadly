package com.threadly.felixx.dev.data

import androidx.room.withTransaction
import com.threadly.felixx.dev.mail.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class ThreadlyRepository(private val db: ThreadlyDatabase) {
    val accounts: Flow<List<AccountEntity>> = db.accounts().observeAll()
    val clubs: Flow<List<ClubEntity>> = db.clubs().observeAll()
    val settings: Flow<AppSettingsEntity?> = db.settings().observe()

    suspend fun ensureDefaults() {
        if (db.clubs().get("unsorted") == null) db.clubs().upsert(ClubEntity("unsorted", "Unsorted", isUnsorted = true))
        if (db.settings().observeInitial() == null) db.settings().save(AppSettingsEntity())
    }

    fun threads(clubId: String) = db.threads().observeForClub(clubId)
    fun thread(id: String) = db.threads().observe(id)
    fun messages(threadId: String) = db.messages().observeForThread(threadId)
    fun club(id: String) = db.clubs().observe(id)
    suspend fun getClubKeywords(id: String) = db.clubs().keywordValues(id)
    suspend fun getClubContacts(id: String) = db.clubs().contactValues(id)
    suspend fun getClubAccounts(id: String) = db.clubs().accountIds(id)

    suspend fun saveSettings(value: AppSettingsEntity) = db.settings().save(value)
    suspend fun saveAccount(value: AccountEntity) = db.accounts().upsert(value)
    suspend fun deleteAccount(value: AccountEntity) = db.accounts().delete(value)
    suspend fun saveClub(value: ClubEntity, keywords: List<String>, contacts: List<String>, accountIds: List<String>) {
        db.withTransaction {
            db.clubs().upsert(value)
            db.clubs().clearKeywords(value.id)
            db.clubs().keywords(keywords.map { ClubKeywordEntity(value.id, it.trim()) }.filter { it.keyword.isNotEmpty() })
            db.clubs().clearContacts(value.id)
            db.clubs().contacts(contacts.map { ClubContactEntity(value.id, it.trim().lowercase()) }.filter { it.email.isNotEmpty() })
            accountIds.forEach { db.clubs().link(ClubAccountCrossRef(value.id, it)) }
        }
        
        // Rematch threads after updating rules
        val matcher = ClubMatcher(db.clubs())
        val clubsList = db.clubs().observeAllOnce()
        val allThreads = db.threads().getAll()
        val unsortedClub = clubsList.first { it.isUnsorted }
        
        for (thread in allThreads) {
            val firstMsg = db.messages().getFirstForThread(thread.id) ?: continue
            val newClub = matcher.match(thread.subject, firstMsg.senderEmail, clubsList) ?: unsortedClub
            if (newClub.id != thread.clubId) {
                db.threads().updateClubId(thread.id, newClub.id)
            }
        }
    }
    suspend fun deleteClub(id: String) { if (id != "unsorted") db.clubs().softDelete(id, System.currentTimeMillis()) }
    suspend fun deleteThread(id: String) = db.threads().softDelete(id, System.currentTimeMillis())
    suspend fun undoDelete(id: String) = db.threads().undoDelete(id)
    suspend fun markRead(id: String) = db.threads().markRead(id)
    suspend fun markUnread(id: String) = db.threads().markUnread(id)
    suspend fun archive(id: String) = db.threads().archive(id)
    suspend fun snooze(id: String) = db.threads().snooze(id, System.currentTimeMillis() + 86_400_000)
    suspend fun setMuted(id: String, muted: Boolean) = db.threads().setMuted(id, muted)

    suspend fun sendReply(threadId: String, body: String) {
        val thread = db.threads().get(threadId) ?: return
        val accounts = db.accounts().observeAll().first()
        val account = accounts.firstOrNull() ?: return
        val now = System.currentTimeMillis()
        val msgId = UUID.randomUUID().toString()
        val message = MessageEntity(
            id = msgId,
            threadId = threadId,
            providerMessageId = "local-$msgId",
            senderName = account.displayName.ifBlank { account.email },
            senderEmail = account.email,
            recipients = "",
            body = body,
            sentAt = now,
            isFromUser = true,
            isRead = true
        )
        db.withTransaction {
            db.messages().insertAll(listOf(message))
            db.threads().upsert(thread.copy(preview = body.take(160), lastMessageAt = now))
        }
    }

    suspend fun ingest(account: AccountEntity, mail: IncomingMail, matcher: ClubMatcher) {
        val clubs = db.clubs().observeAllOnce()
        val club = matcher.match(mail, clubs) ?: clubs.first { it.isUnsorted }
        val normalizedSub = normalizeSubject(mail.subject)
        
        var threadId: String? = null

        // 1. Try references (highest fidelity)
        if (threadId == null && mail.references.isNotEmpty()) {
            for (ref in mail.references) {
                val match = db.messages().findByProviderId(ref)
                if (match != null) {
                    threadId = match.threadId
                    break
                }
            }
        }

        // 2. Try In-Reply-To
        if (threadId == null && mail.inReplyTo != null) {
            threadId = db.messages().findByProviderId(mail.inReplyTo)?.threadId
        }

        // 3. Try explicit thread ID (e.g. Thread-Topic header)
        if (threadId == null && mail.providerThreadId != null) {
            // Find existing thread by provider ID that's in the SAME club
            threadId = db.threads().findByProviderId(mail.providerThreadId)?.takeIf { it.clubId == club.id }?.id
        }

        // 4. Fallback to matching subject exactly within the same club
        if (threadId == null) {
            threadId = db.threads().getForClub(club.id).firstOrNull { normalizeSubject(it.subject) == normalizedSub }?.id
        }

        // 5. Still null? New thread.
        val finalThreadId = threadId ?: UUID.randomUUID().toString()
        val old = db.threads().get(finalThreadId)
        val cleanedBody = cleanMessageBody(mail.body)
        val message = MessageEntity(
            id = UUID.randomUUID().toString(),
            threadId = finalThreadId,
            providerMessageId = mail.providerMessageId,
            senderName = mail.senderName,
            senderEmail = mail.senderEmail,
            recipients = mail.recipients.joinToString(","),
            cc = mail.cc.joinToString(","),
            body = cleanedBody,
            sentAt = mail.sentAt,
            isFromUser = mail.isFromUser,
            isRead = mail.isRead,
            inReplyTo = mail.inReplyTo
        )
        db.withTransaction {
            db.threads().upsert(ThreadEntity(finalThreadId, club.id, account.id, mail.providerThreadId, mail.subject, cleanedBody.take(160), mail.sentAt, (old?.unreadCount ?: 0) + if (!mail.isRead && !mail.isFromUser) 1 else 0))
            db.messages().insertAll(listOf(message))
        }
    }
}

private suspend fun SettingsDao.observeInitial(): AppSettingsEntity? = observe().firstOrNull()
private suspend fun ClubDao.observeAllOnce(): List<ClubEntity> = observeAll().first()
