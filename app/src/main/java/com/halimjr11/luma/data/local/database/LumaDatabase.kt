package com.halimjr11.luma.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.halimjr11.luma.data.local.model.FavoriteEntity
import com.halimjr11.luma.data.local.model.RemoteKeys
import com.halimjr11.luma.data.local.model.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeys::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LumaDatabase : RoomDatabase() {
    abstract fun lumaDao(): LumaDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}