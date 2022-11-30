package com.example.mypet.googlemaps.util

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

val FOREGROUND_LOCATION_PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
)

@RequiresApi(Build.VERSION_CODES.Q)
val BACKGROUND_LOCATION_PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_BACKGROUND_LOCATION
)