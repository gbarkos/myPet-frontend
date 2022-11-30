package com.example.mypet.googlemaps.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.mypet.googlemaps.util.ConnectivityLiveData
import com.example.mypet.googlemaps.util.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
) : BaseViewModel() {

    private val _locationServicesUI = SingleLiveEvent<Unit>()
    val locationServicesUI: LiveData<Unit> = _locationServicesUI

    fun showLocationOnMap() {
        _locationServicesUI.value = Unit
    }
}