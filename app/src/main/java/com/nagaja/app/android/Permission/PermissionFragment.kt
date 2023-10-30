package com.nagaja.app.android.Permission

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.GpsTracker
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.Utils.NetworkProvider.Companion.API_DOMAIN

import kotlinx.android.synthetic.main.fragment_permission.view.*
import kotlinx.android.synthetic.main.fragment_select_language.view.*
import java.util.*

class PermissionFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mRequestQueue: RequestQueue

    private lateinit var mListener: OnPermissionFragmentListener

    interface OnPermissionFragmentListener {
        fun onFinish()
        fun onLoginScreen()
        fun onMainScreen()
    }

    companion object {
        const val PERMISSION_REQUEST_CODE     = 0x01

        fun newInstance(): PermissionFragment {
            val args = Bundle()
            val fragment = PermissionFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mContainer = inflater.inflate(R.layout.fragment_permission, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Push Permission View
        val pushPermissionView = mContainer.fragment_permission_push_view
        pushPermissionView.visibility = if (Build.VERSION.SDK_INT >= 32) View.VISIBLE else View.GONE

        // Confirm Text View
        val confirmTextView = mContainer.fragment_permission_confirm_text_view
        confirmTextView.setOnClickListener {
            SharedPreferencesUtil().setIsNewInstall(mActivity, false)
            checkPermission()
        }
    }

    private fun checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA,
                        Manifest.permission.POST_NOTIFICATIONS
                    ),
                    PERMISSION_REQUEST_CODE
                )
            } else {
//                mListener.onLoginScreen()
                showMainScreen()
            }
        } else{
            if (/*ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || */ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        /*Manifest.permission.WRITE_EXTERNAL_STORAGE,*/ Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA
                    ),
                    PERMISSION_REQUEST_CODE
                )
            } else {
//                mListener.onLoginScreen()
                showMainScreen()
            }
        }

        setFcmSubscribe()
    }

    private fun setFcmSubscribe() {
        var dev = ""
        if (API_DOMAIN.contains("dev")) {
            dev = "dev_"
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("dev_$PUSH_TOPIC_TYPE_EVENT")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        NagajaLog().d("wooks, Success dev_PUSH_TOPIC_TYPE_EVENT unsubscribeFromTopic")
                    } else {
                        NagajaLog().e("wooks, Fail dev_PUSH_TOPIC_TYPE_EVENT unsubscribeFromTopic")
                    }
                }

            FirebaseMessaging.getInstance().unsubscribeFromTopic("dev_$PUSH_TOPIC_TYPE_NOTICE")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        NagajaLog().d("wooks, Success dev_PUSH_TOPIC_TYPE_NOTICE unsubscribeFromTopic")
                    } else {
                        NagajaLog().e("wooks, Fail dev_PUSH_TOPIC_TYPE_NOTICE unsubscribeFromTopic")
                    }
                }

            FirebaseMessaging.getInstance().unsubscribeFromTopic("dev_$PUSH_TOPIC_TYPE_SERVER_CHECK")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        NagajaLog().d("wooks, Success dev_PUSH_TOPIC_TYPE_SERVER_CHECK unsubscribeFromTopic")
                    } else {
                        NagajaLog().e("wooks, Fail dev_PUSH_TOPIC_TYPE_SERVER_CHECK unsubscribeFromTopic")
                    }
                }
        }

        FirebaseMessaging.getInstance().subscribeToTopic(dev + PUSH_TOPIC_TYPE_EVENT)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    NagajaLog().d("wooks, Success PUSH_TOPIC_TYPE_EVENT Subscribe")
                } else {
                    NagajaLog().e("wooks, Fail PUSH_TOPIC_TYPE_EVENT Subscribe")
                }
            }

        FirebaseMessaging.getInstance().subscribeToTopic(dev + PUSH_TOPIC_TYPE_NOTICE)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    NagajaLog().d("wooks, Success PUSH_TOPIC_TYPE_NOTICE Subscribe")
                } else {
                    NagajaLog().e("wooks, Fail PUSH_TOPIC_TYPE_NOTICE Subscribe")
                }
            }

        FirebaseMessaging.getInstance().subscribeToTopic(dev + PUSH_TOPIC_TYPE_SERVER_CHECK)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    NagajaLog().d("wooks, Success PUSH_TOPIC_TYPE_SERVER_CHECK Subscribe")
                } else {
                    NagajaLog().e("wooks, Fail PUSH_TOPIC_TYPE_SERVER_CHECK Subscribe")
                }
            }
    }

    private fun showMainScreen() {
        MAPP.IS_NON_MEMBER_SERVICE = true
        MAPP.USER_DATA.setNationPhone(SharedPreferencesUtil().getNationPhoneCode(mActivity)!!)
        mListener.onMainScreen()

        getUserDeviceInformation()

//        mListener.onLoginScreen()
    }

    /**
     * Permission Result
     */
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED) {

//                    mListener.onLoginScreen()
                    showMainScreen()
                } else {
                    mListener.onFinish()
                }
            } else {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    /*&& grantResults[4] == PackageManager.PERMISSION_GRANTED*/) {

//                    mListener.onLoginScreen()
                    showMainScreen()
                } else {
                    mListener.onFinish()
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel One1"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channelId = "one-channel"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = "channel description"
            val notificationManager = mActivity.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getLocation(): String {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )

            return ""
        } else {
            val gpsTracker = GpsTracker(mActivity)
            val latitude = gpsTracker.getLatitude()
            val longitude = gpsTracker.getLongitude()

//            SharedPreferencesUtil().setSaveLocation(mActivity, "$latitude,$longitude")

            return "$latitude,$longitude"
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                if (Environment.isExternalStorageManager()) {
//                    getUserDeviceInformation()
//                } else {
//                    mListener.onFinish()
//                }
//            } else {
//                getUserDeviceInformation()
//            }
//        }
//    }

    /**
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivity = activity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnPermissionFragmentListener) {
            mActivity = context as Activity

            if (context is OnPermissionFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnPermissionFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnPermissionFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnPermissionFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    /**
     * API. Deivce Information
     * */
//    private fun getUserDeviceInformation() {
//        var location = ""
//        location = if (null == getLocation() || TextUtils.isEmpty(getLocation()) || getLocation().contains("null")) {
//            "0,0"
//        } else {
//            getLocation()
//        }
//
//        val nagajaManager = NagajaManager().getInstance()
//        nagajaManager?.getUserDeviceInformation(
//            mRequestQueue,
//            object : NagajaManager.RequestListener<String?> {
//                override fun onSuccess(resultData: String?) {
//                    mListener.onMainScreen()
//                }
//
//                override fun onFail(errorMsg: String?) {
//                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
//                    NagajaLog().e("wooks, PermissionFragment getUserDeviceInformation API Error = $errorMsg")
//                }
//
//                override fun onFail(errorCode: Int?) {
//                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
//                    NagajaLog().e("wooks, PermissionFragment getUserDeviceInformation API Error Code = $errorCode")
//                }
//
//            },
//            Util().getAndroidID(mActivity),
//            "ANDROID",
//            Build.MODEL,
//            mActivity.packageManager.getPackageInfo(mActivity.packageName, 0).versionName,
//            location,
//            MAPP.USER_FCM_TOKEN,
//            SharedPreferencesUtil().getSelectLanguage(mActivity)!!.lowercase(Locale.getDefault())
//        )
//    }

    /**
     * API. Device Information
     * */
    private fun getUserDeviceInformation() {
        var location = ""
        location = if (null == getLocation() || TextUtils.isEmpty(getLocation()) || getLocation() == "null") {
            "0,0"
        } else {
            getLocation()
        }

        if (TextUtils.isEmpty(location) || location == "0,0") {
            when (SharedPreferencesUtil().getSelectLanguage(mActivity)) {
                SELECT_LANGUAGE_EN -> {
                    location = CURRENT_LOCATION_EN
                }

                SELECT_LANGUAGE_KO -> {
                    location = CURRENT_LOCATION_KO
                }

                SELECT_LANGUAGE_FIL -> {
                    location = CURRENT_LOCATION_FIL
                }

                SELECT_LANGUAGE_ZH -> {
                    location = CURRENT_LOCATION_ZH
                }

                SELECT_LANGUAGE_JA -> {
                    location = CURRENT_LOCATION_JA
                }
            }
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getUserDeviceInformation(
            mRequestQueue,
            object : NagajaManager.RequestListener<String?> {
                override fun onSuccess(resultData: String?) {
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, Splash getUserDeviceInformation API Error = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    NagajaLog().e("wooks, Splash getUserDeviceInformation API Error Code = $errorCode")
                }

            },
            Util().getAndroidID(mActivity),
            "ANDROID",
            Build.MODEL,
            mActivity.packageManager.getPackageInfo(mActivity.packageName, 0).versionName,
            location,
            MAPP.USER_FCM_TOKEN,
            SharedPreferencesUtil().getSelectLanguage(mActivity)!!.lowercase(Locale.getDefault())
        )
    }
}