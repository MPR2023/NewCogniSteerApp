package com.example.blebeaconapp

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.ParcelUuid
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.blebeaconapp.ui.theme.BLEBeaconAppTheme

class MainActivity : ComponentActivity() {

    private val requestBluetoothCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (hasPermissions()) {
            startBleScan()
        } else {
            requestPermissions()
        }

        setContent {
            BLEBeaconAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }

    private fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestBluetoothCode)
    }

    @SuppressLint("MissingPermission")
    private fun startBleScan() {
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val bleScanner = bluetoothAdapter.bluetoothLeScanner

        val scanCallback: ScanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val device: BluetoothDevice = result.device
                val scanRecordUuids = result.scanRecord?.serviceUuids
                if (scanRecordUuids?.contains(ParcelUuid.fromString("19b10000-e8f2-537e-4f6c-d104768a1214")) == true
                        || "94:B5:55:C0-6B:7A" == device.address) {
                    bleScanner.stopScan(this)
                }
            }
        }

        val filter = ScanFilter.Builder()
                .setServiceUuid(ParcelUuid.fromString("19b10000-e8f2-537e-4f6c-d104768a1214"))
                .build()
        val scanSettings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()

        bleScanner.startScan(listOf(filter), scanSettings, scanCallback)
    }

    @Deprecated("Suppress")
    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestBluetoothCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBleScan()
            } else {
                // TODO: Handle the case where the user denied the permissions.
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BLEBeaconAppTheme {
        Greeting("Android")
    }
}
