package com.halimjr11.luma.domain.repository

import android.location.Location

interface LocationRepository {
    suspend fun getCurrentLocation(): Location?
}