package com.example.mypet.activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mypet.R
import com.example.mypet.databinding.FragmentMoreMenuBinding

class MoreMenuFragment : Fragment(R.layout.fragment_more_menu) {

    private lateinit var binding: FragmentMoreMenuBinding
    private lateinit var treatmentsFragment: TreatmentsFragment
    private lateinit var surgeriesFragment: SurgeriesFragment
    private lateinit var diagnosticTestsFragment: DiagnosticTestsFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoreMenuBinding.bind(view)

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
}