package com.halimjr11.luma.domain.repository

interface LumaLocalRepository {
    suspend fun isLoggedIn(): Boolean
    suspend fun logout(): Boolean
}