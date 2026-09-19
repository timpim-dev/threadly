package com.threadly.felixx.dev.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

object MailTypes {
    const val GMAIL = "gmail"
    const val IMAP = "imap"
    const val UNSORTED = "unsorted"
}

enum class AccountStatus { CONNECTED, ERROR, NEEDS_REAUTH, DISCONNECTED }
enum class ClubMatchMode { KEYWORD_ONLY, CONTACT_ONLY, KEYWORD_OR_CONTACT }

enum class SwipeAction { MARK_READ, ARCHIVE, SNOOZE }

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: String,
    val displayName: String = "",
    val email: String,
    val provider: String,
    val imapHost: String? = null,
    val smtpHost: String? = null,
    val refreshToken: String? = null,
    val status: AccountStatus = AccountStatus.DISCONNECTED,
    val enabled: Boolean = true,
    val lastSyncAt: Long? = null,
    val errorMessage: String? = null
)

@Entity(tableName = "clubs")
data class ClubEntity(
    @PrimaryKey val id: String,
    val name: String,
    val avatarUri: String? = null,
    val avatarColor: Long = 0xFF465D91,
    val isUnsorted: Boolean = false,
    val matchMode: ClubMatchMode = ClubMatchMode.KEYWORD_OR_CONTACT,
    val deletedAt: Long? = null
)

@Entity(primaryKeys = ["clubId", "accountId"], tableName = "club_accounts")
data class ClubAccountCrossRef(val clubId: String, val accountId: String)

@Entity(primaryKeys = ["clubId", "keyword"], tableName = "club_keywords")
data class ClubKeywordEntity(val clubId: String, val keyword: String)

@Entity(primaryKeys = ["clubId", "email"], tableName = "club_contacts")
data class ClubContactEntity(val clubId: String, val email: String)

@Entity(
    tableName = "threads",
    indices = [Index("clubId"), Index("accountId"), Index("providerThreadId")]
)
data class ThreadEntity(
    @PrimaryKey val id: String,
    val clubId: String,
    val accountId: String,
    val providerThreadId: String? = null,
    val subject: String,
    val preview: String = "",
    val lastMessageAt: Long = 0,
    val unreadCount: Int = 0,
    val archived: Boolean = false,
    val snoozedUntil: Long? = null,
    val deletedAt: Long? = null,
    val muted: Boolean = false,
    val notificationImportance: Int? = null,
    val customSoundUri: String? = null
)

@Entity(
    tableName = "messages",
    indices = [Index("threadId"), Index(value = ["providerMessageId"], unique = true)]
)
data class MessageEntity(
    @PrimaryKey val id: String,
    val threadId: String,
    val providerMessageId: String,
    val senderName: String,
    val senderEmail: String,
    val recipients: String,
    val cc: String = "",
    val body: String,
    val sentAt: Long,
    val isFromUser: Boolean,
    val isRead: Boolean,
    val inReplyTo: String? = null
)

@Entity(tableName = "attachments", indices = [Index("messageId")])
data class AttachmentEntity(
    @PrimaryKey val id: String,
    val messageId: String,
    val fileName: String,
    val mimeType: String,
    val sizeBytes: Long,
    val localUri: String? = null,
    val providerPartId: String? = null
)

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val fasterSync: Boolean = false,
    val fasterSyncMinutes: Int = 2,
    val swipeActionLeft: SwipeAction = SwipeAction.MARK_READ,
    val swipeActionRight: SwipeAction = SwipeAction.ARCHIVE,
    val notificationsEnabled: Boolean = true,
    val notificationImportance: Int = 3,
    val notificationSoundUri: String? = null,
    val vibrationEnabled: Boolean = true,
    val theme: String = "system",
    val dynamicColors: Boolean = true,
    val seedColor: Long = 0xFF465D91,
    val fontFamily: String = "default",
    val messageCornerRadius: Int = 18,
    val compactLayout: Boolean = false,
    val enterToSend: Boolean = false,
    val showAvatarsInThread: Boolean = true,
    val defaultClubId: String = "unsorted",
    val openRouterApiKey: String? = null,
    val openRouterModel: String = "meta-llama/llama-3.3-70b-instruct:free"
)

@Entity(tableName = "sync_states")
data class SyncStateEntity(
    @PrimaryKey val accountId: String,
    val lastCursor: String? = null,
    val lastSuccessAt: Long? = null,
    val lastError: String? = null
)
