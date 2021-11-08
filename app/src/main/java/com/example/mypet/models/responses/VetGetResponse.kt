package com.example.mypet.models.responses

import com.example.mypet.models.Vet
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VetGetResponse(
    @SerializedName("status")
    @Expose()
    val status : String,
    @SerializedName("vet")
    @Expose()
    val vet : Vet
)
