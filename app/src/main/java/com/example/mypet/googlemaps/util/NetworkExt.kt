package com.example.mypet.googlemaps.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

val ConnectivityManager.isNetworkConnected: Boolean
    get() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
                activeNetwork?.let {
                    val networkCapabilities = getNetworkCapabilities(it)
                    return (networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false) ||
                            (networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false) ||
                            (networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ?: false)
                }

            else ->
                activeNetworkInfo?.let {
                    return it.isConnected && (it.type == ConnectivityManager.TYPE_WIFI || it.type == ConnectivityManager.TYPE_MOBILE)
                }
        }
        return false
    }