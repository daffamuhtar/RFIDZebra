package com.example.rfidzebra.ui.feature.home.amb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.rfidzebra.databinding.ActivityAssetBinding
import com.example.rfidzebra.ui.feature.home.amb.ticket.TicketActivity
import com.example.rfidzebra.ui.feature.navigation.NavigationActivity

class AssetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigate()
    }

    private fun navigate() {
        binding.imageButtonBack.setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java))
        }

        binding.btnSave.setOnClickListener {
            val ticketNumber = binding.etTicketNumber.text.toString()
            if (ticketNumber.isNotEmpty()) {
                val intent = Intent(this, TicketActivity::class.java)
                intent.putExtra("TICKET_NUMBER", ticketNumber)
                intent.putExtra("FROM_ASSET_ACTIVITY", true)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Input nomor tiket dahulu..", Toast.LENGTH_SHORT).show()
            }
        }

    }
}