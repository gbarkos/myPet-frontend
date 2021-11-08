package com.example.mypet.models

data class MedicalRecord(
    val vaccinations: List<Vaccination>,
    val vermifugations: List<Vermifugation>,
    val treatments: List<Treatment>,
    val surgeries: List<Surgery>,
    val diagnosticTests: List<DiagnosticTest>,
    val _id: String
)
