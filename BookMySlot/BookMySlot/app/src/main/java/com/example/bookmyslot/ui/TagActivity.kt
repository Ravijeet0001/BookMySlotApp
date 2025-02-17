package com.example.bookmyslot.ui

import android.content.Intent

import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmyslot.R
import com.example.bookmyslot.adapter.BookingAdapter
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class TagActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var adapter: BookingAdapter
    private val slotList = mutableListOf<Map<String, Any>>()
    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)

        searchView = findViewById(R.id.searchView)
        searchView.clearFocus()



        val rvSlots = findViewById<RecyclerView>(R.id.rvInterviewerSlots)
        rvSlots.layoutManager = LinearLayoutManager(this)
        adapter = BookingAdapter(slotList, ::bookSlot, ::releaseSlot)
        rvSlots.adapter = adapter
        fetchAllInterviewerSlots()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterSlots(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterSlots(newText)
                return true
            }
        })
    }


    private fun fetchAllInterviewerSlots() {
        firestore.collection("InterviewerSlots")
            .get()
            .addOnSuccessListener { documents ->
                slotList.clear()
                for (document in documents) {
                    val slots = document["timeSlots"] as? List<Map<String, Any>> ?: emptyList()
                    slots.forEach { slot ->
                        slotList.add(slot + ("userId" to document.id))
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun bookSlot(slot: Map<String, Any>) {
        if (slot["status"] == "pending") {
            updateSlotStatus(slot, "booked", "Your slot has been booked, check your Slot status in app")
            Toast.makeText(this,"Slot has been booked",Toast.LENGTH_SHORT).show()
            fetchAllInterviewerSlots()
        } else {
            Toast.makeText(this, "Slot already booked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun releaseSlot(slot: Map<String, Any>) {
        if (slot["status"] == "booked") {
            updateSlotStatus(slot, "pending", "Your slot has been released.")
            Toast.makeText(this,"Slot has been released",Toast.LENGTH_SHORT).show()
            fetchAllInterviewerSlots()
        } else {
            Toast.makeText(this, "Slot is already pending", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateSlotStatus(slot: Map<String, Any>, status: String, emailMessage: String) {
        val userId = slot["userId"].toString()
        firestore.runTransaction { transaction ->
            val docRef = firestore.collection("InterviewerSlots").document(userId)
            val snapshot = transaction.get(docRef)
            val currentSlots = snapshot.get("timeSlots") as? MutableList<Map<String, Any>> ?: mutableListOf()

            // Find existing slot and update status
            val updatedSlots = currentSlots.map {
                if (it["startTime"] == slot["startTime"] && it["endTime"] == slot["endTime"] &&
                    it["date"] == slot["date"] && it["specialization"] == slot["specialization"]
                ) {
                    it.toMutableMap().apply { put("status", status) }
                } else it
            }

            transaction.update(docRef, "timeSlots", updatedSlots)
        }.addOnSuccessListener {
            sendEmail(slot["email"].toString(), emailMessage)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Slot status updated", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to update slot", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendEmail(email: String, message: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"  // Ensures only email clients show up
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, "Interview Slot Update ")
            putExtra(Intent.EXTRA_TEXT, message)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(intent, "Choose Email Client"))
        } else {
            Toast.makeText(this, "No email client found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filterSlots(query: String?) {
        val filteredList = slotList.filter { slot ->
            val email = slot["email"].toString().lowercase()
            val specialization = slot["specialization"].toString().lowercase()
            val date = slot["date"].toString().lowercase()

            query?.lowercase()?.let {
                email.contains(it) || specialization.contains(it) || date.contains(it)
            } ?: true
        }
        slotList.clear()
        slotList.addAll(filteredList)
        adapter.notifyDataSetChanged()
    }
}
