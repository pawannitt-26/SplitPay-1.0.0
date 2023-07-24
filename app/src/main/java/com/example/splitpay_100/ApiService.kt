package com.example.splitpay_100

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("users")
    fun getUsers(): Call<List<UserInfo>>

    @POST("signup")
    fun signup(@Body user: User): Call<Void>

    @POST("/login")
    fun loginUser(@Body request: User): Call<UserResponse>

    @POST("/splits")
    fun createSplit(@Body splitDetails: SplitDetails): Call<Void>

    @POST("transactions")
    fun createTransaction(@Body transaction: Transactions): Call<Void>


    @GET("/transactions/{userId}")
    fun getTransactionsForUser(@Path("userId") userId: Int): Call<List<TransactionDetails>>

    @GET("/splitshistory")
    fun getSplits(): Call<List<Split>>

}