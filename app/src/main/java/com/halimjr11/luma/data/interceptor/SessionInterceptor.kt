package com.halimjr11.luma.data.interceptor

import android.content.Context
import android.content.Intent
import com.halimjr11.luma.data.local.SharedPreferenceHelper
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
            context.startActivity(Intent(context, AuthActivity::class.java))
            return response
        }

        return response
    }
}
