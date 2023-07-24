package com.example.splitpay_100

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MembersPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members_page)


        val recyclerViewMembers = findViewById<RecyclerView>(R.id.listMembers)
        recyclerViewMembers.layoutManager = LinearLayoutManager(this)

        val BASE_URL = "http://127.0.0.1:3000/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        val usernameList = ArrayList<String>()
        val userIdList = ArrayList<String>()

        apiService.getUsers().enqueue(object : Callback<List<UserInfo>> {
            override fun onResponse(call: Call<List<UserInfo>>, response: Response<List<UserInfo>>) {
                if (response.isSuccessful) {
                    val Users = response.body()
                    if (Users != null) {
                        for (user in Users){
                            usernameList.add(user.username)
                            userIdList.add(user.id.toString())
                        }
                        Log.d("UsersList", Users.toString())
                    }
                    val adapter = Users?.let { it1 -> MembersAdapter(it1) }
                    recyclerViewMembers.adapter = adapter

                } else {
                    Log.e("API Call", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {
                Log.e("API Call", "Error: ${t.message}")
            }
        })



        val btnNxt = findViewById<Button>(R.id.btnNext)
        btnNxt.setOnClickListener {
            val adapter = recyclerViewMembers.adapter as MembersAdapter
            val selectedUsernames = adapter.getSelectedUsernames()
            val selectedUserIds=adapter.getSelectedUserIds()

            val intent = Intent(this, SplitPage::class.java)
            intent.putStringArrayListExtra("selectedUsernames", ArrayList(selectedUsernames))
            intent.putStringArrayListExtra("selectedUserIds", ArrayList(selectedUserIds))
            intent.putStringArrayListExtra("usernames", ArrayList(usernameList))
            intent.putStringArrayListExtra("userIds", ArrayList(userIdList))
            startActivity(intent)
        }


    }

}