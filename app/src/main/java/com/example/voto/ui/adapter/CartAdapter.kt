package com.example.voto.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voto.data.CartManager
import com.example.voto.data.model.Cart
import com.example.voto.databinding.ItemCartBinding
import com.example.voto.ui.CartActivity
import com.example.voto.util.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartAdapter(
    private val storeIndex: Int,
    private val list: MutableList<Cart> = mutableListOf()
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = list[position]
        holder.apply {
            binding.tvName.text = cart.camera.name
            binding.tvQty.text = cart.qty.toString()
            binding.tvPrice.text = "Rp${Helper.formatPrice(cart.qty * cart.camera.price!!)},-"

            CoroutineScope(Dispatchers.Main).launch {
                val image = Helper.loadImage(cart.camera.photo!!)
                binding.imgPhoto.setImageBitmap(image)
            }

            binding.btnMinus.setOnClickListener {
                if (cart.qty == 1) {
                    list.removeAt(position)
                    for (i in CartManager.list) {
                        if (i.cart.isEmpty()) {
                            CartManager.list.removeAt(storeIndex)
                        }
                    }
                    ((holder.itemView.context as CartActivity).countTotal())
                    notifyDataSetChanged()
                    return@setOnClickListener
                }
                cart.qty -= 1
                binding.tvQty.text = cart.qty.toString()
                binding.tvPrice.text = "Rp${Helper.formatPrice(cart.qty * cart.camera.price!!)},-"
                ((holder.itemView.context as CartActivity).countTotal())
                notifyDataSetChanged()
            }

            binding.btnPlus.setOnClickListener {
                cart.qty += 1
                binding.tvQty.text = cart.qty.toString()
                binding.tvPrice.text = "Rp${Helper.formatPrice(cart.qty * cart.camera.price!!)},-"
                ((holder.itemView.context as CartActivity).countTotal())
                notifyDataSetChanged()
            }

            if (cart.isSelected == true) {
                binding.cbSelected.isChecked = true
            } else {
                binding.cbSelected.isChecked = false
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}