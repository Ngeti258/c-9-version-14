package com.example.oliver.Announcements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oliver.R

class AnnouncementsAdapter(private val announcements: List<Announcement>) :
    RecyclerView.Adapter<AnnouncementsAdapter.AnnouncementViewHolder>() {

    class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val paragraphTextView: TextView = itemView.findViewById(R.id.paragraphTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.announcement_item, parent, false)
        return AnnouncementViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val announcement = announcements[position]
        holder.titleTextView.text = announcement.title
        holder.paragraphTextView.text = announcement.paragraph
    }

    override fun getItemCount(): Int {
        return announcements.size
    }
}
