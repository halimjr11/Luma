package com.halimjr11.luma.data.remote.model


import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("error")
    val error: Boolean? = null,
    @SerializedName("loginResult")
    val loginResult: LoginResult? = null,
    @SerializedName("message")
    val message: String? = null
) {
    data class LoginResult(
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("token")
        val token: String? = null,
        @SerializedName("userId")
        val userId: String? = null
    )
}