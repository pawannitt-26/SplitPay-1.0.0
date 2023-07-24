package com.example.splitpay_100

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TransactionPage : AppCompatActivity() {

    lateinit var usersList :List<UserInfo>
    lateinit var userIdToUsernameMap: Map<Int, String>

    val BASE_URL = "http://127.0.0.1:3000/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_page)


        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "")?.toInt()
        if (userId != null) {
            fetchTransactionsAndCalculateAmounts(userId)
        }


        val homeButton = findViewById<Button>(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }




        apiService.getUsers().enqueue(object : Callback<List<UserInfo>> {
            override fun onResponse(call: Call<List<UserInfo>>, response: Response<List<UserInfo>>) {
                if (response.isSuccessful) {
                    val Users = response.body()
                    if (Users != null) {
                        usersList=Users
                        userIdToUsernameMap = usersList.associateBy { it.id }.mapValues { it.value.username }
                    }
                }
            }
            override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {
                Log.e("API Call", "Error: ${t.message}")
            }
        })

    }



   private fun fetchTransactionsAndCalculateAmounts(userId: Int) {

        apiService.getTransactionsForUser(userId).enqueue(object : Callback<List<TransactionDetails>> {
            override fun onResponse(call: Call<List<TransactionDetails>>, response: Response<List<TransactionDetails>>) {
                if (response.isSuccessful) {
                    val transactions = response.body()
                    if (transactions != null) {

                        val userBalances = calculateAmounts(userId, transactions)

                        val recyclerView: RecyclerView = findViewById(R.id.listTransaction)
                        recyclerView.layoutManager = LinearLayoutManager(this@TransactionPage)
                        recyclerView.adapter = UserBalanceAdapter(userBalances)
                    }else{
                        Toast.makeText(this@TransactionPage, "No transaction done yet!", Toast.LENGTH_SHORT).show()
                    }
                } else {

                    Log.e("GET Request", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<TransactionDetails>>, t: Throwable) {

                Log.e("GET Request", "Error: ${t.message}")
            }
        })
    }



    private fun calculateAmounts(userId: Int, transactions: List<TransactionDetails>): List<UserDetails> {
        val userOwesMap = mutableMapOf<Int, Double>().withDefault { 0.0 }
        val userSettledMap = mutableMapOf<Int, Double>().withDefault { 0.0 }

        // Calculate the total amount that the current user has already paid to others
        for (transaction in transactions) {
            if (transaction.createdBy == userId) {
                for (receiverId in transaction.receivers) {
                    userSettledMap[receiverId] = userSettledMap.getValue(receiverId) + transaction.amount
                }
            }
        }

        // Calculate the total amount that others have paid to the current user
        for (transaction in transactions) {
            if (transaction.receivers.contains(userId)) {
                val createdBy = transaction.createdBy
                userOwesMap[createdBy] = userOwesMap.getValue(createdBy) + transaction.amount
            }
        }

        for ((user, amountSettled) in userSettledMap) {
            val amountOwed = userOwesMap.getValue(user)
            val finalBalance = amountSettled - amountOwed

            if (finalBalance > 0) {
                // Others owe money to the current user (Pawan)
                Log.d("User Settled", "User $user owes $finalBalance to User $userId")
            } else if (finalBalance < 0) {
                // The current user (Pawan) owes money to others
                Log.d("User Owes", "User $userId owes ${-finalBalance} to User $user")
            } else {
                // No amount owed or settled
                Log.d("Balance", "User $userId and User $user are settled")
            }
        }

        // Create a list of UserBalance objects from the userSettledMap and userOwesMap
        val userBalances = mutableListOf<UserBalance>()

        for ((user, amountSettled) in userSettledMap) {
            val amountOwed = userOwesMap.getValue(user)
            val finalBalance = amountSettled - amountOwed
            userBalances.add(UserBalance(user, finalBalance))
        }

        val userDetailsList = mutableListOf<UserDetails>()

        for (userBalance in userBalances) {
            val username = userIdToUsernameMap[userBalance.userId] ?: "" // Get the username for the user ID
            userDetailsList.add(UserDetails(username, userBalance.balance))
        }

        return userDetailsList

    }




}