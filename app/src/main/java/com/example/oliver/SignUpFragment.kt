package com.example.oliver

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.oliver.Announcements.ClerkAnnouncementsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var farmSizeEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var textViewLogin: TextView
    private lateinit var confirmPasswordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        emailEditText = view.findViewById(R.id.editTextEmail)
        nameEditText = view.findViewById(R.id.editTextName)
        locationEditText = view.findViewById(R.id.editTextLocation)
        farmSizeEditText = view.findViewById(R.id.editTextFarmSize)
        passwordEditText = view.findViewById(R.id.editTextPassword)
        confirmPasswordEditText = view.findViewById(R.id.editTextConfirmPassword)
//        textViewLogin = view.findViewById(R.id.textViewLogin)

        val signUpButton: Button = view.findViewById(R.id.buttonSignUp)

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
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Registration successful, save user details to Firebase Database
                        saveUserDetailsToDatabase()

                        Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()

                        if (email == "clerk@gmail.com") {
                            val intent = Intent(requireContext(), ClerkAnnouncementsActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        } else {
                            val intent = Intent(requireContext(), FarmerActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
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
                nameEditText.text.clear()
                emailEditText.text.clear()
                locationEditText.text.clear()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}
