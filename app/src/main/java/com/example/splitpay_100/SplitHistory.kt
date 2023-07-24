package com.example.splitpay_100

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SplitHistory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split_history)

        val btnHome = findViewById<Button>(R.id.btnHome)
        btnHome.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        val BASE_URL = "http://127.0.0.1:3000/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        apiService.getSplits().enqueue(object : Callback<List<Split>> {
            override fun onResponse(call: Call<List<Split>>, response: Response<List<Split>>) {
                if (response.isSuccessful) {
                    val splits = response.body()
                    if (splits != null) {
                        // Set up the RecyclerView
                        val recyclerView: RecyclerView = findViewById(R.id.splitHistory)
                        recyclerView.layoutManager = LinearLayoutManager(this@SplitHistory)
                        recyclerView.adapter = SplitHistoryAdapter(splits)
                    }
                } else {
                    Log.e("GET Request", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Split>>, t: Throwable) {
                Log.e("GET Request", "Error: ${t.message}")
            }
        })

    }
}