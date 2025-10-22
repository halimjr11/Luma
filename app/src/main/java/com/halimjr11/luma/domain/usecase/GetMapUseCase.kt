package com.halimjr11.luma.domain.usecase

import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.domain.repository.LumaRemoteRepository
import com.halimjr11.luma.utils.Constants.PAGE_SIZE
import com.halimjr11.luma.utils.DomainResult
import kotlinx.coroutines.withContext

class GetMapStoryUseCase(
    private val repository: LumaRemoteRepository,
    private val dispatcher: CoroutineDispatcherProvider
) {
    suspend operator fun invoke(): DomainResult<List<StoryDomain>> =
        withContext(dispatcher.io) {
            val result = repository.getStories(PAGE_SIZE, 1, location = 1)
            when (result) {
                is DomainResult.Success -> {
                    DomainResult.Success(result.data)
                }

                is DomainResult.Error -> DomainResult.Error(result.message, result.code)
            }
        }
}