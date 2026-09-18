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
public final class ClubDao_Impl implements ClubDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ClubEntity> __insertionAdapterOfClubEntity;

  private final RoomConverters __roomConverters = new RoomConverters();

  private final EntityInsertionAdapter<ClubAccountCrossRef> __insertionAdapterOfClubAccountCrossRef;

  private final EntityInsertionAdapter<ClubKeywordEntity> __insertionAdapterOfClubKeywordEntity;

  private final EntityInsertionAdapter<ClubContactEntity> __insertionAdapterOfClubContactEntity;

  private final SharedSQLiteStatement __preparedStmtOfSoftDelete;

  private final SharedSQLiteStatement __preparedStmtOfClearKeywords;

  private final SharedSQLiteStatement __preparedStmtOfClearContacts;

  public ClubDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfClubEntity = new EntityInsertionAdapter<ClubEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `clubs` (`id`,`name`,`avatarUri`,`avatarColor`,`isUnsorted`,`matchMode`,`deletedAt`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ClubEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getAvatarUri() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getAvatarUri());
        }
        statement.bindLong(4, entity.getAvatarColor());
        final int _tmp = entity.isUnsorted() ? 1 : 0;
        statement.bindLong(5, _tmp);
        final String _tmp_1 = __roomConverters.matchMode(entity.getMatchMode());
        statement.bindString(6, _tmp_1);
        if (entity.getDeletedAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getDeletedAt());
        }
      }
    };
    this.__insertionAdapterOfClubAccountCrossRef = new EntityInsertionAdapter<ClubAccountCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `club_accounts` (`clubId`,`accountId`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ClubAccountCrossRef entity) {
        statement.bindString(1, entity.getClubId());
        statement.bindString(2, entity.getAccountId());
      }
    };
    this.__insertionAdapterOfClubKeywordEntity = new EntityInsertionAdapter<ClubKeywordEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `club_keywords` (`clubId`,`keyword`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ClubKeywordEntity entity) {
        statement.bindString(1, entity.getClubId());
        statement.bindString(2, entity.getKeyword());
      }
    };
    this.__insertionAdapterOfClubContactEntity = new EntityInsertionAdapter<ClubContactEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `club_contacts` (`clubId`,`email`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ClubContactEntity entity) {
        statement.bindString(1, entity.getClubId());
        statement.bindString(2, entity.getEmail());
      }
    };
    this.__preparedStmtOfSoftDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE clubs SET deletedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearKeywords = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM club_keywords WHERE clubId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearContacts = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM club_contacts WHERE clubId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final ClubEntity club, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfClubEntity.insert(club);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object link(final ClubAccountCrossRef ref, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfClubAccountCrossRef.insert(ref);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object keywords(final List<ClubKeywordEntity> keywords,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfClubKeywordEntity.insert(keywords);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object contacts(final List<ClubContactEntity> contacts,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfClubContactEntity.insert(contacts);
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
  public Object clearKeywords(final String clubId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearKeywords.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, clubId);
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
          __preparedStmtOfClearKeywords.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearContacts(final String clubId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearContacts.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, clubId);
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
          __preparedStmtOfClearContacts.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ClubEntity>> observeAll() {
    final String _sql = "SELECT * FROM clubs WHERE deletedAt IS NULL ORDER BY isUnsorted DESC, name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"clubs"}, new Callable<List<ClubEntity>>() {
      @Override
      @NonNull
      public List<ClubEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAvatarUri = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUri");
          final int _cursorIndexOfAvatarColor = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarColor");
          final int _cursorIndexOfIsUnsorted = CursorUtil.getColumnIndexOrThrow(_cursor, "isUnsorted");
          final int _cursorIndexOfMatchMode = CursorUtil.getColumnIndexOrThrow(_cursor, "matchMode");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final List<ClubEntity> _result = new ArrayList<ClubEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ClubEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAvatarUri;
            if (_cursor.isNull(_cursorIndexOfAvatarUri)) {
              _tmpAvatarUri = null;
            } else {
              _tmpAvatarUri = _cursor.getString(_cursorIndexOfAvatarUri);
            }
            final long _tmpAvatarColor;
            _tmpAvatarColor = _cursor.getLong(_cursorIndexOfAvatarColor);
            final boolean _tmpIsUnsorted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsUnsorted);
            _tmpIsUnsorted = _tmp != 0;
            final ClubMatchMode _tmpMatchMode;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfMatchMode);
            _tmpMatchMode = __roomConverters.matchMode(_tmp_1);
            final Long _tmpDeletedAt;
            if (_cursor.isNull(_cursorIndexOfDeletedAt)) {
              _tmpDeletedAt = null;
            } else {
              _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            }
            _item = new ClubEntity(_tmpId,_tmpName,_tmpAvatarUri,_tmpAvatarColor,_tmpIsUnsorted,_tmpMatchMode,_tmpDeletedAt);
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
  public Flow<ClubEntity> observe(final String id) {
    final String _sql = "SELECT * FROM clubs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"clubs"}, new Callable<ClubEntity>() {
      @Override
      @Nullable
      public ClubEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAvatarUri = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUri");
          final int _cursorIndexOfAvatarColor = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarColor");
          final int _cursorIndexOfIsUnsorted = CursorUtil.getColumnIndexOrThrow(_cursor, "isUnsorted");
          final int _cursorIndexOfMatchMode = CursorUtil.getColumnIndexOrThrow(_cursor, "matchMode");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final ClubEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAvatarUri;
            if (_cursor.isNull(_cursorIndexOfAvatarUri)) {
              _tmpAvatarUri = null;
            } else {
              _tmpAvatarUri = _cursor.getString(_cursorIndexOfAvatarUri);
            }
            final long _tmpAvatarColor;
            _tmpAvatarColor = _cursor.getLong(_cursorIndexOfAvatarColor);
            final boolean _tmpIsUnsorted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsUnsorted);
            _tmpIsUnsorted = _tmp != 0;
            final ClubMatchMode _tmpMatchMode;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfMatchMode);
            _tmpMatchMode = __roomConverters.matchMode(_tmp_1);
            final Long _tmpDeletedAt;
            if (_cursor.isNull(_cursorIndexOfDeletedAt)) {
              _tmpDeletedAt = null;
            } else {
              _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            }
            _result = new ClubEntity(_tmpId,_tmpName,_tmpAvatarUri,_tmpAvatarColor,_tmpIsUnsorted,_tmpMatchMode,_tmpDeletedAt);
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
  public Object get(final String id, final Continuation<? super ClubEntity> $completion) {
    final String _sql = "SELECT * FROM clubs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ClubEntity>() {
      @Override
      @Nullable
      public ClubEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAvatarUri = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUri");
          final int _cursorIndexOfAvatarColor = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarColor");
          final int _cursorIndexOfIsUnsorted = CursorUtil.getColumnIndexOrThrow(_cursor, "isUnsorted");
          final int _cursorIndexOfMatchMode = CursorUtil.getColumnIndexOrThrow(_cursor, "matchMode");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final ClubEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAvatarUri;
            if (_cursor.isNull(_cursorIndexOfAvatarUri)) {
              _tmpAvatarUri = null;
            } else {
              _tmpAvatarUri = _cursor.getString(_cursorIndexOfAvatarUri);
            }
            final long _tmpAvatarColor;
            _tmpAvatarColor = _cursor.getLong(_cursorIndexOfAvatarColor);
            final boolean _tmpIsUnsorted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsUnsorted);
            _tmpIsUnsorted = _tmp != 0;
            final ClubMatchMode _tmpMatchMode;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfMatchMode);
            _tmpMatchMode = __roomConverters.matchMode(_tmp_1);
            final Long _tmpDeletedAt;
            if (_cursor.isNull(_cursorIndexOfDeletedAt)) {
              _tmpDeletedAt = null;
            } else {
              _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            }
            _result = new ClubEntity(_tmpId,_tmpName,_tmpAvatarUri,_tmpAvatarColor,_tmpIsUnsorted,_tmpMatchMode,_tmpDeletedAt);
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
  public Object accountIds(final String clubId,
      final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT accountId FROM club_accounts WHERE clubId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, clubId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
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
  public Object keywordValues(final String clubId,
      final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT keyword FROM club_keywords WHERE clubId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, clubId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
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
  public Object contactValues(final String clubId,
      final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT email FROM club_contacts WHERE clubId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, clubId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
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
