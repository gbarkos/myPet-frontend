package com.example.mypet.googlemaps.data

import com.example.mypet.googlemaps.model.GooglePlaceDetailsGenericResponse
import com.example.mypet.googlemaps.model.GooglePlacesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GooglePlacesApi {
    @GET("/maps/api/place/textsearch/json?&radius=10000&key=AIzaSyDd6i78agEdZNiTpM4TD9wffGVUbur5Ljk")
    suspend fun getGooglePlaces(
        @Query("location") location: String,
        @Query("query") query: String
    ): Response<GooglePlacesResponse>

    @GET("/maps/api/place/details/json?&key=AIzaSyDd6i78agEdZNiTpM4TD9wffGVUbur5Ljk")
    suspend fun getGooglePlaceDetails(
        @Query("place_id") placeId: String,
        @Query("fields") fields: String,
        @Query("language") language: String
    ): Response<GooglePlaceDetailsGenericResponse>

}