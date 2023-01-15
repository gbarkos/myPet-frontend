package com.example.mypet.googlemaps.data

import com.example.mypet.googlemaps.model.GooglePlaces
import com.example.mypet.googlemaps.model.PlaceDetailsItem
import com.example.mypet.googlemaps.model.UserLocation
import javax.inject.Inject

interface MapSearcherRepository {
    suspend fun getGooglePlaces(location: String, query: String): GooglePlaces
    suspend fun getUserLocation(): UserLocation
    fun isLocationServiceEnabled(): Boolean
    suspend fun getGooglePlaceDetails(
        placeId: String,
        fields: String
    ): PlaceDetailsItem
}

class MapSearcherRepositoryImpl @Inject constructor(
    private val dataSource: MapSearcherDataSource,
    private val googlePlacesMapper: GooglePlacesMapper
) : MapSearcherRepository {
    override suspend fun getGooglePlaces(location: String, query: String): GooglePlaces {
        return googlePlacesMapper(dataSource.getGooglePlaces(location, query))
    }

    override suspend fun getGooglePlaceDetails(placeId: String, fields: String): PlaceDetailsItem {
        return googlePlacesMapper(dataSource.getGooglePlaceDetails(placeId, fields))
    }

    override fun isLocationServiceEnabled(): Boolean = dataSource.isLocationServiceEnabled()

    override suspend fun getUserLocation(): UserLocation {
        return dataSource.getUserLocation()
    }
}