package com.example.mypet.activities

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.utils.ResponseFunctions
import com.example.mypet.utils.SharedPreferencesUtil
import com.example.mypet.viewmodels.UserViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity(), ResponseFunctions {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SharedPreferencesUtil.init(applicationContext)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            Log.d(TAG, token)
            userViewModel.saveFcmToken(token)
            userViewModel.responseListener = this
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun OnStarted() {
        Log.d(TAG, "FCM Token save to DB started")
    }

    override fun OnSuccess() {
        Log.d(TAG, "Successfully saved FCM Token to DB")
    }

    override fun OnFailure(errorMsg: String?) {
        Log.d(TAG, "Failed at saving FCM Token to DB")
    }
}