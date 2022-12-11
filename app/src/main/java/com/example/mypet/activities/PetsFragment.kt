package com.example.mypet.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypet.R
import com.example.mypet.adapters.PetsAdapter
import com.example.mypet.databinding.FragmentPetsBinding
import com.example.mypet.googlemaps.ui.AppActivity
import com.example.mypet.models.Pet
import com.example.mypet.models.PetLimited
import com.example.mypet.models.responses.PetsLimitedGetResponse
import com.example.mypet.utils.EventObserver
import com.example.mypet.utils.NetworkLoadingState
import com.example.mypet.utils.SharedPreferencesUtil
import com.example.mypet.viewmodels.PetsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.system.exitProcess

class PetsFragment : Fragment(R.layout.fragment_pets) {
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentPetsBinding
    private lateinit var petsList: List<PetLimited>
    private lateinit var aboutFragment: AboutFragment
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var shouldExit : Boolean = false
        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if(shouldExit){
                activity?.moveTaskToBack(true);
                activity?.finish()
                exitProcess(-1)
            }else{
                Toast.makeText(context, "Πατήστε πίσω ακόμα μια φορά για να κλείσετε την εφαρμογή", Toast.LENGTH_LONG).show()
                shouldExit = true
            }
        }

        // The callback can be enabled or disabled here or in the lambda
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPetsBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        binding.petsviewmodel = viewmodel
        viewmodel.requestPets()
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )

        viewmodel.getPetsDataFromRepo().observe(viewLifecycleOwner, {
            Log.d("PetsFragment",it.pets.toString())
            petsList = it.pets

            binding.recyclerViewPets.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = PetsAdapter(petsList,lifecycleScope)
            }
            (binding.recyclerViewPets.adapter as PetsAdapter).onClick.onEach {
                val bundle = bundleOf("petID" to it._id)
                Log.d("OnItemClick",it._id)
                viewmodel._id = it._id;
                val intent = Intent (getActivity(), PetDetailsActivity::class.java)
                intent.putExtras(bundle)
                activity?.startActivity(intent)
            }.launchIn(lifecycleScope)
        })

        binding.addNewPetButton.setOnClickListener{
            findNavController().navigate(R.id.action_petsFragment_to_newPetFragment)
        }

        binding.petsScreenMenu.setOnClickListener{
            showPopup(binding.petsScreenMenu)
        }
    }

    fun showPopup(v: View) {
        val popup = PopupMenu(this.context, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.pets_screen_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId){
                R.id.nearby_vets -> {
                    val intent = Intent(activity, AppActivity::class.java)
                    startActivity(intent)
                    return@setOnMenuItemClickListener true
                }
                R.id.settings -> {
                    settingsFragment = SettingsFragment()
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(this.id, settingsFragment)
                    transaction?.disallowAddToBackStack()
                    transaction?.commit()
                    return@setOnMenuItemClickListener true
                }
                R.id.help -> {
                    aboutFragment = AboutFragment()
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(this.id, aboutFragment)
                    transaction?.disallowAddToBackStack()
                    transaction?.commit()
                    return@setOnMenuItemClickListener true
                }
                R.id.logout -> {
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SharedPreferencesUtil.clearPreferences()
                    startActivity(intent)
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }
}