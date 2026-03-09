package com.example.voto.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.voto.R
import com.example.voto.data.HttpHandler
import com.example.voto.data.model.Camera
import com.example.voto.data.model.Category
import com.example.voto.databinding.ActivityHomeBinding
import com.example.voto.ui.adapter.CameraAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private var list: MutableList<Camera> = mutableListOf()
    private val categortList: MutableList<Category> = mutableListOf()

    private var categoryId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        showData()
        showCategoryData()

        binding.etSearch.addTextChangedListener {
            showData()
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val category = categortList[position]
                categoryId = category.id
                showData()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    fun showData() {
        lifecycleScope.launch {
            list.clear()
            val result = withContext(Dispatchers.IO) {
                if (binding.etSearch.text.toString().isNotEmpty()) {
                    HttpHandler().request(
                        "camera?search=${binding.etSearch.text.toString().lowercase()}"
                    )
//                } else if (categoryId != null) {
//                    HttpHandler().request("camera?categoryID=${categoryId}")
                } else {
                    HttpHandler().request(
                        "camera"
                    )
                }
            }

            val json = JSONObject(result)
            val code = json.getInt("code")
            val body = json.getString("body")

            if (code in 200..300) {
                val array = JSONArray(body)

                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)

                    list.add(
                        Camera(
                            item.getInt("id"),
                            item.getString("name"),
                            item.getString("resolution"),
                            item.getInt("price"),
                            item.getString("photo")
                        )
                    )
                }
                binding.rvCamera.adapter = CameraAdapter(list)
            }
        }
    }

    fun showCategoryData() {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "category"
                )
            }

            val json = JSONObject(result)
            val code = json.getInt("code")
            val body = json.getString("body")

            if (code in 200..300) {
                val array = JSONArray(body)
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)

                    categortList.add(
                        Category(
                            item.getInt("id"),
                            item.getString("name")
                        )
                    )
                }

                val adapter = ArrayAdapter(
                    this@HomeActivity,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    categortList
                )

                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)

                binding.spinner.adapter = adapter
            }
        }
    }
}