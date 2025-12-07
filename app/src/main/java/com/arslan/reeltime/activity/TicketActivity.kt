package com.arslan.reeltime.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.arslan.reeltime.R
import com.arslan.reeltime.databinding.ActivityTicketBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

class TicketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTicketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(this, "Ticket purchased successfully", Toast.LENGTH_SHORT).show()

        val filmTitle = intent.getStringExtra("filmTitle")
        val seatIds = intent.getStringExtra("seatIds")
        val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)

        binding.titleTxt.text = filmTitle
        binding.filmTitleTxt.text = filmTitle
        binding.seatsTxt.text = seatIds
        binding.ticketPriceTxt.text = getString(R.string.price_format, totalPrice)

        binding.TicketVBackBtn.setOnClickListener {
            // Navigate back to the home screen
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val qrCodeData = "Film: $filmTitle, Seats: $seatIds, Price: $totalPrice"
        try {
            val qrCodeBitmap = generateQrCode(qrCodeData)
            binding.qrCode.setImageBitmap(qrCodeBitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        binding.shareBtn.setOnClickListener {
            shareTicket()
        }

        binding.calendarBtn.setOnClickListener {
            addToCalendar(filmTitle)
        }
    }

    private fun generateQrCode(data: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp[x, y] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }
        return bmp
    }

    private fun shareTicket() {
        val bitmap = getScreenShotFromView(binding.ticketLayout)
        val file = File(externalCacheDir, "ticket.png")
        val fOut = FileOutputStream(file)
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fOut)
        fOut.flush()
        fOut.close()
        val uri = FileProvider.getUriForFile(this, "com.arslan.reeltime.provider", file)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(intent, "Share Ticket"))
    }

    private fun getScreenShotFromView(view: View): Bitmap? {
        var screenshot: Bitmap? = null
        try {
            screenshot = createBitmap(view.measuredWidth, view.measuredHeight)
            val canvas = Canvas(screenshot)
            view.draw(canvas)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return screenshot
    }

    private fun addToCalendar(title: String?) {
        val beginTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        endTime.add(Calendar.HOUR, 2)

        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis)
            .putExtra(CalendarContract.Events.TITLE, title)
            .putExtra(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().timeZone.id)

        startActivity(intent)
    }
}
