package com.halimjr11.luma.data.repository

import com.halimjr11.luma.domain.model.AuthDomain
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.domain.model.UploadStoryDomain
import com.halimjr11.luma.utils.DomainResult
import okhttp3.RequestBody

interface LumaRepository {
    suspend fun login(email: String, password: String): DomainResult<AuthDomain>
    suspend fun register(name: String, email: String, password: String): DomainResult<AuthDomain>
    suspend fun getStories(): DomainResult<List<StoryDomain>>
    suspend fun uploadStory(
        fileName: String,
        description: String,
        fileBody: RequestBody,
        lat: Double?,
        lon: Double?
    ): DomainResult<UploadStoryDomain>

    suspend fun detailStory(id: String): DomainResult<StoryDomain>
    suspend fun logout(): Boolean
}