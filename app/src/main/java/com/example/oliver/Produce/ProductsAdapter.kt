package com.example.oliver.Produce

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oliver.Product
import com.example.oliver.R  // Replace with the actual package name and R class of your app
import java.text.SimpleDateFormat
import java.util.*

class ProductsAdapter(private val productList: List<Product>) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    // Define a ViewHolder to hold the views for each item in the RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val customerNameTV: TextView = itemView.findViewById(R.id.customerNameTV)
        val coffeePriceTV: TextView = itemView.findViewById(R.id.coffeePriceTV)
        val orderTime: TextView = itemView.findViewById(R.id.orderTime)
        val processOrderButton: Button = itemView.findViewById(R.id.process_order)
    }

    // Create new ViewHolders (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_layout, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a ViewHolder (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = productList[position]

        // Set the data to the views
        holder.customerNameTV.text = currentItem.name
        holder.coffeePriceTV.text = "ksh ${currentItem.amount} per kilogram"
        holder.orderTime.text = "Order Time: ${formatTimestamp(currentItem.timestamp)}"

        // Set a click listener for the process order button if needed
        holder.processOrderButton.setOnClickListener {
            // Handle button click
            // You can use position to get the clicked item
        }
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
