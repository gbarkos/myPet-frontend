package com.example.mypet.googlemaps

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GoogleMapSearcherApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}