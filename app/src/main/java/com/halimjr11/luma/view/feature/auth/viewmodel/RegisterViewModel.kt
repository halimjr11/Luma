package com.halimjr11.luma.view.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.data.repository.LumaRemoteRepository
import com.halimjr11.luma.utils.Constants.UNKNOWN_ERROR
import com.halimjr11.luma.utils.DomainResult
import com.halimjr11.luma.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: LumaRemoteRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {
    private val _registerState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val registerState: StateFlow<UiState<Boolean>> = _registerState

    fun registerUser(
        name: String,
        email: String,
        password: String
    ) = viewModelScope.launch(dispatcher.io) {
        _registerState.value = when (repository.register(name, email, password)) {
            is DomainResult.Success -> UiState.Success(true)
            else -> UiState.Error(UNKNOWN_ERROR)
        }
    }
}