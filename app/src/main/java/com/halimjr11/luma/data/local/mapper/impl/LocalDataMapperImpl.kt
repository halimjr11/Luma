package com.halimjr11.luma.data.local.mapper.impl

import com.halimjr11.luma.data.local.mapper.LocalDataMapper
import com.halimjr11.luma.data.local.model.StoryEntity
import com.halimjr11.luma.data.remote.model.StoryResponse
import com.halimjr11.luma.data.utils.orZero
import com.halimjr11.luma.domain.model.StoryDomain


class LocalDataMapperImpl : LocalDataMapper {
    override suspend fun mapEntityToDomain(entity: StoryEntity): StoryDomain {
        val domain = StoryDomain(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            photoUrl = entity.photoUrl,
            createdAt = entity.createdAt,
            lat = entity.lat.orZero(),
            lon = entity.lon.orZero()
        )
        return domain
    }

    override suspend fun mapDomainToEntity(domain: StoryDomain): StoryEntity {
        val entity = StoryEntity(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            photoUrl = domain.photoUrl,
            createdAt = domain.createdAt,
            lat = domain.lat,
            lon = domain.lon
        )
        return entity
    }

    override suspend fun mapResponseToEntity(response: StoryResponse): StoryEntity {
        val entity = StoryEntity(
            id = response.id.orEmpty(),
            name = response.name.orEmpty(),
            description = response.description.orEmpty(),
            photoUrl = response.photoUrl.orEmpty(),
            createdAt = response.createdAt.orEmpty(),
            lat = response.lat.orZero(),
            lon = response.lon.orZero()
        )
        return entity
    }
}