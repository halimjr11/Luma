package com.halimjr11.luma.view.feature.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halimjr11.luma.domain.repository.LumaLocalRepository
import com.halimjr11.luma.utils.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform

class FavoriteViewModel(private val localRepository: LumaLocalRepository) : ViewModel() {
    val favoriteStories = localRepository.getFavoriteStories().transform {
        if (it.isNotEmpty()) {
            emit(UiState.Success(it))
        } else {
            emit(UiState.Error(""))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )
}