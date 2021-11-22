package com.example.mypet.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentPetsBinding
import com.example.mypet.viewmodels.PetsViewModel

class MainContentActivity: AppCompatActivity() {
    private lateinit var binding : FragmentPetsBinding
    private lateinit var viewmodel : PetsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_content)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}