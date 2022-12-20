import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const platform = MethodChannel('com.example.myapp/bluetooth');
  bool bluetoothEnabled = false;
  bool permissionRequested = false;
  String permissionRequestedKey = 'bluetooth_permission_requested_';

  @override
  void initState() {
    // Check if the permission has been requested before
    checkPermission();

    super.initState();
  }

  checkPermission() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    permissionRequested = prefs.getBool(permissionRequestedKey) ?? false;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Bluetooth Demo'),
        ),
        body: Center(
          child: ElevatedButton(
            child: const Text('Enable Bluetooth'),
            onPressed: () async {
              if (!permissionRequested) {
                // Request Bluetooth permissions from the user
                bool granted = await platform.invokeMethod('requestPermission');
                print("permission granted: $granted");

                // Save the flag indicating that the permission has been requested
                SharedPreferences prefs = await SharedPreferences.getInstance();
                prefs.setBool(permissionRequestedKey, true);
              }

              // Enable Bluetooth
              bool enabled = await platform.invokeMethod('enableBluetooth');
              print("bluetooth enabled: $enabled");
            },
          ),
        ),
      ),
    );
  }
}
