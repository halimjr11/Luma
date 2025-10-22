package com.halimjr11.luma.data.local.repository

import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.data.local.database.LumaDatabase
import com.halimjr11.luma.data.local.mapper.LocalDataMapper
import com.halimjr11.luma.data.local.preferences.SharedPreferenceHelper
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.domain.repository.LumaLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext

class LumaLocalRepositoryImpl(
    private val database: LumaDatabase,
    private val mapper: LocalDataMapper,
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val dispatcher: CoroutineDispatcherProvider
) : LumaLocalRepository {
    override suspend fun isLoggedIn(): Boolean = sharedPreferenceHelper.isLoggedIn()

    override suspend fun logout(): Boolean = withContext(dispatcher.io) {
        sharedPreferenceHelper.clearLogin()
        sharedPreferenceHelper.isLoggedIn()
    }

    override suspend fun checkFavorite(id: String): Boolean {
        return database.favoriteDao().isFavorite(id)
    }

    override suspend fun addFavorite(story: StoryDomain) {
        return database.favoriteDao().insertFavorite(mapper.mapFavDomainToEntity(story))
    }

    override suspend fun removeFavorite(story: StoryDomain) {
        return database.favoriteDao().deleteFavorite(mapper.mapFavDomainToEntity(story))
    }

    override fun getFavoriteStories(): Flow<List<StoryDomain>> {
        val movies = database.favoriteDao().getFavoriteStories()
        return movies.transform {
            val mapped = it.map { entity -> mapper.mapFavEntityToDomain(entity) }
            emit(mapped)
        }
    }
}