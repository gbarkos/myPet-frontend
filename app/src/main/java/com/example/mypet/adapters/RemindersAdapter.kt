package com.example.mypet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mypet.R
import com.example.mypet.databinding.ReminderItemBinding
import com.example.mypet.models.PetLimited
import com.example.mypet.models.Reminder
import com.example.mypet.models.enums.ReminderTypes
import com.example.mypet.utils.MongoDateAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class RemindersAdapter(private val reminders: List<Reminder>, private val coroutineScope: CoroutineScope)  : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    private val onClicksChannel = Channel<Reminder>(Channel.RENDEZVOUS)
    val onClick: Flow<Reminder> = onClicksChannel.receiveAsFlow()

    override fun getItemCount(): Int {
        return reminders.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RemindersViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.reminder_item, parent, false), coroutineScope, onClicksChannel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RemindersViewHolder -> {
                holder.bind(reminders[position])
            }
        }
    }

    class RemindersViewHolder(itemView: View, private val coroutineScope: CoroutineScope, private val onClicksChannel:Channel<Reminder>) : RecyclerView.ViewHolder(itemView) {
        val binding: ReminderItemBinding = ReminderItemBinding.bind(itemView);

        fun bind(reminder: Reminder) {
           val date = MongoDateAdapter(reminder.dateScheduled).getDate()

            binding.reminderName.text = reminder.type +" - "+ reminder.pet.name
            binding.reminderDate.text = date

            binding.reminderDelete.setOnClickListener {
                onClicksChannel.trySend(reminder).isSuccess
            }
        }
    }
}