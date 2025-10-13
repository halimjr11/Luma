package com.halimjr11.luma.data.mapper

import com.halimjr11.luma.data.model.AuthResponse
import com.halimjr11.luma.data.model.StoryResponse
import com.halimjr11.luma.data.model.UploadStoryResponse
import com.halimjr11.luma.domain.model.AuthDomain
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.domain.model.UploadStoryDomain

interface LumaDataMapper {
    suspend fun mapAuth(response: AuthResponse): AuthDomain
    suspend fun mapStory(response: StoryResponse): StoryDomain
    suspend fun mapUploadStory(response: UploadStoryResponse): UploadStoryDomain
}