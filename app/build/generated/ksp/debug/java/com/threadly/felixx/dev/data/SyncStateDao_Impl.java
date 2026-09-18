package com.threadly.felixx.dev.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SyncStateDao_Impl implements SyncStateDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SyncStateEntity> __insertionAdapterOfSyncStateEntity;

  public SyncStateDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSyncStateEntity = new EntityInsertionAdapter<SyncStateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sync_states` (`accountId`,`lastCursor`,`lastSuccessAt`,`lastError`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SyncStateEntity entity) {
        statement.bindString(1, entity.getAccountId());
        if (entity.getLastCursor() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getLastCursor());
        }
        if (entity.getLastSuccessAt() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getLastSuccessAt());
        }
        if (entity.getLastError() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLastError());
        }
      }
    };
  }

  @Override
  public Object save(final SyncStateEntity state, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSyncStateEntity.insert(state);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object get(final String id, final Continuation<? super SyncStateEntity> $completion) {
    final String _sql = "SELECT * FROM sync_states WHERE accountId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SyncStateEntity>() {
      @Override
      @Nullable
      public SyncStateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfLastCursor = CursorUtil.getColumnIndexOrThrow(_cursor, "lastCursor");
          final int _cursorIndexOfLastSuccessAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSuccessAt");
          final int _cursorIndexOfLastError = CursorUtil.getColumnIndexOrThrow(_cursor, "lastError");
          final SyncStateEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpAccountId;
            _tmpAccountId = _cursor.getString(_cursorIndexOfAccountId);
            final String _tmpLastCursor;
            if (_cursor.isNull(_cursorIndexOfLastCursor)) {
              _tmpLastCursor = null;
            } else {
              _tmpLastCursor = _cursor.getString(_cursorIndexOfLastCursor);
            }
            final Long _tmpLastSuccessAt;
            if (_cursor.isNull(_cursorIndexOfLastSuccessAt)) {
              _tmpLastSuccessAt = null;
            } else {
              _tmpLastSuccessAt = _cursor.getLong(_cursorIndexOfLastSuccessAt);
            }
            final String _tmpLastError;
            if (_cursor.isNull(_cursorIndexOfLastError)) {
              _tmpLastError = null;
            } else {
              _tmpLastError = _cursor.getString(_cursorIndexOfLastError);
            }
            _result = new SyncStateEntity(_tmpAccountId,_tmpLastCursor,_tmpLastSuccessAt,_tmpLastError);
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
