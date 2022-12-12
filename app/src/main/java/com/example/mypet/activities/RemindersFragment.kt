package com.example.mypet.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypet.R
import com.example.mypet.adapters.PetsAdapter
import com.example.mypet.adapters.RemindersAdapter
import com.example.mypet.databinding.FragmentRemindersBinding
import com.example.mypet.models.PetLimited
import com.example.mypet.models.Reminder
import com.example.mypet.utils.ResponseFunctions
import com.example.mypet.viewmodels.PetsViewModel
import com.example.mypet.viewmodels.RemindersViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RemindersFragment : Fragment(R.layout.fragment_reminders), ResponseFunctions {
    private lateinit var viewmodel: RemindersViewModel
    private lateinit var binding: FragmentRemindersBinding
    private lateinit var reminders: List<Reminder>
    lateinit var confirmDeleteDialog : ConfirmReminderDeleteDialog
    lateinit var loadingDialog : LoadingCircleDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRemindersBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[RemindersViewModel::class.java]
        loadingDialog = LoadingCircleDialog()

        viewmodel.getReminders()
        viewmodel.getRemindersDataFromRepo().observe(viewLifecycleOwner, {
            if(it != null){
                reminders = it.reminders

                binding.recyclerViewReminders.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = RemindersAdapter(reminders,lifecycleScope)
                }
                (binding.recyclerViewReminders.adapter as RemindersAdapter).onClick.onEach {
                    confirmDeleteDialog = ConfirmReminderDeleteDialog.newInstance(it._id)
                    confirmDeleteDialog.show(childFragmentManager, "dialog")
                }.launchIn(lifecycleScope)
            }
        })
    }

    override fun OnStarted() {
        loadingDialog.show(parentFragmentManager, "")
    }

    override fun OnSuccess() {
        loadingDialog.dismiss()
    }

    override fun OnFailure(errorMsg: String?) {
        loadingDialog.dismiss()
        Toast.makeText(context, "Σφάλμα λήψης δεδομένων!", Toast.LENGTH_LONG).show()
    }
}