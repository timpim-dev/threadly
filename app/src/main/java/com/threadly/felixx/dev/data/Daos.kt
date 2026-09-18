package com.threadly.felixx.dev.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts ORDER BY displayName") fun observeAll(): Flow<List<AccountEntity>>
    @Query("SELECT * FROM accounts WHERE id = :id") suspend fun get(id: String): AccountEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(account: AccountEntity)
    @Delete suspend fun delete(account: AccountEntity)
    @Query("UPDATE accounts SET status = :status, errorMessage = :error WHERE id = :id") suspend fun updateStatus(id: String, status: AccountStatus, error: String?)
}

@Dao
interface ClubDao {
    @Query("SELECT * FROM clubs WHERE deletedAt IS NULL ORDER BY isUnsorted DESC, name") fun observeAll(): Flow<List<ClubEntity>>
    @Query("SELECT * FROM clubs WHERE id = :id") fun observe(id: String): Flow<ClubEntity?>
    @Query("SELECT * FROM clubs WHERE id = :id") suspend fun get(id: String): ClubEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(club: ClubEntity)
    @Query("UPDATE clubs SET deletedAt = :at WHERE id = :id") suspend fun softDelete(id: String, at: Long)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun link(ref: ClubAccountCrossRef)
    @Query("SELECT accountId FROM club_accounts WHERE clubId = :clubId") suspend fun accountIds(clubId: String): List<String>
    @Query("DELETE FROM club_keywords WHERE clubId = :clubId") suspend fun clearKeywords(clubId: String)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun keywords(keywords: List<ClubKeywordEntity>)
    @Query("SELECT keyword FROM club_keywords WHERE clubId = :clubId") suspend fun keywordValues(clubId: String): List<String>
    @Query("DELETE FROM club_contacts WHERE clubId = :clubId") suspend fun clearContacts(clubId: String)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun contacts(contacts: List<ClubContactEntity>)
    @Query("SELECT email FROM club_contacts WHERE clubId = :clubId") suspend fun contactValues(clubId: String): List<String>
}

@Dao
interface ThreadDao {
    @Query("SELECT * FROM threads WHERE clubId = :clubId AND deletedAt IS NULL AND archived = 0 ORDER BY lastMessageAt DESC") fun observeForClub(clubId: String): Flow<List<ThreadEntity>>
    @Query("SELECT * FROM threads WHERE id = :id") fun observe(id: String): Flow<ThreadEntity?>
    @Query("SELECT * FROM threads WHERE id = :id") suspend fun get(id: String): ThreadEntity?
    @Query("SELECT * FROM threads WHERE providerThreadId = :providerId LIMIT 1") suspend fun findByProviderId(providerId: String): ThreadEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(thread: ThreadEntity)
    @Query("UPDATE threads SET deletedAt = :at WHERE id = :id") suspend fun softDelete(id: String, at: Long)
    @Query("UPDATE threads SET unreadCount = 0 WHERE id = :id") suspend fun markRead(id: String)
    @Query("UPDATE threads SET unreadCount = 1 WHERE id = :id AND unreadCount = 0") suspend fun markUnread(id: String)
    @Query("UPDATE threads SET archived = 1 WHERE id = :id") suspend fun archive(id: String)
    @Query("UPDATE threads SET snoozedUntil = :until WHERE id = :id") suspend fun snooze(id: String, until: Long)
    @Query("UPDATE threads SET deletedAt = NULL WHERE id = :id") suspend fun undoDelete(id: String)
    @Query("UPDATE threads SET muted = :muted WHERE id = :id") suspend fun setMuted(id: String, muted: Boolean)
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE threadId = :threadId ORDER BY sentAt") fun observeForThread(threadId: String): Flow<List<MessageEntity>>
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insertAll(messages: List<MessageEntity>)
    @Query("SELECT * FROM messages WHERE providerMessageId = :providerId LIMIT 1") suspend fun findByProviderId(providerId: String): MessageEntity?
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM app_settings WHERE id = 1") fun observe(): Flow<AppSettingsEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun save(settings: AppSettingsEntity)
}

@Dao
interface SyncStateDao {
    @Query("SELECT * FROM sync_states WHERE accountId = :id") suspend fun get(id: String): SyncStateEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun save(state: SyncStateEntity)
}
