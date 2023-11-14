package com.example.oliver.Announcements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oliver.R
import com.google.firebase.database.*

class AnnouncementsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var announcementsAdapter: AnnouncementsAdapter
    private val announcementsList: MutableList<Announcement> = ArrayList()

    private val database = FirebaseDatabase.getInstance()
    private val announcementsRef = database.getReference("clerk_announcements")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_announcements, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewAnnouncements)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        announcementsAdapter = AnnouncementsAdapter(announcementsList)
        recyclerView.adapter = announcementsAdapter

        loadAnnouncements()

        return view
    }

    private fun loadAnnouncements() {
        announcementsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                announcementsList.clear()
                val reversedAnnouncements = snapshot.children.reversed() // Reverse the order
                for (dataSnapshot in reversedAnnouncements) {
                    val announcement = dataSnapshot.getValue(Announcement::class.java)
                    if (announcement != null) {
                        announcementsList.add(announcement)
                    }
                }
                announcementsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database read error
            }
        })
    }

}
