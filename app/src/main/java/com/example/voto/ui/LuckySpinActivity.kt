package com.example.voto.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.voto.R
import com.example.voto.data.HttpHandler
import com.example.voto.data.TokenManager
import com.example.voto.data.UserSession
import com.example.voto.databinding.ActivityLuckySpinBinding
import com.example.voto.util.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LuckySpinActivity : AppCompatActivity() {
    private var _binding: ActivityLuckySpinBinding? = null
    private val binding get() = _binding!!
    private var isSpinning = false

    var rewardValue = 0

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

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.needle.post {
            binding.needle.pivotX = binding.needle.width / 2f

            val density = resources.displayMetrics.density
            binding.needle.pivotY = 90f * density
        }

        binding.needle.setOnClickListener {
            if (isSpinning) return@setOnClickListener

            val user = UserSession.user
            val cost = 2200

            if (user?.votoken!! < cost) {
                Helper.toast(this, "Votoken must be higher")
                return@setOnClickListener
            }

            user?.votoken -= cost
            binding.tvVotoken.text = UserSession.user?.votoken.toString()
            isSpinning = true
            val rewardCount = 6
            val anglePerReward = 360f / rewardCount
            val randomRewardIndex = (0 until rewardCount).random()

            val currentRotation = binding.needle.rotation
            val targetLandingAngle = randomRewardIndex * anglePerReward
            val totalRotation = currentRotation + 1080f + ((targetLandingAngle - (currentRotation % 360f) + 360f) % 360f)

            binding.needle.animate()
                .rotation(totalRotation)
                .setDuration(4000)
                .setInterpolator(DecelerateInterpolator())
                .withEndAction {
                    isSpinning = false
                    handleReward(randomRewardIndex)
                }
                .start()
        }
    }

    fun updateVotoken() {
        lifecycleScope.launch {
            val votoken = (UserSession.user?.votoken!! + rewardValue)
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "me/votoken/update?value=${votoken}",
                    "PUT",
                    token = TokenManager(this@LuckySpinActivity).getToken(),
                )
            }

            val json = JSONObject(result)
            val code = json.getInt("code")
            val body = json.getString("body")

            if (code in 200..300) {
                Helper.toast(this@LuckySpinActivity, "Update token berhasil")
            } else {
                Helper.toast(this@LuckySpinActivity, "Update token gagal")
            }
        }
    }

    private fun handleReward(index: Int) {
        var message = ""

        when (index) {
            0 -> {
                rewardValue = 3000
                message = "3000"
                UserSession.user?.votoken += rewardValue
                binding.tvVotoken.text = UserSession.user?.votoken.toString()
                updateVotoken()
            }
            1 -> {
                rewardValue = (15000 until 30000).random()
                message = "jackpot"
                UserSession.user?.votoken += rewardValue
                binding.tvVotoken.text = UserSession.user?.votoken.toString()
                updateVotoken()
            }
            2 -> {
                rewardValue = 2500
                message = "2500"
                UserSession.user?.votoken += rewardValue
                binding.tvVotoken.text = UserSession.user?.votoken.toString()
                updateVotoken()
            }
            3 -> {
                message = "draw again"
                binding.needle.callOnClick()
            }
            4 -> {
                rewardValue = 1500
                message = "1500"
                UserSession.user?.votoken += rewardValue
                binding.tvVotoken.text = UserSession.user?.votoken.toString()
                updateVotoken()
            }
            5 -> {
                rewardValue = 0
                message = "try again"
                UserSession.user?.votoken += rewardValue
                binding.tvVotoken.text = UserSession.user?.votoken.toString()
            }
        }

        Helper.toast(this, message)
    }
}
