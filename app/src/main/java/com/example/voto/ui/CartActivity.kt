package com.example.voto.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.voto.R
import com.example.voto.data.CartManager
import com.example.voto.databinding.ActivityCartBinding
import com.example.voto.ui.adapter.StoreCartAdapter
import com.example.voto.util.Helper

class CartActivity : AppCompatActivity() {
    private var _binding: ActivityCartBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvCamera.adapter = StoreCartAdapter(CartManager.list)
        countTotal()
    }

    fun countTotal() {
        var total = 0
        for (i in CartManager.list) {
            for (cart in i.cart) {
                if (cart.isSelected == true) {
                    total += cart.camera.price!!
                }
            }
        }
        binding.tvTotal.text = "Rp${Helper.formatPrice(total)},-"
    }
}