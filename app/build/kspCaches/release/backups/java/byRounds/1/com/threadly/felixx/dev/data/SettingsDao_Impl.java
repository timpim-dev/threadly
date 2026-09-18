package com.threadly.felixx.dev.data;

import android.database.Cursor;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SettingsDao_Impl implements SettingsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AppSettingsEntity> __insertionAdapterOfAppSettingsEntity;

  private final RoomConverters __roomConverters = new RoomConverters();

  public SettingsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAppSettingsEntity = new EntityInsertionAdapter<AppSettingsEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `app_settings` (`id`,`fasterSync`,`fasterSyncMinutes`,`swipeAction`,`notificationsEnabled`,`notificationImportance`,`notificationSoundUri`,`vibrationEnabled`,`theme`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppSettingsEntity entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.getFasterSync() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindLong(3, entity.getFasterSyncMinutes());
        final String _tmp_1 = __roomConverters.swipe(entity.getSwipeAction());
        statement.bindString(4, _tmp_1);
        final int _tmp_2 = entity.getNotificationsEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp_2);
        statement.bindLong(6, entity.getNotificationImportance());
        if (entity.getNotificationSoundUri() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNotificationSoundUri());
        }
        final int _tmp_3 = entity.getVibrationEnabled() ? 1 : 0;
        statement.bindLong(8, _tmp_3);
        statement.bindString(9, entity.getTheme());
      }
    };
  }

  @Override
  public Object save(final AppSettingsEntity settings,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppSettingsEntity.insert(settings);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<AppSettingsEntity> observe() {
    final String _sql = "SELECT * FROM app_settings WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_settings"}, new Callable<AppSettingsEntity>() {
      @Override
      @Nullable
      public AppSettingsEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFasterSync = CursorUtil.getColumnIndexOrThrow(_cursor, "fasterSync");
          final int _cursorIndexOfFasterSyncMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "fasterSyncMinutes");
          final int _cursorIndexOfSwipeAction = CursorUtil.getColumnIndexOrThrow(_cursor, "swipeAction");
          final int _cursorIndexOfNotificationsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationsEnabled");
          final int _cursorIndexOfNotificationImportance = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationImportance");
          final int _cursorIndexOfNotificationSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationSoundUri");
          final int _cursorIndexOfVibrationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "vibrationEnabled");
          final int _cursorIndexOfTheme = CursorUtil.getColumnIndexOrThrow(_cursor, "theme");
          final AppSettingsEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final boolean _tmpFasterSync;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFasterSync);
            _tmpFasterSync = _tmp != 0;
            final int _tmpFasterSyncMinutes;
            _tmpFasterSyncMinutes = _cursor.getInt(_cursorIndexOfFasterSyncMinutes);
            final SwipeAction _tmpSwipeAction;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfSwipeAction);
            _tmpSwipeAction = __roomConverters.swipe(_tmp_1);
            final boolean _tmpNotificationsEnabled;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfNotificationsEnabled);
            _tmpNotificationsEnabled = _tmp_2 != 0;
            final int _tmpNotificationImportance;
            _tmpNotificationImportance = _cursor.getInt(_cursorIndexOfNotificationImportance);
            final String _tmpNotificationSoundUri;
            if (_cursor.isNull(_cursorIndexOfNotificationSoundUri)) {
              _tmpNotificationSoundUri = null;
            } else {
              _tmpNotificationSoundUri = _cursor.getString(_cursorIndexOfNotificationSoundUri);
            }
            final boolean _tmpVibrationEnabled;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfVibrationEnabled);
            _tmpVibrationEnabled = _tmp_3 != 0;
            final String _tmpTheme;
            _tmpTheme = _cursor.getString(_cursorIndexOfTheme);
            _result = new AppSettingsEntity(_tmpId,_tmpFasterSync,_tmpFasterSyncMinutes,_tmpSwipeAction,_tmpNotificationsEnabled,_tmpNotificationImportance,_tmpNotificationSoundUri,_tmpVibrationEnabled,_tmpTheme);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
