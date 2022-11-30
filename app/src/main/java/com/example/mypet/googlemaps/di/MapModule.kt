package com.example.mypet.googlemaps.di

import android.app.Application
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.PlacesClient
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import com.example.mypet.googlemaps.data.*
import com.example.mypet.googlemaps.util.ConnectivityInterceptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapModule {

    @Singleton
    @Provides
    fun provideGooglePlacesClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        connectionSpec: ConnectionSpec,
        connectivityInterceptor: ConnectivityInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(connectivityInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectionSpecs(listOf(connectionSpec))

     /*   if (BuildConfig.DEBUG) {
            okHttpClient.addNetworkInterceptor(httpLoggingInterceptor)
        }*/

        return okHttpClient.build()
    }

    @Provides
    fun provideAutoCompleteToken(): AutocompleteSessionToken = AutocompleteSessionToken.newInstance()

    @Provides
    fun provideRetrofitForGooglePlaces(okHttpClient: OkHttpClient, moshi: Moshi): GooglePlacesApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(GooglePlacesApi::class.java)
    }

    @Provides
    fun providePlacesClient(context: Context): PlacesClient {
        Places.initialize(context, PLACES_API_KEY)
        return Places.createClient(context)
    }

    const val PLACES_API_KEY = "AIzaSyAM4ohNphBqIoD2d8Nc_aE08sRsnH6Dg5E"

    @Provides
    fun provideLocationManager(application: Application): LocationManager {
        return application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
@InstallIn(ActivityRetainedComponent::class)
interface MapBindsModule {

    @Binds
    fun bindMapSearcherDataSource(dataSource: MapSearcherDataSourceImpl): MapSearcherDataSource

    @Binds
    fun bindMapSearcherRepository(repository: MapSearcherRepositoryImpl): MapSearcherRepository
}