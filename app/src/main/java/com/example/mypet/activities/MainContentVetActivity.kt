package com.example.mypet.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mypet.R

class MainContentVetActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_content_vet)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}