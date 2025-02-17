package com.example.bookmyslot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmyslot.R

class SlotsAdapter(private val slots: List<Map<String, String>>, private val onEdit: (Map<String, String>) -> Unit, private val onDelete: (Map<String, String>) -> Unit) :
    RecyclerView.Adapter<SlotsAdapter.SlotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_interviewer_slots, parent, false)
        return SlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val slot = slots[position]
        holder.bind(slot)


        holder.editButton.setOnClickListener {
            onEdit(slot)
        }

        holder.deleteButton.setOnClickListener {
            onDelete(slot)
        }
    }

    override fun getItemCount(): Int {
        return slots.size
    }

    class SlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val slotDetails: TextView = itemView.findViewById(R.id.slotDetails)

        fun bind(slot: Map<String, String>) {
            val specialization = slot["specialization"]
            val date = slot["date"]
            val startTime = slot["startTime"]
            val endTime = slot["endTime"]
            val email = slot["email"]
            val status = slot["status"] ?: "Pending"
            slotDetails.text = "$specialization $date: $startTime - $endTime (Status: $status) $email"
        }
    }
}
