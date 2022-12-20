//package com.example.native_code_integration_demo
//
//import android.Manifest
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.content.pm.PackageManager
//import android.location.LocationManager
//import android.os.Bundle
//import android.provider.Settings
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import io.flutter.embedding.android.FlutterActivity
//import io.flutter.plugin.common.MethodChannel
//
//class MainActivity : FlutterActivity() {
//
//    private val REQUEST_LOCATION_PERMISSION = 1
//    private val PREFS_NAME = "MyPrefsFile"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        MethodChannel(
//            flutterEngine!!.dartExecutor.binaryMessenger ,
//            "com.example.myapp/bluetooth"
//        ).setMethodCallHandler { call , result ->
//            when (call.method) {
//
//                "enableLocation" -> {
//                    enableLocation()
//                    result.success(null)
//                }
//                "disableLocation" -> {
//                    disableLocation()
//                    result.success(null)
//                }
//            }
//
//        }
//
//        // Check if location permission has been granted
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            // Check if the user has denied the permission
//            if (ActivityCompat.shouldShowRequestPermissionRationale(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )
//            ) {
//                // Permission has been denied, show a message explaining why the permission is needed
//                Toast.makeText(this, "Location permission is needed to use this app", Toast.LENGTH_SHORT)
//                    .show()
//            }
//            // Request the permission
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                REQUEST_LOCATION_PERMISSION
//            )
//        } else {
//            // Permission is already granted, proceed with enabling location
//            enableLocation()
//        }
//    }
//
//
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            REQUEST_LOCATION_PERMISSION -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//// Permission granted, proceed with enabling location
//                    enableLocation()
//                } else {
//// Permission denied, show a message explaining why the permission is needed
//                    Toast.makeText(this, "Location permission is needed to use this app", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        }
//    }
//
//    private fun enableLocation() {
//        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//            startActivity(intent)
//        }
//    }
//
//    private fun disableLocation() {
//        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        locationManager.allProviders.forEach {
//            locationManager.setTestProviderEnabled(it, false)
//        }
//    }
//
//    fun checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//// Permission has not been granted, show a message explaining why the permission is needed
//            Toast.makeText(this, "Location permission is needed to use this app", Toast.LENGTH_SHORT).show()
//        } else {
//// Permission has been granted, proceed with enabling location
//            enableLocation()
//        }
//    }
////}