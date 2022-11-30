package com.example.mypet.googlemaps.util

import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(private val connectivityManager: ConnectivityManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!connectivityManager.isNetworkConnected) {
            throw NoInternetException()
        }
        return chain.proceed(chain.request())
    }
}

open class NoInternetException : IOException()