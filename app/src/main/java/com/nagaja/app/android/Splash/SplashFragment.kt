package com.nagaja.app.android.Splash

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.*
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.nagaja.app.android.Data.DeviceData
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.JwtTokenRefreshData
import com.nagaja.app.android.Data.UserData
import com.nagaja.app.android.Data.VersionCheckData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.GpsTracker
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_EXPIRED_TOKEN
import com.nagaja.app.android.R
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_BLOCK_MEMBER
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_HUMAN_ACCOUNT
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_NOT_FOUND_PASSWORD
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_WITHDRAWAL_COMPLETE
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_WITHDRAWAL_REQUEST
import com.nagaja.app.android.Utils.*

import java.util.*


class SplashFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mRequestQueue: RequestQueue

    private lateinit var mListener: OnSplashFragmentListener

    interface OnSplashFragmentListener {
        fun onFinish()
        fun onPermissionScreen()
        fun onLoginScreen()
        fun onMainScreen()
    }

    companion object {
        const val PERMISSION_REQUEST_CODE     = 0x01

        fun newInstance(): SplashFragment {
            val args = Bundle()
            val fragment = SplashFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_splash, container, false)

        init()

        //Test
//        SharedPreferencesUtil().setSecureKey(mActivity, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJFWFBJUkVEX0RBVEUiOjE2OTMwNDExMjUsIlVJRCI6NzIsIk5JQ0tOQU1FIjoi7J6E7KeE7Jqx7LCo7J6l64uYIiwiaXNzIjoibmFnYWphIiwiTkFNRSI6IuyehOynhOyasSJ9.70ivkyYj9Hsb2AguYXgNxqkZknmYjSmSW3WIPPMcjhU")

        return mContainer
    }

    private fun init() {

        if (NetworkManager.checkNetworkState(mActivity)) {
            Handler(Looper.getMainLooper()).postDelayed({
                selectLanguage()
            }, 1000)
        } else {
            showDisconnectNetworkDialog()
        }

//        getDeviceInformation()

    }


    private fun getLocation(): String {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                SplashFragment.PERMISSION_REQUEST_CODE
            )

            return ""
        } else {
            val gpsTracker = GpsTracker(mActivity)
            val latitude = gpsTracker.getLatitude()
            val longitude = gpsTracker.getLongitude()
//            SharedPreferencesUtil().setSaveLocation(mActivity, "$latitude,$longitude")
//            SharedPreferencesUtil().setSaveLocation(mActivity, CURRENT_LOCATION_FIL)
            return "$latitude,$longitude"
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
                secureKeyCheck()
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
                secureKeyCheck()
            }
        }
    }

    private fun setDeviceLanguage() {

        var locale: Locale? = null
        var language = ""
        val saveLanguage = SharedPreferencesUtil().getSelectLanguage(mActivity)

        NagajaLog().d("wooks, Save Language = $saveLanguage")

        if (!TextUtils.isEmpty(saveLanguage)) {
            locale = when {
                saveLanguage.equals(SELECT_LANGUAGE_EN, ignoreCase = true) -> {
                    language = SELECT_LANGUAGE_EN
                    Locale.ENGLISH
                }
                saveLanguage.equals(SELECT_LANGUAGE_KO, ignoreCase = true) -> {
                    language = SELECT_LANGUAGE_KO
                    Locale.KOREA
                }
                saveLanguage.equals(SELECT_LANGUAGE_FIL, ignoreCase = true) -> {
                    language = SELECT_LANGUAGE_FIL
                    Locale("fil", "PH")
                }
                saveLanguage.equals(SELECT_LANGUAGE_ZH, ignoreCase = true) -> {
                    language = SELECT_LANGUAGE_ZH
                    Locale.CHINA
                }
                saveLanguage.equals(SELECT_LANGUAGE_JA, ignoreCase = true) -> {
                    language = SELECT_LANGUAGE_JA
                    Locale.JAPAN
                }
                else -> {
                    language = SELECT_LANGUAGE_EN
                    Locale.ENGLISH
                }
            }
        } else {
            language = SELECT_LANGUAGE_EN
            locale = Locale.ENGLISH
        }

//        val cfg = Configuration()
//        cfg.locale = Locale(language)
//        cfg.setLocale(locale)
//
//        mActivity.resources.updateConfiguration(cfg, null)

        Locale.setDefault(locale!!)
        Handler(Looper.getMainLooper()).postDelayed({
            val config = mActivity.resources.configuration
            config.setLocale(locale)
            config.locale = Locale(language)
            mActivity.resources.updateConfiguration(config, mActivity.resources.displayMetrics)
        }, 1000)


        if (!SharedPreferencesUtil().getIsNewInstall(mActivity)) {
            getVersionCheck()
        } else {
            mListener.onPermissionScreen()
        }
    }

    private fun showDisconnectNetworkDialog() {

        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_title_network_disconnect),
            mActivity.resources.getString(R.string.text_message_network_disconnect),
            mActivity.resources.getString(R.string.text_finish),
            mActivity.resources.getString(R.string.text_retry)
        )

        customDialog.setCancelable(false)
        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
                mListener.onFinish()
            }

            override fun onConfirm() {
                customDialog.dismiss()
                init()
            }
        })
        customDialog.show()
    }

    private fun getDeviceInformation() {

//        NagajaLog().d("wooks, ===== Device BOARD = ${Build.BOARD}")
//        NagajaLog().d("wooks, ===== Device BRAND = ${Build.BRAND}")
//        NagajaLog().d("wooks, ===== Device CPU_ABI = ${Build.CPU_ABI}")
//        NagajaLog().d("wooks, ===== Device DEVICE = ${Build.DEVICE}")
//        NagajaLog().d("wooks, ===== Device DISPLAY = ${Build.DISPLAY}")
//        NagajaLog().d("wooks, ===== Device FINGERPRINT = ${Build.FINGERPRINT}")
//        NagajaLog().d("wooks, ===== Device HOST = ${Build.HOST}")
//        NagajaLog().d("wooks, ===== Device ID = ${Build.ID}")
//        NagajaLog().d("wooks, ===== Device MANUFACTURER = ${Build.MANUFACTURER}")
//        NagajaLog().d("wooks, ===== Device MODEL = ${Build.MODEL}")
//        NagajaLog().d("wooks, ===== Device PRODUCT = ${Build.PRODUCT}")
//        NagajaLog().d("wooks, ===== Device TIME = ${Build.TIME}")
//        NagajaLog().d("wooks, ===== Device TYPE = ${Build.TYPE}")
//
//
//        val tm = mActivity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
//        NagajaLog().d("wooks, ===== Device 음성통화 상태 : [ getCallState ] >>> " + tm!!.callState);
//        NagajaLog().d("wooks, ===== Device 데이터통신 상태 : [ getDataState ] >>> " + tm.dataState);
//        NagajaLog().d("wooks, ===== Device 통신사 ISO 국가코드 : [ getNetworkCountryIso ] >>> "+tm.networkCountryIso);
//        NagajaLog().d("wooks, ===== Device 통신사 ISO 국가코드 : [ getSimCountryIso ] >>> "+tm.simCountryIso);
//        NagajaLog().d("wooks, ===== Device 망사업자 MCC+MNC : [ getNetworkOperator ] >>> "+tm.networkOperator);
//        NagajaLog().d("wooks, ===== Device 망사업자 MCC+MNC : [ getSimOperator ] >>> "+tm.simOperator);
//        NagajaLog().d("wooks, ===== Device 망사업자명 : [ getNetworkOperatorName ] >>> "+tm.networkOperatorName);
//        NagajaLog().d("wooks, ===== Device 망사업자명 : [ getSimOperatorName ] >>> "+tm.simOperatorName);
//        NagajaLog().d("wooks, ===== Device SIM 카드 상태 : [ getSimState ] >>> "+tm.simState);

        val deviceData = DeviceData()

        deviceData.setBoard(Build.BOARD)
        deviceData.setBrand(Build.BRAND)
        deviceData.setCpuAbi(Build.CPU_ABI)
        deviceData.setDevice(Build.DEVICE)
        deviceData.setDisplay(Build.DISPLAY)
        deviceData.setFingerPrint(Build.FINGERPRINT)
        deviceData.setHost(Build.HOST)
        deviceData.setId(Build.ID)
        deviceData.setManufacturer(Build.MANUFACTURER)
        deviceData.setModel(Build.MODEL)
        deviceData.setProduct(Build.PRODUCT)
        deviceData.setTime(Build.TIME)
        deviceData.setType(Build.TYPE)

//        NagajaLog().d("wooks, ===== Device BOARD = ${deviceData.getBoard()}")
//        NagajaLog().d("wooks, ===== Device BRAND = ${deviceData.getBrand()}")
//        NagajaLog().d("wooks, ===== Device CPU_ABI = ${deviceData.getCpuAbi()}")
//        NagajaLog().d("wooks, ===== Device DEVICE = ${deviceData.getDevice()}")
//        NagajaLog().d("wooks, ===== Device DISPLAY = ${deviceData.getDisplay()}")
//        NagajaLog().d("wooks, ===== Device FINGERPRINT = ${deviceData.getFingerPrint()}")
//        NagajaLog().d("wooks, ===== Device HOST = ${deviceData.getHost()}")
//        NagajaLog().d("wooks, ===== Device ID = ${deviceData.getId()}")
//        NagajaLog().d("wooks, ===== Device MANUFACTURER = ${deviceData.getManufacturer()}")
//        NagajaLog().d("wooks, ===== Device MODEL = ${deviceData.getModel()}")
//        NagajaLog().d("wooks, ===== Device PRODUCT = ${deviceData.getProduct()}")
//        NagajaLog().d("wooks, ===== Device TIME = ${deviceData.getTime()}")
//        NagajaLog().d("wooks, ===== Device TYPE = ${deviceData.getType()}")
    }

    private fun showUpdateDialog() {

        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_title_update),
            mActivity.resources.getString(R.string.text_message_update),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_update)
        )

        customDialog.setCancelable(false)
        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()

                mListener.onFinish()
            }

            override fun onConfirm() {
                customDialog.dismiss()

                val appPackageName = mActivity.packageName
                if (!TextUtils.isEmpty(appPackageName)) {
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")))
                    } catch (e: ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=$appPackageName")))
                    }
                }

                mListener.onFinish()
            }
        })
        customDialog.show()
    }

    private fun showServerCheckDialog() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_SERVER_CHECK,
            "",
            "",
            "",
            ""
        )

        customDialog.setCancelable(false)
        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
                mListener.onFinish()
            }
        })
        customDialog.show()
    }

    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get FCM Registration Token
                val token: String = task.result!!
                NagajaLog().d("wooks, FCM Token = $token")

                if (SharedPreferencesUtil().getIsNewInstall(mActivity)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(PUSH_TOPIC_TYPE_EVENT)
                    FirebaseMessaging.getInstance().subscribeToTopic(PUSH_TOPIC_TYPE_NOTICE)
                    FirebaseMessaging.getInstance().subscribeToTopic(PUSH_TOPIC_TYPE_SERVER_CHECK)
                }

                MAPP.USER_FCM_TOKEN = token
            })
    }

    private fun checkVersion(versionCheckData: VersionCheckData) {

        var packageInfo: PackageInfo? = null
        try {
            packageInfo = mActivity.packageManager.getPackageInfo(mActivity.packageName, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val deviceVersion = packageInfo!!.versionName

        if (versionCheckData.getVersion()!! > deviceVersion) {
            NagajaLog().d("wooks, version = ${versionCheckData.getVersion()}")
            showUpdateDialog()
        } else {
            if (versionCheckData.getIsServerCheck()!!) {
                showServerCheckDialog()
            } else {
                checkPermission()
            }
        }
    }

    private fun secureKeyCheck() {

        if (!TextUtils.isEmpty(SharedPreferencesUtil().getSecureKey(mActivity))) {
            getSecureKeyLogin(SharedPreferencesUtil().getSecureKey(mActivity)!!)
        } else {
//            mListener.onLoginScreen()

            MAPP.IS_NON_MEMBER_SERVICE = true
            MAPP.USER_DATA.setNationPhone(SharedPreferencesUtil().getNationPhoneCode(mActivity)!!)
            mListener.onMainScreen()

            getUserDeviceInformation()
        }
    }

    private fun selectLanguage() {

        var locale: Locale? = null
        var language = ""

        when (SharedPreferencesUtil().getSelectLanguage(mActivity)) {
            SELECT_LANGUAGE_EN -> {
                language = SELECT_LANGUAGE_EN
                locale = Locale.ENGLISH
                MAPP.SELECT_LANGUAGE_CODE = "1"
            }

            SELECT_LANGUAGE_KO -> {
                language = SELECT_LANGUAGE_KO
                locale = Locale.KOREA
                MAPP.SELECT_LANGUAGE_CODE = "82"
            }

            SELECT_LANGUAGE_FIL -> {
                language = SELECT_LANGUAGE_FIL
                locale = Locale("fil", "PH")
                MAPP.SELECT_LANGUAGE_CODE = "63"
            }

            SELECT_LANGUAGE_ZH -> {
                language = SELECT_LANGUAGE_ZH
                locale = Locale.CHINA
                MAPP.SELECT_LANGUAGE_CODE = "86"
            }

            SELECT_LANGUAGE_JA -> {
                language = SELECT_LANGUAGE_JA
                locale = Locale.JAPAN
                MAPP.SELECT_LANGUAGE_CODE = "81"
            }

            else -> {
                language = SELECT_LANGUAGE_EN
                locale = Locale.ENGLISH
                MAPP.SELECT_LANGUAGE_CODE = "1"
            }
        }

        val cfg = Configuration()
        cfg.locale = Locale(language)
        cfg.setLocale(locale)

        mActivity.resources.updateConfiguration(cfg, null)

//        setDeviceLanguage()

        if (!SharedPreferencesUtil().getIsNewInstall(mActivity)) {
            getVersionCheck()
        } else {
            mListener.onPermissionScreen()
        }

        getFcmToken()
    }

    private fun showLoginErrorCustomPopup(errorCode: Int) {

        var title = ""
        var message = ""
        when (errorCode) {
            ERROR_CODE_BLOCK_MEMBER -> {
                title = mActivity.resources.getString(R.string.text_block_account)
                message = mActivity.resources.getString(R.string.text_message_block_account)
            }

            ERROR_CODE_HUMAN_ACCOUNT -> {
                title = mActivity.resources.getString(R.string.text_dormant_account)
//                message = mActivity.resources.getString(R.string.text_message_block_account)
            }

            ERROR_CODE_WITHDRAWAL_REQUEST,
            ERROR_CODE_WITHDRAWAL_COMPLETE -> {
                title = mActivity.resources.getString(R.string.text_withdrawal_account)
                message = mActivity.resources.getString(R.string.text_message_withdrawal_account)
            }

            ERROR_CODE_NOT_FOUND_PASSWORD -> {
                title = mActivity.resources.getString(R.string.text_noti)
                message = mActivity.resources.getString(R.string.text_error_not_match_password)
            }
        }

        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NO_CANCEL,
            title,
            message,
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()

                SharedPreferencesUtil().setSecureKey(mActivity, "")
                SharedPreferencesUtil().setRefreshKey(mActivity, "")

                MAPP.IS_NON_MEMBER_SERVICE = true
                MAPP.USER_DATA.setNationPhone(SharedPreferencesUtil().getNationPhoneCode(mActivity)!!)
                mListener.onMainScreen()
                getUserDeviceInformation()
            }
        })
        customDialog.show()
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

                    secureKeyCheck()
                } else {
                    mListener.onFinish()
                }
            } else {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED) {

                    secureKeyCheck()

                } else {
                    mListener.onFinish()
                }
            }
        }
    }

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
        if (context is OnSplashFragmentListener) {
            mActivity = context as Activity

            if (context is OnSplashFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnSplashFragmentListener"
                )
            }
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        if (context is OnSplashFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnSplashFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }


    // ============================================================================================
    // API.
    // ============================================================================================

    /**
     * API. getVersionCheck
     */
    private fun getVersionCheck() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getVersionCheck(
            mRequestQueue,
            object : NagajaManager.RequestListener<VersionCheckData?> {
                override fun onSuccess(resultData: VersionCheckData?) {
                    if (null == resultData) {
                        return
                    }

                    checkVersion(resultData)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().d("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

            }
        )
    }

    /**
     * API. getSecureKeyLogin
     */
    private fun getSecureKeyLogin(secureKey: String) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getSecureKeyLogin(
            mRequestQueue,
            object : NagajaManager.RequestListener<UserData> {
                override fun onSuccess(resultData: UserData) {
                    NagajaLog().d("wooks, Login Success Email = ${resultData.getEmailId()}")

                    SharedPreferencesUtil().setSecureKey(mActivity, resultData.getSecureKey())
                    SharedPreferencesUtil().setRefreshKey(mActivity, resultData.getRefreshKey())

                    MAPP.USER_DATA = resultData

                    mListener.onMainScreen()

                    getLoginSuccessDeviceInformation()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
//                    if (errorCode == 1) {
//                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_checked_email_password))
//                    }
                    if (errorCode == ERROR_EXPIRED_TOKEN) {
                        getJwtTokenRefresh()
                    } else {
//                        mListener.onLoginScreen()

                        if (errorCode == ERROR_CODE_BLOCK_MEMBER || errorCode == ERROR_CODE_HUMAN_ACCOUNT
                            || errorCode == ERROR_CODE_WITHDRAWAL_REQUEST || errorCode == ERROR_CODE_WITHDRAWAL_COMPLETE) {

                            showLoginErrorCustomPopup(errorCode)
                        } else {
                            MAPP.IS_NON_MEMBER_SERVICE = true
                            MAPP.USER_DATA.setNationPhone(SharedPreferencesUtil().getNationPhoneCode(mActivity)!!)
                            mListener.onMainScreen()
                            getUserDeviceInformation()
                        }
                    }
                }
            },
            secureKey
        )
    }

    /**
     * API. getSecureKeyLogin
     */
    private fun getJwtTokenRefresh() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getJwtTokenRefresh(
            mRequestQueue,
            object : NagajaManager.RequestListener<JwtTokenRefreshData> {
                override fun onSuccess(resultData: JwtTokenRefreshData) {

                    getSecureKeyLogin(resultData.getSecureKey()!!)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                    SharedPreferencesUtil().setSecureKey(mActivity, "")
                    SharedPreferencesUtil().setRefreshKey(mActivity, "")
                    getUserDeviceInformation()

                    mListener.onLoginScreen()
                }
            },
            SharedPreferencesUtil().getRefreshKey(mActivity)!!
        )
    }

    /**
     * API. getLoginSuccessDeviceInformation
     */
    private fun getLoginSuccessDeviceInformation() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getLoginSuccessDeviceInformation(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            Util().getAndroidID(mActivity),
            "ANDROID",
            MAPP.USER_FCM_TOKEN
        )
    }

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