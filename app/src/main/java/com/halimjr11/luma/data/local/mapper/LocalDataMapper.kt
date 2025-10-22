package com.halimjr11.luma.data.local.mapper

import com.halimjr11.luma.data.local.model.FavoriteEntity
import com.halimjr11.luma.data.local.model.StoryEntity
import com.halimjr11.luma.data.remote.model.StoryResponse
import com.halimjr11.luma.domain.model.StoryDomain

interface LocalDataMapper {
    suspend fun mapEntityToDomain(entity: StoryEntity): StoryDomain
    suspend fun mapFavEntityToDomain(entity: FavoriteEntity): StoryDomain
    suspend fun mapDomainToEntity(domain: StoryDomain): StoryEntity
    suspend fun mapFavDomainToEntity(domain: StoryDomain): FavoriteEntity
    suspend fun mapResponseToEntity(response: StoryResponse): StoryEntity

}