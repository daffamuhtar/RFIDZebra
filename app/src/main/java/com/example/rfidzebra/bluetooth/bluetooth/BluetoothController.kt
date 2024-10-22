package com.example.rfidzebra.bluetooth.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.Closeable

class BluetoothController(

    private val context: Activity,
    private val bluetooth: BluetoothAdapter?, listener: BluetoothDiscoveryDeviceListener?
) :
    Closeable {

    private val broadcastReceiverDelegator: BroadcastReceiverDelegator


    private var bluetoothDiscoveryScheduled = false
    private var boundingDevice: BluetoothDevice? = null

    init {
        broadcastReceiverDelegator = listener?.let { BroadcastReceiverDelegator(context, it, this) }!!
    }

    fun isBluetoothEnabled(): Boolean {
        return bluetooth?.isEnabled == true
    }

//    fun startDiscovery() {
//        broadcastReceiverDelegator.onDeviceDiscoveryStarted()
//
//        if (ContextCompat.checkSelfPermission(
//                context, Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                context, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1
//            )
//            return
//        }
//
//        if (bluetooth!!.isDiscovering) {
//            bluetooth.cancelDiscovery()
//        }
//
//        Log.d(TAG, "Bluetooth starting discovery.")
//        if (!bluetooth.startDiscovery()) {
//            Toast.makeText(context, "Error while starting device discovery!", Toast.LENGTH_SHORT).show()
//            Log.d(TAG, "StartDiscovery returned false. Maybe Bluetooth isn't on?")
//            broadcastReceiverDelegator.onDeviceDiscoveryEnd()
//        }
//    }

    fun startDiscovery() {
        broadcastReceiverDelegator.onDeviceDiscoveryStarted()

        // This line of code is very important. In Android >= 6.0 you have to ask for the runtime
        // permission as well in order for the discovery to get the devices ids. If you don't do
        // this, the discovery won't find any device.
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        }

        // If another discovery is in progress, cancels it before starting the new one.
        if (bluetooth!!.isDiscovering) {
            bluetooth.cancelDiscovery()
        }

        // Tries to start the discovery. If the discovery returns false, this means that the
        // bluetooth has not started yet.
        Log.d(TAG, "Bluetooth starting discovery.")
        if (!bluetooth.startDiscovery()) {
            Toast.makeText(context, "Error while starting device discovery!", Toast.LENGTH_SHORT)
                .show()
            Log.d(TAG, "StartDiscovery returned false. Maybe Bluetooth isn't on?")

            // Ends the discovery.
            broadcastReceiverDelegator.onDeviceDiscoveryEnd()
        }
    }

    private fun turnOnBluetooth() {
        Log.d(TAG, "Enabling Bluetooth.")
        broadcastReceiverDelegator.onBluetoothTurningOn()

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(
                    context, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 2
                )
            }
            return
        }
        bluetooth?.enable()
    }


    fun pair(device: BluetoothDevice): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(
                    context, arrayOf(Manifest.permission.BLUETOOTH_SCAN), 3
                )
            }
            return false
        }

        if (bluetooth?.isDiscovering!!) {
            Log.d(TAG, "Bluetooth cancelling discovery.")
            bluetooth.cancelDiscovery()
        }

        Log.d(TAG, "Bluetooth bonding with device: ${deviceToString(context, device)}")
        val outcome = device.createBond()
        Log.d(TAG, "Bounding outcome: $outcome")

        if (outcome) {
            boundingDevice = device
        }
        return outcome
    }

    fun isAlreadyPaired(device: BluetoothDevice?): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the required permission and return false until permission is granted.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(
                    context, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1
                )
            }
            false
        } else {
            bluetooth!!.bondedDevices.contains(device)
        }
    }

    override fun close() {
        broadcastReceiverDelegator.close()
    }

    fun isDiscovering(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(
                    context, arrayOf(Manifest.permission.BLUETOOTH_SCAN), 2
                )
            }
            false
        } else {
            bluetooth!!.isDiscovering
        }
    }

    fun cancelDiscovery() {
        if (bluetooth != null) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(
                        context, arrayOf(Manifest.permission.BLUETOOTH_SCAN), 4
                    )
                }
                return
            }
            bluetooth.cancelDiscovery()
            broadcastReceiverDelegator.onDeviceDiscoveryEnd()
        }
    }

    fun turnOnBluetoothAndScheduleDiscovery() {
        bluetoothDiscoveryScheduled = true
        turnOnBluetooth()
    }

    fun onBluetoothStatusChanged() {
        // Does anything only if a device discovery has been scheduled.
        if (bluetoothDiscoveryScheduled) {
            when (bluetooth!!.state) {
                BluetoothAdapter.STATE_ON -> {
                    // Bluetooth is ON.
                    Log.d(TAG, "Bluetooth succesfully enabled, starting discovery")
                    startDiscovery()
                    // Resets the flag since this discovery has been performed.
                    bluetoothDiscoveryScheduled = false
                }

                BluetoothAdapter.STATE_OFF -> {
                    // Bluetooth is OFF.
                    Log.d(TAG, "Error while turning Bluetooth on.")
                    Toast.makeText(context, "Error while turning Bluetooth on.", Toast.LENGTH_SHORT).show()
                    // Resets the flag since this discovery has been performed.
                    bluetoothDiscoveryScheduled = false
                }

                else -> {}
            }
        }
    }

    fun getPairingDeviceStatus(): Int {
        boundingDevice?.let { device ->
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request the required permission and return an invalid state until permission is granted.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(
                        context, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 3
                    )
                }
                return BluetoothDevice.BOND_NONE // Return a default or invalid state when permission is not granted.
            }

            val bondState = device.bondState
            if (bondState != BluetoothDevice.BOND_BONDING) {
                boundingDevice = null
            }
            return bondState
        } ?: throw IllegalStateException("No device currently bonding")
    }


    fun isPairingInProgress(): Boolean {
        return boundingDevice != null
    }

    fun getBoundingDevice(): BluetoothDevice? {
        return boundingDevice
    }

    companion object {
        private const val TAG = "BluetoothManager"

        fun deviceToString(context: Activity, device: BluetoothDevice): String {
            return if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request the required permission and return a placeholder string.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(
                        context, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 4
                    )
                }
                "[Address: Unknown, Name: Unknown]"
            } else {
                "[Address: ${device.address}, Name: ${device.name}]"
            }
        }

        fun getDeviceName(context: Activity, device: BluetoothDevice?): String {
            return if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request the required permission and return a placeholder string.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(
                        context, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 5
                    )
                }
                "Unknown Device"
            } else {
                device?.name ?: "Unknown Device"
            }
        }
    }

}
