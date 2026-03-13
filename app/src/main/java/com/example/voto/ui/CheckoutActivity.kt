package com.example.voto.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.voto.R
import com.example.voto.data.CartManager
import com.example.voto.data.HttpHandler
import com.example.voto.data.TokenManager
import com.example.voto.data.UserSession
import com.example.voto.databinding.ActivityCheckoutBinding
import com.example.voto.util.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity() {
    private var _binding: ActivityCheckoutBinding? = null
    private val binding get() = _binding!!

    val fee: Int = 30000
    var subtotal = 0
    var total = 0

    var rBody: JSONObject? = null
    var items = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        for (i in CartManager.list) {
            for (cart in i.cart) {
                subtotal += cart.camera.price!!
                val item = JSONObject().apply {
                    put("cameraID", cart.camera.id)
                    put("qty", cart.qty)
                    put("subtotal", cart.camera.price * cart.qty)
                }
                items.put(item)
            }
        }

        total = subtotal + fee
        binding.tvSubtotal.text = "Rp${Helper.formatPrice(subtotal)}"
        binding.tvTotal.text = "Rp${Helper.formatPrice(total)}"
        binding.tvFee.text = "Rp${Helper.formatPrice(fee)},-"

        binding.btn.setOnClickListener {
            transaction()
        }
    }

    fun transaction() {
        if (binding.cbSelected.isChecked) {
            val user = UserSession.user

            rBody = JSONObject().apply {
                put("recipientName", user?.name)
                put("recipientPhoneNumber", user?.phoneNumber)
                put("shippingAddress", user?.address)
                put("totalPrice", total)
                put("items", items)
            }
        } else {
            if (binding.etName.text.isEmpty() || binding.etPhone.text.isEmpty() || binding.etAddress.text.isEmpty()) {
                return Helper.toast(this, "All field must be filled")
            }
            rBody = JSONObject().apply {
                put("recipientName", binding.etName.text.toString())
                put("recipientPhoneNumber", binding.etPhone.text.toString())
                put("shippingAddress", binding.etAddress.text.toString())
                put("totalPrice", total)
                put("items", items)
            }
        }

        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "transaction",
                    "POST",
                    token = TokenManager(this@CheckoutActivity).getToken(),
                    rBody = rBody.toString()
                )
            }

            val json = JSONObject(result)
            val code = json.getInt("code")
            val body = json.getString("body")

            if (code in 200..300) {
                CartManager.list.clear()
                lifecycleScope.launch {
                    updateVotoken()
                }
                Helper.toast(this@CheckoutActivity, "berhasil")
            } else {
                Helper.toast(this@CheckoutActivity, "berhasil")
            }
        }
    }

    fun updateVotoken() {
        lifecycleScope.launch {
            val votoken = (UserSession.user?.votoken!! + (total * 0.1 / 100).toInt())
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "me/votoken/update?value=${votoken}",
                    "PUT",
                    token = TokenManager(this@CheckoutActivity).getToken(),
                )
            }

            val json = JSONObject(result)
            val code = json.getInt("code")
            val body = json.getString("body")

            if (code in 200..300) {
                Helper.toast(this@CheckoutActivity, "Update token berhasil")
                finish()
                startActivity(Intent(this@CheckoutActivity, OrderHistoryActivity::class.java))
            } else {
                Helper.toast(this@CheckoutActivity, "Update token gagal")
            }
        }
    }
}