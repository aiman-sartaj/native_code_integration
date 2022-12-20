//package com.example.native_code_integration_demo
//
//
//import android.content.pm.PackageManager
//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.AlarmManager
//import android.app.PendingIntent
//import android.bluetooth.BluetoothAdapter
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import android.os.Bundle
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import io.flutter.embedding.android.FlutterActivity
//import io.flutter.plugin.common.MethodChannel
//import java.util.*
//
//class MainActivity: FlutterActivity() {
//    private val bluetoothPermissionRequestCode = 1
//    private val REQUEST_ENABLE_BT = 2
//    private val REQUEST_BLUETOOTH_ADMIN = 1
//    private val REQUEST_BLUETOOTH_CONNECT = 3
//
//    private val CHANNEL = "com.example.myapp/bluetooth"
//
//    @RequiresApi(Build.VERSION_CODES.KITKAT)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, "com.example.myapp/bluetooth").setMethodCallHandler { call, result ->
//            when (call.method) {
//                "hasPermission" -> {
//                    val hasPermission = checkBluetoothPermission()
//                    result.success(hasPermission)
//                }
//                "requestPermission" -> {
//                    if (checkBluetoothPermission()) {
//                        result.success(true)
//                    } else {
//                        requestBluetoothPermission()
//                        result.success(null)
//                    }
//                }
//                "enableBluetooth" -> {
//                    enableBluetooth()
//                    result.success(null)
//                }
//
//                "disableBluetooth" -> {
//                    disableBluetooth()
//                    result.success(null)
//                }
//                else -> result.notImplemented()
//            }
//        }
//
//
//
//    }
//
//    private fun enableBluetoothAtSpecificTime() {
//
//        val triggerTime = Calendar.getInstance().apply {
//            set(Calendar.HOUR_OF_DAY, 10)
//            set(Calendar.MINUTE, 5)
//            set(Calendar.SECOND, 0)
//        }.timeInMillis
//
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE  )
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
//
//        Toast.makeText(this, "Bluetooth will be enabled at 6 PM", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun enableBluetooth() {
//        val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//                ?: // Device does not support Bluetooth
//                return
//
//        if (!bluetoothAdapter.isEnabled) {
//            // If Bluetooth is not enabled, prompt the user to enable it
//            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                // Check if the app has the BLUETOOTH_ADMIN permission
//                if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
//                    // If the app doesn't have the BLUETOOTH_ADMIN permission, request it
//                    requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_ADMIN), REQUEST_BLUETOOTH_ADMIN)
//                    return
//                }
//
//                if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                    // If the app doesn't have the BLUETOOTH_CONNECT permission, request it
//                    requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_BLUETOOTH_CONNECT)
//                    return
//                }
//            }
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
//        }
//
//
//    }
//
//    private fun disableBluetooth() {
//        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
//        if (bluetoothAdapter == null) {
//            // Device does not support Bluetooth
//            return
//        }
//
//
//        // Disable Bluetooth
//        if (bluetoothAdapter?.isEnabled == true) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return
//            }
//            bluetoothAdapter.disable()
//
//        } else {
//            Toast.makeText(this, "Bluetooth is already disabled", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun checkBluetoothPermission(): Boolean {
//        val permission = Manifest.permission.BLUETOOTH
//        val permissionStatus = ContextCompat.checkSelfPermission(this, permission)
//        return permissionStatus == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestBluetoothPermission() {
//        val permission = Manifest.permission.BLUETOOTH
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
//            // Show an explanation to the user
//            // ...
//        }
//        ActivityCompat.requestPermissions(this, arrayOf(permission), bluetoothPermissionRequestCode)
//    }
//
//
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        when (requestCode) {
//            bluetoothPermissionRequestCode -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Bluetooth permission has been granted
//                    // Proceed with your code
//                    // ...
//                } else {
//                    // Bluetooth permission has not been granted
//                    // Show an error message or take some other action
//                    // ...
//                }
//            }
//        }
//    }
//}
//
//
//
//
////
////import io.flutter.embedding.android.FlutterActivity
////import android.bluetooth.BluetoothAdapter
////import android.content.Intent
////import android.os.Build
////import android.Manifest
////import android.app.Activity
////import android.content.pm.PackageManager
////import android.os.Bundle
////import android.os.Handler
////import android.widget.Toast
////import io.flutter.plugin.common.MethodChannel
////
////class MainActivity: FlutterActivity() {
////
////  private val CHANNEL = "com.example.myapp/bluetooth"
////  private val REQUEST_BLUETOOTH_ADMIN = 1
////  private val REQUEST_ENABLE_BT = 2
////  private val REQUEST_BLUETOOTH_CONNECT = 3
////
////
////  override fun onCreate(savedInstanceState: Bundle?) {
////    super.onCreate(savedInstanceState)
////    MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
////      if (call.method == "enableBluetooth") {
////        enableBluetooth()
////        //enableBluetoothDelayed()
////
////        result.success(null)
////      } else {
////        result.notImplemented()
////      }
////    }
////
////
////  }
////
////  private fun enableBluetoothDelayed() {
////    val handler = Handler()
////    val runnable = Runnable {
////enableBluetooth()
////    }
////
////// Enable Bluetooth after 5 seconds
////    handler.postDelayed(runnable, 10000)
////  }
////
////  private fun enableBluetooth() {
////    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
////    if (bluetoothAdapter == null) {
////      // Device does not support Bluetooth
////      return
////    }
////
////
////    // Check if Bluetooth is already enabled
////    if (!bluetoothAdapter.isEnabled) {
////      // If Bluetooth is not enabled, prompt the user to enable it
////      val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
////      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////        // Check if the app has the BLUETOOTH_ADMIN permission
////        if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
////          // If the app doesn't have the BLUETOOTH_ADMIN permission, request it
////          requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_ADMIN), REQUEST_BLUETOOTH_ADMIN)
////          return
////        }
////
////        if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
////          // If the app doesn't have the BLUETOOTH_CONNECT permission, request it
////          requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_BLUETOOTH_CONNECT)
////          return
////        }
////      }
////      startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
////    }
////  }
////
////  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
////    if (grantResults.isEmpty()) {
////      // The permissions request was cancelled, do nothing
////      return
////    }
////    when (requestCode) {
////      REQUEST_BLUETOOTH_ADMIN -> {
////        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////          // Permission granted, proceed with the action
////          enableBluetooth()
////        } else {
////          // Permission denied, show a message to the user
////          Toast.makeText(this, "Bluetooth admin permission denied", Toast.LENGTH_SHORT).show()
////        }
////      }
////    }
////  }
////
////  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
////    super.onActivityResult(requestCode, resultCode, data)
////    if (requestCode == REQUEST_ENABLE_BT) {
////      if (resultCode == Activity.RESULT_OK) {
////        // Bluetooth was enabled, do something here if needed
////      } else if (resultCode == Activity.RESULT_CANCELED) {
////        // Bluetooth was not enabled, show a message to the user
////        Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show()
////      }
////    }
////
////
////
////
////  }
////
////}
////
////
////
