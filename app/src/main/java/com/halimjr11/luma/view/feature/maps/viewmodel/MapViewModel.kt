package com.halimjr11.luma.view.feature.maps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.domain.usecase.GetMapStoryUseCase
import com.halimjr11.luma.utils.DomainResult
import com.halimjr11.luma.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel(
    private val getMapStoryUseCase: GetMapStoryUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {
    private val _mapStories: MutableStateFlow<UiState<List<StoryDomain>>> =
        MutableStateFlow(UiState.Loading)
    val mapStory: StateFlow<UiState<List<StoryDomain>>> = _mapStories

    val list: MutableList<StoryDomain> = mutableListOf()

    init {
        loadMapStories()
    }

    fun loadMapStories() = viewModelScope.launch(dispatcher.io) {
        _mapStories.value = when (val result = getMapStoryUseCase()) {
            is DomainResult.Success -> {
                list.addAll(result.data)
                UiState.Success(result.data)
            }

            is DomainResult.Error -> UiState.Error(result.message)
        }
    }
}