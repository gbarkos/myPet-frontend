package com.example.mypet.googlemaps.usecases

import com.example.mypet.googlemaps.data.MapSearcherRepository
import com.example.mypet.googlemaps.model.GooglePlaces
import javax.inject.Inject

class GetGooglePlacesUseCase @Inject constructor(
    private val repository: MapSearcherRepository
) {
    suspend operator fun invoke(location: String, query: String): GooglePlaces {
        return try {
            repository.getGooglePlaces(location, query)
        } catch (ex: Exception) {

            GooglePlaces()
        }
    }
}