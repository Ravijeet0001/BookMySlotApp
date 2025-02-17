package com.example.bookmyslot.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookmyslot.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class InterviewerActivity : AppCompatActivity() {

    private lateinit var specialization: EditText
    private lateinit var slotdate: EditText
    private lateinit var sTime: EditText
    private lateinit var eTime: EditText
    private lateinit var email: EditText
    private lateinit var btnAddSlot: Button
    private lateinit var btnSubmit: Button
    private lateinit var seeyourslot: Button

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val timeSlots = mutableListOf<Map<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interviewer)

        specialization = findViewById(R.id.specialization)
        slotdate = findViewById(R.id.picDate)
        sTime = findViewById(R.id.starttime)
        eTime = findViewById(R.id.endtime)
        email = findViewById(R.id.email)
        btnAddSlot = findViewById(R.id.addslotbtn)
        btnSubmit = findViewById(R.id.submitbtn)
        seeyourslot = findViewById(R.id.seeyourslot)

        slotdate.setOnClickListener{ showDatePickerDialog(slotdate) }
        sTime.setOnClickListener{ showTimePickerDialog(sTime) }
        eTime.setOnClickListener{ showTimePickerDialog(eTime) }

        btnAddSlot.setOnClickListener{
            addSlot()
        }

        btnSubmit.setOnClickListener{
            submitSlotsToDatabase()
        }
        seeyourslot.setOnClickListener{
            val intent = Intent(this, InterviewerBookedSlots::class.java)
            startActivity(intent)
        }
    }
    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,{ _, year, month, dayOfMonth ->
                val date = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
                editText.setText(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this, { _, hourOfDay, minute ->
                val time = String.format("%02d:%02d", hourOfDay, minute)
                editText.setText(time)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun addSlot() {
        val specialization = specialization.text.toString()
        val date = slotdate.text.toString().trim()
        val startTime = sTime.text.toString().trim()
        val endTime = eTime.text.toString().trim()
        val email = email.text.toString().trim()

        if (specialization.isNotEmpty() && date.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty()) {
            val slot = mapOf(
                "specialization" to specialization,
                "date" to date,
                "startTime" to startTime,
                "endTime" to endTime,
                "email" to email,
                "status" to "pending"
            )
            timeSlots.add(slot)
            Toast.makeText(this, "Slot added", Toast.LENGTH_SHORT).show()

            //specialization.text.clear()
            slotdate.text.clear()
            sTime.text.clear()
            eTime.text.clear()
        } else {
            Toast.makeText(this, "Please fill all slot details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun submitSlotsToDatabase() {
        val userId = auth.currentUser?.uid
        val specialization = specialization.text.toString().trim()

        if (specialization.isEmpty() || timeSlots.isEmpty()) {
            Toast.makeText(
                this,
                "Please enter specialization and at least one slot",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val dbRef = db.collection("InterviewerSlots").document(userId!!)

        // Create or update the document
        dbRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                // If document exists, append new slots using arrayUnion
                for (slot in timeSlots) {
                    dbRef.update("timeSlots", FieldValue.arrayUnion(slot))
                        .addOnSuccessListener {
                            Toast.makeText(this, "Slots updated successfully", Toast.LENGTH_LONG)
                                .show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to update slots", Toast.LENGTH_LONG).show()
                        }
                }
            } else {
                // If document doesn't exist, create it
                val interviewerData = mapOf(
                    "userId" to userId,
                    //"specialization" to specialization,
                    "timeSlots" to timeSlots,
                    //"status" to "pending",
                    "createdAt" to System.currentTimeMillis()
                )
                dbRef.set(interviewerData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Slots submitted successfully", Toast.LENGTH_LONG)
                            .show()
                        timeSlots.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Not Submitted", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

}