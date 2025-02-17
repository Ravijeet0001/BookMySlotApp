package com.example.bookmyslot.ui

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmyslot.R
import com.example.bookmyslot.adapter.SlotsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class InterviewerBookedSlots : AppCompatActivity() {

    private lateinit var recyclerViewSlots: RecyclerView
    private val slots = mutableListOf<Map<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interviewer_booked_slots)

        recyclerViewSlots = findViewById(R.id.recycleViewSlots)
        recyclerViewSlots.layoutManager = LinearLayoutManager(this)

        getInterviewerSlots()

    }

    private fun getInterviewerSlots() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("InterviewerSlots").document(userId!!)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                //val specialization = documentSnapshot.getString("specialization")
                val slots = documentSnapshot.get("timeSlots") as List<Map<String, String>>


                // set slots into your RecyclerView
                updateRecyclerView(slots)
            }
            .addOnFailureListener {
                Toast.makeText(this,"Not fetch any data",Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateRecyclerView(slots: List<Map<String, String>>) {
        val adapter = SlotsAdapter(slots, { slot -> showEditSlotDialog(slot) }, { slot -> deleteSlotFromFirestore(slot) })
        recyclerViewSlots.adapter = adapter
    }

    private fun deleteSlotFromFirestore(slot: Map<String, String>) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("InterviewerSlots").document(userId!!)
            .update("timeSlots", FieldValue.arrayRemove(slot))
            .addOnSuccessListener {
                Toast.makeText(this, "Slot removed successfully", Toast.LENGTH_SHORT).show()
                getInterviewerSlots() // Refresh slot list
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error removing slot", Toast.LENGTH_SHORT).show()
            }
    }
    private fun showEditSlotDialog(slot: Map<String, String>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Slot")

        // Create a layout for the dialog
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(32, 32, 32, 32)

        // Create input fields
        val editTextDate = EditText(this).apply {
            hint = "Date (e.g., 2025-02-16)"
            setText(slot["date"])
        }

        val editTextStartTime = EditText(this).apply {
            hint = "Start Time (e.g., 10:00 AM)"
            setText(slot["startTime"])
        }

        val editTextEndTime = EditText(this).apply {
            hint = "End Time (e.g., 11:00 AM)"
            setText(slot["endTime"])
        }
        val editTextEmail = EditText(this).apply {
            hint = "Email"
            setText(slot["email"])
        }

        // Add inputs to the layout
        layout.addView(editTextDate)
        layout.addView(editTextStartTime)
        layout.addView(editTextEndTime)
        layout.addView(editTextEmail)

        builder.setView(layout)

        // Set buttons
        builder.setPositiveButton("Update") { _, _ ->
            val newDate = editTextDate.text.toString()
            val newStartTime = editTextStartTime.text.toString()
            val newEndTime = editTextEndTime.text.toString()
            val newEmail = editTextEmail.text.toString()

            // Call function to update slot in Firestore
            updateSlotInFirestore(slot, newDate, newStartTime, newEndTime,newEmail)
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        // Show the dialog
        builder.create().show()
    }
    private fun updateSlotInFirestore(
        oldSlot: Map<String, String>,
        newDate: String,
        newStartTime: String,
        newEndTime: String,
        newEmail: String
    ) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val updatedSlot = mapOf(
            "date" to newDate,
            "startTime" to newStartTime,
            "endTime" to newEndTime,
            "status" to (oldSlot["status"] ?: "Pending"),
            "email" to newEmail
        )

        db.collection("InterviewerSlots").document(userId!!)
            .update("timeSlots", FieldValue.arrayRemove(oldSlot))
            .addOnSuccessListener {
                db.collection("InterviewerSlots").document(userId)
                    .update("timeSlots", FieldValue.arrayUnion(updatedSlot))
                    .addOnSuccessListener {
                        Toast.makeText(this, "Slot updated successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to update slot", Toast.LENGTH_SHORT).show()
                    }
            }
    }

}