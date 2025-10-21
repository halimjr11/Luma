package com.halimjr11.luma.domain.repository

import androidx.paging.PagingData
import com.halimjr11.luma.domain.model.StoryDomain
import kotlinx.coroutines.flow.Flow

interface LumaPagingRepository {
    suspend fun getPagingStories(): Flow<PagingData<StoryDomain>>
}