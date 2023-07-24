package com.example.splitpay_100

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SplitPage : AppCompatActivity() {
    lateinit var amountPerUser: String
    var isSplitClicked:Boolean=false
    var isSplitCreated:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split_page)

        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnSplit = findViewById<Button>(R.id.btnSplit)
        val btnReset = findViewById<Button>(R.id.btnReset)
        val place = findViewById<EditText>(R.id.tvPlace)
        val costSpent = findViewById<EditText>(R.id.tvCost)
        val userList = findViewById<Spinner>(R.id.usersList)
        var selectedUsername=""



        val selectedUsernames = intent.getStringArrayListExtra("selectedUsernames")
        val selectedUserIds = intent.getStringArrayListExtra("selectedUserIds")
        val allUsers = intent.getStringArrayListExtra("usernames")
        val allUsersIds = intent.getStringArrayListExtra("userIds")
        val Users=ArrayList<UserInfo>()

        if (allUsers != null && allUsersIds != null && allUsers.size == allUsersIds.size) {
            for (i in allUsers.indices) {
                val user = UserInfo(allUsersIds[i].toInt(), allUsers[i])
                Users.add(user)
            }
        }


        val BASE_URL = "http://127.0.0.1:3000/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        val adapter1 = CustomSpinnerAdapter(this@SplitPage, R.layout.customspinner, Users)
        userList.adapter=adapter1
        var  selectedUserId = 0

        userList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val selectedUser = adapter1.getItem(position)
                 selectedUserId = selectedUser?.id!!
                 selectedUsername = selectedUser.username.toString()

                // Now i got the selected User and its userId and username
                Log.d("SelectedUserId", selectedUserId.toString())
                Log.d("SelectedUsername", selectedUsername)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }



        btnSplit.setOnClickListener {

            if (!isSplitCreated) {

                if (costSpent.text.isNotEmpty() && selectedUsernames != null && place.text.isNotEmpty()) {

                    isSplitClicked = true
                    isSplitCreated=true

                    // Calculate the amount
                    val amount = (costSpent.text.toString().toDouble() / selectedUsernames.size)
                    amountPerUser = String.format("%.2f", amount)

                    // Create the ArrayList of Transaction objects
                    val transactions = selectedUsernames.map { username ->
                        Transaction(username, amountPerUser)
                    }

                    val splitDetails = SplitDetails(
                        Place_Name = place.text.toString(),
                        Created_by = selectedUsername,
                        Total_Cost = costSpent.text.toString().toDouble(),
                        Split_Amount = amount
                    )

                    apiService.createSplit(splitDetails).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {}
                        override fun onFailure(call: Call<Void>, t: Throwable) {}
                    })

                    // Set up the RecyclerView with transactions
                    val recyclerView = findViewById<RecyclerView>(R.id.listSplit)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    val adapter = TransactionAdapter(transactions)
                    recyclerView.adapter = adapter

                } else {
                    Toast.makeText(this, "Error! First enter the details", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                Toast.makeText(this, "Split has been already created!", Toast.LENGTH_SHORT).show()
            }
        }


        btnReset.setOnClickListener {

                if(place.text.isNotEmpty() || costSpent.text.isNotEmpty()){
                    if (!isSplitClicked) {
                        place.text.clear()
                        costSpent.text.clear()
                    }else{
                        Toast.makeText(this, "reset can be dn only before splitting!", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "First enter details !", Toast.LENGTH_SHORT).show()
                }
            }


        btnSave.setOnClickListener {

            if (isSplitClicked){

                val intent = Intent(this,TransactionPage::class.java)
                val amount = amountPerUser.toDouble()
                val created_by = selectedUserId
                val receivers = selectedUserIds?.map { it.toInt() } ?: emptyList()

                // Create a new transaction
                val transaction = Transactions(amount, created_by, receivers)

                apiService.createTransaction(transaction).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Log.d("POST Request", "Transaction created successfully")
                        } else {
                            Log.e("POST Request", "Response not successful: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("POST Request", "Error: ${t.message}")
                    }
                })
                startActivity(intent)
            }else{
                Toast.makeText(this, "first create split!", Toast.LENGTH_SHORT).show()
            }

        }


    }
}