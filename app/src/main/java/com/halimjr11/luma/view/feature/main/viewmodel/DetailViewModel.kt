package com.halimjr11.luma.view.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.domain.repository.LumaLocalRepository
import com.halimjr11.luma.domain.repository.LumaRemoteRepository
import com.halimjr11.luma.utils.Constants.UNKNOWN_ERROR
import com.halimjr11.luma.utils.DomainResult
import com.halimjr11.luma.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: LumaRemoteRepository,
    private val localRepository: LumaLocalRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {
    private val _detailStory =
        MutableStateFlow<UiState<StoryDomain>>(UiState.Loading)
    val detailStory: StateFlow<UiState<StoryDomain>> = _detailStory.asStateFlow()

    private val isFavorite = MutableStateFlow(false)
    val isFavoriteFlow: StateFlow<Boolean> = isFavorite.asStateFlow()


    fun loadDetailStory(id: String) = viewModelScope.launch(dispatcher.io) {
        checkFavorite(id)
        _detailStory.value = when (val result = repository.detailStory(id)) {
            is DomainResult.Success -> UiState.Success(result.data)
            else -> UiState.Error(UNKNOWN_ERROR)
        }
    }

    fun checkFavorite(id: String) = viewModelScope.launch(dispatcher.io) {
        isFavorite.value = localRepository.checkFavorite(id)
    }

    fun toggleFavorite(story: StoryDomain) = viewModelScope.launch(dispatcher.io) {
        if (isFavorite.value) {
            localRepository.removeFavorite(story)
        } else {
            localRepository.addFavorite(story)
        }
        isFavorite.value = !isFavorite.value
    }
}