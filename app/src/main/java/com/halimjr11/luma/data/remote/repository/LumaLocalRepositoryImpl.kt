package com.halimjr11.luma.data.remote.repository

import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.data.local.preferences.SharedPreferenceHelper
import com.halimjr11.luma.domain.repository.LumaLocalRepository
import kotlinx.coroutines.withContext

class LumaLocalRepositoryImpl(
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val dispatcher: CoroutineDispatcherProvider
) : LumaLocalRepository {
    override suspend fun isLoggedIn(): Boolean = sharedPreferenceHelper.isLoggedIn()

    override suspend fun logout(): Boolean = withContext(dispatcher.io) {
        sharedPreferenceHelper.clearLogin()
        sharedPreferenceHelper.isLoggedIn()
    }
}