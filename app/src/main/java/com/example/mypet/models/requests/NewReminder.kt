package com.example.mypet.models.requests

import com.example.mypet.models.enums.ReminderTypes
import java.util.*

data class NewReminder(
        var petId: String,
        var dateScheduled: Date,
        var type: ReminderTypes,
)
