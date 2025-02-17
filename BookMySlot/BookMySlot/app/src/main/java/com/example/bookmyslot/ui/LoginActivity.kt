package com.example.bookmyslot.ui

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookmyslot.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        val loginemail=findViewById<EditText>(R.id.loginEmail)
        val loginpassword=findViewById<EditText>(R.id.loginpassword)
        val loginbutton=findViewById<Button>(R.id.loginButton)
        val forgotpassword=findViewById<TextView>(R.id.forgotPassword)
        val signuptext=findViewById<TextView>(R.id.signupText)

        loginbutton.setOnClickListener {
            val email = loginemail.text.toString()
            val password = loginpassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null && user.isEmailVerified) {
                            // Fetch the user's role from Firestore
                            fetchUserRoleAndNavigate(user.uid)
                        } else {
                            // If email is not verified, show a message
                            Toast.makeText(this, "Please verify your email first", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Authentication failed due to invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
        }


        forgotpassword.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val view=layoutInflater.inflate(R.layout.dialog_forgotpassword, null)
            val userEmail=view.findViewById<EditText>(R.id.desc)

            builder.setView(view)
            val dialog=builder.create()

            view.findViewById<Button>(R.id.btnreset).setOnClickListener{
                compareEmail(userEmail)
                dialog.dismiss()

            }
            view.findViewById<Button>(R.id.cancel).setOnClickListener{
                dialog.dismiss()
            }
            if(dialog.window!=null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }
        signuptext.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun fetchUserRoleAndNavigate(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val role = document.getString("role")
                    when (role) {
                        "TAG" -> {
                            // Navigate to TAG Activity
                            val intent = Intent(this, TagActivity::class.java)
                            startActivity(intent)
                            finish() // Optionally finish login activity
                        }
                        "Interviewer" -> {
                            // Navigate to Interviewer Activity
                            val intent = Intent(this, InterviewerActivity::class.java)
                            startActivity(intent)
                            finish() // Optionally finish login activity
                        }
                        else -> {
                            Toast.makeText(this, "Role not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun compareEmail(email:EditText){
        if(email.text.toString().isEmpty()){
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }
        auth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener{
                task ->
            if(task.isSuccessful){
                Toast.makeText(this,"check your email",Toast.LENGTH_SHORT).show()
            }
        }

    }
}