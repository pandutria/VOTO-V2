package com.example.voto.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voto.data.model.Camera
import com.example.voto.databinding.ItemCameraBinding
import com.example.voto.ui.DetailActivity
import com.example.voto.util.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CameraAdapter(
    private val list: MutableList<Camera> = mutableListOf()
): RecyclerView.Adapter<CameraAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemCameraBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCameraBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val camera = list[position]
        holder.apply {
            binding.tvName.text = camera.name
            binding.tvResolution.text = camera.resolution
            binding.tvPrice.text = "Rp${Helper.formatPrice(camera.price)}.-"

            CoroutineScope(Dispatchers.Main).launch {
                binding.imgPhoto.setImageBitmap(Helper.loadImage(camera.photo))
            }
        }

        holder.itemView.setOnClickListener {
            val i = Intent(holder.itemView.context, DetailActivity::class)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}