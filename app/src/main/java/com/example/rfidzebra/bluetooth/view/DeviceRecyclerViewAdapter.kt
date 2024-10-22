package com.example.rfidzebra.bluetooth.view

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidzebra.R
import com.example.rfidzebra.bluetooth.bluetooth.BluetoothController
import com.example.rfidzebra.bluetooth.bluetooth.BluetoothDiscoveryDeviceListener


class DeviceRecyclerViewAdapter(
    private val listener: ListInteractionListener<BluetoothDevice?>?
) :
    RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder?>(),
    BluetoothDiscoveryDeviceListener {

    private val devices: MutableList<BluetoothDevice>

    private var bluetooth: BluetoothController? = null

    init {
        devices = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_device_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = devices[position]
        holder.mImageView.setImageResource(getDeviceIcon(devices[position]))
        if (ActivityCompat.checkSelfPermission(
                holder.mView.context, // Use context from the View
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Handle the case where permission is not granted.
            // You can show a message or disable functionality here.
            holder.mDeviceNameView.text = "Permission required"
            holder.mDeviceAddressView.text = "Bluetooth permission not granted"
            holder.mView.setOnClickListener(null) // Disable click if needed
            return
        }
        holder.mDeviceNameView.text = devices[position].name
        holder.mDeviceAddressView.text = devices[position].address
        holder.mView.setOnClickListener { listener?.onItemClick(holder.mItem) }
    }

    private fun getDeviceIcon(device: BluetoothDevice): Int {
        return if (bluetooth!!.isAlreadyPaired(device)) {
            R.drawable.ic_bluetooth_connected_black_24dp
        } else {
            R.drawable.ic_bluetooth_black_24dp
        }
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    override fun onDeviceDiscovered(device: BluetoothDevice?) {
        listener!!.endLoading(true)
        if (device != null) {
            devices.add(device)
        }
        notifyDataSetChanged()
    }

    override fun onDeviceDiscoveryStarted() {
        cleanView()
        listener!!.startLoading()
    }

    fun cleanView() {
        devices.clear()
        notifyDataSetChanged()
    }

    override fun setBluetoothController(bluetooth: BluetoothController?) {
        this.bluetooth = bluetooth
    }

    override fun onDeviceDiscoveryEnd() {
        listener!!.endLoading(false)
    }

    override fun onBluetoothStatusChanged() {
        // Notifies the Bluetooth controller.
        bluetooth!!.onBluetoothStatusChanged()
    }

    override fun onBluetoothTurningOn() {
        listener!!.startLoading()
    }

    override fun onDevicePairingEnded() {
        if (bluetooth!!.isPairingInProgress()) {
            val device: BluetoothDevice? = bluetooth!!.getBoundingDevice()
            when (bluetooth!!.getPairingDeviceStatus()) {
                BluetoothDevice.BOND_BONDING -> {}
                BluetoothDevice.BOND_BONDED -> {
                    // Successfully paired.
                    listener!!.endLoadingWithDialog(false, device)

                    // Updates the icon for this element.
                    notifyDataSetChanged()
                }

                BluetoothDevice.BOND_NONE ->                     // Failed pairing.
                    listener!!.endLoadingWithDialog(true, device)
            }
        }
    }

    inner class ViewHolder(

        val mView: View
    ) : RecyclerView.ViewHolder(mView) {

        val mImageView: ImageView = mView.findViewById<View>(R.id.device_icon) as ImageView

        val mDeviceNameView: TextView = mView.findViewById<View>(R.id.device_name) as TextView

        val mDeviceAddressView: TextView = mView.findViewById<View>(R.id.device_address) as TextView

        var mItem: BluetoothDevice? = null

        override fun toString(): String {
            return super.toString() + " '" + BluetoothController.deviceToString(mView.context as Activity, mItem!!) + "'"
        }

    }
}

