package com.example.mypet.googlemaps.data

import android.annotation.SuppressLint
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.example.mypet.googlemaps.model.GooglePlaceDetailsGenericResponse
import com.example.mypet.googlemaps.model.GooglePlacesResponse
import com.example.mypet.googlemaps.model.UserLocation
import com.example.mypet.googlemaps.util.requireNotNull
import com.example.mypet.googlemaps.util.suspended
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface MapSearcherDataSource {
    suspend fun getGooglePlaces(
        location: String,
        query: String
    ): GooglePlacesResponse

    suspend fun getUserLocation(): UserLocation
    fun isLocationServiceEnabled(): Boolean

    suspend fun getGooglePlaceDetails(
        placeId: String,
        fields: String
    ): GooglePlaceDetailsGenericResponse
}

class MapSearcherDataSourceImpl @Inject constructor(
    private val api: GooglePlacesApi,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationManager: LocationManager,
): MapSearcherDataSource {
    override suspend fun getGooglePlaces(
        location: String,
        query: String
    ): GooglePlacesResponse {
        return withContext(Dispatchers.IO) {
            api.getGooglePlaces(location, query).requireNotNull()
        }
    }

    override suspend fun getGooglePlaceDetails(
        placeId: String,
        fields: String
    ): GooglePlaceDetailsGenericResponse {
        return withContext(Dispatchers.IO) {
            api.getGooglePlaceDetails(placeId, fields, "el").requireNotNull()
        }
    }

    override fun isLocationServiceEnabled(): Boolean = LocationManagerCompat.isLocationEnabled(locationManager)

    @SuppressLint("MissingPermission")
    override suspend fun getUserLocation(): UserLocation {
        val location = fusedLocationProviderClient.lastLocation.suspended()
        return UserLocation(
            location.latitude,
            location.longitude
        )
    }
}

