package com.example.oliver

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class OrdersAdapter(
    private val context: Context,
    private var ordersList: MutableList<OrdersFragment.Product>
) : RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.orders_layout, parent, false)
        return OrdersViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order = ordersList[position]

        // Fetch the customer's name based on the UID
        val userUid = order.userId
        val usersRef = FirebaseDatabase.getInstance().getReference("users")

        if (userUid != null) {
            usersRef.child(userUid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.getValue(User::class.java)
                        val customerName = user?.name ?: "Unknown Customer"
                        holder.customerNameTV.text = customerName.uppercase()

                        holder.coffeeTypeTV.text = "${order.coffeeType}"
                        holder.coffeeQuantityTV.text = " ${order.quantity}"

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    holder.customerNameTV.text = "Failed to load name"
                }
            })
        }

        holder.processButton.setOnClickListener {
            val order = ordersList[position]

            // Get a reference to your Firebase Database
            val database = FirebaseDatabase.getInstance()

            // Reference to the "history" table
            val historyRef = database.getReference("history")

            // Reference to the "orders" table
            val ordersRef = database.getReference("orders")

            // Push the order to the "history" section in the database
            val orderId = historyRef.push().key
            if (orderId != null) {
                val orderToMove = order.copy(productId = orderId) // Create a copy with a new order ID
                historyRef.child(orderId).setValue(orderToMove)

                // Remove the order from the "orders" table
                order.productId?.let {
                    ordersRef.child(it).removeValue()
                }

                // Display a success message
                // Add any additional processing if needed
            }
        }
    }

    override fun getItemCount() = ordersList.size

    inner class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val customerNameTV: TextView = itemView.findViewById(R.id.customerNameTV)
        val coffeeQuantityTV: TextView = itemView.findViewById(R.id.fertilizerQuantityGradeTV)
        val coffeeTypeTV: TextView = itemView.findViewById(R.id.fertilizerTypeTV)
        val processButton: Button = itemView.findViewById(R.id.process_order)
    }
}
