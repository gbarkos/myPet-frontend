package com.example.mypet.api

import com.example.mypet.utils.Constants
import com.example.mypet.utils.HeaderInterceptor
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {

    private val TAG: String = "ServiceGenerator"

    private var interceptor = HttpLoggingInterceptor()
    private var headerInterceptor = HeaderInterceptor()

    val client = OkHttpClient.Builder()
            .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)) //log the body
            //.addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)) //log the headers
            .addInterceptor(headerInterceptor) //add Authorization header
            .build()

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(Constants().getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)

    private val retrofit : Retrofit = retrofitBuilder.build();
    private val petApi : MyPetApi = retrofit.create(MyPetApi::class.java)

    fun getMyPetApi() : MyPetApi{
        return petApi
    }
}