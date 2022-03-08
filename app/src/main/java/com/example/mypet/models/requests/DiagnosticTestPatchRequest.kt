package com.example.mypet.models.requests

import com.example.mypet.models.Vet
import java.util.*

data class DiagnosticTestPatchRequest(
    val name: String,
    val date: String,
    val result: String,
    val veterinarian: Vet?
)
