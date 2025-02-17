package com.example.bookmyslot.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity

import com.example.bookmyslot.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val etName = findViewById<EditText>(R.id.signupName)
        val etEmail = findViewById<EditText>(R.id.signupemail)
        val etPassword = findViewById<EditText>(R.id.signuppswrd)
        val role=findViewById<EditText>(R.id.etRole)
        val btnSignup = findViewById<Button>(R.id.signupButton)
        val redirectlogin=findViewById<TextView>(R.id.redirectLogin)
        val etSecretKey = findViewById<EditText>(R.id.etSecretKey)


        role.setOnClickListener {
            val roles = arrayOf("TAG", "Interviewer")

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Role")
                .setItems(roles) { _, which ->
                    role.setText(roles[which])
                    etSecretKey.visibility = View.VISIBLE
                }
                .show()
        }

        btnSignup.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val selectedRole = role.text.toString()
            val secretKey = etSecretKey.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || selectedRole.isEmpty() || secretKey.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }

            if ((selectedRole == "TAG" && secretKey != "123") ||
                (selectedRole == "Interviewer" && secretKey != "321")) {
                Toast.makeText(this, "Invalid Secret Key for $selectedRole", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful) {
                        val user= auth.currentUser
                        val userData = hashMapOf(
                            "name" to name,
                            "role" to selectedRole,
                            "email" to email
                        )
                        user?.let {
                            db.collection("users").document(it.uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    user.sendEmailVerification()
                                        .addOnCompleteListener { verificationTask ->
                                            if (verificationTask.isSuccessful) {
                                                Toast.makeText(this, "Verification email sent. Please check your inbox.", Toast.LENGTH_SHORT).show()
                                                Toast.makeText(this, "User registered successfully. Please verify your email.", Toast.LENGTH_SHORT).show()
                                                val intent = Intent(this, LoginActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                            } else {
                                                Toast.makeText(this, "Error sending verification email: ${verificationTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Error saving user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }

                    }else{
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        redirectlogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}