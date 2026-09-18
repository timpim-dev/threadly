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
        return "INSERT OR REPLACE INTO `app_settings` (`id`,`fasterSync`,`fasterSyncMinutes`,`swipeActionLeft`,`swipeActionRight`,`notificationsEnabled`,`notificationImportance`,`notificationSoundUri`,`vibrationEnabled`,`theme`,`dynamicColors`,`seedColor`,`fontFamily`,`messageCornerRadius`,`compactLayout`,`enterToSend`,`showAvatarsInThread`,`openRouterApiKey`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppSettingsEntity entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.getFasterSync() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindLong(3, entity.getFasterSyncMinutes());
        final String _tmp_1 = __roomConverters.swipe(entity.getSwipeActionLeft());
        statement.bindString(4, _tmp_1);
        final String _tmp_2 = __roomConverters.swipe(entity.getSwipeActionRight());
        statement.bindString(5, _tmp_2);
        final int _tmp_3 = entity.getNotificationsEnabled() ? 1 : 0;
        statement.bindLong(6, _tmp_3);
        statement.bindLong(7, entity.getNotificationImportance());
        if (entity.getNotificationSoundUri() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNotificationSoundUri());
        }
        final int _tmp_4 = entity.getVibrationEnabled() ? 1 : 0;
        statement.bindLong(9, _tmp_4);
        statement.bindString(10, entity.getTheme());
        final int _tmp_5 = entity.getDynamicColors() ? 1 : 0;
        statement.bindLong(11, _tmp_5);
        statement.bindLong(12, entity.getSeedColor());
        statement.bindString(13, entity.getFontFamily());
        statement.bindLong(14, entity.getMessageCornerRadius());
        final int _tmp_6 = entity.getCompactLayout() ? 1 : 0;
        statement.bindLong(15, _tmp_6);
        final int _tmp_7 = entity.getEnterToSend() ? 1 : 0;
        statement.bindLong(16, _tmp_7);
        final int _tmp_8 = entity.getShowAvatarsInThread() ? 1 : 0;
        statement.bindLong(17, _tmp_8);
        if (entity.getOpenRouterApiKey() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getOpenRouterApiKey());
        }
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
          final int _cursorIndexOfSwipeActionLeft = CursorUtil.getColumnIndexOrThrow(_cursor, "swipeActionLeft");
          final int _cursorIndexOfSwipeActionRight = CursorUtil.getColumnIndexOrThrow(_cursor, "swipeActionRight");
          final int _cursorIndexOfNotificationsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationsEnabled");
          final int _cursorIndexOfNotificationImportance = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationImportance");
          final int _cursorIndexOfNotificationSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationSoundUri");
          final int _cursorIndexOfVibrationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "vibrationEnabled");
          final int _cursorIndexOfTheme = CursorUtil.getColumnIndexOrThrow(_cursor, "theme");
          final int _cursorIndexOfDynamicColors = CursorUtil.getColumnIndexOrThrow(_cursor, "dynamicColors");
          final int _cursorIndexOfSeedColor = CursorUtil.getColumnIndexOrThrow(_cursor, "seedColor");
          final int _cursorIndexOfFontFamily = CursorUtil.getColumnIndexOrThrow(_cursor, "fontFamily");
          final int _cursorIndexOfMessageCornerRadius = CursorUtil.getColumnIndexOrThrow(_cursor, "messageCornerRadius");
          final int _cursorIndexOfCompactLayout = CursorUtil.getColumnIndexOrThrow(_cursor, "compactLayout");
          final int _cursorIndexOfEnterToSend = CursorUtil.getColumnIndexOrThrow(_cursor, "enterToSend");
          final int _cursorIndexOfShowAvatarsInThread = CursorUtil.getColumnIndexOrThrow(_cursor, "showAvatarsInThread");
          final int _cursorIndexOfOpenRouterApiKey = CursorUtil.getColumnIndexOrThrow(_cursor, "openRouterApiKey");
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
            final SwipeAction _tmpSwipeActionLeft;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfSwipeActionLeft);
            _tmpSwipeActionLeft = __roomConverters.swipe(_tmp_1);
            final SwipeAction _tmpSwipeActionRight;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfSwipeActionRight);
            _tmpSwipeActionRight = __roomConverters.swipe(_tmp_2);
            final boolean _tmpNotificationsEnabled;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfNotificationsEnabled);
            _tmpNotificationsEnabled = _tmp_3 != 0;
            final int _tmpNotificationImportance;
            _tmpNotificationImportance = _cursor.getInt(_cursorIndexOfNotificationImportance);
            final String _tmpNotificationSoundUri;
            if (_cursor.isNull(_cursorIndexOfNotificationSoundUri)) {
              _tmpNotificationSoundUri = null;
            } else {
              _tmpNotificationSoundUri = _cursor.getString(_cursorIndexOfNotificationSoundUri);
            }
            final boolean _tmpVibrationEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfVibrationEnabled);
            _tmpVibrationEnabled = _tmp_4 != 0;
            final String _tmpTheme;
            _tmpTheme = _cursor.getString(_cursorIndexOfTheme);
            final boolean _tmpDynamicColors;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfDynamicColors);
            _tmpDynamicColors = _tmp_5 != 0;
            final long _tmpSeedColor;
            _tmpSeedColor = _cursor.getLong(_cursorIndexOfSeedColor);
            final String _tmpFontFamily;
            _tmpFontFamily = _cursor.getString(_cursorIndexOfFontFamily);
            final int _tmpMessageCornerRadius;
            _tmpMessageCornerRadius = _cursor.getInt(_cursorIndexOfMessageCornerRadius);
            final boolean _tmpCompactLayout;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfCompactLayout);
            _tmpCompactLayout = _tmp_6 != 0;
            final boolean _tmpEnterToSend;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfEnterToSend);
            _tmpEnterToSend = _tmp_7 != 0;
            final boolean _tmpShowAvatarsInThread;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfShowAvatarsInThread);
            _tmpShowAvatarsInThread = _tmp_8 != 0;
            final String _tmpOpenRouterApiKey;
            if (_cursor.isNull(_cursorIndexOfOpenRouterApiKey)) {
              _tmpOpenRouterApiKey = null;
            } else {
              _tmpOpenRouterApiKey = _cursor.getString(_cursorIndexOfOpenRouterApiKey);
            }
            _result = new AppSettingsEntity(_tmpId,_tmpFasterSync,_tmpFasterSyncMinutes,_tmpSwipeActionLeft,_tmpSwipeActionRight,_tmpNotificationsEnabled,_tmpNotificationImportance,_tmpNotificationSoundUri,_tmpVibrationEnabled,_tmpTheme,_tmpDynamicColors,_tmpSeedColor,_tmpFontFamily,_tmpMessageCornerRadius,_tmpCompactLayout,_tmpEnterToSend,_tmpShowAvatarsInThread,_tmpOpenRouterApiKey);
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
  public Object getSettings(final Continuation<? super AppSettingsEntity> $completion) {
    final String _sql = "SELECT * FROM app_settings WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AppSettingsEntity>() {
      @Override
      @Nullable
      public AppSettingsEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFasterSync = CursorUtil.getColumnIndexOrThrow(_cursor, "fasterSync");
          final int _cursorIndexOfFasterSyncMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "fasterSyncMinutes");
          final int _cursorIndexOfSwipeActionLeft = CursorUtil.getColumnIndexOrThrow(_cursor, "swipeActionLeft");
          final int _cursorIndexOfSwipeActionRight = CursorUtil.getColumnIndexOrThrow(_cursor, "swipeActionRight");
          final int _cursorIndexOfNotificationsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationsEnabled");
          final int _cursorIndexOfNotificationImportance = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationImportance");
          final int _cursorIndexOfNotificationSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationSoundUri");
          final int _cursorIndexOfVibrationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "vibrationEnabled");
          final int _cursorIndexOfTheme = CursorUtil.getColumnIndexOrThrow(_cursor, "theme");
          final int _cursorIndexOfDynamicColors = CursorUtil.getColumnIndexOrThrow(_cursor, "dynamicColors");
          final int _cursorIndexOfSeedColor = CursorUtil.getColumnIndexOrThrow(_cursor, "seedColor");
          final int _cursorIndexOfFontFamily = CursorUtil.getColumnIndexOrThrow(_cursor, "fontFamily");
          final int _cursorIndexOfMessageCornerRadius = CursorUtil.getColumnIndexOrThrow(_cursor, "messageCornerRadius");
          final int _cursorIndexOfCompactLayout = CursorUtil.getColumnIndexOrThrow(_cursor, "compactLayout");
          final int _cursorIndexOfEnterToSend = CursorUtil.getColumnIndexOrThrow(_cursor, "enterToSend");
          final int _cursorIndexOfShowAvatarsInThread = CursorUtil.getColumnIndexOrThrow(_cursor, "showAvatarsInThread");
          final int _cursorIndexOfOpenRouterApiKey = CursorUtil.getColumnIndexOrThrow(_cursor, "openRouterApiKey");
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
            final SwipeAction _tmpSwipeActionLeft;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfSwipeActionLeft);
            _tmpSwipeActionLeft = __roomConverters.swipe(_tmp_1);
            final SwipeAction _tmpSwipeActionRight;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfSwipeActionRight);
            _tmpSwipeActionRight = __roomConverters.swipe(_tmp_2);
            final boolean _tmpNotificationsEnabled;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfNotificationsEnabled);
            _tmpNotificationsEnabled = _tmp_3 != 0;
            final int _tmpNotificationImportance;
            _tmpNotificationImportance = _cursor.getInt(_cursorIndexOfNotificationImportance);
            final String _tmpNotificationSoundUri;
            if (_cursor.isNull(_cursorIndexOfNotificationSoundUri)) {
              _tmpNotificationSoundUri = null;
            } else {
              _tmpNotificationSoundUri = _cursor.getString(_cursorIndexOfNotificationSoundUri);
            }
            final boolean _tmpVibrationEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfVibrationEnabled);
            _tmpVibrationEnabled = _tmp_4 != 0;
            final String _tmpTheme;
            _tmpTheme = _cursor.getString(_cursorIndexOfTheme);
            final boolean _tmpDynamicColors;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfDynamicColors);
            _tmpDynamicColors = _tmp_5 != 0;
            final long _tmpSeedColor;
            _tmpSeedColor = _cursor.getLong(_cursorIndexOfSeedColor);
            final String _tmpFontFamily;
            _tmpFontFamily = _cursor.getString(_cursorIndexOfFontFamily);
            final int _tmpMessageCornerRadius;
            _tmpMessageCornerRadius = _cursor.getInt(_cursorIndexOfMessageCornerRadius);
            final boolean _tmpCompactLayout;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfCompactLayout);
            _tmpCompactLayout = _tmp_6 != 0;
            final boolean _tmpEnterToSend;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfEnterToSend);
            _tmpEnterToSend = _tmp_7 != 0;
            final boolean _tmpShowAvatarsInThread;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfShowAvatarsInThread);
            _tmpShowAvatarsInThread = _tmp_8 != 0;
            final String _tmpOpenRouterApiKey;
            if (_cursor.isNull(_cursorIndexOfOpenRouterApiKey)) {
              _tmpOpenRouterApiKey = null;
            } else {
              _tmpOpenRouterApiKey = _cursor.getString(_cursorIndexOfOpenRouterApiKey);
            }
            _result = new AppSettingsEntity(_tmpId,_tmpFasterSync,_tmpFasterSyncMinutes,_tmpSwipeActionLeft,_tmpSwipeActionRight,_tmpNotificationsEnabled,_tmpNotificationImportance,_tmpNotificationSoundUri,_tmpVibrationEnabled,_tmpTheme,_tmpDynamicColors,_tmpSeedColor,_tmpFontFamily,_tmpMessageCornerRadius,_tmpCompactLayout,_tmpEnterToSend,_tmpShowAvatarsInThread,_tmpOpenRouterApiKey);
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
