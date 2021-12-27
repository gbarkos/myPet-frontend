package com.example.mypet.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mypet.R
import com.example.mypet.databinding.ActivityPetDetailsBinding

import com.example.mypet.databinding.FragmentPetInfoBinding
import com.example.mypet.models.Pet
import com.example.mypet.models.Vaccination
import com.example.mypet.viewmodels.PetsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class PetDetailsActivity: AppCompatActivity() {

    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: ActivityPetDetailsBinding
    private val petInfoFragment = PetInfoFragment()
    private val vaccinationsFragment = VaccinationsFragment()
    private val vermifugationsFragment = VermifugationsFragment()
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_details)

        binding = ActivityPetDetailsBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this).get(PetsViewModel::class.java)
        binding.petsviewmodelDetails = viewmodel
        viewmodel._id = this.intent.extras?.getString("petID") //pass the id to the viewmodel, so that we can call requestPet(:id)

        replaceFragment(petInfoFragment)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.navigation_menu_pet_profile -> {
                    Log.d("Pet!!!","option1")
                    replaceFragment(petInfoFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_menu_vaccinations -> {
                    Log.d("Pet!!!","option2")
                    replaceFragment(vaccinationsFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_menu_vermifugations -> {
                    Log.d("Pet!!!","option3")
                    replaceFragment(vermifugationsFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_menu_more-> {
                    Log.d("Pet!!!","option4")
                    //replaceFragment(vaccinationsFragment)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener true
        }
   }

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.navigationFragmentContainer, fragment)
//          transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}