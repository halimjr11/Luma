package com.halimjr11.luma.data.remote.model

data class AuthRequest(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null
)