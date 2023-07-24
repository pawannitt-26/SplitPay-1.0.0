package com.example.splitpay_100

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val splitPage = findViewById<CardView>(R.id.splitPage)
        val transactionPage = findViewById<CardView>(R.id.transactionPage)
        val welcomeAddress = findViewById<TextView>(R.id.welcomeView)
        val accountInfo = findViewById<ImageView>(R.id.accountInfo)
        val oweView = findViewById<TextView>(R.id.oweView)
        val lentView = findViewById<TextView>(R.id.lentView)
        val btnHistory = findViewById<Button>(R.id.btnHistory)

        btnHistory.setOnClickListener {
            val intent = Intent(this,SplitHistory::class.java)
            startActivity(intent)
        }

        accountInfo.setOnClickListener {
            val intent = Intent(this,Account::class.java)
            startActivity(intent)
        }

        splitPage.setOnClickListener {
            val intent = Intent(this,MembersPage::class.java)
            startActivity(intent)
        }

        transactionPage.setOnClickListener {
            val intent = Intent(this,TransactionPage::class.java)
            startActivity(intent)
        }


        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        welcomeAddress.text ="Hello,"+username+" !"

        val (owesAmount, lentAmount) = UserBalanceAdapter.getUserTotal()

            oweView.text=owesAmount.toString()
            lentView.text=lentAmount.toString()


    }
}