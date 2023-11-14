package com.example.oliver.Orders

import com.google.firebase.auth.FirebaseAuth

data class Product(
    val coffeeType: String? = null,
    val quantity: Double? = 0.0,
    val userId: String? = null,
    var productId: String? = null,

    )
