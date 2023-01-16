package com.example.mypet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mypet.R
import com.example.mypet.databinding.SurgeryItemBinding
import com.example.mypet.models.Surgery
import com.example.mypet.utils.MongoDateAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class SurgeriesAdapter (private val surgeries: List<Surgery>, private val coroutineScope: CoroutineScope) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val onClicksChannel = Channel<Surgery>(Channel.RENDEZVOUS)
    val onClick: Flow<Surgery> = onClicksChannel.receiveAsFlow()

    override fun getItemCount(): Int {
        return surgeries.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SurgeriesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.surgery_item, parent, false), coroutineScope, onClicksChannel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is SurgeriesViewHolder -> {
                holder.bind(surgeries[position])
            }
        }
    }

    class SurgeriesViewHolder(itemView: View, private val coroutineScope: CoroutineScope, private val onClicksChannel: Channel<Surgery>) : RecyclerView.ViewHolder(itemView){
        val binding: SurgeryItemBinding = SurgeryItemBinding.bind(itemView);

        fun bind(surgery: Surgery){
            binding.surgeryName.setText(surgery.name)
            val date = MongoDateAdapter(surgery.date).getDate()
            binding.surgeryDate.setText(date)

            itemView.setOnClickListener{
                onClicksChannel.trySend(surgery).isSuccess
            }
        }
    }
}