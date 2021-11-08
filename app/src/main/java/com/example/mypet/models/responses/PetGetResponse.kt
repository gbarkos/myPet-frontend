package com.example.mypet.models.responses

import com.example.mypet.models.Pet
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PetGetResponse(
    @SerializedName("status")
    @Expose()
    val status: String,
    @SerializedName("pet")
    @Expose()
    val pet: Pet
)
