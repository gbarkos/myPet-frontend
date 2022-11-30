package com.example.mypet.googlemaps

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GoogleMapSearcherApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}