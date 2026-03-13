package com.example.voto.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voto.data.model.StoreCart
import com.example.voto.databinding.ItemStoreCartBinding
import com.example.voto.ui.CartActivity

class StoreCartAdapter(
    private val list: MutableList<StoreCart> = mutableListOf()
): RecyclerView.Adapter<StoreCartAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemStoreCartBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoreCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storeCart = list[position]
        holder.apply {
            binding.cbSelected.text = storeCart.storeName
            binding.rvCart.adapter = CartAdapter(position, storeCart.cart)

            binding.cbSelected.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) {
                    binding.cbSelected.isChecked = true
                    for (i in storeCart.cart) {
                        i.isSelected = true
                    }
                    notifyDataSetChanged()
                } else {
                    binding.cbSelected.isChecked = false
                    for (i in storeCart.cart) {
                        i.isSelected = false
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}