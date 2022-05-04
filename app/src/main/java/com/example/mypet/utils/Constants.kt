package com.example.mypet.utils

class Constants {
    companion object {
        private const val BASE_URL = "https://shrouded-oasis-95834.herokuapp.com/"
        /*private const val LOCAL_BASE_URL = "http://192.168.1.2:8005/";*/
        private const val ACCESS_TOKEN = "ACCESS_TOKEN";
    }

    fun getBaseUrl() = BASE_URL;
    fun getAccessToken() = ACCESS_TOKEN;

}