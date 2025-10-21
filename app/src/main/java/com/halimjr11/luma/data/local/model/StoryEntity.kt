package com.halimjr11.luma.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.halimjr11.luma.utils.Constants.ENTITY_NAME

@Entity(tableName = ENTITY_NAME)
data class StoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "photo_url")
    val photoUrl: String,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "latitude")
    val lat: Double? = null,

    @ColumnInfo(name = "longitude")
    val lon: Double? = null
)