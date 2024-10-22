package com.example.rfidzebra.bluetooth.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import java.io.Closeable


class BroadcastReceiverDelegator(
    private val context: Context,
    private val listener: BluetoothDiscoveryDeviceListener, bluetooth: BluetoothController?
) :
    BroadcastReceiver(), Closeable {
    private val tag = "BroadcastReceiver"
    init {
        listener.setBluetoothController(bluetooth)

        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        context.registerReceiver(this, filter)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d(tag, "Incoming intent : $action")
        when (action) {
            BluetoothDevice.ACTION_FOUND -> {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                Log.d(
                    tag, "Device discovered! " + BluetoothController.deviceToString(
                        context as Activity, device!!
                    )
                )
                listener.onDeviceDiscovered(device)
            }

            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                // Discovery has ended.
                Log.d(tag, "Discovery ended.")
                listener.onDeviceDiscoveryEnd()
            }

            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                // Discovery state changed.
                Log.d(tag, "Bluetooth state changed.")
                listener.onBluetoothStatusChanged()
            }

            BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                // Pairing state has changed.
                Log.d(tag, "Bluetooth bonding state changed.")
                listener.onDevicePairingEnded()
            }

            else -> {}
        }
    }

    fun onDeviceDiscoveryStarted() {
        listener.onDeviceDiscoveryStarted()
    }

    fun onDeviceDiscoveryEnd() {
        listener.onDeviceDiscoveryEnd()
    }

    fun onBluetoothTurningOn() {
        listener.onBluetoothTurningOn()
    }

    override fun close() {
        context.unregisterReceiver(this)
    }
}
