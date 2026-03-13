package com.example.voto.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voto.data.model.HistoryOrder
import com.example.voto.databinding.ItemOrderHistoryBinding
import com.example.voto.util.Helper

class HistoryOrderAdapter(
    private val list: MutableList<HistoryOrder> = mutableListOf()
): RecyclerView.Adapter<HistoryOrderAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemOrderHistoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = list[position]
        holder.apply {
            binding.tvCameraName.text = order.name
            binding.tvQty.text = "x${order.qty}"
            binding.tvPrice.text = "Rp${Helper.formatPrice(order.subtotal)},-"

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}