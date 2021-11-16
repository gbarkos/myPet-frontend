package com.example.mypet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mypet.R
import com.example.mypet.models.Pet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class PetsAdapter(private val pets: List<Pet>, private val coroutineScope: CoroutineScope) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val onClicksChannel = Channel<Pet>(Channel.RENDEZVOUS)
    val onClick: Flow<Pet> = onClicksChannel.receiveAsFlow()

    override fun getItemCount(): Int {
        return pets.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PetsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.pet_item, parent, false), coroutineScope, onClicksChannel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PetsViewHolder -> {
                holder.bind(pets.get(position))
            }
        }
    }

    class PetsViewHolder(itemView: View, private val coroutineScope: CoroutineScope, private val onClicksChannel:Channel<Pet>) : RecyclerView.ViewHolder(itemView){
        fun bind(pet: Pet){
            itemView.petName.setText(pet.name)
            itemView.petGender.setText(pet.sex)
            itemView.petProfilePic.setText()

            itemView.setOnClickListener{
                onClicksChannel.trySend(pet).isSuccess
            }
        }
    }
}