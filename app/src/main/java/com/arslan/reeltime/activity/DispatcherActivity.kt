package com.arslan.reeltime.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * An invisible activity that acts as a router.
 * It checks the user's login state and dispatches them to the correct screen.
 * This is the new launcher activity for the app.
 */
class DispatcherActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var authStateListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()

        // The AuthStateListener is the reliable way to get the current user state.
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            // This listener is called when the auth state is confirmed.
            // We can now safely navigate.
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is logged in, go directly to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // User is not logged in, go to the Splash/Intro screen
                startActivity(Intent(this, SplashActivity::class.java))
            }
            // Finish this activity so it's removed from the back stack
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        // Attach the listener here to get the auth state update
        authStateListener?.let { auth.addAuthStateListener(it) }
    }

    override fun onStop() {
        super.onStop()
        // Detach the listener here to avoid memory leaks
        authStateListener?.let { auth.removeAuthStateListener(it) }
    }
}
