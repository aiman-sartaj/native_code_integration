//import android.Manifest
//import android.bluetooth.BluetoothAdapter
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import io.flutter.embedding.android.FlutterActivity
//import io.flutter.plugin.common.MethodChannel
//
//class MainActivity : FlutterActivity() {
//
//    private val REQUEST_BLUETOOTH_PERMISSION = 1
//    private val REQUEST_ENABLE_BT = 2
//    private val REQUEST_BLUETOOTH_ADMIN_PERMISSION = 3
//    private val PREFS_NAME = "MyPrefsFile"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, "com.example.myapp/bluetooth").setMethodCallHandler { call, result ->
//            when (call.method) {
//
//                "enableBluetooth" -> {
//                    // Check if Bluetooth permission has been granted
//                    val bluetoothPermissionGranted = getBluetoothPermissionFromPreferences()
//                    if (!bluetoothPermissionGranted) {
//                        // Request Bluetooth permission
//                        requestBluetoothPermission()
//                    } else {
//                        // Bluetooth permission has already been granted, proceed with using Bluetooth
//                        useBluetooth()
//                    }
//                    result.success(null)
//                }
//
////        "disableBluetooth" -> {
////          disableBluetooth()
////          result.success(null)
////        }
//                else -> result.notImplemented()
//            }
//        }
//
//
//    }
//
//    private fun getBluetoothPermissionFromPreferences(): Boolean {
//        val preferences: SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        return preferences.getBoolean("bluetooth_permission_granted", false)
//    }
//
//    private fun requestBluetoothPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            // Permission is not granted, request it
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.BLUETOOTH),
//                REQUEST_BLUETOOTH_PERMISSION
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            REQUEST_BLUETOOTH_PERMISSION -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission has been granted, save it to preferences and proceed with using Bluetooth
//                    saveBluetoothPermissionToPreferences(true)
//                    useBluetooth()
//                } else {
//                    // Permission has been denied, show a message to the user
//                    Toast.makeText(this, "Bluetooth permission is required", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    private fun saveBluetoothPermissionToPreferences(granted: Boolean) {
//        val preferences: SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        val editor: SharedPreferences.Editor = preferences.edit()
//        editor.putBoolean("bluetooth_permission_granted", granted)
//        editor.apply()
//    }
//
//    private fun useBluetooth() {
//        // Enable Bluetooth if it is not already enabled
//        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
//        if (bluetoothAdapter == null) {
//            // Device does not support Bluetooth
//            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show()
//        } else if (!bluetoothAdapter.isEnabled) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)
//                == PackageManager.PERMISSION_GRANTED
//            ) {
//                // Permission is granted, proceed with enabling Bluetooth
//                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
//            } else {
//                // Check if the user has denied the permission
//                if (ActivityCompat.shouldShowRequestPermissionRationale(
//                        this,
//                        Manifest.permission.BLUETOOTH_ADMIN
//                    )
//                ) {
//                    // Permission has been denied, show a message explaining why the permission is needed
//                    Toast.makeText(
//                        this,
//                        "Bluetooth admin permission is needed to enable Bluetooth",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                // Request the permission
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.BLUETOOTH_ADMIN),
//                    REQUEST_BLUETOOTH_ADMIN_PERMISSION
//                )
//            }
//        } else {
//        }
//    }
//}