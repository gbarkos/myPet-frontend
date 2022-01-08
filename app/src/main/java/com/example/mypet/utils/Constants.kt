package com.example.mypet.utils

class Constants {
    companion object {
        private const val BASE_URL = "http://192.168.1.8:8001/";
        private const val ACCESS_TOKEN = "ACCESS_TOKEN";
    }

    fun getBaseUrl() = BASE_URL;
    fun getAccessToken() = ACCESS_TOKEN;

}