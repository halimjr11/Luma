package com.halimjr11.luma.domain.repository

import com.halimjr11.luma.domain.model.StoryDomain
import kotlinx.coroutines.flow.Flow

interface LumaLocalRepository {
    suspend fun isLoggedIn(): Boolean
    suspend fun logout(): Boolean
    suspend fun checkFavorite(id: String): Boolean
    suspend fun addFavorite(story: StoryDomain)
    suspend fun removeFavorite(story: StoryDomain)
    fun getFavoriteStories(): Flow<List<StoryDomain>>
}