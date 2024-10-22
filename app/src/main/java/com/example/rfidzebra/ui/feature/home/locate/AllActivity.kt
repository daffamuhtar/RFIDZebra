package com.example.rfidzebra.ui.feature.home.locate

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rfidzebra.R
import com.example.rfidzebra.RFIDHandler
import com.example.rfidzebra.databinding.ActivityAllBinding
import com.example.rfidzebra.databinding.ActivityMainBinding
import com.zebra.rfid.api3.BEEPER_VOLUME
import com.zebra.rfid.api3.RFIDReader

class AllActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}






