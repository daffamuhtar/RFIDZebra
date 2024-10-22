package com.example.rfidzebra

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.rfidzebra.databinding.ActivityHomeBinding

class BluetoothReceiver(private val binding: ActivityHomeBinding) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            binding.bluetoothSwitchCompat.isChecked = state == BluetoothAdapter.STATE_ON
        }
    }
}