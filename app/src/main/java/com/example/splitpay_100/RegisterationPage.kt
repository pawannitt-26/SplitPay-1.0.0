package com.example.splitpay_100

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterationPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeration_page)

        val btnCreate = findViewById<Button>(R.id.btnLogin)
        val loginText = findViewById<TextView>(R.id.loginText)
        val etEmail = findViewById<EditText>(R.id.loginEmail)
        val etPassword = findViewById<EditText>(R.id.loginPassword)


        loginText.setOnClickListener {
            val intent = Intent(this,LoginPage::class.java)
            startActivity(intent)
        }

        val BASE_URL = "http://127.0.0.1:3000/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)



        btnCreate.setOnClickListener {
            val username = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {

                val user = User(username = username, password = password)

                apiService.signup(user).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            val intent = Intent(this@RegisterationPage, LoginPage::class.java)
                            startActivity(intent)
                            Toast.makeText(this@RegisterationPage, "Signup Successful!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@RegisterationPage, "Account already exists !", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@RegisterationPage, "Network error. Please check your internet connection.", Toast.LENGTH_SHORT).show()
                    }
                })

            } else {
                Toast.makeText(this, "Please enter valid credentials", Toast.LENGTH_SHORT).show()
            }
        }


    }
}