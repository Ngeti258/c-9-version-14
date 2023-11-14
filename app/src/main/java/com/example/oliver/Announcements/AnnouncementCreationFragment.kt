package com.example.oliver.Announcements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.oliver.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase

class AnnouncementCreationFragment : Fragment() {
    private lateinit var titleEditText: EditText
    private lateinit var paragraphEditText: EditText
    private lateinit var saveButton: Button

    private val database = FirebaseDatabase.getInstance()
    private val clerkAnnouncementsRef = database.getReference("clerk_announcements")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_announcement_creation, container, false)

        titleEditText = view.findViewById(R.id.editTextTitle)
        paragraphEditText = view.findViewById(R.id.editTextParagraph)
        saveButton = view.findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            saveClerkAnnouncementToDatabase()
        }

        return view
    }

    private fun saveClerkAnnouncementToDatabase() {
        val title = titleEditText.text.toString()
        val paragraph = paragraphEditText.text.toString()

        if (title.isNotEmpty() && paragraph.isNotEmpty()) {
            val announcement = Announcement(title, paragraph)
            val key = clerkAnnouncementsRef.push().key

            if (key != null) {
                clerkAnnouncementsRef.child(key).setValue(announcement)
                    .addOnSuccessListener(OnSuccessListener {
                        Toast.makeText(requireContext(), "Announcement made successfully", Toast.LENGTH_SHORT).show()
                        titleEditText.text.clear()
                        paragraphEditText.text.clear()
                    })
            }
        }
    }
}

//data class ClerkAnnouncement(val title: String, val paragraph: String)
