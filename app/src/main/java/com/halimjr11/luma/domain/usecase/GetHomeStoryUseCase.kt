package com.halimjr11.luma.domain.usecase

import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.data.repository.LumaRemoteRepository
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.utils.Constants.FEATURED_SIZE
import com.halimjr11.luma.utils.Constants.PAGE_SIZE
import com.halimjr11.luma.utils.DomainResult
import kotlinx.coroutines.withContext

class GetHomeStoryUseCase(
    private val repository: LumaRemoteRepository,
    private val dispatcher: CoroutineDispatcherProvider
) {
    suspend operator fun invoke(): DomainResult<Pair<List<StoryDomain>, List<StoryDomain>>> =
        withContext(dispatcher.io) {
            val result = repository.getStories(PAGE_SIZE)
            when (result) {
                is DomainResult.Success -> {
                    val featured = result.data.take(FEATURED_SIZE)
                    val more = result.data.drop(FEATURED_SIZE)
                    DomainResult.Success(Pair(featured, more))
                }

                is DomainResult.Error -> DomainResult.Error(result.message, result.code)
            }
        }
}