package com.example.mypet.googlemaps.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GooglePlacesResponse(
    @Json(name = "html_attributions")
    val htmlAttributions: List<String>?,
    @Json(name = "results")
    val candidates: List<GooglePlacesDetailsResponse>?,
    @Json(name = "status")
    val status: String?,
)

@JsonClass(generateAdapter = true)
data class GooglePlacesDetailsResponse(
    @Json(name = "formatted_address")
    val formattedΑddress: String?,
    @Json(name = "rating")
    val rating: Float?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "place_id")
    val placeId: String?,
    @Json(name = "geometry")
    val geometry: GooglePlacesLocationResponse?
)

@JsonClass(generateAdapter = true)
data class GooglePlacesLocationResponse(
    val location: GooglePlaceCoordinatesResponse?
)

@JsonClass(generateAdapter = true)
data class GooglePlaceCoordinatesResponse(
    val lat: Double?,
    val lng: Double?
)

@JsonClass(generateAdapter = true)
data class GooglePlaceDetailsGenericResponse(
    @Json(name = "html_attributions")
    val htmlAttributions: List<String>?,
    @Json(name = "result")
    val candidates: GooglePlaceDetailsResponse?,
    @Json(name = "status")
    val status: String?,
)

@JsonClass(generateAdapter = true)
data class GooglePlaceDetailsResponse(
    @Json(name = "formatted_address")
    val formattedΑddress: String?,
    @Json(name = "rating")
    val rating: Float?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "place_id")
    val placeId: String?,
    @Json(name = "international_phone_number")
    val phone: String?
)