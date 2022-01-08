package com.example.mypet.models.responses

import com.example.mypet.models.PetLimited
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PetsLimitedGetResponse(
    @SerializedName("status")
    @Expose()
    val status: String,
    @SerializedName("pets")
    @Expose()
    val pets: List<PetLimited>
)
