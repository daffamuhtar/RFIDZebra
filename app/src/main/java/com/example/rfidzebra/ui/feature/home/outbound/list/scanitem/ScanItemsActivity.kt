package com.example.rfidzebra.ui.feature.home.outbound.list.scanitem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rfidzebra.databinding.ActivityScanItemsBinding

class ScanItemsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanItemsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}