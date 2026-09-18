package com.threadly.felixx.dev.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

class RoomConverters {
    @androidx.room.TypeConverter fun status(value: String) = AccountStatus.valueOf(value)
    @androidx.room.TypeConverter fun status(value: AccountStatus) = value.name
    @androidx.room.TypeConverter fun matchMode(value: String) = ClubMatchMode.valueOf(value)
    @androidx.room.TypeConverter fun matchMode(value: ClubMatchMode) = value.name
    @androidx.room.TypeConverter fun swipe(value: String) = SwipeAction.valueOf(value)
    @androidx.room.TypeConverter fun swipe(value: SwipeAction) = value.name
}

@Database(
    entities = [AccountEntity::class, ClubEntity::class, ClubAccountCrossRef::class, ClubKeywordEntity::class, ClubContactEntity::class, ThreadEntity::class, MessageEntity::class, AttachmentEntity::class, AppSettingsEntity::class, SyncStateEntity::class],
    version = 4,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class ThreadlyDatabase : RoomDatabase() {
    abstract fun accounts(): AccountDao
    abstract fun clubs(): ClubDao
    abstract fun threads(): ThreadDao
    abstract fun messages(): MessageDao
    abstract fun settings(): SettingsDao
    abstract fun syncStates(): SyncStateDao

    companion object {
        fun create(context: Context): ThreadlyDatabase = Room.databaseBuilder(context, ThreadlyDatabase::class.java, "threadly.db").fallbackToDestructiveMigration().build()
    }
}
