package com.halimjr11.luma.data.local.preferences

interface SharedPreferenceHelper {
    fun saveLoginResult(name: String, token: String, userId: String)
    fun getName(): String
    fun getToken(): String
    fun getUserId(): String
    fun isLoggedIn(): Boolean
    fun clearLogin()
}