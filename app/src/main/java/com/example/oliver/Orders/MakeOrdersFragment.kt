package com.example.oliver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class MakeOrdersFragment : Fragment() {
    private lateinit var coffeeTypeDropdown: AutoCompleteTextView
    private lateinit var quantityEditText: EditText
    private lateinit var userId: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_make_orders, container, false)

        coffeeTypeDropdown = view.findViewById(R.id.dropdown_field)
        storage = FirebaseStorage.getInstance()

        val coffeeTypeDropdownLayout = view.findViewById<TextInputLayout>(R.id.dropdown_field2)
        quantityEditText = view.findViewById(R.id.edt_quantity)

        val postButton = view.findViewById<Button>(R.id.btn_post)

        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        val name = auth.currentUser?.displayName

        val coffeeTypes = listOf("Potash", "Nitrogen", "Phosphoric Acid", "Manure")
        val adapter = ArrayAdapter(requireContext(), R.layout.fertilizer_list_view, coffeeTypes)
        coffeeTypeDropdown.setAdapter(adapter)
        coffeeTypeDropdownLayout.setEndIconOnClickListener {
            coffeeTypeDropdown.setText("")
        }
        postButton.setOnClickListener {
            val coffeeType = coffeeTypeDropdown.text.toString()
            val quantity = quantityEditText.text.toString().toDoubleOrNull()

            if (coffeeType.isBlank()) {
                coffeeTypeDropdownLayout.error = "Please select a Coffee type"
                return@setOnClickListener
            } else {
                coffeeTypeDropdownLayout.error = null
            }

            if ((quantity == null) || (quantity <= 0)) {
                quantityEditText.error = "Please enter a valid Quantity"
                return@setOnClickListener
            } else {
                quantityEditText.error = null
            }

            // Set productId before creating the Product object
            val productsRef = database.getReference("orders")
            val productId = productsRef.push().key

            val product = Product(coffeeType, quantity, userId, productId)

            productId?.let {
                productsRef.child(it).setValue(product)
                    .addOnSuccessListener {
                        Toast.makeText(
                            activity?.applicationContext,
                            "order placed successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Clear input areas after successful addition
                        coffeeTypeDropdown.text.clear()
                        quantityEditText.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            activity?.applicationContext,
                            "Error adding product: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

        return view
    }
}

data class Product(
    val coffeeType: String? = null,
    val quantity: Double? = 0.0,
    val userId: String? = null,
    var productId: String? = null,
)
