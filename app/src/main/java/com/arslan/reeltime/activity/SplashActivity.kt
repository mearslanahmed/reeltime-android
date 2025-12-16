package com.arslan.reeltime.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.arslan.reeltime.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Restore the UI by setting the content view
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.startBtn.setOnClickListener {
            // Use the reliable auth check on button click
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // User is signed in, go to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // No user is signed in, go to LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }
    }
}
