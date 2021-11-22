package com.example.mypet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mypet.R
import com.example.mypet.databinding.PetItemBinding
import com.example.mypet.models.Pet
import com.squareup.picasso.Picasso
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
                holder.bind(pets[position])
            }
        }
    }

    class PetsViewHolder(itemView: View, private val coroutineScope: CoroutineScope, private val onClicksChannel:Channel<Pet>) : RecyclerView.ViewHolder(itemView){
        val binding: PetItemBinding = PetItemBinding.bind(itemView);

        fun bind(pet: Pet){
            binding.petName.setText(pet.name)
            binding.petGender.setText(pet.sex)
            Picasso.get().load(pet.photo).into(binding.petProfilePic)

            itemView.setOnClickListener{
                onClicksChannel.trySend(pet).isSuccess
            }
        }
    }
}