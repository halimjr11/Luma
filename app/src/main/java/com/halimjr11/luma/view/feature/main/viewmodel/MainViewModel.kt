package com.halimjr11.luma.view.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.domain.usecase.GetHomeStoryUseCase
import com.halimjr11.luma.utils.DomainResult
import com.halimjr11.luma.utils.UiState
import com.halimjr11.luma.utils.UiState.Error
import com.halimjr11.luma.utils.UiState.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getHomeStoryUseCase: GetHomeStoryUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {
    private val _homeStories =
        MutableStateFlow<UiState<Pair<List<StoryDomain>, List<StoryDomain>>>>(UiState.Loading)
    val homeStories: StateFlow<UiState<Pair<List<StoryDomain>, List<StoryDomain>>>> =
        _homeStories.asStateFlow()

    init {
        loadHomeEvents()
    }

    fun loadHomeEvents() = viewModelScope.launch(dispatcher.io) {
        _homeStories.value = when (val result = getHomeStoryUseCase()) {
            is DomainResult.Success -> Success(result.data)
            is DomainResult.Error -> Error(result.message)
        }
    }
}