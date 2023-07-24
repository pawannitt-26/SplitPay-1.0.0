package com.example.splitpay_100

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class UserBalanceAdapter(private val userBalances: List<UserDetails>) : RecyclerView.Adapter<UserBalanceAdapter.UserBalanceViewHolder>() {

    companion object {
        var you_owes = 0.0
        var you_lent = 0.0
        fun getUserTotal(): Pair<Double, Double> {
            return Pair(you_owes, you_lent)
        }
    }


    class UserBalanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userIdTextView: TextView = itemView.findViewById(R.id.userName)
        val balanceTextView: TextView = itemView.findViewById(R.id.amount)
        val settleButton:Button=itemView.findViewById(R.id.btnSettle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserBalanceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.transactionlistholder, parent, false)
        return UserBalanceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserBalanceViewHolder, position: Int) {
        val userBalance = userBalances[position]
        holder.userIdTextView.text = "${userBalance.username}"


        // Check the balance value and set the appropriate text and visibility
        when {
            userBalance.balance == 0.0 -> {
                holder.balanceTextView.text = "${userBalance.balance}"
                holder.settleButton.text="Settled"
                holder.settleButton.setTextColor(Color.RED)
                holder.settleButton.setBackgroundColor(1)
            }
            userBalance.balance > 0.0 -> {
                holder.balanceTextView.text =  "${userBalance.balance}"
                holder.settleButton.text="Owes You"
                holder.settleButton.setTextColor(Color.GREEN)
                holder.settleButton.setBackgroundColor(1)
                you_lent+=userBalance.balance
            }
            else -> {
                holder.balanceTextView.text = "${userBalance.balance}"
                holder.settleButton.text="Settle"
                you_owes+=userBalance.balance
                }
            }



    }

    override fun getItemCount(): Int {
        return userBalances.size
    }



}