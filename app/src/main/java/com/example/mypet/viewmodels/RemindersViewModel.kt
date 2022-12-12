package com.example.mypet.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypet.models.responses.PetsLimitedGetResponse
import com.example.mypet.models.responses.RemindersGetResponse
import com.example.mypet.repositories.PetsRepository
import com.example.mypet.repositories.RemindersRepository
import com.example.mypet.utils.ResponseFunctions
import kotlinx.coroutines.launch

class RemindersViewModel: ViewModel()  {
    var responseListener: ResponseFunctions? = null
    private val repository : RemindersRepository = RemindersRepository

    fun getRemindersDataFromRepo(): MutableLiveData<RemindersGetResponse> {
        return repository.getRemindersResponse()
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
}