package com.example.rfidzebra.ui.feature.home.outbound.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rfidzebra.databinding.ActivityOutboundListBinding
import com.example.rfidzebra.ui.feature.home.outbound.OutboundActivity

class OutboundListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOutboundListBinding

    private lateinit var outboundAdapter: OutboundAdapter
    private val dummyTickets = listOf(
        OutboundItem("E2801170000002095B90F197", "10/23/2022", 1),
        OutboundItem("E2801170000002095B90F1B7", "11/23/2022", 2),
        OutboundItem("E280689400005024B078209A", "12/23/2022", 3),
        OutboundItem("E280689400004024B078209B", "01/23/2023", 4),
        OutboundItem("E280689400005024B078209C", "02/23/2023", 5)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutboundListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigate()
        setupRecyclerView()


    }

    private fun navigate() {
        binding.imageButtonBack.setOnClickListener {
            startActivity(Intent(this, OutboundActivity::class.java))
        }

        binding.btnScanItemOutbound.setOnClickListener {
            startActivity(Intent(this, OutboundActivity::class.java))
        }

    }

    private fun setupRecyclerView() {
        outboundAdapter = OutboundAdapter(dummyTickets)
        binding.recyclerViewTicketsOutbound.adapter = outboundAdapter
        binding.recyclerViewTicketsOutbound.layoutManager = LinearLayoutManager(this)
    }

}