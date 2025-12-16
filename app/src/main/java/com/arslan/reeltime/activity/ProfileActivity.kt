package com.arslan.reeltime.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.arslan.reeltime.R
import com.arslan.reeltime.databinding.ActivityProfileBinding
import com.arslan.reeltime.model.User
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            uploadToCloudinary(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        loadUserData()

        binding.editProfileImageBtn.setOnClickListener {
            openGallery()
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

    private fun loadUserData() {
        val user = auth.currentUser
        if (user != null) {
            binding.nameTxt.text = user.displayName
            binding.emailTxt.text = user.email

            val userRef = database.getReference("Users").child(user.uid)
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)

                        if (!profileImageUrl.isNullOrEmpty()) {
                            binding.profileImage.visibility = View.VISIBLE
                            binding.initialsTxt.visibility = View.GONE
                            Glide.with(this@ProfileActivity)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.profile)
                                .into(binding.profileImage)
                        } else {
                            binding.profileImage.visibility = View.GONE
                            binding.initialsTxt.visibility = View.VISIBLE
                            if (!user.displayName.isNullOrEmpty()) {
                                binding.initialsTxt.text = user.displayName!![0].toString().uppercase()
                            }
                        }
                    } else {
                        val name = user.displayName
                        val email = user.email
                        if (!name.isNullOrEmpty() && !email.isNullOrEmpty()) {
                            val newUser = User(name, email)
                            userRef.setValue(newUser)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("ProfileActivity", "Could not load profile image: ${error.message}")
                }
            })
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun uploadToCloudinary(imageUri: Uri) {
        Handler(Looper.getMainLooper()).post {
            binding.progressBar.visibility = View.VISIBLE
        }
        MediaManager.get().upload(imageUri).callback(object : UploadCallback {
            override fun onStart(requestId: String?) {}
            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                val imageUrl = resultData?.get("secure_url") as? String
                if (!imageUrl.isNullOrEmpty()) {
                    saveImageUrlToDatabase(imageUrl)
                } else {
                    Handler(Looper.getMainLooper()).post {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@ProfileActivity, "Upload failed: URL not found.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                Handler(Looper.getMainLooper()).post {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@ProfileActivity, "Upload failed: ${error?.description}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
        }).dispatch()
    }

    private fun saveImageUrlToDatabase(imageUrl: String) {
        val user = auth.currentUser
        if (user != null) {
            val userRef = database.getReference("Users").child(user.uid)
            userRef.child("profileImageUrl").setValue(imageUrl).addOnCompleteListener {
                Handler(Looper.getMainLooper()).post {
                    binding.progressBar.visibility = View.GONE
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Profile image updated!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to save image URL.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}