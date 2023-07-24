package com.example.splitpay_100

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etLoginEmail = findViewById<EditText>(R.id.loginEmail)
        val etLoginPassword = findViewById<EditText>(R.id.loginPassword)


        val BASE_URL = "http://127.0.0.1:3000/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)


        btnLogin.setOnClickListener {
            val username = etLoginEmail.text.toString()
            val password = etLoginPassword.text.toString()

            if (etLoginEmail.text.isNotEmpty() && etLoginPassword.text.isNotEmpty()){
                val user = User(username = username, password = password)
                apiService.loginUser(user).enqueue(object : retrofit2.Callback<UserResponse> {
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if (response.isSuccessful) {
                            val userResponse = response.body()
                             if (userResponse != null) {
                                 val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                                 sharedPreferences.edit().apply {
                                     putString("username", userResponse.username)
                                     putString("userId",userResponse.id.toString())
                                     apply()
                                 }
                             }
                            val intent = Intent(this@LoginPage,MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this@LoginPage, "Login Successful!", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(this@LoginPage, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Toast.makeText(this@LoginPage, "Network error. Please check your internet connection.", Toast.LENGTH_SHORT).show()
                    }
                })
            }else{
                Toast.makeText(this, "please enter valid credentials", Toast.LENGTH_SHORT).show()
            }
        }

    }

}