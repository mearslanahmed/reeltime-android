package com.arslan.reeltime.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.arslan.reeltime.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Set user's name and email
            binding.nameTxt.text = currentUser.displayName
            binding.emailTxt.text = currentUser.email

            // Set user's initial for the avatar
            val displayName = currentUser.displayName
            if (!displayName.isNullOrEmpty()) {
                binding.initialsTxt.text = displayName[0].toString().uppercase()
            }

        } else {
            // If no user is logged in, redirect to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}