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
public final class MessageDao_Impl implements MessageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MessageEntity> __insertionAdapterOfMessageEntity;

  public MessageDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMessageEntity = new EntityInsertionAdapter<MessageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `messages` (`id`,`threadId`,`providerMessageId`,`senderName`,`senderEmail`,`recipients`,`cc`,`body`,`sentAt`,`isFromUser`,`isRead`,`inReplyTo`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getThreadId());
        statement.bindString(3, entity.getProviderMessageId());
        statement.bindString(4, entity.getSenderName());
        statement.bindString(5, entity.getSenderEmail());
        statement.bindString(6, entity.getRecipients());
        statement.bindString(7, entity.getCc());
        statement.bindString(8, entity.getBody());
        statement.bindLong(9, entity.getSentAt());
        final int _tmp = entity.isFromUser() ? 1 : 0;
        statement.bindLong(10, _tmp);
        final int _tmp_1 = entity.isRead() ? 1 : 0;
        statement.bindLong(11, _tmp_1);
        if (entity.getInReplyTo() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getInReplyTo());
        }
      }
    };
  }

  @Override
  public Object insertAll(final List<MessageEntity> messages,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMessageEntity.insert(messages);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MessageEntity>> observeForThread(final String threadId) {
    final String _sql = "SELECT * FROM messages WHERE threadId = ? ORDER BY sentAt";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, threadId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"messages"}, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfThreadId = CursorUtil.getColumnIndexOrThrow(_cursor, "threadId");
          final int _cursorIndexOfProviderMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "providerMessageId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "senderEmail");
          final int _cursorIndexOfRecipients = CursorUtil.getColumnIndexOrThrow(_cursor, "recipients");
          final int _cursorIndexOfCc = CursorUtil.getColumnIndexOrThrow(_cursor, "cc");
          final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final int _cursorIndexOfIsFromUser = CursorUtil.getColumnIndexOrThrow(_cursor, "isFromUser");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfInReplyTo = CursorUtil.getColumnIndexOrThrow(_cursor, "inReplyTo");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpThreadId;
            _tmpThreadId = _cursor.getString(_cursorIndexOfThreadId);
            final String _tmpProviderMessageId;
            _tmpProviderMessageId = _cursor.getString(_cursorIndexOfProviderMessageId);
            final String _tmpSenderName;
            _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            final String _tmpSenderEmail;
            _tmpSenderEmail = _cursor.getString(_cursorIndexOfSenderEmail);
            final String _tmpRecipients;
            _tmpRecipients = _cursor.getString(_cursorIndexOfRecipients);
            final String _tmpCc;
            _tmpCc = _cursor.getString(_cursorIndexOfCc);
            final String _tmpBody;
            _tmpBody = _cursor.getString(_cursorIndexOfBody);
            final long _tmpSentAt;
            _tmpSentAt = _cursor.getLong(_cursorIndexOfSentAt);
            final boolean _tmpIsFromUser;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFromUser);
            _tmpIsFromUser = _tmp != 0;
            final boolean _tmpIsRead;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRead);
            _tmpIsRead = _tmp_1 != 0;
            final String _tmpInReplyTo;
            if (_cursor.isNull(_cursorIndexOfInReplyTo)) {
              _tmpInReplyTo = null;
            } else {
              _tmpInReplyTo = _cursor.getString(_cursorIndexOfInReplyTo);
            }
            _item = new MessageEntity(_tmpId,_tmpThreadId,_tmpProviderMessageId,_tmpSenderName,_tmpSenderEmail,_tmpRecipients,_tmpCc,_tmpBody,_tmpSentAt,_tmpIsFromUser,_tmpIsRead,_tmpInReplyTo);
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
  public Object findByProviderId(final String providerId,
      final Continuation<? super MessageEntity> $completion) {
    final String _sql = "SELECT * FROM messages WHERE providerMessageId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, providerId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MessageEntity>() {
      @Override
      @Nullable
      public MessageEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfThreadId = CursorUtil.getColumnIndexOrThrow(_cursor, "threadId");
          final int _cursorIndexOfProviderMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "providerMessageId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "senderEmail");
          final int _cursorIndexOfRecipients = CursorUtil.getColumnIndexOrThrow(_cursor, "recipients");
          final int _cursorIndexOfCc = CursorUtil.getColumnIndexOrThrow(_cursor, "cc");
          final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final int _cursorIndexOfIsFromUser = CursorUtil.getColumnIndexOrThrow(_cursor, "isFromUser");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfInReplyTo = CursorUtil.getColumnIndexOrThrow(_cursor, "inReplyTo");
          final MessageEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpThreadId;
            _tmpThreadId = _cursor.getString(_cursorIndexOfThreadId);
            final String _tmpProviderMessageId;
            _tmpProviderMessageId = _cursor.getString(_cursorIndexOfProviderMessageId);
            final String _tmpSenderName;
            _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            final String _tmpSenderEmail;
            _tmpSenderEmail = _cursor.getString(_cursorIndexOfSenderEmail);
            final String _tmpRecipients;
            _tmpRecipients = _cursor.getString(_cursorIndexOfRecipients);
            final String _tmpCc;
            _tmpCc = _cursor.getString(_cursorIndexOfCc);
            final String _tmpBody;
            _tmpBody = _cursor.getString(_cursorIndexOfBody);
            final long _tmpSentAt;
            _tmpSentAt = _cursor.getLong(_cursorIndexOfSentAt);
            final boolean _tmpIsFromUser;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFromUser);
            _tmpIsFromUser = _tmp != 0;
            final boolean _tmpIsRead;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRead);
            _tmpIsRead = _tmp_1 != 0;
            final String _tmpInReplyTo;
            if (_cursor.isNull(_cursorIndexOfInReplyTo)) {
              _tmpInReplyTo = null;
            } else {
              _tmpInReplyTo = _cursor.getString(_cursorIndexOfInReplyTo);
            }
            _result = new MessageEntity(_tmpId,_tmpThreadId,_tmpProviderMessageId,_tmpSenderName,_tmpSenderEmail,_tmpRecipients,_tmpCc,_tmpBody,_tmpSentAt,_tmpIsFromUser,_tmpIsRead,_tmpInReplyTo);
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
