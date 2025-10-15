package com.halimjr11.luma.view.feature.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.data.repository.LumaLocalRepository
import com.halimjr11.luma.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val repository: LumaLocalRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {
    private val _splashState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val splashState: StateFlow<UiState<Boolean>> = _splashState

    init {
        checkLogin()
    }

    fun checkLogin() = viewModelScope.launch(dispatcher.io) {
        _splashState.value = UiState.Success(repository.isLoggedIn())
    }

}