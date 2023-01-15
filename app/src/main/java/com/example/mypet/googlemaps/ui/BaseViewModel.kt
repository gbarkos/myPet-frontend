package com.example.mypet.googlemaps.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypet.R
import com.example.mypet.googlemaps.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var errorLiveData: ErrorLiveData

    protected fun launch(delayValue: Long = 0, function: suspend () -> Unit) {
        viewModelScope.launch {
            delay(delayValue)
            try {
                function.invoke()
            } catch (exception: NoInternetException) {
                errorLiveData.value = R.string.error_no_internet
            }
        }
    }

    protected fun launchWithProgress(
        delayValue: Long = 0,
        preload: suspend () -> Unit = {},
        function: suspend () -> Unit
    ) {
        viewModelScope.launch {
            delay(delayValue)
            preload.invoke()
            LoadingLiveData.postValue(true)
            try {
                function.invoke()
            } catch (exception: NoInternetException) {
                errorLiveData.value = R.string.error_no_internet
            }
        }.apply {
            invokeOnCompletion {
                LoadingLiveData.postValue(false)
            }
        }
    }
}