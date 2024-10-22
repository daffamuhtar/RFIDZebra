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

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var requestBluetoothPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestBluetoothConnectPermissionLauncher: ActivityResultLauncher<String>
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var bluetoothReceiver: BluetoothReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothReceiver = BluetoothReceiver(binding)

        // Initialize permission launchers
        requestBluetoothPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                requestBluetoothConnectPermission()
            } else {
                Toast.makeText(this, "Bluetooth permission is required", Toast.LENGTH_LONG).show()
            }
        }

        requestBluetoothConnectPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                checkBluetoothState()
            } else {
                Toast.makeText(this, "Bluetooth connect permission is required", Toast.LENGTH_LONG).show()
            }
        }

        // Request Bluetooth permission if not already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
            requestBluetoothConnectPermission()
        } else {
            requestBluetoothPermissionLauncher.launch(Manifest.permission.BLUETOOTH)
        }

        // Set up Bluetooth Switch listener
        binding.bluetoothSwitchCompat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableBluetooth()
            } else {
                promptUserToTurnOffBluetooth()
            }
        }

        binding.searchLabel.setOnClickListener {

            // Checks if Bluetooth Adapter is present
            if (bluetoothAdapter == null) {
                Toast.makeText(applicationContext, "Bluetooth Not Supported", Toast.LENGTH_SHORT).show()
            } else {
                // Arraylist of all the bonded (paired) devices
                val pairedDevices = bluetoothAdapter.bondedDevices
                if (pairedDevices.size > 0) {
                    for (device in pairedDevices) {

                        // get the device name
                        val deviceName = device.name

                        // get the mac address
                        val macAddress = device.address

                        // append in the two separate views
                        binding.deviceList.append("$deviceName\n")
                        binding.addressList.append("$macAddress\n")

                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(bluetoothReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(bluetoothReceiver)
    }

    private fun requestBluetoothConnectPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            checkBluetoothState()
        } else {
            requestBluetoothConnectPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
        }
    }

    private fun checkBluetoothState() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show()
            return
        }

        binding.bluetoothSwitchCompat.isChecked = bluetoothAdapter.isEnabled
        if (!bluetoothAdapter.isEnabled) {
            Toast.makeText(this, "Please turn on Bluetooth", Toast.LENGTH_LONG).show()
        }
    }

    private fun enableBluetooth() {
        if (bluetoothAdapter == null) return
        if (!bluetoothAdapter.isEnabled) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                requestBluetoothConnectPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
                Toast.makeText(this, "Bluetooth is on", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun promptUserToTurnOffBluetooth() {
        Toast.makeText(this, "Please turn off Bluetooth manually in the Bluetooth settings.", Toast.LENGTH_LONG).show()
        openBluetoothSettings()
    }

    private fun openBluetoothSettings() {
        val intent = Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS)
        startActivity(intent)
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }
}
