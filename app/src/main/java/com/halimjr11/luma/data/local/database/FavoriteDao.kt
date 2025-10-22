package com.halimjr11.luma.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.halimjr11.luma.data.local.model.FavoriteEntity
import com.halimjr11.luma.utils.Constants.ENTITY_FAVORITE
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(story: FavoriteEntity)

    @Query("SELECT * FROM $ENTITY_FAVORITE")
    fun getFavoriteStories(): Flow<List<FavoriteEntity>>

    @Delete
    suspend fun deleteFavorite(story: FavoriteEntity)

    @Query("SELECT EXISTS(SELECT * FROM $ENTITY_FAVORITE WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean
}
