package com.halimjr11.luma.data.repository.impl

import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.data.local.SharedPreferenceHelper
import com.halimjr11.luma.data.repository.LumaLocalRepository
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