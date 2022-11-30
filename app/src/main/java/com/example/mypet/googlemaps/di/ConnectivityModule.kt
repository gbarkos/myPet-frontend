package com.example.mypet.googlemaps.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.mypet.googlemaps.util.*

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {

    @Provides
    fun provideConnectivityManager(application: Application): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun provideConnectivityInterceptor(connectivityManager: ConnectivityManager): ConnectivityInterceptor {
        return ConnectivityInterceptor(connectivityManager)
    }

    @Provides
    fun provideConnectivityLiveData(monitor: ConnectivityMonitor): ConnectivityLiveData {
        return ConnectivityLiveData(monitor)
    }

    @Provides
    fun provideConnectivityMonitor(
        @ApplicationContext context: Context,
        connectivityManager: ConnectivityManager
    ): ConnectivityMonitor {
        return when {
            Build.VERSION.SDK_INT > Build.VERSION_CODES.M -> NougatConnectivityMonitor(connectivityManager)
            else -> LegacyConnectivityMonitor(context, connectivityManager)
        }
    }
}