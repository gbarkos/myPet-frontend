package com.example.mypet.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.ActivityPetDetailsBinding
import com.example.mypet.utils.SharedPreferencesUtil
import com.example.mypet.viewmodels.PetsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class PetDetailsActivity: AppCompatActivity() {
    public var lng = 0.0
    public var lat = 0.0

    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: ActivityPetDetailsBinding
    private val petInfoFragment = PetInfoFragment()
    private val vaccinationsFragment = VaccinationsFragment()
    private val vermifugationsFragment = VermifugationsFragment()
    private val moreMenuFragment = MoreMenuFragment()
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_details)
        sharedPreferences = getSharedPreferences(
            packageName,
            Activity.MODE_PRIVATE
        )
        binding = ActivityPetDetailsBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this)[PetsViewModel::class.java]
        binding.petsviewmodelDetails = viewmodel
        viewmodel._id = this.intent.extras?.getString("petID") //pass the id to the viewmodel, so that we can call requestPet(:id)
        adjustViewForVet()
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
                    replaceFragment(moreMenuFragment)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener true
        }
   }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Retrieve data in the intent
        lat = data?.getDoubleExtra("Lat",0.0)!!
        lng = data?.getDoubleExtra("Long",0.0)!!
    }

    private fun adjustViewForVet(){
        var stringVet = SharedPreferencesUtil.getVetData()
        if(!stringVet.isNullOrEmpty()) {
            binding.apply {
                bottomNavigationView.itemTextColor = ColorStateList.valueOf(resources.getColor(R.color.vet_blue))
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.navigationFragmentContainer, fragment)
            transaction.commit()
        }
    }
}