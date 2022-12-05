package com.example.mypet.googlemaps.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import java.io.Serializable

data class MapClusterItem(
    val formattedΑddress: String,
    val rating: Float,
    val name: String,
    val id: String,
    val latitude: Double,
    val longitude: Double,
) : ClusterItem, Serializable {
    override fun getSnippet(): String = ""

    override fun getTitle(): String = id

    override fun getPosition(): LatLng = LatLng(latitude, longitude)
}

data class PlaceDetailsItem(
    val formattedΑddress: String = "",
    val rating: Float = 0.0f,
    val name: String = "",
    val id: String = "",
    val phone: String = "",
    val businessStatus: String = "",
    val openingHours: String? = ""
) :Serializable