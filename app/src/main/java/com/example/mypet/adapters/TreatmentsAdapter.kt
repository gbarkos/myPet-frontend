package com.example.mypet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mypet.R
import com.example.mypet.databinding.TreatmentItemBinding
import com.example.mypet.models.Treatment
import com.example.mypet.utils.MongoDateAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class TreatmentsAdapter (private val treatments: List<Treatment>, private val coroutineScope: CoroutineScope) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val onClicksChannel = Channel<Treatment>(Channel.RENDEZVOUS)
    val onClick: Flow<Treatment> = onClicksChannel.receiveAsFlow()

    override fun getItemCount(): Int {
        return treatments.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TreatmentsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.treatment_item, parent, false), coroutineScope, onClicksChannel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is TreatmentsViewHolder -> {
                holder.bind(treatments[position])
            }
        }
    }

    class TreatmentsViewHolder(itemView: View, private val coroutineScope: CoroutineScope, private val onClicksChannel: Channel<Treatment>) : RecyclerView.ViewHolder(itemView){
        val binding: TreatmentItemBinding = TreatmentItemBinding.bind(itemView);

        fun bind(treatment: Treatment){
            binding.treatmentDisease.setText(treatment.disease)
            val date = MongoDateAdapter(treatment.startOfTreatment.toString()).getDate()
            binding.treatmentStartDate.setText(date)

            itemView.setOnClickListener{
                onClicksChannel.trySend(treatment).isSuccess
            }
        }
    }
}