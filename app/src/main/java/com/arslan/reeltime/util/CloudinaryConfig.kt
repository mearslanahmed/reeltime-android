package com.arslan.reeltime.util

import com.arslan.reeltime.ReelTimeApp
import com.cloudinary.Cloudinary
import com.cloudinary.android.MediaManager

object CloudinaryConfig {
    private var cloudinary: Cloudinary? = null

    fun setup(cloudName: String, apiKey: String, apiSecret: String) {
        val config = mapOf(
            "cloud_name" to cloudName,
            "api_key" to apiKey,
            "api_secret" to apiSecret
        )
        // Initialize MediaManager
        MediaManager.init(ReelTimeApp.instance, config)
        cloudinary = Cloudinary(config)
    }

    fun getCloudinary(): Cloudinary {
        // Added a check to prevent crash if not initialized
        if (cloudinary == null) {
            throw IllegalStateException("Cloudinary has not been initialized. Call setup() first.")
        }
        return cloudinary!!
    }
}
