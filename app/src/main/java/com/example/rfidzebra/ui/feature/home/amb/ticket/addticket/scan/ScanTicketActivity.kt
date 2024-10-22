package com.example.rfidzebra.ui.feature.home.amb.ticket.addticket.scan

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rfidzebra.databinding.ActivityScanTicketBinding
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ScanTicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanTicketBinding
    private val cameraPermissionRequestCode = 200

    private val tag = "ScanTicketActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            startCameraScanner()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                cameraPermissionRequestCode)
        }
    }

    private fun startCameraScanner() {
        binding.barcodeScanner.decodeContinuous { result ->
            result?.let {
                val scannedBarcode = it.text
                Log.e(tag, "Scanned barcode: $scannedBarcode")

                val intent = Intent()
                intent.putExtra("SCANNED_BARCODE", scannedBarcode)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermissionRequestCode && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCameraScanner()
        } else {
            // Permission denied, handle accordingly
        }
    }

    override fun onResume() {
        super.onResume()
        binding.barcodeScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.barcodeScanner.pause()
    }
}
