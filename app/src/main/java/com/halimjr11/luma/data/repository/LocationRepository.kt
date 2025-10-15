package com.halimjr11.luma.data.repository

import android.location.Location

interface LocationRepository {
    suspend fun getCurrentLocation(): Location?
}