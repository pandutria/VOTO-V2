package com.example.voto.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.voto.R
import com.example.voto.data.CartManager
import com.example.voto.data.HttpHandler
import com.example.voto.data.model.Camera
import com.example.voto.data.model.Cart
import com.example.voto.data.model.StoreCart
import com.example.voto.databinding.ActivityDetailBinding
import com.example.voto.util.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    var camera: Camera? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        val id = intent.getIntExtra("id", 0)
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request("camera/${id}")
            }

            val json = JSONObject(result)
            val code = json.getInt("code")
            val body = json.getString("body")

            if (code in 200..300) {
                val data = JSONObject(body)

                camera = Camera(
                    id = data.getInt("id"),
                    sensor = data.getString("sensor"),
                    isoRange = data.getString("isoRange"),
                    weight = data.getInt("weight"),
                    flash = data.getBoolean("flash"),
                    resolution = data.getString("resolution"),
                    shuterSpeedRange = data.getString("shuterSpeedRange"),
                    wiFi = data.getBoolean("wiFi"),
                    bluetooth = data.getBoolean("bluetooth"),
                    autoFocusSystem = data.getString("autoFocusSystem"),
                    dimensions = data.getString("dimensions"),
                    sellerShop = data.getString("sellerShop"),
                    touchScreen = data.getBoolean("touchScreen"),
                    price = data.getInt("price"),
                    photo = data.getString("photo"),
                )

                binding.tvSensor.text = camera?.sensor
                binding.tvIso.text = camera?.isoRange
                binding.tvWeight.text = camera?.weight.toString()
                binding.tvFlash.text = camera?.flash.toString()
                binding.tvResolution.text = camera?.resolution
                binding.tvShutter.text = camera?.shuterSpeedRange
                binding.tvWifi.text = camera?.wiFi.toString()
                binding.tvBlootooth.text = camera?.bluetooth.toString()
                binding.tvAutofocus.text = camera?.autoFocusSystem
                binding.tvDimensions.text = camera?.dimensions
                binding.tvSelles.text = camera?.sellerShop
                binding.tvTouchSreen.text = camera?.touchScreen.toString()

                binding.tvPrice.text = "Rp${Helper.formatPrice(camera?.price!!)},-"

                val image = Helper.loadImage(data.getString("photo"))
                binding.imgImage.setImageBitmap(image)
            }
        }

        binding.btn.setOnClickListener {
            for(i in CartManager.list) {
                if (i.storeName == camera?.sellerShop) {
                    for (cart in i.cart) {
                        if (cart.camera.id == camera?.id) {
                            cart.qty += 1
                            return@setOnClickListener
                        }
                    }
                    i.cart.add(
                        Cart(
                            camera!!,
                            1
                        )
                    )
                    return@setOnClickListener
                }
            }

            CartManager.list.add(
                StoreCart(
                    camera?.sellerShop!!,
                    cart = mutableListOf(
                        Cart(
                            camera!!,
                            1
                        )
                    )
                )
            )
        }
    }
}