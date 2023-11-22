package com.example.oliver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class BalanceFragment : Fragment() {

    private lateinit var produceReturnsTextView: TextView
    private lateinit var fertilizerPriceTextView: TextView
    private lateinit var systemMaintenanceTextView: TextView
    private lateinit var annualFeeTextView: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var database: FirebaseDatabase
    private lateinit var produceDatabaseRef: DatabaseReference
    private lateinit var ordersDatabaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        currentUser = auth.currentUser!!

        database = FirebaseDatabase.getInstance()
        produceDatabaseRef = database.reference.child("products")
        ordersDatabaseRef = database.reference.child("orders")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_balance, container, false)

        produceReturnsTextView = view.findViewById(R.id.produceReturns)
        fertilizerPriceTextView = view.findViewById(R.id.fertilizerPrice)
        systemMaintenanceTextView = view.findViewById(R.id.systemMaintanance)
        annualFeeTextView = view.findViewById(R.id.annualFee)

        calculateProduceReturns()
        fetchFertilizerPrice()
        setSystemMaintenanceAmount()
        setAnnualFeeAmount()

        return view
    }

    private fun calculateProduceReturns() {
        produceDatabaseRef.orderByChild("userId").equalTo(currentUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var totalProduceReturns = 0

                    for (produceSnapShot in snapshot.children) {
                        val produceAmount = produceSnapShot.child("amount").getValue(Int::class.java) ?: 0
                        totalProduceReturns += produceAmount
                    }

                    produceReturnsTextView.text = "Produce Return: $totalProduceReturns"
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error if needed
                }
            })
    }

    private fun fetchFertilizerPrice() {
        ordersDatabaseRef.orderByChild("userId").equalTo(currentUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var totalFertilizerPrice = 0

                    for (orderSnapShot in snapshot.children) {
                        val orderAmount = orderSnapShot.child("amount").getValue(Int::class.java) ?: 0
                        totalFertilizerPrice += orderAmount
                    }

                    fertilizerPriceTextView.text = "Fertilizer Price: $totalFertilizerPrice"
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error if needed
                }
            })
    }

    private fun setSystemMaintenanceAmount() {
        systemMaintenanceTextView.text = "System Maintenance: 2000"
    }

    private fun setAnnualFeeAmount() {
        annualFeeTextView.text = "Annual Fee: 500"
    }
}
