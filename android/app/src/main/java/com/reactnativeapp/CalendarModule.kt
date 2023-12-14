package com.reactnativeapp
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.vnpt.smartca.ConfigSDK
import com.vnpt.smartca.SmartCAEnvironment
import com.vnpt.smartca.SmartCALanguage
import com.vnpt.smartca.SmartCAResultCode
import com.vnpt.smartca.VNPTSmartCASDK
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.vnpt.smartca.EkycService

class CalendarModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "CalendarModule"

    @ReactMethod(isBlockingSynchronousMethod = true) fun createCalendarEvent(name: String, location: String) {
        Log.d("CalendarModule", "Create event called with name: $name and location: $location")
        Toast.makeText(reactApplicationContext, location, Toast.LENGTH_SHORT).show()
//
//        currentActivity?.runOnUiThread {
//            init(currentActivity!!)
////            getAuthentication()
//        }

//        currentActivity?.runOnUiThread {
//            getAuthentication()
//        }
    }

//    @ReactMethod(isBlockingSynchronousMethod = true) fun getAuth(callback: Callback) {
////        Log.d("CalendarModule", "Create event called with name: $name and location: $location")
////        Toast.makeText(reactApplicationContext, location, Toast.LENGTH_SHORT).show()
//
//        currentActivity?.runOnUiThread {
////            init(currentActivity!!.baseContext)
//            getAuthentication(callback)
//
//            sendEvent(this.reactApplicationContext, "")
//        }
////        currentActivity?.runOnUiThread {
////            getAuthentication()
////        }
//    }

//    fun initEngineGroup(context: Context) {
//        // Instantiate a FlutterEngine.
//        val flutterEngineGroup = FlutterEngineGroup(context)
//        // Cache the FlutterEngineGroup to be used by FlutterFragment.
//        FlutterEngineGroupCache
//            .getInstance()
//            .put(ENGINE_GROUP_ID, flutterEngineGroup)
//    }

    private fun sendEvent(reactContext: ReactContext, eventName: String, params: WritableMap?) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }

    @ReactMethod
    fun addListener(type: String?) {
        // Keep: Required for RN built in Event Emitter Calls.
    }

    @ReactMethod
    fun removeListeners(type: Int?) {
        // Keep: Required for RN built in Event Emitter Calls.
    }

    @ReactMethod()
    private fun getAuth(callback: Callback) {
        currentActivity?.runOnUiThread {
            try {
                VNPTSmartCA.getAuthentication { result ->
                    when (result.status) {
                        SmartCAResultCode.SUCCESS_CODE -> {
                            val obj: CallbackResult = Json.decodeFromString(
                                CallbackResult.serializer(), result.data.toString()
                            )
                            // SDK trả lại token, credential của khách hàng
                            // Đối tác tạo transaction cho khách hàng để lấy transId, sau đó gọi getWaitingTransaction
                            val token = obj.accessToken
                            val credentialId = obj.credentialId

                            val params = Arguments.createMap().apply {
                                putInt("code", 0)
                                putString("token", token)
                                putString("credentialId", credentialId)
                            }

                            sendEvent(this.reactApplicationContext, "EventReminder", params)

//                            callback.invoke(0, token, credentialId)

//                            val builder = AlertDialog.Builder(context)
//                            builder.setTitle("Xác thực thành công")
//                            builder.setMessage("CredentialId: $credentialId;\nAccessToken: $token")
//                            builder.setPositiveButton(
//                                "Close"
//                            ) { dialog, _ -> dialog.dismiss() }
//                            builder.show()
                        }

                        else -> {
                            // Xử lý lỗi
//                            val builder = AlertDialog.Builder(context)
//                            builder.setTitle("Thông báo")
//                            builder.setMessage("status: ${result.status}; statusDesc:  ${result.statusDesc}")
//                            builder.setPositiveButton(
//                                "Close"
//                            ) { dialog, _ -> dialog.dismiss() }
//                            builder.show()

                            val params = Arguments.createMap().apply {
                                putInt("code", 1)
                                putString("token",  result.status.toString())
                                putString("credentialId", result.statusDesc)
                            }
//
                            sendEvent(this.reactApplicationContext, "EventReminder", params)
//                            callback.invoke(1, result.status.toString(), result.statusDesc)
                        }
                    }
                }
            } catch (ex: Exception) {
                throw ex;
            }
        }
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    private fun getMainInfo() {
        currentActivity?.runOnUiThread {
            try {
                VNPTSmartCA.getMainInfo { result ->
                    when (result.status) {
                        SmartCAResultCode.SUCCESS_CODE -> {
                            // Xử lý khi confirm thành công
                        }

                        else -> {
                            // Xử lý khi confirm thất bại
                        }
                    }
                }
            } catch (ex: java.lang.Exception) {
                throw ex;
            }
        }
    }

    @ReactMethod()
    private fun getWaitingTransaction(transId: String) {
        currentActivity?.runOnUiThread {
            try {
                if (transId.isNullOrEmpty()) {
//                editTextTrans.setError("Vui lòng điền Id giao dịch");
//                    return
                }

                VNPTSmartCA.getWaitingTransaction(transId) { result ->
//                val builder = AlertDialog.Builder(this)
//                builder.setTitle("Thông báo")
//                builder.setMessage("status: ${result.status}; statusDesc:  ${result.statusDesc}")
//                builder.setPositiveButton(
//                    "Close"
//                ) { dialog, _ -> dialog.dismiss() }
//                builder.show()

                    val params = Arguments.createMap().apply {
                        putInt("code", 0)
                        putString("token", result.status.toString())
                        putString("credentialId", result.statusDesc)
                    }

                    sendEvent(this.reactApplicationContext, "EventReminder", params)

                    when (result.status) {
                        SmartCAResultCode.SUCCESS_CODE -> {
                            // Xử lý khi thành công
                        }

                        else -> {
                            // Xử lý khi thất bại
                        }
                    }

                }
            } catch (ex: Exception) {
                throw ex;
            }
        }
    }


    companion object {

        private var VNPTSmartCA = VNPTSmartCASDK()

        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun init(context: Context) {
            this.context = context
            val config = ConfigSDK()
            config.context = context
            config.partnerId = "VNPTSmartCAPartner-add1fb94-9629-49`47-b7d8-f2671b04c747"
            config.environment = SmartCAEnvironment.DEMO_ENV
            config.lang = SmartCALanguage.VI
            config.isFlutter = false
            VNPTSmartCA.initSDK(config)
            VNPTSmartCA.initEkycService(EkycService(vnptSmartCA = VNPTSmartCA))
            val x = mutableListOf<Int>()
            x.add(Intent.FLAG_ACTIVITY_NEW_TASK)
            VNPTSmartCA.initCustomIntentFlag(x)
//            VNPTSmartCA.ekycService = EkycService(vnptSmartCA = VNPTSmartCA)
        }

        fun onDestroy() {
//        super.onDestroy()
            VNPTSmartCA.destroySDK();
        }
    }

}

@Serializable
data class CallbackResult(
    val credentialId: String,
    val accessToken: String,
) : java.io.Serializable