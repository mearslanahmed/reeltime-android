package com.arslan.reeltime.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.arslan.reeltime.databinding.ActivityRegisterBinding
import com.arslan.reeltime.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.registerBtn.setOnClickListener {
            val name = binding.nameEdt.text.toString().trim()
            val email = binding.emailEdt.text.toString().trim()
            val password = binding.passwordEdt.text.toString()
            val confirmPassword = binding.confirmPasswordEdt.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.registerBtn.isEnabled = false

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val firebaseUser = auth.currentUser
                                if (firebaseUser == null) {
                                    binding.progressBar.visibility = View.GONE
                                    binding.registerBtn.isEnabled = true
                                    Toast.makeText(this, "Registration failed, please try again.", Toast.LENGTH_LONG).show()
                                    return@addOnCompleteListener
                                }

                                // Update Firebase Auth profile
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build()
                                firebaseUser.updateProfile(profileUpdates)

                                // Save user to Realtime Database
                                val user = User(name, email)
                                database.getReference("Users").child(firebaseUser.uid).setValue(user)
                                    .addOnCompleteListener { dbTask ->
                                        binding.progressBar.visibility = View.GONE
                                        binding.registerBtn.isEnabled = true
                                        if (dbTask.isSuccessful) {
                                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this, MainActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Toast.makeText(this, "Failed to save profile: ${dbTask.exception?.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                            } else {
                                binding.progressBar.visibility = View.GONE
                                binding.registerBtn.isEnabled = true
                                Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginTxt.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}