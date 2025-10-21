package com.halimjr11.luma.data.remote.mapper

import com.halimjr11.luma.data.remote.model.AuthResponse
import com.halimjr11.luma.data.remote.model.StoryResponse
import com.halimjr11.luma.data.remote.model.UploadStoryResponse
import com.halimjr11.luma.domain.model.AuthDomain
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.domain.model.UploadStoryDomain

interface RemoteDataMapper {
    suspend fun mapAuth(response: AuthResponse): AuthDomain
    suspend fun mapStory(response: StoryResponse): StoryDomain
    suspend fun mapUploadStory(response: UploadStoryResponse): UploadStoryDomain
}