package com.example.mypet.googlemaps.model

data class UserLocation(
    val latitude: Double,
    val longitude: Double
)

data class GooglePlaceLocation(
    val latitude: Double,
    val longitude: Double
)

sealed class UserLocationResultType {
    data class Success(
        val userLocation: UserLocation
    ) : UserLocationResultType()

    object GpsNotEnabled : UserLocationResultType()
    object Failure : UserLocationResultType()
}