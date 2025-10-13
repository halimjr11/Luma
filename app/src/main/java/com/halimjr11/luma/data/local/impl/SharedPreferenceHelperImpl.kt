package com.halimjr11.luma.data.local.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.halimjr11.luma.data.local.SharedPreferenceHelper

class SharedPreferenceHelperImpl(private val pref: SharedPreferences) : SharedPreferenceHelper {

    override fun saveLoginResult(name: String, token: String, userId: String) {
        pref.edit(commit = true) {
            putString(KEY_NAME, name)
            putString(KEY_TOKEN, token)
            putString(KEY_USER_ID, userId)
        }
    }

    override fun getName(): String = pref.getString(KEY_NAME, "").orEmpty()

    override fun getToken(): String = pref.getString(KEY_TOKEN, "").orEmpty()

    override fun getUserId(): String = pref.getString(KEY_USER_ID, "").orEmpty()

    override fun isLoggedIn(): Boolean = getToken().isNotEmpty()

    override fun clearLogin() {
        pref.edit(commit = true) { clear() }
    }

    companion object {
        private const val KEY_NAME = "key_name"
        private const val KEY_TOKEN = "key_token"
        private const val KEY_USER_ID = "key_user_id"
    }
}
