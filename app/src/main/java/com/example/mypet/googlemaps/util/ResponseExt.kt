package com.example.mypet.googlemaps.util

import retrofit2.Response

fun <T : Any> Response<T>.requireNotNull(): T {
    require(isSuccessful && body() != null)
    return body()!!
}