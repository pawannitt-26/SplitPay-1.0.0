package com.example.splitpay_100

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SplitHistoryAdapter(private val splits: List<Split>) : RecyclerView.Adapter<SplitHistoryAdapter.SplitViewHolder>() {

    class SplitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val createdBy: TextView = itemView.findViewById(R.id.createdBy)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val totalCost: TextView = itemView.findViewById(R.id.totalCost)
        val splitAmount: TextView = itemView.findViewById(R.id.splitAmount)
        val  name: TextView = itemView.findViewById(R.id.name)
        val  splitId: TextView = itemView.findViewById(R.id.splitId)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SplitViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.splithistoryadapter, parent, false)
        return SplitViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SplitViewHolder, position: Int) {
        val split = splits[position]
        holder.createdBy.text = "Created_By: "+split.Created_by
        holder.createdAt.text = "Created_At:"+ split.Created_at
        holder.totalCost.text = "Total Amount: " + split.Total_Cost.toString()
        holder.splitAmount.text = "Splitted Amount: " + split.Split_Amount.toString()
        holder.name.text = split.Place_Name
        holder.splitId.text = split.id.toString()

    }

    override fun getItemCount(): Int {
        return splits.size
    }
}