package com.example.voto.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.voto.R
import com.example.voto.data.HttpHandler
import com.example.voto.data.TokenManager
import com.example.voto.data.model.History
import com.example.voto.data.model.HistoryOrder
import com.example.voto.databinding.ActivityOrderHistoryBinding
import com.example.voto.ui.adapter.HistoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class OrderHistoryActivity : AppCompatActivity() {
    private var _binding: ActivityOrderHistoryBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        showData()
    }

    fun showData() {
        val list: MutableList<History> = mutableListOf()
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "me/transaction",
                    token = TokenManager(this@OrderHistoryActivity).getToken()
                )
            }

            val json = JSONObject(result)
            val code = json.getInt("code")
            val body = json.getString("body")

            if (code in 200..300) {
                val array = JSONArray(body)

                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)

                    val transactionList: MutableList<HistoryOrder> = mutableListOf()
                    val transactions = item.getJSONArray("transactions")
                    for (trx in 0 until transactions.length()) {
                        val transaction = transactions.getJSONObject(trx)

                        transactionList.add(
                            HistoryOrder(
                                transaction.getString("name"),
                                transaction.getInt("qty"),
                                transaction.getInt("subtotal"),
                            )
                        )
                    }

                    list.add(History(
                        item.getString("id"),
                        item.getString("status"),
                        item.getInt("totalPrice"),
                        transactionList
                    ))
                }

                binding.rvHistory.adapter = HistoryAdapter(list)
            }
        }
    }
}