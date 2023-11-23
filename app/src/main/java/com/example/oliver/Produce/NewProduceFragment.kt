package com.example.oliver.Produce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.oliver.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

data class Product(
    var productId: String?,
    val userId: String,
    val name: String,
    val quantity: Double,
    val amount: Double,
    val timestamp: Long
) {

    constructor() : this(null, "", "", 0.0, 0.0, 0L)
}




class NewProduceFragment : Fragment() {

    private lateinit var autoCompleteTextViewFarmerName: AutoCompleteTextView
    private lateinit var editTextQuantity: EditText
    private lateinit var textViewAmount: TextView
    private lateinit var saveButton: Button
    private lateinit var calcButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var usersRef: DatabaseReference
    private lateinit var productsRef: DatabaseReference

    private val farmerNames = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_produce, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        usersRef = FirebaseDatabase.getInstance().reference.child("users")
        productsRef = FirebaseDatabase.getInstance().reference.child("products")

        autoCompleteTextViewFarmerName = view.findViewById(R.id.autoCompleteTextViewFarmerName)
        editTextQuantity = view.findViewById(R.id.editTextQuantity)
        textViewAmount = view.findViewById(R.id.textViewAmount)
        calcButton = view.findViewById(R.id.calcButton)
        saveButton = view.findViewById(R.id.saveButton)

        setupAutoCompleteAdapter()

        calcButton.setOnClickListener {
            calculateAndDisplayAmount()
        }

        saveButton.setOnClickListener {
            saveProduct()
        }
    }

    private fun setupAutoCompleteAdapter() {
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, farmerNames)
        autoCompleteTextViewFarmerName.setAdapter(adapter)
        autoCompleteTextViewFarmerName.threshold = 1 // Set the threshold to 1 character

        // Retrieve farmer names from the database
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val userName = userSnapshot.child("name").getValue(String::class.java)
                    userName?.let {
                        farmerNames.add(it)
                    }
                }
                adapter.notifyDataSetChanged() // Notify the adapter that data has changed
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    private fun calculateAndDisplayAmount() {
        val quantityStr = editTextQuantity.text.toString()
        if (quantityStr.isNotEmpty()) {
            val quantity = quantityStr.toDouble()
            val amount = quantity * 100 // Each kilogram is worth 100 shillings
            textViewAmount.text = "Amount: $$amount"
        } else {
            // Handle empty quantity input if needed
            textViewAmount.text = "Amount: $0.00"
        }
    }

    private fun saveProduct() {
        val userId = auth.currentUser?.uid
        val name = autoCompleteTextViewFarmerName.text.toString().trim()
        val quantityStr = editTextQuantity.text.toString().trim()

        if (userId != null && name.isNotEmpty() && quantityStr.isNotEmpty()) {
            // Check if the entered name exists in the farmerNames list
            if (farmerNames.contains(name)) {
                val quantity = quantityStr.toDouble()
                val amount = quantity * 100 // Each kilogram is worth 100 shillings
                val timestamp = System.currentTimeMillis()

                val product = Product(null, userId, name, quantity, amount, timestamp)
                val productKey = productsRef.push().key

                if (productKey != null) {
                    product.productId = productKey  // Set the productId after push()
                    productsRef.child(productKey).setValue(product)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Product saved successfully
                                clearInputFields()
                                Toast.makeText(context, "Product successfully saved.", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                // Handle the error
                            }
                        }
                }
            } else {
                // The entered name is not a farmer in the database
                // Show an error message to the user
                autoCompleteTextViewFarmerName.error = "Not a valid farmer"
            }
        } else {
            // Handle the case where userId is null or name/quantity is empty
        }
    }


    private fun clearInputFields() {
        autoCompleteTextViewFarmerName.text.clear()
        editTextQuantity.text.clear()
        textViewAmount.text = "Amount: $0.00"
    }
}