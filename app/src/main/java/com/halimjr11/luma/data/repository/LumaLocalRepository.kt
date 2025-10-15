package com.halimjr11.luma.data.repository

interface LumaLocalRepository {
    suspend fun isLoggedIn(): Boolean
    suspend fun logout(): Boolean
}