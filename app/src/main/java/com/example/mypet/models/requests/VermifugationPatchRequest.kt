package com.example.mypet.models.requests

import com.example.mypet.models.Vet
import java.util.*

data class VermifugationPatchRequest (
    val manufacturer: String,
    val name: String,
    val expirationDate: String?,
    val vermifugationDate: String,
    val validUntil: String,
    val veterinarian: Vet?
)