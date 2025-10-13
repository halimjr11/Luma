package com.halimjr11.luma.data.interceptor

import com.halimjr11.luma.data.local.SharedPreferenceHelper
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sharedPreferenceHelper: SharedPreferenceHelper) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val modifiedRequest = request.newBuilder().apply {
            if (request.url.encodedPath !in listOf("/login", "/register")) {
                addHeader("Authorization", "Bearer ${sharedPreferenceHelper.getToken()}")
            }
        }.build()
        return chain.proceed(modifiedRequest)
    }
}