package com.example.mypet.googlemaps.ui

import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.mypet.R
import com.example.mypet.googlemaps.model.MapClusterItem
import com.example.mypet.googlemaps.model.PlaceDetailsItem
import com.example.mypet.googlemaps.util.SingleLiveEvent
import com.example.mypet.googlemaps.model.UserLocation
import com.example.mypet.googlemaps.model.UserLocationResultType
import com.example.mypet.googlemaps.usecases.GetGooglePlaceDetailsUseCase
import com.example.mypet.googlemaps.usecases.GetGooglePlacesUseCase
import com.example.mypet.googlemaps.usecases.GetUserLocationUseCase
import com.example.mypet.googlemaps.usecases.IsLocationServiceEnabledUseCase
import javax.inject.Inject

@HiltViewModel
class MapSearcherViewModel @Inject constructor(
    private val getGooglePlacesUseCase: GetGooglePlacesUseCase,
    private val getUserLocationUseCase: GetUserLocationUseCase,
    private val getGooglePlaceDetailsUseCase: GetGooglePlaceDetailsUseCase,
    private val isLocationServiceEnabledUseCase: IsLocationServiceEnabledUseCase
): BaseViewModel() {

    private val _showUserLocationOnMapUI = SingleLiveEvent<UserLocation>()
    val showUserLocationOnMapUI: LiveData<UserLocation> = _showUserLocationOnMapUI

    private val _initUserLocationUI = SingleLiveEvent<Unit>()
    val initUserLocationUI: LiveData<Unit> = _initUserLocationUI

    private val _showGooglePlacesUI = SingleLiveEvent<List<MapClusterItem>>()
    val showGooglePlacesUI: LiveData<List<MapClusterItem>> = _showGooglePlacesUI

    private val _showGooglePlaceDetailsUI = SingleLiveEvent<PlaceDetailsItem>()
    val showGooglePlaceDetailsUI: LiveData<PlaceDetailsItem> = _showGooglePlaceDetailsUI

    private var userLocation: UserLocation? = null
    fun centerOnUserLocation() {
        launch {
            when (val result = getUserLocationUseCase()) {
                is UserLocationResultType.Success -> result.userLocation.let {
                    _showUserLocationOnMapUI.value = it
                    userLocation = it
                    searchForGooglePlaces("Κτηνιατρεία")
                }
                is UserLocationResultType.GpsNotEnabled -> errorLiveData.value = R.string.error_no_gps
                is UserLocationResultType.Failure -> errorLiveData.value = R.string.error_generic
            }
        }
    }

    fun initUserLocation() {
        if (isLocationServiceEnabledUseCase()) {
            _initUserLocationUI.value = Unit
        }
    }


    fun googlePlaceDetails(placeId: String) {
        launchWithProgress {
            _showGooglePlaceDetailsUI.value = getGooglePlaceDetailsUseCase(placeId, "")!!
        }
    }

    fun searchForGooglePlaces(query: String) {
        launchWithProgress {
            userLocation?.let {
                _showGooglePlacesUI.value = getGooglePlacesUseCase("${it.latitude},${it.longitude}", query).clusterItems
            }
        }
    }
}