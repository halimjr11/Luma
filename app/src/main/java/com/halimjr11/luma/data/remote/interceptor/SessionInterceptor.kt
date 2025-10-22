package com.halimjr11.luma.data.remote.interceptor

import android.content.Context
import android.content.Intent
import com.halimjr11.luma.data.local.preferences.SharedPreferenceHelper
import com.halimjr11.luma.utils.Constants.UNAUTHORIZED_CODE
import com.halimjr11.luma.view.feature.auth.AuthActivity
import okhttp3.Interceptor
import okhttp3.Response

class SessionInterceptor(
    private val context: Context,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val response = chain.proceed(request)

        if (response.code == UNAUTHORIZED_CODE) {
            sharedPreferenceHelper.clearLogin()
            val intent = Intent(context, AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
            return response
        }

        return response
    }
}
