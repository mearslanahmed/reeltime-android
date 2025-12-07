package com.arslan.reeltime.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.arslan.reeltime.R
import com.arslan.reeltime.adapter.DateAdapter
import com.arslan.reeltime.adapter.SeatListAdapter
import com.arslan.reeltime.adapter.TimeAdapter
import com.arslan.reeltime.databinding.ActivitySeatListBinding
import com.arslan.reeltime.model.Film
import com.arslan.reeltime.model.Seat
import com.arslan.reeltime.model.TicketData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SeatListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeatListBinding
    private lateinit var film: Film
    private var price: Double = 0.0
    private var selectedSeats: Int = 0
    private var selectedSeatNames: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySeatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentExtra()
        setVariable()
        initTimeDateList()
        initSeatList()
        downloadTicketBtn()
    }

    private fun downloadTicketBtn() {
        binding.downloadBtn.setOnClickListener {
            if (selectedSeats > 0) {
                saveTicketToFirebase()
            } else {
                Toast.makeText(this, "Please select at least one seat", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveTicketToFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("tickets").child(userId)
        val ticketId = database.push().key!!

        val ticketData = TicketData(
            id = ticketId,
            filmTitle = film.Title,
            seatIds = selectedSeatNames,
            totalPrice = price,
            poster = film.Poster,
            date = (binding.dateRecyclerview.adapter as DateAdapter).getSelectedDate(),
            time = (binding.timeRecyclerview.adapter as TimeAdapter).getSelectedTime()
        )

        database.child(ticketId).setValue(ticketData).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this@SeatListActivity, TicketActivity::class.java)
                intent.putExtra("filmTitle", film.Title)
                intent.putExtra("seatIds", selectedSeatNames)
                intent.putExtra("totalPrice", price)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to save ticket", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initSeatList() {
        val gridLayoutManager = GridLayoutManager(this, 7)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position % 7 == 3) 1 else 1
            }
        }

        binding.apply {
            seatRecyclerView.layoutManager = gridLayoutManager

            val seatList = mutableListOf<Seat>()
//            val numberSeats = 81
            val totalRows = 11
            val seatsPerRow = 7
            for (i in 0 until totalRows * seatsPerRow) {
                val row = i / seatsPerRow
                val col = i % seatsPerRow
                val seatName = "${'A' + row}${col + 1}"
                val seatStatus = {
                    if (i == 2 || i == 20 || i == 33 || i == 41 || i == 50 || i == 72 || i == 73) Seat.SeatStatus.UNAVAILABLE
                    else Seat.SeatStatus.AVAILABLE
                }
                seatList.add(Seat(seatStatus(), seatName))
            }

            val seatAdapter =
                SeatListAdapter(seatList, this@SeatListActivity, object : SeatListAdapter.SelectedSeats {
                    override fun Return(slectedName: String, num: Int) {
                        runOnUiThread {
                            numberSelectedTxt.text = getString(R.string.seat_selected, num)
                            val df = DecimalFormat("#.##")
                            price = df.format(num * (film.Price ?: 0.0)).toDouble()
                            selectedSeats = num
                            priceTxt.text = "$$price"
                            selectedSeatNames = slectedName
                        }

                    }
                })
            seatRecyclerView.adapter = seatAdapter
            seatRecyclerView.isNestedScrollingEnabled = false
        }
    }

    private fun initTimeDateList() {
        binding.apply {
            dateRecyclerview.layoutManager =
                LinearLayoutManager(this@SeatListActivity, LinearLayoutManager.HORIZONTAL, false)
            dateRecyclerview.adapter = DateAdapter(generateDates())

            timeRecyclerview.layoutManager =
                LinearLayoutManager(this@SeatListActivity, LinearLayoutManager.HORIZONTAL, false)
            timeRecyclerview.adapter = TimeAdapter(generateTimeSlots())
        }
    }

    private fun setVariable() {
        binding.backButton.setOnClickListener { finish() }
    }

    private fun getIntentExtra() {
        film = intent.getSerializableExtra("film", Film::class.java) as Film
    }

    private fun generateDates(): List<String> {
        val dates = mutableListOf<String>()
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEE/dd/MMM")

        for (i in 0 until 7) {
            dates.add(today.plusDays(i.toLong()).format(formatter))
        }

        return dates

    }

    private fun generateTimeSlots(): List<String> {
        val timeSlots = mutableListOf<String>()
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")

        for (i in 0 until 24 step 2) {
            val time = LocalDate.now().atTime(i, 0)
            timeSlots.add(time.format(formatter))
        }
        return timeSlots
    }
}
