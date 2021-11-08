package com.example.mypet.models.responses

import com.example.mypet.models.Pet
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PetsGetResponse(
    @SerializedName("status")
    @Expose()
    val status: String,
    @SerializedName("pets")
    @Expose()
    val pets: List<Pet>
)
