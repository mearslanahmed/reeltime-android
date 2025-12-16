package com.arslan.reeltime

import android.app.Application
import com.arslan.reeltime.util.CloudinaryConfig

class ReelTimeApp : Application() {

    companion object {
        lateinit var instance: ReelTimeApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        val cloudName = BuildConfig.CLOUDINARY_CLOUD_NAME
        val apiKey = BuildConfig.CLOUDINARY_API_KEY
        val apiSecret = BuildConfig.CLOUDINARY_API_SECRET

        if (cloudName.isNotEmpty()) {
            CloudinaryConfig.setup(cloudName, apiKey, apiSecret)
        }
    }
}
