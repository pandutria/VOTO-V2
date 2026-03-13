package com.example.voto.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.voto.R
import com.example.voto.data.UserSession
import com.example.voto.databinding.ActivityLuckySpinBinding

class LuckySpinActivity : AppCompatActivity() {
    private var _binding: ActivityLuckySpinBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityLuckySpinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvVotoken.text = UserSession.user?.votoken.toString()

        val reward = 6
        val angle = 360 / reward

        val randomReward = (0 until reward).random()
        val target = 1080 + (randomReward * angle)

        binding.needle.setOnClickListener {
            binding.wheel.animate()
                .rotationBy(target.toFloat())
                .setDuration(3000)
                .start()
        }
    }
}