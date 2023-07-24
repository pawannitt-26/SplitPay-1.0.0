package com.example.splitpay_100

data class User(
    var username:String,
    val password:String
)
data class UserInfo(
    var id:Int,
    var username:String
)

data class UserResponse(
    val id:Int,
    val username:String,
    val password:String
)

data class Transaction(
    val username: String,
    val amount: String
    )

data class SplitDetails(
    val Place_Name: String,
    val Created_by: String,
    val Total_Cost: Double,
    val Split_Amount: Double
)

data class Transactions(
    val amount: Double,
    val created_by: Int,
    val receivers: List<Int>
)

data class TransactionDetails(
    val transactionId: Int,
    val amount: Double,
    val createdBy: Int,
    val receivers: List<Int>
)

data class UserBalance(
    val userId: Int,
    val balance: Double
    )
data class UserDetails(
    val username: String,
    val balance: Double
)
data class Split(
    val id: Int,
    val Place_Name: String,
    val Created_at: String,
    val Created_by: String,
    val Total_Cost: Double,
    val Split_Amount: Double
)



