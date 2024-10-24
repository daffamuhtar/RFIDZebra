package com.example.rfidzebra

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidzebra.databinding.ActivityMainBinding
import com.example.rfidzebra.ui.TagItem
import com.zebra.rfid.api3.BatteryStatistics
import com.zebra.rfid.api3.TagData

class MainActivity : AppCompatActivity(), RFIDHandler.ResponseHandlerInterface {

    lateinit var binding: ActivityMainBinding
    private var rfidHandler: RFIDHandler? = null

    private var batteryStatistics: BatteryStatistics? = null

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    private lateinit var adapter: TagAdapter
    private val tagDataList = mutableListOf<TagData>()
    private val tagCountMap = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rfidHandler = RFIDHandler(this)

        // Inisialisasi BatteryStatistics
        batteryStatistics = BatteryStatistics()

        //Scanner Initializations
        //Handling Runtime BT permissions for Android 12 and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ),
                    BLUETOOTH_PERMISSION_REQUEST_CODE
                )
            } else {
                rfidHandler!!.onCreate()
            }
        } else {
            rfidHandler!!.onCreate()
        }

        checkBluetoothState()

        // Clear Tag ID button action
        binding.clearTagIDButton.setOnClickListener {
            clearTagID()
        }

        binding.connectButton.setOnClickListener {
            val result = rfidHandler?.connectReader()
            binding.textViewStatusrfid.text = result.toString()
        }

        binding.buttonHigh.setOnClickListener {
            if (isRFIDConnected()) {
                setBeeperVolume(com.zebra.rfid.api3.BEEPER_VOLUME.HIGH_BEEP)
            } else {
                showToastError("RFID not connected. Please connect first.")
            }
        }

        binding.buttonQuiet.setOnClickListener {
            if (isRFIDConnected()) {
                setBeeperVolume(com.zebra.rfid.api3.BEEPER_VOLUME.QUIET_BEEP)
            } else {
                showToastError("RFID not connected. Please connect first.")
            }
        }

//        binding.buttonBattery.setOnClickListener {
//            if (isRFIDConnected()) {
//                displayBatteryStatistics()
//            } else {
//                showToastError("RFID not connected. Please connect first.")
//            }
//        }

//        binding.buttonLocation.setOnClickListener {
//            if (isRFIDConnected()) {
//                val tagID = binding.editTextSearchLocate.text.toString().trim()
//                if (tagID.isNotEmpty()) {
//                    performTagLocationing(tagID)
//                } else {
//                    showToastError("Please enter a valid Tag ID.")
//                }
//            } else {
//                showToastError("RFID not connected. Please connect first.")
//            }
//        }

//        // Set up RecyclerView
//        tagLocateAdapter = TagAdapter(tagList)
//        binding.tagLocateRecyclerView.adapter = tagLocateAdapter
//        binding.tagLocateRecyclerView.layoutManager = LinearLayoutManager(this)
//
//        // Button to add Tag ID
//        binding.buttonInput.setOnClickListener {
//            val tagID = binding.editTextSearchLocate.text.toString().trim()
//            if (tagID.isNotEmpty()) {
//                // Add new tag to the list and update the RecyclerView
//                tagList.add(TagItem(tagID, 0, "0%"))
//                tagLocateAdapter.notifyDataSetChanged()
//
//                // Clear the input field
//                binding.editTextSearchLocate.text.clear()
//
////                // Start the location process for the added tag
////                rfidHandler!!.setMultiTagLocate(tagID)
//            } else {
//                showToastError("Please enter a valid Tag ID.")
//            }
//        }

        // Initialize RecyclerView and set the adapter
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTags)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TagAdapter(tagDataList, tagCountMap)
        recyclerView.adapter = adapter

    }

    private fun isRFIDConnected(): Boolean {
        return rfidHandler?.reader?.isConnected == true
    }

//    private fun performTagLocationing(tagID: String) {
//        try {
//            rfidHandler?.setLocate(tagID)
//        } catch (e: Exception) {
//            showToastError("Failed to perform tag locationing: ${e.message}")
//        }
//    }

    private fun displayBatteryStatistics() {
        try {
            rfidHandler?.setBattery()
        } catch (e: Exception) {
            showToastError("Failed to set beeper volume")
        }
    }

    private fun setBeeperVolume(volume: com.zebra.rfid.api3.BEEPER_VOLUME) {
        try {
            rfidHandler?.setBeeperVolume(volume)
            showToastSuccess("Beeper volume set to: $volume")
        } catch (e: Exception) {
            showToastError("Failed to set beeper volume")
        }
    }

    // Helper method to show success toast
    private fun showToastSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Helper method to show error toast
    private fun showToastError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Check Bluetooth state
    private fun checkBluetoothState() {
        if (bluetoothAdapter == null) {
            binding.textViewStatusrfid.text = "Bluetooth is not supported on this device."
            binding.connectButton.isEnabled = false
            Toast.makeText(this, "Bluetooth is turned off. Please enable it to connect.", Toast.LENGTH_SHORT).show()
        } else if (!bluetoothAdapter!!.isEnabled) {
            binding.textViewStatusrfid.text = "Bluetooth is turned off. Please enable it."
            binding.connectButton.isEnabled = false
            Toast.makeText(this, "Bluetooth is turned off. Please enable it to connect.", Toast.LENGTH_SHORT).show()
        } else {
            binding.textViewStatusrfid.text = "Bluetooth is turned on."
            binding.connectButton.isEnabled = true
        }
    }

    // Listen for Bluetooth state changes
    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                    BluetoothAdapter.STATE_ON -> {
                        binding.textViewStatusrfid.text = "Bluetooth is turned on."
                        binding.connectButton.isEnabled = true
                    }
                    BluetoothAdapter.STATE_OFF -> {
                        binding.textViewStatusrfid.text = "Bluetooth is turned off. Please enable it."
                        binding.connectButton.isEnabled = false
                        Toast.makeText(context, "Bluetooth is turned off. Please enable it to connect.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(bluetoothReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(bluetoothReceiver)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.connectButton.isEnabled = true
            } else {
                Toast.makeText(this, "Bluetooth Permissions not granted", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPostResume() {
        super.onPostResume()
        binding.textViewStatusrfid.text = "Ready to connect"
    }

    override fun onDestroy() {
        super.onDestroy()
        rfidHandler!!.onDestroy()
    }

    fun StartInventory(view: View?) {
        if (isRFIDConnected()) {
            tagDataList.clear()
            tagCountMap.clear()
            adapter.notifyDataSetChanged()

            rfidHandler!!.performInventory()

            binding.TestButton.isEnabled = false
            binding.TestButton2.isEnabled = true

            Log.d("RecyclerView", "Inventory started, cleared previous data")

            Handler(Looper.getMainLooper()).postDelayed({
                StopInventory(view)
            }, 2000)
        } else {
            showToastError("RFID not connected. Please connect first.")
        }
    }

    fun StopInventory(view: View?) {
        if (isRFIDConnected()) {
            rfidHandler!!.stopInventory()

            binding.TestButton.isEnabled = true
            binding.TestButton2.isEnabled = false
        } else {
            showToastError("RFID not connected. Please connect first.")
        }
    }


    fun clearTagID() {
        tagDataList.clear()
        tagCountMap.clear()
        adapter.notifyDataSetChanged()
        Log.d("RecyclerView", "TAG data cleared")
    }

    override fun handleTagdata(tagData: Array<TagData?>?) {
        if (tagData != null) {
            for (data in tagData) {
                data?.let {
                    val currentCount = tagCountMap.getOrDefault(it.tagID, 0)
                    tagCountMap[it.tagID] = currentCount + 1

                    if (!tagDataList.any { tag -> tag.tagID == it.tagID }) {
                        tagDataList.add(it)
                    }
                }
            }

            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun handleTriggerPress(pressed: Boolean) {
        if (pressed) {
            runOnUiThread {
                tagDataList.clear()
                tagCountMap.clear()
                adapter.notifyDataSetChanged()
            }
            rfidHandler!!.performInventory()
        } else {
            rfidHandler!!.stopInventory()
        }
    }

    override fun barcodeData(`val`: String?) {
        TODO("Not yet implemented")
    }

    override fun sendToast(`val`: String?) {
        runOnUiThread { Toast.makeText(this@MainActivity, `val`, Toast.LENGTH_SHORT).show() }
    }

    companion object {
        private const val BLUETOOTH_PERMISSION_REQUEST_CODE = 100
    }
}