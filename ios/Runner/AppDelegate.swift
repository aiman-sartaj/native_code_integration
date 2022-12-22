import UIKit
import CoreBluetooth
import Flutter


class AppDelegate: FlutterAppDelegate {
  let bluetoothPermissionRequestCode = 1
  let requestEnableBT = 2
  let requestBluetoothAdmin = 1
  let requestBluetoothConnect = 3
  let channel = "com.example.myapp/bluetooth"
  
  var centralManager: CBCentralManager!
  
  override func viewDidLoad() {
    super.viewDidLoad()
    
    centralManager = CBCentralManager(delegate: self, queue: nil)
  }
  
  func enableBluetooth() {
    guard let bluetoothAdapter = centralManager.state else {
      // Device does not support Bluetooth
      return
    }
    
    if bluetoothAdapter != .poweredOn {
      // If Bluetooth is not enabled, prompt the user to enable it
      let enableBTIntent = CBManager.authorization
      
      if #available(iOS 13.0, *) {
        // Check if the app has the BLUETOOTH_ADMIN permission
        if self.checkSelfPermission(for: .bluetoothAdmin) != .authorized {
          // If the app doesn't have the BLUETOOTH_ADMIN permission, request it
          self.requestPermission(for: .bluetoothAdmin)
          return
        }
        
        if self.checkSelfPermission(for: .bluetoothConnect) != .authorized {
          // If the app doesn't have the BLUETOOTH_CONNECT permission, request it
          self.requestPermission(for: .bluetoothConnect)
          return
        }
      }
      
      self.centralManager.scanForPeripherals(withServices: nil, options: nil)
    }
  }
  
  func enableBluetoothAutomatically() {
    guard let bluetoothAdapter = centralManager.state, bluetoothAdapter != .poweredOn else { return }
    
    if self.checkSelfPermission(for: .bluetoothConnect) != .authorized {
      // If the app doesn't have the BLUETOOTH_CONNECT permission, request it
      self.requestPermission(for: .bluetoothConnect)
      return
    }
    
    self.centralManager.scanForPeripherals(withServices: nil, options: nil)
  }
  
  func disableBluetooth() {
    guard let bluetoothAdapter = centralManager.state, bluetoothAdapter == .poweredOn else { return }
    
    self.centralManager.stopScan()
  }
}

extension AppDelegate: CBCentralManagerDelegate {
   func centralManagerDidUpdateState(_ central: CBCentralManager) {
    switch central.state {
    case .unknown:
      print("Bluetooth state is unknown")
    case .resetting:
      print("Bluetooth state is resetting")
    case .unsupported:
      print("Bluetooth state is unsupported")
    case .unauthorized:
      print("Bluetooth state is unauthorized")
    case .poweredOff:
      print("Bluetooth state is powered off")
    case .poweredOn:
      print("Bluetooth state is powered on")
    @unknown default:
      print("Bluetooth state is unknown")
    }
  }
}

extension AppDelegate: CBPeripheralDelegate {
  func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber) {
    print("Discovered \(peripheral.name ?? "unknown")")
  }
}


// import UIKit
// import Flutter

// @UIApplicationMain
// @objc class AppDelegate: FlutterAppDelegate {
//   override func application(
//     _ application: UIApplication,
//     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
//   ) -> Bool {
//     GeneratedPluginRegistrant.register(with: self)
//     return super.application(application, didFinishLaunchingWithOptions: launchOptions)
//   }
// }
