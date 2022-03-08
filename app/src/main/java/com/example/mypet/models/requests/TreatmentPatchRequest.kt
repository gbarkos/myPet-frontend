package com.example.mypet.models.requests

import com.example.mypet.models.Vet
import java.util.*

data class TreatmentPatchRequest(
    val medicine: String,
    val disease: String,
    val startOfTreatment: String,
    val endOfTreatment: String,
    val frequency: String,
    val veterinarian: Vet?
)
