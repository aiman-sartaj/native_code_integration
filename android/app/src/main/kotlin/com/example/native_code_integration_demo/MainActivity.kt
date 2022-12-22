package com.example.native_code_integration_demo


import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import java.util.*

class MainActivity: FlutterActivity() {
    private val bluetoothPermissionRequestCode = 1
    private val REQUEST_ENABLE_BT = 2
    private val REQUEST_BLUETOOTH_ADMIN = 1
    private val REQUEST_BLUETOOTH_CONNECT = 3

    private val CHANNEL = "com.example.myapp/bluetooth"

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, "com.example.myapp/bluetooth").setMethodCallHandler { call, result ->
            when (call.method) {
                "enableBluetooth" -> {
                    enableBluetooth()
                    result.success(null)
                }
                "enableBluetoothAutomatically" -> {
                    enableBluetoothAutomatically()
                    result.success(null)
                }

                "disableBluetooth" -> {
                    disableBluetooth()
                    result.success(null)
                }

                "showToast" -> {
                    showToast()
                    result.success(null)
                }
                else -> result.notImplemented()
            }
        }



    }

    private fun enableBluetooth() {

        val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                ?: // Device does not support Bluetooth
                return

        if (!bluetoothAdapter.isEnabled) {
            // If Bluetooth is not enabled, prompt the user to enable it
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Check if the app has the BLUETOOTH_ADMIN permission
                if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                    // If the app doesn't have the BLUETOOTH_ADMIN permission, request it
                    requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_ADMIN), REQUEST_BLUETOOTH_ADMIN)
                    return
                }

                if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // If the app doesn't have the BLUETOOTH_CONNECT permission, request it
                    requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_BLUETOOTH_CONNECT)
                    return
                }
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }


    }

    private fun enableBluetoothAutomatically() {
        val mBtAdapter = BluetoothAdapter.getDefaultAdapter()

        if (! mBtAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(
                    this ,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mBtAdapter.enable();
        }
    }

    private fun disableBluetooth() {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            // Device does not support Bluetooth
            return
        }


        // Disable Bluetooth
        if (bluetoothAdapter?.isEnabled == true) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            bluetoothAdapter.disable()

        } else {
            Toast.makeText(this, "Bluetooth is already disabled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showToast() {
        Toast.makeText(this, "Bluetooth enable time has been set", Toast.LENGTH_SHORT).show()
    }

}


