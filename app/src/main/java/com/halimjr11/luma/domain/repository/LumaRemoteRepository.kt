package com.halimjr11.luma.domain.repository

import android.net.Uri
import com.halimjr11.luma.domain.model.AuthDomain
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.domain.model.UploadStoryDomain
import com.halimjr11.luma.utils.DomainResult
import okhttp3.RequestBody
import java.io.File

interface LumaRemoteRepository {
    suspend fun login(email: String, password: String): DomainResult<AuthDomain>
    suspend fun register(name: String, email: String, password: String): DomainResult<AuthDomain>
    suspend fun getStories(
        size: Int,
        page: Int,
        location: Int? = null
    ): DomainResult<List<StoryDomain>>

    suspend fun uploadStory(
        fileName: String,
        description: String,
        fileBody: RequestBody,
        lat: Double?,
        lon: Double?
    ): DomainResult<UploadStoryDomain>

    suspend fun detailStory(id: String): DomainResult<StoryDomain>
    suspend fun compressAndSave(uri: Uri): File
}