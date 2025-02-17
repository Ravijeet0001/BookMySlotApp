package com.example.bookmyslot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmyslot.R


class BookingAdapter(
    private var slotList: List<Map<String, Any>>,
    private val onBookClick: (Map<String, Any>) -> Unit,
    private val onReleaseClick: (Map<String, Any>) -> Unit
) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    inner class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEmail: TextView = view.findViewById(R.id.intemail)
        val tvSpecialization: TextView = view.findViewById(R.id.intSpecialization)
        val tvDate: TextView = view.findViewById(R.id.intDate)
        val tvTime: TextView = view.findViewById(R.id.intTime)
        val tvStatus: TextView = view.findViewById(R.id.intStatus)
        val btnBook: Button = view.findViewById(R.id.btnBook)
        val btnRelease: Button = view.findViewById(R.id.btnRelease)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val slot = slotList[position]

        holder.tvEmail.text = "Email: ${slot["email"]}"
        holder.tvSpecialization.text = "Specialization: ${slot["specialization"]}"
        holder.tvDate.text = "Date: ${slot["date"]}"
        holder.tvTime.text = "Time: ${slot["startTime"]} - ${slot["endTime"]}"
        holder.tvStatus.text = "Status: ${slot["status"]}"

        holder.btnBook.setOnClickListener { onBookClick(slot) }
        holder.btnRelease.setOnClickListener { onReleaseClick(slot) }
    }

    override fun getItemCount() = slotList.size

}