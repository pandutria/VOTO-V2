package com.example.voto.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.voto.R
import com.example.voto.data.HttpHandler
import com.example.voto.data.TokenManager
import com.example.voto.databinding.ActivityLoginBinding
import com.example.voto.util.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.etEmail.setText("mahdi@gmail.com")
        binding.etPassword.setText("1234")

        binding.btn.setOnClickListener {
            if (binding.etEmail.text.isEmpty() || binding.etPassword.text.isEmpty()) {
                Helper.toast(this, "All field must be filled")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val rBody = JSONObject().apply {
                    put("email", binding.etEmail.text.toString())
                    put("password", binding.etPassword.text.toString())
                }
                val result = withContext(Dispatchers.IO) {
                    HttpHandler().request(
                        "auth",
                        "POST",
                        rBody = rBody.toString()
                    )
                }

                val json = JSONObject(result)
                val code = json.getInt("code")
                val body = json.getString("body")

                if (code in 200..300) {
                    TokenManager(this@LoginActivity).saveToken(body)
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                } else {
                    Helper.toast(this@LoginActivity, body)
                }

            }
        }
    }


}