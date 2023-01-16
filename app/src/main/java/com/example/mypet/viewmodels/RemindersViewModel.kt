package com.example.mypet.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypet.models.responses.RemindersGetResponse
import com.example.mypet.models.responses.SingleReminderGetResponse
import com.example.mypet.repositories.RemindersRepository
import com.example.mypet.utils.ResponseFunctions
import kotlinx.coroutines.launch

class RemindersViewModel: ViewModel()  {
    var responseListener: ResponseFunctions? = null
    private val repository : RemindersRepository = RemindersRepository

    fun getRemindersDataFromRepo(): MutableLiveData<RemindersGetResponse> {
        return repository.getRemindersResponse()
    }

    fun getNewReminderDataFromRepo(): MutableLiveData<SingleReminderGetResponse> {
        return repository.getReminderAddResponse()
    }


    fun getReminders(){
        responseListener?.OnStarted()
        viewModelScope.launch {
            repository.requestReminders(fun(){
                Log.d("STATUS", repository.getStatusFromGetReminders())
                if(repository.getStatusFromGetReminders() == "fail"){
                    responseListener?.OnFailure(null)
                    Log.d("On Failure","failed")
                }else{
                    responseListener?.OnSuccess()
                }
            })
        }
    }

    fun addReminder(petId : String?, dateScheduled : String, timeScheduled : String, type : String){
        responseListener?.OnStarted()
        viewModelScope.launch {
            repository.addReminder(petId, dateScheduled, timeScheduled, type, fun(){
                Log.d("STATUS", repository.getStatusFromNewReminder())
                if(repository.getStatusFromNewReminder() == "fail"){
                    responseListener?.OnFailure(null)
                    Log.d("On Failure","failed")
                }else{
                    responseListener?.OnSuccess()
                }
            })
        }
    }

    fun deleteReminder(reminderId : String){
        responseListener?.OnStarted()
        viewModelScope.launch {
            repository.deleteReminder(reminderId, fun(){
                Log.d("STATUS", repository.getStatusFromDeletedReminder())
                if(repository.getStatusFromDeletedReminder() == "fail"){
                    responseListener?.OnFailure(null)
                    Log.d("On Failure","failed")
                }else{
                    responseListener?.OnSuccess()
                }
            })
        }
    }
}
