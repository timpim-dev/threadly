package com.threadly.felixx.dev.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ThreadDao_Impl implements ThreadDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ThreadEntity> __insertionAdapterOfThreadEntity;

  private final SharedSQLiteStatement __preparedStmtOfSoftDelete;

  private final SharedSQLiteStatement __preparedStmtOfMarkRead;

  private final SharedSQLiteStatement __preparedStmtOfMarkUnread;

  private final SharedSQLiteStatement __preparedStmtOfArchive;

  private final SharedSQLiteStatement __preparedStmtOfSnooze;

  private final SharedSQLiteStatement __preparedStmtOfUndoDelete;

  private final SharedSQLiteStatement __preparedStmtOfSetMuted;

  public ThreadDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfThreadEntity = new EntityInsertionAdapter<ThreadEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `threads` (`id`,`clubId`,`accountId`,`providerThreadId`,`subject`,`preview`,`lastMessageAt`,`unreadCount`,`archived`,`snoozedUntil`,`deletedAt`,`muted`,`notificationImportance`,`customSoundUri`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ThreadEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getClubId());
        statement.bindString(3, entity.getAccountId());
        if (entity.getProviderThreadId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getProviderThreadId());
        }
        statement.bindString(5, entity.getSubject());
        statement.bindString(6, entity.getPreview());
        statement.bindLong(7, entity.getLastMessageAt());
        statement.bindLong(8, entity.getUnreadCount());
        final int _tmp = entity.getArchived() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getSnoozedUntil() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getSnoozedUntil());
        }
        if (entity.getDeletedAt() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getDeletedAt());
        }
        final int _tmp_1 = entity.getMuted() ? 1 : 0;
        statement.bindLong(12, _tmp_1);
        if (entity.getNotificationImportance() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getNotificationImportance());
        }
        if (entity.getCustomSoundUri() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getCustomSoundUri());
        }
      }
    };
    this.__preparedStmtOfSoftDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE threads SET deletedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkRead = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE threads SET unreadCount = 0 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkUnread = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE threads SET unreadCount = 1 WHERE id = ? AND unreadCount = 0";
        return _query;
      }
    };
    this.__preparedStmtOfArchive = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE threads SET archived = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSnooze = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE threads SET snoozedUntil = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUndoDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE threads SET deletedAt = NULL WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetMuted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE threads SET muted = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final ThreadEntity thread, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfThreadEntity.insert(thread);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object softDelete(final String id, final long at,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSoftDelete.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, at);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSoftDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markRead(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkRead.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkRead.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markUnread(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkUnread.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkUnread.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object archive(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfArchive.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfArchive.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object snooze(final String id, final long until,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSnooze.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, until);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSnooze.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object undoDelete(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUndoDelete.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUndoDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setMuted(final String id, final boolean muted,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetMuted.acquire();
        int _argIndex = 1;
        final int _tmp = muted ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetMuted.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ThreadEntity>> observeForClub(final String clubId) {
    final String _sql = "SELECT * FROM threads WHERE clubId = ? AND deletedAt IS NULL AND archived = 0 ORDER BY lastMessageAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, clubId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"threads"}, new Callable<List<ThreadEntity>>() {
      @Override
      @NonNull
      public List<ThreadEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClubId = CursorUtil.getColumnIndexOrThrow(_cursor, "clubId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfProviderThreadId = CursorUtil.getColumnIndexOrThrow(_cursor, "providerThreadId");
          final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
          final int _cursorIndexOfPreview = CursorUtil.getColumnIndexOrThrow(_cursor, "preview");
          final int _cursorIndexOfLastMessageAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageAt");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final int _cursorIndexOfArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "archived");
          final int _cursorIndexOfSnoozedUntil = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntil");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfMuted = CursorUtil.getColumnIndexOrThrow(_cursor, "muted");
          final int _cursorIndexOfNotificationImportance = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationImportance");
          final int _cursorIndexOfCustomSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "customSoundUri");
          final List<ThreadEntity> _result = new ArrayList<ThreadEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ThreadEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpClubId;
            _tmpClubId = _cursor.getString(_cursorIndexOfClubId);
            final String _tmpAccountId;
            _tmpAccountId = _cursor.getString(_cursorIndexOfAccountId);
            final String _tmpProviderThreadId;
            if (_cursor.isNull(_cursorIndexOfProviderThreadId)) {
              _tmpProviderThreadId = null;
            } else {
              _tmpProviderThreadId = _cursor.getString(_cursorIndexOfProviderThreadId);
            }
            final String _tmpSubject;
            _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
            final String _tmpPreview;
            _tmpPreview = _cursor.getString(_cursorIndexOfPreview);
            final long _tmpLastMessageAt;
            _tmpLastMessageAt = _cursor.getLong(_cursorIndexOfLastMessageAt);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            final boolean _tmpArchived;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfArchived);
            _tmpArchived = _tmp != 0;
            final Long _tmpSnoozedUntil;
            if (_cursor.isNull(_cursorIndexOfSnoozedUntil)) {
              _tmpSnoozedUntil = null;
            } else {
              _tmpSnoozedUntil = _cursor.getLong(_cursorIndexOfSnoozedUntil);
            }
            final Long _tmpDeletedAt;
            if (_cursor.isNull(_cursorIndexOfDeletedAt)) {
              _tmpDeletedAt = null;
            } else {
              _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            }
            final boolean _tmpMuted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfMuted);
            _tmpMuted = _tmp_1 != 0;
            final Integer _tmpNotificationImportance;
            if (_cursor.isNull(_cursorIndexOfNotificationImportance)) {
              _tmpNotificationImportance = null;
            } else {
              _tmpNotificationImportance = _cursor.getInt(_cursorIndexOfNotificationImportance);
            }
            final String _tmpCustomSoundUri;
            if (_cursor.isNull(_cursorIndexOfCustomSoundUri)) {
              _tmpCustomSoundUri = null;
            } else {
              _tmpCustomSoundUri = _cursor.getString(_cursorIndexOfCustomSoundUri);
            }
            _item = new ThreadEntity(_tmpId,_tmpClubId,_tmpAccountId,_tmpProviderThreadId,_tmpSubject,_tmpPreview,_tmpLastMessageAt,_tmpUnreadCount,_tmpArchived,_tmpSnoozedUntil,_tmpDeletedAt,_tmpMuted,_tmpNotificationImportance,_tmpCustomSoundUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<ThreadEntity> observe(final String id) {
    final String _sql = "SELECT * FROM threads WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"threads"}, new Callable<ThreadEntity>() {
      @Override
      @Nullable
      public ThreadEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClubId = CursorUtil.getColumnIndexOrThrow(_cursor, "clubId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfProviderThreadId = CursorUtil.getColumnIndexOrThrow(_cursor, "providerThreadId");
          final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
          final int _cursorIndexOfPreview = CursorUtil.getColumnIndexOrThrow(_cursor, "preview");
          final int _cursorIndexOfLastMessageAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageAt");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final int _cursorIndexOfArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "archived");
          final int _cursorIndexOfSnoozedUntil = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntil");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfMuted = CursorUtil.getColumnIndexOrThrow(_cursor, "muted");
          final int _cursorIndexOfNotificationImportance = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationImportance");
          final int _cursorIndexOfCustomSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "customSoundUri");
          final ThreadEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpClubId;
            _tmpClubId = _cursor.getString(_cursorIndexOfClubId);
            final String _tmpAccountId;
            _tmpAccountId = _cursor.getString(_cursorIndexOfAccountId);
            final String _tmpProviderThreadId;
            if (_cursor.isNull(_cursorIndexOfProviderThreadId)) {
              _tmpProviderThreadId = null;
            } else {
              _tmpProviderThreadId = _cursor.getString(_cursorIndexOfProviderThreadId);
            }
            final String _tmpSubject;
            _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
            final String _tmpPreview;
            _tmpPreview = _cursor.getString(_cursorIndexOfPreview);
            final long _tmpLastMessageAt;
            _tmpLastMessageAt = _cursor.getLong(_cursorIndexOfLastMessageAt);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            final boolean _tmpArchived;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfArchived);
            _tmpArchived = _tmp != 0;
            final Long _tmpSnoozedUntil;
            if (_cursor.isNull(_cursorIndexOfSnoozedUntil)) {
              _tmpSnoozedUntil = null;
            } else {
              _tmpSnoozedUntil = _cursor.getLong(_cursorIndexOfSnoozedUntil);
            }
            final Long _tmpDeletedAt;
            if (_cursor.isNull(_cursorIndexOfDeletedAt)) {
              _tmpDeletedAt = null;
            } else {
              _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            }
            final boolean _tmpMuted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfMuted);
            _tmpMuted = _tmp_1 != 0;
            final Integer _tmpNotificationImportance;
            if (_cursor.isNull(_cursorIndexOfNotificationImportance)) {
              _tmpNotificationImportance = null;
            } else {
              _tmpNotificationImportance = _cursor.getInt(_cursorIndexOfNotificationImportance);
            }
            final String _tmpCustomSoundUri;
            if (_cursor.isNull(_cursorIndexOfCustomSoundUri)) {
              _tmpCustomSoundUri = null;
            } else {
              _tmpCustomSoundUri = _cursor.getString(_cursorIndexOfCustomSoundUri);
            }
            _result = new ThreadEntity(_tmpId,_tmpClubId,_tmpAccountId,_tmpProviderThreadId,_tmpSubject,_tmpPreview,_tmpLastMessageAt,_tmpUnreadCount,_tmpArchived,_tmpSnoozedUntil,_tmpDeletedAt,_tmpMuted,_tmpNotificationImportance,_tmpCustomSoundUri);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object get(final String id, final Continuation<? super ThreadEntity> $completion) {
    final String _sql = "SELECT * FROM threads WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ThreadEntity>() {
      @Override
      @Nullable
      public ThreadEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClubId = CursorUtil.getColumnIndexOrThrow(_cursor, "clubId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfProviderThreadId = CursorUtil.getColumnIndexOrThrow(_cursor, "providerThreadId");
          final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
          final int _cursorIndexOfPreview = CursorUtil.getColumnIndexOrThrow(_cursor, "preview");
          final int _cursorIndexOfLastMessageAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageAt");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final int _cursorIndexOfArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "archived");
          final int _cursorIndexOfSnoozedUntil = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntil");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfMuted = CursorUtil.getColumnIndexOrThrow(_cursor, "muted");
          final int _cursorIndexOfNotificationImportance = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationImportance");
          final int _cursorIndexOfCustomSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "customSoundUri");
          final ThreadEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpClubId;
            _tmpClubId = _cursor.getString(_cursorIndexOfClubId);
            final String _tmpAccountId;
            _tmpAccountId = _cursor.getString(_cursorIndexOfAccountId);
            final String _tmpProviderThreadId;
            if (_cursor.isNull(_cursorIndexOfProviderThreadId)) {
              _tmpProviderThreadId = null;
            } else {
              _tmpProviderThreadId = _cursor.getString(_cursorIndexOfProviderThreadId);
            }
            final String _tmpSubject;
            _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
            final String _tmpPreview;
            _tmpPreview = _cursor.getString(_cursorIndexOfPreview);
            final long _tmpLastMessageAt;
            _tmpLastMessageAt = _cursor.getLong(_cursorIndexOfLastMessageAt);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            final boolean _tmpArchived;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfArchived);
            _tmpArchived = _tmp != 0;
            final Long _tmpSnoozedUntil;
            if (_cursor.isNull(_cursorIndexOfSnoozedUntil)) {
              _tmpSnoozedUntil = null;
            } else {
              _tmpSnoozedUntil = _cursor.getLong(_cursorIndexOfSnoozedUntil);
            }
            final Long _tmpDeletedAt;
            if (_cursor.isNull(_cursorIndexOfDeletedAt)) {
              _tmpDeletedAt = null;
            } else {
              _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            }
            final boolean _tmpMuted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfMuted);
            _tmpMuted = _tmp_1 != 0;
            final Integer _tmpNotificationImportance;
            if (_cursor.isNull(_cursorIndexOfNotificationImportance)) {
              _tmpNotificationImportance = null;
            } else {
              _tmpNotificationImportance = _cursor.getInt(_cursorIndexOfNotificationImportance);
            }
            final String _tmpCustomSoundUri;
            if (_cursor.isNull(_cursorIndexOfCustomSoundUri)) {
              _tmpCustomSoundUri = null;
            } else {
              _tmpCustomSoundUri = _cursor.getString(_cursorIndexOfCustomSoundUri);
            }
            _result = new ThreadEntity(_tmpId,_tmpClubId,_tmpAccountId,_tmpProviderThreadId,_tmpSubject,_tmpPreview,_tmpLastMessageAt,_tmpUnreadCount,_tmpArchived,_tmpSnoozedUntil,_tmpDeletedAt,_tmpMuted,_tmpNotificationImportance,_tmpCustomSoundUri);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object findByProviderId(final String providerId,
      final Continuation<? super ThreadEntity> $completion) {
    final String _sql = "SELECT * FROM threads WHERE providerThreadId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, providerId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ThreadEntity>() {
      @Override
      @Nullable
      public ThreadEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClubId = CursorUtil.getColumnIndexOrThrow(_cursor, "clubId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfProviderThreadId = CursorUtil.getColumnIndexOrThrow(_cursor, "providerThreadId");
          final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
          final int _cursorIndexOfPreview = CursorUtil.getColumnIndexOrThrow(_cursor, "preview");
          final int _cursorIndexOfLastMessageAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageAt");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final int _cursorIndexOfArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "archived");
          final int _cursorIndexOfSnoozedUntil = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntil");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfMuted = CursorUtil.getColumnIndexOrThrow(_cursor, "muted");
          final int _cursorIndexOfNotificationImportance = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationImportance");
          final int _cursorIndexOfCustomSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "customSoundUri");
          final ThreadEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpClubId;
            _tmpClubId = _cursor.getString(_cursorIndexOfClubId);
            final String _tmpAccountId;
            _tmpAccountId = _cursor.getString(_cursorIndexOfAccountId);
            final String _tmpProviderThreadId;
            if (_cursor.isNull(_cursorIndexOfProviderThreadId)) {
              _tmpProviderThreadId = null;
            } else {
              _tmpProviderThreadId = _cursor.getString(_cursorIndexOfProviderThreadId);
            }
            final String _tmpSubject;
            _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
            final String _tmpPreview;
            _tmpPreview = _cursor.getString(_cursorIndexOfPreview);
            final long _tmpLastMessageAt;
            _tmpLastMessageAt = _cursor.getLong(_cursorIndexOfLastMessageAt);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            final boolean _tmpArchived;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfArchived);
            _tmpArchived = _tmp != 0;
            final Long _tmpSnoozedUntil;
            if (_cursor.isNull(_cursorIndexOfSnoozedUntil)) {
              _tmpSnoozedUntil = null;
            } else {
              _tmpSnoozedUntil = _cursor.getLong(_cursorIndexOfSnoozedUntil);
            }
            final Long _tmpDeletedAt;
            if (_cursor.isNull(_cursorIndexOfDeletedAt)) {
              _tmpDeletedAt = null;
            } else {
              _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            }
            final boolean _tmpMuted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfMuted);
            _tmpMuted = _tmp_1 != 0;
            final Integer _tmpNotificationImportance;
            if (_cursor.isNull(_cursorIndexOfNotificationImportance)) {
              _tmpNotificationImportance = null;
            } else {
              _tmpNotificationImportance = _cursor.getInt(_cursorIndexOfNotificationImportance);
            }
            final String _tmpCustomSoundUri;
            if (_cursor.isNull(_cursorIndexOfCustomSoundUri)) {
              _tmpCustomSoundUri = null;
            } else {
              _tmpCustomSoundUri = _cursor.getString(_cursorIndexOfCustomSoundUri);
            }
            _result = new ThreadEntity(_tmpId,_tmpClubId,_tmpAccountId,_tmpProviderThreadId,_tmpSubject,_tmpPreview,_tmpLastMessageAt,_tmpUnreadCount,_tmpArchived,_tmpSnoozedUntil,_tmpDeletedAt,_tmpMuted,_tmpNotificationImportance,_tmpCustomSoundUri);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
