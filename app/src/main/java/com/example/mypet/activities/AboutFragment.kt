package com.example.mypet.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mypet.R
import com.example.mypet.databinding.FragmentAboutBinding

class AboutFragment : Fragment(R.layout.fragment_about){

    private lateinit var binding: FragmentAboutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAboutBinding.bind(view)

        binding.aboutReturn.setOnClickListener{
            /*val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.remove(this)
            transaction?.commit()*/
            val intent = Intent(activity, MainContentActivity::class.java)
            startActivity(intent)
        }

    }
}