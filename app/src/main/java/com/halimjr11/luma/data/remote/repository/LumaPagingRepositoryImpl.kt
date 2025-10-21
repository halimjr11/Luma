package com.halimjr11.luma.data.remote.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.halimjr11.luma.data.local.database.LumaDatabase
import com.halimjr11.luma.data.local.mapper.LocalDataMapper
import com.halimjr11.luma.data.paging.LumaRemoteMediator
import com.halimjr11.luma.data.remote.mapper.RemoteDataMapper
import com.halimjr11.luma.data.remote.service.LumaService
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.domain.repository.LumaPagingRepository
import com.halimjr11.luma.utils.Constants.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LumaPagingRepositoryImpl(
    private val mapper: RemoteDataMapper,
    private val localDataMapper: LocalDataMapper,
    private val database: LumaDatabase,
    private val lumaService: LumaService
) : LumaPagingRepository {
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPagingStories(): Flow<PagingData<StoryDomain>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE
            ),
            remoteMediator = LumaRemoteMediator(database, lumaService, localDataMapper),
            pagingSourceFactory = {
                database.lumaDao().getAllQuote()
            }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                localDataMapper.mapEntityToDomain(entity)
            }
        }
    }
}