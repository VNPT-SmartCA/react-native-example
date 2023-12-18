//
//  Counter.swift
//  ReactNativeApp
//
//  Created by TuanNT on 14/12/2023.
//

import Foundation
import SmartCASDK

@objc(Counter)
class Counter: RCTEventEmitter {
  
  private var manager: TestManager?
  private var hasListeners = false
  
  override init() {
      super.init()
      self.manager = TestManager.shared
  }
  
  private var count = 0
  
//  private var hasListeners = false
//
//  var vnptSmartCASDK: VNPTSmartCASDK?
  
  
  override func startObserving() {
    hasListeners = true
  }
  
  override func stopObserving() {
    hasListeners = false
  }
  
  @objc
    func increment() {
      count += 1
      print("count is \(count)")
      sendEvent(withName: "onIncrement", body: ["count": count])
    }
  
//    @objc
//  func initSDK(vc: UIViewController) {
//    if let ab = RCTPresentedViewController() {
//      self.vnptSmartCASDK = VNPTSmartCASDK(
//        viewController: ab,
//        partnerId: "CLIENT_ID",
//        environment: VNPTSmartCASDK.ENVIRONMENT.DEMO,
//        lang: VNPTSmartCASDK.LANG.VI,
//        isFlutterApp: false)
//    }
//
//  }
  
  @objc func getAuth() {
      // SDK tự động xử lý các trường hợp về token: Hết hạn, chưa kích hoạt...
    self.manager?.vnptSmartCASDK?.getAuthentication(callback: { result in
              if result.status == SmartCAResultCode.SUCCESS_CODE {
                  // Xử lý khi thành công
//                Counter.sharedInstance().sendEvent(withName: "EventReminder", body: ["code": 0, "token": result.data, "credentialId": ""])

                if self.hasListeners {
                  self.sendEvent(withName: "EventReminder", body: ["code": 0, "token": result.data, "credentialId": ""])
                    }
                
                
              } else {
                  // Xử lý khi thất bại
              }
          });
  }
  
  @objc func  getMainInfo(){
    self.manager?.vnptSmartCASDK?.getMainInfo(callback: { result in
          
      })
  }
  
  @objc func getWaitingTransaction(tranId: String) {
//      self.tranId = "xxxx"; // tạo giao dịch từ backend, lấy tranId từ hệ thống VNPT SmartCA trả về

    self.manager?.vnptSmartCASDK?.getWaitingTransaction(tranId: tranId, callback: { result in
          if result.status == SmartCAResultCode.SUCCESS_CODE {
              print("Giao dịch thành công: \(result.status) - \(result.statusDesc) - \(result.data)");
            
            if self.hasListeners {
              self.sendEvent(withName: "EventReminder", body: ["code": 0, "token": result.status, "credentialId": result.statusDesc])
                }
            
            
          } else {
            if self.hasListeners {
              self.sendEvent(withName: "EventReminder", body: ["code": 1, "token": result.status, "credentialId": result.statusDesc])
                }
          }
      });
  }

    // we need to override this method and
    // return an array of event names that we can listen to
    override func supportedEvents() -> [String]! {
      return ["onIncrement", "EventReminder"]
    }
  

  @objc
  override func constantsToExport() -> [AnyHashable : Any]! {
      return ["initialCount": 0]
  }
  
  override static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  
}

class TestManager: NSObject {
  static let shared = TestManager()
  
  @objc class func getInstance() -> TestManager {
    return TestManager.shared
  }
  
  var testEventCallback: ((String) -> Void)?
  
  func sendTest() {
    testEventCallback?("This is a test")
  }
  
  var vnptSmartCASDK: VNPTSmartCASDK?
  
  @objc
  func initSDK() {
    if let ab = RCTPresentedViewController() {
      self.vnptSmartCASDK = VNPTSmartCASDK(
        viewController: ab,
        partnerId: "CLIENT_ID",
        environment: VNPTSmartCASDK.ENVIRONMENT.DEMO,
        lang: VNPTSmartCASDK.LANG.VI,
        isFlutterApp: false)
    }
  }
  
  @objc
  func destroySDK() {
    self.vnptSmartCASDK?.destroySDK();
  }
}

