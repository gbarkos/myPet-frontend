package com.example.mypet.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mypet.R
import com.example.mypet.utils.SharedPreferencesUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SharedPreferencesUtil.init(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        //SharedPreferencesUtil.clearPreferences()
    }
}