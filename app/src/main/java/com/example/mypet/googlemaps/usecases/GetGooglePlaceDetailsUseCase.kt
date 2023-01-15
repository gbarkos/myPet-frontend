package com.example.mypet.googlemaps.usecases

import com.example.mypet.googlemaps.data.MapSearcherRepository
import com.example.mypet.googlemaps.model.PlaceDetailsItem
import javax.inject.Inject

class GetGooglePlaceDetailsUseCase @Inject constructor(
    private val repository: MapSearcherRepository
) {
    suspend operator fun invoke(placeId: String, fields: String): PlaceDetailsItem {
        return try {
            repository.getGooglePlaceDetails(placeId, fields)
        } catch (ex: Exception) {
            PlaceDetailsItem()
        }
    }
}