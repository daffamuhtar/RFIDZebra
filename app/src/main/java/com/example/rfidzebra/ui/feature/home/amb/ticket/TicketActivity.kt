package com.example.rfidzebra.ui.feature.home.amb.ticket

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rfidzebra.databinding.ActivityTicketBinding
import com.example.rfidzebra.ui.feature.home.amb.AssetActivity
import com.example.rfidzebra.ui.feature.home.amb.ticket.addticket.NewAssetTicketActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicketBinding
    private lateinit var ticketAdapter: TicketAdapter
    private val ticketList = mutableListOf<TicketItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigate()
        setupRecyclerView()
        loadTickets()

        // Restore saved ticket number
        val savedTicketNumber = getSharedPreferences("TicketPrefs", MODE_PRIVATE).getString("TV_ASSET_MOVE", null)
        if (!savedTicketNumber.isNullOrEmpty()) {
            binding.tvAssetMove.text = savedTicketNumber
        }

        val ticketNumber = intent.getStringExtra("TICKET_NUMBER")
        val fromAssetActivity = intent.getBooleanExtra("FROM_ASSET_ACTIVITY", false)

        if (!ticketNumber.isNullOrEmpty()) {
            if (fromAssetActivity) {
                binding.tvAssetMove.text = ticketNumber
                saveTvAssetMove(ticketNumber)
            } else {
                val currentDate = getCurrentDate()
                ticketList.add(TicketItem(ticketNumber, currentDate))
                ticketAdapter.updateData(ticketList)

                saveTickets()
            }
        }

        // Add text change listener to search EditText
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                ticketAdapter.filter.filter(s)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun navigate() {
        binding.imageButtonBack.setOnClickListener {
            startActivity(Intent(this, AssetActivity::class.java))
        }

        binding.btnAddList.setOnClickListener {
            startActivity(Intent(this, NewAssetTicketActivity::class.java))
        }

        binding.btnClearList.setOnClickListener {
            clearTicketList()
        }
    }

    private fun setupRecyclerView() {
        ticketAdapter = TicketAdapter(ticketList)
        binding.recyclerViewTickets.adapter = ticketAdapter
        binding.recyclerViewTickets.layoutManager = LinearLayoutManager(this)
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun saveTickets() {
        val sharedPreferences = getSharedPreferences("TicketPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val jsonString = Gson().toJson(ticketList)
        editor.putString("ticketList", jsonString)
        editor.apply()
    }

    private fun loadTickets() {
        val sharedPreferences = getSharedPreferences("TicketPrefs", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("ticketList", null)

        if (!jsonString.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<TicketItem>>() {}.type
            val savedTicketList: MutableList<TicketItem> = Gson().fromJson(jsonString, type)
            ticketList.addAll(savedTicketList)
        }
    }

    private fun saveTvAssetMove(ticketNumber: String) {
        val prefs = getSharedPreferences("TicketPrefs", MODE_PRIVATE)
        prefs.edit().putString("TV_ASSET_MOVE", ticketNumber).apply()
    }

    private fun clearTicketList() {
        ticketList.clear()
        ticketAdapter.updateData(ticketList)

        val prefs = getSharedPreferences("TicketPrefs", MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}
