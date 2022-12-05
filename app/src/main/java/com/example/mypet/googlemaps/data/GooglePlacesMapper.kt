package com.example.mypet.googlemaps.data

import com.example.mypet.googlemaps.model.*
import com.example.mypet.googlemaps.util.Mapper
import javax.inject.Inject

class GooglePlacesMapper @Inject constructor() : Mapper {

    operator fun invoke(response: GooglePlacesResponse): GooglePlaces {
        val googlePlacesList = mutableListOf<MapClusterItem>()
        response.candidates?.forEach {
            googlePlacesList.add(
                MapClusterItem(
                    formattedΑddress = it.formattedΑddress ?: "",
                    rating = it.rating ?: 0.0f,
                    name = it.name ?: "",
                    id = it.placeId ?: "",
                    latitude = it.geometry?.location?.lat ?: (0).toDouble(),
                    longitude = it.geometry?.location?.lng ?: (0).toDouble()

                )
            )
        }
        return GooglePlaces(googlePlacesList)
    }

    operator fun invoke(response: GooglePlaceDetailsGenericResponse): PlaceDetailsItem {
        return response.candidates.let{
            PlaceDetailsItem(
                    formattedΑddress = it?.formattedΑddress ?: "",
                    rating = it?.rating ?: 0.0f,
                    name = it?.name ?: "",
                    id = it?.placeId ?: "",
                    phone = it?.phone ?: "",
                    businessStatus = it?.businessStatus ?: "",
                    openingHours = it?.openingHours ?: ""
                )
        }
    }
}