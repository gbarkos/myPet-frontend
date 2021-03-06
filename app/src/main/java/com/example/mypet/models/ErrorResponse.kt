package com.example.mypet.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("status")
    @Expose()
    val status: String,
    @SerializedName("message")
    @Expose()
    val message: String
)
