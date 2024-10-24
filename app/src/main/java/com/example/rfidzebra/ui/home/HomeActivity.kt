package com.example.rfidzebra.ui.home

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.rfidzebra.BluetoothReceiver
import com.example.rfidzebra.databinding.ActivityHomeBinding
import com.google.android.material.slider.Slider

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.slider.addOnChangeListener { _, value, _ ->
//            Toast.makeText(this, "Slider Value: $value", Toast.LENGTH_SHORT).show()
//        }

        binding.slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                Toast.makeText(this@HomeActivity, "Started tracking slider", Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(slider: Slider) {
                val sliderValue = slider.value.toInt()
                Toast.makeText(this@HomeActivity, "Slider Stopped at: $sliderValue", Toast.LENGTH_SHORT).show()            }
        })

    }

}
