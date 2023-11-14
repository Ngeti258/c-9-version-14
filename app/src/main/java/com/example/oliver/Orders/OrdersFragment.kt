package com.example.oliver

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OrdersFragment : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var OrdersList: MutableList<Product>
    private lateinit var auth: FirebaseAuth

    private lateinit var adapter: OrdersAdapter
    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var databaseRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_orders, container, false)
        database = FirebaseDatabase.getInstance()
        databaseRef = database.reference.child("orders")

        OrdersList = mutableListOf()
        adapter = OrdersAdapter(requireContext(), OrdersList)
        productsRecyclerView = rootView.findViewById(R.id.orders_recyclerview)
        productsRecyclerView.layoutManager = LinearLayoutManager(activity)
        productsRecyclerView.adapter = adapter

        // Attach listener to databaseRef
        databaseRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                OrdersList.clear()
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(Product::class.java)
                    if (order != null) {
                        OrdersList.add(order)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })

        return rootView
    }

    data class Product(
        val coffeeType: String? = null,
        val quantity: Double? = 0.0,
        val userId: String? = null,
        var productId: String? = null,
    )
}
