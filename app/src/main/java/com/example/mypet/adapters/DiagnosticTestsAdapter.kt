package com.example.mypet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mypet.R
import com.example.mypet.databinding.DiagnosticTestItemBinding
import com.example.mypet.models.DiagnosticTest
import com.example.mypet.utils.MongoDateAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class DiagnosticTestsAdapter (private val tests: List<DiagnosticTest>, private val coroutineScope: CoroutineScope) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val onClicksChannel = Channel<DiagnosticTest>(Channel.RENDEZVOUS)
    val onClick: Flow<DiagnosticTest> = onClicksChannel.receiveAsFlow()

    override fun getItemCount(): Int {
        return tests.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DiagnosticTestsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.diagnostic_test_item, parent, false), coroutineScope, onClicksChannel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is DiagnosticTestsViewHolder -> {
                holder.bind(tests[position])
            }
        }
    }

    class DiagnosticTestsViewHolder(itemView: View, private val coroutineScope: CoroutineScope, private val onClicksChannel: Channel<DiagnosticTest>) : RecyclerView.ViewHolder(itemView){
        val binding: DiagnosticTestItemBinding = DiagnosticTestItemBinding.bind(itemView);

        fun bind(test: DiagnosticTest){
            binding.testName.setText(test.name)
            val date = MongoDateAdapter(test.date).getDate()
            binding.testDate.setText(date)

            itemView.setOnClickListener{
                onClicksChannel.trySend(test).isSuccess
            }
        }
    }
}