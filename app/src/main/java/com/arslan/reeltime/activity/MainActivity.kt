package com.arslan.reeltime.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.arslan.reeltime.R
import com.arslan.reeltime.adapter.FilmListAdapter
import com.arslan.reeltime.adapter.SliderAdapter
import com.arslan.reeltime.databinding.ActivityMainBinding
import com.arslan.reeltime.model.Film
import com.arslan.reeltime.model.SliderItems
import com.arslan.reeltime.model.User
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private val sliderHandle = Handler(Looper.getMainLooper())
    private val sliderRunnable = Runnable {
        binding.viewPager2.currentItem += 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        initBanner()
        initTopMovies()
        initUpcoming()

        binding.bottomMenu.setOnItemSelectedListener(object : ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                when (id) {
                    R.id.saved -> {
                        val intent = Intent(this@MainActivity, SavedMoviesActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.my_tickets -> {
                        val intent = Intent(this@MainActivity, MyTicketsActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.profile -> {
                        val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.bottomMenu.setItemSelected(R.id.explorer, true)
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val user = auth.currentUser
        if (user != null) {
            binding.greetingTxt.text = "Hello, ${user.displayName}"

            val userRef = database.getReference("Users").child(user.uid)
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)

                        if (!profileImageUrl.isNullOrEmpty()) {
                            binding.profileImageMain.visibility = View.VISIBLE
                            binding.initialsTxt.visibility = View.GONE
                            Glide.with(this@MainActivity)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.profile)
                                .into(binding.profileImageMain)
                        } else {
                            binding.profileImageMain.visibility = View.GONE
                            binding.initialsTxt.visibility = View.VISIBLE
                            if (!user.displayName.isNullOrEmpty()) {
                                binding.initialsTxt.text = user.displayName!![0].toString().uppercase()
                            }
                        }
                    } else {
                        // Self-healing in case the DB record is missing
                        val name = user.displayName
                        val email = user.email
                        if (!name.isNullOrEmpty() && !email.isNullOrEmpty()) {
                            val newUser = User(name, email)
                            userRef.setValue(newUser)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("MainActivity", "Could not load profile image: ${error.message}")
                }
            })
        } else {
            binding.greetingTxt.text = getString(R.string.hello_guest)
            binding.initialsTxt.text = "G"
        }
    }

    private fun initTopMovies() {
        val myRef: DatabaseReference = database.getReference("Items")
        binding.progressBarTopMovies.visibility = View.VISIBLE

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val items = mutableListOf<Film>()
                    for (i in snapshot.children) {
                        val item = i.getValue(Film::class.java)
                        if (item != null) {
                            items.add(item)
                        }
                    }
                    if (items.isNotEmpty()) {
                        binding.recyclerViewTopMovies.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                        binding.recyclerViewTopMovies.adapter = FilmListAdapter(items as ArrayList<Film>)
                    }
                    binding.progressBarTopMovies.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MainActivity", "Failed to read top movies.", error.toException())
            }
        })
    }

    private fun initBanner() {
        val myRef = database.getReference("Banners")
        binding.progressBarSlider.visibility = View.VISIBLE

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderItems>()
                for (i in snapshot.children) {
                    val list = i.getValue(SliderItems::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                binding.progressBarSlider.visibility = View.GONE
                banners(lists)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MainActivity", "Failed to read banners.", error.toException())
            }
        })
    }

    private fun banners(list: MutableList<SliderItems>) {
        binding.viewPager2.adapter = SliderAdapter(list, binding.viewPager2)
        binding.viewPager2.clipToPadding = false
        binding.viewPager2.clipChildren = false
        binding.viewPager2.offscreenPageLimit = 3
        binding.viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }
        }

        binding.viewPager2.setPageTransformer(compositePageTransformer)
        binding.viewPager2.currentItem = 1
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandle.removeCallbacks(sliderRunnable)
            }
        })
    }

    private fun initUpcoming() {
        val myRef: DatabaseReference = database.getReference("Upcomming")
        binding.progressBarUpcoming.visibility = View.VISIBLE

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val items = mutableListOf<Film>()
                    for (i in snapshot.children) {
                        val item = i.getValue(Film::class.java)
                        if (item != null) {
                            items.add(item)
                        }
                    }
                    if (items.isNotEmpty()) {
                        binding.recyclerViewUpcoming.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                        binding.recyclerViewUpcoming.adapter = FilmListAdapter(items as ArrayList<Film>)
                    }
                    binding.progressBarUpcoming.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MainActivity", "Failed to read upcoming movies.", error.toException())
            }
        })
    }
}
