package com.example.mypet.models.requests

import com.example.mypet.models.Vet
import java.util.*

data class TreatmentPatchRequest(
    val medicine: String,
    val disease: String,
    val sartOfTreatment: Date,
    val endOfTreatment: Date,
    val frequency: Int,
    val veterinarian: Vet
)
