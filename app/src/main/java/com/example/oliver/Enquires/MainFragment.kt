package com.example.oliver

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivityFragment : Fragment(R.layout.activity_main_enquires) {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        adapter = UserAdapter(requireContext(), userList)

        userRecyclerView = view.findViewById(R.id.userRecyclerView)

        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerView.adapter = adapter

        mDbRef.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUserRole = mAuth.currentUser?.displayName
                for (postSnapShot in snapshot.children) {
                    val currentUser = postSnapShot.getValue(User::class.java)

                    if (currentUser != null && mAuth.currentUser?.uid != currentUser.uid) {
                        if (currentUser.name == "clerk") {
                            // If the current user is a clerk, add all users to the list
                            userList.add(currentUser)
                        } else if (currentUser.name != "clerk") {
                            // If the current user is not a clerk, only add users with the name "clerk" to the list
                            userList.add(currentUser)
                        }
                    }
                }
                adapter.notifyDataSetChanged()

            }


            override fun onCancelled(error: DatabaseError) {
                // Handle the error as needed
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            // Write logic for logout
            mAuth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            requireActivity().finish()
            requireActivity().startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
