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
    }
    suspend fun deleteClub(id: String) { if (id != "unsorted") db.clubs().softDelete(id, System.currentTimeMillis()) }
    suspend fun deleteThread(id: String) = db.threads().softDelete(id, System.currentTimeMillis())
    suspend fun undoDelete(id: String) = db.threads().undoDelete(id)
    suspend fun markRead(id: String) = db.threads().markRead(id)
    suspend fun markUnread(id: String) = db.threads().markUnread(id)
    suspend fun archive(id: String) = db.threads().archive(id)
    suspend fun snooze(id: String) = db.threads().snooze(id, System.currentTimeMillis() + 86_400_000)
    suspend fun setMuted(id: String, muted: Boolean) = db.threads().setMuted(id, muted)

    suspend fun ingest(account: AccountEntity, mail: IncomingMail, matcher: ClubMatcher) {
        val clubs = db.clubs().observeAllOnce()
        val club = matcher.match(mail, clubs) ?: clubs.first { it.isUnsorted }
        val threadId = mail.providerThreadId?.let { providerId -> db.threads().findByProviderId(providerId)?.id }
            ?: mail.inReplyTo?.let { reply -> db.messages().findByProviderId(reply)?.threadId }
            ?: UUID.randomUUID().toString()
        val old = db.threads().get(threadId)
        val cleanedBody = cleanMessageBody(mail.body)
        val message = MessageEntity(UUID.randomUUID().toString(), threadId, mail.providerMessageId, mail.senderName, mail.senderEmail, mail.recipients.joinToString(","), mail.cc.joinToString(","), cleanedBody, mail.sentAt, mail.isFromUser, mail.isRead)
        db.withTransaction {
            db.threads().upsert(ThreadEntity(threadId, club.id, account.id, mail.providerThreadId, mail.subject, cleanedBody.take(160), mail.sentAt, (old?.unreadCount ?: 0) + if (!mail.isRead && !mail.isFromUser) 1 else 0))
            db.messages().insertAll(listOf(message))
        }
    }
}

private suspend fun SettingsDao.observeInitial(): AppSettingsEntity? = observe().firstOrNull()
private suspend fun ClubDao.observeAllOnce(): List<ClubEntity> = observeAll().first()
