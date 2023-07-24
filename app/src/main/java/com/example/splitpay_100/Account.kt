package com.example.splitpay_100

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Account : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val gotoHome = findViewById<Button>(R.id.gotoHome)
        gotoHome.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        val nameOfUser = findViewById<TextView>(R.id.name)
        val initialLetter = findViewById<TextView>(R.id.letter)
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")

        if (username != null) {
            nameOfUser.text =username.uppercase()
            initialLetter.text=username.substring(0, 1).uppercase()
        }


    }
}