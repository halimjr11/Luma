package com.halimjr11.luma.data.repository.impl

import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.data.local.SharedPreferenceHelper
import com.halimjr11.luma.data.mapper.LumaDataMapper
import com.halimjr11.luma.data.model.AuthRequest
import com.halimjr11.luma.data.model.StoryResponse
import com.halimjr11.luma.data.repository.LumaRepository
import com.halimjr11.luma.data.service.LumaService
import com.halimjr11.luma.data.utils.safeApiCall
import com.halimjr11.luma.domain.model.AuthDomain
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.domain.model.UploadStoryDomain
import com.halimjr11.luma.utils.DomainResult
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class LumaRepositoryImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val mapper: LumaDataMapper,
    private val lumaService: LumaService,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : LumaRepository {
    override suspend fun login(
        email: String,
        password: String
    ): DomainResult<AuthDomain> = safeApiCall {
        val result = lumaService.login(AuthRequest(email, password))
        mapper.mapAuth(result)
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): DomainResult<AuthDomain> = safeApiCall {
        val result = lumaService.register(AuthRequest(name, email, password))
        mapper.mapAuth(result)
    }

    override suspend fun getStories(): DomainResult<List<StoryDomain>> = safeApiCall {
        val result = lumaService.getStories()
        result.data?.mapNotNull {
            it?.let { response -> mapper.mapStory(response) }
        }.orEmpty()
    }

    override suspend fun uploadStory(
        fileName: String,
        description: String,
        fileBody: RequestBody,
        lat: Double?,
        lon: Double?
    ): DomainResult<UploadStoryDomain> = safeApiCall {
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("description", description)
            .addFormDataPart("file", fileName, fileBody)
        if (lat != null) {
            builder.addFormDataPart("lat", lat.toString())
        }
        if (lon != null) {
            builder.addFormDataPart("lon", lon.toString())
        }
        val requestBody = builder.build()

        val result = lumaService.uploadStory(requestBody)
        mapper.mapUploadStory(result)
    }

    override suspend fun detailStory(id: String): DomainResult<StoryDomain> = safeApiCall {
        val result = lumaService.getDetailStories(id)
        mapper.mapStory(result.data ?: StoryResponse())
    }

    override suspend fun logout(): Boolean = withContext(dispatcher.io) {
        sharedPreferenceHelper.clearLogin()
        sharedPreferenceHelper.isLoggedIn()
    }
}