package com.example.oliver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var productsRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!
        productsRef = FirebaseDatabase.getInstance().reference.child("products")

        // Fetch products that belong to the current user from Firebase
        fetchUserProducts()
    }

    private fun fetchUserProducts() {
        productsRef.orderByChild("userId").equalTo(currentUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val productList = mutableListOf<Product>()

                    for (productSnapshot in dataSnapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)

                        if (product != null) {
                            productList.add(product)
                        }
                    }

                    setupRecyclerView(productList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                }
            })
    }

    private fun setupRecyclerView(productList: List<Product>) {
        if (isAdded) { // Check if the fragment is attached
            recyclerView = requireView().findViewById(R.id.history_recyclerview)
            historyAdapter = HistoryAdapter(productList)
            recyclerView.adapter = historyAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
