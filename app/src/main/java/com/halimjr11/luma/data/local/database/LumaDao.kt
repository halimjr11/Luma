package com.halimjr11.luma.data.local.database

import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.halimjr11.luma.data.local.model.StoryEntity
import com.halimjr11.luma.utils.Constants.ENTITY_NAME

interface LumaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: List<StoryEntity>)

    @Query("SELECT * FROM $ENTITY_NAME")
    fun getAllQuote(): PagingSource<Int, StoryEntity>

    @Query("SELECT * FROM $ENTITY_NAME WHERE id = :id LIMIT 1")
    suspend fun getStoryById(id: String): StoryEntity?

    @Query("DELETE FROM $ENTITY_NAME")
    suspend fun deleteAll()
}