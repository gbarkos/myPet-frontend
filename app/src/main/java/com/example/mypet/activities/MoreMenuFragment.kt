package com.example.mypet.activities

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mypet.R
import com.example.mypet.databinding.FragmentMoreMenuBinding
import com.example.mypet.models.Vet
import com.example.mypet.utils.SharedPreferencesUtil
import com.google.gson.Gson

class MoreMenuFragment : Fragment(R.layout.fragment_more_menu) {

    private lateinit var binding: FragmentMoreMenuBinding
    private lateinit var treatmentsFragment: TreatmentsFragment
    private lateinit var surgeriesFragment: SurgeriesFragment
    private lateinit var diagnosticTestsFragment: DiagnosticTestsFragment
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoreMenuBinding.bind(view)
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        adjustViewForVet()

        binding.treatmentsOption.setOnClickListener{
            treatmentsFragment = TreatmentsFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.navigationFragmentContainer, treatmentsFragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }

        binding.surgeriesCard.setOnClickListener{
            surgeriesFragment = SurgeriesFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.navigationFragmentContainer, surgeriesFragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }

        binding.testsCard.setOnClickListener{
            diagnosticTestsFragment = DiagnosticTestsFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.navigationFragmentContainer, diagnosticTestsFragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }
    }

    private fun adjustViewForVet(){
        var stringVet = SharedPreferencesUtil.getVetData()
        if(!stringVet.isNullOrEmpty()) {
            var gson = Gson()
            var vet = gson.fromJson(stringVet, Vet::class.java)

            binding.apply {
                topAppBar.setBackgroundColor(resources.getColor(R.color.vet_blue))
                backgroundLayout.background = resources.getDrawable(R.drawable.generic_background_vet)
            }
        }
    }
}