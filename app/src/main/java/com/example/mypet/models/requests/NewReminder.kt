package com.example.mypet.models.requests

import com.example.mypet.models.enums.ReminderTypes
import java.util.*

data class NewReminder(
        var petId: String?,
        var dateScheduled: String,
        var timeScheduled : String,
        var type: String,
)
