import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:permission_handler/permission_handler.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();

  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const platform = MethodChannel('com.example.myapp/bluetooth');
  bool bluetoothEnabled = false;
  bool? permissionGranted;
  bool status = false;
  String permissionRequestedKey = 'bluetooth_permission_requested';

  final FlutterLocalNotificationsPlugin flutterLocalNotificationsPlugin =
      FlutterLocalNotificationsPlugin();
  final AndroidInitializationSettings androidInitializationSettings =
      const AndroidInitializationSettings('app_icon');

  @override
  void initState() {
    super.initState();

    InitializationSettings initializationSettings =
        InitializationSettings(android: androidInitializationSettings);
    flutterLocalNotificationsPlugin.initialize(initializationSettings);
    check();
    scheduleBluetoothEnable();
  }

  Future<void> _scheduleNotification() async {
    // try {
    //   await enableBluetoothAutomatically();

    //   print("enableBluetoothAutomatically method invoked");
    // } on PlatformException catch (e) {
    //   print(e.message);
    // } catch (e) {
    //   print("$e error at enableBluetoothAutomatically");
    // }

    var time = const Time(14, 17, 0);
    var androidPlatformChannelSpecifics = const AndroidNotificationDetails(
      '1',
      'bluetooth_schedular',
      priority: Priority.high,
      ticker: 'ticker',
    );
    var platformChannelSpecifics = NotificationDetails(
      android: androidPlatformChannelSpecifics,
    );

    await flutterLocalNotificationsPlugin.show(
      0,
      'Notification',
      'Bluetooth has been enabled',
      platformChannelSpecifics,
      payload: 'item x',
    );
  }

  TimeOfDay targetTime = const TimeOfDay(hour: 14, minute: 32);
  TimeOfDay offTime = const TimeOfDay(hour: 14, minute: 33);

  void scheduleBluetoothEnable() async {
    DateTime now = DateTime.now();
    DateTime scheduledTime = DateTime(
        now.year, now.month, now.day, targetTime.hour, targetTime.minute);
    if (scheduledTime.isBefore(now)) {
      scheduledTime = scheduledTime.add(const Duration(days: 1));
    }

    DateTime offTimeSchedule =
        DateTime(now.year, now.month, now.day, offTime.hour, offTime.minute);
    if (offTimeSchedule.isBefore(now)) {
      offTimeSchedule = offTimeSchedule.add(const Duration(days: 1));
    }

    Timer(scheduledTime.difference(now), enableBluetoothAutomatically);
    Timer(offTimeSchedule.difference(now), disableBluetooth);
    await Future.delayed(
        Duration(seconds: scheduledTime.difference(now).inSeconds));
    _scheduleNotification();
  }

  check() async {
    // permissionGranted = await checkPermission();
    status = await Permission.bluetoothConnect.status.isGranted;

    setState(() {
      permissionGranted = status;
    });
    print("status: $status");
  }

  checkPermission() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    permissionGranted = prefs.getBool("isGranted")!;
    return permissionGranted;
  }

  grantPermission() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setBool("isGranted", true);
  }

  enableBluetoothAutomatically() async {
    await platform.invokeMethod('enableBluetoothAutomatically');
  }

  enableBluetooth() async {
    await platform.invokeMethod('enableBluetooth');
    grantPermission();
  }

  disableBluetooth() async {
    try {
      await platform.invokeMethod('disableBluetooth');
      print("method invoked");
    } on PlatformException catch (e) {
      print(e.message);
    } catch (e) {
      print(" granted");
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Bluetooth Demo'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ElevatedButton(
                child: const Text('Enable Bluetooth'),
                onPressed: () async {
                  //  enableBluetooth();

                  check();
                  if (permissionGranted == true) {
                    // // Request Bluetooth permissions from the user
                    // bool granted =
                    //     await platform.invokeMethod('requestPermission');
                    // print("permission granted: $granted");

                    //    if (granted) {
                    _scheduleNotification();
                  } else {
                    try {
                      enableBluetooth();
                      print(" enableBluetooth method invoked");
                    } on PlatformException catch (e) {
                      print(e.message);
                    } catch (e) {
                      print(" $e error at enableBluetooth");
                    }
                  }
                  //  }
                },
              ),
              ElevatedButton(
                  child: const Text('Disable Bluetooth'),
                  style: ButtonStyle(
                      backgroundColor: MaterialStateProperty.all(Colors.red)),
                  onPressed: () async {
                    try {
                      await platform.invokeMethod('disableBluetooth');
                      print("method invoked");
                    } on PlatformException catch (e) {
                      print(e.message);
                    } catch (e) {
                      print(" granted");
                    }
                  }),
              // ElevatedButton(
              //     child: const Text('Enable Location'),
              //     style: ButtonStyle(
              //         backgroundColor: MaterialStateProperty.all(Colors.green)),
              //     onPressed: () async {
              //       try {
              //         checkLocationPermission();
              //         print("location method invoked");
              //       } on PlatformException catch (e) {
              //         print(e.message);
              //       } catch (e) {
              //         print(" $e error");
              //       }
              //     }),
              // ElevatedButton(
              // child: const Text('Disable Location'),
              // style: ButtonStyle(
              //     backgroundColor: MaterialStateProperty.all(Colors.red)),
              // onPressed: () async {
              //   try {
              //     await platform.invokeMethod("disableLocation");
              //     print("location method invoked");
              //   } on PlatformException catch (e) {
              //     print(e.message);
              //   } catch (e) {
              //     print(" $e error");
              //   }
              // }),
            ],
          ),
        ),
      ),
    );
  }
}
