package com.example.mypet.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mypet.api.ServiceGenerator
import com.example.mypet.models.ErrorResponse
import com.example.mypet.models.requests.NewReminder
import com.example.mypet.models.responses.BarebonesResponse
import com.example.mypet.models.responses.PetsLimitedGetResponse
import com.example.mypet.models.responses.RemindersGetResponse
import com.example.mypet.models.responses.SingleReminderGetResponse
import com.example.mypet.utils.Event
import com.example.mypet.utils.NetworkLoadingState
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RemindersRepository {

    private const val TAG = "RemindersRepository"
    val gson = Gson()
    private var statusFromGetReminders: String = ""
    private var statusFromNewReminder: String = ""
    private var statusFromDeletedReminder: String = ""

    fun getStatusFromGetReminders(): String{
        return statusFromGetReminders
    }

    fun getStatusFromNewReminder(): String{
        return statusFromNewReminder
    }

    fun getStatusFromDeletedReminder(): String{
        return statusFromNewReminder
    }

    private val reminderAddResponse: MutableLiveData<SingleReminderGetResponse> = MutableLiveData()
    fun getReminderAddResponse() : MutableLiveData<SingleReminderGetResponse>{
        return reminderAddResponse
    }

    private val remindersGetResponse : MutableLiveData<RemindersGetResponse> = MutableLiveData()
    fun getRemindersResponse() : MutableLiveData<RemindersGetResponse> {
        return remindersGetResponse
    }

    fun requestReminders(callback : () -> Unit) {
        val dataSource = ServiceGenerator
        Log.i(TAG, "Reminders response: Call started")
        dataSource.getMyPetApi()
            .getReminders()
            .enqueue(object : Callback<RemindersGetResponse> {
                override fun onResponse(
                    call: Call<RemindersGetResponse>,
                    response: Response<RemindersGetResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.i(TAG, "onResponse: Response Successful")
                        remindersGetResponse.postValue(response.body())
                        statusFromGetReminders = "success"
                        callback()
                    }else{
                        try {
                            val responseObj: ErrorResponse = gson.fromJson(
                                response.errorBody()?.string(),
                                ErrorResponse::class.java
                            )
                            Log.d("FAILURE MESSAGE", "Failed to get reminders")
                            statusFromGetReminders = responseObj.status
                            callback()
                        } catch (e: Exception) {
                            statusFromGetReminders = "Something went wrong"
                            Log.d("IN CATCH", e.message.toString())
                            callback()
                        }
                    }
                }
                override fun onFailure(call: Call<RemindersGetResponse>, t: Throwable) {
                    Log.i(TAG, "onFailure: " + t.message)
                    callback()
                }
            })
    }

    fun addReminder(petId: String?, dateScheduled : String, timeScheduled: String, type: String, callback : () -> Unit) {
        val dataSource = ServiceGenerator
        var body = NewReminder(petId, dateScheduled, timeScheduled, type)
        Log.i(TAG, "Reminders response: Call started")
        dataSource.getMyPetApi()
            .newReminder(body)
            .enqueue(object : Callback<SingleReminderGetResponse> {
                override fun onResponse(
                    call: Call<SingleReminderGetResponse>,
                    response: Response<SingleReminderGetResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.i(TAG, "onResponse: Response Successful")
                        reminderAddResponse.postValue(response.body())
                        statusFromNewReminder = "success"
                        callback()
                    }else{
                        try {
                            val responseObj: ErrorResponse = gson.fromJson(
                                response.errorBody()?.string(),
                                ErrorResponse::class.java
                            )
                            Log.d("FAILURE MESSAGE", "Failed to get reminders")
                            statusFromNewReminder = responseObj.status
                            callback()
                        } catch (e: Exception) {
                            statusFromNewReminder = "Something went wrong"
                            Log.d("IN CATCH", e.message.toString())
                            callback()
                        }
                    }
                }
                override fun onFailure(call: Call<SingleReminderGetResponse>, t: Throwable) {
                    Log.i(TAG, "onFailure: " + t.message)
                    callback()
                }
            })
    }

    fun deleteReminder(reminderId: String, callback : () -> Unit) {
        val dataSource = ServiceGenerator
        Log.i(TAG, "Reminders response: Call started")
        dataSource.getMyPetApi()
            .deleteReminder(reminderId)
            .enqueue(object : Callback<BarebonesResponse> {
                override fun onResponse(
                    call: Call<BarebonesResponse>,
                    response: Response<BarebonesResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.i(TAG, "onResponse: Response Successful")
                        //reminderAddResponse.postValue(response.body())
                        statusFromNewReminder = "success"
                        callback()
                    }else{
                        try {
                            val responseObj: ErrorResponse = gson.fromJson(
                                response.errorBody()?.string(),
                                ErrorResponse::class.java
                            )
                            Log.d("FAILURE MESSAGE", "Failed to get reminders")
                            statusFromNewReminder = responseObj.status
                            callback()
                        } catch (e: Exception) {
                            statusFromNewReminder = "Something went wrong"
                            Log.d("IN CATCH", e.message.toString())
                            callback()
                        }
                    }
                }
                override fun onFailure(call: Call<BarebonesResponse>, t: Throwable) {
                    Log.i(TAG, "onFailure: " + t.message)
                    callback()
                }
            })
    }
}