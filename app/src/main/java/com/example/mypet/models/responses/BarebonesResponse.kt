package com.example.mypet.models.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BarebonesResponse(
    @SerializedName("status")
    @Expose()
    val status: String,
)
