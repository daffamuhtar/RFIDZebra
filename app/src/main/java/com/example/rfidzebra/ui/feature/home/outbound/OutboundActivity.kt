package com.example.rfidzebra.ui.feature.home.outbound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rfidzebra.databinding.ActivityOutboundBinding
import com.example.rfidzebra.ui.feature.home.outbound.list.OutboundListActivity
import com.example.rfidzebra.ui.feature.navigation.NavigationActivity

class OutboundActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOutboundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutboundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigate()

    }

    private fun navigate() {
        binding.imageButtonBack.setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java))
        }

        binding.btnSaveOutbound.setOnClickListener {
            startActivity(Intent(this, OutboundListActivity::class.java))
        }

    }
}