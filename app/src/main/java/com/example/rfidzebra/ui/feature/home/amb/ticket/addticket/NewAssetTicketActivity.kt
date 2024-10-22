package com.example.rfidzebra.ui.feature.home.amb.ticket.addticket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.rfidzebra.databinding.ActivityNewAssetTicketBinding
import com.example.rfidzebra.ui.feature.home.amb.ticket.TicketActivity
import com.example.rfidzebra.ui.feature.home.amb.ticket.addticket.scan.ScanTicketActivity

class NewAssetTicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewAssetTicketBinding

    private val scanBarcodeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val scannedBarcode = result.data?.getStringExtra("SCANNED_BARCODE")
            scannedBarcode?.let {
                binding.etAddTicket.setText(it) // Set the scanned barcode to etAddTicket
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAssetTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigate()

        binding.btnSaveTicket.setOnClickListener {
            val ticketNumber = binding.etAddTicket.text.toString()
            if (ticketNumber.isNotEmpty()) {
                val intent = Intent(this, TicketActivity::class.java)
                intent.putExtra("TICKET_NUMBER", ticketNumber)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Input tiket dahulu..", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun navigate() {
        binding.imageButtonBack.setOnClickListener {
            startActivity(Intent(this, TicketActivity::class.java))
        }

        binding.btnScanQR.setOnClickListener {
            val intent = Intent(this, ScanTicketActivity::class.java)
            scanBarcodeLauncher.launch(intent) // Launch ScanTicketActivity for result
        }
    }

}