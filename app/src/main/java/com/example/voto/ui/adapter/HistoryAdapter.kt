package com.example.voto.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voto.data.model.History
import com.example.voto.databinding.ItemHistoryBinding
import com.example.voto.util.Helper

class HistoryAdapter(
    private val list: MutableList<History> = mutableListOf()
): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = list[position]
        holder.apply {
            binding.tvId.text = history.id
            binding.tvStatus.text = "Status: ${history.status}"
            binding.tvTotal.text = "Rp${Helper.formatPrice(history.totalPrice)}, -"

            binding.rvCamera.adapter = HistoryOrderAdapter(history.transactions)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}