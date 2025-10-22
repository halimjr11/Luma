package com.halimjr11.luma.view.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import com.halimjr11.luma.domain.repository.LumaPagingRepository
import kotlinx.coroutines.runBlocking

class MainViewModel(
    private val repository: LumaPagingRepository
) : ViewModel() {
    val homePaging = runBlocking { repository.getPagingStories() }
}