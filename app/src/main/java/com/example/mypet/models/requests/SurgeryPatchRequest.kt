package com.example.mypet.models.requests

import com.example.mypet.models.Vet
import java.util.*

data class SurgeryPatchRequest(
    val name: String,
    val date: String,
    val veterinarian: Vet?
)
