package com.example.mypet.models.responses

import com.example.mypet.models.Reminder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SingleReminderGetResponse(
        @SerializedName("status")
        @Expose()
        val status: String,
        @SerializedName("reminders")
        @Expose()
        val newReminder: Reminder?
)
