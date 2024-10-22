package com.example.rfidzebra.bluetooth

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rfidzebra.R
import com.example.rfidzebra.bluetooth.bluetooth.BluetoothController
import com.example.rfidzebra.bluetooth.view.DeviceRecyclerViewAdapter
import com.example.rfidzebra.bluetooth.view.ListInteractionListener
import com.example.rfidzebra.databinding.ActivityBluetoothBinding
import com.google.android.material.snackbar.Snackbar

class BluetoothActivity : AppCompatActivity(), ListInteractionListener<BluetoothDevice?> {

    private var bluetooth: BluetoothController? = null
    private var bondingProgressDialog: ProgressDialog? = null
    private var recyclerViewAdapter: DeviceRecyclerViewAdapter? = null

    private lateinit var binding: ActivityBluetoothBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SystemClock.sleep(resources.getInteger(R.integer.splashscreen_duration).toLong())
        setTheme(R.style.AppTheme_NoActionBar)
        setSupportActionBar(binding.toolbar)

//        // Ads.
//        MobileAds.initialize(this)
//        adContainer = findViewById<LinearLayout>(R.id.ad_container)
//        reloadAd()

        // Sets up the RecyclerView.
        recyclerViewAdapter = DeviceRecyclerViewAdapter(this)
        binding.list.layoutManager = LinearLayoutManager(this)

        // Sets the view to show when the dataset is empty. IMPORTANT : this method must be called
        // before recyclerView.setAdapter().
        binding.list.setEmptyView(binding.emptyList)

        // Sets the view to show during progress.
        binding.list.setProgressView(binding.progressBar)
        binding.list.adapter = recyclerViewAdapter

        // [#11] Ensures that the Bluetooth is available on this device before proceeding.
        val hasBluetooth = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
        if (!hasBluetooth) {
            val dialog: AlertDialog = AlertDialog.Builder(this@BluetoothActivity).create()
            dialog.setTitle(getString(R.string.bluetooth_not_available_title))
            dialog.setMessage(getString(R.string.bluetooth_not_available_message))
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK"
            ) { _, _ -> // Closes the dialog and terminates the activity.
                dialog.dismiss()
                finish()
            }
            dialog.setCancelable(false)
            dialog.show()
        }

        // Sets up the bluetooth controller.
        bluetooth =
            BluetoothController(this, BluetoothAdapter.getDefaultAdapter(), recyclerViewAdapter)

        binding.fab.setOnClickListener { view ->
            // If the bluetooth is not enabled, turns it on.
            if (!bluetooth!!.isBluetoothEnabled()) {
                Snackbar.make(view, R.string.enabling_bluetooth, Snackbar.LENGTH_SHORT).show()
                bluetooth!!.turnOnBluetoothAndScheduleDiscovery()
            } else {
                //Prevents the user from spamming the button and thus glitching the UI.
                if (!bluetooth!!.isDiscovering()) {
                    // Starts the discovery.
                    Snackbar.make(view, R.string.device_discovery_started, Snackbar.LENGTH_SHORT)
                        .show()
                    bluetooth!!.startDiscovery()
                } else {
                    Snackbar.make(view, R.string.device_discovery_stopped, Snackbar.LENGTH_SHORT)
                        .show()
                    bluetooth!!.cancelDiscovery()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_about) {
            showAbout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAbout() {
        // Inflate the about message contents
        val messageView: View = layoutInflater.inflate(R.layout.about, null, false)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setTitle(R.string.app_name)
        builder.setView(messageView)
        builder.create()
        builder.show()
    }

    override fun onItemClick(item: BluetoothDevice?) {
        Log.d(TAG, "Item clicked : " + item?.let { BluetoothController.deviceToString(this, it) })
        if (bluetooth!!.isAlreadyPaired(item)) {
            Log.d(TAG, "Device already paired!")
            Toast.makeText(this, R.string.device_already_paired, Toast.LENGTH_SHORT).show()
        } else {
            Log.d(TAG, "Device not paired. Pairing.")
            val outcome: Boolean = item?.let { bluetooth!!.pair(it) } == true

            // Prints a message to the user.
            val deviceName: String = BluetoothController.getDeviceName(this, item)
            if (outcome) {
                // The pairing has started, shows a progress dialog.
                Log.d(TAG, "Showing pairing dialog")
                bondingProgressDialog = ProgressDialog.show(
                    this, "",
                    "Pairing with device $deviceName...", true, false
                )
            } else {
                Log.d(
                    TAG,
                    "Error while pairing with device $deviceName!"
                )
                Toast.makeText(
                    this,
                    "Error while pairing with device $deviceName!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun startLoading() {
        binding.list.startLoading()

        // Changes the button icon.
        binding.fab.setImageResource(R.drawable.ic_bluetooth_searching_white_24dp)
    }

    override fun endLoading(partialResults: Boolean) {
        binding.list.endLoading()

        // If discovery has ended, changes the button icon.
        if (!partialResults) {
            binding.fab.setImageResource(R.drawable.ic_bluetooth_white_24dp)
        }
    }

    override fun endLoadingWithDialog(error: Boolean, element: BluetoothDevice?) {
        if (bondingProgressDialog != null) {
            val view: View = findViewById(R.id.main_content)
            val message: String
            val deviceName: String = BluetoothController.getDeviceName(this, element)

            // Gets the message to print.
            message = if (error) {
                "Failed pairing with device $deviceName!"
            } else {
                "Succesfully paired with device $deviceName!"
            }

            // Dismisses the progress dialog and prints a message to the user.
            bondingProgressDialog!!.dismiss()
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()

            // Cleans up state.
            bondingProgressDialog = null
        }
    }

    override fun onDestroy() {
        bluetooth!!.close()
        super.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
        // Stops the discovery.
        if (bluetooth != null) {
            bluetooth!!.cancelDiscovery()
        }
        // Cleans the view.
        recyclerViewAdapter?.cleanView()
    }

    override fun onStop() {
        super.onStop()
        // Stoops the discovery.
        bluetooth?.cancelDiscovery()
    }


    companion object {
        private const val TAG = "MainActivity"
    }
}