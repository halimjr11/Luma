package com.halimjr11.luma.domain.model

data class AuthDomain(
    val error: Boolean = false,
    val message: String = "",
    val loginResult: LoginResult = LoginResult()
) {
    data class LoginResult(
        val name: String = "",
        val token: String = "",
        val userId: String = ""
    )
}