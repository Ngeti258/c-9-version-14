package com.example.oliver

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oliver.Announcements.ClerkAnnouncementsActivity
import com.example.oliver.databinding.ActivitySignupBinding
import com.google.firebase.Firebase

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database

import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivitySignupBinding
    private lateinit var emailEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var farmSizeEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var textViewLogin : TextView
    private lateinit var confirmPasswordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emailEditText = findViewById(R.id.editTextEmail)
        nameEditText = findViewById(R.id.editTextName)
        locationEditText = findViewById(R.id.editTextLocation)
        farmSizeEditText = findViewById(R.id.editTextFarmSize)
        passwordEditText = findViewById(R.id.editTextPassword)
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword)
        textViewLogin = findViewById(R.id.textViewLogin)


        val signUpButton: Button = findViewById(R.id.buttonSignUp)

        textViewLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (email.isEmpty() || !isValidEmail(email)) {
                emailEditText.error = "Enter a valid email address"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password != confirmPassword) {
                passwordEditText.error = "Passwords do not match"
                confirmPasswordEditText.error = "Passwords do not match"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            // Create a user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registration successful, save user details to Firebase Database
                        saveUserDetailsToDatabase()

                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                        if (email == "clerk@gmail.com") {
                            val intent = Intent(this, ClerkAnnouncementsActivity::class.java)
                            startActivity(intent)
                        }else {
                            val intent = Intent(this, FarmerActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Registration failed. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun saveUserDetailsToDatabase() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            val user = User(
                uid,
                nameEditText.text.toString(),
                emailEditText.text.toString(),
                locationEditText.text.toString(),
                farmSizeEditText.text.toString()
            )

            val usersDatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
            usersDatabaseReference.child(uid).setValue(user).addOnSuccessListener {
                binding.editTextName.text.clear()
                binding.editTextEmail.text.clear()
                binding.editTextLocation.text.clear()
            }
        }
    }



    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}