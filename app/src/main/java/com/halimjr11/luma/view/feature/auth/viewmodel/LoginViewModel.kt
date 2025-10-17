package com.halimjr11.luma.view.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.data.repository.LumaRemoteRepository
import com.halimjr11.luma.utils.DomainResult
import com.halimjr11.luma.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LumaRemoteRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {
    private val _loginState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val loginState: StateFlow<UiState<Boolean>> = _loginState

    fun doLogin(email: String, password: String) = viewModelScope.launch(dispatcher.io) {
        _loginState.value = when (val state = repository.login(email, password)) {
            is DomainResult.Success -> UiState.Success(true)
            is DomainResult.Error -> UiState.Error(state.message)
        }
    }
}