package com.example.mypet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mypet.R
import com.example.mypet.databinding.VaccinationItemBinding
import com.example.mypet.databinding.VermifugationItemBinding
import com.example.mypet.models.Vaccination
import com.example.mypet.models.Vermifugation
import com.example.mypet.utils.MongoDateAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class VermifugationsAdapter (private val vermifugations: List<Vermifugation>, private val coroutineScope: CoroutineScope) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val onClicksChannel = Channel<Vermifugation>(Channel.RENDEZVOUS)
    val onClick: Flow<Vermifugation> = onClicksChannel.receiveAsFlow()

    override fun getItemCount(): Int {
        return vermifugations.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VermifugationsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vermifugation_item, parent, false), coroutineScope, onClicksChannel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is VermifugationsViewHolder -> {
                holder.bind(vermifugations[position])
            }
        }
    }

    class VermifugationsViewHolder(itemView: View, private val coroutineScope: CoroutineScope, private val onClicksChannel: Channel<Vermifugation>) : RecyclerView.ViewHolder(itemView){
        val binding: VermifugationItemBinding = VermifugationItemBinding.bind(itemView);

        fun bind(vermifugation: Vermifugation){
            binding.vermifugationName.setText(vermifugation.name)
            val date = MongoDateAdapter(vermifugation.vermifugationDate.toString()).getDate()
            binding.vermifugationDate.setText(date)

            itemView.setOnClickListener{
                onClicksChannel.trySend(vermifugation).isSuccess
            }
        }
    }
}