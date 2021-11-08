package com.example.mypet.models.responses

import com.example.mypet.models.MedicalRecord
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MedicalRecordPatchResponse (
    @SerializedName("status")
    @Expose()
    val status: String,
    @SerializedName("medicalRecord")
    @Expose()
    val medicalRecord: MedicalRecord
)