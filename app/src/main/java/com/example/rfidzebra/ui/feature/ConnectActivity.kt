package com.example.rfidzebra.ui.feature

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.rfidzebra.R
import com.example.rfidzebra.databinding.ActivityConnectBinding
import com.example.rfidzebra.ui.feature.encapsulation.interf.RFIDSDK
import com.example.rfidzebra.ui.feature.encapsulation.obj.RFIDSDKFactory
import com.example.rfidzebra.ui.feature.home.locate.AllActivity
import com.zebra.rfid.api3.BEEPER_VOLUME
import com.zebra.rfid.api3.RFIDReader
import com.zebra.rfid.api3.TagData

class ConnectActivity : AppCompatActivity(), RFIDHandlerNew.ResponseHandlerInterface {

    lateinit var binding: ActivityConnectBinding

    private var rfidHandler: RFIDHandlerNew? = null

    private lateinit var deviceListAdapter: ArrayAdapter<String>
    private val pairedDevices = mutableListOf<BluetoothDevice>()
    private lateinit var pairedDeviceListAdapter: ArrayAdapter<String>

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        BluetoothAdapter.getDefaultAdapter()
    }

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    handleBluetoothDevice(intent)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rfidHandler = RFIDHandlerNew(this)

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

        // Set up connect button
        binding.connectButton.setOnClickListener {
            val result = rfidHandler?.connectReader()
            binding.textConnect.text = result.toString()
        }

        // Set up navButton to navigate to another activity
        binding.navButton.setOnClickListener {
            val intent = Intent(this, AllActivity::class.java)
            startActivity(intent)
        }

        val actionBar = supportActionBar
        if (actionBar != null) {
            val textColor = Color.parseColor("#FDFDFD")
            actionBar.title = "Bluetooth App"
            actionBar.setLogo(R.drawable.bluetooth)
            actionBar.setDisplayShowTitleEnabled(true)
            actionBar.title = Html.fromHtml("<font color='$textColor'>Bluetooth App</font>")
        }

        pairedDeviceListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        binding.pairedDeviceListView.adapter = pairedDeviceListAdapter

        // Register BroadcastReceiver to listen for ACTION_FOUND
        registerReceiver(bluetoothReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))

        // Populate the paired devices list
        populatePairedDevicesList()

        deviceListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        binding.deviceListView.adapter = deviceListAdapter
        showNearbyDevices()

        binding.enableBluetoothButton.setOnClickListener {
            enableBluetooth()
        }

        binding.showNearbyDevicesButton.setOnClickListener {
            showNearbyDevices()
        }

        binding.pairedDeviceListView.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            showToastInfo("Clicked on: $selectedItem")
            // Retrieve the BluetoothDevice object associated with the clicked item
            val selectedDeviceAddress =
                selectedItem.split(" - ")[1] // Assuming the format is "Name - Address"
            val bluetoothDevice = pairedDevices.find { it.address == selectedDeviceAddress }
            // Pass the BluetoothDevice object to another function
            bluetoothDevice?.let { device ->
                showUnpairDialog(device)
            }
        }

        binding.deviceListView.setOnItemClickListener { _, _, position, _ ->
            binding.progressBar.visibility = View.VISIBLE
            val deviceInfo = deviceListAdapter.getItem(position)
            deviceInfo?.let {
                val address = it.split(" - ")[1] // Assuming the format is "Name - Address"
                val bluetoothDevice = bluetoothAdapter?.getRemoteDevice(address)
                bluetoothDevice?.let { device ->
                    pairDevice(device)
                }
            }
            // progressBar.visibility= View.GONE
        }

        binding.refreshButton.setOnClickListener {
            // Refresh paired devices list
            updatePairedDevicesList()

            // Refresh nearby devices list
            deviceListAdapter.clear() // Clear current list
            showNearbyDevices() // Start discovery again

            // Provide feedback to the user
            showToastInfo("Device lists refreshed")
        }

        requestBluetoothPermissions()

    }

    private fun requestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_BLUETOOTH_PERMISSIONS
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun pairDevice(device: BluetoothDevice) {
        when (device.bondState) {
            BluetoothDevice.BOND_BONDED -> {
                showUnpairDialog(device)
                showToastInfo("Device ${device.name} is already paired")
                binding.progressBar.visibility = View.GONE
                updatePairedDevicesList() // Update the list of paired devices
            }

            BluetoothDevice.BOND_NONE -> {
                try {
                    val method = device.javaClass.getMethod("createBond")
                    method.invoke(device)
                    showToastInfo("Pairing with device: ${device.address}")
                    populatePairedDevicesList()
                    // Notify the adapter about the changes in the data set
                    pairedDeviceListAdapter.notifyDataSetChanged()


                } catch (e: Exception) {
                    showToastError("Pairing failed: ${e.message}")
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun populatePairedDevicesList() {
        // Clear the existing list
        pairedDevices.clear()
        pairedDeviceListAdapter.clear()

        // Get the list of paired devices
        val pairedDevicesSet = bluetoothAdapter?.bondedDevices

        // Add paired devices to the list and adapter
        pairedDevicesSet?.forEach { device ->
            pairedDevices.add(device)
            pairedDeviceListAdapter.add("${device.name} - ${device.address}")
        }
    }

    @SuppressLint("MissingPermission")
    private fun updatePairedDevicesList() {
        // Clear the existing list
        pairedDevices.clear()
        pairedDeviceListAdapter.clear()

        // Get the list of paired devices
        val pairedDevicesSet = bluetoothAdapter?.bondedDevices

        // Add paired devices to the list and adapter
        pairedDevicesSet?.forEach { device ->
            pairedDevices.add(device)
            pairedDeviceListAdapter.add("${device.name} - ${device.address}")
        }

        // Notify the adapter about the changes in the data set
        pairedDeviceListAdapter.notifyDataSetChanged()
    }

    @SuppressLint("MissingPermission")
    private fun showUnpairDialog(device: BluetoothDevice) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Unpair Device")
        builder.setMessage("Do you want to unpair ${device.name}?")
        builder.setPositiveButton("Yes") { _, _ ->

            unpairDevice(device)
            // After unpairing, update the paired devices list
            updatePairedDevicesList()

        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    @SuppressLint("MissingPermission")
    private fun unpairDevice(device: BluetoothDevice) {
        try {
            val method = device.javaClass.getMethod("removeBond")
            method.invoke(device)
            showToastSuccess("Device ${device.name} unpaired successfully")
            // Remove device from the list of paired devices
            pairedDevices.remove(device)
            // Remove device from the list view
            pairedDeviceListAdapter.remove("${device.name} - ${device.address}")
            populatePairedDevicesList()
        } catch (e: Exception) {
            showToastError("Failed to unpair device: ${e.message}")
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableBluetooth() {
        if (bluetoothAdapter == null) {
            showToastError("Device does not support Bluetooth.")
            return
        }

        if (!bluetoothAdapter!!.isEnabled) {
            // Check if the permission is granted before enabling Bluetooth
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {

                // If permission is not granted, request it
                requestBluetoothPermission()
            } else {
                // Bluetooth is not enabled, start the enable Bluetooth intent
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }
        } else {
            showToastWarning("Bluetooth is already enabled")
        }
    }

    private fun requestBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                REQUEST_ENABLE_BT
            )
        }
    }

    private fun showNearbyDevices() {
        //deviceListAdapter.clear()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            startBluetoothDiscovery()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }

    private fun startBluetoothDiscovery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.startDiscovery()
            } else {
                requestBluetoothPermissions()
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.startDiscovery()
            } else {
                requestBluetoothPermissions()
            }
        }
    }

    private val discoveredDevicesSet = mutableSetOf<String>()

    @SuppressLint("MissingPermission")
    private fun handleBluetoothDevice(intent: Intent) {
        val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        device?.let {
            val deviceInfo = "${it.name} - ${it.address}"
            if (deviceInfo !in discoveredDevicesSet) {
                discoveredDevicesSet.add(deviceInfo)
                deviceListAdapter.add(deviceInfo)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            showToastSuccess("Bluetooth is enabled")
            showNearbyDevices()
        } else {
            showToastError("Failed to enable Bluetooth")
        }
    }

    private fun showToastSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showToastError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showToastInfo(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showToastWarning(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        populatePairedDevicesList()
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show()
        binding.progressBar.visibility = View.GONE
        registerReceiver(bluetoothReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
    }

    override fun onPause() {

        super.onPause()
        unregisterReceiver(bluetoothReceiver)
    }

    //find item bisa nampilin progress bar
    //tingkat sinyal

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(bluetoothReceiver, filter)
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

        when (requestCode) {
            REQUEST_BLUETOOTH_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permissions granted, start Bluetooth discovery
                    startBluetoothDiscovery()
                } else {
                    showToastError("Bluetooth permissions denied.")
                }
            }
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startBluetoothDiscovery()
                } else {
                    showToastError("Location permission denied.")
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        binding.textConnect.text = "Ready to connect"
    }

    override fun onDestroy() {
        super.onDestroy()
        rfidHandler!!.onDestroy()
    }

    companion object {
        private const val BLUETOOTH_PERMISSION_REQUEST_CODE = 100
        private const val REQUEST_ENABLE_BT = 1
        private const val REQUEST_LOCATION_PERMISSION = 2
        private const val REQUEST_BLUETOOTH_PERMISSIONS = 1
    }

    override fun handleTagdata(tagData: Array<TagData?>?) {
        TODO("Not yet implemented")
    }

    override fun handleTriggerPress(pressed: Boolean) {
        TODO("Not yet implemented")
    }

    override fun barcodeData(`val`: String?) {
        TODO("Not yet implemented")
    }

    override fun sendToast(`val`: String?) {
        runOnUiThread { Toast.makeText(this@ConnectActivity, `val`, Toast.LENGTH_SHORT).show() }
    }

//    fun onClick(v: View) {
//        when (v.id) {
//            R.id.connectButton -> onClickCreateReader()
//            else -> {}
//        }
//    }
//
//    private fun getCnntState(): ConnectionState? {
//        return mRfidMgr!!.connectionState
//    }
//
//    private fun isConnected(): Boolean {
//        return getCnntState() == ConnectionState.STATE_CONNECTED
//    }
//
//    private fun onClickCreateReader() {
//        if (isConnected()) {
//            mRfidMgr!!.createReader()
//            mMyHandler!!.sendEmptyMessage(MSG_CREATING_READER)
//        } else {
//            Toast.makeText(this, getString(R.string.bluetooth_not_connected), Toast.LENGTH_SHORT)
//                .show()
//        }
//    }
//
//
//    private class MyHandler private constructor(act: ConnectActivity) :
//        Handler() {
//        private val ref: WeakReference<*>
//
//        init {
//            ref = WeakReference<ConnectActivity>(act)
//        }
//
//        override fun handleMessage(msg: Message) {
//            super.handleMessage(msg)
//            if (ref.get() != null) {
//                (ref.get() as ConnectActivity?).handleMessage(msg)
//            }
//        }
//    }


}