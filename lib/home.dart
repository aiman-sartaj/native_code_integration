import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:permission_handler/permission_handler.dart';

class Home extends StatefulWidget {
  const Home({super.key});

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  static const platform = MethodChannel('com.example.myapp/bluetooth');
  bool bluetoothEnabled = false;
  bool? permissionGranted;
  bool status = false;
  String permissionRequestedKey = 'bluetooth_permission_requested';
  TimeOfDay onTime = const TimeOfDay(hour: 12, minute: 00);
  TimeOfDay offTime = const TimeOfDay(hour: 12, minute: 00);

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
  }

  Future<void> _scheduleNotification(bool bluetoothOn) async {
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
      bluetoothOn
          ? 'Bluetooth has been enabled'
          : 'Bluetooth has been disabled',
      platformChannelSpecifics,
      payload: 'item x',
    );
  }

  void scheduleBluetoothEnable(TimeOfDay onTime, TimeOfDay offTime) async {
    DateTime now = DateTime.now();
    DateTime scheduledTime =
        DateTime(now.year, now.month, now.day, onTime.hour, onTime.minute);
    if (scheduledTime.isBefore(now)) {
      scheduledTime = scheduledTime.add(const Duration(days: 1));
    }

    DateTime offTimeSchedule =
        DateTime(now.year, now.month, now.day, offTime.hour, offTime.minute);
    if (offTimeSchedule.isBefore(now)) {
      offTimeSchedule = offTimeSchedule.add(const Duration(days: 1));
    }

    Timer(scheduledTime.difference(now), enableBluetooth);
    Timer(offTimeSchedule.difference(now), disableBluetooth);
    await Future.delayed(
        Duration(seconds: scheduledTime.difference(now).inSeconds));
    _scheduleNotification(true);

    await Future.delayed(
        Duration(seconds: offTimeSchedule.difference(now).inSeconds));
    _scheduleNotification(false);
  }

  check() async {
    // permissionGranted = await checkPermission();
    status = await Permission.bluetoothConnect.status.isGranted;

    setState(() {
      permissionGranted = status;
    });
    print("status: $status");

    if (status != true) {
      enableBluetooth();
    }
  }

  enableBluetooth() async {
    if (status) {
      await platform.invokeMethod('enableBluetoothAutomatically');
    } else {
      await platform.invokeMethod('enableBluetooth');
    }
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

  Future displayTimePicker(BuildContext context) async {
    TimeOfDay timeOfDay = TimeOfDay.now();

    var startTime =
        await showTimePicker(context: context, initialTime: timeOfDay);
    var endTime =
        await showTimePicker(context: context, initialTime: timeOfDay);

    if (startTime != null) {
      setState(() {
        onTime = startTime;
      });
    }
    if (endTime != null) {
      setState(() {
        offTime = endTime;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(18.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Text(
                "Enable/Disable Bluetooth",
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              SizedBox(
                height: 15,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  ElevatedButton(
                      style: ButtonStyle(
                          backgroundColor:
                              MaterialStateProperty.all(Colors.green)),
                      onPressed: () {
                        displayTimePicker(context);
                      },
                      child: const Text("Start Time")),
                  Card(
                    child: Padding(
                      padding: const EdgeInsets.all(10.0),
                      child: Text(
                        onTime.format(context),
                        style: TextStyle(fontSize: 14),
                      ),
                    ),
                  ),
                ],
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  ElevatedButton(
                      style: ButtonStyle(
                          backgroundColor:
                              MaterialStateProperty.all(Colors.green)),
                      onPressed: () {
                        displayTimePicker(context);
                      },
                      child: const Text("End Time")),
                  Card(
                    child: Padding(
                      padding: const EdgeInsets.all(10.0),
                      child: Text(
                        offTime.format(context),
                        style: TextStyle(fontSize: 14),
                      ),
                    ),
                  ),
                ],
              ),
              ElevatedButton(
                  onPressed: () async {
                    scheduleBluetoothEnable(onTime, offTime);
                    await platform.invokeMethod("showToast");
                  },
                  child: Text("Save"))
              // ElevatedButton(
              //   child: const Text('Enable Bluetooth'),
              //   onPressed: () async {
              //     //  enableBluetooth();

              //     check();
              //     if (permissionGranted == true) {
              //       // // Request Bluetooth permissions from the user
              //       // bool granted =
              //       //     await platform.invokeMethod('requestPermission');
              //       // print("permission granted: $granted");

              //       //    if (granted) {
              //       _scheduleNotification();
              //     } else {
              //       try {
              //         enableBluetooth();
              //         print(" enableBluetooth method invoked");
              //       } on PlatformException catch (e) {
              //         print(e.message);
              //       } catch (e) {
              //         print(" $e error at enableBluetooth");
              //       }
              //     }
              //     //  }
              //   },
              // ),
              // ElevatedButton(
              //     child: const Text('Disable Bluetooth'),
              //     style: ButtonStyle(
              //         backgroundColor: MaterialStateProperty.all(Colors.red)),
              //     onPressed: () async {
              //       try {
              //         await platform.invokeMethod('disableBluetooth');
              //         print("method invoked");
              //       } on PlatformException catch (e) {
              //         print(e.message);
              //       } catch (e) {
              //         print(" granted");
              //       }
              //     }),
              // // ElevatedButton(
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
