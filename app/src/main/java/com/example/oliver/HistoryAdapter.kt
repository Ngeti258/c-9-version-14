package com.example.oliver

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private val productList: List<Product>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val produceDetails: TextView = itemView.findViewById(R.id.produceDetails)
        val receiveTime: TextView = itemView.findViewById(R.id.receiveTime)


    }

    // Create new ViewHolders (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_layout, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a ViewHolder (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        holder.produceDetails.text = " ${product.quantity}kgs for {${product.amount}}"
        holder.receiveTime.text = " ${product.timestamp?.let { formatTimestamp(it) }}"



    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return productList.size
    }

    // Helper function to format timestamp to dd-mm-yyyy
    private fun formatTimestamp(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }
}
