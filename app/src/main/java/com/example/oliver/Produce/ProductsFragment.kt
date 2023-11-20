package com.example.oliver.Produce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oliver.Produce.ProductsAdapter
import com.example.oliver.Product
import com.example.oliver.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProductsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productsAdapter: ProductsAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var productsRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_products, container, false)
        recyclerView = view.findViewById(R.id.orders_recyclerview) // Replace with your RecyclerView ID
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        productsRef = FirebaseDatabase.getInstance().reference.child("products")

        // Fetch products from Firebase
        fetchProducts()
    }

    private fun fetchProducts() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            // Fetch user role and name from the database
            val usersRef = FirebaseDatabase.getInstance().reference.child("users")
            usersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {

                    val userName = userSnapshot.child("name").getValue(String::class.java)

// Check if the user is a clerk
                    val isClerk = userName == "clerk"

                    productsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val productList = mutableListOf<Product>()

                            for (productSnapshot in dataSnapshot.children) {
                                val product = productSnapshot.getValue(Product::class.java)

                                if (product != null) {
                                    // If the user is a clerk, display all products
                                    // If the user is not a clerk, only display products matching the user's name
                                    if (isClerk || (product.userId == userId && product.name == userName)) {
                                        productList.add(product)
                                    }
                                }
                            }

                            setupRecyclerView(productList)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle errors
                        }
                    })
                }

                    override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                }
            })
        }
    }

    private fun setupRecyclerView(productList: List<Product>) {
        productsAdapter = ProductsAdapter(productList)
        recyclerView.adapter = productsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
