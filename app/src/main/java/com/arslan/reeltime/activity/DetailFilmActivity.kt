package com.arslan.reeltime.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.arslan.reeltime.R
import com.arslan.reeltime.adapter.CastListAdapter
import com.arslan.reeltime.adapter.GenreEachFilmAdapter
import com.arslan.reeltime.databinding.ActivityDetailFilmBinding
import com.arslan.reeltime.model.Film
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import eightbitlab.com.blurview.BlurTarget
import java.io.File
import java.io.FileOutputStream

class DetailFilmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFilmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setVariables()

        binding.shareBtn.setOnClickListener {
            shareMovieDetails()
        }
    }

    private fun setVariables() {
        val film = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("object", Film::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("object") as? Film
        }

        film?.let { filmObject ->
            val requestOptions = RequestOptions().transform(
                CenterCrop(),
                GranularRoundedCorners(0f, 0f, 50f, 50f)
            )

            Glide.with(this)
                .load(filmObject.Poster)
                .apply(requestOptions)
                .into(binding.filmPic)

            binding.titleTxt.text = filmObject.Title
            binding.imbdTxt.text = getString(R.string.imdb_rating, filmObject.Imdb)
            binding.movieTimeTxt.text = getString(R.string.movie_duration, filmObject.Year, filmObject.Time)
            binding.movieSummaryTxt.text = filmObject.Description

            binding.backBtn.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

            val decorView = window.decorView
            val windowBackground = decorView.background

            val blurTarget = findViewById<BlurTarget>(R.id.blurTarget)

            binding.blurView.setupWith(blurTarget)
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(10f)

            filmObject.Genre?.let { genre ->
                binding.genreView.adapter = GenreEachFilmAdapter(genre)
                binding.genreView.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            }

            filmObject.Casts?.let { casts ->
                binding.castListView.adapter = CastListAdapter(casts)
                binding.castListView.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            }

            binding.buyTicketBtn.setOnClickListener {
                val intent = Intent(this, SeatListActivity::class.java)
                intent.putExtra("film", filmObject)
                startActivity(intent)
            }
        }
    }

    private fun shareMovieDetails(){
        val bitmap = getScreenShotFromView(binding.root)
        if (bitmap != null) {
            val file = File(externalCacheDir, "movie.png")
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
            val uri = FileProvider.getUriForFile(this, "com.arslan.reeltime.provider", file)

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/png"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, "Share Movie"))
        }
    }

    private fun getScreenShotFromView(view: View): Bitmap? {
        var screenshot: Bitmap? = null
        try {
            screenshot = createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(screenshot)
            view.draw(canvas)
        } catch (e: Exception){
            e.printStackTrace()
        }
        return screenshot
    }
}