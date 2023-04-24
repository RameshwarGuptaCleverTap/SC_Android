package com.clevertapTestAll

//import android.support.v4.app.

import android.Manifest
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.PushPermissionResponseListener
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener
import com.clevertap.android.signedcall.enums.VoIPCallStatus
import com.clevertap.android.signedcall.exception.CallException
import com.clevertap.android.signedcall.exception.InitException
import com.clevertap.android.signedcall.init.SignedCallAPI
import com.clevertap.android.signedcall.init.SignedCallInitConfiguration
import com.clevertap.android.signedcall.interfaces.OutgoingCallResponse
import com.clevertap.android.signedcall.interfaces.SignedCallInitResponse
import com.clevertapTestAll.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity(), PushPermissionResponseListener, CTPushNotificationListener {

    var cleverTapDefaultInstance: CleverTapAPI? = null
    private lateinit var requestLauncher: ActivityResultLauncher<String>
    lateinit var binding: ActivityMainBinding


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {}
            }
            else {
                //show error message
                Toast.makeText(applicationContext, "Enable Notification To Receive Notifications", Toast.LENGTH_SHORT).show()
            }
        }

        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this)
        val cleverTapAPI = CleverTapAPI.getDefaultInstance(
            applicationContext
        )

        cleverTapDefaultInstance!!.enableDeviceNetworkInfoReporting(true)
        CleverTapAPI.setDebugLevel(3)
        SignedCallAPI.setDebugLevel(SignedCallAPI.LogLevel.VERBOSE)

        if(cleverTapDefaultInstance != null){
            CleverTapAPI.createNotificationChannelGroup(applicationContext, "12345", "CleverTapPushAndroidxiaomi")
            CleverTapAPI.createNotificationChannel(applicationContext, "CT1104", "CT-PushAndroidNew", "Test-NotificationsAndroid", NotificationManager.IMPORTANCE_MAX, "12345", true)
        }


        if (!cleverTapDefaultInstance?.isPushPermissionGranted!!){

            askForNotificationPermission()
            Toast.makeText(applicationContext, "Enable Notification", Toast.LENGTH_SHORT).show()
        }

        cleverTapDefaultInstance?.ctPushNotificationListener = this;

//        val btn = findViewById<Button>(R.id.button_id)
//        btn.setOnClickListener {
//            val i = Intent(applicationContext, WebActivity::class.java)
//            startActivity(i)
//            finish()
//        }

        initCleverTapCall()

        val btnCall = findViewById<Button>(R.id.button_id)
        btnCall.setOnClickListener { view: View? ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.RECORD_AUDIO),
                    101
                )
                Toast.makeText(this, "Microphone permission is required", Toast.LENGTH_SHORT).show()
            } else startCleverTapSignedCall()
        }


        onUserLoginMethod()
    }


    private fun initCleverTapCall() {
        val signedCallInitListener: SignedCallInitResponse = object : SignedCallInitResponse {
            override fun onSuccess() {
                //App is notified on the main thread when the Signed Call SDK is initialized
                Log.d("cleverTap", "init success")
            }

            override fun onFailure(initException: InitException) {
                //App is notified on the main thread when the initialization is failed
                Log.d(
                    "SignedCall: ", """error code: ${initException.errorCode}
 error message: ${initException.message}
 error explanation: ${initException.explanation}"""
                )
                if (initException.errorCode == InitException.SdkNotInitializedException.errorCode) {
                    //Handle this error here
                    Log.d("cleverTap", "init Failed")
                }
            }
        }
        val initOptions = JSONObject()
        try {
            initOptions.put("accountId", "640f4e74fe2466eed4a7ec45")
            initOptions.put(
                "apiKey",
                "xzyoaK5rgdIzHlfVU3gmolqcD0YLrghXhnOd0hcFOytg7hlBWJmIdtzmLTIs8U8M"
            )
            initOptions.put("cuid", "Ck1117378")
            initOptions.put("appId", "co.clubmahindra.mahindraholidays") //optional
            initOptions.put("name", "Test Name") //optional
            //initOptions.put("ringtone", ""); //optional
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        //Create a Builder instance of SignedCallInitConfiguration and pass it inside the init() method
        val initConfiguration = SignedCallInitConfiguration.Builder(initOptions, false)
            .promptReceiverReadPhoneStatePermission(true) //.promptPushPrimer(jsonObjectConfig)
            .build()
        SignedCallAPI.setDebugLevel(SignedCallAPI.LogLevel.VERBOSE)
        SignedCallAPI.getInstance().init(
            applicationContext,
            initConfiguration,
            cleverTapDefaultInstance,
            signedCallInitListener
        )
    }

    private fun startCleverTapSignedCall() {
        val outgoingCallResponseListener: OutgoingCallResponse = object : OutgoingCallResponse {
            override fun callStatus(callStatus: VoIPCallStatus) {
                //App is notified on the main thread to notify the changes in the call-state
                if (callStatus == VoIPCallStatus.CALL_CANCELLED) {
                    //when the call is cancelled from the initiator's end
                } else if (callStatus == VoIPCallStatus.CALL_DECLINED) {
                    //when the call is declined from the receiver's end
                } else if (callStatus == VoIPCallStatus.CALL_MISSED) {
                    //when the call is missed at the receiver's end
                } else if (callStatus == VoIPCallStatus.CALL_ANSWERED) {
                    //When the call is picked up by the receiver
                } else if (callStatus == VoIPCallStatus.CALL_IN_PROGRESS) {
                    //when the connection to the receiver is established
                } else if (callStatus == VoIPCallStatus.CALL_OVER) {
                    //when the call has been disconnected
                } else if (callStatus == VoIPCallStatus.CALLEE_BUSY_ON_ANOTHER_CALL) {
                    //when the receiver is busy on another call
                }
            }

            override fun onSuccess() {
                //App is notified on the main thread when the call-request is accepted and being processed by the signalling channel
                Log.d("cleverTap", "Call Success")
            }

            override fun onFailure(callException: CallException) {
                //App is notified on the main thread when the call is failed
                Log.d(
                    "SignedCall: ", """error code: ${callException.errorCode}
 error message: ${callException.message}
 error explanation: ${callException.explanation}"""
                )
                if (callException.errorCode == CallException.BadNetworkException.errorCode) {
                    //Handle this error here
                    Log.d("cleverTap", "call Failed")
                }
            }
        }
        val callOptions = JSONObject()
        try {
            callOptions.put(
                "receiver_image",
                "https://cdn4.vectorstock.com/i/1000x1000/98/38/person-gray-photo-placeholder-woman-vector-23519838.jpg"
            )
            callOptions.put(
                "receiver_image",
                "https://cdn2.vectorstock.com/i/1000x1000/46/41/person-gray-photo-placeholder-man-vector-22964641.jpg"
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        SignedCallAPI.getInstance().call(
            applicationContext,
            "Ck111737",
            "Test clevertap call",
            callOptions,
            outgoingCallResponseListener
        )
    }

    fun onInAppButtonClick(hashMap: HashMap<String, String>?) {
        // Read the values
        val i = Intent(applicationContext, WebActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        /**
         * On Android 12, Raise notification clicked event when Activity is already running in activity backstack
         */
        CleverTapAPI.getDefaultInstance(applicationContext)!!
            .pushNotificationClickedEvent(intent!!.extras)
        val extras = intent.extras
        val newURL = extras?.get("wzrk_dl").toString()
        val intents = Intent(this, WebActivity::class.java)
        intents.putExtra("webURL", newURL)
        startActivity(intents)
        finish()
    }



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun askForNotificationPermission() {
        requestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }


    override fun onPushPermissionResponse(accepted: Boolean) {
        if (accepted) {
            CleverTapAPI.createNotificationChannel(getApplicationContext(), "xiaominew", "CT-PushAndroidNew",
                "Test-NotificationsAndroid", NotificationManager.IMPORTANCE_HIGH, true);
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cleverTapDefaultInstance?.unregisterPushPermissionNotificationResponseListener(this)
    }

    private fun onUserLoginMethod() {

        val profileUpdate = HashMap<String, Any>()
        profileUpdate["Name"] = "Rameshwar Gupta" // String
        profileUpdate["Identity"] = 1122252021 // String or number
        profileUpdate["Email"] = "priyanka.papola16feb@gmail.com" // Email address of the user
        profileUpdate["Phone"] = "+919599721544" // Phone (with the country code, starting with +)
        profileUpdate["Gender"] = "M" // Can be either M or F
        profileUpdate["DOB"] = Date() // Date of Birth. Set the Date object to the appropriate value first
        profileUpdate["Photo"] = "www.foobar.com/image.jpeg" // URL to the Image

        profileUpdate["MSG-email"] = false // Disable email notifications
        profileUpdate["MSG-push"] = true // Enable push notifications
        profileUpdate["MSG-sms"] = false // Disable SMS notifications
        profileUpdate["MSG-dndPhone"] = true // Opt out phone
        profileUpdate["MSG-dndEmail"] = true // Opt out email
        profileUpdate["MyStuff"] = arrayListOf("bag", "shoes") //ArrayList of Strings
        profileUpdate["MyStuff"] = arrayOf("Jeans", "Perfume") //String Array
        cleverTapDefaultInstance?.onUserLogin(profileUpdate)
    }


    fun sendEventsOnProfile() {

        val prodViewedAction = mapOf(
            "Product Name" to "Casio Chronograph Watch",
            "Category" to "Mens Accessories",
            "Price" to 59.99,
            "Date" to Date()
        )
        cleverTapDefaultInstance?.pushEvent("Product viewed", prodViewedAction)
    }

    override fun onNotificationClickedPayloadReceived(payload: HashMap<String, Any>?) {
        Log.d("TAG", "onNotificationClickedPayloadReceived: ")
    }

}
