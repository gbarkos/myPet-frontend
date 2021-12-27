package com.example.mypet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mypet.R
import com.example.mypet.databinding.PetItemBinding
import com.example.mypet.databinding.VaccinationItemBinding
import com.example.mypet.models.Pet
import com.example.mypet.models.Vaccination
import com.example.mypet.utils.MongoDateAdapter
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class VaccinationsAdapter(private val vaccinations: List<Vaccination>, private val coroutineScope: CoroutineScope) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val onClicksChannel = Channel<Vaccination>(Channel.RENDEZVOUS)
    val onClick: Flow<Vaccination> = onClicksChannel.receiveAsFlow()

    override fun getItemCount(): Int {
        return vaccinations.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VaccinationsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vaccination_item, parent, false), coroutineScope, onClicksChannel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is VaccinationsViewHolder -> {
                holder.bind(vaccinations[position])
            }
        }
    }

    class VaccinationsViewHolder(itemView: View, private val coroutineScope: CoroutineScope, private val onClicksChannel: Channel<Vaccination>) : RecyclerView.ViewHolder(itemView){
        val binding: VaccinationItemBinding = VaccinationItemBinding.bind(itemView);

        fun bind(vaccine: Vaccination){
            binding.vaccinationName.setText(vaccine.name)
            val date = MongoDateAdapter(vaccine.vaccinationDate.toString()).getDate()
            binding.vaccinationDate.setText(date)

            itemView.setOnClickListener{
                onClicksChannel.trySend(vaccine).isSuccess
            }
        }
    }
}