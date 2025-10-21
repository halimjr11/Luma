package com.halimjr11.luma.data.remote.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.halimjr11.luma.domain.repository.LocationRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationRepositoryImpl(
    private val context: Context
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location? = suspendCoroutine { cont ->
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)

        fusedClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    cont.resume(location)
                } else {
                    // Request single update if null
                    val request = LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        1000L
                    ).setMaxUpdates(1).build()

                    val callback = object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            fusedClient.removeLocationUpdates(this)
                            cont.resume(result.lastLocation)
                        }
                    }
                    fusedClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
                }
            }.addOnFailureListener {
                cont.resume(null)
            }
    }
}
