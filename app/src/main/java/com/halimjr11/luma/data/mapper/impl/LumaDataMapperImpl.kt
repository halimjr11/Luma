package com.halimjr11.luma.data.mapper.impl

import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.data.mapper.LumaDataMapper
import com.halimjr11.luma.data.model.AuthResponse
import com.halimjr11.luma.data.model.StoryResponse
import com.halimjr11.luma.data.model.UploadStoryResponse
import com.halimjr11.luma.data.utils.orFalse
import com.halimjr11.luma.data.utils.orZero
import com.halimjr11.luma.domain.model.AuthDomain
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.domain.model.UploadStoryDomain
import kotlinx.coroutines.withContext

class LumaDataMapperImpl(private val dispatcher: CoroutineDispatcherProvider) : LumaDataMapper {
    override suspend fun mapAuth(
        response: AuthResponse
    ): AuthDomain = withContext(dispatcher.io) {
        AuthDomain(
            error = response.error.orFalse(),
            message = response.message.orEmpty(),
            loginResult = response.loginResult?.let {
                AuthDomain.LoginResult(
                    name = it.name.orEmpty(),
                    token = it.token.orEmpty(),
                    userId = it.userId.orEmpty()
                )
            } ?: AuthDomain.LoginResult()
        )
    }

    override suspend fun mapStory(
        response: StoryResponse
    ): StoryDomain = withContext(dispatcher.io) {
        StoryDomain(
            id = response.id.orEmpty(),
            name = response.name.orEmpty(),
            description = response.description.orEmpty(),
            photoUrl = response.photoUrl.orEmpty(),
            createdAt = response.createdAt.orEmpty(),
            lat = response.lat.orZero(),
            lon = response.lon.orZero()
        )
    }

    override suspend fun mapUploadStory(
        response: UploadStoryResponse
    ): UploadStoryDomain = withContext(dispatcher.io) {
        UploadStoryDomain(
            error = response.error.orFalse(),
            message = response.message.orEmpty()
        )
    }
}