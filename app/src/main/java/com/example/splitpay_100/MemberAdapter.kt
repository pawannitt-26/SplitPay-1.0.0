package com.example.splitpay_100

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MembersAdapter(private val members: List<UserInfo>) : RecyclerView.Adapter<MembersAdapter.ViewHolder>() {

    private val selectedUsernames = mutableListOf<String>()
    private val selectedUserIds = mutableListOf<String>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val initialsTextView: TextView = view.findViewById(R.id.initials)
        val usernameTextView: TextView = view.findViewById(R.id.username)
        val btnAdd: Button = view.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.memberlistholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = members[position]
        val username = user.username
        val initial = username.take(1).uppercase()

        holder.initialsTextView.text = initial
        holder.usernameTextView.text = username

        // Access the userId from the User object
        val userId = user.id.toString()

        // Add or remove the username from the selectedUsernames list on "Add" button click
        holder.btnAdd.setOnClickListener {
            val isSelected = selectedUsernames.contains(username)
            if (isSelected) {
                selectedUsernames.remove(username)
                selectedUserIds.remove(userId)
                holder.btnAdd.text = "+ Add"
            } else {
                selectedUsernames.add(username)
                selectedUserIds.add(userId)
                holder.btnAdd.text = "-Remove"
            }
        }
    }

    override fun getItemCount(): Int {
        return members.size
    }

    fun getSelectedUsernames(): List<String> {
        return selectedUsernames.toList()
    }

    fun getSelectedUserIds(): List<String> {
        return selectedUserIds.toList()
    }

}
class CustomSpinnerAdapter(context: Context, resource: Int, items: List<UserInfo>) :
    ArrayAdapter<UserInfo>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.customspinner, parent, false)

        val textViewUsername = view.findViewById<TextView>(R.id.userName)
        val textViewUserId = view.findViewById<TextView>(R.id.userId)

        val user = getItem(position)
        if (user != null) {
            textViewUsername.text = user.username
            textViewUserId.text = user.id.toString()
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}

