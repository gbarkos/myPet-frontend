package com.example.mypet.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mypet.R
import com.example.mypet.databinding.PetItemBinding
import com.example.mypet.models.PetLimited
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class PetsAdapter(private val pets: List<PetLimited>, private val coroutineScope: CoroutineScope) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val onClicksChannel = Channel<PetLimited>(Channel.RENDEZVOUS)
    val onClick: Flow<PetLimited> = onClicksChannel.receiveAsFlow()

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

    class PetsViewHolder(itemView: View, private val coroutineScope: CoroutineScope, private val onClicksChannel:Channel<PetLimited>) : RecyclerView.ViewHolder(itemView){
        val binding: PetItemBinding = PetItemBinding.bind(itemView);

        fun bind(pet: PetLimited){
            binding.petName.text = pet.name.trim()
            binding.petBreed.text = pet.breed.trim()
            val age = pet.age
            binding.petAge.text = pet.age.trim()

            if(pet.sex.equals("Αρσενικό") || pet.sex.equals("Male")){
                    Picasso.get().load(R.drawable.ic_male).into(binding.petGender)
            }else{
                Picasso.get().load(R.drawable.ic_female).into(binding.petGender)
            }

            var picasso = Picasso.get()
            picasso.isLoggingEnabled = true

            picasso
                .load("https://drive.google.com/uc?export=download&id="+pet.photo)
                .config(Bitmap.Config.RGB_565)
                .rotate(90F)
                .error(R.drawable.palceholder_profile_pic)
                .into(binding.petProfilePic)

            itemView.setOnClickListener{
                onClicksChannel.trySend(pet).isSuccess
            }
        }
    }
}