package com.halimjr11.luma.view.feature.create.viewmodel

import android.location.Location
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.data.repository.LocationRepository
import com.halimjr11.luma.data.repository.LumaRemoteRepository
import com.halimjr11.luma.utils.Constants.UNKNOWN_ERROR
import com.halimjr11.luma.utils.DomainResult
import com.halimjr11.luma.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody

class CreateViewModel(
    private val repository: LumaRemoteRepository,
    private val locationRepository: LocationRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {
    private val _createState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val createState: StateFlow<UiState<Boolean>> = _createState
    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    fun handleImageAndPost(
        uri: Uri,
        description: String,
        lat: Double?,
        lon: Double?
    ) = viewModelScope.launch(dispatcher.io) {
        val file = repository.compressAndSave(uri)
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val result = repository.uploadStory(
            fileName = file.name,
            fileBody = requestBody,
            description = description,
            lat = lat,
            lon = lon
        )

        _createState.value = when (result) {
            is DomainResult.Success -> UiState.Success(!result.data.error)
            else -> UiState.Error(UNKNOWN_ERROR)
        }
    }

    fun fetchLocation() = viewModelScope.launch(dispatcher.io) {
        val result = locationRepository.getCurrentLocation()
        _location.value = result
    }
}