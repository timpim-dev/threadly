package com.threadly.felixx.dev.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ThreadlyDatabase_Impl extends ThreadlyDatabase {
  private volatile AccountDao _accountDao;

  private volatile ClubDao _clubDao;

  private volatile ThreadDao _threadDao;

  private volatile MessageDao _messageDao;

  private volatile SettingsDao _settingsDao;

  private volatile SyncStateDao _syncStateDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `accounts` (`id` TEXT NOT NULL, `displayName` TEXT NOT NULL, `email` TEXT NOT NULL, `provider` TEXT NOT NULL, `imapHost` TEXT, `smtpHost` TEXT, `status` TEXT NOT NULL, `enabled` INTEGER NOT NULL, `lastSyncAt` INTEGER, `errorMessage` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `clubs` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `avatarUri` TEXT, `avatarColor` INTEGER NOT NULL, `isUnsorted` INTEGER NOT NULL, `matchMode` TEXT NOT NULL, `deletedAt` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `club_accounts` (`clubId` TEXT NOT NULL, `accountId` TEXT NOT NULL, PRIMARY KEY(`clubId`, `accountId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `club_keywords` (`clubId` TEXT NOT NULL, `keyword` TEXT NOT NULL, PRIMARY KEY(`clubId`, `keyword`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `club_contacts` (`clubId` TEXT NOT NULL, `email` TEXT NOT NULL, PRIMARY KEY(`clubId`, `email`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `threads` (`id` TEXT NOT NULL, `clubId` TEXT NOT NULL, `accountId` TEXT NOT NULL, `providerThreadId` TEXT, `subject` TEXT NOT NULL, `preview` TEXT NOT NULL, `lastMessageAt` INTEGER NOT NULL, `unreadCount` INTEGER NOT NULL, `archived` INTEGER NOT NULL, `snoozedUntil` INTEGER, `deletedAt` INTEGER, `muted` INTEGER NOT NULL, `notificationImportance` INTEGER, `customSoundUri` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_threads_clubId` ON `threads` (`clubId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_threads_accountId` ON `threads` (`accountId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_threads_providerThreadId` ON `threads` (`providerThreadId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `messages` (`id` TEXT NOT NULL, `threadId` TEXT NOT NULL, `providerMessageId` TEXT NOT NULL, `senderName` TEXT NOT NULL, `senderEmail` TEXT NOT NULL, `recipients` TEXT NOT NULL, `cc` TEXT NOT NULL, `body` TEXT NOT NULL, `sentAt` INTEGER NOT NULL, `isFromUser` INTEGER NOT NULL, `isRead` INTEGER NOT NULL, `inReplyTo` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_messages_threadId` ON `messages` (`threadId`)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_messages_providerMessageId` ON `messages` (`providerMessageId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `attachments` (`id` TEXT NOT NULL, `messageId` TEXT NOT NULL, `fileName` TEXT NOT NULL, `mimeType` TEXT NOT NULL, `sizeBytes` INTEGER NOT NULL, `localUri` TEXT, `providerPartId` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_attachments_messageId` ON `attachments` (`messageId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `app_settings` (`id` INTEGER NOT NULL, `fasterSync` INTEGER NOT NULL, `fasterSyncMinutes` INTEGER NOT NULL, `swipeActionLeft` TEXT NOT NULL, `swipeActionRight` TEXT NOT NULL, `notificationsEnabled` INTEGER NOT NULL, `notificationImportance` INTEGER NOT NULL, `notificationSoundUri` TEXT, `vibrationEnabled` INTEGER NOT NULL, `theme` TEXT NOT NULL, `dynamicColors` INTEGER NOT NULL, `seedColor` INTEGER NOT NULL, `fontFamily` TEXT NOT NULL, `messageCornerRadius` INTEGER NOT NULL, `compactLayout` INTEGER NOT NULL, `enterToSend` INTEGER NOT NULL, `showAvatarsInThread` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sync_states` (`accountId` TEXT NOT NULL, `lastCursor` TEXT, `lastSuccessAt` INTEGER, `lastError` TEXT, PRIMARY KEY(`accountId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '1d9655be50412ff55feb82203d19b103')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `accounts`");
        db.execSQL("DROP TABLE IF EXISTS `clubs`");
        db.execSQL("DROP TABLE IF EXISTS `club_accounts`");
        db.execSQL("DROP TABLE IF EXISTS `club_keywords`");
        db.execSQL("DROP TABLE IF EXISTS `club_contacts`");
        db.execSQL("DROP TABLE IF EXISTS `threads`");
        db.execSQL("DROP TABLE IF EXISTS `messages`");
        db.execSQL("DROP TABLE IF EXISTS `attachments`");
        db.execSQL("DROP TABLE IF EXISTS `app_settings`");
        db.execSQL("DROP TABLE IF EXISTS `sync_states`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAccounts = new HashMap<String, TableInfo.Column>(10);
        _columnsAccounts.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("provider", new TableInfo.Column("provider", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("imapHost", new TableInfo.Column("imapHost", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("smtpHost", new TableInfo.Column("smtpHost", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("lastSyncAt", new TableInfo.Column("lastSyncAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("errorMessage", new TableInfo.Column("errorMessage", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAccounts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAccounts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAccounts = new TableInfo("accounts", _columnsAccounts, _foreignKeysAccounts, _indicesAccounts);
        final TableInfo _existingAccounts = TableInfo.read(db, "accounts");
        if (!_infoAccounts.equals(_existingAccounts)) {
          return new RoomOpenHelper.ValidationResult(false, "accounts(com.threadly.felixx.dev.data.AccountEntity).\n"
                  + " Expected:\n" + _infoAccounts + "\n"
                  + " Found:\n" + _existingAccounts);
        }
        final HashMap<String, TableInfo.Column> _columnsClubs = new HashMap<String, TableInfo.Column>(7);
        _columnsClubs.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClubs.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClubs.put("avatarUri", new TableInfo.Column("avatarUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClubs.put("avatarColor", new TableInfo.Column("avatarColor", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClubs.put("isUnsorted", new TableInfo.Column("isUnsorted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClubs.put("matchMode", new TableInfo.Column("matchMode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClubs.put("deletedAt", new TableInfo.Column("deletedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysClubs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesClubs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoClubs = new TableInfo("clubs", _columnsClubs, _foreignKeysClubs, _indicesClubs);
        final TableInfo _existingClubs = TableInfo.read(db, "clubs");
        if (!_infoClubs.equals(_existingClubs)) {
          return new RoomOpenHelper.ValidationResult(false, "clubs(com.threadly.felixx.dev.data.ClubEntity).\n"
                  + " Expected:\n" + _infoClubs + "\n"
                  + " Found:\n" + _existingClubs);
        }
        final HashMap<String, TableInfo.Column> _columnsClubAccounts = new HashMap<String, TableInfo.Column>(2);
        _columnsClubAccounts.put("clubId", new TableInfo.Column("clubId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClubAccounts.put("accountId", new TableInfo.Column("accountId", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysClubAccounts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesClubAccounts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoClubAccounts = new TableInfo("club_accounts", _columnsClubAccounts, _foreignKeysClubAccounts, _indicesClubAccounts);
        final TableInfo _existingClubAccounts = TableInfo.read(db, "club_accounts");
        if (!_infoClubAccounts.equals(_existingClubAccounts)) {
          return new RoomOpenHelper.ValidationResult(false, "club_accounts(com.threadly.felixx.dev.data.ClubAccountCrossRef).\n"
                  + " Expected:\n" + _infoClubAccounts + "\n"
                  + " Found:\n" + _existingClubAccounts);
        }
        final HashMap<String, TableInfo.Column> _columnsClubKeywords = new HashMap<String, TableInfo.Column>(2);
        _columnsClubKeywords.put("clubId", new TableInfo.Column("clubId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClubKeywords.put("keyword", new TableInfo.Column("keyword", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysClubKeywords = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesClubKeywords = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoClubKeywords = new TableInfo("club_keywords", _columnsClubKeywords, _foreignKeysClubKeywords, _indicesClubKeywords);
        final TableInfo _existingClubKeywords = TableInfo.read(db, "club_keywords");
        if (!_infoClubKeywords.equals(_existingClubKeywords)) {
          return new RoomOpenHelper.ValidationResult(false, "club_keywords(com.threadly.felixx.dev.data.ClubKeywordEntity).\n"
                  + " Expected:\n" + _infoClubKeywords + "\n"
                  + " Found:\n" + _existingClubKeywords);
        }
        final HashMap<String, TableInfo.Column> _columnsClubContacts = new HashMap<String, TableInfo.Column>(2);
        _columnsClubContacts.put("clubId", new TableInfo.Column("clubId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClubContacts.put("email", new TableInfo.Column("email", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysClubContacts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesClubContacts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoClubContacts = new TableInfo("club_contacts", _columnsClubContacts, _foreignKeysClubContacts, _indicesClubContacts);
        final TableInfo _existingClubContacts = TableInfo.read(db, "club_contacts");
        if (!_infoClubContacts.equals(_existingClubContacts)) {
          return new RoomOpenHelper.ValidationResult(false, "club_contacts(com.threadly.felixx.dev.data.ClubContactEntity).\n"
                  + " Expected:\n" + _infoClubContacts + "\n"
                  + " Found:\n" + _existingClubContacts);
        }
        final HashMap<String, TableInfo.Column> _columnsThreads = new HashMap<String, TableInfo.Column>(14);
        _columnsThreads.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThreads.put("clubId", new TableInfo.Column("clubId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThreads.put("accountId", new TableInfo.Column("accountId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThreads.put("providerThreadId", new TableInfo.Column("providerThreadId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThreads.put("subject", new TableInfo.Column("subject", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThreads.put("preview", new TableInfo.Column("preview", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThreads.put("lastMessageAt", new TableInfo.Column("lastMessageAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThreads.put("unreadCount", new TableInfo.Column("unreadCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThreads.put("archived", new TableInfo.Column("archived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThreads.put("snoozedUntil", new TableInfo.Column("snoozedUntil", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThreads.put("deletedAt", new TableInfo.Column("deletedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThreads.put("muted", new TableInfo.Column("muted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThreads.put("notificationImportance", new TableInfo.Column("notificationImportance", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThreads.put("customSoundUri", new TableInfo.Column("customSoundUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysThreads = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesThreads = new HashSet<TableInfo.Index>(3);
        _indicesThreads.add(new TableInfo.Index("index_threads_clubId", false, Arrays.asList("clubId"), Arrays.asList("ASC")));
        _indicesThreads.add(new TableInfo.Index("index_threads_accountId", false, Arrays.asList("accountId"), Arrays.asList("ASC")));
        _indicesThreads.add(new TableInfo.Index("index_threads_providerThreadId", false, Arrays.asList("providerThreadId"), Arrays.asList("ASC")));
        final TableInfo _infoThreads = new TableInfo("threads", _columnsThreads, _foreignKeysThreads, _indicesThreads);
        final TableInfo _existingThreads = TableInfo.read(db, "threads");
        if (!_infoThreads.equals(_existingThreads)) {
          return new RoomOpenHelper.ValidationResult(false, "threads(com.threadly.felixx.dev.data.ThreadEntity).\n"
                  + " Expected:\n" + _infoThreads + "\n"
                  + " Found:\n" + _existingThreads);
        }
        final HashMap<String, TableInfo.Column> _columnsMessages = new HashMap<String, TableInfo.Column>(12);
        _columnsMessages.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("threadId", new TableInfo.Column("threadId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("providerMessageId", new TableInfo.Column("providerMessageId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("senderName", new TableInfo.Column("senderName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("senderEmail", new TableInfo.Column("senderEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("recipients", new TableInfo.Column("recipients", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("cc", new TableInfo.Column("cc", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("body", new TableInfo.Column("body", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("sentAt", new TableInfo.Column("sentAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("isFromUser", new TableInfo.Column("isFromUser", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("isRead", new TableInfo.Column("isRead", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("inReplyTo", new TableInfo.Column("inReplyTo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMessages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMessages = new HashSet<TableInfo.Index>(2);
        _indicesMessages.add(new TableInfo.Index("index_messages_threadId", false, Arrays.asList("threadId"), Arrays.asList("ASC")));
        _indicesMessages.add(new TableInfo.Index("index_messages_providerMessageId", true, Arrays.asList("providerMessageId"), Arrays.asList("ASC")));
        final TableInfo _infoMessages = new TableInfo("messages", _columnsMessages, _foreignKeysMessages, _indicesMessages);
        final TableInfo _existingMessages = TableInfo.read(db, "messages");
        if (!_infoMessages.equals(_existingMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "messages(com.threadly.felixx.dev.data.MessageEntity).\n"
                  + " Expected:\n" + _infoMessages + "\n"
                  + " Found:\n" + _existingMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsAttachments = new HashMap<String, TableInfo.Column>(7);
        _columnsAttachments.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("messageId", new TableInfo.Column("messageId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("fileName", new TableInfo.Column("fileName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("mimeType", new TableInfo.Column("mimeType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("sizeBytes", new TableInfo.Column("sizeBytes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("localUri", new TableInfo.Column("localUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttachments.put("providerPartId", new TableInfo.Column("providerPartId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAttachments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAttachments = new HashSet<TableInfo.Index>(1);
        _indicesAttachments.add(new TableInfo.Index("index_attachments_messageId", false, Arrays.asList("messageId"), Arrays.asList("ASC")));
        final TableInfo _infoAttachments = new TableInfo("attachments", _columnsAttachments, _foreignKeysAttachments, _indicesAttachments);
        final TableInfo _existingAttachments = TableInfo.read(db, "attachments");
        if (!_infoAttachments.equals(_existingAttachments)) {
          return new RoomOpenHelper.ValidationResult(false, "attachments(com.threadly.felixx.dev.data.AttachmentEntity).\n"
                  + " Expected:\n" + _infoAttachments + "\n"
                  + " Found:\n" + _existingAttachments);
        }
        final HashMap<String, TableInfo.Column> _columnsAppSettings = new HashMap<String, TableInfo.Column>(17);
        _columnsAppSettings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("fasterSync", new TableInfo.Column("fasterSync", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("fasterSyncMinutes", new TableInfo.Column("fasterSyncMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("swipeActionLeft", new TableInfo.Column("swipeActionLeft", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("swipeActionRight", new TableInfo.Column("swipeActionRight", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("notificationsEnabled", new TableInfo.Column("notificationsEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("notificationImportance", new TableInfo.Column("notificationImportance", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("notificationSoundUri", new TableInfo.Column("notificationSoundUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("vibrationEnabled", new TableInfo.Column("vibrationEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("theme", new TableInfo.Column("theme", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("dynamicColors", new TableInfo.Column("dynamicColors", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("seedColor", new TableInfo.Column("seedColor", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("fontFamily", new TableInfo.Column("fontFamily", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("messageCornerRadius", new TableInfo.Column("messageCornerRadius", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("compactLayout", new TableInfo.Column("compactLayout", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("enterToSend", new TableInfo.Column("enterToSend", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("showAvatarsInThread", new TableInfo.Column("showAvatarsInThread", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAppSettings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAppSettings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAppSettings = new TableInfo("app_settings", _columnsAppSettings, _foreignKeysAppSettings, _indicesAppSettings);
        final TableInfo _existingAppSettings = TableInfo.read(db, "app_settings");
        if (!_infoAppSettings.equals(_existingAppSettings)) {
          return new RoomOpenHelper.ValidationResult(false, "app_settings(com.threadly.felixx.dev.data.AppSettingsEntity).\n"
                  + " Expected:\n" + _infoAppSettings + "\n"
                  + " Found:\n" + _existingAppSettings);
        }
        final HashMap<String, TableInfo.Column> _columnsSyncStates = new HashMap<String, TableInfo.Column>(4);
        _columnsSyncStates.put("accountId", new TableInfo.Column("accountId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncStates.put("lastCursor", new TableInfo.Column("lastCursor", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncStates.put("lastSuccessAt", new TableInfo.Column("lastSuccessAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncStates.put("lastError", new TableInfo.Column("lastError", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSyncStates = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSyncStates = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSyncStates = new TableInfo("sync_states", _columnsSyncStates, _foreignKeysSyncStates, _indicesSyncStates);
        final TableInfo _existingSyncStates = TableInfo.read(db, "sync_states");
        if (!_infoSyncStates.equals(_existingSyncStates)) {
          return new RoomOpenHelper.ValidationResult(false, "sync_states(com.threadly.felixx.dev.data.SyncStateEntity).\n"
                  + " Expected:\n" + _infoSyncStates + "\n"
                  + " Found:\n" + _existingSyncStates);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "1d9655be50412ff55feb82203d19b103", "19388f9f7a7066fa10a6c41cbb2fc548");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "accounts","clubs","club_accounts","club_keywords","club_contacts","threads","messages","attachments","app_settings","sync_states");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `accounts`");
      _db.execSQL("DELETE FROM `clubs`");
      _db.execSQL("DELETE FROM `club_accounts`");
      _db.execSQL("DELETE FROM `club_keywords`");
      _db.execSQL("DELETE FROM `club_contacts`");
      _db.execSQL("DELETE FROM `threads`");
      _db.execSQL("DELETE FROM `messages`");
      _db.execSQL("DELETE FROM `attachments`");
      _db.execSQL("DELETE FROM `app_settings`");
      _db.execSQL("DELETE FROM `sync_states`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AccountDao.class, AccountDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ClubDao.class, ClubDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ThreadDao.class, ThreadDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MessageDao.class, MessageDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SettingsDao.class, SettingsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SyncStateDao.class, SyncStateDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AccountDao accounts() {
    if (_accountDao != null) {
      return _accountDao;
    } else {
      synchronized(this) {
        if(_accountDao == null) {
          _accountDao = new AccountDao_Impl(this);
        }
        return _accountDao;
      }
    }
  }

  @Override
  public ClubDao clubs() {
    if (_clubDao != null) {
      return _clubDao;
    } else {
      synchronized(this) {
        if(_clubDao == null) {
          _clubDao = new ClubDao_Impl(this);
        }
        return _clubDao;
      }
    }
  }

  @Override
  public ThreadDao threads() {
    if (_threadDao != null) {
      return _threadDao;
    } else {
      synchronized(this) {
        if(_threadDao == null) {
          _threadDao = new ThreadDao_Impl(this);
        }
        return _threadDao;
      }
    }
  }

  @Override
  public MessageDao messages() {
    if (_messageDao != null) {
      return _messageDao;
    } else {
      synchronized(this) {
        if(_messageDao == null) {
          _messageDao = new MessageDao_Impl(this);
        }
        return _messageDao;
      }
    }
  }

  @Override
  public SettingsDao settings() {
    if (_settingsDao != null) {
      return _settingsDao;
    } else {
      synchronized(this) {
        if(_settingsDao == null) {
          _settingsDao = new SettingsDao_Impl(this);
        }
        return _settingsDao;
      }
    }
  }

  @Override
  public SyncStateDao syncStates() {
    if (_syncStateDao != null) {
      return _syncStateDao;
    } else {
      synchronized(this) {
        if(_syncStateDao == null) {
          _syncStateDao = new SyncStateDao_Impl(this);
        }
        return _syncStateDao;
      }
    }
  }
}
