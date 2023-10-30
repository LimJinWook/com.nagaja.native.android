package com.nagaja.app.android.Utils

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Utils.NetworkProvider
import com.nagaja.app.android.Utils.NetworkProvider.Companion.API_DOMAIN
import com.nagaja.app.android.Utils.NetworkProvider.Companion.APPLICATION_COMPANY
import com.nagaja.app.android.Utils.NetworkProvider.Companion.APPLICATION_COMPANY_MEMBER_TYPE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.BANNER_EVENT
import com.nagaja.app.android.Utils.NetworkProvider.Companion.BOARD_COMMENT_DELETE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.BOARD_COMMENT_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.BOARD_COMMENT_WRITE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.BOARD_DELETE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.BOARD_DETAIL
import com.nagaja.app.android.Utils.NetworkProvider.Companion.BOARD_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.BOARD_REGISTER_MODIFY
import com.nagaja.app.android.Utils.NetworkProvider.Companion.BOARD_REGISTER_SAVE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.CHANGE_ADDRESS
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_AND_MEMBER_SEARCH
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_BREAK_TIME
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_BUSINESS_TIME
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_CHANGE_SERVICE_STATUS
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_DAY_OFF_CHANGE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_DEFAULT
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_INFORMATION
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_JOB_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_JOB_MODIFY
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_JOB_REGISTER_SAVE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_MANAGER
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_MEMBER
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_MEMBER_REGISTER
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_MEMBER_REMOVE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_NOTE_COMPANY_STATUS
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_NOTE_DELETE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_NOTE_DETAIL
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_NOTE_DETAIL_SENDER
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_NOTE_INFORMATION
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_NOTE_KEEP
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_NOTE_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_NOTE_SEND_SAVE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_NOTIFICATION_COUNT
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_PRODUCT_CATEGORY
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_PRODUCT_CURRENCY
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_PRODUCT_ITEM
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_PRODUCT_MODIFY
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_PRODUCT_REMOVE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_PRODUCT_SAVE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_PRODUCT_UPDATE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_REGULAR_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_REPORT_UPLOAD
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_RESERVATION_DETAIL
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_RESERVATION_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_RESERVATION_MEMO_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_RESERVATION_MEMO_SAVE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_RESERVATION_PERSON_LIMIT
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_SALES_CHANGE_ADD_FEATURES
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_SALES_DEFAULT_INFORMATION
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_USED_MARKET_DELETE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_USED_MARKET_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_USED_MARKET_REGISTER
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_USED_MARKET_REGISTER_MODIFY
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_USED_MARKET_STATUS_CHANGE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_USE_CURRENCY
import com.nagaja.app.android.Utils.NetworkProvider.Companion.CURRENCY_DATA
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_SETTING_AREA
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DELETE_PROFILE_IMAGE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.EVENT_DATA
import com.nagaja.app.android.Utils.NetworkProvider.Companion.EXCHANGE_RATE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.EXIST_EMAIL_CHECK
import com.nagaja.app.android.Utils.NetworkProvider.Companion.FIND_MEMBER
import com.nagaja.app.android.Utils.NetworkProvider.Companion.JWT_TOKEN_BY_EMAIL
import com.nagaja.app.android.Utils.NetworkProvider.Companion.JWT_TOKEN_REFRESH
import com.nagaja.app.android.Utils.NetworkProvider.Companion.LOCATION_AREA
import com.nagaja.app.android.Utils.NetworkProvider.Companion.LOCATION_NAME
import com.nagaja.app.android.Utils.NetworkProvider.Companion.LOCATION_NATION
import com.nagaja.app.android.Utils.NetworkProvider.Companion.LOGIN
import com.nagaja.app.android.Utils.NetworkProvider.Companion.LOGOUT
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MAIN_MENU_ITEM
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MEMBER_CHANGE_PASSWORD
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MEMBER_FIND_EMAIL_DISCONNECT
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MEMBER_FIND_PASSWORD_EMAIL_CHECK
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MEMBER_INFORMATION
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MEMBER_PHONE_AUTH_CHECK_CODE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MEMBER_PHONE_AUTH_CODE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MEMBER_SIGN_UP
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MODIFY_COMPANY_DEFAULT_INFORMATION
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MODIFY_PROFILE_UPDATE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MY_JOB_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MY_MISSING_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MY_USED_MARKET_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTE_COMPANY_STATUS
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTE_DELETE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTE_DETAIL
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTE_DETAIL_SENDER
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTE_KEEP
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTE_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTE_SEND_SAVE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTICE_COMMENT
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTICE_COMMENT_DELETE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTICE_COMMENT_WRITE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTICE_DETAIL
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTICE_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTIFICATION_CONFIRM
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTIFICATION_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.PHONE_AUTH_CHECK_CODE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.PHONE_AUTH_CODE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.PHONE_NUMBER_CHANGE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.RECOMMEND_PLACE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.REGULAR_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.REPORT_UPLOAD
import com.nagaja.app.android.Utils.NetworkProvider.Companion.RESERVATION_CHANGE_STATUS
import com.nagaja.app.android.Utils.NetworkProvider.Companion.RESERVATION_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.RESERVATION_SAVE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.RESERVATION_SCHEDULE_DAY
import com.nagaja.app.android.Utils.NetworkProvider.Companion.RESERVATION_SCHEDULE_TIME
import com.nagaja.app.android.Utils.NetworkProvider.Companion.REVIEW_RECOMMEND_SAVE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.SAVE_USER_LOCATION
import com.nagaja.app.android.Utils.NetworkProvider.Companion.SEARCH
import com.nagaja.app.android.Utils.NetworkProvider.Companion.SECURE_KEY_LOGIN
import com.nagaja.app.android.Utils.NetworkProvider.Companion.SERVICE_AREA_LOCATION
import com.nagaja.app.android.Utils.NetworkProvider.Companion.SERVICE_INFORMATION
import com.nagaja.app.android.Utils.NetworkProvider.Companion.SIGN_UP_EMAIL_CHECK
import com.nagaja.app.android.Utils.NetworkProvider.Companion.SIGN_UP_NICK_NAME_CHECK
import com.nagaja.app.android.Utils.NetworkProvider.Companion.SIGN_UP_SEND_EMAIL
import com.nagaja.app.android.Utils.NetworkProvider.Companion.SNS_EMAIL_CHECK
import com.nagaja.app.android.Utils.NetworkProvider.Companion.SOCIAL_MEMBER_SIGN_UP
import com.nagaja.app.android.Utils.NetworkProvider.Companion.STORE_CATEGORY_DATA
import com.nagaja.app.android.Utils.NetworkProvider.Companion.STORE_DETAIL
import com.nagaja.app.android.Utils.NetworkProvider.Companion.STORE_LIST_DATA
import com.nagaja.app.android.Utils.NetworkProvider.Companion.STORE_MENU
import com.nagaja.app.android.Utils.NetworkProvider.Companion.STORE_RECOMMEND_SAVE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.STORE_REGULAR_SAVE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.STORE_REVIEW
import com.nagaja.app.android.Utils.NetworkProvider.Companion.STORE_REVIEW_DELETE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.STORE_REVIEW_MODIFY
import com.nagaja.app.android.Utils.NetworkProvider.Companion.STORE_REVIEW_UPLOAD_SAVE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.USED_MARKET_DELETE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.USED_MARKET_DETAIL
import com.nagaja.app.android.Utils.NetworkProvider.Companion.USED_MARKET_LIST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.USED_MARKET_RECOMMEND_SAVE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.USED_MARKET_REGISTER
import com.nagaja.app.android.Utils.NetworkProvider.Companion.USED_MARKET_REGISTER_MODIFY
import com.nagaja.app.android.Utils.NetworkProvider.Companion.USED_MARKET_STATUS_CHANGE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.VERSION_CHECK
import com.nagaja.app.android.Base.NagajaFragment.Companion.LOGIN_TYPE_FACEBOOK
import com.nagaja.app.android.Base.NagajaFragment.Companion.LOGIN_TYPE_GOOGLE
import com.nagaja.app.android.Base.NagajaFragment.Companion.LOGIN_TYPE_KAKAO
import com.nagaja.app.android.Base.NagajaFragment.Companion.LOGIN_TYPE_NAVER
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_COMPANY_MEMBER_REGISTER_ERROR_MANAGER_LIMIT
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_COMPANY_MEMBER_REGISTER_ERROR_WRONG_GRANT
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_DIFFERENT_TYPE_SIGN_UP
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_DO_NOT_MATCH_ID
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_EXISTING_ID
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_EXISTING_MANAGER
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_EXISTING_NICK_NAME
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_EXISTING_PHONE_NUMBER
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_NOT_ENOUGH_POINT
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_NOT_FOUND_ID
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_NOT_FOUND_USER
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_NO_MEMBER
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_NO_RECOMMEND
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_OLD_PASSWORD
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_RESERVATION_DATE_SELECT_ERROR
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_WRONG_FORMAT_PASSWORD
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_DO_NOT_EXIST_TOKEN
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_EXPIRED_TOKEN
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_INVALID_TOKEN
import com.nagaja.app.android.Utils.NetworkProvider.Companion.CHANGE_PUSH_STATUS
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_POINT
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_POINT_HISTORY
import com.nagaja.app.android.Utils.NetworkProvider.Companion.COMPANY_RESERVATION_STATUS_CHANGE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEVICE_INFORMATION
import com.nagaja.app.android.Utils.NetworkProvider.Companion.LANGUAGE_CHANGE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.LOGIN_SUCCESS_DEVICE_INFORMATION
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MEMBER_POINT
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MEMBER_POINT_HISTORY
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MEMBER_PROFILE
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MEMBER_WITHDRAWAL
import com.nagaja.app.android.Utils.NetworkProvider.Companion.MY_LOCATION_SETTING
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTE_COMPANY_RECEIVER
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTE_COMPANY_SENDER
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTE_MEMBER_RECEIVER
import com.nagaja.app.android.Utils.NetworkProvider.Companion.NOTE_MEMBER_SENDER
import com.nagaja.app.android.Utils.NetworkProvider.Companion.USED_MARKET_COMPANY_PULL_UP
import com.nagaja.app.android.Utils.NetworkProvider.Companion.USED_MARKET_COMPANY_PULL_UP_COUNT
import com.nagaja.app.android.Utils.NetworkProvider.Companion.USED_MARKET_MEMBER_PULL_UP
import com.nagaja.app.android.Utils.NetworkProvider.Companion.USED_MARKET_MEMBER_PULL_UP_COUNT
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


open class NagajaManager {

    private val API_RESULT_DEFAULT_VALUE = -9999

    private var mInstance: NagajaManager? = null

    @Synchronized
    fun getInstance(): NagajaManager? {
        if (null == mInstance) {
            mInstance = try {
                NagajaManager()
            } finally {
            }
        }
        return mInstance
    }

    interface RequestListener<T> : Serializable {
        fun onSuccess(resultData: T)
        fun onFail(errorMsg: String?)
        fun onFail(errorCode: Int?)
    }

    private fun getJsonString(jsonObject: JSONObject?, key: String): Boolean {
        var isUse = false
        try {
            if (null != jsonObject) {
                if (jsonObject.has(key)) {
                    isUse = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isUse
    }

    fun getTextEmptyCheck(text: String): Boolean {
        return TextUtils.isEmpty(text) || text == "null"
    }

    // ======================================================================================================================
    // ======================================================================================================================
    // ======================================================================================================================
    // ======================================================================================================================
    // ======================================================================================================================
    // ======================================================================================================================
    // ======================================================================================================================

    /**
     * Get Version Check
     * POST
     */
    fun getVersionCheck(requestQueue: RequestQueue, listener: RequestListener<VersionCheckData?>?) {

        NagajaLog().d("wooks, API ========= getVersionCheck : os = Android")
        NagajaLog().d("wooks, API ========= getVersionCheck : ${API_DOMAIN + VERSION_CHECK}")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + VERSION_CHECK,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= VERSION_CHECK : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val versionCheckData = VersionCheckData()

                        versionCheckData.setVersion(jsonObject.getString("version"))
                        versionCheckData.setIsServerCheck(jsonObject.getBoolean("is_server_check"))
                        versionCheckData.setServerCheckStartDate(jsonObject.getString("server_check_start_date"))
                        versionCheckData.setServerCheckEndDate(jsonObject.getString("server_check_end_date"))

                        listener?.onSuccess(versionCheckData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener!!.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener!!.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message = "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String>? {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "aaaaaaaaaaaaaaaaaaaaaaaa"
//                return params
//            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["os"] = "Android"
                return params
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Board Popularity List
     * POST
     */
    fun getBoardPopularityList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<NativeBoardPopularityData>>?) {

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            API_DOMAIN + "app/board/popularity?cago=30",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= getBoardPopularityList : $response")

                try {
//                    val json = JSONObject(response)
                    val status = response.getInt("status")
                    if (status == 0) {
                        val jsonArray = response.getJSONArray("data")
                        val nativeBoardPopularityListData = ArrayList<NativeBoardPopularityData>()
                        for (i in 0 until jsonArray.length()) {
                            val nativeBoardPopularityData = NativeBoardPopularityData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                nativeBoardPopularityData.setBoardUid(jsonObject.getInt("boardUID"))
                                nativeBoardPopularityData.setBoardStatus(jsonObject.getInt("boardStatus"))
                                nativeBoardPopularityData.setBoardSubject(jsonObject.getString("boardSubject"))
                                nativeBoardPopularityData.setBoardContent(jsonObject.getString("boardContent"))
                                nativeBoardPopularityData.setViewCount(jsonObject.getInt("viewCount"))
                                nativeBoardPopularityData.setIsUse(jsonObject.getBoolean("useYn"))
                                nativeBoardPopularityData.setCreateDate(jsonObject.getString("createDate"))
                                if (!jsonObject.isNull("updateDate")) {
                                    nativeBoardPopularityData.setUpdateDate(jsonObject.getString("updateDate"))
                                }
                                nativeBoardPopularityData.setMemberUid(jsonObject.getInt("memberUID"))
                                nativeBoardPopularityData.setMemberName(jsonObject.getString("memberNName"))
                                nativeBoardPopularityData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                nativeBoardPopularityData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                                nativeBoardPopularityData.setCategoryName(jsonObject.getString("cagoName"))
                                nativeBoardPopularityData.setParentCategoryName(jsonObject.getString("parentCagoName"))

                                nativeBoardPopularityListData.add(nativeBoardPopularityData)
                            }

                        }

                        listener?.onSuccess(nativeBoardPopularityListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener!!.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener!!.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message = "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Board Comment List
     * POST
     */
    fun getBoardCommentList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<NativeBoardPopularityData>>?) {

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            API_DOMAIN + "app/board/standby?cago=30",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= getBoardCommentList : $response")

                try {
//                    val json = JSONObject(response)
                    val status = response.getInt("status")
                    if (status == 0) {
                        val jsonArray = response.getJSONArray("data")
                        val nativeBoardPopularityListData = ArrayList<NativeBoardPopularityData>()
                        for (i in 0 until jsonArray.length()) {
                            val nativeBoardPopularityData = NativeBoardPopularityData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                nativeBoardPopularityData.setBoardUid(jsonObject.getInt("boardUID"))
                                nativeBoardPopularityData.setBoardStatus(jsonObject.getInt("boardStatus"))
                                nativeBoardPopularityData.setBoardSubject(jsonObject.getString("boardSubject"))
                                nativeBoardPopularityData.setBoardContent(jsonObject.getString("boardContent"))
                                nativeBoardPopularityData.setViewCount(jsonObject.getInt("viewCount"))
                                nativeBoardPopularityData.setIsUse(jsonObject.getBoolean("useYn"))
                                nativeBoardPopularityData.setCreateDate(jsonObject.getString("createDate"))
                                if (!jsonObject.isNull("updateDate")) {
                                    nativeBoardPopularityData.setUpdateDate(jsonObject.getString("updateDate"))
                                }
                                nativeBoardPopularityData.setMemberUid(jsonObject.getInt("memberUID"))
                                nativeBoardPopularityData.setMemberName(jsonObject.getString("memberNName"))
                                nativeBoardPopularityData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                nativeBoardPopularityData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                                nativeBoardPopularityData.setCategoryName(jsonObject.getString("cagoName"))
                                nativeBoardPopularityData.setParentCategoryName(jsonObject.getString("parentCagoName"))

                                nativeBoardPopularityListData.add(nativeBoardPopularityData)
                            }

                        }

                        listener?.onSuccess(nativeBoardPopularityListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener!!.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener!!.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message = "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Board Data
     * POST
     */
    fun getBoardData(requestQueue: RequestQueue, listener: RequestListener<NativeBoardData>?,
                     boardUid: Int) {

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            API_DOMAIN + "app/board/detail/$boardUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= getBoardData : $response")

                try {
//                    val json = JSONObject(response)
                    val status = response.getInt("status")
                    if (status == 0) {
                        val jsonObject = response.getJSONObject("data")
                        val nativeBoardData = NativeBoardData()
                        if (null != jsonObject) {
                            nativeBoardData.setBoardUid(jsonObject.getInt("boardUID"))
                            nativeBoardData.setBoardStatus(jsonObject.getInt("boardStatus"))
                            nativeBoardData.setBoardSubject(jsonObject.getString("boardSubject"))
                            nativeBoardData.setBoardContent(jsonObject.getString("boardContent"))
                            nativeBoardData.setViewCount(jsonObject.getInt("viewCount"))
                            nativeBoardData.setIsUse(jsonObject.getBoolean("useYn"))
                            nativeBoardData.setCreateDate(jsonObject.getString("createDate"))
                            if (!jsonObject.isNull("updateDate")) {
                                nativeBoardData.setUpdateDate(jsonObject.getString("updateDate"))
                            }
                            nativeBoardData.setMemberUid(jsonObject.getInt("memberUID"))
                            nativeBoardData.setMemberName(jsonObject.getString("memberNName"))
                            nativeBoardData.setCategoryUid(jsonObject.getInt("cagoUID"))
                            nativeBoardData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                            nativeBoardData.setCategoryName(jsonObject.getString("cagoName"))
                            nativeBoardData.setParentCategoryName(jsonObject.getString("parentCagoName"))
                            nativeBoardData.setRecommendCount(jsonObject.getInt("recommCount"))
                            nativeBoardData.setBookMarkCount(jsonObject.getInt("bookmarkCount"))
                            nativeBoardData.setCommentCount(jsonObject.getInt("commentCount"))
                            nativeBoardData.setIsBookMark(jsonObject.getBoolean("bookmarkYn"))
                            nativeBoardData.setIsRecommend(jsonObject.getBoolean("recommYn"))
                            nativeBoardData.setIsPopularity(jsonObject.getBoolean("popularityYn"))
                            nativeBoardData.setIsStandBy(jsonObject.getBoolean("standByYn"))

                            val jsonBoardImageArray = jsonObject.getJSONArray("boardImg")
                            val nativeBoardImageListData = ArrayList<NativeBoardImageData>()
                            for (k in 0 until jsonBoardImageArray.length()) {
                                val nativeBoardImageData = NativeBoardImageData()
                                val jsonBoardImageObject = jsonBoardImageArray.getJSONObject(k)
                                if (null != jsonBoardImageObject) {
                                    nativeBoardImageData.setBoardImageUid(jsonBoardImageObject.getInt("boardImageUID"))
                                    nativeBoardImageData.setBoardImageSort(jsonBoardImageObject.getInt("boardImageSort"))
                                    nativeBoardImageData.setBoardImageOrigin(jsonBoardImageObject.getString("boardImageOrigin"))
                                    nativeBoardImageData.setBoardImageName(jsonBoardImageObject.getString("boardImageName"))
                                    nativeBoardImageData.setIsUseYN(jsonBoardImageObject.getBoolean("useYn"))
                                    nativeBoardImageData.setCreateDate(jsonBoardImageObject.getString("createDate"))
                                    if (!jsonBoardImageObject.isNull("updateDate")) {
                                        nativeBoardImageData.setUpdateDate(jsonBoardImageObject.getString("updateDate"))
                                    }
                                    nativeBoardImageData.setBoardUid(jsonBoardImageObject.getInt("boardUID"))
                                }

                                nativeBoardImageListData.add(nativeBoardImageData)
                            }
                            nativeBoardData.setBoardImageList(nativeBoardImageListData)

                            val jsonBoardFileArray = jsonObject.getJSONArray("boardFile")
                            val nativeBoardFileListData = ArrayList<NativeBoardFileData>()
                            for (k in 0 until jsonBoardFileArray.length()) {
                                val nativeBoardFileData = NativeBoardFileData()
                                val jsonBoardFileObject = jsonBoardFileArray.getJSONObject(k)
                                if (null != jsonBoardFileObject) {
                                    nativeBoardFileData.setBoardFileUid(jsonBoardFileObject.getInt("boardFileUID"))
                                    nativeBoardFileData.setBoardFileSort(jsonBoardFileObject.getInt("boardFileSort"))
                                    nativeBoardFileData.setBoardFileOrigin(jsonBoardFileObject.getString("boardFileOrigin"))
                                    nativeBoardFileData.setBoardFileName(jsonBoardFileObject.getString("boardFileName"))
                                    nativeBoardFileData.setIsUseYN(jsonBoardFileObject.getBoolean("useYn"))
                                    nativeBoardFileData.setCreateDate(jsonBoardFileObject.getString("createDate"))
                                    if (!jsonBoardFileObject.isNull("updateDate")) {
                                        nativeBoardFileData.setUpdateDate(jsonBoardFileObject.getString("updateDate"))
                                    }

                                    nativeBoardFileData.setBoardUid(jsonBoardFileObject.getInt("boardUID"))
                                }

                                nativeBoardFileListData.add(nativeBoardFileData)
                            }
                            nativeBoardData.setBoardFileList(nativeBoardFileListData)
                        }

                        listener?.onSuccess(nativeBoardData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener!!.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener!!.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message = "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Board Comment Data
     * POST
     */
    fun getBoardCommentData(requestQueue: RequestQueue, listener: RequestListener<ArrayList<NativeCommentData>>?,
                            boardUid: Int) {

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            API_DOMAIN + "app/board/comment/$boardUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= getBoardCommentData : $response")

                try {
                    val status = response.getInt("status")
                    if (status == 0) {
                        val jsonArray = response.getJSONArray("data")
                        val nativeCommentListData = ArrayList<NativeCommentData>()
                        for (i in 0 until jsonArray.length()) {
                            val nativeCommentData = NativeCommentData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                nativeCommentData.setCommentUid(jsonObject.getInt("commentUID"))
                                nativeCommentData.setCommentStatus(jsonObject.getInt("commentStatus"))
                                nativeCommentData.setComment(jsonObject.getString("comment"))
                                nativeCommentData.setIsUse(jsonObject.getBoolean("useYn"))
                                nativeCommentData.setCreateDate(jsonObject.getString("createDate"))
                                if (!jsonObject.isNull("updateDate")) {
                                    nativeCommentData.setUpdateDate(jsonObject.getString("updateDate"))
                                }
                                nativeCommentData.setMemberUid(jsonObject.getInt("memberUID"))
                                nativeCommentData.setMemberName(jsonObject.getString("memberName"))
                                nativeCommentData.setBoardUid(jsonObject.getInt("boardUID"))
                                nativeCommentData.setMemberNName(jsonObject.getString("memNName"))

                                nativeCommentListData.add(nativeCommentData)
                            }
                        }

                        listener?.onSuccess(nativeCommentListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener!!.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener!!.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message = "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Job List Data
     * POST
     * @param type > 0: 구인, 1: 구직, 2: 신고, 3: 실종, 4: 지명수배
     */
    fun getJobAndMissingListData(requestQueue: RequestQueue, listener: RequestListener<ArrayList<NativeJobAndMissingData>>?,
                                 type: Int) {

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + "app/board",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= getJobListData : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val nativeJobAndMissingListData = ArrayList<NativeJobAndMissingData>()
                        for (i in 0 until jsonArray.length()) {
                            val nativeJobAndMissingData = NativeJobAndMissingData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                nativeJobAndMissingData.setBoardUid(jsonObject.getInt("boardUID"))
                                nativeJobAndMissingData.setBoardStatus(jsonObject.getInt("boardStatus"))
                                nativeJobAndMissingData.setBoardSubject(jsonObject.getString("boardSubject"))
                                nativeJobAndMissingData.setBoardContent(jsonObject.getString("boardContent"))
                                nativeJobAndMissingData.setViewCount(jsonObject.getInt("viewCount"))
                                nativeJobAndMissingData.setIsUse(jsonObject.getBoolean("useYn"))
                                nativeJobAndMissingData.setCreateDate(jsonObject.getString("createDate"))
                                if (!jsonObject.isNull("updateDate")) {
                                    nativeJobAndMissingData.setUpdateDate(jsonObject.getString("updateDate"))
                                }
                                nativeJobAndMissingData.setMemberUid(jsonObject.getInt("memberUID"))
                                nativeJobAndMissingData.setMemberName(jsonObject.getString("memberNName"))
                                nativeJobAndMissingData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                nativeJobAndMissingData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                                nativeJobAndMissingData.setCategoryName(jsonObject.getString("cagoName"))
                                nativeJobAndMissingData.setParentCategoryName(jsonObject.getString("parentCagoName"))
                                nativeJobAndMissingData.setRecommendCount(jsonObject.getInt("recommCount"))
                                nativeJobAndMissingData.setBookMarkCount(jsonObject.getInt("bookmarkCount"))
                                nativeJobAndMissingData.setCommentCount(jsonObject.getInt("commentCount"))
                                nativeJobAndMissingData.setIsBookMark(jsonObject.getBoolean("bookmarkYn"))
                                nativeJobAndMissingData.setIsRecommend(jsonObject.getBoolean("recommYn"))
                                nativeJobAndMissingData.setIsPopularity(jsonObject.getBoolean("popularityYn"))
                                nativeJobAndMissingData.setIsStandBy(jsonObject.getBoolean("standByYn"))

                                val jsonBoardImageArray = jsonObject.getJSONArray("boardImg")
                                val nativeBoardImageListData = ArrayList<NativeBoardImageData>()
                                for (k in 0 until jsonBoardImageArray.length()) {
                                    val nativeBoardImageData = NativeBoardImageData()
                                    val jsonBoardImageObject = jsonBoardImageArray.getJSONObject(k)
                                    if (null != jsonBoardImageObject) {
                                        nativeBoardImageData.setBoardImageUid(jsonBoardImageObject.getInt("boardImageUID"))
                                        nativeBoardImageData.setBoardImageSort(jsonBoardImageObject.getInt("boardImageSort"))
                                        nativeBoardImageData.setBoardImageOrigin(jsonBoardImageObject.getString("boardImageOrigin"))
                                        nativeBoardImageData.setBoardImageName(jsonBoardImageObject.getString("boardImageName"))
                                        nativeBoardImageData.setIsUseYN(jsonBoardImageObject.getBoolean("useYn"))
                                        nativeBoardImageData.setCreateDate(jsonBoardImageObject.getString("createDate"))
                                        if (!jsonBoardImageObject.isNull("updateDate")) {
                                            nativeBoardImageData.setUpdateDate(jsonBoardImageObject.getString("updateDate"))
                                        }
                                        nativeBoardImageData.setBoardUid(jsonBoardImageObject.getInt("boardUID"))
                                    }

                                    nativeBoardImageListData.add(nativeBoardImageData)
                                }
                                nativeJobAndMissingData.setBoardImageList(nativeBoardImageListData)

                                val jsonBoardFileArray = jsonObject.getJSONArray("boardFile")
                                val nativeBoardFileListData = ArrayList<NativeBoardFileData>()
                                for (k in 0 until jsonBoardFileArray.length()) {
                                    val nativeBoardFileData = NativeBoardFileData()
                                    val jsonBoardFileObject = jsonBoardFileArray.getJSONObject(k)
                                    if (null != jsonBoardFileObject) {
                                        nativeBoardFileData.setBoardFileUid(jsonBoardFileObject.getInt("boardFileUID"))
                                        nativeBoardFileData.setBoardFileSort(jsonBoardFileObject.getInt("boardFileSort"))
                                        nativeBoardFileData.setBoardFileOrigin(jsonBoardFileObject.getString("boardFileOrigin"))
                                        nativeBoardFileData.setBoardFileName(jsonBoardFileObject.getString("boardFileName"))
                                        nativeBoardFileData.setIsUseYN(jsonBoardFileObject.getBoolean("useYn"))
                                        nativeBoardFileData.setCreateDate(jsonBoardFileObject.getString("createDate"))
                                        if (!jsonBoardFileObject.isNull("updateDate")) {
                                            nativeBoardFileData.setUpdateDate(jsonBoardFileObject.getString("updateDate"))
                                        }

                                        nativeBoardFileData.setBoardUid(jsonBoardFileObject.getInt("boardUID"))
                                    }

                                    nativeBoardFileListData.add(nativeBoardFileData)
                                }
                                nativeJobAndMissingData.setBoardFileList(nativeBoardFileListData)

                                // Job And Missing
                                nativeJobAndMissingData.setLocationUid(jsonObject.getInt("locationUID"))
                                nativeJobAndMissingData.setLocationStateUid(jsonObject.getInt("locationStateUID"))
                                nativeJobAndMissingData.setLocationState(jsonObject.getString("locationState"))
                                nativeJobAndMissingData.setLocationGuGunUid(jsonObject.getInt("locationGugunUID"))
                                nativeJobAndMissingData.setLocationGuGun(jsonObject.getString("locationGugun"))
                                nativeJobAndMissingData.setLocationDesc(jsonObject.getString("locationDesc"))

                                if ((type == 0) || (type == 1)) {
                                    // Only Job
                                    nativeJobAndMissingData.setSnsDesc(jsonObject.getString("snsDesc"))
                                    nativeJobAndMissingData.setBeginDate(jsonObject.getString("beginDate"))
                                    nativeJobAndMissingData.setEndDate(jsonObject.getString("endDate"))
                                } else {
                                    // Only Missing
                                    nativeJobAndMissingData.setMissingDate(jsonObject.getString("missingDate"))
                                }

                                nativeJobAndMissingListData.add(nativeJobAndMissingData)
                            }

                        }

                        listener?.onSuccess(nativeJobAndMissingListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener!!.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener!!.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message = "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                var valueType = ""
                var mode = ""
                when (type) {
                    0 -> {
                        valueType = "40"
                        mode = "job"
                    }

                    1 -> {
                        valueType = "41"
                        mode = "job"
                    }

                    2 -> {
                        valueType = "46"
                        mode = "missing"
                    }

                    3 -> {
                        valueType = "47"
                        mode = "missing"
                    }

                    4 -> {
                        valueType = "48"
                    }
                }
                params["cago"] = valueType
                params["mode"] = mode
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Login
     * POST
     */
    fun getLogin(requestQueue: RequestQueue, listener: RequestListener<UserData>,
                 email: String, password: String) {

        NagajaLog().d("wooks, API ========= getLogin : email = $email / password : $password")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + LOGIN,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= LOGIN : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val userData = UserData()
                        userData.setMemberUid(jsonObject.getInt("memberUID"))
                        userData.setMemberGrant(jsonObject.getInt("memGrant"))
                        userData.setMemberStatus(jsonObject.getInt("memStatus"))
                        userData.setMemberType(jsonObject.getInt("memType"))
                        userData.setMemberReferral( if (!jsonObject.isNull("memReferral")) jsonObject.getString("memReferral") else "" )
                        userData.setMemberId( if (!jsonObject.isNull("memberID")) jsonObject.getString("memberID") else "" )
                        userData.setName( if (!jsonObject.isNull("memName")) jsonObject.getString("memName") else "" )
                        userData.setNickName( if (!jsonObject.isNull("memNName")) jsonObject.getString("memNName") else "" )
                        userData.setEmailId( if (!jsonObject.isNull("memEmailID")) jsonObject.getString("memEmailID") else "" )
                        userData.setNationPhone( if (!jsonObject.isNull("memNationPhone")) jsonObject.getString("memNationPhone") else "" )
                        userData.setPhoneNumber( if (!jsonObject.isNull("memPhone")) jsonObject.getString("memPhone") else "" )
                        userData.setNationUid(jsonObject.getInt("nationUID"))
                        userData.setSocialUid(jsonObject.getInt("socialUID"))
                        userData.setSocialSecureKey( if (!jsonObject.isNull("socialSecureKey")) jsonObject.getString("socialSecureKey") else "" )
                        userData.setSecureToken( if (!jsonObject.isNull("secureToken")) jsonObject.getString("secureToken") else "")
                        userData.setSecureKey( if (!jsonObject.isNull("secureKey")) jsonObject.getString("secureKey") else "" )
                        userData.setRefreshKey( if (!jsonObject.isNull("refreshKey")) jsonObject.getString("refreshKey") else "" )
                        userData.setIsEmailAuth(jsonObject.getBoolean("confirmEmailIDYn"))
                        userData.setIsPhoneAuth(jsonObject.getBoolean("confirmPhoneYn"))
                        userData.setIsNameAuth(jsonObject.getBoolean("confirmUserNameYn"))
                        userData.setLoginDate( if (!jsonObject.isNull("loginDate")) jsonObject.getString("loginDate") else "")
                        userData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "")
                        userData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "")
                        userData.setLocationUid(jsonObject.getInt("memLocationUID"))
                        userData.setLatitude(jsonObject.getString("memLatitude"))
                        userData.setLongitude(jsonObject.getString("memLongitude"))
                        userData.setAddressCode( if (!jsonObject.isNull("memAddressZipCode")) jsonObject.getString("memAddressZipCode") else "" )
                        userData.setAddress( if (!jsonObject.isNull("memAddress")) jsonObject.getString("memAddress") else "" )
                        userData.setAddressDetail( if (!jsonObject.isNull("memAddressDetail")) jsonObject.getString("memAddressDetail") else "" )
                        userData.setProfileUrl( if (!jsonObject.isNull("memberImageOrigin")) jsonObject.getString("memberImageOrigin") else "" )
                        userData.setProfileName( if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "")
                        userData.setMemberLocationUid(jsonObject.getInt("memLocationUID"))

                        listener?.onSuccess(userData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String>? {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "aaaaaaaaaaaaaaaaaaaaaaaa"
//                return params
//            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["emailid"] = email
                params["password"] = password
                return params
            }

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Secure Key Login
     * POST
     * @param secureKey
     */
    fun getSecureKeyLogin(requestQueue: RequestQueue, listener: RequestListener<UserData>,
                          secureKey: String) {

        NagajaLog().d("wooks, API ========= getSecureKeyLogin : secureKey = $secureKey")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + SECURE_KEY_LOGIN,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= SECURE_KEY_LOGIN : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val userData = UserData()
                        userData.setMemberUid(jsonObject.getInt("memberUID"))
                        userData.setMemberGrant(jsonObject.getInt("memGrant"))
                        userData.setMemberStatus(jsonObject.getInt("memStatus"))
                        userData.setMemberType(jsonObject.getInt("memType"))
                        userData.setMemberReferral( if (!jsonObject.isNull("memReferral")) jsonObject.getString("memReferral") else "" )
                        userData.setMemberId( if (!jsonObject.isNull("memberID")) jsonObject.getString("memberID") else "" )
                        userData.setName( if (!jsonObject.isNull("memName")) jsonObject.getString("memName") else "" )
                        userData.setNickName( if (!jsonObject.isNull("memNName")) jsonObject.getString("memNName") else "" )
                        userData.setEmailId( if (!jsonObject.isNull("memEmailID")) jsonObject.getString("memEmailID") else "" )
                        userData.setNationPhone( if (!jsonObject.isNull("memNationPhone")) jsonObject.getString("memNationPhone") else "" )
                        userData.setPhoneNumber( if (!jsonObject.isNull("memPhone")) jsonObject.getString("memPhone") else "" )
                        userData.setNationUid(jsonObject.getInt("nationUID"))
                        userData.setSocialUid(jsonObject.getInt("socialUID"))
                        userData.setSocialSecureKey( if (!jsonObject.isNull("socialSecureKey")) jsonObject.getString("socialSecureKey") else "" )
                        userData.setSecureToken( if (!jsonObject.isNull("secureToken")) jsonObject.getString("secureToken") else "")
                        userData.setSecureKey( if (!jsonObject.isNull("secureKey")) jsonObject.getString("secureKey") else "" )
                        userData.setRefreshKey( if (!jsonObject.isNull("refreshKey")) jsonObject.getString("refreshKey") else "" )
                        userData.setIsEmailAuth(jsonObject.getBoolean("confirmEmailIDYn"))
                        userData.setIsPhoneAuth(jsonObject.getBoolean("confirmPhoneYn"))
                        userData.setIsNameAuth(jsonObject.getBoolean("confirmUserNameYn"))
                        userData.setLoginDate( if (!jsonObject.isNull("loginDate")) jsonObject.getString("loginDate") else "")
                        userData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "")
                        userData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "")
                        userData.setLocationUid(jsonObject.getInt("memLocationUID"))
                        userData.setLatitude(jsonObject.getString("memLatitude"))
                        userData.setLongitude(jsonObject.getString("memLongitude"))
                        userData.setAddressCode( if (!jsonObject.isNull("memAddressZipCode")) jsonObject.getString("memAddressZipCode") else "" )
                        userData.setAddress( if (!jsonObject.isNull("memAddress")) jsonObject.getString("memAddress") else "" )
                        userData.setAddressDetail( if (!jsonObject.isNull("memAddressDetail")) jsonObject.getString("memAddressDetail") else "" )
                        userData.setProfileUrl( if (!jsonObject.isNull("memberImageOrigin")) jsonObject.getString("memberImageOrigin") else "" )
                        userData.setProfileName( if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "")
                        userData.setMemberLocationUid(jsonObject.getInt("memLocationUID"))

                        listener.onSuccess(userData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN -> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_INVALID_TOKEN -> {
                                errorCode = ERROR_INVALID_TOKEN
                            }

                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_DO_NOT_EXIST_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Request Phone Auth Number
     * POST
     */
    fun getRequestPhoneAuthCode(requestQueue: RequestQueue, listener: RequestListener<String>,
                                nationPhoneCode: String, phoneNumber: String) {

        NagajaLog().d("wooks, API ========= getRequestPhoneAuthNumber : nationPhoneCode = $nationPhoneCode / phoneNumber = $phoneNumber")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + PHONE_AUTH_CODE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= PHONE_AUTH_CODE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener?.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_EXISTING_PHONE_NUMBER -> {
                                errorCode = ERROR_CODE_EXISTING_PHONE_NUMBER
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("nationphone", nationPhoneCode)
                jsonBody.put("phone", phoneNumber)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Request Phone Auth Check Code
     * POST
     * @param nationPhoneCode
     * @param phoneNumber
     * @param code
     */
    fun getRequestPhoneAuthCheckCode(requestQueue: RequestQueue, listener: RequestListener<PhoneAuthData>,
                                     nationPhoneCode: String, phoneNumber: String, code: String) {

        NagajaLog().d("wooks, API ========= getRequestPhoneAuthCheckCode : nationPhoneCode = $nationPhoneCode / phoneNumber = $phoneNumber / code: $code")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + PHONE_AUTH_CHECK_CODE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= PHONE_AUTH_CHECK_CODE : $response")


                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val phoneAuthData = PhoneAuthData()
                        phoneAuthData.setConfirmUid(jsonObject.getInt("confirmUID"))
                        phoneAuthData.setMemberUid(jsonObject.getInt("memberUID"))
                        phoneAuthData.setMemberId( if (!jsonObject.isNull("memberID")) jsonObject.getString("memberID") else "" )
                        phoneAuthData.setPhoneNumber( if (!jsonObject.isNull("phoneNumber")) jsonObject.getString("phoneNumber") else "" )
                        phoneAuthData.setPhoneNationCode( if (!jsonObject.isNull("phoneNationCode")) jsonObject.getString("phoneNationCode") else "" )
                        phoneAuthData.setSecureCode( if (!jsonObject.isNull("secureCode")) jsonObject.getString("secureCode") else "" )
                        phoneAuthData.setConfirmLimitDate( if (!jsonObject.isNull("confirmLimitDate")) jsonObject.getString("confirmLimitDate") else "" )
                        phoneAuthData.setUserLimitDate( if (!jsonObject.isNull("useLimitDate")) jsonObject.getString("useLimitDate") else "" )
                        phoneAuthData.setConfirmTypeUid(jsonObject.getInt("confirmtypeuid"))

                        listener.onSuccess(phoneAuthData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("nationphone", nationPhoneCode)
                jsonBody.put("phone", phoneNumber)
                jsonBody.put("code", code)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Request MemberPhone Auth Number
     * POST
     */
    fun getRequestMemberPhoneAuthCode(requestQueue: RequestQueue, listener: RequestListener<String>,
                                      nationPhoneCode: String, phoneNumber: String) {

        NagajaLog().d("wooks, API ========= getRequestMemberPhoneAuthCode : nationPhoneCode = $nationPhoneCode / phoneNumber = $phoneNumber")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + MEMBER_PHONE_AUTH_CODE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= MEMBER_PHONE_AUTH_CODE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener?.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_NOT_FOUND_USER -> {
                                errorCode = ERROR_CODE_NOT_FOUND_USER
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("nationphone", nationPhoneCode)
                jsonBody.put("phone", phoneNumber)
                jsonBody.put("confirmtypeuid", 1)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Request Member Phone Auth Check Code
     * POST
     * @param nationPhoneCode
     * @param phoneNumber
     * @param code
     */
    fun getRequestMemberPhoneAuthCheckCode(requestQueue: RequestQueue, listener: RequestListener<PhoneAuthData>,
                                           nationPhoneCode: String, phoneNumber: String, code: String) {

        NagajaLog().d("wooks, API ========= getRequestMemberPhoneAuthCheckCode : nationPhoneCode = $nationPhoneCode / phoneNumber = $phoneNumber / code: $code")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + MEMBER_PHONE_AUTH_CHECK_CODE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= MEMBER_PHONE_AUTH_CHECK_CODE : $response")


                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val phoneAuthData = PhoneAuthData()
                        phoneAuthData.setConfirmUid(jsonObject.getInt("confirmUID"))
                        phoneAuthData.setMemberUid(jsonObject.getInt("memberUID"))
                        phoneAuthData.setMemberId( if (!jsonObject.isNull("memberID")) jsonObject.getString("memberID") else "" )
                        phoneAuthData.setPhoneNumber( if (!jsonObject.isNull("phoneNumber")) jsonObject.getString("phoneNumber") else "" )
                        phoneAuthData.setPhoneNationCode( if (!jsonObject.isNull("phoneNationCode")) jsonObject.getString("phoneNationCode") else "" )
                        phoneAuthData.setSecureCode( if (!jsonObject.isNull("secureCode")) jsonObject.getString("secureCode") else "" )
                        phoneAuthData.setConfirmLimitDate( if (!jsonObject.isNull("confirmLimitDate")) jsonObject.getString("confirmLimitDate") else "" )
                        phoneAuthData.setUserLimitDate( if (!jsonObject.isNull("useLimitDate")) jsonObject.getString("useLimitDate") else "" )
                        phoneAuthData.setConfirmTypeUid(jsonObject.getInt("confirmtypeuid"))

                        listener.onSuccess(phoneAuthData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("nationphone", nationPhoneCode)
                jsonBody.put("phone", phoneNumber)
                jsonBody.put("code", code)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Request Member Phone Auth Check Code
     * POST
     * @param nationPhoneCode
     * @param phoneNumber
     * @param code
     */
    fun getFindEmailSuccessDisconnect(requestQueue: RequestQueue, listener: RequestListener<String>,
                                      nationPhoneCode: String, phoneNumber: String, code: String) {

        NagajaLog().d("wooks, API ========= getFindEmailSuccessDisconnect : nationPhoneCode = $nationPhoneCode / phoneNumber = $phoneNumber / code: $code")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + MEMBER_FIND_EMAIL_DISCONNECT,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= MEMBER_FIND_EMAIL_DISCONNECT : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("nationphone", nationPhoneCode)
                jsonBody.put("phone", phoneNumber)
                jsonBody.put("code", code)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Exist Email Check
     * POST
     * @param email
     */
    fun getExistEmailCheck(requestQueue: RequestQueue, listener: RequestListener<String>,
                           email: String) {

        NagajaLog().d("wooks, API ========= getExistEmailCheck : email = $email")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + EXIST_EMAIL_CHECK,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= EXIST_EMAIL_CHECK : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val isUser = json.getString("message") == "true"
                        if (isUser) {
                            val jsonObject: JSONObject = json.getJSONObject("data")
                            val password = jsonObject.getString("memberPassword")
                            listener?.onSuccess(password)
                        } else {
                            listener?.onSuccess("false")
                        }
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["emailID"] = email
                return params
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Location Name
     * POST
     * @param language
     * @param location
     */
    fun getLocationName(requestQueue: RequestQueue, listener: RequestListener<String>,
                        language: String, location:String) {

        NagajaLog().d("wooks, API ========= getLocationName : language = $language / location = $location")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + LOCATION_NAME,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= LOCATION_NAME : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val locationName = json.getString("data")
                        if (!TextUtils.isEmpty(locationName)) {
                            listener.onSuccess(locationName)
                        }
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["language"] = language
                params["location"] = location
                return params
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Type
     * GET
     * @param nationPhoneCode
     */
    fun getApplicationCompanyMemberType(requestQueue: RequestQueue, listener: RequestListener<ArrayList<CompanyTypeData>>,
                                        nationPhoneCode: String) {

        NagajaLog().d("wooks, API ========= getApplicationCompanyMemberType : nationPhoneCode = $nationPhoneCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$APPLICATION_COMPANY_MEMBER_TYPE?uid=3&nuid=$nationPhoneCode",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= APPLICATION_COMPANY_MEMBER_TYPE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val companyTypeListData = ArrayList<CompanyTypeData>()

                        for (i in 0 until jsonArray.length()) {
                            val companyTypeData = CompanyTypeData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                companyTypeData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                companyTypeData.setCategoryDepth(jsonObject.getInt("cagoDepth"))
                                companyTypeData.setCategorySort(jsonObject.getInt("cagoSort"))
                                companyTypeData.setCategoryName(jsonObject.getString("cagoName"))
                                companyTypeData.setCategoryNameEnglish(jsonObject.getString("cagoNameEng"))
                                companyTypeData.setCategoryUri(jsonObject.getString("cagoUri"))
                                companyTypeData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                companyTypeData.setCreateDate(jsonObject.getString("createDate"))
                                companyTypeData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))

                                companyTypeListData.add(companyTypeData)
                            }
                        }

                        listener.onSuccess(companyTypeListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }





    // ==========================================================================================
    // 이하 Get
    // ==========================================================================================










    /**
     * Get Company Member
     * GET
     * @param nationPhoneCode
     * @param secureKey
     */
    fun getCompanyMember(requestQueue: RequestQueue, listener: RequestListener<ArrayList<CompanyMemberData>>,
                         nationPhoneCode: String, secureKey: String) {

        NagajaLog().d("wooks, API ========= getCompanyMember : nationPhoneCode = $nationPhoneCode / secureKey = $secureKey")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_MEMBER?nuid=$nationPhoneCode",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_MEMBER : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val companyMemberListData = ArrayList<CompanyMemberData>()

                        for (i in 0 until jsonArray.length()) {
                            val companyMemberData = CompanyMemberData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                companyMemberData.setCompanyManagerUid(jsonObject.getInt("compManagerUID"))
                                companyMemberData.setCompanyManagerGrant(jsonObject.getInt("compManagerGrant"))
                                companyMemberData.setMemberUid(jsonObject.getInt("memberUID"))
                                companyMemberData.setCompanyUid(jsonObject.getInt("compUID"))
                                companyMemberData.setMemberId(jsonObject.getInt("memberID"))
                                companyMemberData.setCompanyName(jsonObject.getString("compName"))
                                companyMemberData.setCompanyNameEnglish(jsonObject.getString("compNameEng"))
                                companyMemberData.setCreateDate(jsonObject.getString("createDate"))
                                companyMemberData.setUpdateDate(jsonObject.getString("updateDate"))
                                companyMemberData.setChatCount(jsonObject.getInt("chatCount"))
                                companyMemberData.setNoteCount(jsonObject.getInt("noteCount"))
                                companyMemberData.setReservationCount(jsonObject.getInt("reservateCount"))
                                companyMemberData.setRegularCount(jsonObject.getInt("regularCount"))
                                companyMemberData.setCompanyStatus(jsonObject.getInt("compStatus"))

                                companyMemberListData.add(companyMemberData)
                            }
                        }

                        listener.onSuccess(companyMemberListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Default Data
     * GET
     * @param secureKey
     * @param companyUid
     */
    fun getCompanyDefaultData(requestQueue: RequestQueue, listener: RequestListener<CompanyDefaultData>,
                              secureKey: String, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyDefaultData : secureKey = $secureKey / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_DEFAULT/$companyUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_DEFAULT : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val companyDefaultData = CompanyDefaultData()
                        companyDefaultData.setCompanyUid(jsonObject.getInt("compUID"))
                        companyDefaultData.setCategoryUid(jsonObject.getInt("cagoUID"))
                        companyDefaultData.setCategoryName(jsonObject.getString("cagoName"))
                        companyDefaultData.setCompanyNameEnglish(jsonObject.getString("compNameEng"))
                        companyDefaultData.setCompanyName(jsonObject.getString("compName"))
                        companyDefaultData.setManageName(jsonObject.getString("compManagerName"))
                        companyDefaultData.setCompanyAddress(jsonObject.getString("compAddress"))
                        companyDefaultData.setCompanyAddressDetail(jsonObject.getString("compAddressDetail"))
                        companyDefaultData.setCompanyMainImageUrl(jsonObject.getString("compMainImage"))
                        companyDefaultData.setCompanyMainOrigin(jsonObject.getString("compMainImageOrigin"))
                        companyDefaultData.setCompanyServiceUid(jsonObject.getInt("compServiceUID"))
                        companyDefaultData.setServiceStatus(jsonObject.getString("serviceStatus"))

                        listener.onSuccess(companyDefaultData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Manager Data
     * GET
     * @param secureKey
     * @param companyUid
     */
    fun getCompanyManagerData(requestQueue: RequestQueue, listener: RequestListener<ArrayList<CompanyManagerData>>,
                              secureKey: String, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyDefaultData : secureKey = $secureKey / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_MANAGER/$companyUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_MANAGER : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val companyManagerListData = ArrayList<CompanyManagerData>()

                        for (i in 0 until jsonArray.length()) {
                            val companyManagerData = CompanyManagerData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                companyManagerData.setCompanyManagerUid(jsonObject.getInt("compManagerUID"))
                                companyManagerData.setCompanyManagerGrant(jsonObject.getInt("compManagerGrant"))
                                companyManagerData.setMemberUid(jsonObject.getInt("memberUID"))
                                companyManagerData.setCompanyUid(jsonObject.getInt("compUID"))
                                companyManagerData.setMemberName(jsonObject.getString("memName"))
                                companyManagerData.setCreateDate(jsonObject.getString("createDate"))
                                companyManagerData.setUpdateDate(jsonObject.getString("updateDate"))

                                companyManagerListData.add(companyManagerData)
                            }
                        }

                        listener.onSuccess(companyManagerListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Notification Count
     * GET
     * @param secureKey
     * @param companyUid
     */
    fun getCompanyNotificationCount(requestQueue: RequestQueue, listener: RequestListener<CompanyNotificationCountData>,
                                    secureKey: String, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyNotificationCount : secureKey = $secureKey / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_NOTIFICATION_COUNT/$companyUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_NOTIFICATION_COUNT : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val companyNotificationCountData = CompanyNotificationCountData()

                        companyNotificationCountData.setCompanyUid(jsonObject.getInt("compUID"))
                        companyNotificationCountData.setNoteCount(jsonObject.getInt("noteCount"))
                        companyNotificationCountData.setRegularCount(jsonObject.getInt("regularCount"))
                        companyNotificationCountData.setReservationCount(jsonObject.getInt("reservateCount"))
                        companyNotificationCountData.setDeclareCount(jsonObject.getInt("declareCount"))

                        listener.onSuccess(companyNotificationCountData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Change Service Status
     * PUT
     * @param secureKey
     * @param companyServiceUid
     * @param serviceStatus
     * @param companyUid
     */
    fun getCompanyChangeServiceStatus(requestQueue: RequestQueue, listener: RequestListener<String>,
                                      secureKey: String, companyServiceUid: Int, serviceStatus: Int, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyChangeServiceStatus : secureKey = $secureKey / companyServiceUid = $companyServiceUid / serviceStatus = $serviceStatus / companyUid: $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.PUT,
            API_DOMAIN + COMPANY_CHANGE_SERVICE_STATUS,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_CHANGE_SERVICE_STATUE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("compserviceuid", companyServiceUid)
                jsonBody.put("servicestatus", serviceStatus)
                jsonBody.put("compuid", companyUid)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get company Product Item Data
     * GET
     * @param nationPhoneCode
     * @param secureKey
     */
    fun getCompanyProductItemData(requestQueue: RequestQueue, listener: RequestListener<ArrayList<CompanyProductData>>,
                                  secureKey: String, companyUid: String, categoryUid: String) {

        NagajaLog().d("wooks, API ========= getCompanyProductItemData : secureKey = $secureKey / companyUid = $companyUid / categoryUid = $categoryUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_PRODUCT_ITEM/$companyUid?cago=$categoryUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_PRODUCT_ITEM : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val companyProductDataList = ArrayList<CompanyProductData>()

                        for (i in 0 until jsonArray.length()) {
                            val jsonItemObject = jsonArray.getJSONObject(i)
                            val jsonItemArray = jsonItemObject.getJSONArray("itemList")
                            for (value in 0 until jsonItemArray.length()) {
                                val companyProductData = CompanyProductData()
                                val jsonObject = jsonItemArray.getJSONObject(value)
                                if (null != jsonObject) {
                                    companyProductData.setItemUid(jsonObject.getInt("itemUID"))
                                    companyProductData.setItemStatus(jsonObject.getInt("itemStatus"))
                                    companyProductData.setItemName( if (!jsonObject.isNull("itemName")) jsonObject.getString("itemName") else "" )
                                    companyProductData.setItemContent( if (!jsonObject.isNull("itemContent")) jsonObject.getString("itemContent") else "" )
                                    companyProductData.setItemCurrencyCode( if (!jsonObject.isNull("itemCurrencyCode")) jsonObject.getString("itemCurrencyCode") else "" )
                                    companyProductData.setItemPrice( if (!jsonObject.isNull("itemPrice")) jsonObject.getString("itemPrice") else "" )
                                    companyProductData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                    companyProductData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                    companyProductData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                    companyProductData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                    companyProductData.setItemCategoryUid(jsonObject.getInt("itemCagoUID"))
                                    companyProductData.setCompanyUid(jsonObject.getInt("compUID"))
                                    companyProductData.setMemberUid(jsonObject.getInt("memberUID"))

                                    val jsonImageArray = jsonObject.getJSONArray("imageList")
                                    val companyProductImageDataList = ArrayList<CompanyProductImageData>()
                                    for (k in 0 until jsonImageArray.length()) {
                                        val companyProductImageData = CompanyProductImageData()
                                        val jsonImageObject = jsonImageArray.getJSONObject(k)
                                        if (null != jsonImageObject) {
                                            companyProductImageData.setItemImageUid(jsonImageObject.getInt("itemImageUID"))
                                            companyProductImageData.setItemImageSort(jsonImageObject.getInt("itemImageSort"))

                                            companyProductImageData.setItemImageOrigin( if (!jsonImageObject.isNull("itemImageOrigin")) jsonImageObject.getString("itemImageOrigin") else "" )
                                            companyProductImageData.setItemImageName( if (!jsonImageObject.isNull("itemImageName")) jsonImageObject.getString("itemImageName") else "" )

                                            companyProductImageData.setIsUseYn(jsonImageObject.getBoolean("useYn"))

                                            companyProductImageData.setCreateDate( if (!jsonImageObject.isNull("createDate")) jsonImageObject.getString("createDate") else "" )
                                            companyProductImageData.setUpdateDate( if (!jsonImageObject.isNull("updateDate")) jsonImageObject.getString("updateDate") else "" )

                                            companyProductImageData.setItemUid(jsonImageObject.getInt("itemUID"))
                                        }

                                        companyProductImageDataList.add(companyProductImageData)
                                    }
                                    companyProductData.setImageListData(companyProductImageDataList)

                                    val jsonCurrencyArray = jsonObject.getJSONArray("currencyList")
                                    val companyProductCurrencyDataList = ArrayList<CompanyProductCurrencyData>()
                                    for (j in 0 until jsonCurrencyArray.length()) {
                                        val companyProductCurrencyData = CompanyProductCurrencyData()
                                        val jsonCurrencyObject = jsonCurrencyArray.getJSONObject(j)
                                        if (null != jsonCurrencyObject) {
                                            companyProductCurrencyData.setItemUid(jsonCurrencyObject.getInt("itemUID"))
                                            companyProductCurrencyData.setCurrencyCode( if (!jsonCurrencyObject.isNull("currencyCode")) jsonCurrencyObject.getString("currencyCode") else "" )
                                            companyProductCurrencyData.setCurrencyPrice( if (!jsonCurrencyObject.isNull("currencyPrice")) jsonCurrencyObject.getDouble("currencyPrice") else 0.0 )
                                            companyProductCurrencyData.setCreateDate( if (!jsonCurrencyObject.isNull("createDate")) jsonCurrencyObject.getString("createDate") else "" )
                                            companyProductCurrencyData.setUpdateDate( if (!jsonCurrencyObject.isNull("updateDate")) jsonCurrencyObject.getString("updateDate") else "" )
                                        }

                                        companyProductCurrencyDataList.add(companyProductCurrencyData)
                                    }

                                    companyProductData.setCurrencyListData(companyProductCurrencyDataList)
                                }

                                companyProductDataList.add(companyProductData)
                            }
                        }

                        listener.onSuccess(companyProductDataList)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get company Information
     * GET
     * @param secureKey
     * @param companyUid
     */
    fun getCompanyInformation(requestQueue: RequestQueue, listener: RequestListener<CompanyInformationData>,
                              secureKey: String, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyInformation : secureKey = $secureKey / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_INFORMATION/$companyUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_INFORMATION : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val companyInformationData = CompanyInformationData()

                        companyInformationData.setCompanyUid(jsonObject.getInt("compUID"))
                        companyInformationData.setCompanyStatus(jsonObject.getInt("compStatus"))
                        companyInformationData.setCompanyNameEnglish( if (!jsonObject.isNull("compNameEng")) jsonObject.getString("compNameEng") else "" )
                        companyInformationData.setCompanyNationPhone( if (!jsonObject.isNull("setCompanyNationPhone")) jsonObject.getString("compNameEng") else "" )
                        companyInformationData.setCompanyEmail( if (!jsonObject.isNull("compEmail")) jsonObject.getString("compEmail") else "" )
                        companyInformationData.setCompanyPhone( if (!jsonObject.isNull("compPhone")) jsonObject.getString("compPhone") else "" )
                        companyInformationData.setCompanyLatitude(jsonObject.getDouble("compLatitude"))
                        companyInformationData.setCompanyLongitude(jsonObject.getDouble("compLongitude"))
                        companyInformationData.setCompanyAddress( if (!jsonObject.isNull("compAddress")) jsonObject.getString("compAddress") else "" )
                        companyInformationData.setCompanyAddressDetail( if (!jsonObject.isNull("compAddressDetail")) jsonObject.getString("compAddressDetail") else "" )
                        companyInformationData.setCompanyLicenseImageOrigin( if (!jsonObject.isNull("compLicenseImageOrigin")) jsonObject.getString("compLicenseImageOrigin") else "" )
                        companyInformationData.setCompanyLicenseImageUrl( if (!jsonObject.isNull("compLicenseImage")) jsonObject.getString("compLicenseImage") else "" )
                        companyInformationData.setCompanyLicenseName( if (!jsonObject.isNull("compLicenseName")) jsonObject.getString("compLicenseName") else "" )
                        companyInformationData.setCompanyLicenseMasterName( if (!jsonObject.isNull("compLicenseMasterName")) jsonObject.getString("compLicenseMasterName") else "" )
                        companyInformationData.setCompanyLicenseNumber( if (!jsonObject.isNull("compLicenseNo")) jsonObject.getString("compLicenseNo") else "" )
                        companyInformationData.setCompanyDescription( if (!jsonObject.isNull("compDesc")) jsonObject.getString("compDesc") else "" )
                        companyInformationData.setCompanyFacebookUrl( if (!jsonObject.isNull("compFacebookUrl")) jsonObject.getString("compFacebookUrl") else "" )
                        companyInformationData.setCompanyKakaoUrl( if (!jsonObject.isNull("compKakaoUrl")) jsonObject.getString("compKakaoUrl") else "" )
                        companyInformationData.setCompanyLineUrl( if (!jsonObject.isNull("compLineUrl")) jsonObject.getString("compLineUrl") else "" )
                        companyInformationData.setCompanyManagerLimit(jsonObject.getInt("compManagerlimit"))
                        companyInformationData.setCompanyManagerName( if (!jsonObject.isNull("compManagerName")) jsonObject.getString("compManagerName") else "" )
                        companyInformationData.setCategoryUid(jsonObject.getInt("cagoUID"))
                        companyInformationData.setNationUid(jsonObject.getInt("nationUID"))
                        companyInformationData.setMemberUid(jsonObject.getInt("memberUID"))
                        companyInformationData.setIsCompanyPublic(jsonObject.getBoolean("compPublicYn"))
                        companyInformationData.setIsCompanyCertification(jsonObject.getBoolean("compCertificationYn"))
                        companyInformationData.setIsUseYn(jsonObject.getBoolean("useYn"))
                        companyInformationData.setCompanyPayment( if (!jsonObject.isNull("compPayment")) jsonObject.getString("compPayment") else "" )

                        val nameJsonArray = jsonObject.getJSONArray("compNameList")
                        val companyNameListData = ArrayList<CompanyNameData>()
                        for (i in 0 until nameJsonArray.length()) {
                            val companyNameData = CompanyNameData()
                            val nameJsonObject = nameJsonArray.getJSONObject(i)
                            if (null != nameJsonObject) {
                                companyNameData.setCompanyNameUid(nameJsonObject.getInt("compNameUID"))
                                companyNameData.setCompanyUid(nameJsonObject.getInt("compUID"))
                                companyNameData.setNationUid(nameJsonObject.getInt("nationUID"))
                                companyNameData.setCompanyName( if (!nameJsonObject.isNull("compName")) nameJsonObject.getString("compName") else "" )
                                companyNameData.setIsUseYn(nameJsonObject.getBoolean("useYn"))
                                companyNameData.setCreateDate( if (!nameJsonObject.isNull("createDate")) nameJsonObject.getString("createDate") else "" )
                                companyNameData.setUpdateDate( if (!nameJsonObject.isNull("updateDate")) nameJsonObject.getString("updateDate") else "" )
                            }
                            companyNameListData.add(companyNameData)
                        }
                        companyInformationData.setCompanyNameListData(companyNameListData)

                        val imageJsonArray = jsonObject.getJSONArray("compImageList")
                        val companyInformationImageListData = ArrayList<CompanyInformationImageData>()
                        for (k in 0 until imageJsonArray.length()) {
                            val companyInformationImageData = CompanyInformationImageData()
                            val imageJsonObject = imageJsonArray.getJSONObject(k)
                            if (null != imageJsonObject) {
                                companyInformationImageData.setCompanyImageUid(imageJsonObject.getInt("compImageUID"))
                                companyInformationImageData.setCompanyUid(imageJsonObject.getInt("compUID"))
                                companyInformationImageData.setCompanyImageSort(imageJsonObject.getInt("compUID"))
                                companyInformationImageData.setCompanyImageOrigin( if (!imageJsonObject.isNull("compImageOrigin")) imageJsonObject.getString("compImageOrigin") else "" )
                                companyInformationImageData.setCompanyImageUrl( if (!imageJsonObject.isNull("compImageName")) imageJsonObject.getString("compImageName") else "" )
                                companyInformationImageData.setIsUseYn(imageJsonObject.getBoolean("useYn"))
                                companyInformationImageData.setCreateDate( if (!imageJsonObject.isNull("createDate")) imageJsonObject.getString("createDate") else "" )
                                companyInformationImageData.setUpdateDate( if (!imageJsonObject.isNull("updateDate")) imageJsonObject.getString("updateDate") else "" )
                            }
                            companyInformationImageListData.add(companyInformationImageData)
                        }
                        companyInformationData.setCompanyImageListData(companyInformationImageListData)

                        listener.onSuccess(companyInformationData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get company Sales Default Information
     * GET
     * @param secureKey
     * @param companyUid
     */
    fun getCompanySalesDefaultInformation(requestQueue: RequestQueue, listener: RequestListener<CompanySalesData>,
                                          secureKey: String, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanySalesDefaultInformation : secureKey = $secureKey / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_SALES_DEFAULT_INFORMATION/$companyUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_SALES_DEFAULT_INFORMATION : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val companySalesData = CompanySalesData()

                        companySalesData.setCompanyUid(jsonObject.getInt("compUID"))
                        companySalesData.setCompanyServiceUid(jsonObject.getInt("compServiceUID"))
                        companySalesData.setServiceMonthOffDays( if (!jsonObject.isNull("serviceMonthOffDays")) jsonObject.getString("serviceMonthOffDays") else "" )
                        companySalesData.setServiceWeekOffDays( if (!jsonObject.isNull("serviceWeekOffDays")) jsonObject.getString("serviceWeekOffDays") else "" )
                        companySalesData.setServiceWeekDoubleOffDays( if (!jsonObject.isNull("serviceWeekDoubleOffDays")) jsonObject.getString("serviceWeekDoubleOffDays") else "" )
                        companySalesData.setServiceBeginTime( if (!jsonObject.isNull("serviceBeginTime")) jsonObject.getString("serviceBeginTime") else "" )
                        companySalesData.setServiceEndTime( if (!jsonObject.isNull("serviceBeginTime")) jsonObject.getString("serviceEndTime") else "" )
                        companySalesData.setFirstBreakBeginTime( if (!jsonObject.isNull("firstBreakBeginTime")) jsonObject.getString("firstBreakBeginTime") else "" )
                        companySalesData.setFirstBreakEndTime( if (!jsonObject.isNull("firstBreakEndTime")) jsonObject.getString("firstBreakEndTime") else "" )
                        companySalesData.setSecondBreakBeginTime( if (!jsonObject.isNull("secondBreakBeginTime")) jsonObject.getString("secondBreakBeginTime") else "" )
                        companySalesData.setSecondBreakEndTime( if (!jsonObject.isNull("secondBreakEndTime")) jsonObject.getString("secondBreakEndTime") else "" )
                        companySalesData.setFirstRestBeginTime( if (!jsonObject.isNull("firstRestBeginTime")) jsonObject.getString("firstRestBeginTime") else "" )
                        companySalesData.setFirstRestEndTime( if (!jsonObject.isNull("firstRestEndTime")) jsonObject.getString("firstRestEndTime") else "" )
                        companySalesData.setSecondRestBeginTime( if (!jsonObject.isNull("secondRestBeginTime")) jsonObject.getString("secondRestBeginTime") else "" )
                        companySalesData.setSecondRestEndTime( if (!jsonObject.isNull("secondRestEndTime")) jsonObject.getString("secondRestEndTime") else "" )
                        companySalesData.setDeliveryBeginTime( if (!jsonObject.isNull("deliveryBeginTime")) jsonObject.getString("deliveryBeginTime") else "" )
                        companySalesData.setDeliveryEndTime( if (!jsonObject.isNull("deliveryEndTime")) jsonObject.getString("deliveryEndTime") else "" )
                        companySalesData.setReservationBeginTime( if (!jsonObject.isNull("reservationBeginTime")) jsonObject.getString("reservationBeginTime") else "" )
                        companySalesData.setReservationEndTime( if (!jsonObject.isNull("reservationEndTime")) jsonObject.getString("reservationEndTime") else "" )
                        companySalesData.setReservationDayLimit(jsonObject.getInt("reservationDayLimit"))
                        companySalesData.setReservationPersonLimit(jsonObject.getInt("reservationPersonLimit"))
                        companySalesData.setReservationTeamLimit(jsonObject.getInt("reservationTeamLimit"))
                        companySalesData.setIsCompanyDeliveryUseYn(jsonObject.getBoolean("compDeliveryUseYn"))
                        companySalesData.setIsCompanyReservationUseYn(jsonObject.getBoolean("compReservationUseYn"))
                        companySalesData.setIsCompanyPickupUseYn(jsonObject.getBoolean("compPickupUseYn"))
                        companySalesData.setIsCompanyParkingUseYn(jsonObject.getBoolean("compParkingUseYn"))
                        companySalesData.setIsCompanyPetUseYn(jsonObject.getBoolean("compPetUseYn"))

                        val currencyJsonArray = jsonObject.getJSONArray("currencyList")
                        val companySalesCurrencyListData = ArrayList<CompanySalesCurrencyData>()
                        for (i in 0 until currencyJsonArray.length()) {
                            val companySalesCurrencyData = CompanySalesCurrencyData()
                            val currencyJsonObject = currencyJsonArray.getJSONObject(i)
                            if (null != currencyJsonObject) {
                                companySalesCurrencyData.setCompanyUid(currencyJsonObject.getInt("compUID"))
                                companySalesCurrencyData.setCurrencyUid(currencyJsonObject.getInt("currencyUID"))
                                companySalesCurrencyData.setIsCurrencyBase(currencyJsonObject.getBoolean("currencyBaseYn"))
                                companySalesCurrencyData.setIsUseYn(currencyJsonObject.getBoolean("useYn"))
                                companySalesCurrencyData.setCreateDate( if (!currencyJsonObject.isNull("createDate")) currencyJsonObject.getString("createDate") else "" )
                                companySalesCurrencyData.setUpdateDate( if (!currencyJsonObject.isNull("updateDate")) currencyJsonObject.getString("updateDate") else "" )
                            }
                            companySalesCurrencyListData.add(companySalesCurrencyData)
                        }
                        companySalesData.setCompanySalesCurrencyDataList(companySalesCurrencyListData)

                        listener.onSuccess(companySalesData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message = "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "bearer $secureKey"
                return params
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Change Add Features
     * PUT
     * @param secureKey
     * @param companyUid
     * @param companyServiceUid
     *
     * Sequence
     * Delivery, Reservation, Pickup, Parking, Pet
     */
    fun getCompanyChangeAddFeatures(requestQueue: RequestQueue, listener: RequestListener<String>,
                                    secureKey: String, companyUid: Int, companyServiceUid: Int,
                                    isDelivery: Boolean, isReservation: Boolean, isPickUp: Boolean, isParking: Boolean, isPet: Boolean) {

        NagajaLog().d("wooks, API ========= COMPANY_SALES_CHANGE_ADD_FEATURES : secureKey = $secureKey / companyUid: $companyUid / companyServiceUid = $companyServiceUid " +
                "/ isDelivery = $isDelivery / isReservation = $isReservation / isPickUp = $isPickUp / isParking = $isParking / isParking = $isParking")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.PUT,
            API_DOMAIN + COMPANY_SALES_CHANGE_ADD_FEATURES,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_SALES_CHANGE_ADD_FEATURES : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("compserviceuid", companyServiceUid)
                jsonBody.put("compuid", companyUid)
                jsonBody.put("compdeliveryuseyn", isDelivery)
                jsonBody.put("compreservationuseyn", isReservation)
                jsonBody.put("comppickupuseyn", isPickUp)
                jsonBody.put("compparkinguseyn", isParking)
                jsonBody.put("comppetuseyn", isPet)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get JWT Token Refresh
     * POST
     * @param refreshKey
     */
    fun getJwtTokenRefresh(requestQueue: RequestQueue, listener: RequestListener<JwtTokenRefreshData>,
                           refreshKey: String) {

        NagajaLog().d("wooks, API ========= getJwtTokenRefresh : refreshKey = $refreshKey")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            API_DOMAIN + JWT_TOKEN_REFRESH,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= JWT_TOKEN_REFRESH : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val jwtTokenRefreshData = JwtTokenRefreshData()
                        jwtTokenRefreshData.setSecureKey(jsonObject.getString("secureKey"))
                        jwtTokenRefreshData.setRefreshKey(jsonObject.getString("refreshKey"))

                        listener.onSuccess(jwtTokenRefreshData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["RefreshToken"] = "Bearer $refreshKey"
                return params
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get JWT Token By Email
     * POST
     * @param email
     */
    fun getJwtTokenByEmail(requestQueue: RequestQueue, listener: RequestListener<JwtTokenRefreshData>,
                           email: String) {

        NagajaLog().d("wooks, API ========= getJwtTokenByEmail : email = $email")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + JWT_TOKEN_BY_EMAIL,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= JWT_TOKEN_BY_EMAIL : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val jwtTokenRefreshData = JwtTokenRefreshData()
                        jwtTokenRefreshData.setSecureKey(jsonObject.getString("secureKey"))
                        jwtTokenRefreshData.setRefreshKey(jsonObject.getString("refreshKey"))

                        listener.onSuccess(jwtTokenRefreshData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["emailID"] = email
                return params
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Day Off Change
     * POST
     * @param secureKey
     * @param weekOffDay
     * @param doubleWeekOffDay
     * @param monthOffDay
     * @param companyServiceUid
     * @param companyUid
     */
    fun getCompanyDayOffChange(requestQueue: RequestQueue, listener: RequestListener<String>,
                               secureKey: String, weekOffDay: String, doubleWeekOffDay: String, monthOffDay: String,
                               companyServiceUid: Int, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyDayOffChange : secureKey = $secureKey / weekOffDay = $weekOffDay / doubleWeekOffDay = $doubleWeekOffDay / monthOffDay = $monthOffDay" +
                " / companyServiceUid = $companyServiceUid / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.PUT,
            API_DOMAIN + COMPANY_DAY_OFF_CHANGE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_DAY_OFF_CHANGE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("serviceweekoffdays", weekOffDay)
                jsonBody.put("serviceweekdoubleoffdays", doubleWeekOffDay)
                jsonBody.put("servicemonthoffdays", monthOffDay)
                jsonBody.put("compserviceuid", companyServiceUid)
                jsonBody.put("compuid", companyUid)
                return jsonBody.toString().toByteArray()
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Find Member
     * GET
     * @param secureKey
     */
    fun getFindMember(requestQueue: RequestQueue, listener: RequestListener<ArrayList<FindUserData>>,
                      secureKey: String, keyWord: String) {

        NagajaLog().d("wooks, API ========= getFindMember : keyWord = $keyWord")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$FIND_MEMBER?tuid=5&keyword=$keyWord",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= FIND_MEMBER : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val findUserListData = ArrayList<FindUserData>()

                        for (i in 0 until jsonArray.length()) {
                            val findUserData = FindUserData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                findUserData.setUid(jsonObject.getInt("uid"))
                                findUserData.setName(jsonObject.getString("name"))
                            }

                            findUserListData.add(findUserData)
                        }

                        listener.onSuccess(findUserListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["tuid"] = "5"
                params["keyword"] = keyWord
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Member Register
     * POST
     * @param secureKey
     * @param memberType
     * @param memberUid
     * @param companyUid
     */
    fun getCompanyMemberRegister(requestQueue: RequestQueue, listener: RequestListener<String>,
                                 secureKey: String, memberType: Int, memberUid: Int, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyMemberRegister : secureKey = $secureKey / memberType = $memberType / memberUid = $memberUid / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + COMPANY_MEMBER_REGISTER,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_MEMBER_REGISTER : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_COMPANY_MEMBER_REGISTER_ERROR_WRONG_GRANT -> {
                                errorCode = ERROR_CODE_COMPANY_MEMBER_REGISTER_ERROR_WRONG_GRANT
                            }

                            ERROR_CODE_COMPANY_MEMBER_REGISTER_ERROR_MANAGER_LIMIT -> {
                                errorCode = ERROR_CODE_COMPANY_MEMBER_REGISTER_ERROR_MANAGER_LIMIT
                            }

                            ERROR_CODE_EXISTING_MANAGER -> {
                                errorCode = ERROR_CODE_EXISTING_MANAGER
                            }
                        }

                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("compmanagergrant", memberType)
                jsonBody.put("memberuid", memberUid)
                jsonBody.put("compuid", companyUid)
                return jsonBody.toString().toByteArray()
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Member Remove
     * POST
     * @param secureKey
     * @param companyManagerUid
     * @param companyUid
     */
    fun getCompanyMemberRemove(requestQueue: RequestQueue, listener: RequestListener<String>,
                               secureKey: String, companyManagerUid: Int, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyMemberRegister : secureKey = $secureKey / companyManagerUid = $companyManagerUid / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + COMPANY_MEMBER_REMOVE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_MEMBER_REMOVE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("compmanageruid", companyManagerUid)
                jsonBody.put("compuid", companyUid)
                jsonBody.put("useyn", false)
                return jsonBody.toString().toByteArray()
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Business Time
     * PUT
     * @param secureKey
     * @param startTime
     * @param endTime
     * @param companyServiceUid
     * @param companyUid
     */
    fun getCompanyBusinessTime(requestQueue: RequestQueue, listener: RequestListener<String>,
                               secureKey: String, startTime: String, endTime: String,
                               companyServiceUid: Int, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyBusinessTime : secureKey = $secureKey / startTime = $startTime / endTime = $endTime" +
                " / companyServiceUid = $companyServiceUid / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.PUT,
            API_DOMAIN + COMPANY_BUSINESS_TIME,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_BUSINESS_TIME : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("servicebegintime", startTime)
                jsonBody.put("serviceendtime", endTime)
                jsonBody.put("compserviceuid", companyServiceUid)
                jsonBody.put("compuid", companyUid)
                return jsonBody.toString().toByteArray()
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Break Time
     * PUT
     * @param secureKey
     * @param firstStartTime
     * @param firstEndTime
     * @param secondStartTime
     * @param secondEndTime
     * @param companyServiceUid
     * @param companyUid
     */
    fun getCompanyBreakTime(requestQueue: RequestQueue, listener: RequestListener<String>,
                            secureKey: String, firstStartTime: String, firstEndTime: String,
                            secondStartTime: String, secondEndTime: String,
                            companyServiceUid: Int, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyBreakTime : secureKey = $secureKey / firstStartTime = $firstStartTime / firstEndTime = $firstEndTime" +
                " / secondStartTime = $secondStartTime / secondEndTime = $secondEndTime" +
                " / companyServiceUid = $companyServiceUid / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.PUT,
            API_DOMAIN + COMPANY_BREAK_TIME,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_BREAK_TIME : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("firstbreakbegintime", firstStartTime)
                jsonBody.put("firstbreakendtime", firstEndTime)
                jsonBody.put("secondbreakbegintime", secondStartTime)
                jsonBody.put("secondbreakendtime", secondEndTime)
                jsonBody.put("compserviceuid", companyServiceUid)
                jsonBody.put("compuid", companyUid)
                return jsonBody.toString().toByteArray()
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Reservation Person Limit
     * PUT
     * @param secureKey
     * @param personLimit
     * @param companyServiceUid
     * @param companyUid
     */
    fun getCompanyReservationPersonLimit(requestQueue: RequestQueue, listener: RequestListener<String>,
                                         secureKey: String, personLimit: Int, companyServiceUid: Int, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyReservationPersonLimit : secureKey = $secureKey / personLimit = $personLimit / companyServiceUid = $companyServiceUid" +
                " / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.PUT,
            API_DOMAIN + COMPANY_RESERVATION_PERSON_LIMIT,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_RESERVATION_PERSON_LIMIT : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("reservationpersonlimit", personLimit)
                jsonBody.put("compserviceuid", companyServiceUid)
                jsonBody.put("compuid", companyUid)
                return jsonBody.toString().toByteArray()
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Use Currency
     * PUT
     * @param secureKey
     * @param currencyStatus
     * @param companyUid
     * @param currencyUid
     */
    fun getCompanyUseCurrency(requestQueue: RequestQueue, listener: RequestListener<String>,
                              secureKey: String, currencyStatus: Boolean, companyUid: Int, currencyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyUseCurrency : secureKey = $secureKey / currencyStatus = $currencyStatus / companyUid = $companyUid" +
                " / currencyUid = $currencyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.PUT,
            API_DOMAIN + COMPANY_USE_CURRENCY,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_USE_CURRENCY : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("currencybaseyn", currencyStatus)
                jsonBody.put("currencyuid", currencyUid)
                jsonBody.put("compuid", companyUid)
                return jsonBody.toString().toByteArray()
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Product Update
     * PUT
     * @param secureKey
     * @param currencyStatus
     * @param companyUid
     * @param currencyUid
     */
    fun getCompanyProductUpdate(requestQueue: RequestQueue, listener: RequestListener<String>,
                                secureKey: String, test: String) {

        NagajaLog().d("wooks, API ========= getCompanyProductUpdate : secureKey = $secureKey")
        NagajaLog().d("wooks, API ========= getCompanyProductUpdate : $API_DOMAIN$COMPANY_PRODUCT_UPDATE/130" )

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.PUT,
            "$API_DOMAIN$COMPANY_PRODUCT_UPDATE/130",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_PRODUCT_UPDATE : $response")

                try {
//                    val encoding = charset(HttpHeaderParser.parseCharset(response.headers))
//
//                    val jsonObject = JSONObject(String(response.data, encoding))
//                    val status = jsonObject.getInt("status")

                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

//            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
//                return try {
//                    val utf8String = String(response.data, charset("UTF-8"))
//                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
//                } catch (e: UnsupportedEncodingException) {
//                    // log error
//                    Response.error(ParseError(e))
//                } catch (e: java.lang.Exception) {
//                    // log error
//                    Response.error(ParseError(e))
//                }
//            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["itemDto"] = test
                return params
            }


//            override val byteData: Map<String, DataPart>?
//                protected get() {
//                    val params: MutableMap<String, DataPart> = HashMap()
////                    if (null != bitmap) {
////                        params["image_file"] = DataPart(
////                            "$fileName",
////                            getFileDataFromDrawable(bitmap)!!
////                        )
////                    }
//
//                    return params
//                }

//            override fun getBody(): ByteArray {
//                val jsonBody = JSONObject()
//                NagajaLog().d("wooks, API ========= getCompanyProductUpdate : test = $test")
//                jsonBody.put("itemDto", "")
//                return jsonBody.toString().toByteArray()
//            }

//            override fun getPostParams(): MutableMap<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["itemDto"] = test
//                return params
//            }


            //            override fun getPostBodyContentType(): String {
//                return "application/json"
//            }

            //            override fun getBody(): ByteArray {
//                val params = HashMap<String, String>()
//                params["itemDto"] = test
//                return params.toString().toByteArray()
//            }

//            override fun getBody(): ByteArray {
//                val params = HashMap<String, String>()
//                params["itemDto"] = test
//
//                val map: List<String> = params.map {
//                        (key, value) -> "--$BOUNDARY\nContent-Disposition: form-data; name=\"$key\"\n\n$value\n"
//                }
//                val endResult = "${map.joinToString("")}\n--$BOUNDARY--\n"
//                return endResult.toByteArray()
//            }

//            @Throws(AuthFailureError::class)
//            override fun getParams(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["itemDto"] = test
//                return params
//            }


        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Main Menu Item
     * GET
     * @param secureKey
     * @param groupId
     * @param nationCode
     */
    fun getMainMenuItem(requestQueue: RequestQueue, listener: RequestListener<ArrayList<MainMenuItemData>>,
                        secureKey: String, groupId: String, nationCode: String) {

        NagajaLog().d("wooks, API ========= getMainMenuItem : secureKey = $secureKey / groupId = $groupId / nationCode = $nationCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$MAIN_MENU_ITEM?guid=$groupId&nuid=$nationCode",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= MAIN_MENU_ITEM : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val mainMenuItemListData = ArrayList<MainMenuItemData>()

                        for (i in 0 until jsonArray.length()) {
                            val mainMenuItemData = MainMenuItemData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                mainMenuItemData.setBaseMenuUid(jsonObject.getInt("baseMenuUID"))
                                mainMenuItemData.setMenuName( if (!jsonObject.isNull("menuName")) jsonObject.getString("menuName") else "" )
                                mainMenuItemData.setMenuNameEnglish( if (!jsonObject.isNull("menuNameEng")) jsonObject.getString("menuNameEng") else "" )
                                mainMenuItemData.setMenuImageOrigin( if (!jsonObject.isNull("menuImageOrigin")) jsonObject.getString("menuImageOrigin") else "" )
                                mainMenuItemData.setMenuImageName( if (!jsonObject.isNull("menuImageName")) jsonObject.getString("menuImageName") else "" )
                                mainMenuItemData.setMenuGroup(jsonObject.getInt("menuGroup"))
                                mainMenuItemData.setMenuSort(jsonObject.getInt("menuSort"))
                                mainMenuItemData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                mainMenuItemData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                mainMenuItemData.setLastUpdateDate( if (!jsonObject.isNull("lastUpdateDate")) jsonObject.getString("lastUpdateDate") else "" )
                                mainMenuItemData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                mainMenuItemData.setCategoryUri(jsonObject.getString("cagoUri"))

                                mainMenuItemListData.add(mainMenuItemData)
                            }
                        }

                        listener.onSuccess(mainMenuItemListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Recommend Place
     * GET
     * @param nationCode
     */
    fun getRecommendPlace(requestQueue: RequestQueue, listener: RequestListener<ArrayList<RecommendPlaceData>>,
                          nationCode: String) {

        NagajaLog().d("wooks, API ========= getRecommendPlace : nationCode = $nationCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$RECOMMEND_PLACE$nationCode",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= RECOMMEND_PLACE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val recommendPlaceListData = ArrayList<RecommendPlaceData>()

                        for (i in 0 until jsonArray.length()) {
                            val recommendPlaceData = RecommendPlaceData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                recommendPlaceData.setSuggestUid(jsonObject.getInt("suggestUID"))
                                recommendPlaceData.setCompanyUid(jsonObject.getInt("compUID"))
                                recommendPlaceData.setMessage( if (!jsonObject.isNull("message")) jsonObject.getString("message") else "" )
                                recommendPlaceData.setMessageNote( if (!jsonObject.isNull("messageNote")) jsonObject.getString("messageNote") else "" )
                                recommendPlaceData.setBeginDate( if (!jsonObject.isNull("beginDate")) jsonObject.getString("beginDate") else "" )
                                recommendPlaceData.setEndDate( if (!jsonObject.isNull("endDate")) jsonObject.getString("endDate") else "" )
                                recommendPlaceData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                recommendPlaceData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                recommendPlaceData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                recommendPlaceData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                recommendPlaceData.setCompanyName( if (!jsonObject.isNull("compName")) jsonObject.getString("compName") else "" )
                                recommendPlaceData.setCompanyNameEnglish( if (!jsonObject.isNull("compNameEng")) jsonObject.getString("compNameEng") else "" )
                                recommendPlaceData.setIsDeliveryAvailable(jsonObject.getBoolean("compDeliveryUseYn"))
                                recommendPlaceData.setIsReservationAvailable(jsonObject.getBoolean("compReservationUseYn"))
                                recommendPlaceData.setIsPickUpAvailable(jsonObject.getBoolean("compPickupUseYn"))
                                recommendPlaceData.setIsParkingAvailable(jsonObject.getBoolean("compParkingUseYn"))
                                recommendPlaceData.setIsPetAvailable(jsonObject.getBoolean("compPetUseYn"))
                                recommendPlaceData.setCompanyAddress( if (!jsonObject.isNull("compAddress")) jsonObject.getString("compAddress") else "" )

                                val imageJsonArray = jsonObject.getJSONArray("imageList")
                                val recommendPlaceImageListData = ArrayList<RecommendPlaceImageData>()
                                for (i in 0 until imageJsonArray.length()) {
                                    val recommendPlaceImageData = RecommendPlaceImageData()
                                    val imageJsonObject = imageJsonArray.getJSONObject(i)
                                    if (null != imageJsonObject) {
                                        recommendPlaceImageData.setCompanyImageUid(imageJsonObject.getInt("compImageUID"))
                                        recommendPlaceImageData.setCompanyUid(imageJsonObject.getInt("compUID"))
                                        recommendPlaceImageData.setCompanyImageSort(imageJsonObject.getInt("compImageSort"))
                                        recommendPlaceImageData.setCompanyImageOrigin( if (!imageJsonObject.isNull("compImageOrigin")) imageJsonObject.getString("compImageOrigin") else "" )
                                        recommendPlaceImageData.setCompanyImageName( if (!imageJsonObject.isNull("compImageName")) imageJsonObject.getString("compImageName") else "" )
                                        recommendPlaceImageData.setIsUseYn(imageJsonObject.getBoolean("useYn"))
                                        recommendPlaceImageData.setCreateDate( if (!imageJsonObject.isNull("createDate")) imageJsonObject.getString("createDate") else "" )
                                        recommendPlaceImageData.setUpdateDate( if (!imageJsonObject.isNull("updateDate")) imageJsonObject.getString("updateDate") else "" )
                                    }

                                    recommendPlaceImageListData.add(recommendPlaceImageData)
                                }

                                recommendPlaceData.setRecommendPlaceImageList(recommendPlaceImageListData)

                            }

                            recommendPlaceListData.add(recommendPlaceData)
                        }

                        listener.onSuccess(recommendPlaceListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Banner Event
     * GET
     * @param groupId
     * @param nationCode
     */
    fun getBannerEvent(requestQueue: RequestQueue, listener: RequestListener<ArrayList<BannerEventData>>,
                       groupId: String, nationCode: String) {

        NagajaLog().d("wooks, API ========= getBannerEvent : groupId = $groupId / nationCode = $nationCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$BANNER_EVENT?group=$groupId&locnuid=$nationCode",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= BANNER_EVENT : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val bannerEventListData = ArrayList<BannerEventData>()

                        for (i in 0 until jsonArray.length()) {
                            val bannerEventData = BannerEventData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                bannerEventData.setBannerUid(jsonObject.getInt("bannerUID"))
                                bannerEventData.setBannerSubject( if (!jsonObject.isNull("bannerSubject")) jsonObject.getString("bannerSubject") else "" )
                                bannerEventData.setBannerContent( if (!jsonObject.isNull("bannerContent")) jsonObject.getString("bannerContent") else "" )
                                bannerEventData.setBannerImageName( if (!jsonObject.isNull("bannerImageName")) jsonObject.getString("bannerImageName") else "" )
                                bannerEventData.setBannerImageOrigin( if (!jsonObject.isNull("bannerImageOrigin")) jsonObject.getString("bannerImageOrigin") else "" )
                                bannerEventData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                bannerEventData.setBannerTargetUri( if (!jsonObject.isNull("bannerTargetUri")) jsonObject.getString("bannerTargetUri") else /*"https://www.nagaja.com"*/"" )
                                bannerEventData.setBannerGroup(jsonObject.getInt("bannerGroup"))
                                bannerEventData.setBeginDate( if (!jsonObject.isNull("beginDate")) jsonObject.getString("beginDate") else "" )
                                bannerEventData.setEndDate( if (!jsonObject.isNull("endDate")) jsonObject.getString("endDate") else "" )
                                bannerEventData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                bannerEventData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                bannerEventData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                bannerEventData.setTargetUid(jsonObject.getInt("targetUID"))
                            }

                            bannerEventListData.add(bannerEventData)
                        }

                        listener.onSuccess(bannerEventListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Service Information
     * GET
     */
    fun getServiceInformation(requestQueue: RequestQueue, listener: RequestListener<ServiceInformationData>) {

        NagajaLog().d("wooks, API ========= getServiceInformation")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            API_DOMAIN + SERVICE_INFORMATION,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= SERVICE_INFORMATION : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val serviceInformationData = ServiceInformationData()

                        serviceInformationData.setServiceSite( if (!jsonObject.isNull("serviceSite")) jsonObject.getString("serviceSite") else "" )
                        serviceInformationData.setServiceName( if (!jsonObject.isNull("serviceName")) jsonObject.getString("serviceName") else "" )
                        serviceInformationData.setServiceNameEnglish( if (!jsonObject.isNull("serviceNameEng")) jsonObject.getString("serviceNameEng") else "" )
                        serviceInformationData.setServiceMasterName( if (!jsonObject.isNull("serviceMasterName")) jsonObject.getString("serviceMasterName") else "" )
                        serviceInformationData.setServiceMasterNameEnglish( if (!jsonObject.isNull("serviceMasterNameEng")) jsonObject.getString("serviceMasterNameEng") else "" )
                        serviceInformationData.setServiceNumber( if (!jsonObject.isNull("serviceNumber")) jsonObject.getString("serviceNumber") else "" )
                        serviceInformationData.setServiceNumberEx( if (!jsonObject.isNull("serviceNumberEx")) jsonObject.getString("serviceNumberEx") else "" )
                        serviceInformationData.setServiceAddress( if (!jsonObject.isNull("serviceAddress")) jsonObject.getString("serviceAddress") else "" )
                        serviceInformationData.setServiceAddressEnglish( if (!jsonObject.isNull("serviceAddressEng")) jsonObject.getString("serviceAddressEng") else "" )
                        serviceInformationData.setServiceAddressDetail( if (!jsonObject.isNull("serviceAddressDetail")) jsonObject.getString("serviceAddressDetail") else "" )
                        serviceInformationData.setServiceAddressDetailEnglish( if (!jsonObject.isNull("serviceAddressDetailEng")) jsonObject.getString("serviceAddressDetailEng") else "" )
                        serviceInformationData.setServicePhone( if (!jsonObject.isNull("servicePhone")) jsonObject.getString("servicePhone") else "" )
                        serviceInformationData.setServiceEmail( if (!jsonObject.isNull("serviceEmail")) jsonObject.getString("serviceEmail") else "" )

                        listener?.onSuccess(serviceInformationData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Nation
     * GET
     */
    fun getLocationNation(requestQueue: RequestQueue, listener: RequestListener<ArrayList<LocationNationData>>,
                          nationCode: String) {

        NagajaLog().d("wooks, API ========= getLocationNation : nationCode = $nationCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$LOCATION_NATION?nuid=$nationCode",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= LOCATION_NATION : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val locationNationListData = ArrayList<LocationNationData>()

                        for (i in 0 until jsonArray.length()) {
                            val locationNationData = LocationNationData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                locationNationData.setLocationUid(jsonObject.getInt("locationUID"))
                                locationNationData.setLocationName( if (!jsonObject.isNull("locationName")) jsonObject.getString("locationName") else "" )
                                locationNationData.setNationUid(jsonObject.getInt("nationUID"))
                            }

                            locationNationListData.add(locationNationData)
                        }

                        listener.onSuccess(locationNationListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Location Area
     * GET
     * @param parentUid
     * @param nationCode
     */
    fun getLocationArea(requestQueue: RequestQueue, listener: RequestListener<ArrayList<LocationAreaMainData>>,
                        parentUid: String, nationCode: String) {

        NagajaLog().d("wooks, API ========= getLocationArea : parentUid = $parentUid / nationCode = $nationCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$LOCATION_AREA?puid=$parentUid&nuid=$nationCode",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= LOCATION_AREA : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val locationAreaMainListData = ArrayList<LocationAreaMainData>()

                        for (i in 0 until jsonArray.length()) {
                            val locationAreaMainData = LocationAreaMainData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                locationAreaMainData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                locationAreaMainData.setCategoryDepth(jsonObject.getInt("cagoDepth"))
                                locationAreaMainData.setCategorySort(jsonObject.getInt("cagoSort"))
                                locationAreaMainData.setCategoryName( if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "" )
                                locationAreaMainData.setCategoryNameEnglish( if (!jsonObject.isNull("cagoNameEng")) jsonObject.getString("cagoNameEng") else "" )
                                locationAreaMainData.setCategoryUri( if (!jsonObject.isNull("cagoUri")) jsonObject.getString("cagoUri") else "" )
                                locationAreaMainData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                locationAreaMainData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                locationAreaMainData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                                locationAreaMainData.setCategoryLatitude(jsonObject.getDouble("cagoLatitude"))
                                locationAreaMainData.setCategoryLongitude(jsonObject.getDouble("cagoLongitude"))

                                val jsonSubArray = jsonObject.getJSONArray("cagoList")
                                val locationAreaSubListData = ArrayList<LocationAreaSubData>()
                                for (k in 0 until jsonSubArray.length()) {
                                    val locationAreaSubData = LocationAreaSubData()
                                    val jsonSubObject = jsonSubArray.getJSONObject(k)
                                    if (null != jsonSubObject) {
                                        locationAreaSubData.setCategoryUid(jsonSubObject.getInt("cagoUID"))
                                        locationAreaSubData.setCategoryDepth(jsonSubObject.getInt("cagoDepth"))
                                        locationAreaSubData.setCategorySort(jsonSubObject.getInt("cagoSort"))
                                        locationAreaSubData.setCategoryName( if (!jsonSubObject.isNull("cagoName")) jsonSubObject.getString("cagoName") else "" )
                                        locationAreaSubData.setCategoryNameEnglish( if (!jsonSubObject.isNull("cagoNameEng")) jsonSubObject.getString("cagoNameEng") else "" )
                                        locationAreaSubData.setCategoryUri( if (!jsonSubObject.isNull("cagoUri")) jsonSubObject.getString("cagoUri") else "" )
                                        locationAreaSubData.setIsUseYn(jsonSubObject.getBoolean("useYn"))
                                        locationAreaSubData.setCreateDate( if (!jsonSubObject.isNull("createDate")) jsonSubObject.getString("createDate") else "" )
                                        locationAreaSubData.setParentCategoryUid(jsonSubObject.getInt("parentCagoUID"))
                                        locationAreaSubData.setCategoryLatitude(jsonSubObject.getDouble("cagoLatitude"))
                                        locationAreaSubData.setCategoryLongitude(jsonSubObject.getDouble("cagoLongitude"))
                                        locationAreaSubData.setIsCategoryStatusEnable(jsonSubObject.getInt("cagoStatus") != 0)
                                    }

                                    locationAreaSubListData.add(locationAreaSubData)
                                }

                                locationAreaMainData.setLocationAreaSubListData(locationAreaSubListData)
                            }

                            locationAreaMainListData.add(locationAreaMainData)
                        }

                        listener.onSuccess(locationAreaMainListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Save User Location
     * POST
     * @param locationUid
     * @param locationCategoryUid
     * @param locationAreaUid
     * @param latitude
     * @param longitude
     * @param zipCode
     * @param address
     * @param addressDetail
     * @param memberUid
     */
    fun getSaveUserLocation(requestQueue: RequestQueue, listener: RequestListener<String>,
                            locationUid: Int, locationCategoryUid: Int, locationAreaUid: Int, latitude: String, longitude: String,
                            zipCode: String, address: String, addressDetail: String, memberUid: Int) {

        NagajaLog().d("wooks, API ========= getSaveUserLocation : locationUid = $locationUid / locationCategoryUid = $locationCategoryUid / locationAreaUid = $locationAreaUid " +
                "/ latitude = $latitude / longitude = $longitude / zipCode = $zipCode / address = $address / addressDetail = $addressDetail / memberUid = $memberUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + SAVE_USER_LOCATION,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= SAVE_USER_LOCATION : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("locationuid", locationUid)
                jsonBody.put("loccagouid", locationCategoryUid)
                jsonBody.put("loccagoareauid", locationAreaUid)
                jsonBody.put("virtuallatitude", latitude)
                jsonBody.put("virtuallongitude", longitude)
                jsonBody.put("virtualaddresszipcode", zipCode)
                jsonBody.put("virtualaddress", address)
                jsonBody.put("virtualaddressdetail", addressDetail)
                jsonBody.put("useyn", true)
                jsonBody.put("memberuid", memberUid)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Location Area
     * GET
     * @param latitude
     * @param longitude
     * @param nationCode
     */
    fun getDefaultSettingArea(requestQueue: RequestQueue, listener: RequestListener<DefaultSettingAreaData>,
                              latitude: String, longitude: String, nationCode: String) {

        NagajaLog().d("wooks, API ========= getDefaultSettingArea : latitude = $latitude / longitude = $longitude / nationCode = $nationCode")
        NagajaLog().d("wooks, API ========= getDefaultSettingArea : $API_DOMAIN$DEFAULT_SETTING_AREA?lati=$latitude&long=$longitude&nuid=$nationCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$DEFAULT_SETTING_AREA?lati=$latitude&long=$longitude&nuid=$nationCode",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= DEFAULT_SETTING_AREA : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val defaultSettingAreaData = DefaultSettingAreaData()

                        defaultSettingAreaData.setVirtualUid(jsonObject.getInt("virtualUID"))
                        defaultSettingAreaData.setLocationUid(jsonObject.getInt("locationUID"))
                        defaultSettingAreaData.setLocationName(jsonObject.getString("locationName"))
                        defaultSettingAreaData.setCategoryUid(jsonObject.getInt("locCagoUID"))
                        defaultSettingAreaData.setCategoryName(jsonObject.getString("locaCagoName"))
                        defaultSettingAreaData.setCategoryNameEnglish(jsonObject.getString("locaCagoNameEng"))
                        defaultSettingAreaData.setCategoryAreaUid(jsonObject.getInt("locCagoAreaUID"))
                        defaultSettingAreaData.setCategoryAreaName(jsonObject.getString("locaAreaName"))
                        defaultSettingAreaData.setCategoryAreaNameEnglish(jsonObject.getString("locaAreaNameEng"))
                        defaultSettingAreaData.setVirtualLatitude(jsonObject.getDouble("virtualLatitude"))
                        defaultSettingAreaData.setVirtualLongitude(jsonObject.getDouble("virtualLongitude"))

                        listener.onSuccess(defaultSettingAreaData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Store Category Data
     * GET
     * @param categoryUid
     * @param nationCode
     */
    fun getStoreCategoryData(requestQueue: RequestQueue, listener: RequestListener<ArrayList<StoreCategoryData>>,
                             categoryUid: String, nationCode: String) {

        NagajaLog().d("wooks, API ========= getStoreCategoryData : categoryUid = $categoryUid / nationCode = $nationCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$STORE_CATEGORY_DATA?uid=$categoryUid&nuid=$nationCode",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= STORE_CATEGORY_DATA : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val storeCategoryListData = ArrayList<StoreCategoryData>()

                        for (i in 0 until jsonArray.length()) {
                            val storeCategoryData = StoreCategoryData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                storeCategoryData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                storeCategoryData.setCategoryDepth(jsonObject.getInt("cagoDepth"))
                                storeCategoryData.setCategorySort(jsonObject.getInt("cagoSort"))
                                storeCategoryData.setCategoryName( if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "" )
                                storeCategoryData.setCategoryNameEnglish( if (!jsonObject.isNull("cagoNameEng")) jsonObject.getString("cagoNameEng") else "" )
                                storeCategoryData.setCategoryUri( if (!jsonObject.isNull("cagoUri")) jsonObject.getString("cagoUri") else "" )
                                storeCategoryData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                storeCategoryData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                storeCategoryData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))

                                storeCategoryListData.add(storeCategoryData)
                            }
                        }

                        listener.onSuccess(storeCategoryListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Store List Data
     * POST
     */
    fun getStoreListData(requestQueue: RequestQueue, listener: RequestListener<ArrayList<StoreData>>,
                         secureKey: String, pageNow: Int, pageCount: Int, searchType: Int, companySearch: String, type: Int, sort: Int,
                         nationCode:String, categoryUid: Int, latitude: String, longitude: String, gugunUid: Int) {

        NagajaLog().d("wooks, API ========= getStoreListData : pageNow = $pageNow / pageCount = $pageCount / searchType = $searchType / companySearch = $companySearch / type = $type / sort = $sort " +
                "/ nationCode = $nationCode / categoryUid = $categoryUid / latitude = $latitude / longitude = $longitude / gugunUid = $gugunUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + STORE_LIST_DATA,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= STORE_LIST_DATA : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val storeListData = ArrayList<StoreData>()

                        for (i in 0 until jsonArray.length()) {
                            val storeData = StoreData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                storeData.setTotalCount(json.getInt("tcount"))
                                storeData.setCompanyUid(jsonObject.getInt("compUID"))
                                storeData.setCompanyStatus(jsonObject.getInt("compStatus"))
                                storeData.setCompanyName( if (!jsonObject.isNull("compName")) jsonObject.getString("compName") else "" )
                                storeData.setCompanyNameEnglish( if (!jsonObject.isNull("compNameEng")) jsonObject.getString("compNameEng") else "" )
                                storeData.setCompanyNationPhoneCode( if (!jsonObject.isNull("compNationPhone")) jsonObject.getString("compNationPhone") else "" )
                                storeData.setCompanyEmail( if (!jsonObject.isNull("compEmail")) jsonObject.getString("compEmail") else "" )
                                storeData.setCompanyPhoneNumber( if (!jsonObject.isNull("compPhone")) jsonObject.getString("compPhone") else "" )
                                storeData.setCompanyLatitude(jsonObject.getDouble("compLatitude"))
                                storeData.setCompanyLongitude(jsonObject.getDouble("compLongitude"))
                                storeData.setCompanyAddress( if (!jsonObject.isNull("compAddress")) jsonObject.getString("compAddress") else "" )
                                storeData.setCompanyAddressDetail( if (!jsonObject.isNull("compAddressDetail")) jsonObject.getString("compAddressDetail") else "" )
                                storeData.setCompanyLicenseImageOrigin( if (!jsonObject.isNull("compLicenseImageOrigin")) jsonObject.getString("compLicenseImageOrigin") else "" )
                                storeData.setCompanyLicenseImage( if (!jsonObject.isNull("compLicenseImage")) jsonObject.getString("compLicenseImage") else "" )
                                storeData.setCompanyDesc( if (!jsonObject.isNull("compDesc")) jsonObject.getString("compDesc") else "" )
                                storeData.setCompanyFacebookUrl( if (!jsonObject.isNull("compFacebookUrl")) jsonObject.getString("compFacebookUrl") else "" )
                                storeData.setCompanyKakaoUrl( if (!jsonObject.isNull("compKakaoUrl")) jsonObject.getString("compKakaoUrl") else "" )
                                storeData.setCompanyLineUrl( if (!jsonObject.isNull("compLineUrl")) jsonObject.getString("compLineUrl") else "" )
                                storeData.setCompanyManagerLimit(jsonObject.getInt("compManagerLimit"))
                                storeData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                storeData.setNationUid(jsonObject.getInt("nationUID"))
                                storeData.setMemberUid(jsonObject.getInt("memberUID"))
                                storeData.setIsCompanyPublicYn(jsonObject.getBoolean("compPublicYn"))
                                storeData.setIsCompanyCertificationYn(jsonObject.getBoolean("compCertificationYn"))
//                                storeData.setIsCompanyCertificationMarkYn(jsonObject.getBoolean("compCertificationMarkYn"))
                                storeData.setIsCompanyCertificationMarkYn( if (!jsonObject.isNull("compCertificationMarkYn")) jsonObject.getBoolean("compCertificationMarkYn") else false )
                                storeData.setCertificationBeginDate( if (!jsonObject.isNull("certificationBeginDate")) jsonObject.getString("certificationBeginDate") else "" )
                                storeData.setCertificationEndDate( if (!jsonObject.isNull("certificationEndDate")) jsonObject.getString("certificationEndDate") else "" )
                                storeData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                storeData.setConfirmDate( if (!jsonObject.isNull("confirmDate")) jsonObject.getString("confirmDate") else "" )
                                storeData.setCompanyManagerName( if (!jsonObject.isNull("compManagerName")) jsonObject.getString("compManagerName") else "" )
                                storeData.setCompanyMainImage( if (!jsonObject.isNull("compMainImage")) jsonObject.getString("compMainImage") else "" )
                                storeData.setCompanyMainImageOrigin( if (!jsonObject.isNull("compMainImageOrigin")) jsonObject.getString("compMainImageOrigin") else "" )
                                storeData.setCompanyServiceUid(jsonObject.getInt("compServiceUID"))
                                storeData.setServiceStatusUid(jsonObject.getInt("serviceStatus"))
                                storeData.setServiceMonthOffDays( if (!jsonObject.isNull("serviceMonthOffDays")) jsonObject.getString("serviceMonthOffDays") else "" )
                                storeData.setServiceWeekOffDays( if (!jsonObject.isNull("serviceWeekOffDays")) jsonObject.getString("serviceWeekOffDays") else "" )
                                storeData.setServiceWeekDoubleOffDays( if (!jsonObject.isNull("serviceWeekDoubleOffDays")) jsonObject.getString("serviceWeekDoubleOffDays") else "" )
                                storeData.setServiceBeginTime( if (!jsonObject.isNull("serviceBeginTime")) jsonObject.getString("serviceBeginTime") else "" )
                                storeData.setServiceEndTime( if (!jsonObject.isNull("serviceEndTime")) jsonObject.getString("serviceEndTime") else "" )
                                storeData.setIsDeliveryUseYn(jsonObject.getBoolean("compDeliveryUseYn"))
                                storeData.setIsReservationUseYn(jsonObject.getBoolean("compReservationUseYn"))
                                storeData.setIsPickUpUseYn(jsonObject.getBoolean("compPickupUseYn"))
                                storeData.setIsParkingUseYn(jsonObject.getBoolean("compParkingUseYn"))
                                storeData.setIsPetUseYn(jsonObject.getBoolean("compPetUseYn"))
                                storeData.setRegularUid(jsonObject.getInt("regularUID"))
                                storeData.setIsRegularUseYn(jsonObject.getBoolean("regularUseYn"))
                                storeData.setIsRecommendUseYn(jsonObject.getBoolean("recommUseYn"))
                                storeData.setCompanyReviewCount(jsonObject.getInt("compReviewCount"))
                                storeData.setCompanyRecommendCount(jsonObject.getInt("compRecommCount"))
                                storeData.setCompanyRegularCount(jsonObject.getInt("compRegularCount"))
                                storeData.setCompanyReviewPointAverage(jsonObject.getDouble("reviewPointAverage"))

                                storeListData.add(storeData)
                            }
                        }

                        listener.onSuccess(storeListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (!TextUtils.isEmpty(secureKey)) {
                    params["Authorization"] = "Bearer $secureKey"
                }
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["pagenow"] = pageNow.toString()
                params["pagesz"] = pageCount.toString()
                params["sr"] = searchType.toString()
                params["srtxt"] = companySearch
                params["srtype"] = type.toString()
                params["sort"] = sort.toString()
                params["nuid"] = nationCode
                params["cago"] = categoryUid.toString()
                params["lati"] = latitude
                params["long"] = longitude
                params["gugunuid"] = gugunUid.toString()

                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Store Recommend Save
     * POST
     * @param secureKey
     * @param companyUid
     * @param like
     */
    fun getStoreRecommendSave(requestQueue: RequestQueue, listener: RequestListener<String>,
                              secureKey: String, companyUid: Int, like: Int) {

        NagajaLog().d("wooks, API ========= getStoreRecommendSave : secureKey = $secureKey / companyUid = $companyUid / like = $like")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + STORE_RECOMMEND_SAVE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= STORE_RECOMMEND_SAVE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener?.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("compuid", companyUid)
                jsonBody.put("useyn", like)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Store Recommend Save
     * POST
     * @param secureKey
     * @param companyUid
     * @param isRegular
     */
    fun getStoreRegularSave(requestQueue: RequestQueue, listener: RequestListener<String>,
                            secureKey: String, companyUid: Int, isRegular: Int) {

        NagajaLog().d("wooks, API ========= getStoreRegularSave : secureKey = $secureKey / companyUid = $companyUid / isRegular = $isRegular")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + STORE_REGULAR_SAVE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= STORE_RECOMMEND_SAVE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener?.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("compuid", companyUid)
                jsonBody.put("useyn", isRegular)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Store Detail
     * GET
     * @param companyUid
     * @param nationCode
     */
    fun getStoreDetail(requestQueue: RequestQueue, listener: RequestListener<StoreDetailData>,
                       secureKey: String, companyUid: String, nationCode: String) {

        NagajaLog().d("wooks, API ========= getStoreDetail : secureKey = $secureKey /companyUid = $companyUid / nationCode = $nationCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$STORE_DETAIL/$companyUid?nuid=$nationCode",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= STORE_DETAIL : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val storeDetailData = StoreDetailData()

                        storeDetailData.setCompanyUid(jsonObject.getInt("compUID"))
                        storeDetailData.setStatus(jsonObject.getInt("compStatus"))
                        storeDetailData.setCompanyName(if (!jsonObject.isNull("compName")) jsonObject.getString("compName") else "")
                        storeDetailData.setCompanyNameEnglish(if (!jsonObject.isNull("compNameEng")) jsonObject.getString("compNameEng") else "")
                        storeDetailData.setNationPhone(if (!jsonObject.isNull("compNationPhone")) jsonObject.getString("compNationPhone") else "")
                        storeDetailData.setEmail(if (!jsonObject.isNull("compEmail")) jsonObject.getString("compEmail") else "")
                        storeDetailData.setPhoneNumber(if (!jsonObject.isNull("compPhone")) jsonObject.getString("compPhone") else "")
                        storeDetailData.setLatitude(jsonObject.getDouble("compLatitude"))
                        storeDetailData.setLongitude(jsonObject.getDouble("compLongitude"))
                        storeDetailData.setAddress(if (!jsonObject.isNull("compAddress")) jsonObject.getString("compAddress") else "")
                        storeDetailData.setAddressDetail(if (!jsonObject.isNull("compAddressDetail")) jsonObject.getString("compAddressDetail") else "")
                        storeDetailData.setDesc(if (!jsonObject.isNull("compDesc")) jsonObject.getString("compDesc") else "")
                        storeDetailData.setFacebookUrl(if (!jsonObject.isNull("compFacebookUrl")) jsonObject.getString("compFacebookUrl") else "")
                        storeDetailData.setKakaoUrl(if (!jsonObject.isNull("compKakaoUrl")) jsonObject.getString("compKakaoUrl") else "")
                        storeDetailData.setLineUrl(if (!jsonObject.isNull("compLineUrl")) jsonObject.getString("compLineUrl") else "")
                        storeDetailData.setCategoryUid(jsonObject.getInt("cagoUID"))
                        storeDetailData.setNationUid(jsonObject.getInt("nationUID"))
                        storeDetailData.setMemberUid(jsonObject.getInt("memberUID"))
                        storeDetailData.setIsPublicYn(jsonObject.getBoolean("compPublicYn"))
                        storeDetailData.setIsCertificationYn(jsonObject.getBoolean("compCertificationYn"))
                        storeDetailData.setIsCertificationMarkYn(if (!jsonObject.isNull("compCertificationMarkYn")) jsonObject.getBoolean("compCertificationMarkYn") else false)
                        storeDetailData.setCertificationBeginDate(if (!jsonObject.isNull("certificationBeginDate")) jsonObject.getString("certificationBeginDate") else "")
                        storeDetailData.setCertificationEndDate(if (!jsonObject.isNull("certificationEndDate")) jsonObject.getString("cerificationEndDate") else "")
                        storeDetailData.setIsUseYn(jsonObject.getBoolean("useYn"))
                        storeDetailData.setCreateDate(if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "")
                        storeDetailData.setUpdateDate(if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "")
                        storeDetailData.setConfirmDate(if (!jsonObject.isNull("confirmDate")) jsonObject.getString("confirmDate") else "")
                        storeDetailData.setManagerName(if (!jsonObject.isNull("compManagerName")) jsonObject.getString("compManagerName") else "")
                        storeDetailData.setPayment(if (!jsonObject.isNull("compPayment")) jsonObject.getString("compPayment") else "")
                        storeDetailData.setMainImageUrl(if (!jsonObject.isNull("compMainImage")) jsonObject.getString("compMainImage") else "")
                        storeDetailData.setMainImageOrigin(if (!jsonObject.isNull("compMainImageOrigin")) jsonObject.getString("compMainImageOrigin") else "")
                        storeDetailData.setServiceUid(jsonObject.getInt("compServiceUID"))
                        storeDetailData.setServiceStatus(jsonObject.getInt("serviceStatus"))
                        storeDetailData.setServiceMonthOffDays(if (!jsonObject.isNull("serviceMonthOffDays")) jsonObject.getString("serviceMonthOffDays") else "")
                        storeDetailData.setServiceWeekOffDays(if (!jsonObject.isNull("serviceWeekOffDays")) jsonObject.getString("serviceWeekOffDays") else "")
                        storeDetailData.setServiceWeekDoubleOffDays(if (!jsonObject.isNull("serviceWeekDoubleOffDays")) jsonObject.getString("serviceWeekDoubleOffDays") else "")
                        storeDetailData.setServiceBeginTime(if (!jsonObject.isNull("serviceBeginTime")) jsonObject.getString("serviceBeginTime") else "")
                        storeDetailData.setServiceEndTime(if (!jsonObject.isNull("serviceEndTime")) jsonObject.getString("serviceEndTime") else "")
                        storeDetailData.setFirstBreakBeginTime(if (!jsonObject.isNull("firstBreakBeginTime")) jsonObject.getString("firstBreakBeginTime") else "")
                        storeDetailData.setFirstBreakEndTime(if (!jsonObject.isNull("firstBreakEndTime")) jsonObject.getString("firstBreakEndTime") else "")
                        storeDetailData.setSecondBreakBeginTime(if (!jsonObject.isNull("secondBreakBeginTime")) jsonObject.getString("secondBreakBeginTime") else "")
                        storeDetailData.setSecondBreakEndTime(if (!jsonObject.isNull("secondBreakEndTime")) jsonObject.getString("secondBreakEndTime") else "")
                        storeDetailData.setFirstRestBeginTime(if (!jsonObject.isNull("firstRestBeginTime")) jsonObject.getString("firstRestBeginTime") else "")
                        storeDetailData.setFirstRestEndTime(if (!jsonObject.isNull("firstRestEndTime")) jsonObject.getString("firstRestEndTime") else "")
                        storeDetailData.setSecondRestBeginTime(if (!jsonObject.isNull("secondRestBeginTime")) jsonObject.getString("secondRestBeginTime") else "")
                        storeDetailData.setSecondRestEndTime(if (!jsonObject.isNull("secondRestEndTime")) jsonObject.getString("secondRestEndTime") else "")
                        storeDetailData.setDeliveryBeginTime(if (!jsonObject.isNull("deliveryBeginTime")) jsonObject.getString("deliveryBeginTime") else "")
                        storeDetailData.setDeliveryEndTime(if (!jsonObject.isNull("deliveryEndTime")) jsonObject.getString("deliveryEndTime") else "")
                        storeDetailData.setReservationBeginTime(if (!jsonObject.isNull("reservationBeginTime")) jsonObject.getString("reservationBeginTime") else "")
                        storeDetailData.setReservationEndTime(if (!jsonObject.isNull("reservationEndTime")) jsonObject.getString("reservationEndTime") else "")
                        storeDetailData.setReservationDayLimit(jsonObject.getInt("reservationDayLimit"))
                        storeDetailData.setReservationPersonLimit(jsonObject.getInt("reservationPersonLimit"))
                        storeDetailData.setReservationTeamLimit(jsonObject.getInt("reservationTeamLimit"))
                        storeDetailData.setIsDeliveryUseYn(jsonObject.getBoolean("compDeliveryUseYn"))
                        storeDetailData.setIsReservationUseYn(jsonObject.getBoolean("compReservationUseYn"))
                        storeDetailData.setIsPickUpUseYn(jsonObject.getBoolean("compPickupUseYn"))
                        storeDetailData.setIsParkingUseYn(jsonObject.getBoolean("compParkingUseYn"))
                        storeDetailData.setIsPetUseYn(jsonObject.getBoolean("compPetUseYn"))
                        storeDetailData.setReviewCount(jsonObject.getInt("compReviewCount"))
                        storeDetailData.setRecommendCount(jsonObject.getInt("compRecommCount"))
                        storeDetailData.setRegularCount(jsonObject.getInt("compRegularCount"))
                        storeDetailData.setRegularUid(jsonObject.getInt("regularUID"))
                        storeDetailData.setContractStatus(jsonObject.getInt("contractStatus"))
                        storeDetailData.setIsRegularUseYn(jsonObject.getBoolean("regularUseYn"))
                        storeDetailData.setIsRecommendUseYn(jsonObject.getBoolean("recommUseYn"))

                        val jsonImageArray = jsonObject.getJSONArray("compImageList")
                        val storeDetailImageListData = ArrayList<StoreDetailImageData>()
                        for (i in 0 until jsonImageArray.length()) {
                            val storeDetailImageData = StoreDetailImageData()
                            val jsonImageObject = jsonImageArray.getJSONObject(i)
                            if (null != jsonImageObject) {
                                storeDetailImageData.setCompanyImageUid(jsonImageObject.getInt("compImageUID"))
                                storeDetailImageData.setCompanyUid(jsonImageObject.getInt("compUID"))
                                storeDetailImageData.setCompanyImageSort(jsonImageObject.getInt("compImageSort"))
                                storeDetailImageData.setCompanyOrigin(if (!jsonImageObject.isNull("compImageOrigin")) jsonImageObject.getString("compImageOrigin") else "")
                                storeDetailImageData.setCompanyImageName(if (!jsonImageObject.isNull("compImageName")) jsonImageObject.getString("compImageName") else "")
                                storeDetailImageData.setIsUseYn(jsonImageObject.getBoolean("useYn"))
                                storeDetailImageData.setCreateDate(if (!jsonImageObject.isNull("createDate")) jsonImageObject.getString("createDate") else "")
                                storeDetailImageData.setUpdateDate(if (!jsonImageObject.isNull("updateDate")) jsonImageObject.getString("updateDate") else "")
                            }

                            storeDetailImageListData.add(storeDetailImageData)
                        }

                        storeDetailData.setStoreDetailImageListData(storeDetailImageListData)


                        listener.onSuccess(storeDetailData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (!TextUtils.isEmpty(secureKey)) {
                    params["Authorization"] = "Bearer $secureKey"
                }
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Store Detail Menu Data
     * GET
     * @param companyUid
     * @param categoryUid
     */
    fun getStoreDetailMenuData(requestQueue: RequestQueue, listener: RequestListener<ArrayList<StoreDetailMenuData>>,
                               companyUid: String, categoryUid: String) {

        NagajaLog().d("wooks, API ========= getStoreDetailMenuData : companyUid = $companyUid / categoryUid = $categoryUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$STORE_MENU/$companyUid?cago=$categoryUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= STORE_MENU : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val storeDetailMenuListData = ArrayList<StoreDetailMenuData>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                val jsonMenuArray = jsonObject.getJSONArray("itemList")
                                for (k in 0 until jsonMenuArray.length()) {
                                    val json = jsonMenuArray.getJSONObject(k)
                                    val storeDetailMenuData = StoreDetailMenuData()
                                    if (null != json) {
                                        storeDetailMenuData.setItemUid(json.getInt("itemUID"))
                                        storeDetailMenuData.setItemStatus(json.getInt("itemStatus"))
                                        storeDetailMenuData.setItemName( if (!json.isNull("itemName")) json.getString("itemName") else "" )
                                        storeDetailMenuData.setItemContent( if (!json.isNull("itemContent")) json.getString("itemContent") else "" )
                                        storeDetailMenuData.setItemCurrencyCode( if (!json.isNull("itemCurrencyCode")) json.getString("itemCurrencyCode") else "" )
                                        storeDetailMenuData.setItemPrice( if (!json.isNull("itemPrice")) json.getString("itemPrice") else "" )
                                        storeDetailMenuData.setIsUseYn(json.getBoolean("useYn"))
                                        storeDetailMenuData.setCreateDate( if (!json.isNull("createDate")) json.getString("createDate") else "" )
                                        storeDetailMenuData.setUpdateDate( if (!json.isNull("updateDate")) json.getString("updateDate") else "" )
                                        storeDetailMenuData.setCategoryUid(json.getInt("cagoUID"))
                                        storeDetailMenuData.setItemCategoryUid(json.getInt("itemCagoUID"))
                                        storeDetailMenuData.setCompanyUid(json.getInt("compUID"))
                                        storeDetailMenuData.setMemberUid(json.getInt("memberUID"))
                                        storeDetailMenuData.setIsItemRecommend(json.getBoolean("itemRecommYn"))

                                        val jsonImageArray = json.getJSONArray("imageList")
                                        val storeDetailMenuImageListData = ArrayList<StoreDetailMenuImageData>()
                                        for (j in 0 until jsonImageArray.length()) {
                                            val storeDetailMenuImageData = StoreDetailMenuImageData()
                                            val jsonImageObject = jsonImageArray.getJSONObject(j)
                                            if (null != jsonImageObject) {
                                                storeDetailMenuImageData.setItemImageUid(jsonImageObject.getInt("itemImageUID"))
                                                storeDetailMenuImageData.setItemImageSort(jsonImageObject.getInt("itemImageSort"))
                                                storeDetailMenuImageData.setItemImageOrigin( if (!jsonImageObject.isNull("itemImageOrigin")) jsonImageObject.getString("itemImageOrigin") else "" )
                                                storeDetailMenuImageData.setItemImageName( if (!jsonImageObject.isNull("itemImageName")) jsonImageObject.getString("itemImageName") else "" )
                                                storeDetailMenuImageData.setIsUseYn(jsonImageObject.getBoolean("useYn"))
                                                storeDetailMenuImageData.setCreateDate( if (!jsonImageObject.isNull("createDate")) jsonImageObject.getString("createDate") else "" )
                                                storeDetailMenuImageData.setUpdateDate( if (!jsonImageObject.isNull("updateDate")) jsonImageObject.getString("updateDate") else "" )
                                                storeDetailMenuImageData.setItemUid(jsonImageObject.getInt("itemUID"))
                                            }

                                            storeDetailMenuImageListData.add(storeDetailMenuImageData)
                                        }

                                        storeDetailMenuData.setImageListData(storeDetailMenuImageListData)

                                        val jsonCurrencyArray = json.getJSONArray("currencyList")
                                        val storeDetailMenuCurrencyListData = ArrayList<StoreDetailMenuCurrencyData>()
                                        for (m in 0 until jsonCurrencyArray.length()) {
                                            val storeDetailMenuCurrencyData = StoreDetailMenuCurrencyData()
                                            val jsonCurrencyObject = jsonCurrencyArray.getJSONObject(m)
                                            if (null != jsonCurrencyObject) {
                                                storeDetailMenuCurrencyData.setItemUid(jsonCurrencyObject.getInt("itemUID"))
                                                storeDetailMenuCurrencyData.setCurrencyCode( if (!jsonCurrencyObject.isNull("currencyCode")) jsonCurrencyObject.getString("currencyCode") else "" )
                                                storeDetailMenuCurrencyData.setCurrencyPrice( if (!jsonCurrencyObject.isNull("currencyPrice")) jsonCurrencyObject.getDouble("currencyPrice") else 0.0 )
                                                storeDetailMenuCurrencyData.setCreateDate( if (!jsonCurrencyObject.isNull("createDate")) jsonCurrencyObject.getString("createDate") else "" )
                                                storeDetailMenuCurrencyData.setUpdateDate( if (!jsonCurrencyObject.isNull("updateDate")) jsonCurrencyObject.getString("updateDate") else "" )
                                            }

                                            storeDetailMenuCurrencyListData.add(storeDetailMenuCurrencyData)
                                        }
                                        storeDetailMenuData.setCurrencyListData(storeDetailMenuCurrencyListData)
                                    }
                                    storeDetailMenuListData.add(storeDetailMenuData)
                                }
                            }
                        }
                        listener.onSuccess(storeDetailMenuListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "Bearer $secureKey"
//                return params
//            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Store Review
     * GET
     * @param secureKey
     * @param pageNow
     * @param pageCount
     * @param companyUid
     */
    fun getStoreReviewData(requestQueue: RequestQueue, listener: RequestListener<ArrayList<StoreDetailReviewData>>,
                           secureKey: String, pageNow: Int, pageCount: Int, companyUid: String) {

        NagajaLog().d("wooks, API ========= getStoreReviewData : secureKey = $secureKey / pageNow = $pageNow / pageCount = $pageCount / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$STORE_REVIEW/$companyUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= STORE_REVIEW : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val reviewCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val storeDetailReviewListData = ArrayList<StoreDetailReviewData>()
                        for (i in 0 until jsonArray.length()) {
                            val storeDetailReviewData = StoreDetailReviewData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                storeDetailReviewData.setReviewUid(jsonObject.getInt("compReviewUID"))
                                storeDetailReviewData.setReviewStatus(jsonObject.getInt("compReviewStatus"))
                                storeDetailReviewData.setReviewSubject( if (!jsonObject.isNull("compReviewSubject")) jsonObject.getString("compReviewSubject") else "" )
                                storeDetailReviewData.setReviewContent( if (!jsonObject.isNull("compReviewContent")) jsonObject.getString("compReviewContent") else "" )
                                storeDetailReviewData.setCount(reviewCount)
                                storeDetailReviewData.setReviewViewCount(jsonObject.getInt("compReviewViewCount"))
                                storeDetailReviewData.setReviewViewPoint(jsonObject.getInt("compReviewPoint"))
                                storeDetailReviewData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                storeDetailReviewData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                storeDetailReviewData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                storeDetailReviewData.setCompanyUid(jsonObject.getInt("compUID"))
                                storeDetailReviewData.setMemberUid(jsonObject.getInt("memberUID"))
                                storeDetailReviewData.setMemberName( if (!jsonObject.isNull("memberNName")) jsonObject.getString("memberNName") else "" )
                                storeDetailReviewData.setMemberEmail( if (!jsonObject.isNull("memberID")) jsonObject.getString("memberID") else "" )
                                storeDetailReviewData.setProfileImageUrl( if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "" )
                                storeDetailReviewData.setIsRecommendYn(jsonObject.getBoolean("recommYn"))
                                storeDetailReviewData.setIsReportYn(jsonObject.getBoolean("reportYn"))

                                val jsonImageArray = jsonObject.getJSONArray("reviewImageList")
                                val storeDetailReviewImageListData = ArrayList<StoreDetailReviewImageData>()
                                for (k in 0 until jsonImageArray.length()) {
                                    val storeDetailReviewImageData = StoreDetailReviewImageData()
                                    val jsonImageObject = jsonImageArray.getJSONObject(k)
                                    if (null != jsonImageObject) {
                                        storeDetailReviewImageData.setReviewImageUid(jsonImageObject.getInt("reviewImageUID"))
                                        storeDetailReviewImageData.setCompanyReviewUid(jsonImageObject.getInt("compReviewUID"))
                                        storeDetailReviewImageData.setReviewSort(jsonImageObject.getInt("reviewImageSort"))
                                        storeDetailReviewImageData.setReviewImageOrigin( if (!jsonImageObject.isNull("reviewImageOrigin")) jsonImageObject.getString("reviewImageOrigin") else "" )
                                        storeDetailReviewImageData.setReviewImageName( if (!jsonImageObject.isNull("reviewImageName")) jsonImageObject.getString("reviewImageName") else "" )
                                        storeDetailReviewImageData.setIsUseYn(jsonImageObject.getBoolean("useYn"))
                                        storeDetailReviewImageData.setCreateDate( if (!jsonImageObject.isNull("createDate")) jsonImageObject.getString("createDate") else "" )
                                        storeDetailReviewImageData.setUpdateDate( if (!jsonImageObject.isNull("updateDate")) jsonImageObject.getString("updateDate") else "" )
                                    }

                                    storeDetailReviewImageListData.add(storeDetailReviewImageData)
                                }

                                storeDetailReviewData.setReviewImageListData(storeDetailReviewImageListData)
                            }

                            storeDetailReviewListData.add(storeDetailReviewData)
                        }
                        listener.onSuccess(storeDetailReviewListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (!TextUtils.isEmpty(secureKey)) {
                    params["Authorization"] = "Bearer $secureKey"
                }
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("pagenow", pageNow)
                jsonBody.put("pagesz", pageCount)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Store Review Upload
     * POST Array
     * @param secureKey
     * @param companyUid
     * @param title
     * @param content
     * @param starPoint
     * @param imageUploadListData
     */
    fun getStoreReviewUpload(requestQueue: RequestQueue, listener: RequestListener<String>,
                             secureKey: String, companyUid: String, title: String, content: String, starPoint: String,
                             imageUploadListData: ArrayList<ImageUploadData>) {

        NagajaLog().d("wooks, API ========= getStoreReviewUpload : secureKey = $secureKey / companyUid = $companyUid " +
                "/ title = $title / content = $content / starPoint = $starPoint / imageUploadListData = ${imageUploadListData.size}")

        val volleyMultipartArrayRequest: com.nagaja.app.android.Utils.VolleyMultipartArrayRequest = object : com.nagaja.app.android.Utils.VolleyMultipartArrayRequest(
            Method.POST,
            API_DOMAIN + STORE_REVIEW_UPLOAD_SAVE,
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= STORE_REVIEW_UPLOAD_SAVE : $it")

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["compreviewsubject"] = title
                params["compreviewcontent"] = content
                params["compuid"] = companyUid
                params["compreviewpoint"] = starPoint
                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val imageList: MutableMap<String, ArrayList<DataPart>> = HashMap()
                val dataPart: ArrayList<DataPart> = ArrayList()
                for (i in 0 until imageUploadListData.size) {
                    val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                    dataPart.add(dp)
                }
                imageList["images"] = dataPart
                return imageList
            }
        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)

    }

    /**
     * Training Result (Multi-Part)
     * POST Array
     * @param secureKey
     * @param companyUid
     * @param title
     * @param content
     * @param starPoint
     * @param imageUploadListData
     */
    fun getStoreReviewModify(requestQueue: RequestQueue, listener: RequestListener<String>,
                             secureKey: String, reviewUid: String, companyUid: String, memberUid: String, title: String, content: String,
                             starPoint: String, deleteImageUid: String,
                             imageUploadListData: ArrayList<ImageUploadData>) {

        NagajaLog().d("wooks, API ========= getStoreReviewModify : secureKey = $secureKey / reviewUid = $reviewUid / companyUid = $companyUid / memberUid = $memberUid " +
                "/ title = $title / content = $content / starPoint = $starPoint / deleteImageUid = $deleteImageUid / imageUploadListData = ${imageUploadListData.size}")

        val volleyMultipartArrayRequest: com.nagaja.app.android.Utils.VolleyMultipartArrayRequest = object : com.nagaja.app.android.Utils.VolleyMultipartArrayRequest(
            Method.PUT,
            "$API_DOMAIN$STORE_REVIEW_MODIFY/$reviewUid",
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= STORE_REVIEW_MODIFY : $it")

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["compreviewsubject"] = title
                params["compreviewcontent"] = content
                params["compuid"] = companyUid
                params["memberuid"] = memberUid
                params["compreviewpoint"] = starPoint
                params["delimageuid"] = deleteImageUid
                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val imageList: MutableMap<String, ArrayList<DataPart>> = HashMap()
                val dataPart: ArrayList<DataPart> = ArrayList()
                for (i in 0 until imageUploadListData.size) {
                    val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                    dataPart.add(dp)
                }
                imageList["images"] = dataPart
                return imageList
            }
        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)

    }

    /**
     * Get Store Review Delete
     * POST
     * @param secureKey
     * @param reviewUid
     * @param companyUid
     * @param isDelete
     */
    fun getStoreReviewDelete(requestQueue: RequestQueue, listener: RequestListener<String>,
                             secureKey: String, reviewUid: Int, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getStoreReviewDelete : secureKey = $secureKey / reviewUid = $reviewUid / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + STORE_REVIEW_DELETE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= STORE_REVIEW_DELETE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener?.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("compreviewuid", reviewUid)
                jsonBody.put("compuid", companyUid)
                jsonBody.put("useyn", false)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * get Report Upload (Multi-Part)
     * POST Array
     * @param secureKey
     * @param reportClassification
     * @param reportTypeUid
     * @param content
     * @param keyUid
     * @param companyUid
     * @param imageUploadListData
     */
    fun getReportUpload(requestQueue: RequestQueue, listener: RequestListener<String>,
                        secureKey: String, reportClassification: String, reportTypeUid: String, content: String, keyUid: String, companyUid: Int,
                        imageUploadListData: ArrayList<ImageUploadData>) {

        NagajaLog().d("wooks, API ========= getReportUpload : secureKey = $secureKey / reportClassification = $reportClassification " +
                "/ reportTypeUid = $reportTypeUid / content = $content / keyUid = $keyUid / companyUid = $companyUid / imageUploadListData = ${imageUploadListData.size}")

        val volleyMultipartArrayRequest: com.nagaja.app.android.Utils.VolleyMultipartArrayRequest = object : com.nagaja.app.android.Utils.VolleyMultipartArrayRequest(
            Method.POST,
            if (companyUid <= 0) API_DOMAIN + REPORT_UPLOAD
            else API_DOMAIN + COMPANY_REPORT_UPLOAD,
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= REPORT_UPLOAD : $it")

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["reportclassuid"] = reportClassification
                params["reporttypeuid"] = reportTypeUid
                params["reportcontent"] = content
                params["contentkeyuid"] = keyUid
                if (companyUid > 0) {
                    params["compuid"] = companyUid.toString()
                }
                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val imageList: MutableMap<String, ArrayList<DataPart>> = HashMap()
                val dataPart: ArrayList<DataPart> = ArrayList()
                for (i in 0 until imageUploadListData.size) {
                    val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                    dataPart.add(dp)
                }
                imageList["images"] = dataPart
                return imageList
            }
        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)

    }

    /**
     * Get Review Recommend Save
     * POST
     * @param secureKey
     * @param reviewUid
     * @param recommend
     */
    fun getReviewRecommendSave(requestQueue: RequestQueue, listener: RequestListener<String>,
                               secureKey: String, reviewUid: Int, recommend: Int) {

        NagajaLog().d("wooks, API ========= getReviewRecommendSave : secureKey = $secureKey / reviewUid = $reviewUid / recommend = $recommend")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + REVIEW_RECOMMEND_SAVE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= REVIEW_RECOMMEND_SAVE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener?.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("compreviewuid", reviewUid)
                jsonBody.put("useyn", recommend)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Store List Data
     * POST
     * @param secureKey
     * @param pageNow
     * @param pageCount
     * @param searchType
     * @param search
     * @param nationCode
     * @param categoryUid
     * @param latitude
     * @param longitude
     * @param gugunUid
     */
    fun getUsedMarketListData(requestQueue: RequestQueue, listener: RequestListener<ArrayList<UsedMarketData>>,
                              secureKey: String, pageNow: Int, pageCount: Int, searchType: Int, search: String,
                              nationCode:String, categoryUid: Int, latitude: String, longitude: String, gugunUid: Int) {

        NagajaLog().d("wooks, API ========= getUsedMarketListData : pageNow = $pageNow / pageCount = $pageCount / searchType = $searchType / companySearch = $search" +
                "/ nationCode = $nationCode / categoryUid = $categoryUid / latitude = $latitude / longitude = $longitude / gugunUid = $gugunUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + USED_MARKET_LIST,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= USED_MARKET_LIST : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val usedMarketListData = ArrayList<UsedMarketData>()

                        for (i in 0 until jsonArray.length()) {
                            val usedMarketData = UsedMarketData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                usedMarketData.setTotalCount(json.getInt("tcount"))
                                usedMarketData.setItemUid(jsonObject.getInt("itemUID"))
                                usedMarketData.setItemStatus(jsonObject.getInt("itemStatus"))
                                usedMarketData.setItemSubject( if (!jsonObject.isNull("itemSubject")) jsonObject.getString("itemSubject") else "" )
                                usedMarketData.setItemName( if (!jsonObject.isNull("itemName")) jsonObject.getString("itemName") else "" )
                                usedMarketData.setItemContent( if (!jsonObject.isNull("itemContent")) jsonObject.getString("itemContent") else "" )
                                usedMarketData.setItemCurrencyCode( if (!jsonObject.isNull("itemCurrencyCode")) jsonObject.getString("itemCurrencyCode") else "" )
                                usedMarketData.setItemPrice(if (!jsonObject.isNull("itemPrice")) jsonObject.getDouble("itemPrice") else 0.0)
                                usedMarketData.setItemViewCount(jsonObject.getInt("itemViewCount"))
                                usedMarketData.setItemChatCount(jsonObject.getInt("itemChatCount"))
                                usedMarketData.setItemInterestCount(jsonObject.getInt("itemInterestCount"))
                                usedMarketData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                usedMarketData.setCreateDate(if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                usedMarketData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                usedMarketData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                usedMarketData.setCategoryName( if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "" )
                                usedMarketData.setCompanyUid(jsonObject.getInt("compUID"))
                                usedMarketData.setMemberUid(jsonObject.getInt("memberUID"))
                                usedMarketData.setMemberEmail( if (!jsonObject.isNull("memberID")) jsonObject.getString("memberID") else "" )

                                val jsonImageArray = jsonObject.getJSONArray("imageList")
                                val usedMarketImageListData = ArrayList<UsedMarketImageData>()
                                for (k in 0 until jsonImageArray.length()) {
                                    val usedMarketImageData = UsedMarketImageData()
                                    val jsonImageObject = jsonImageArray.getJSONObject(k)
                                    if (null != jsonImageObject) {
                                        usedMarketImageData.setItemImageUid(jsonImageObject.getInt("itemImageUID"))
                                        usedMarketImageData.setItemImageSort(jsonImageObject.getInt("itemImageSort"))
                                        usedMarketImageData.setItemImageOrigin( if (!jsonImageObject.isNull("itemImageOrigin")) jsonImageObject.getString("itemImageOrigin") else "" )
                                        usedMarketImageData.setItemImageName( if (!jsonImageObject.isNull("itemImageName")) jsonImageObject.getString("itemImageName") else "" )
                                        usedMarketImageData.setIsUseYn(jsonImageObject.getBoolean("useYn"))
                                        usedMarketImageData.setCreateDate( if (!jsonImageObject.isNull("createDate")) jsonImageObject.getString("createDate") else "" )
                                        usedMarketImageData.setUpdateDate( if (!jsonImageObject.isNull("updateDate")) jsonImageObject.getString("updateDate") else "" )
                                        usedMarketImageData.setItemImageUid(jsonImageObject.getInt("itemUID"))
                                    }

                                    usedMarketImageListData.add(usedMarketImageData)
                                }

                                usedMarketData.setImageListData(usedMarketImageListData)

                                val jsonCurrencyArray = jsonObject.getJSONArray("currencyList")
                                val usedMarketCurrencyListData = ArrayList<UsedMarketCurrencyData>()
                                for (j in 0 until jsonCurrencyArray.length()) {
                                    val usedMarketCurrencyData = UsedMarketCurrencyData()
                                    val jsonCurrencyObject = jsonCurrencyArray.getJSONObject(j)
                                    if (null != jsonCurrencyObject) {
                                        usedMarketCurrencyData.setItemUid(jsonCurrencyObject.getInt("itemUID"))
                                        usedMarketCurrencyData.setCurrencyCode(if (!jsonCurrencyObject.isNull("currencyCode")) jsonCurrencyObject.getString("currencyCode") else "")
                                        usedMarketCurrencyData.setCurrencyPrice(if (!jsonCurrencyObject.isNull("currencyPrice")) jsonCurrencyObject.getDouble("currencyPrice") else 0.0)
                                        usedMarketCurrencyData.setCreateDate(if (!jsonCurrencyObject.isNull("createDate")) jsonCurrencyObject.getString("createDate") else "")
                                        usedMarketCurrencyData.setUpdateDate(if (!jsonCurrencyObject.isNull("updateDate")) jsonCurrencyObject.getString("updateDate") else "")
                                    }

                                    usedMarketCurrencyListData.add(usedMarketCurrencyData)
                                }

                                usedMarketData.setCurrencyListData(usedMarketCurrencyListData)


                                usedMarketData.setItemLocationUid(jsonObject.getInt("itemLocationUID"))
                                usedMarketData.setLatitude( if (!jsonObject.isNull("locationLatitude")) jsonObject.getString("locationLatitude").toString() else "" )
                                usedMarketData.setLongitude( if (!jsonObject.isNull("locationLongitude")) jsonObject.getString("locationLongitude").toString() else "" )
                                usedMarketData.setSiDoUid(jsonObject.getInt("locationSiDoUID"))
                                usedMarketData.setSiDoName( if (!jsonObject.isNull("locationSiDo")) jsonObject.getString("locationSiDo") else "" )
                                usedMarketData.setGuGunUid(jsonObject.getInt("locationGuGunUID"))
                                usedMarketData.setGuGunName( if (!jsonObject.isNull("locationGuGun")) jsonObject.getString("locationGuGun") else "" )
                                usedMarketData.setLocationDetail( if (!jsonObject.isNull("locationDetail")) jsonObject.getString("locationDetail") else "" )
                                usedMarketData.setSellerImageOrigin( if (!jsonObject.isNull("sellerImageOrigin")) jsonObject.getString("sellerImageOrigin") else "" )
                                usedMarketData.setSellerImageName( if (!jsonObject.isNull("sellerImageName")) jsonObject.getString("sellerImageName") else "" )
                                usedMarketData.setSellerName( if (!jsonObject.isNull("sellerNName")) jsonObject.getString("sellerNName") else "" )
                                usedMarketData.setIsRecommendYn(jsonObject.getBoolean("recommYn"))
                            }

                            usedMarketListData.add(usedMarketData)
                        }

                        listener.onSuccess(usedMarketListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (!TextUtils.isEmpty(secureKey)) {
                    params["Authorization"] = "Bearer $secureKey"
                }
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["pagenow"] = pageNow.toString()
                params["pagesz"] = pageCount.toString()
                params["sr"] = searchType.toString()
                params["srtxt"] = search
                params["nuid"] = nationCode
                params["cago"] = categoryUid.toString()
                params["lati"] = latitude
                params["long"] = longitude
                params["gugunuid"] = gugunUid.toString()

                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Used Market Recommend Save
     * POST
     * @param secureKey
     * @param itemUid
     * @param isRecommend
     */
    fun getUsedMarketRecommendSave(requestQueue: RequestQueue, listener: RequestListener<String>,
                                   secureKey: String, itemUid: Int, isRecommend: Int) {

        NagajaLog().d("wooks, API ========= getUsedMarketRecommendSave : secureKey = $secureKey / itemUid = $itemUid / isRecommend = $isRecommend")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + USED_MARKET_RECOMMEND_SAVE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= USED_MARKET_RECOMMEND_SAVE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("itemuid", itemUid)
                jsonBody.put("useyn", isRecommend)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Location Area
     * GET
     * @param secureKey
     * @param itemUid
     * @param nationCode
     */
    fun getUsedMarketDetail(requestQueue: RequestQueue, listener: RequestListener<UsedMarketData>,
                            secureKey: String, itemUid: Int, nationCode: String) {

        NagajaLog().d("wooks, API ========= getUsedMarketDetail : secureKey = $secureKey / itemUid: = $itemUid: / nationCode = $nationCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$USED_MARKET_DETAIL/$itemUid?nuid=$nationCode",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= USED_MARKET_DETAIL : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val usedMarketData = UsedMarketData()

                        usedMarketData.setTotalCount(json.getInt("tcount"))
                        usedMarketData.setItemUid(jsonObject.getInt("itemUID"))
                        usedMarketData.setItemStatus(jsonObject.getInt("itemStatus"))
                        usedMarketData.setItemSubject(if (!jsonObject.isNull("itemSubject")) jsonObject.getString("itemSubject") else "")
                        usedMarketData.setItemName(if (!jsonObject.isNull("itemName")) jsonObject.getString("itemName") else "")
                        usedMarketData.setItemContent(if (!jsonObject.isNull("itemContent")) jsonObject.getString("itemContent") else "")
                        usedMarketData.setItemCurrencyCode(if (!jsonObject.isNull("itemCurrencyCode")) jsonObject.getString("itemCurrencyCode") else "")
                        usedMarketData.setItemPrice(if (!jsonObject.isNull("itemPrice")) jsonObject.getDouble("itemPrice") else 0.0)
                        usedMarketData.setItemViewCount(jsonObject.getInt("itemViewCount"))
                        usedMarketData.setItemChatCount(jsonObject.getInt("itemChatCount"))
                        usedMarketData.setItemInterestCount(jsonObject.getInt("itemInterestCount"))
                        usedMarketData.setIsUseYn(jsonObject.getBoolean("useYn"))
                        usedMarketData.setCreateDate(if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "")
                        usedMarketData.setUpdateDate(if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "")
                        usedMarketData.setCategoryUid(jsonObject.getInt("cagoUID"))
                        usedMarketData.setCategoryName(if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "")
                        usedMarketData.setCompanyUid(jsonObject.getInt("compUID"))
                        usedMarketData.setMemberUid(jsonObject.getInt("memberUID"))
                        usedMarketData.setMemberEmail(if (!jsonObject.isNull("memberID")) jsonObject.getString("memberID") else "")

                        val jsonImageArray = jsonObject.getJSONArray("imageList")
                        val usedMarketImageListData = ArrayList<UsedMarketImageData>()
                        for (k in 0 until jsonImageArray.length()) {
                            val usedMarketImageData = UsedMarketImageData()
                            val jsonImageObject = jsonImageArray.getJSONObject(k)
                            if (null != jsonImageObject) {
                                usedMarketImageData.setItemImageUid(jsonImageObject.getInt("itemImageUID"))
                                usedMarketImageData.setItemImageSort(jsonImageObject.getInt("itemImageSort"))
                                usedMarketImageData.setItemImageOrigin(if (!jsonImageObject.isNull("itemImageOrigin")) jsonImageObject.getString("itemImageOrigin") else "")
                                usedMarketImageData.setItemImageName(if (!jsonImageObject.isNull("itemImageName")) jsonImageObject.getString("itemImageName") else "")
                                usedMarketImageData.setIsUseYn(jsonImageObject.getBoolean("useYn"))
                                usedMarketImageData.setCreateDate(if (!jsonImageObject.isNull("createDate")) jsonImageObject.getString("createDate") else "")
                                usedMarketImageData.setUpdateDate(if (!jsonImageObject.isNull("updateDate")) jsonImageObject.getString("updateDate") else "")
                                usedMarketImageData.setItemUid(jsonImageObject.getInt("itemUID"))
                            }

                            usedMarketImageListData.add(usedMarketImageData)
                        }

                        usedMarketData.setImageListData(usedMarketImageListData)

                        val jsonCurrencyArray = jsonObject.getJSONArray("currencyList")
                        val usedMarketCurrencyListData = ArrayList<UsedMarketCurrencyData>()
                        for (j in 0 until jsonCurrencyArray.length()) {
                            val usedMarketCurrencyData = UsedMarketCurrencyData()
                            val jsonCurrencyObject = jsonCurrencyArray.getJSONObject(j)
                            if (null != jsonCurrencyObject) {
                                usedMarketCurrencyData.setItemUid(jsonCurrencyObject.getInt("itemUID"))
                                usedMarketCurrencyData.setCurrencyCode(if (!jsonCurrencyObject.isNull("currencyCode")) jsonCurrencyObject.getString("currencyCode") else "")
                                usedMarketCurrencyData.setCurrencyPrice(if (!jsonCurrencyObject.isNull("currencyPrice")) jsonCurrencyObject.getDouble("currencyPrice") else 0.0)
                                usedMarketCurrencyData.setCreateDate(if (!jsonCurrencyObject.isNull("createDate")) jsonCurrencyObject.getString("createDate") else "")
                                usedMarketCurrencyData.setUpdateDate(if (!jsonCurrencyObject.isNull("updateDate")) jsonCurrencyObject.getString("updateDate") else "")
                            }

                            usedMarketCurrencyListData.add(usedMarketCurrencyData)
                        }

                        usedMarketData.setCurrencyListData(usedMarketCurrencyListData)


                        usedMarketData.setItemLocationUid(jsonObject.getInt("itemLocationUID"))
                        usedMarketData.setLatitude(if (!jsonObject.isNull("locationLatitude")) jsonObject.getString("locationLatitude").toString() else "")
                        usedMarketData.setLongitude(if (!jsonObject.isNull("locationLongitude")) jsonObject.getString("locationLongitude").toString() else "")
                        usedMarketData.setSiDoUid(jsonObject.getInt("locationSiDoUID"))
                        usedMarketData.setSiDoName(if (!jsonObject.isNull("locationSiDo")) jsonObject.getString("locationSiDo") else "")
                        usedMarketData.setGuGunUid(jsonObject.getInt("locationGuGunUID"))
                        usedMarketData.setGuGunName(if (!jsonObject.isNull("locationGuGun")) jsonObject.getString("locationGuGun") else "")
                        usedMarketData.setLocationDetail(if (!jsonObject.isNull("locationDetail")) jsonObject.getString("locationDetail") else "")
                        usedMarketData.setSellerImageOrigin(if (!jsonObject.isNull("sellerImageOrigin")) jsonObject.getString("sellerImageOrigin") else "")
                        usedMarketData.setSellerImageName(if (!jsonObject.isNull("sellerImageName")) jsonObject.getString("sellerImageName") else "")
                        usedMarketData.setSellerName(if (!jsonObject.isNull("sellerNName")) jsonObject.getString("sellerNName") else "")
                        usedMarketData.setIsRecommendYn(jsonObject.getBoolean("recommYn"))

                        listener.onSuccess(usedMarketData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (!TextUtils.isEmpty(secureKey)) {
                    params["Authorization"] = "Bearer $secureKey"
                }
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Location Area
     * GET
     * @param secureKey
     * @param pageNow
     * @param pageCount
     */
    fun getCurrencyData(requestQueue: RequestQueue, listener: RequestListener<ArrayList<CurrencyData>>,
                        secureKey: String, pageNow: Int, pageCount: Int) {

        NagajaLog().d("wooks, API ========= getCurrencyData : secureKey = $secureKey / pageNow: = $pageNow: / pageCount = $pageCount")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$CURRENCY_DATA/?pagenow=$pageNow&pagesz=$pageCount",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= CURRENCY_DATA : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val currencyListData = ArrayList<CurrencyData>()

                        for (i in 0 until jsonArray.length()) {
                            val currencyData = CurrencyData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                currencyData.setCurrencyUid(jsonObject.getInt("currencyUID"))
                                currencyData.setCurrencyCode( if (!jsonObject.isNull("currencyCode")) jsonObject.getString("currencyCode") else "" )
                                currencyData.setCurrencyName( if (!jsonObject.isNull("currencyName")) jsonObject.getString("currencyName") else "" )
                                currencyData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                currencyData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                currencyData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                            }

                            currencyListData.add(currencyData)
                        }

                        listener.onSuccess(currencyListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Used Market Register (Multi-Part)
     * POST Array
     * @param secureKey
     * @param itemSubject
     * @param itemName
     * @param itemContent
     * @param categoryUid
     * @param defaultCurrencyCode
     * @param price
     * @param memberUid
     * @param currencyCodeKrw
     * @param currencyCodeUsd
     * @param currencyCodePhp
     * @param currencyCodeCny
     * @param currencyCodeJpy
     * @param latitude
     * @param longitude
     * @param mainAreaUid
     * @param mainAreaName
     * @param subAreaUid
     * @param subAreaName
     * @param locationDetail
     * @param imageUploadListData
     * @param companyUid
     */
    fun getUsedMarketRegister(requestQueue: RequestQueue, listener: RequestListener<String>,
                              secureKey: String, itemSubject: String, itemName: String, itemContent: String, categoryUid: Int, defaultCurrencyCode: String,
                              price: String, memberUid: Int, currencyCodeKrw: String, currencyCodeUsd: String, currencyCodePhp: String, currencyCodeCny: String, currencyCodeJpy: String,
                              latitude: String, longitude: String, mainAreaUid: Int, mainAreaName: String, subAreaUid: Int, subAreaName: String, locationDetail: String,
                              imageUploadListData: ArrayList<ImageUploadData>, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getUsedMarketRegister : secureKey = $secureKey / itemSubject = $itemSubject / itemName = $itemName / itemContent = $itemContent " +
                "/ categoryUid = $categoryUid / defaultCurrencyCode = $defaultCurrencyCode / price = $price  / memberUid = $memberUid " +
                "/ currencyCodeKrw = $currencyCodeKrw / currencyCodeUsd = $currencyCodeUsd / currencyCodePhp = $currencyCodePhp / currencyCodeCny = $currencyCodeCny " +
                "/ currencyCodeJpy = $currencyCodeJpy / latitude = $latitude / longitude = $longitude / mainAreaUid = $mainAreaUid / mainAreaName = $mainAreaName" +
                "/ subAreaUid = $subAreaUid / subAreaName = $subAreaName / locationDetail = $locationDetail / imageUploadListData = ${imageUploadListData.size} / companyUid = $companyUid")

        val volleyMultipartArrayRequest: com.nagaja.app.android.Utils.VolleyMultipartArrayRequest = object : com.nagaja.app.android.Utils.VolleyMultipartArrayRequest(
            Method.POST,
            if (companyUid <= 0) API_DOMAIN + USED_MARKET_REGISTER
            else API_DOMAIN + COMPANY_USED_MARKET_REGISTER,
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= USED_MARKET_REGISTER : $it")

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["itemsubject"] = itemSubject
                params["itemname"] = itemName
                params["itemcontent"] = itemContent
                params["cagouid"] = categoryUid.toString()
                params["itemcurrencycode"] = defaultCurrencyCode
                params["itemprice"] = price
                if (companyUid > 0) {
                    params["compuid"] = companyUid.toString()
                }
                params["memberuid"] = memberUid.toString()
                params["currencycodekrw"] = currencyCodeKrw
                params["currencycodeusd"] = currencyCodeUsd
                params["currencycodephp"] = currencyCodePhp
                params["currencycodecny"] = currencyCodeCny
                params["currencycodejpy"] = currencyCodeJpy
                params["locationlatitude"] = latitude
                params["locationlongitude"] = longitude
                params["locationsidouid"] = mainAreaUid.toString()
                params["locationsido"] = mainAreaName
                params["locationgugunuid"] = subAreaUid.toString()
                params["locationgugun"] = subAreaName
                params["locationdetail"] = locationDetail

                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val imageList: MutableMap<String, ArrayList<DataPart>> = HashMap()
                val dataPart: ArrayList<DataPart> = ArrayList()
                for (i in 0 until imageUploadListData.size) {
                    val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                    dataPart.add(dp)
                }
                imageList["images"] = dataPart
                return imageList
            }

        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)

    }

    /**
     * Get Used Market Register (Multi-Part)
     * POST Array
     * @param secureKey
     * @param itemSubject
     * @param itemName
     * @param itemContent
     * @param categoryUid
     * @param defaultCurrencyCode
     * @param price
     * @param memberUid
     * @param currencyCodeKrw
     * @param currencyCodeUsd
     * @param currencyCodePhp
     * @param currencyCodeCny
     * @param currencyCodeJpy
     * @param latitude
     * @param longitude
     * @param mainAreaUid
     * @param mainAreaName
     * @param subAreaUid
     * @param subAreaName
     * @param locationDetail
     * @param imageUploadListData
     * @param itemUid
     * @param deleteImageUid
     * @param companyUid
     */
    fun getUsedMarketRegisterModify(requestQueue: RequestQueue, listener: RequestListener<String>,
                                    secureKey: String, itemSubject: String, itemName: String, itemContent: String, categoryUid: Int, defaultCurrencyCode: String,
                                    price: String, memberUid: Int, currencyCodeKrw: String, currencyCodeUsd: String, currencyCodePhp: String, currencyCodeCny: String, currencyCodeJpy: String,
                                    latitude: String, longitude: String, mainAreaUid: Int, mainAreaName: String, subAreaUid: Int, subAreaName: String, locationDetail: String,
                                    imageUploadListData: ArrayList<ImageUploadData>, itemUid: String, deleteImageUid: String, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getUsedMarketRegisterModify : secureKey = $secureKey / itemSubject = $itemSubject / itemName = $itemName / itemContent = $itemContent " +
                "/ categoryUid = $categoryUid / defaultCurrencyCode = $defaultCurrencyCode / price = $price  / memberUid = $memberUid " +
                "/ currencyCodeKrw = $currencyCodeKrw / currencyCodeUsd = $currencyCodeUsd / currencyCodePhp = $currencyCodePhp / currencyCodeCny = $currencyCodeCny " +
                "/ currencyCodeJpy = $currencyCodeJpy / latitude = $latitude / longitude = $longitude / mainAreaUid = $mainAreaUid / mainAreaName = $mainAreaName" +
                "/ subAreaUid = $subAreaUid / subAreaName = $subAreaName / locationDetail = $locationDetail / imageUploadListData = ${imageUploadListData.size}" +
                "/ itemUid = $itemUid / deleteImageUid = $deleteImageUid / companyUid = $companyUid")

        val volleyMultipartArrayRequest: com.nagaja.app.android.Utils.VolleyMultipartArrayRequest = object : com.nagaja.app.android.Utils.VolleyMultipartArrayRequest(
            Method.PUT,
            if (companyUid <= 0) "$API_DOMAIN$USED_MARKET_REGISTER_MODIFY/$itemUid"
            else "$API_DOMAIN$COMPANY_USED_MARKET_REGISTER_MODIFY/$itemUid",
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= USED_MARKET_REGISTER_MODIFY : $it")

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["itemsubject"] = itemSubject
                params["itemname"] = itemName
                params["itemcontent"] = itemContent
                params["cagouid"] = categoryUid.toString()
                params["itemcurrencycode"] = defaultCurrencyCode
                params["itemprice"] = price
                params["memberuid"] = memberUid.toString()
                params["currencycodekrw"] = currencyCodeKrw
                params["currencycodeusd"] = currencyCodeUsd
                params["currencycodephp"] = currencyCodePhp
                params["currencycodecny"] = currencyCodeCny
                params["currencycodejpy"] = currencyCodeJpy
                params["locationlatitude"] = latitude
                params["locationlongitude"] = longitude
                params["locationsidouid"] = mainAreaUid.toString()
                params["locationsido"] = mainAreaName
                params["locationgugunuid"] = subAreaUid.toString()
                params["locationgugun"] = subAreaName
                params["locationdetail"] = locationDetail
                params["delimageuid"] = deleteImageUid

                if (companyUid > 0) {
                    params["compuid"] = companyUid.toString()
                }

                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val imageList: MutableMap<String, ArrayList<DataPart>> = HashMap()
                val dataPart: ArrayList<DataPart> = ArrayList()
                for (i in 0 until imageUploadListData.size) {
                    val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                    dataPart.add(dp)
                }
                imageList["images"] = dataPart
                return imageList
            }

        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)

    }

    /**
     * Get Delete My Used Market
     * POST
     * @param secureKey
     * @param itemUid
     * @param companyUid
     */
    fun getDeleteMyUsedMarket(requestQueue: RequestQueue, listener: RequestListener<String>,
                              secureKey: String, itemUid: Int, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getDeleteMyUsedMarket : secureKey = $secureKey / itemUid = $itemUid / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            if (companyUid <= 0) API_DOMAIN + USED_MARKET_DELETE
            else {
                NagajaLog().d("wooks, API ========= getDeleteMyUsedMarket : $API_DOMAIN$COMPANY_USED_MARKET_DELETE/$companyUid")
                "$API_DOMAIN$COMPANY_USED_MARKET_DELETE/$companyUid"
            },
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= USED_MARKET_DELETE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener?.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["itemuid"] = itemUid.toString()
                if (companyUid > 0) {
                    params["compuid"] = companyUid.toString()
                }
                params["useyn"] = "false"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Notice List
     * GET
     * @param secureKey
     * @param pageNow
     * @param pageCount
     * @param companyUid
     */
    fun getNoticeList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<NoticeData>>,
                      secureKey: String, pageNow: Int, pageCount: Int, searchWord: String, sortType: Int, categoryUid: Int, subAreaUid: Int, nationUid: String) {

        val searchWordType = if (!TextUtils.isEmpty(searchWord)) {
            1
        } else {
            0
        }

        NagajaLog().d("wooks, API ========= getLocalNews : secureKey = $secureKey / pageNow = $pageNow / pageCount = $pageCount / sortType = $sortType / categoryUid = $categoryUid / subAreaUid = $subAreaUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + NOTICE_LIST,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTICE (LOCAL NEWS) : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val totalCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val noticeListData = ArrayList<NoticeData>()
                        for (i in 0 until jsonArray.length()) {
                            val noticeData = NoticeData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                noticeData.setTotalCount(totalCount)
                                noticeData.setNoticeUid(jsonObject.getInt("noticeUID"))
                                noticeData.setNoticeStatus(jsonObject.getInt("noticeStatus"))
                                noticeData.setNoticeSubject( if (!jsonObject.isNull("noticeSubject")) jsonObject.getString("noticeSubject") else "" )
                                noticeData.setNoticeContent( if (!jsonObject.isNull("noticeContent")) jsonObject.getString("noticeContent") else "" )
                                noticeData.setViewCount(jsonObject.getInt("viewCount"))
                                noticeData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                noticeData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                noticeData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                noticeData.setMemberUid(jsonObject.getInt("memberUID"))
                                noticeData.setMemberName( if (!jsonObject.isNull("memberNName")) jsonObject.getString("memberNName") else "" )
                                noticeData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                noticeData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                                noticeData.setCategoryName( if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "" )
                                noticeData.setParentCategoryName( if (!jsonObject.isNull("parentCagoName")) jsonObject.getString("parentCagoName") else "" )
                                noticeData.setRecommendCount(jsonObject.getInt("recommCount"))
                                noticeData.setCommentCount(jsonObject.getInt("commentCount"))
                                noticeData.setLocationNationUid(jsonObject.getInt("locationNationUID"))
                                noticeData.setMainAreaUid(jsonObject.getInt("locationSiDoUID"))
                                noticeData.setSubAreaUid(jsonObject.getInt("locationGuGunUID"))
                                noticeData.setMainAreaName( if (!jsonObject.isNull("locationSiDo")) jsonObject.getString("locationSiDo") else "" )
                                noticeData.setSubAreaName( if (!jsonObject.isNull("locationGuGun")) jsonObject.getString("locationGuGun") else "" )

                                val jsonImageArray = jsonObject.getJSONArray("noticeImg")
                                val imageListData = ArrayList<UsedMarketImageData>()
                                for (k in 0 until jsonImageArray.length()) {
                                    val imageData = UsedMarketImageData()
                                    val jsonImageObject = jsonImageArray.getJSONObject(k)
                                    if (null != jsonImageObject) {
                                        imageData.setItemImageUid(jsonImageObject.getInt("noticeImageUID"))
                                        imageData.setItemImageSort(jsonImageObject.getInt("noticeImageSort"))
                                        imageData.setItemImageOrigin( if (!jsonImageObject.isNull("noticeImageOrigin")) jsonImageObject.getString("noticeImageOrigin") else "" )
                                        imageData.setItemImageName( if (!jsonImageObject.isNull("noticeImageName")) jsonImageObject.getString("noticeImageName") else "" )
                                        imageData.setIsUseYn(jsonImageObject.getBoolean("useYn"))
                                        imageData.setCreateDate( if (!jsonImageObject.isNull("createDate")) jsonImageObject.getString("createDate") else "" )
                                        imageData.setUpdateDate( if (!jsonImageObject.isNull("updateDate")) jsonImageObject.getString("updateDate") else "" )
                                        imageData.setItemUid(jsonImageObject.getInt("noticeUID"))
                                    }

                                    imageListData.add(imageData)
                                }

                                noticeData.setNoticeImageListData(imageListData)
                            }

                            noticeListData.add(noticeData)
                        }
                        listener.onSuccess(noticeListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (!TextUtils.isEmpty(secureKey)) {
                    params["Authorization"] = "Bearer $secureKey"
                }
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["pagenow"] = pageNow.toString()
                params["pagesz"] = pageCount.toString()
                params["sr"] = searchWordType.toString()
                params["srtxt"] = searchWord
                params["srstatus"] = ""
                params["srbegindate"] = ""
                params["srenddate"] = ""
                params["sort"] = sortType.toString()
                params["cago"] = categoryUid.toString()
                params["mode"] = ""
                params["gugunuid"] = subAreaUid.toString()
                params["nuid"] = nationUid

                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Notice Detail
     * GET
     * @param secureKey
     * @param itemUid
     */
    fun getNoticeDetail(requestQueue: RequestQueue, listener: RequestListener<NoticeData>,
                        secureKey: String, itemUid: Int) {

        NagajaLog().d("wooks, API ========= getNoticeDetail : secureKey = $secureKey / itemUid = $itemUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$NOTICE_DETAIL/$itemUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTICE_DETAIL : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val noticeData = NoticeData()
                        if (null != jsonObject) {
                            noticeData.setNoticeUid(jsonObject.getInt("noticeUID"))
                            noticeData.setNoticeStatus(jsonObject.getInt("noticeStatus"))
                            noticeData.setNoticeSubject(if (!jsonObject.isNull("noticeSubject")) jsonObject.getString("noticeSubject") else "")
                            noticeData.setNoticeContent(if (!jsonObject.isNull("noticeContent")) jsonObject.getString("noticeContent") else "")
                            noticeData.setViewCount(jsonObject.getInt("viewCount"))
                            noticeData.setIsUseYn(jsonObject.getBoolean("useYn"))
                            noticeData.setCreateDate(if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "")
                            noticeData.setUpdateDate(if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "")
                            noticeData.setMemberUid(jsonObject.getInt("memberUID"))
                            noticeData.setMemberName(if (!jsonObject.isNull("memberNName")) jsonObject.getString("memberNName") else "")
                            noticeData.setCategoryUid(jsonObject.getInt("cagoUID"))
                            noticeData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                            noticeData.setCategoryName(if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "")
                            noticeData.setParentCategoryName(if (!jsonObject.isNull("parentCagoName")) jsonObject.getString("parentCagoName") else "")
                            noticeData.setRecommendCount(jsonObject.getInt("recommCount"))
                            noticeData.setCommentCount(jsonObject.getInt("commentCount"))
                            noticeData.setLocationNationUid(jsonObject.getInt("locationNationUID"))
                            noticeData.setMainAreaUid(jsonObject.getInt("locationSiDoUID"))
                            noticeData.setSubAreaUid(jsonObject.getInt("locationGuGunUID"))
                            noticeData.setMainAreaName(if (!jsonObject.isNull("locationSiDo")) jsonObject.getString("locationSiDo") else "")
                            noticeData.setSubAreaName(if (!jsonObject.isNull("locationGuGun")) jsonObject.getString("locationGuGun") else "")

                            val jsonImageArray = jsonObject.getJSONArray("noticeImg")
                            val imageListData = ArrayList<UsedMarketImageData>()
                            for (k in 0 until jsonImageArray.length()) {
                                val imageData = UsedMarketImageData()
                                val jsonImageObject = jsonImageArray.getJSONObject(k)
                                if (null != jsonImageObject) {
                                    imageData.setItemImageUid(jsonImageObject.getInt("noticeImageUID"))
                                    imageData.setItemImageSort(jsonImageObject.getInt("noticeImageSort"))
                                    imageData.setItemImageOrigin(if (!jsonImageObject.isNull("noticeImageOrigin")) jsonImageObject.getString("noticeImageOrigin") else "")
                                    imageData.setItemImageName(if (!jsonImageObject.isNull("noticeImageName")) jsonImageObject.getString("noticeImageName") else "")
                                    imageData.setIsUseYn(jsonImageObject.getBoolean("useYn"))
                                    imageData.setCreateDate(if (!jsonImageObject.isNull("createDate")) jsonImageObject.getString("createDate") else "")
                                    imageData.setUpdateDate(if (!jsonImageObject.isNull("updateDate")) jsonImageObject.getString("updateDate") else "")
                                    imageData.setItemUid(jsonImageObject.getInt("noticeUID"))
                                }

                                imageListData.add(imageData)
                            }

                            noticeData.setNoticeImageListData(imageListData)
                        }
                        listener.onSuccess(noticeData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (!TextUtils.isEmpty(secureKey)) {
                    params["Authorization"] = "Bearer $secureKey"
                }
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Notice Comment
     * GET
     * @param secureKey
     * @param noticeUid
     */
    fun getNoticeComment(requestQueue: RequestQueue, listener: RequestListener<ArrayList<CommentData>>,
                         secureKey: String, noticeUid: Int, pageNow: Int, pageCount: Int) {

        NagajaLog().d("wooks, API ========= getNoticeComment : secureKey = $secureKey / noticeUid = $noticeUid / pageNow = $pageNow / pageCount = $pageCount")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$NOTICE_COMMENT/$noticeUid?pagenow=$pageNow&pagesz=$pageCount",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTICE_COMMENT : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val totalCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val commentListData = ArrayList<CommentData>()
                        for (i in 0 until jsonArray.length()) {
                            val commentData = CommentData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                commentData.setTotalCount(totalCount)
                                commentData.setCommentUid(jsonObject.getInt("commentUID"))
                                commentData.setCommentStatus(jsonObject.getInt("commentStatus"))
                                commentData.setComment( if (!jsonObject.isNull("comment")) jsonObject.getString("comment") else "" )
                                commentData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                commentData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                commentData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                commentData.setMemberUid(jsonObject.getInt("memberUID"))
                                commentData.setMemberName( if (!jsonObject.isNull("memberName")) jsonObject.getString("memberName") else "" )
                                commentData.setNoticeUid(jsonObject.getInt("noticeUID"))
                                commentData.setMemberNickName( if (!jsonObject.isNull("memNName")) jsonObject.getString("memNName") else "" )
                            }

                            commentListData.add(commentData)
                        }
                        listener.onSuccess(commentListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (!TextUtils.isEmpty(secureKey)) {
                    params["Authorization"] = "Bearer $secureKey"
                }
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Notice Comment Write
     * POST
     * @param secureKey
     * @param comment
     * @param itemUid
     */
    fun getNoticeCommentWrite(requestQueue: RequestQueue, listener: RequestListener<CommentData>,
                              secureKey: String, comment: String, itemUid: Int) {

        NagajaLog().d("wooks, API ========= getNoticeCommentWrite : secureKey = $secureKey / comment = $comment / itemUid = $itemUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + NOTICE_COMMENT_WRITE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTICE_COMMENT_WRITE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val totalCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val commentData = CommentData()
                        commentData.setTotalCount(totalCount)
                        commentData.setCommentUid(jsonObject.getInt("commentUID"))
                        commentData.setCommentStatus(jsonObject.getInt("commentStatus"))
                        commentData.setComment(if (!jsonObject.isNull("comment")) jsonObject.getString("comment") else "")
                        commentData.setIsUseYn(jsonObject.getBoolean("useYn"))
                        commentData.setCreateDate(if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "")
                        commentData.setUpdateDate(if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "")
                        commentData.setMemberUid(jsonObject.getInt("memberUID"))
                        commentData.setMemberName(if (!jsonObject.isNull("memberName")) jsonObject.getString("memberName") else "")
                        commentData.setNoticeUid(jsonObject.getInt("noticeUID"))

                        listener.onSuccess(commentData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("comment", comment)
                jsonBody.put("noticeuid", itemUid)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Notice Comment Delete
     * POST
     * @param secureKey
     * @param commentUid
     */
    fun getNoticeCommentDelete(requestQueue: RequestQueue, listener: RequestListener<String>,
                               secureKey: String, commentUid: Int) {

        NagajaLog().d("wooks, API ========= getNoticeCommentDelete : secureKey = $secureKey / commentUid = $commentUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + NOTICE_COMMENT_DELETE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTICE_COMMENT_DELETE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("commentuid", commentUid)
                jsonBody.put("useyn", false)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Board List
     * POST
     * @param secureKey
     * @param categoryUid
     * @param sort
     */
    fun getBoardList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<BoardData>>?,
                     secureKey: String, pageNow: Int, pageCount: Int, searchWord: String, sortType: Int, categoryUid: Int, subAreaUid: Int, mode: String, nationCode: String) {

        val searchWordType = if (!TextUtils.isEmpty(searchWord)) {
            1
        } else {
            0
        }

        NagajaLog().d("wooks, API ========= getBoardList : secureKey = $secureKey / pageNow = $pageNow / pageCount = $pageCount / sortType = $sortType / categoryUid = $categoryUid / subAreaUid = $subAreaUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + BOARD_LIST,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= BOARD_LIST : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val totalCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val boardListData = ArrayList<BoardData>()
                        for (i in 0 until jsonArray.length()) {
                            val boardData = BoardData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                boardData.setTotalCount(totalCount)
                                boardData.setBoardUid(jsonObject.getInt("boardUID"))
                                boardData.setBoardStatus(jsonObject.getInt("boardStatus"))
                                boardData.setBoardSubject( if (!jsonObject.isNull("boardSubject")) jsonObject.getString("boardSubject") else "" )
                                boardData.setBoardContent( if (!jsonObject.isNull("boardContent")) jsonObject.getString("boardContent") else "" )
                                boardData.setViewCount(jsonObject.getInt("viewCount"))
                                boardData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                boardData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                boardData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                boardData.setMemberUid(jsonObject.getInt("memberUID"))
                                boardData.setMemberName( if (!jsonObject.isNull("memberNName")) jsonObject.getString("memberNName") else "" )
                                boardData.setCompanyUid(jsonObject.getInt("compUID"))
                                boardData.setCompanyName( if (!jsonObject.isNull("compName")) jsonObject.getString("compName") else "" )
                                boardData.setMemberImageOrigin( if (!jsonObject.isNull("memberImageOrigin")) jsonObject.getString("memberImageOrigin") else "" )
                                boardData.setMemberImageName( if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "" )
                                boardData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                boardData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                                boardData.setCategoryName( if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "" )
                                boardData.setRecommendCount(jsonObject.getInt("recommCount"))
                                boardData.setBookMarkCount(jsonObject.getInt("bookmarkCount"))
                                boardData.setCommentCount(jsonObject.getInt("commentCount"))
                                boardData.setIsBookMark(jsonObject.getBoolean("bookmarkYn"))
                                boardData.setIsRecommend(jsonObject.getBoolean("recommYn"))
                                boardData.setIsReport(jsonObject.getBoolean("reportYn"))
                                boardData.setIsPopularity(jsonObject.getBoolean("popularityYn"))
                                boardData.setIsStandBy(jsonObject.getBoolean("standByYn"))
                                boardData.setLocationNationUid(jsonObject.getInt("locationNationUID"))
                                boardData.setLocationMainAreaUid(jsonObject.getInt("locationSiDoUID"))
                                boardData.setLocationSubAreaUid(jsonObject.getInt("locationGuGunUID"))
                                boardData.setLocationMainAreaName( if (!jsonObject.isNull("locationSiDo")) jsonObject.getString("locationSiDo") else "" )
                                boardData.setLocationSubAreaName( if (!jsonObject.isNull("locationGuGun")) jsonObject.getString("locationGuGun") else "" )
                                boardData.setLocationDesc( if (!jsonObject.isNull("locationDesc")) jsonObject.getString("locationDesc") else "" )
                                boardData.setSnsDesc( if (!jsonObject.isNull("snsDesc")) jsonObject.getString("snsDesc") else "" )
                                boardData.setBeginDate( if (!jsonObject.isNull("beginDate")) jsonObject.getString("beginDate") else "" )
                                boardData.setEndDate( if (!jsonObject.isNull("endDate")) jsonObject.getString("endDate") else "" )
                                boardData.setMissingDate( if (!jsonObject.isNull("missingDate")) jsonObject.getString("missingDate") else "" )

                                val jsonBoardImageArray = jsonObject.getJSONArray("boardImg")
                                val usedMarketImageListData = ArrayList<UsedMarketImageData>()
                                for (k in 0 until jsonBoardImageArray.length()) {
                                    val usedMarketImageData = UsedMarketImageData()
                                    val jsonBoardImageObject = jsonBoardImageArray.getJSONObject(k)
                                    if (null != jsonBoardImageObject) {
                                        usedMarketImageData.setItemImageUid(jsonBoardImageObject.getInt("boardImageUID"))
                                        usedMarketImageData.setItemImageSort(jsonBoardImageObject.getInt("boardImageSort"))
                                        usedMarketImageData.setItemImageOrigin( if (!jsonBoardImageObject.isNull("boardImageOrigin")) jsonBoardImageObject.getString("boardImageOrigin") else "" )
                                        usedMarketImageData.setItemImageName( if (!jsonBoardImageObject.isNull("boardImageName")) jsonBoardImageObject.getString("boardImageName") else "" )
                                        usedMarketImageData.setIsUseYn(jsonBoardImageObject.getBoolean("useYn"))
                                        usedMarketImageData.setCreateDate( if (!jsonBoardImageObject.isNull("createDate")) jsonBoardImageObject.getString("createDate") else "" )
                                        usedMarketImageData.setUpdateDate( if (!jsonBoardImageObject.isNull("updateDate")) jsonBoardImageObject.getString("updateDate") else "" )
                                        usedMarketImageData.setItemUid(jsonBoardImageObject.getInt("boardUID"))
                                    }

                                    usedMarketImageListData.add(usedMarketImageData)
                                }

                                boardData.setBoardImageListData(usedMarketImageListData)

                                val jsonBoardFileArray = jsonObject.getJSONArray("boardFile")
                                val fileListData = ArrayList<FileData>()
                                for (k in 0 until jsonBoardFileArray.length()) {
                                    val fileData = FileData()
                                    val jsonBoardFileObject = jsonBoardFileArray.getJSONObject(k)
                                    if (null != jsonBoardFileObject) {
                                        fileData.setBoardFileUid(jsonBoardFileObject.getInt("boardFileUID"))
                                        fileData.setBoardFileSort(jsonBoardFileObject.getInt("boardFileSort"))
                                        fileData.setBoardFileOrigin(if (!jsonBoardFileObject.isNull("boardFileOrigin")) jsonBoardFileObject.getString("boardFileOrigin") else "")
                                        fileData.setBoardFileName(if (!jsonBoardFileObject.isNull("boardFileName")) jsonBoardFileObject.getString("boardFileName") else "")
                                        fileData.setIsUseYn(jsonBoardFileObject.getBoolean("useYn"))
                                        fileData.setCreateDate(if (!jsonBoardFileObject.isNull("createDate")) jsonBoardFileObject.getString("createDate") else "")
                                        fileData.setUpdateDate(if (!jsonBoardFileObject.isNull("updateDate")) jsonBoardFileObject.getString("updateDate") else "")
                                        fileData.setBoardUid(jsonBoardFileObject.getInt("boardUID"))
                                    }

                                    fileListData.add(fileData)
                                }

                                boardData.setBoardFileListData(fileListData)
                            }
                            boardListData.add(boardData)
                        }

                        listener?.onSuccess(boardListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener!!.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener!!.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message = "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (!TextUtils.isEmpty(secureKey)) {
                    params["Authorization"] = "Bearer $secureKey"
                }
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["pagenow"] = pageNow.toString()
                params["pagesz"] = pageCount.toString()
                params["sr"] = searchWordType.toString()
                params["srtxt"] = searchWord
                params["srstatus"] = ""
                params["srbegindate"] = ""
                params["srenddate"] = ""
                params["sort"] = sortType.toString()
                params["cago"] = categoryUid.toString()
                params["gugunuid"] = subAreaUid.toString()
                params["mode"] = mode
                params["nuid"] = nationCode
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Board Horizontal List
     * POST
     * @param secureKey
     * @param categoryUid
     * @param sort
     */
    fun getBoardHorizontalList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<BoardData>>,
                               secureKey: String, pageNow: Int, pageCount: Int, categoryUid: Int, subAreaUid: Int, boardType: String, nationCode: String) {

        NagajaLog().d("wooks, API ========= getBoardHorizontalList : secureKey = $secureKey / pageNow = $pageNow / pageCount = $pageCount" +
                " / categoryUid = $categoryUid / subAreaUid = $subAreaUid / boardType = $boardType")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            "$API_DOMAIN$BOARD_LIST/$boardType",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= BOARD_HORIZONTA_LIST : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val totalCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val boardListData = ArrayList<BoardData>()
                        for (i in 0 until jsonArray.length()) {
                            val boardData = BoardData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                boardData.setTotalCount(totalCount)
                                boardData.setBoardUid(jsonObject.getInt("boardUID"))
                                boardData.setBoardStatus(jsonObject.getInt("boardStatus"))
                                boardData.setBoardSubject( if (!jsonObject.isNull("boardSubject")) jsonObject.getString("boardSubject") else "" )
                                boardData.setBoardContent( if (!jsonObject.isNull("boardContent")) jsonObject.getString("boardContent") else "" )
                                boardData.setViewCount(jsonObject.getInt("viewCount"))
                                boardData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                boardData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                boardData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                boardData.setMemberUid(jsonObject.getInt("memberUID"))
                                boardData.setMemberName( if (!jsonObject.isNull("memberNName")) jsonObject.getString("memberNName") else "" )
                                boardData.setCompanyUid(jsonObject.getInt("compUID"))
                                boardData.setCompanyName( if (!jsonObject.isNull("compName")) jsonObject.getString("compName") else "" )
                                boardData.setMemberImageOrigin( if (!jsonObject.isNull("memberImageOrigin")) jsonObject.getString("memberImageOrigin") else "" )
                                boardData.setMemberImageName( if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "" )
                                boardData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                boardData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                                boardData.setCategoryName( if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "" )
                                boardData.setParentCategoryName( if (!jsonObject.isNull("parentCagoName")) jsonObject.getString("parentCagoName") else "" )
                            }
                            boardListData.add(boardData)
                        }

                        listener?.onSuccess(boardListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message = "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (!TextUtils.isEmpty(secureKey)) {
                    params["Authorization"] = "Bearer $secureKey"
                }
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["pagenow"] = pageNow.toString()
                params["pagesz"] = pageCount.toString()
                params["sr"] = "0"
//                params["srtxt"] = ""
//                params["srstatus"] = ""
//                params["srbegindate"] = ""
//                params["srenddate"] = ""
                params["cago"] = categoryUid.toString()
                params["gugunuid"] = subAreaUid.toString()
                params["nuid"] = nationCode
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Board Detail
     * POST
     * @param secureKey
     * @param boardUid
     */
    fun getBoardDetail(requestQueue: RequestQueue, listener: RequestListener<BoardData>,
                       secureKey: String, boardUid: Int, nationPhone: String) {

        NagajaLog().d("wooks, API ========= getBoardList : secureKey = $secureKey / boardUid = $boardUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            "$API_DOMAIN$BOARD_DETAIL/$boardUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= BOARD_DETAIL : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val boardData = BoardData()
                        if (null != jsonObject) {
                            boardData.setBoardUid(jsonObject.getInt("boardUID"))
                            boardData.setBoardStatus(jsonObject.getInt("boardStatus"))
                            boardData.setBoardSubject(if (!jsonObject.isNull("boardSubject")) jsonObject.getString("boardSubject") else "")
                            boardData.setBoardContent(if (!jsonObject.isNull("boardContent")) jsonObject.getString("boardContent") else "")
                            boardData.setViewCount(jsonObject.getInt("viewCount"))
                            boardData.setIsUseYn(jsonObject.getBoolean("useYn"))
                            boardData.setCreateDate(if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "")
                            boardData.setUpdateDate(if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "")
                            boardData.setMemberUid(jsonObject.getInt("memberUID"))
                            boardData.setMemberName(if (!jsonObject.isNull("memberNName")) jsonObject.getString("memberNName") else "")
                            boardData.setCompanyUid(jsonObject.getInt("compUID"))
                            boardData.setCompanyName(if (!jsonObject.isNull("compName")) jsonObject.getString("compName") else "")
                            boardData.setMemberImageOrigin(if (!jsonObject.isNull("memberImageOrigin")) jsonObject.getString("memberImageOrigin") else "")
                            boardData.setMemberImageName(if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "")
                            boardData.setCategoryUid(jsonObject.getInt("cagoUID"))
                            boardData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                            boardData.setCategoryName(if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "")
                            boardData.setRecommendCount(jsonObject.getInt("recommCount"))
                            boardData.setBookMarkCount(jsonObject.getInt("bookmarkCount"))
                            boardData.setCommentCount(jsonObject.getInt("commentCount"))
                            boardData.setIsBookMark(jsonObject.getBoolean("bookmarkYn"))
                            boardData.setIsRecommend(jsonObject.getBoolean("recommYn"))
                            boardData.setIsReport(jsonObject.getBoolean("reportYn"))
                            boardData.setIsPopularity(jsonObject.getBoolean("popularityYn"))
                            boardData.setIsStandBy(jsonObject.getBoolean("standByYn"))
                            boardData.setLocationNationUid(jsonObject.getInt("locationNationUID"))
                            boardData.setLocationMainAreaUid(jsonObject.getInt("locationSiDoUID"))
                            boardData.setLocationSubAreaUid(jsonObject.getInt("locationGuGunUID"))
                            boardData.setLocationMainAreaName(if (!jsonObject.isNull("locationSiDo")) jsonObject.getString("locationSiDo") else "")
                            boardData.setLocationSubAreaName(if (!jsonObject.isNull("locationGuGun")) jsonObject.getString("locationGuGun") else "")
                            boardData.setLocationDesc(if (!jsonObject.isNull("locationDesc")) jsonObject.getString("locationDesc") else "")
                            boardData.setSnsDesc(if (!jsonObject.isNull("snsDesc")) jsonObject.getString("snsDesc") else "")
                            boardData.setBeginDate(if (!jsonObject.isNull("beginDate")) jsonObject.getString("beginDate") else "")
                            boardData.setEndDate(if (!jsonObject.isNull("endDate")) jsonObject.getString("endDate") else "")
                            boardData.setMissingDate(if (!jsonObject.isNull("missingDate")) jsonObject.getString("missingDate") else "")

                            val jsonBoardImageArray = jsonObject.getJSONArray("boardImg")
                            val usedMarketImageListData = ArrayList<UsedMarketImageData>()
                            for (k in 0 until jsonBoardImageArray.length()) {
                                val usedMarketImageData = UsedMarketImageData()
                                val jsonBoardImageObject = jsonBoardImageArray.getJSONObject(k)
                                if (null != jsonBoardImageObject) {
                                    usedMarketImageData.setItemImageUid(jsonBoardImageObject.getInt("boardImageUID"))
                                    usedMarketImageData.setItemImageSort(jsonBoardImageObject.getInt("boardImageSort"))
                                    usedMarketImageData.setItemImageOrigin(if (!jsonBoardImageObject.isNull("boardImageOrigin")) jsonBoardImageObject.getString("boardImageOrigin") else "")
                                    usedMarketImageData.setItemImageName(if (!jsonBoardImageObject.isNull("boardImageName")) jsonBoardImageObject.getString("boardImageName") else "")
                                    usedMarketImageData.setIsUseYn(jsonBoardImageObject.getBoolean("useYn"))
                                    usedMarketImageData.setCreateDate(if (!jsonBoardImageObject.isNull("createDate")) jsonBoardImageObject.getString("createDate") else "")
                                    usedMarketImageData.setUpdateDate(if (!jsonBoardImageObject.isNull("updateDate")) jsonBoardImageObject.getString("updateDate") else "")
                                    usedMarketImageData.setItemUid(jsonBoardImageObject.getInt("boardUID"))
                                }

                                usedMarketImageListData.add(usedMarketImageData)
                            }

                            boardData.setBoardImageListData(usedMarketImageListData)

                            val jsonBoardFileArray = jsonObject.getJSONArray("boardFile")
                            val fileListData = ArrayList<FileData>()
                            for (k in 0 until jsonBoardFileArray.length()) {
                                val fileData = FileData()
                                val jsonBoardFileObject = jsonBoardFileArray.getJSONObject(k)
                                if (null != jsonBoardFileObject) {
                                    fileData.setBoardFileUid(jsonBoardFileObject.getInt("boardFileUID"))
                                    fileData.setBoardFileSort(jsonBoardFileObject.getInt("boardFileSort"))
                                    fileData.setBoardFileOrigin(if (!jsonBoardFileObject.isNull("boardFileOrigin")) jsonBoardFileObject.getString("boardFileOrigin") else "")
                                    fileData.setBoardFileName(if (!jsonBoardFileObject.isNull("boardFileName")) jsonBoardFileObject.getString("boardFileName") else "")
                                    fileData.setIsUseYn(jsonBoardFileObject.getBoolean("useYn"))
                                    fileData.setCreateDate(if (!jsonBoardFileObject.isNull("createDate")) jsonBoardFileObject.getString("createDate") else "")
                                    fileData.setUpdateDate(if (!jsonBoardFileObject.isNull("updateDate")) jsonBoardFileObject.getString("updateDate") else "")
                                    fileData.setBoardUid(jsonBoardFileObject.getInt("boardUID"))
                                }

                                fileListData.add(fileData)
                            }

                            boardData.setBoardFileListData(fileListData)
                        }

                        listener.onSuccess(boardData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN -> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message = "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (!TextUtils.isEmpty(secureKey)) {
                    params["Authorization"] = "Bearer $secureKey"
                }
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["nuid"] = nationPhone
                return params
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Board Missing Detail
     * POST
     * @param secureKey
     * @param boardUid
     */
    fun getBoardMissingAndJobDetail(requestQueue: RequestQueue, listener: RequestListener<BoardData>,
                                    secureKey: String, boardUid: Int, isMissing: Boolean) {

        NagajaLog().d("wooks, API ========= getBoardMissingAndJobDetail : secureKey = $secureKey / boardUid = $boardUid / isMissing = $isMissing")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            "$API_DOMAIN$BOARD_DETAIL/$boardUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= BOARD_DETAIL : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val boardData = BoardData()
                        if (null != jsonObject) {
                            boardData.setBoardUid(jsonObject.getInt("boardUID"))
                            boardData.setBoardStatus(jsonObject.getInt("boardStatus"))
                            boardData.setBoardSubject(if (!jsonObject.isNull("boardSubject")) jsonObject.getString("boardSubject") else "")
                            boardData.setBoardContent(if (!jsonObject.isNull("boardContent")) jsonObject.getString("boardContent") else "")
                            boardData.setViewCount(jsonObject.getInt("viewCount"))
                            boardData.setIsUseYn(jsonObject.getBoolean("useYn"))
                            boardData.setCreateDate(if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "")
                            boardData.setUpdateDate(if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "")
                            boardData.setMemberUid(jsonObject.getInt("memberUID"))
                            boardData.setMemberName(if (!jsonObject.isNull("memberNName")) jsonObject.getString("memberNName") else "")
                            boardData.setCompanyUid(jsonObject.getInt("compUID"))
                            boardData.setCompanyName(if (!jsonObject.isNull("compName")) jsonObject.getString("compName") else "")
                            boardData.setMemberImageOrigin(if (!jsonObject.isNull("memberImageOrigin")) jsonObject.getString("memberImageOrigin") else "")
                            boardData.setMemberImageName(if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "")
                            boardData.setCategoryUid(jsonObject.getInt("cagoUID"))
                            boardData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                            boardData.setCategoryName(if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "")
                            boardData.setRecommendCount(jsonObject.getInt("recommCount"))
                            boardData.setBookMarkCount(jsonObject.getInt("bookmarkCount"))
                            boardData.setCommentCount(jsonObject.getInt("commentCount"))
                            boardData.setIsBookMark(jsonObject.getBoolean("bookmarkYn"))
                            boardData.setIsRecommend(jsonObject.getBoolean("recommYn"))
                            boardData.setIsReport(jsonObject.getBoolean("reportYn"))
                            boardData.setIsPopularity(jsonObject.getBoolean("popularityYn"))
                            boardData.setIsStandBy(jsonObject.getBoolean("standByYn"))
                            boardData.setLocationNationUid(jsonObject.getInt("locationNationUID"))
                            boardData.setLocationMainAreaUid(jsonObject.getInt("locationSiDoUID"))
                            boardData.setLocationSubAreaUid(jsonObject.getInt("locationGuGunUID"))
                            boardData.setLocationMainAreaName(if (!jsonObject.isNull("locationSiDo")) jsonObject.getString("locationSiDo") else "")
                            boardData.setLocationSubAreaName(if (!jsonObject.isNull("locationGuGun")) jsonObject.getString("locationGuGun") else "")
                            boardData.setLocationDesc(if (!jsonObject.isNull("locationDesc")) jsonObject.getString("locationDesc") else "")
                            boardData.setSnsDesc(if (!jsonObject.isNull("snsDesc")) jsonObject.getString("snsDesc") else "")
                            boardData.setBeginDate(if (!jsonObject.isNull("beginDate")) jsonObject.getString("beginDate") else "")
                            boardData.setEndDate(if (!jsonObject.isNull("endDate")) jsonObject.getString("endDate") else "")
                            boardData.setMissingDate(if (!jsonObject.isNull("missingDate")) jsonObject.getString("missingDate") else "")

                            boardData.setLatitude( if (!jsonObject.isNull("locationLatitude")) jsonObject.getString("locationLatitude").toString() else "" )
                            boardData.setLongitude( if (!jsonObject.isNull("locationLongitude")) jsonObject.getString("locationLongitude").toString() else "" )

                            val jsonBoardImageArray = jsonObject.getJSONArray("boardImg")
                            val usedMarketImageListData = ArrayList<UsedMarketImageData>()
                            for (k in 0 until jsonBoardImageArray.length()) {
                                val usedMarketImageData = UsedMarketImageData()
                                val jsonBoardImageObject = jsonBoardImageArray.getJSONObject(k)
                                if (null != jsonBoardImageObject) {
                                    usedMarketImageData.setItemImageUid(jsonBoardImageObject.getInt("boardImageUID"))
                                    usedMarketImageData.setItemImageSort(jsonBoardImageObject.getInt("boardImageSort"))
                                    usedMarketImageData.setItemImageOrigin(if (!jsonBoardImageObject.isNull("boardImageOrigin")) jsonBoardImageObject.getString("boardImageOrigin") else "")
                                    usedMarketImageData.setItemImageName(if (!jsonBoardImageObject.isNull("boardImageName")) jsonBoardImageObject.getString("boardImageName") else "")
                                    usedMarketImageData.setIsUseYn(jsonBoardImageObject.getBoolean("useYn"))
                                    usedMarketImageData.setCreateDate(if (!jsonBoardImageObject.isNull("createDate")) jsonBoardImageObject.getString("createDate") else "")
                                    usedMarketImageData.setUpdateDate(if (!jsonBoardImageObject.isNull("updateDate")) jsonBoardImageObject.getString("updateDate") else "")
                                    usedMarketImageData.setItemUid(jsonBoardImageObject.getInt("boardUID"))
                                }

                                usedMarketImageListData.add(usedMarketImageData)
                            }

                            boardData.setBoardImageListData(usedMarketImageListData)


                            val jsonBoardFileArray = jsonObject.getJSONArray("boardFile")
                            val fileListData = ArrayList<FileData>()
                            for (k in 0 until jsonBoardFileArray.length()) {
                                val fileData = FileData()
                                val jsonBoardFileObject = jsonBoardFileArray.getJSONObject(k)
                                if (null != jsonBoardFileObject) {
                                    fileData.setBoardFileUid(jsonBoardFileObject.getInt("boardFileUID"))
                                    fileData.setBoardFileSort(jsonBoardFileObject.getInt("boardFileSort"))
                                    fileData.setBoardFileOrigin(if (!jsonBoardFileObject.isNull("boardFileOrigin")) jsonBoardFileObject.getString("boardFileOrigin") else "")
                                    fileData.setBoardFileName(if (!jsonBoardFileObject.isNull("boardFileName")) jsonBoardFileObject.getString("boardFileName") else "")
                                    fileData.setIsUseYn(jsonBoardFileObject.getBoolean("useYn"))
                                    fileData.setCreateDate(if (!jsonBoardFileObject.isNull("createDate")) jsonBoardFileObject.getString("createDate") else "")
                                    fileData.setUpdateDate(if (!jsonBoardFileObject.isNull("updateDate")) jsonBoardFileObject.getString("updateDate") else "")
                                    fileData.setBoardUid(jsonBoardFileObject.getInt("boardUID"))
                                }

                                fileListData.add(fileData)
                            }

                            boardData.setBoardFileListData(fileListData)
                        }

                        listener.onSuccess(boardData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN -> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message = "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (!TextUtils.isEmpty(secureKey)) {
                    params["Authorization"] = "Bearer $secureKey"
                }
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["mode"] = if (isMissing) "missing" else "job"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Board Comment
     * GET
     * @param secureKey
     * @param boardUid
     * @param pageNow
     * @param pageCount
     */
    fun getBoardComment(requestQueue: RequestQueue, listener: RequestListener<ArrayList<CommentData>>,
                        secureKey: String, boardUid: Int, pageNow: Int, pageCount: Int, isSecure: Boolean) {

        NagajaLog().d("wooks, API ========= getBoardComment : secureKey = $secureKey / boardUid = $boardUid / pageNow = $pageNow / pageCount = $pageCount / isSecure = $isSecure")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$BOARD_COMMENT_LIST/$boardUid?pagenow=$pageNow&pagesz=$pageCount&secureyn=$isSecure",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= BOARD_COMMENT_LIST : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val totalCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val commentListData = ArrayList<CommentData>()
                        for (i in 0 until jsonArray.length()) {
                            val commentData = CommentData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                commentData.setTotalCount(totalCount)
                                commentData.setCommentUid(jsonObject.getInt("commentUID"))
                                commentData.setCommentStatus(jsonObject.getInt("commentStatus"))
                                commentData.setComment( if (!jsonObject.isNull("comment")) jsonObject.getString("comment") else "" )
                                commentData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                commentData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                commentData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                commentData.setMemberUid(jsonObject.getInt("memberUID"))
                                commentData.setMemberName( if (!jsonObject.isNull("memberName")) jsonObject.getString("memberName") else "" )
                                commentData.setBoardUid(jsonObject.getInt("boardUID"))
                                commentData.setMemberNickName( if (!jsonObject.isNull("memNName")) jsonObject.getString("memNName") else "" )
                                commentData.setMemberImageOrigin( if (!jsonObject.isNull("memberImageOrigin")) jsonObject.getString("memberImageOrigin") else "" )
                                commentData.setMemberImageName( if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "" )
                            }

                            commentListData.add(commentData)
                        }
                        listener.onSuccess(commentListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (!TextUtils.isEmpty(secureKey)) {
                    params["Authorization"] = "Bearer $secureKey"
                }
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Board Comment Delete
     * POST
     * @param secureKey
     * @param commentUid
     */
    fun getBoardCommentDelete(requestQueue: RequestQueue, listener: RequestListener<String>,
                              secureKey: String, commentUid: Int) {

        NagajaLog().d("wooks, API ========= getBoardCommentDelete : secureKey = $secureKey / commentUid = $commentUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + BOARD_COMMENT_DELETE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= BOARD_COMMENT_DELETE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("commentuid", commentUid)
                jsonBody.put("useyn", false)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Board Comment Write
     * POST
     * @param secureKey
     * @param comment
     * @param itemUid
     */
    fun getBoardCommentWrite(requestQueue: RequestQueue, listener: RequestListener<CommentData>,
                             secureKey: String, comment: String, itemUid: Int) {

        NagajaLog().d("wooks, API ========= getBoardCommentWrite : secureKey = $secureKey / comment = $comment / itemUid = $itemUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + BOARD_COMMENT_WRITE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= BOARD_COMMENT_WRITE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val totalCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val commentData = CommentData()
                        commentData.setTotalCount(totalCount)
                        commentData.setCommentUid(jsonObject.getInt("commentUID"))
                        commentData.setCommentStatus(jsonObject.getInt("commentStatus"))
                        commentData.setComment(if (!jsonObject.isNull("comment")) jsonObject.getString("comment") else "")
                        commentData.setIsUseYn(jsonObject.getBoolean("useYn"))
                        commentData.setCreateDate(if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "")
                        commentData.setUpdateDate(if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "")
                        commentData.setMemberUid(jsonObject.getInt("memberUID"))
                        commentData.setMemberName(if (!jsonObject.isNull("memberName")) jsonObject.getString("memberName") else "")
                        commentData.setBoardUid(jsonObject.getInt("boardUID"))
                        commentData.setMemberNickName(if (!jsonObject.isNull("memNName")) jsonObject.getString("memNName") else "")
                        commentData.setMemberImageOrigin(if (!jsonObject.isNull("memberImageOrigin")) jsonObject.getString("memberImageOrigin") else "")
                        commentData.setMemberImageName(if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "")

                        listener.onSuccess(commentData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("comment", comment)
                jsonBody.put("boarduid", itemUid)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Recommend Save
     * POST
     * @param secureKey
     * @param boardUid
     * @param isUseYn
     */
    fun getRecommendSave(requestQueue: RequestQueue, listener: RequestListener<String>,
                         secureKey: String, boardUid: Int, isUseYn: Boolean) {

        NagajaLog().d("wooks, API ========= getBoardCommentWrite : secureKey = $secureKey / boarduid = $boardUid / isUseYn = $isUseYn")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + NetworkProvider.BOARD_RECOMMEND_SAVE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= BOARD_RECOMMEND_SAVE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("boarduid", boardUid)
                jsonBody.put("useyn", if (isUseYn) 1 else 0)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Bookmark Save
     * POST
     * @param secureKey
     * @param boardUid
     * @param isUseYn
     */
    fun getBookmarkSave(requestQueue: RequestQueue, listener: RequestListener<String>,
                        secureKey: String, boardUid: Int, isUseYn: Boolean) {

        NagajaLog().d("wooks, API ========= getBoardCommentWrite : secureKey = $secureKey / boarduid = $boardUid / isUseYn = $isUseYn")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + NetworkProvider.BOARD_BOOKMARK_SAVE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= BOARD_BOOKMARK_SAVE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("boarduid", boardUid)
                jsonBody.put("useyn", if (isUseYn) 1 else 0)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Board Register Save (Multi-Part)
     * POST Array
     * @param secureKey
     * @param companyUid
     * @param title
     * @param content
     * @param starPoint
     * @param imageUploadListData
     */
    fun getBoardRegisterSave(requestQueue: RequestQueue, listener: RequestListener<String>,
                             secureKey: String, subject: String, content: String, categoryUid: Int, memberUid: Int,
                             nationUid: Int, mainAreaUid: Int, subAreaUid: Int,
                             imageUploadListData: ArrayList<ImageUploadData>) {

        NagajaLog().d("wooks, API ========= getBoardRegisterSave : secureKey = $secureKey / subject = $subject / content = $content / categoryUid = $categoryUid  " +
                "/ memberUid = $memberUid / nationUid = $nationUid / mainAreaUid = $mainAreaUid / subAreaUid = $subAreaUid / imageUploadListData = ${imageUploadListData.size}")

        val volleyMultipartArrayRequest: com.nagaja.app.android.Utils.VolleyMultipartArrayRequest = object : com.nagaja.app.android.Utils.VolleyMultipartArrayRequest(
            Method.POST,
            API_DOMAIN + BOARD_REGISTER_SAVE,
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= STORE_REVIEW_UPLOAD_SAVE : $it")

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["boardsubject"] = subject
                params["boardcontent"] = content
//                params["useyn"] =
                params["cagouid"] = categoryUid.toString()
                params["memberuid"] = memberUid.toString()
                params["locationnationuid"] = nationUid.toString()
                params["locationsidouid"] = mainAreaUid.toString()
                params["locationgugunuid"] = subAreaUid.toString()
                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val imageList: MutableMap<String, ArrayList<DataPart>> = HashMap()
                val dataPart: ArrayList<DataPart> = ArrayList()
                for (i in 0 until imageUploadListData.size) {
                    val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                    dataPart.add(dp)
                }
                imageList["images"] = dataPart
                return imageList
            }
        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)

    }

    /**
     * Get Board Register Modify (Multi-Part)
     * POST Array
     * @param secureKey
     * @param companyUid
     * @param title
     * @param content
     * @param starPoint
     * @param imageUploadListData
     */
    fun getBoardRegisterModify(requestQueue: RequestQueue, listener: RequestListener<String>,
                               secureKey: String, boardUid: Int, subject: String, content: String, categoryUid: Int, memberUid: Int,
                               nationUid: Int, mainAreaUid: Int, subAreaUid: Int, deleteImageUid: String,
                               imageUploadListData: ArrayList<ImageUploadData>) {

        NagajaLog().d("wooks, API ========= getBoardRegisterModify : secureKey = $secureKey/ boardUid = $boardUid / subject = $subject / content = $content / categoryUid = $categoryUid " +
                "/ memberUid = $memberUid / nationUid = $nationUid / mainAreaUid = $mainAreaUid / subAreaUid = $subAreaUid / deleteImageUid = $deleteImageUid " +
                " / imageUploadListData = ${imageUploadListData.size}")

        NagajaLog().d("wooks, !!! = $API_DOMAIN$BOARD_REGISTER_MODIFY/$boardUid")

        val volleyMultipartArrayRequest: VolleyMultipartArrayRequest = object : VolleyMultipartArrayRequest(
            Method.PUT,
            "$API_DOMAIN$BOARD_REGISTER_MODIFY/$boardUid",
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= BOARD_REGISTER_MODIFY : $it")

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["boardsubject"] = subject
                params["boardcontent"] = content
//                params["useyn"] =
                params["cagouid"] = categoryUid.toString()
                params["memberuid"] = memberUid.toString()
                params["locationnationuid"] = nationUid.toString()
                params["locationsidouid"] = mainAreaUid.toString()
                params["locationgugunuid"] = subAreaUid.toString()
                params["delimageuid"] = deleteImageUid
                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val imageList: MutableMap<String, ArrayList<DataPart>> = HashMap()
                val dataPart: ArrayList<DataPart> = ArrayList()
                for (i in 0 until imageUploadListData.size) {
                    val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                    dataPart.add(dp)
                }
                imageList["images"] = dataPart
                return imageList
            }
        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)
    }

    /**
     * Get Board Missing Register Save (Multi-Part)
     * POST Array
     * @param secureKey
     * @param companyUid
     * @param title
     * @param content
     * @param starPoint
     * @param imageUploadListData
     */
    fun getBoardMissingRegisterSave(requestQueue: RequestQueue, listener: RequestListener<String>,
                                    secureKey: String, subject: String, content: String, categoryUid: Int, memberUid: Int,
                                    nationUid: Int, mainAreaUid: Int, subAreaUid: Int,
                                    locationDesc: String, missingDate: String,
                                    imageUploadListData: ArrayList<ImageUploadData>) {

        NagajaLog().d("wooks, API ========= getBoardMissingRegisterSave : secureKey = $secureKey / subject = $subject / content = $content / categoryUid = $categoryUid  " +
                "/ memberUid = $memberUid / nationUid = $nationUid / mainAreaUid = $mainAreaUid / subAreaUid = $subAreaUid " +
                "/ locationDesc = $locationDesc / missingDate = $missingDate / imageUploadListData = ${imageUploadListData.size}")

        val volleyMultipartArrayRequest: com.nagaja.app.android.Utils.VolleyMultipartArrayRequest = object : com.nagaja.app.android.Utils.VolleyMultipartArrayRequest(
            Method.POST,
            API_DOMAIN + BOARD_REGISTER_SAVE,
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= BOARD_REGISTER_SAVE_MISSING : $it")

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["boardsubject"] = subject
                params["boardcontent"] = content
//                params["useyn"] =
                params["cagouid"] = categoryUid.toString()
                params["memberuid"] = memberUid.toString()
                params["locationnationuid"] = nationUid.toString()
                params["locationsidouid"] = mainAreaUid.toString()
                params["locationgugunuid"] = subAreaUid.toString()
                params["mode"] = "missing"
                params["locationdesc"] = locationDesc
                params["missingdate"] = missingDate
                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val imageList: MutableMap<String, ArrayList<DataPart>> = HashMap()
                val dataPart: ArrayList<DataPart> = ArrayList()
                for (i in 0 until imageUploadListData.size) {
                    val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                    dataPart.add(dp)
                }
                imageList["images"] = dataPart
                return imageList
            }
        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)

    }

    /**
     * Get Board Missing Register Modify (Multi-Part)
     * POST Array
     * @param secureKey
     * @param companyUid
     * @param title
     * @param content
     * @param starPoint
     * @param imageUploadListData
     */
    fun getBoardMissingRegisterModify(requestQueue: RequestQueue, listener: RequestListener<String>,
                                      secureKey: String, boardUid: Int, subject: String, content: String, categoryUid: Int, memberUid: Int,
                                      nationUid: Int, mainAreaUid: Int, subAreaUid: Int, deleteImageUid: String,
                                      locationDesc: String, missingDate: String,
                                      imageUploadListData: ArrayList<ImageUploadData>) {

        NagajaLog().d("wooks, API ========= getBoardMissingRegisterModify : secureKey = $secureKey/ boardUid = $boardUid / subject = $subject / content = $content / categoryUid = $categoryUid " +
                "/ memberUid = $memberUid / nationUid = $nationUid / mainAreaUid = $mainAreaUid / subAreaUid = $subAreaUid / deleteImageUid = $deleteImageUid " +
                "/ locationDesc = $locationDesc / missingDate = $missingDate / imageUploadListData = ${imageUploadListData.size}")

        val volleyMultipartArrayRequest: com.nagaja.app.android.Utils.VolleyMultipartArrayRequest = object : com.nagaja.app.android.Utils.VolleyMultipartArrayRequest(
            Method.PUT,
            "$API_DOMAIN$BOARD_REGISTER_MODIFY/$boardUid",
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= BOARD_REGISTER_MISSING_MODIFY : $it")

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["boardsubject"] = subject
                params["boardcontent"] = content
//                params["useyn"] =
                params["cagouid"] = categoryUid.toString()
                params["memberuid"] = memberUid.toString()
                params["locationnationuid"] = nationUid.toString()
                params["locationsidouid"] = mainAreaUid.toString()
                params["locationgugunuid"] = subAreaUid.toString()
                params["mode"] = "missing"
                params["locationdesc"] = locationDesc
                params["missingdate"] = missingDate
                params["delimageuid"] = deleteImageUid
                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val imageList: MutableMap<String, ArrayList<DataPart>> = HashMap()
                val dataPart: ArrayList<DataPart> = ArrayList()
                for (i in 0 until imageUploadListData.size) {
                    val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                    dataPart.add(dp)
                }
                imageList["images"] = dataPart
                return imageList
            }
        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)
    }

    /**
     * Get Board Board Delete
     * POST
     * @param secureKey
     * @param boardUid
     */
    fun getBoardDelete(requestQueue: RequestQueue, listener: RequestListener<String>,
                       secureKey: String, boardUid: Int) {

        NagajaLog().d("wooks, API ========= getBoardDelete : secureKey = $secureKey / boardUid = $boardUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + BOARD_DELETE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= BOARD_DELETE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["boarduid"] = boardUid.toString()
                params["useyn"] = false.toString()
                return params
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Job Register Save (Multi-Part)
     * POST Array
     * @param secureKey
     * @param companyUid
     * @param title
     * @param content
     * @param starPoint
     * @param imageUploadListData
     */
    fun getJobRegisterSave(requestQueue: RequestQueue, listener: RequestListener<String>, context: Context,
                           secureKey: String, subject: String, content: String, categoryUid: Int, memberUid: Int,
                           nationUid: Int, mainAreaUid: Int, subAreaUid: Int, locationDesc: String, snsDesc: String, beginDate: String, endDate: String,
                           imageUploadListData: ArrayList<ImageUploadData>, fileUploadListData: ArrayList<FileUploadData>, companyUid: Int, latitude: String, longitude: String) {

        NagajaLog().d("wooks, API ========= getJobRegisterSave : secureKey = $secureKey / subject = $subject / content = $content / categoryUid = $categoryUid  " +
                "/ memberUid = $memberUid / nationUid = $nationUid / mainAreaUid = $mainAreaUid / subAreaUid = $subAreaUid " +
                "/ locationDesc = $locationDesc / snsDesc = $snsDesc / beginDate = $beginDate / endDate = $endDate " +
                "/ imageUploadListData = ${imageUploadListData.size} / fileUploadListData = ${fileUploadListData.size}" +
                " / companyUid = $companyUid / latitude = $latitude / longitude = $longitude")

        val volleyMultipartArrayRequest: com.nagaja.app.android.Utils.VolleyMultipartArrayRequest = object : com.nagaja.app.android.Utils.VolleyMultipartArrayRequest(
            Method.POST,
            if (companyUid <= 0) API_DOMAIN + BOARD_REGISTER_SAVE
            else API_DOMAIN + COMPANY_JOB_REGISTER_SAVE,
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                if (companyUid <= 0) {
                    NagajaLog().d("wooks, API ========= BOARD_REGISTER_SAVE_MISSING : $it")
                } else {
                    NagajaLog().d("wooks, API ========= COMPANY_JOB_REGISTER_SAVE : $it")
                }


                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["boardsubject"] = subject
                params["boardcontent"] = content
//                params["useyn"] =
                params["cagouid"] = categoryUid.toString()
                params["memberuid"] = memberUid.toString()
                params["locationnationuid"] = nationUid.toString()
                params["locationsidouid"] = mainAreaUid.toString()
                params["locationgugunuid"] = subAreaUid.toString()
                params["mode"] = "job"
                params["locationdesc"] = locationDesc
                params["snsdesc"] = snsDesc
                params["begindate"] = beginDate
                params["enddate"] = endDate
                if (companyUid > 0) {
                    params["compuid"] = companyUid.toString()
                }
                params["locationlatitude"] = latitude
                params["locationlongitude"] = longitude
                return params
            }



            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val params: MutableMap<String, ArrayList<DataPart>> = HashMap()

                if (imageUploadListData.size > 0) {
                    val dataPart: ArrayList<DataPart> = ArrayList()
                    for (i in 0 until imageUploadListData.size) {
                        val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                        dataPart.add(dp)
                    }
                    params["images"] = dataPart
                }

                if (fileUploadListData.size > 0) {
                    var iStream: InputStream? = null
                    var inputData: ByteArray? = null
                    val dataPartFile: ArrayList<DataPart> = ArrayList()
                    try {
                        for (i in 0 until fileUploadListData.size) {
                            iStream = context.contentResolver.openInputStream(fileUploadListData[i].getFileUri())
                            inputData = getBytes(iStream!!)

                            val dp = DataPart(fileUploadListData[i].getFileName(), inputData!!)
                            dataPartFile.add(dp)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    params["files"] = dataPartFile
                }

                return params
            }
        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)

    }


    /**
     * Get Reservation Schedule Day
     * GET
     * @param companyUid
     */
    fun getReservationScheduleDay(requestQueue: RequestQueue, listener: RequestListener<ArrayList<ReservationScheduleDayData>>,
                                  companyUid: String) {

        NagajaLog().d("wooks, API ========= getReservationScheduleDay : companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$RESERVATION_SCHEDULE_DAY/$companyUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= RESERVATION_SCHEDULE_DAY : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val reservationScheduleDayListData = ArrayList<ReservationScheduleDayData>()
                        for (i in 0 until jsonArray.length()) {
                            val reservationScheduleDayData = ReservationScheduleDayData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                if (jsonObject.getBoolean("restDayYn")) {
                                    reservationScheduleDayData.setScheduleDaysUid(jsonObject.getInt("scheduleDaysUID"))
                                    reservationScheduleDayData.setCompanyUid(jsonObject.getInt("compUID"))
                                    reservationScheduleDayData.setCompanyDays( if (!jsonObject.isNull("compDays")) jsonObject.getString("compDays") else "" )
                                    reservationScheduleDayData.setReservationCount(jsonObject.getInt("reservateCount"))

                                    reservationScheduleDayListData.add(reservationScheduleDayData)
                                }
                            }
                        }
                        listener.onSuccess(reservationScheduleDayListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "Bearer $secureKey"
//                return params
//            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Reservation Schedule Time
     * GET
     * @param scheduleDayUid
     */
    fun getReservationScheduleTime(requestQueue: RequestQueue, listener: RequestListener<ArrayList<ReservationScheduleTimeData>>,
                                   scheduleDayUid: String) {

        NagajaLog().d("wooks, API ========= getReservationScheduleTime : scheduleDayUid = $scheduleDayUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$RESERVATION_SCHEDULE_TIME/$scheduleDayUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= RESERVATION_SCHEDULE_TIME : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val reservationScheduleTimeListData = ArrayList<ReservationScheduleTimeData>()
                        for (i in 0 until jsonArray.length()) {
                            val reservationScheduleTimeData = ReservationScheduleTimeData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                if (jsonObject.getBoolean("restTimeYn")) {
                                    reservationScheduleTimeData.setScheduleTimeUid(jsonObject.getInt("scheduleTimeUID"))
                                    reservationScheduleTimeData.setCompanyUid(jsonObject.getInt("compUID"))
                                    reservationScheduleTimeData.setCompanyDays( if (!jsonObject.isNull("compDays")) jsonObject.getString("compDays") else "" )
                                    reservationScheduleTimeData.setBeginTime( if (!jsonObject.isNull("beginTime")) jsonObject.getString("beginTime") else "" )
                                    reservationScheduleTimeData.setEndTime( if (!jsonObject.isNull("endTime")) jsonObject.getString("endTime") else "" )
                                    reservationScheduleTimeData.setReservationTimeCount(jsonObject.getInt("reservateTimeCount"))
                                    reservationScheduleTimeData.setReservationTimeLimitCount(jsonObject.getInt("reservateTimeLimitCount"))
                                    reservationScheduleTimeData.setReservationTimeTeamCount(jsonObject.getInt("reservateTimeTeamCount"))
                                    reservationScheduleTimeData.setReservationTimeTeamLimitCount(jsonObject.getInt("reservateTimeTeamLimitCount"))
                                    reservationScheduleTimeData.setScheduleDaysUid(jsonObject.getInt("scheduleDaysUID"))

                                    reservationScheduleTimeListData.add(reservationScheduleTimeData)
                                }
                            }


                        }
                        listener.onSuccess(reservationScheduleTimeListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "Bearer $secureKey"
//                return params
//            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Reservation Save
     * POST
     * @param secureKey
     * @param reservateClass
     * @param personCount
     * @param name
     * @param nationPhone
     * @param phoneNumber
     * @param memo
     * @param desc
     * @param companyUid
     */
    fun getReservationSave(requestQueue: RequestQueue, listener: RequestListener<ReservationSuccessData>,
                           secureKey: String, reservateClass: Int, personCount: Int, name: String, nationPhone: String,
                           phoneNumber: String, memo: String, desc: String, companyUid: Int, scheduleTimeUid: Int) {

        NagajaLog().d("wooks, API ========= getReservationSave : secureKey = $secureKey / reservateClass = $reservateClass / personCount = $personCount" +
                " / name = $name / nationPhone = $nationPhone / phoneNumber = $phoneNumber / memo = $memo / desc = $desc / companyUid = $companyUid / scheduleTimeUid = $scheduleTimeUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + RESERVATION_SAVE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= RESERVATION_SAVE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val reservationSuccessData = ReservationSuccessData()
                        if (null != jsonObject) {
                            reservationSuccessData.setReservationUid(jsonObject.getInt("reservateUID"))
                            reservationSuccessData.setReservationStatus(jsonObject.getInt("reservateStatus"))
                            reservationSuccessData.setReservationClass(jsonObject.getInt("reservateClass"))
                            reservationSuccessData.setReservationBeginTime(if (!jsonObject.isNull("reservateBeginTime")) jsonObject.getString("reservateBeginTime") else "")
                            reservationSuccessData.setReservationEndTime(if (!jsonObject.isNull("reservateEndTime")) jsonObject.getString("reservateEndTime") else "")
                            reservationSuccessData.setReservationPersonCount(jsonObject.getInt("reservationPersonCount"))
                            reservationSuccessData.setReservationName(if (!jsonObject.isNull("reservationName")) jsonObject.getString("reservationName") else "")
                            reservationSuccessData.setReservationNationPhone(if (!jsonObject.isNull("reservationNationPhone")) jsonObject.getString("reservationNationPhone") else "")
                            reservationSuccessData.setReservationPhoneNumber(if (!jsonObject.isNull("reservationPhone")) jsonObject.getString("reservationPhone") else "")
                            reservationSuccessData.setReservationMemo(if (!jsonObject.isNull("reservationMemo")) jsonObject.getString("reservationMemo") else "")
                            reservationSuccessData.setReservationDesc(if (!jsonObject.isNull("reservationDesc")) jsonObject.getString("reservationDesc") else "")
                            reservationSuccessData.setIsUseYn(jsonObject.getBoolean("useYn"))
                            reservationSuccessData.setCreateDate(if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "")
                            reservationSuccessData.setUpdateDate(if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "")
                            reservationSuccessData.setMemberUid(jsonObject.getInt("memberUID"))
                            reservationSuccessData.setCompanyUid(jsonObject.getInt("compUID"))
                            reservationSuccessData.setScheduleTimeUid(jsonObject.getInt("scheduleTimeUID"))
                        }

                        listener.onSuccess(reservationSuccessData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN -> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_NOT_ENOUGH_POINT -> {
                                errorCode = ERROR_CODE_NOT_ENOUGH_POINT
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("reservateclass", reservateClass)
                jsonBody.put("reservationpersoncount", personCount)
                jsonBody.put("reservationname", name)
                jsonBody.put("reservationnationphone", nationPhone)
                jsonBody.put("reservationphone", phoneNumber)
                jsonBody.put("reservationmemo", memo)
                jsonBody.put("reservationdesc", desc)
                jsonBody.put("compuid", companyUid)
                jsonBody.put("scheduletimeuid", scheduleTimeUid)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Reservation List
     * GET
     * @param scheduleDayUid
     */
    fun getReservationList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<ReservationData>>,
                           secureKey: String, pageNow: Int, pageCount: Int, nationPhone: String, categoryUid: Int,
                           companyUid: Int, status: Int, startDate: String, endDate: String, locationUid: Int, isCompanyReservation: Boolean) {

        NagajaLog().d("wooks, API ========= getReservationList : secureKey = $secureKey / pageNow = $pageNow / pageCount = $pageCount / nationPhone = $nationPhone" +
                " / categoryUid = $categoryUid / companyUid = $companyUid / status = $status / startDate = $startDate / endDate = $endDate / locationUid = $locationUid" +
                " / isCompanyReservation = $isCompanyReservation")

        NagajaLog().d("wooks, API ========= getReservationList : $API_DOMAIN$COMPANY_RESERVATION_LIST/$companyUid?pagenow=$pageNow&pagesz=$pageCount&nuid=$nationPhone&cuid=$companyUid&srstatus=$status&srbegindate=$startDate&srenddate=$endDate&locnuid=$locationUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            if (!isCompanyReservation) "$API_DOMAIN$RESERVATION_LIST?pagenow=$pageNow&pagesz=$pageCount&nuid=$nationPhone&cago=" +
                    "$categoryUid&cuid=$companyUid&srstatus=$status&srbegindate=$startDate&srenddate=$endDate&locnuid=$locationUid"
            else "$API_DOMAIN$COMPANY_RESERVATION_LIST/$companyUid?pagenow=$pageNow&pagesz=$pageCount&nuid=$nationPhone&cuid=$companyUid" +
                    "&srstatus=$status&srbegindate=$startDate&srenddate=$endDate&locnuid=$locationUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= RESERVATION_LIST : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val totalCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val reservationListData = ArrayList<ReservationData>()
                        for (i in 0 until jsonArray.length()) {
                            val reservationData = ReservationData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                if (jsonObject.getBoolean("useYn")) {
                                    reservationData.setTotalCount(totalCount)
                                    reservationData.setReservationUid(jsonObject.getInt("reservateUID"))
                                    reservationData.setServiceUid(jsonObject.getInt("serviceUID"))
                                    reservationData.setReservationStatus(jsonObject.getInt("reservateStatus"))
                                    reservationData.setReservationClass(jsonObject.getInt("reservateClass"))
                                    reservationData.setReservationBeginTime( if (!jsonObject.isNull("reservateBeginTime")) jsonObject.getString("reservateBeginTime") else "" )
                                    reservationData.setReservationEndTime( if (!jsonObject.isNull("reservateEndTime")) jsonObject.getString("reservateEndTime") else "" )
                                    reservationData.setReservationPersonCount(jsonObject.getInt("reservationPersonCount"))
                                    reservationData.setReservationName( if (!jsonObject.isNull("reservationName")) jsonObject.getString("reservationName") else "" )
                                    reservationData.setReservationNationPhone( if (!jsonObject.isNull("reservationNationPhone")) jsonObject.getString("reservationNationPhone") else "" )
                                    reservationData.setReservationPhoneNumber( if (!jsonObject.isNull("reservationPhone")) jsonObject.getString("reservationPhone") else "" )
                                    reservationData.setReservationMemo( if (!jsonObject.isNull("reservationMemo")) jsonObject.getString("reservationMemo") else "" )
                                    reservationData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                    reservationData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                    reservationData.setMemberUid(jsonObject.getInt("memberUID"))
                                    reservationData.setCompanyUid(jsonObject.getInt("compUID"))
                                    reservationData.setCompanyNameEnglish( if (!jsonObject.isNull("compNameEng")) jsonObject.getString("compNameEng") else "" )
                                    reservationData.setCompanyName( if (!jsonObject.isNull("compName")) jsonObject.getString("compName") else "" )
                                    reservationData.setScheduleTimeUid(jsonObject.getInt("scheduleTimeUID"))

                                    reservationListData.add(reservationData)
                                }
                            }
                        }

                        listener.onSuccess(reservationListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_RESERVATION_DATE_SELECT_ERROR -> {
                                errorCode = ERROR_CODE_RESERVATION_DATE_SELECT_ERROR
                            }

                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Reservation Change Status
     * PUT
     * @param secureKey
     * @param reservationUid
     * @param status
     */
    fun getReservationChangeStatus(requestQueue: RequestQueue, listener: RequestListener<String>,
                                   secureKey: String, reservationUid: Int, status: Int) {

        NagajaLog().d("wooks, API ========= getReservationChangeStatus : secureKey = $secureKey / reservationUid = $reservationUid / status = $status")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.PUT,
            API_DOMAIN + RESERVATION_CHANGE_STATUS,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= RESERVATION_CHANGE_STATUS : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("reservateuid", reservationUid)
                jsonBody.put("reservatestatus", status)
                return jsonBody.toString().toByteArray()
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Event Data
     * GET
     * @param categoryUid
     * @param nationCode
     */
    fun getEventData(requestQueue: RequestQueue, listener: RequestListener<EventData>,
                     nationCode: String) {

        NagajaLog().d("wooks, API ========= getEventData : nationCode = $nationCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$EVENT_DATA/?locnuid=$nationCode",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= EVENT_DATA : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val eventData = EventData()

                        eventData.setPopupUid(jsonObject.getInt("popupUID"))
                        eventData.setPopupSubject(if (!jsonObject.isNull("popupSubject")) jsonObject.getString("popupSubject") else "")
                        eventData.setPopupContent(if (!jsonObject.isNull("popupContent")) jsonObject.getString("popupContent") else "")
                        eventData.setPopupImageName(if (!jsonObject.isNull("popupImageName")) jsonObject.getString("popupImageName") else "")
                        eventData.setPopupImageOrigin(if (!jsonObject.isNull("popupImageOrigin")) jsonObject.getString("popupImageOrigin") else "")
                        eventData.setPopupTargetUrl(if (!jsonObject.isNull("popupTargetUri")) jsonObject.getString("popupTargetUri") else "")
                        eventData.setNationUid(jsonObject.getInt("nationUID"))
                        eventData.setPopupUseYn(jsonObject.getBoolean("popupUseYn"))

                        listener.onSuccess(eventData)
                    } else {
                        var errorCode = status
//                        when (status) {
//                            ERROR_EXPIRED_TOKEN,
//                            ERROR_INVALID_TOKEN,
//                            ERROR_DO_NOT_EXIST_TOKEN-> {
//                                errorCode = ERROR_EXPIRED_TOKEN
//                            }
//                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Phone Number Change
     * POST
     * @param secureKey
     * @param nationPhoneCode
     * @param changePhoneNumber
     * @param authCode
     */
    fun getPhoneNumberChange(requestQueue: RequestQueue, listener: RequestListener<String>,
                             secureKey: String, nationPhoneCode: String, changePhoneNumber: String, authCode: String) {

        NagajaLog().d("wooks, API ========= getPhoneNumberChange : secureKey = $secureKey / nationPhoneCode = $nationPhoneCode / changePhoneNumber = $changePhoneNumber" +
                " / authCode = $authCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + PHONE_NUMBER_CHANGE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= PHONE_NUMBER_CHANGE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["nationphone"] = nationPhoneCode
                params["phone"] = changePhoneNumber
                params["code"] = authCode
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Modify Profile Update
     * POST Array
     * @param secureKey
     * @param memberNickName
     * @param imageData
     */
    fun getModifyProfileUpdate(requestQueue: RequestQueue, listener: RequestListener<ModifyProfileData>,
                               secureKey: String, memberNickName: String, imageData: ImageUploadData) {

        NagajaLog().d("wooks, API ========= getModifyProfileUpdate : secureKey = $secureKey / memberNickName = $memberNickName / imageData = ${imageData.getFileName()}")

        val volleyMultipartArrayRequest: com.nagaja.app.android.Utils.VolleyMultipartArrayRequest = object : com.nagaja.app.android.Utils.VolleyMultipartArrayRequest(
            Method.POST,
            API_DOMAIN + MODIFY_PROFILE_UPDATE,
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= MODIFY_PROFILE_UPDATE : $it")

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        val jsonObject = it.getJSONObject("data")
                        val modifyProfileData = ModifyProfileData()

                        modifyProfileData.setMemberNickName( if (!jsonObject.isNull("memNName")) jsonObject.getString("memNName") else "" )
                        modifyProfileData.setProfileImageName( if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "" )
                        modifyProfileData.setProfileImageOrigin( if (!jsonObject.isNull("memberImageOrigin")) jsonObject.getString("memberImageOrigin") else "" )

                        listener.onSuccess(modifyProfileData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["memnname"] = memberNickName
                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val imageList: MutableMap<String, ArrayList<DataPart>> = HashMap()
                val dataPart: ArrayList<DataPart> = ArrayList()
                try {
                    val dp = DataPart(imageData.getFileName(), getFileDataFromDrawable(imageData.getImageBitmap())!!)
                    dataPart.add(dp)
                    imageList["images"] = dataPart
                } catch (e: Exception) {
                    imageList["images"] = dataPart
                }

                return imageList
            }
        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)

    }

    /**
     * Get Delete Profile Image
     * POST
     * @param secureKey
     */
    fun getDeleteProfileImage(requestQueue: RequestQueue, listener: RequestListener<String>,
                              secureKey: String) {

        NagajaLog().d("wooks, API ========= getDeleteProfileImage : secureKey = $secureKey")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + DELETE_PROFILE_IMAGE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= DELETE_PROFILE_IMAGE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

//            @Throws(AuthFailureError::class)
//            override fun getParams(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["nationphone"] = nationPhoneCode
//                params["phone"] = changePhoneNumber
//                params["code"] = authCode
//                return params
//            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Note List
     * GET
     * @param secureKey
     * @param startDate
     * @param endDate
     * @param locationUid
     * @param isCompanyNote
     * @param companyUid
     */
    fun getNoteList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<NoteData>>,
                    secureKey: String, startDate: String, endDate: String, locationUid: Int, isCompanyNote: Boolean, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getNoteList : secureKey = $secureKey / startDate: = $startDate / endDate = $endDate / locationUid = $locationUid / companyUid = $companyUid")
        NagajaLog().d("wooks, API ========= getNoteList : $API_DOMAIN$COMPANY_NOTE_LIST/$companyUid?locnuid=$locationUid&srbegindate=$startDate&srenddate=$endDate")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            if (!isCompanyNote) "$API_DOMAIN$NOTE_LIST/?locnuid=$locationUid&srbegindate=$startDate&srenddate=$endDate"
            else "$API_DOMAIN$COMPANY_NOTE_LIST/$companyUid?locnuid=$locationUid&srbegindate=$startDate&srenddate=$endDate",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTE_LIST : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val totalCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val noteListData = ArrayList<NoteData>()

                        for (i in 0 until jsonArray.length()) {
                            val noteData = NoteData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                noteData.setTotalCount(totalCount)
                                noteData.setNoteUid(jsonObject.getInt("noteUID"))
                                noteData.setNoteStatus(jsonObject.getInt("noteStatus"))
                                noteData.setNoteMessage( if (!jsonObject.isNull("noteMessage")) jsonObject.getString("noteMessage") else "" )
                                noteData.setSendMemberName( if (!jsonObject.isNull("memberNName")) jsonObject.getString("memberNName") else "" )
                                noteData.setSendMemberUid(jsonObject.getInt("memberUID"))
                                noteData.setCompanyName( if (!jsonObject.isNull("compName")) jsonObject.getString("compName") else "" )
                                noteData.setCompanyUid(jsonObject.getInt("compUID"))
                                noteData.setSendCreateDate( if (!jsonObject.isNull("sendCreateDate")) jsonObject.getString("sendCreateDate") else "" )
                                noteData.setSendUpdateDate( if (!jsonObject.isNull("sendUpdateDate")) jsonObject.getString("sendUpdateDate") else "" )
                                noteData.setNoteReceiveUid(jsonObject.getInt("noteReceiveUID"))
                                noteData.setNoteReceiveStatus(jsonObject.getInt("noteReceiveStatus"))
                                noteData.setReceiveMemberUid(jsonObject.getInt("receiveMemberUID"))
                                noteData.setReceiveMemberName( if (!jsonObject.isNull("receiveMemberName")) jsonObject.getString("receiveMemberName") else "" )
                                noteData.setReceiveCompanyUid(jsonObject.getInt("receiveCompUID"))
                                noteData.setReceiveCompanyName( if (!jsonObject.isNull("receiveCompName")) jsonObject.getString("receiveCompName") else "" )
                                noteData.setReceiveCreateDate( if (!jsonObject.isNull("receiveCreateDate")) jsonObject.getString("receiveCreateDate") else "" )
                                noteData.setReceiveUpdateDate( if (!jsonObject.isNull("receiveUpdateDate")) jsonObject.getString("receiveUpdateDate") else "" )
                                noteData.setNoteTypeUid(jsonObject.getInt("noteTypeUID"))
                            }
                            noteListData.add(noteData)
                        }

                        listener.onSuccess(noteListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Note Detail
     * GET
     * @param secureKey
     * @param noteUid
     * @param isCompanyNote
     * @param companyUid
     */
    fun getNoteDetailData(requestQueue: RequestQueue, listener: RequestListener<NoteDetailData>,
                          secureKey: String, noteUid: Int, isCompanyNote: Boolean, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getNoteDetailData : secureKey = $secureKey / noteUid: = $noteUid / isCompanyNote: = $isCompanyNote / companyUid: = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            if (!isCompanyNote) "$API_DOMAIN$NOTE_DETAIL/$noteUid"
            else "$API_DOMAIN$COMPANY_NOTE_DETAIL/$companyUid/$noteUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTE_DETAIL : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val noteDetailData = NoteDetailData()

                        noteDetailData.setNoteUid(jsonObject.getInt("noteUID"))
                        noteDetailData.setNoteStatus(jsonObject.getInt("noteStatus"))
                        noteDetailData.setNoteMessage( if (!jsonObject.isNull("noteMessage")) jsonObject.getString("noteMessage") else "" )
                        noteDetailData.setMemberUid(jsonObject.getInt("memberUID"))
                        noteDetailData.setCompanyUid(jsonObject.getInt("compUID"))
                        noteDetailData.setNoteTypeUid(jsonObject.getInt("noteTypeUID"))

                        val jsonImageArray = jsonObject.getJSONArray("noteImageList")
                        val usedMarketImageListData = ArrayList<UsedMarketImageData>()
                        for (i in 0 until jsonImageArray.length()) {
                            val usedMarketImageData = UsedMarketImageData()
                            val jsonImageObject = jsonImageArray.getJSONObject(i)
                            if (null != jsonImageObject) {
                                usedMarketImageData.setItemUid(jsonImageObject.getInt("noteImageUID"))
                                usedMarketImageData.setItemImageSort(jsonImageObject.getInt("noteImageSort"))
                                usedMarketImageData.setItemImageOrigin( if (!jsonImageObject.isNull("noteImageOrigin")) jsonImageObject.getString("noteImageOrigin") else "" )
                                usedMarketImageData.setItemImageName( if (!jsonImageObject.isNull("noteImageName")) jsonImageObject.getString("noteImageName") else "" )
                                usedMarketImageData.setCreateDate( if (!jsonImageObject.isNull("createDate")) jsonImageObject.getString("createDate") else "" )
                                usedMarketImageData.setUpdateDate( if (!jsonImageObject.isNull("updateDate")) jsonImageObject.getString("updateDate") else "" )
                            }

                            usedMarketImageListData.add(usedMarketImageData)
                        }
                        noteDetailData.setNoteImageList(usedMarketImageListData)


                        val jsonFileArray = jsonObject.getJSONArray("noteFileList")
                        val fileListData = ArrayList<FileData>()
                        for (i in 0 until jsonFileArray.length()) {
                            val fileData = FileData()
                            val jsonFileObject = jsonFileArray.getJSONObject(i)
                            if (null != jsonFileObject) {
                                fileData.setBoardFileUid(jsonFileObject.getInt("noteFileUID"))
                                fileData.setBoardFileSort(jsonFileObject.getInt("noteFileSort"))
                                fileData.setBoardFileOrigin( if (!jsonFileObject.isNull("noteFileOrigin")) jsonFileObject.getString("noteFileOrigin") else "" )
                                fileData.setBoardFileName( if (!jsonFileObject.isNull("noteFileName")) jsonFileObject.getString("noteFileName") else "" )
                                fileData.setCreateDate( if (!jsonFileObject.isNull("createDate")) jsonFileObject.getString("createDate") else "" )
                                fileData.setUpdateDate( if (!jsonFileObject.isNull("updateDate")) jsonFileObject.getString("updateDate") else "" )
                            }

                            fileListData.add(fileData)
                        }
                        noteDetailData.setNoteFileList(fileListData)

                        listener.onSuccess(noteDetailData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Note Detail Sender Data
     * GET
     * @param secureKey
     * @param noteUid
     * @param isCompanyNote
     * @param companyUid
     */
    fun getNoteDetailSenderData(requestQueue: RequestQueue, listener: RequestListener<NoteDetailSenderData>,
                                secureKey: String, noteUid: Int, isCompanyNote: Boolean, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getNoteDetailSenderData : secureKey = $secureKey / noteUid: = $noteUid / isCompanyNote: = $isCompanyNote / companyUid: = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            if (!isCompanyNote) "$API_DOMAIN$NOTE_DETAIL_SENDER/$noteUid"
            else "$API_DOMAIN$COMPANY_NOTE_DETAIL_SENDER/$companyUid/$noteUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTE_DETAIL_SENDER : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val noteDetailSenderData = NoteDetailSenderData()

                        noteDetailSenderData.setNoteUid(jsonObject.getInt("noteUID"))
                        noteDetailSenderData.setNoteStatus(jsonObject.getInt("noteStatus"))
                        noteDetailSenderData.setMemberNickName( if (!jsonObject.isNull("memberNName")) jsonObject.getString("memberNName") else "" )
                        noteDetailSenderData.setMemberUid(jsonObject.getInt("memberUID"))
                        noteDetailSenderData.setCompanyUid(jsonObject.getInt("compUID"))
                        noteDetailSenderData.setCompanyName( if (!jsonObject.isNull("compName")) jsonObject.getString("compName") else "" )
                        noteDetailSenderData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                        noteDetailSenderData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                        noteDetailSenderData.setEmail( if (!jsonObject.isNull("email")) jsonObject.getString("email") else "" )
                        noteDetailSenderData.setImageOrigin( if (!jsonObject.isNull("imageOrigin")) jsonObject.getString("imageOrigin") else "" )
                        noteDetailSenderData.setImageName( if (!jsonObject.isNull("imageName")) jsonObject.getString("imageName") else "" )

                        listener.onSuccess(noteDetailSenderData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Note Company Status Data
     * GET
     * @param secureKey
     * @param noteUid
     * @param companyUid
     * @param isCompanyNote
     */
    fun getNoteCompanyStatusData(requestQueue: RequestQueue, listener: RequestListener<NoteDetailCompanyStatusData>,
                                 secureKey: String, noteUid: Int, companyUid: Int, isCompanyNote: Boolean) {

        NagajaLog().d("wooks, API ========= getNoteCompanyStatusData : secureKey = $secureKey / noteUid: = $noteUid / companyUid: = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            if (!isCompanyNote) "$API_DOMAIN$NOTE_COMPANY_STATUS/$noteUid?cuid=$companyUid"
            else "$API_DOMAIN$COMPANY_NOTE_COMPANY_STATUS/$companyUid/$noteUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTE_COMPANY_STATUS : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val noteDetailCompanyStatusData = NoteDetailCompanyStatusData()

                        noteDetailCompanyStatusData.setNoteUid(jsonObject.getInt("noteUID"))
                        noteDetailCompanyStatusData.setIsKeepYn(jsonObject.getBoolean("keepYn"))
                        noteDetailCompanyStatusData.setIsReportYn(jsonObject.getBoolean("reportYn"))
                        noteDetailCompanyStatusData.setIsRegularYn(jsonObject.getBoolean("regularYn"))

                        listener.onSuccess(noteDetailCompanyStatusData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Note Delete
     * POST
     * @param secureKey
     * @param deleteNoteUid
     * @param companyUid
     * @param isCompanyNote
     */
    fun getNoteDelete(requestQueue: RequestQueue, listener: RequestListener<String>,
                      secureKey: String, deleteNoteUid: String, companyUid: Int, isCompanyNote: Boolean) {

        NagajaLog().d("wooks, API ========= getNoteDelete : secureKey = $secureKey / deleteNoteUid = $deleteNoteUid / companyUid = $companyUid / isCompanyNote = $isCompanyNote")
        NagajaLog().d("wooks, API ========= getNoteDelete : $API_DOMAIN$COMPANY_NOTE_DELETE/$companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            if (!isCompanyNote) API_DOMAIN + NOTE_DELETE
            else "$API_DOMAIN$COMPANY_NOTE_DELETE$companyUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTE_DELETE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["delnoteuid"] = deleteNoteUid
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Note Keep
     * POST
     * @param secureKey
     * @param noteUid
     * @param isKeep
     * @param companyUid
     * @param isCompanyNote
     */
    fun getNoteKeep(requestQueue: RequestQueue, listener: RequestListener<String>,
                    secureKey: String, noteUid: Int, isKeep: Int, companyUid: Int, isCompanyNote: Boolean) {

        NagajaLog().d("wooks, API ========= getNoteKeep : secureKey = $secureKey / noteUid = $noteUid / isKeep = $isKeep / companyUid = $companyUid / isCompanyNote = $isCompanyNote")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            if (!isCompanyNote) API_DOMAIN + NOTE_KEEP
            else "$API_DOMAIN$COMPANY_NOTE_KEEP/$companyUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                if (!isCompanyNote) {
                    NagajaLog().d("wooks, API ========= NOTE_KEEP : $response")
                } else {
                    NagajaLog().d("wooks, API ========= COMPANY_NOTE_KEEP : $response")
                }


                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener?.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["noteuid"] = noteUid.toString()
                params["keepyn"] = if (isKeep == 1) "true" else "false"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Member Information
     * GET
     * @param secureKey
     * @param memberUid
     */
    fun getMemberInformation(requestQueue: RequestQueue, listener: RequestListener<MemberInformationData>,
                             secureKey: String, memberUid: Int) {

        NagajaLog().d("wooks, API ========= getMemberInformation : secureKey = $secureKey / memberUid = $memberUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$MEMBER_INFORMATION/$memberUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTE_COMPANY_STATUS : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val memberInformationData = MemberInformationData()
                        memberInformationData.setCompanyUid(jsonObject.getInt("compUID"))
                        memberInformationData.setName( if (!jsonObject.isNull("name")) jsonObject.getString("name") else "" )
                        memberInformationData.setNationPhone( if (!jsonObject.isNull("nationPhone")) jsonObject.getString("nationPhone") else "" )
                        memberInformationData.setPhoneNumber( if (!jsonObject.isNull("phone")) jsonObject.getString("phone") else "" )
                        memberInformationData.setAddress( if (!jsonObject.isNull("address")) jsonObject.getString("address") else "" )
                        memberInformationData.setAddressDetail( if (!jsonObject.isNull("addressDetail")) jsonObject.getString("addressDetail") else "" )
                        memberInformationData.setImageOrigin( if (!jsonObject.isNull("imageOrigin")) jsonObject.getString("imageOrigin") else "" )
                        memberInformationData.setImageName( if (!jsonObject.isNull("imageName")) jsonObject.getString("imageName") else "" )
                        memberInformationData.setCategoryUid(jsonObject.getInt("cagoUID"))
                        memberInformationData.setNickName( if (!jsonObject.isNull("nname")) jsonObject.getString("nname") else "" )

                        listener.onSuccess(memberInformationData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Information
     * GET
     * @param secureKey
     * @param companyUid
     */
    fun getCompanyNoteInformation(requestQueue: RequestQueue, listener: RequestListener<MemberInformationData>,
                                  secureKey: String, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyNoteInformation : secureKey = $secureKey / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_NOTE_INFORMATION/$companyUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTE_COMPANY_STATUS : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val memberInformationData = MemberInformationData()
                        memberInformationData.setCompanyUid(jsonObject.getInt("compUID"))
                        memberInformationData.setName( if (!jsonObject.isNull("name")) jsonObject.getString("name") else "" )
                        memberInformationData.setNationPhone( if (!jsonObject.isNull("nationPhone")) jsonObject.getString("nationPhone") else "" )
                        memberInformationData.setPhoneNumber( if (!jsonObject.isNull("phone")) jsonObject.getString("phone") else "" )
                        memberInformationData.setAddress( if (!jsonObject.isNull("address")) jsonObject.getString("address") else "" )
                        memberInformationData.setAddressDetail( if (!jsonObject.isNull("addressDetail")) jsonObject.getString("addressDetail") else "" )
                        memberInformationData.setImageOrigin( if (!jsonObject.isNull("imageOrigin")) jsonObject.getString("imageOrigin") else "" )
                        memberInformationData.setImageName( if (!jsonObject.isNull("imageName")) jsonObject.getString("imageName") else "" )
                        memberInformationData.setCategoryUid(jsonObject.getInt("cagoUID"))
                        memberInformationData.setNickName( if (!jsonObject.isNull("nname")) jsonObject.getString("nname") else "" )

                        listener.onSuccess(memberInformationData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Note Send Save (Multi-Part)
     * POST Array
     * @param secureKey
     * @param noteMessage
     * @param receiveCompanyUid
     * @param receiveMemberUid
     * @param companyUid
     * @param isCompanyNote
     * @param imageUploadListData
     * @param fileUploadListData
     */
    fun getNoteSendSave(requestQueue: RequestQueue, listener: RequestListener<String>, context: Context,
                        secureKey: String, noteMessage: String, receiveCompanyUid: Int, receiveMemberUid: Int,
                        companyUid: Int, isCompanyNote: Boolean,
                        imageUploadListData: ArrayList<ImageUploadData>, fileUploadListData: ArrayList<FileUploadData>) {

        NagajaLog().d("wooks, API ========= getNoteSendSave : secureKey = $secureKey / noteMessage = $noteMessage / receiveCompanyUid = $receiveCompanyUid / receiveMemberUid = $receiveMemberUid " +
                "/ companyUid = $companyUid / isCompanyNote = $isCompanyNote" +
                "/ imageUploadListData = ${imageUploadListData.size} / fileUploadListData = ${fileUploadListData.size}")

        val volleyMultipartArrayRequest: com.nagaja.app.android.Utils.VolleyMultipartArrayRequest = object : com.nagaja.app.android.Utils.VolleyMultipartArrayRequest(
            Method.POST,
            if (!isCompanyNote) API_DOMAIN + NOTE_SEND_SAVE
            else API_DOMAIN + COMPANY_NOTE_SEND_SAVE,
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTE_SEND_SAVE : $it")

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["notemessage"] = noteMessage
                params["receivecompuid"] = receiveCompanyUid.toString()
                params["receivememberuid"] = receiveMemberUid.toString()
                if (isCompanyNote) {
                    params["compuid"] = companyUid.toString()
                    params["notetypeuid"] = "1"
                }
                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val params: MutableMap<String, ArrayList<DataPart>> = HashMap()

                if (imageUploadListData.size > 0) {
                    val dataPart: ArrayList<DataPart> = ArrayList()
                    for (i in 0 until imageUploadListData.size) {
                        val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                        dataPart.add(dp)
                    }
                    params["images"] = dataPart
                }

                if (fileUploadListData.size > 0) {
                    var iStream: InputStream? = null
                    var inputData: ByteArray? = null
                    val dataPartFile: ArrayList<DataPart> = ArrayList()
                    try {
                        for (i in 0 until fileUploadListData.size) {
                            iStream = context.contentResolver.openInputStream(fileUploadListData[i].getFileUri())
                            inputData = getBytes(iStream!!)

                            val dp = DataPart(fileUploadListData[i].getFileName(), inputData!!)
                            dataPartFile.add(dp)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    params["files"] = dataPartFile
                }

                return params
            }
        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)

    }

    /**
     * Get Company Information
     * GET
     * @param secureKey
     * @param searchType
     * @param keyWord
     */
    fun getNoteMemberSearch(requestQueue: RequestQueue, listener: RequestListener<ArrayList<NoteMemberSearchData>>,
                            secureKey: String, searchType: Int, keyWord: String) {

        NagajaLog().d("wooks, API ========= getNoteMemberSearch : secureKey = $secureKey / searchType = $searchType / keyWord = $keyWord")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_AND_MEMBER_SEARCH?tuid=$searchType&keyword=$keyWord",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_AND_MEMBER_SEARCH : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val noteMemberSearchListData = ArrayList<NoteMemberSearchData>()
                        for (i in 0 until jsonArray.length()) {
                            val noteMemberSearchData = NoteMemberSearchData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                noteMemberSearchData.setUid(jsonObject.getInt("uid"))
                                noteMemberSearchData.setName( if (!jsonObject.isNull("name")) jsonObject.getString("name") else "" )
                            }
                            noteMemberSearchListData.add(noteMemberSearchData)
                        }

                        listener.onSuccess(noteMemberSearchListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Regular List
     * GET
     * @param secureKey
     * @param nationPhoneCode
     * @param pageNow
     * @param pageCount
     * @param companyUid
     * @param isCompanyRegular
     */
    fun getRegularList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<RegularData>>,
                       secureKey: String, nationPhoneCode: String, pageNow: Int, pageCount: Int, companyUid: Int, isCompanyRegular: Boolean) {

        NagajaLog().d("wooks, API ========= getRegularList : secureKey = $secureKey / nationPhoneCode = $nationPhoneCode / pageNow = $pageNow / pageCount = $pageCount" +
                " / companyUid = $companyUid / isCompanyRegular = $isCompanyRegular")

        NagajaLog().d("wooks, API ========= getRegularList : $API_DOMAIN$COMPANY_REGULAR_LIST?nuid=$nationPhoneCode&pagenow=$pageNow&pagesz=$pageCount")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            if (!isCompanyRegular) "$API_DOMAIN$REGULAR_LIST?nuid=$nationPhoneCode&pagenow=$pageNow&pagesz=$pageCount"
            else "$API_DOMAIN$COMPANY_REGULAR_LIST/$companyUid?nuid=$nationPhoneCode&pagenow=$pageNow&pagesz=$pageCount",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= REGULAR_LIST : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val totalCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val regularListData = ArrayList<RegularData>()
                        for (i in 0 until jsonArray.length()) {
                            val regularData = RegularData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                if (jsonObject.getBoolean("useYn")) {
                                    regularData.setTotalCount(totalCount)
                                    regularData.setRegularUid(jsonObject.getInt("regularUID"))
                                    regularData.setRegularMemo( if (!jsonObject.isNull("regularMemo")) jsonObject.getString("regularMemo") else "" )
                                    regularData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                    regularData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                    regularData.setMemberUid(jsonObject.getInt("memberUID"))
                                    regularData.setMemberNickName( if (!jsonObject.isNull("memberNName")) jsonObject.getString("memberNName") else "" )
                                    regularData.setCompanyUid(jsonObject.getInt("compUID"))

                                    if (isCompanyRegular) {
                                        regularData.setMemberImageName( if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "" )
                                        regularData.setMemberImageOrigin( if (!jsonObject.isNull("memberImageOrigin")) jsonObject.getString("memberImageOrigin") else "" )
                                        regularData.setMemberNationPhone( if (!jsonObject.isNull("memNationPhone")) jsonObject.getString("memNationPhone") else "" )
                                        regularData.setMemberPhoneNumber( if (!jsonObject.isNull("memPhone")) jsonObject.getString("memPhone") else "" )
                                        regularData.setMemberName( if (!jsonObject.isNull("memName")) jsonObject.getString("memName") else "" )
                                    } else {
                                        regularData.setCompanyImageOrigin( if (!jsonObject.isNull("compImageOrigin")) jsonObject.getString("compImageOrigin") else "" )
                                        regularData.setCompanyImageName( if (!jsonObject.isNull("compImageName")) jsonObject.getString("compImageName") else "" )
                                        regularData.setCompanyName( if (!jsonObject.isNull("compName")) jsonObject.getString("compName") else "" )
                                        regularData.setCompanyNameEnglish( if (!jsonObject.isNull("compNameEng")) jsonObject.getString("compNameEng") else "" )
                                        regularData.setCompanyNationPhone( if (!jsonObject.isNull("compNationPhone")) jsonObject.getString("compNationPhone") else "" )
                                        regularData.setCompanyPhoneNumber( if (!jsonObject.isNull("compPhone")) jsonObject.getString("compPhone") else "" )
                                        regularData.setCompanyAddress( if (!jsonObject.isNull("compAddress")) jsonObject.getString("compAddress") else "" )
                                        regularData.setCompanyAddressDetail( if (!jsonObject.isNull("compAddressDetail")) jsonObject.getString("compAddressDetail") else "" )
                                    }


                                    regularListData.add(regularData)
                                }
                            }
                        }

                        listener.onSuccess(regularListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Regular List
     * GET
     * @param secureKey
     * @param nationPhoneCode
     * @param pageNow
     * @param pageCount
     */
    fun getMyUsedMarketList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<UsedMarketData>>,
                            secureKey: String, pageNow: Int, pageCount: Int, nationPhoneCode: String, status: Int) {

        NagajaLog().d("wooks, API ========= getRegularList : secureKey = $secureKey / pageNow = $pageNow / pageCount = $pageCount / nationPhoneCode = $nationPhoneCode / status = $status")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$MY_USED_MARKET_LIST?pagenow=$pageNow&pagesz=$pageCount&nuid=$nationPhoneCode&srstatus=$status",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= MY_USED_MARKET_LIST : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val totalCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val usedMarketListData = ArrayList<UsedMarketData>()

                        for (i in 0 until jsonArray.length()) {
                            val usedMarketData = UsedMarketData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                usedMarketData.setTotalCount(totalCount)
                                usedMarketData.setItemUid(jsonObject.getInt("itemUID"))
                                usedMarketData.setItemStatus(jsonObject.getInt("itemStatus"))
                                usedMarketData.setItemSubject( if (!jsonObject.isNull("itemSubject")) jsonObject.getString("itemSubject") else "" )
                                usedMarketData.setItemName( if (!jsonObject.isNull("itemName")) jsonObject.getString("itemName") else "" )
                                usedMarketData.setItemContent( if (!jsonObject.isNull("itemContent")) jsonObject.getString("itemContent") else "" )
                                usedMarketData.setItemCurrencyCode( if (!jsonObject.isNull("itemCurrencyCode")) jsonObject.getString("itemCurrencyCode") else "" )
                                usedMarketData.setItemPrice(if (!jsonObject.isNull("itemPrice")) jsonObject.getDouble("itemPrice") else 0.0)
                                usedMarketData.setItemViewCount(jsonObject.getInt("itemViewCount"))
                                usedMarketData.setItemChatCount(jsonObject.getInt("itemChatCount"))
                                usedMarketData.setItemInterestCount(jsonObject.getInt("itemInterestCount"))
                                usedMarketData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                usedMarketData.setCreateDate(if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                usedMarketData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                usedMarketData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                usedMarketData.setCategoryName( if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "" )
                                usedMarketData.setCompanyUid(jsonObject.getInt("compUID"))
                                usedMarketData.setMemberUid(jsonObject.getInt("memberUID"))
                                usedMarketData.setMemberEmail( if (!jsonObject.isNull("memberID")) jsonObject.getString("memberID") else "" )

                                val jsonImageArray = jsonObject.getJSONArray("imageList")
                                val usedMarketImageListData = ArrayList<UsedMarketImageData>()
                                for (k in 0 until jsonImageArray.length()) {
                                    val usedMarketImageData = UsedMarketImageData()
                                    val jsonImageObject = jsonImageArray.getJSONObject(k)
                                    if (null != jsonImageObject) {
                                        usedMarketImageData.setItemImageUid(jsonImageObject.getInt("itemImageUID"))
                                        usedMarketImageData.setItemImageSort(jsonImageObject.getInt("itemImageSort"))
                                        usedMarketImageData.setItemImageOrigin( if (!jsonImageObject.isNull("itemImageOrigin")) jsonImageObject.getString("itemImageOrigin") else "" )
                                        usedMarketImageData.setItemImageName( if (!jsonImageObject.isNull("itemImageName")) jsonImageObject.getString("itemImageName") else "" )
                                        usedMarketImageData.setIsUseYn(jsonImageObject.getBoolean("useYn"))
                                        usedMarketImageData.setCreateDate( if (!jsonImageObject.isNull("createDate")) jsonImageObject.getString("createDate") else "" )
                                        usedMarketImageData.setUpdateDate( if (!jsonImageObject.isNull("updateDate")) jsonImageObject.getString("updateDate") else "" )
                                        usedMarketImageData.setItemImageUid(jsonImageObject.getInt("itemUID"))
                                    }

                                    usedMarketImageListData.add(usedMarketImageData)
                                }

                                usedMarketData.setImageListData(usedMarketImageListData)

                                val jsonCurrencyArray = jsonObject.getJSONArray("currencyList")
                                val usedMarketCurrencyListData = ArrayList<UsedMarketCurrencyData>()
                                for (j in 0 until jsonCurrencyArray.length()) {
                                    val usedMarketCurrencyData = UsedMarketCurrencyData()
                                    val jsonCurrencyObject = jsonCurrencyArray.getJSONObject(j)
                                    if (null != jsonCurrencyObject) {
                                        usedMarketCurrencyData.setItemUid(jsonCurrencyObject.getInt("itemUID"))
                                        usedMarketCurrencyData.setCurrencyCode(if (!jsonCurrencyObject.isNull("currencyCode")) jsonCurrencyObject.getString("currencyCode") else "")
                                        usedMarketCurrencyData.setCurrencyPrice(if (!jsonCurrencyObject.isNull("currencyPrice")) jsonCurrencyObject.getDouble("currencyPrice") else 0.0)
                                        usedMarketCurrencyData.setCreateDate(if (!jsonCurrencyObject.isNull("createDate")) jsonCurrencyObject.getString("createDate") else "")
                                        usedMarketCurrencyData.setUpdateDate(if (!jsonCurrencyObject.isNull("updateDate")) jsonCurrencyObject.getString("updateDate") else "")
                                    }

                                    usedMarketCurrencyListData.add(usedMarketCurrencyData)
                                }

                                usedMarketData.setCurrencyListData(usedMarketCurrencyListData)


                                usedMarketData.setItemLocationUid(jsonObject.getInt("itemLocationUID"))
                                usedMarketData.setLatitude( if (!jsonObject.isNull("locationLatitude")) jsonObject.getString("locationLatitude").toString() else "" )
                                usedMarketData.setLongitude( if (!jsonObject.isNull("locationLongitude")) jsonObject.getString("locationLongitude").toString() else "" )
                                usedMarketData.setSiDoUid(jsonObject.getInt("locationSiDoUID"))
                                usedMarketData.setSiDoName( if (!jsonObject.isNull("locationSiDo")) jsonObject.getString("locationSiDo") else "" )
                                usedMarketData.setGuGunUid(jsonObject.getInt("locationGuGunUID"))
                                usedMarketData.setGuGunName( if (!jsonObject.isNull("locationGuGun")) jsonObject.getString("locationGuGun") else "" )
                                usedMarketData.setLocationDetail( if (!jsonObject.isNull("locationDetail")) jsonObject.getString("locationDetail") else "" )
                                usedMarketData.setSellerImageOrigin( if (!jsonObject.isNull("sellerImageOrigin")) jsonObject.getString("sellerImageOrigin") else "" )
                                usedMarketData.setSellerImageName( if (!jsonObject.isNull("sellerImageName")) jsonObject.getString("sellerImageName") else "" )
                                usedMarketData.setSellerName( if (!jsonObject.isNull("sellerNName")) jsonObject.getString("sellerNName") else "" )
                                usedMarketData.setIsRecommendYn(jsonObject.getBoolean("recommYn"))
                            }

                            usedMarketListData.add(usedMarketData)
                        }

                        listener.onSuccess(usedMarketListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Used Market Status Change
     * POST
     * @param secureKey
     * @param itemStatus
     * @param itemUid
     * @param companyUid
     */
    fun getUsedMarketStatusChange(requestQueue: RequestQueue, listener: RequestListener<String>,
                                  secureKey: String, itemStatus: Int, itemUid: Int, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getUsedMarketStatusChange : secureKey = $secureKey / itemStatus = $itemStatus / itemUid = $itemUid / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            if (companyUid <= 0) API_DOMAIN + USED_MARKET_STATUS_CHANGE
            else API_DOMAIN + COMPANY_USED_MARKET_STATUS_CHANGE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= USED_MARKET_STATUS_CHANGE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["itemstatus"] = itemStatus.toString()
                params["itemuid"] = itemUid.toString()
                if (companyUid > 0) {
                    params["compuid"] = companyUid.toString()
                }
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get My Job List
     * POST
     * @param secureKey
     * @param categoryUid
     * @param sort
     */
    fun getMyJobAndMissingList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<BoardData>>,
                               secureKey: String, pageNow: Int, pageCount: Int, sortType: Int, category: Int, subAreaUid: Int, isJob:Boolean) {

        NagajaLog().d("wooks, API ========= getMyJobList : secureKey = $secureKey / pageNow = $pageNow / pageCount = $pageCount / sortType = $sortType  / category = $category / subAreaUid = $subAreaUid")

        var url = MY_JOB_LIST
        if (!isJob) {
            url = MY_MISSING_LIST
        }

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$url?pagenow=$pageNow&pagesz=$pageCount&sort=$sortType&cago=$category&gugunuid=$subAreaUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= MY_JOB_LIST : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val totalCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val boardListData = ArrayList<BoardData>()
                        for (i in 0 until jsonArray.length()) {
                            val boardData = BoardData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                boardData.setTotalCount(totalCount)
                                boardData.setBoardUid(jsonObject.getInt("boardUID"))
                                boardData.setBoardStatus(jsonObject.getInt("boardStatus"))
                                boardData.setBoardSubject( if (!jsonObject.isNull("boardSubject")) jsonObject.getString("boardSubject") else "" )
                                boardData.setBoardContent( if (!jsonObject.isNull("boardContent")) jsonObject.getString("boardContent") else "" )
                                boardData.setViewCount(jsonObject.getInt("viewCount"))
                                boardData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                boardData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                boardData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                boardData.setMemberUid(jsonObject.getInt("memberUID"))
                                boardData.setMemberName( if (!jsonObject.isNull("memberNName")) jsonObject.getString("memberNName") else "" )
                                boardData.setCompanyUid(jsonObject.getInt("compUID"))
                                boardData.setCompanyName( if (!jsonObject.isNull("compName")) jsonObject.getString("compName") else "" )
                                boardData.setMemberImageOrigin( if (!jsonObject.isNull("memberImageOrigin")) jsonObject.getString("memberImageOrigin") else "" )
                                boardData.setMemberImageName( if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "" )
                                boardData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                boardData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                                boardData.setCategoryName( if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "" )
                                boardData.setRecommendCount(jsonObject.getInt("recommCount"))
                                boardData.setBookMarkCount(jsonObject.getInt("bookmarkCount"))
                                boardData.setCommentCount(jsonObject.getInt("commentCount"))
                                boardData.setIsBookMark(jsonObject.getBoolean("bookmarkYn"))
                                boardData.setIsRecommend(jsonObject.getBoolean("recommYn"))
                                boardData.setIsReport(jsonObject.getBoolean("reportYn"))
                                boardData.setIsPopularity(jsonObject.getBoolean("popularityYn"))
                                boardData.setIsStandBy(jsonObject.getBoolean("standByYn"))
                                boardData.setLocationNationUid(jsonObject.getInt("locationNationUID"))
                                boardData.setLocationMainAreaUid(jsonObject.getInt("locationSiDoUID"))
                                boardData.setLocationSubAreaUid(jsonObject.getInt("locationGuGunUID"))
                                boardData.setLocationMainAreaName( if (!jsonObject.isNull("locationSiDo")) jsonObject.getString("locationSiDo") else "" )
                                boardData.setLocationSubAreaName( if (!jsonObject.isNull("locationGuGun")) jsonObject.getString("locationGuGun") else "" )
                                boardData.setLocationDesc( if (!jsonObject.isNull("locationDesc")) jsonObject.getString("locationDesc") else "" )
                                boardData.setSnsDesc( if (!jsonObject.isNull("snsDesc")) jsonObject.getString("snsDesc") else "" )
                                boardData.setBeginDate( if (!jsonObject.isNull("beginDate")) jsonObject.getString("beginDate") else "" )
                                boardData.setEndDate( if (!jsonObject.isNull("endDate")) jsonObject.getString("endDate") else "" )
                                boardData.setMissingDate( if (!jsonObject.isNull("missingDate")) jsonObject.getString("missingDate") else "" )

                                val jsonBoardImageArray = jsonObject.getJSONArray("boardImg")
                                val usedMarketImageListData = ArrayList<UsedMarketImageData>()
                                for (k in 0 until jsonBoardImageArray.length()) {
                                    val usedMarketImageData = UsedMarketImageData()
                                    val jsonBoardImageObject = jsonBoardImageArray.getJSONObject(k)
                                    if (null != jsonBoardImageObject) {
                                        usedMarketImageData.setItemImageUid(jsonBoardImageObject.getInt("boardImageUID"))
                                        usedMarketImageData.setItemImageSort(jsonBoardImageObject.getInt("boardImageSort"))
                                        usedMarketImageData.setItemImageOrigin( if (!jsonBoardImageObject.isNull("boardImageOrigin")) jsonBoardImageObject.getString("boardImageOrigin") else "" )
                                        usedMarketImageData.setItemImageName( if (!jsonBoardImageObject.isNull("boardImageName")) jsonBoardImageObject.getString("boardImageName") else "" )
                                        usedMarketImageData.setIsUseYn(jsonBoardImageObject.getBoolean("useYn"))
                                        usedMarketImageData.setCreateDate( if (!jsonBoardImageObject.isNull("createDate")) jsonBoardImageObject.getString("createDate") else "" )
                                        usedMarketImageData.setUpdateDate( if (!jsonBoardImageObject.isNull("updateDate")) jsonBoardImageObject.getString("updateDate") else "" )
                                        usedMarketImageData.setItemUid(jsonBoardImageObject.getInt("boardUID"))
                                    }

                                    usedMarketImageListData.add(usedMarketImageData)
                                }

                                boardData.setBoardImageListData(usedMarketImageListData)

                                val jsonBoardFileArray = jsonObject.getJSONArray("boardFile")
                                val fileListData = ArrayList<FileData>()
                                for (k in 0 until jsonBoardFileArray.length()) {
                                    val fileData = FileData()
                                    val jsonBoardFileObject = jsonBoardFileArray.getJSONObject(k)
                                    if (null != jsonBoardFileObject) {
                                        fileData.setBoardFileUid(jsonBoardFileObject.getInt("boardFileUID"))
                                        fileData.setBoardFileSort(jsonBoardFileObject.getInt("boardFileSort"))
                                        fileData.setBoardFileOrigin(if (!jsonBoardFileObject.isNull("boardFileOrigin")) jsonBoardFileObject.getString("boardFileOrigin") else "")
                                        fileData.setBoardFileName(if (!jsonBoardFileObject.isNull("boardFileName")) jsonBoardFileObject.getString("boardFileName") else "")
                                        fileData.setIsUseYn(jsonBoardFileObject.getBoolean("useYn"))
                                        fileData.setCreateDate(if (!jsonBoardFileObject.isNull("createDate")) jsonBoardFileObject.getString("createDate") else "")
                                        fileData.setUpdateDate(if (!jsonBoardFileObject.isNull("updateDate")) jsonBoardFileObject.getString("updateDate") else "")
                                        fileData.setBoardUid(jsonBoardFileObject.getInt("boardUID"))
                                    }

                                    fileListData.add(fileData)
                                }

                                boardData.setBoardFileListData(fileListData)
                            }
                            boardListData.add(boardData)
                        }

                        listener?.onSuccess(boardListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message = "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Application Company (Multi-Part)
     * POST Array
     * @param secureKey
     * @param categoryUid
     * @param companyName
     * @param address
     * @param addressDetail
     * @param latitude
     * @param longitude
     * @param managerName
     * @param companyNationPhoneCode
     * @param phoneNumber
     * @param email
     * @param facebook
     * @param kakao
     * @param line
     * @param nationUid
     * @param memberUid
     * @param imageUploadListData
     * @param fileUploadListData
     */
    fun getApplicationCompany(requestQueue: RequestQueue, listener: RequestListener<String>, context: Context,
                              secureKey: String, categoryUid: Int, companyName: String, address: String, addressDetail: String, latitude: String, longitude: String,
                              managerName: String, companyNationPhoneCode: String, phoneNumber: String, email: String, facebook: String, kakao: String, line: String,
                              nationUid: String, memberUid: Int, locationNationUid: Int, locationMainUid: Int, locationSubUid: Int,
                              imageUploadListData: ArrayList<ImageUploadData>, fileUploadListData: ArrayList<FileUploadData>) {

        NagajaLog().d("wooks, API ========= getApplicationCompany : secureKey = $secureKey / categoryUid = $categoryUid / companyName = $companyName / address = $address  " +
                "/ addressDetail = $addressDetail / latitude = $latitude / longitude = $longitude / managerName = $managerName / companyNationPhoneCode = $companyNationPhoneCode" +
                "/ phoneNumber = $phoneNumber / email = $email / facebook = $facebook / kakao = $kakao / line = $line / nationUid = $nationUid / memberUid = $memberUid " +
                "/ locationNationUid = $locationNationUid / locationMainUid = $locationMainUid / locationSubUid = $locationSubUid " +
                "/ imageUploadListData = ${imageUploadListData.size} / fileUploadListData = ${fileUploadListData.size}")

        val volleyMultipartArrayRequest: com.nagaja.app.android.Utils.VolleyMultipartArrayRequest = object : com.nagaja.app.android.Utils.VolleyMultipartArrayRequest(
            Method.POST,
            API_DOMAIN + APPLICATION_COMPANY,
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= APPLICATION_COMPANY : $it")

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["cagouid"] = categoryUid.toString()
                params["compnameeng"] = companyName
                params["compaddress"] = address
                params["compaddressdetail"] = addressDetail
                params["complatitude"] = latitude
                params["complongitude"] = longitude
                params["compmanagername"] = managerName
                params["compnationphone"] = companyNationPhoneCode
                params["compphone"] = phoneNumber
                params["compemail"] = email
                params["compfacebookurl"] = facebook
                params["compkakaourl"] = kakao
                params["complineurl"] = line
                params["nationuid"] = nationUid
                params["memberuid"] = memberUid.toString()
                params["compdesc"] = ""
                params["compmanagerlimit"] = "1"
                params["locationnationuid"] = locationNationUid.toString()
                params["locationsidouid"] = locationMainUid.toString()
                params["locationgugunuid"] = locationSubUid.toString()
                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val params: MutableMap<String, ArrayList<DataPart>> = HashMap()

                if (imageUploadListData.size > 0) {
                    val dataPart: ArrayList<DataPart> = ArrayList()
                    for (i in 0 until imageUploadListData.size) {
                        val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                        dataPart.add(dp)
                    }
                    params["images"] = dataPart
                }

                if (fileUploadListData.size > 0) {
                    var iStream: InputStream? = null
                    var inputData: ByteArray? = null
                    val dataPartFile: ArrayList<DataPart> = ArrayList()
                    try {
                        for (i in 0 until fileUploadListData.size) {
                            iStream = context.contentResolver.openInputStream(fileUploadListData[i].getFileUri())
                            inputData = getBytes(iStream!!)

                            val dp = DataPart(fileUploadListData[i].getFileName(), inputData!!)
                            dataPartFile.add(dp)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    params["files"] = dataPartFile
                }

                return params
            }
        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)

    }

    /**
     * Get Modify Company Inforamtion (Multi-Part)
     * POST Array
     * @param secureKey
     * @param categoryUid
     * @param companyNameEnglish
     * @param address
     * @param addressDetail
     * @param latitude
     * @param longitude
     * @param managerName
     * @param companyNationPhoneCode
     * @param phoneNumber
     * @param email
     * @param facebook
     * @param kakao
     * @param line
     * @param nationUid
     * @param memberUid
     * @param locationNationUid
     * @param locationMainUid
     * @param locationSubUid
     * @param companyUid
     * @param deleteImageUid
     * @param companyNameEn
     * @param companyNameKr
     * @param companyNameJa
     * @param companyNameZh
     * @param companyNamePh
     * @param imageUploadListData
     * @param fileUploadListData
     */
    fun getModifyCompanyInformation(requestQueue: RequestQueue, listener: RequestListener<String>, context: Context,
                                    secureKey: String, categoryUid: Int, companyNameEnglish: String, address: String, addressDetail: String, latitude: String, longitude: String,
                                    managerName: String, companyNationPhoneCode: String, phoneNumber: String, email: String, facebook: String, kakao: String, line: String,
                                    nationUid: String, memberUid: Int, locationNationUid: Int, locationMainUid: Int, locationSubUid: Int, companyUid: Int, deleteImageUid: String,
                                    companyNameEn: String, companyNameKr: String, companyNameJa: String, companyNameZh: String, companyNamePh: String, companyDesc: String,
                                    imageUploadListData: ArrayList<ImageUploadData>, fileUploadListData: ArrayList<FileUploadData>) {

        NagajaLog().d("wooks, API ========= getModifyCompanyInformation : secureKey = $secureKey / categoryUid = $categoryUid / companyNameEnglish = $companyNameEnglish / address = $address  " +
                "/ addressDetail = $addressDetail / latitude = $latitude / longitude = $longitude / managerName = $managerName / companyNationPhoneCode = $companyNationPhoneCode" +
                "/ phoneNumber = $phoneNumber / email = $email / facebook = $facebook / kakao = $kakao / line = $line / nationUid = $nationUid / memberUid = $memberUid " +
                "/ locationNationUid = $locationNationUid / locationMainUid = $locationMainUid / locationSubUid = $locationSubUid / companyUid = $companyUid / deleteImageUid = $deleteImageUid " +
                "/ companyNameEn = $companyNameEn / companyNameKr = $companyNameKr / companyNameJa = $companyNameJa " +
                "/ companyNameZh = $companyNameZh / companyNamePh = $companyNamePh " +
                "/ imageUploadListData = ${imageUploadListData.size} / fileUploadListData = ${fileUploadListData.size}")

        val volleyMultipartArrayRequest: com.nagaja.app.android.Utils.VolleyMultipartArrayRequest = object : com.nagaja.app.android.Utils.VolleyMultipartArrayRequest(
            Method.POST,
            "$API_DOMAIN$MODIFY_COMPANY_DEFAULT_INFORMATION/$companyUid",
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= APPLICATION_COMPANY : $it")

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["cagouid"] = categoryUid.toString()
                params["compnameeng"] = companyNameEnglish
                params["compaddress"] = address
                params["compaddressdetail"] = addressDetail
                params["complatitude"] = latitude
                params["complongitude"] = longitude
                params["compmanagername"] = managerName
                params["compnationphone"] = companyNationPhoneCode
                params["compphone"] = phoneNumber
                params["compemail"] = email
                params["compfacebookurl"] = facebook
                params["compkakaourl"] = kakao
                params["complineurl"] = line
                params["nationuid"] = nationUid
                params["memberuid"] = memberUid.toString()
                params["compdesc"] = ""
                params["compmanagerlimit"] = "1"
                params["delimageuid"] = deleteImageUid
                params["compnameEn"] = companyNameEn
                params["compnameKr"] = companyNameKr
                params["compnameJa"] = companyNameJa
                params["compnameZh"] = companyNameZh
                params["compnamePh"] = companyNamePh
                params["locationnationuid"] = locationNationUid.toString()
                params["locationsidouid"] = locationMainUid.toString()
                params["locationgugunuid"] = locationSubUid.toString()
                params["compdesc"] = companyDesc
                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val params: MutableMap<String, ArrayList<DataPart>> = HashMap()

                if (imageUploadListData.size > 0) {
                    val dataPart: ArrayList<DataPart> = ArrayList()
                    for (i in 0 until imageUploadListData.size) {
                        val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                        dataPart.add(dp)
                    }
                    params["images"] = dataPart
                }

                if (fileUploadListData.size > 0) {
                    var iStream: InputStream? = null
                    var inputData: ByteArray? = null
                    val dataPartFile: ArrayList<DataPart> = ArrayList()
                    try {
                        for (i in 0 until fileUploadListData.size) {
                            iStream = context.contentResolver.openInputStream(fileUploadListData[i].getFileUri())
                            inputData = getBytes(iStream!!)

                            val dp = DataPart(fileUploadListData[i].getFileName(), inputData!!)
                            dataPartFile.add(dp)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    params["files"] = dataPartFile
                }

                return params
            }
        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)

    }

    /**
     * Get Company Reservation Detail
     * GET
     * @param secureKey
     * @param companyUid
     * @param reservationUid
     */
    fun getCompanyReservationDetail(requestQueue: RequestQueue, listener: RequestListener<ReservationData>,
                                    secureKey: String, companyUid: Int, reservationUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyReservationDetail : secureKey = $secureKey / companyUid = $companyUid / reservationUid = $reservationUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_RESERVATION_DETAIL/$companyUid/$reservationUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_RESERVATION_DETAIL : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject = json.getJSONObject("data")
                        val reservationData = ReservationData()

                        reservationData.setReservationUid(jsonObject.getInt("reservateUID"))
                        reservationData.setReservationStatus(jsonObject.getInt("reservateStatus"))
                        reservationData.setReservationClass(jsonObject.getInt("reservateClass"))
                        reservationData.setReservationBeginTime( if (!jsonObject.isNull("reservateBeginTime")) jsonObject.getString("reservateBeginTime") else "" )
                        reservationData.setReservationEndTime( if (!jsonObject.isNull("reservateEndTime")) jsonObject.getString("reservateEndTime") else "" )
                        reservationData.setReservationPersonCount(jsonObject.getInt("reservationPersonCount"))
                        reservationData.setReservationName( if (!jsonObject.isNull("reservationName")) jsonObject.getString("reservationName") else "" )
                        reservationData.setReservationNationPhone( if (!jsonObject.isNull("reservationNationPhone")) jsonObject.getString("reservationNationPhone") else "" )
                        reservationData.setReservationPhoneNumber( if (!jsonObject.isNull("reservationPhone")) jsonObject.getString("reservationPhone") else "" )
                        reservationData.setReservationMemo( if (!jsonObject.isNull("reservationMemo")) jsonObject.getString("reservationMemo") else "" )
                        reservationData.setReservationDesc( if (!jsonObject.isNull("reservationDesc")) jsonObject.getString("reservationDesc") else "" )
                        reservationData.setIsUseYn(jsonObject.getBoolean("useYn"))
                        reservationData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                        reservationData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                        reservationData.setMemberUid(jsonObject.getInt("memberUID"))
                        reservationData.setCompanyUid(jsonObject.getInt("compUID"))
                        reservationData.setScheduleTimeUid(jsonObject.getInt("scheduleTimeUID"))

                        listener.onSuccess(reservationData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Reservation Memo Save
     * POST
     * @param secureKey
     * @param comment
     * @param reservationUid
     * @param companyUid
     */
    fun getReservationMemoSave(requestQueue: RequestQueue, listener: RequestListener<String>,
                               secureKey: String, comment: String, reservationUid: Int, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getReservationMemoSave : secureKey = $secureKey / comment = $comment / reservationUid = $reservationUid / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + COMPANY_RESERVATION_MEMO_SAVE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_RESERVATION_MEMO_SAVE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["reservatecomment"] = comment
                params["reservateuid"] = reservationUid.toString()
                params["compuid"] = companyUid.toString()
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Reservation Memo List
     * GET
     * @param secureKey
     * @param companyUid
     * @param reservationUid
     */
    fun getReservationMemoList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<ReservationMemoData>>,
                               secureKey: String, companyUid: Int, reservationUid: Int) {

        NagajaLog().d("wooks, API ========= getReservationMemoList : secureKey = $secureKey / companyUid = $companyUid / reservationUid = $reservationUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_RESERVATION_MEMO_LIST/$companyUid/$reservationUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_RESERVATION_MEMO_LIST : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val reservationMemoListData = ArrayList<ReservationMemoData>()
                        for (i in 0 until jsonArray.length()) {
                            val reservationMemoData = ReservationMemoData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                reservationMemoData.setReservationCommentUid(jsonObject.getInt("reservateCommentUID"))
                                reservationMemoData.setReservationComment( if (!jsonObject.isNull("reservateComment")) jsonObject.getString("reservateComment") else "" )
                                reservationMemoData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                reservationMemoData.setReservationUid(jsonObject.getInt("reservateUID"))
                            }
                            reservationMemoListData.add(reservationMemoData)
                        }

                        listener.onSuccess(reservationMemoListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Used Market List
     * GET
     * @param secureKey
     * @param pageNow
     * @param pageCount
     * @param companyUid
     * @param nationUid
     * @param status
     */
    fun getCompanyUsedMarketList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<UsedMarketData>>,
                                 secureKey: String, pageNow: Int, pageCount: Int, companyUid: Int, nationUid: String, status: Int) {

        NagajaLog().d("wooks, API ========= getCompanyUsedMarketList : secureKey = $secureKey / pageNow = $pageNow / pageCount = $pageCount / companyUid = $companyUid" +
                " / nationUid = $nationUid / status = $status")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_USED_MARKET_LIST/$companyUid/?pagenow=$pageNow&pagesize=$pageCount&nuid=$nationUid&cpuid=$companyUid&srstatus=$status",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_USED_MARKET_LIST : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val usedMarketListData = ArrayList<UsedMarketData>()

                        for (i in 0 until jsonArray.length()) {
                            val usedMarketData = UsedMarketData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                usedMarketData.setTotalCount(json.getInt("tcount"))
                                usedMarketData.setItemUid(jsonObject.getInt("itemUID"))
                                usedMarketData.setItemStatus(jsonObject.getInt("itemStatus"))
                                usedMarketData.setItemSubject( if (!jsonObject.isNull("itemSubject")) jsonObject.getString("itemSubject") else "" )
                                usedMarketData.setItemName( if (!jsonObject.isNull("itemName")) jsonObject.getString("itemName") else "" )
                                usedMarketData.setItemContent( if (!jsonObject.isNull("itemContent")) jsonObject.getString("itemContent") else "" )
                                usedMarketData.setItemCurrencyCode( if (!jsonObject.isNull("itemCurrencyCode")) jsonObject.getString("itemCurrencyCode") else "" )
                                usedMarketData.setItemPrice(if (!jsonObject.isNull("itemPrice")) jsonObject.getDouble("itemPrice") else 0.0)
                                usedMarketData.setItemViewCount(jsonObject.getInt("itemViewCount"))
                                usedMarketData.setItemChatCount(jsonObject.getInt("itemChatCount"))
                                usedMarketData.setItemInterestCount(jsonObject.getInt("itemInterestCount"))
                                usedMarketData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                usedMarketData.setCreateDate(if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                usedMarketData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                usedMarketData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                usedMarketData.setCategoryName( if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "" )
                                usedMarketData.setCompanyUid(jsonObject.getInt("compUID"))
                                usedMarketData.setMemberUid(jsonObject.getInt("memberUID"))
                                usedMarketData.setMemberEmail( if (!jsonObject.isNull("memberID")) jsonObject.getString("memberID") else "" )

                                val jsonImageArray = jsonObject.getJSONArray("imageList")
                                val usedMarketImageListData = ArrayList<UsedMarketImageData>()
                                for (k in 0 until jsonImageArray.length()) {
                                    val usedMarketImageData = UsedMarketImageData()
                                    val jsonImageObject = jsonImageArray.getJSONObject(k)
                                    if (null != jsonImageObject) {
                                        usedMarketImageData.setItemImageUid(jsonImageObject.getInt("itemImageUID"))
                                        usedMarketImageData.setItemImageSort(jsonImageObject.getInt("itemImageSort"))
                                        usedMarketImageData.setItemImageOrigin( if (!jsonImageObject.isNull("itemImageOrigin")) jsonImageObject.getString("itemImageOrigin") else "" )
                                        usedMarketImageData.setItemImageName( if (!jsonImageObject.isNull("itemImageName")) jsonImageObject.getString("itemImageName") else "" )
                                        usedMarketImageData.setIsUseYn(jsonImageObject.getBoolean("useYn"))
                                        usedMarketImageData.setCreateDate( if (!jsonImageObject.isNull("createDate")) jsonImageObject.getString("createDate") else "" )
                                        usedMarketImageData.setUpdateDate( if (!jsonImageObject.isNull("updateDate")) jsonImageObject.getString("updateDate") else "" )
                                        usedMarketImageData.setItemImageUid(jsonImageObject.getInt("itemUID"))
                                    }

                                    usedMarketImageListData.add(usedMarketImageData)
                                }

                                usedMarketData.setImageListData(usedMarketImageListData)

                                val jsonCurrencyArray = jsonObject.getJSONArray("currencyList")
                                val usedMarketCurrencyListData = ArrayList<UsedMarketCurrencyData>()
                                for (j in 0 until jsonCurrencyArray.length()) {
                                    val usedMarketCurrencyData = UsedMarketCurrencyData()
                                    val jsonCurrencyObject = jsonCurrencyArray.getJSONObject(j)
                                    if (null != jsonCurrencyObject) {
                                        usedMarketCurrencyData.setItemUid(jsonCurrencyObject.getInt("itemUID"))
                                        usedMarketCurrencyData.setCurrencyCode(if (!jsonCurrencyObject.isNull("currencyCode")) jsonCurrencyObject.getString("currencyCode") else "")
                                        usedMarketCurrencyData.setCurrencyPrice(if (!jsonCurrencyObject.isNull("currencyPrice")) jsonCurrencyObject.getDouble("currencyPrice") else 0.0)
                                        usedMarketCurrencyData.setCreateDate(if (!jsonCurrencyObject.isNull("createDate")) jsonCurrencyObject.getString("createDate") else "")
                                        usedMarketCurrencyData.setUpdateDate(if (!jsonCurrencyObject.isNull("updateDate")) jsonCurrencyObject.getString("updateDate") else "")
                                    }

                                    usedMarketCurrencyListData.add(usedMarketCurrencyData)
                                }

                                usedMarketData.setCurrencyListData(usedMarketCurrencyListData)


                                usedMarketData.setItemLocationUid(jsonObject.getInt("itemLocationUID"))
                                usedMarketData.setLatitude( if (!jsonObject.isNull("locationLatitude")) jsonObject.getString("locationLatitude").toString() else "" )
                                usedMarketData.setLongitude( if (!jsonObject.isNull("locationLongitude")) jsonObject.getString("locationLongitude").toString() else "" )
                                usedMarketData.setSiDoUid(jsonObject.getInt("locationSiDoUID"))
                                usedMarketData.setSiDoName( if (!jsonObject.isNull("locationSiDo")) jsonObject.getString("locationSiDo") else "" )
                                usedMarketData.setGuGunUid(jsonObject.getInt("locationGuGunUID"))
                                usedMarketData.setGuGunName( if (!jsonObject.isNull("locationGuGun")) jsonObject.getString("locationGuGun") else "" )
                                usedMarketData.setLocationDetail( if (!jsonObject.isNull("locationDetail")) jsonObject.getString("locationDetail") else "" )
                                usedMarketData.setSellerImageOrigin( if (!jsonObject.isNull("sellerImageOrigin")) jsonObject.getString("sellerImageOrigin") else "" )
                                usedMarketData.setSellerImageName( if (!jsonObject.isNull("sellerImageName")) jsonObject.getString("sellerImageName") else "" )
                                usedMarketData.setSellerName( if (!jsonObject.isNull("sellerNName")) jsonObject.getString("sellerNName") else "" )
                                usedMarketData.setIsRecommendYn(jsonObject.getBoolean("recommYn"))
                            }

                            usedMarketListData.add(usedMarketData)
                        }

                        listener.onSuccess(usedMarketListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Job List
     * POST
     * @param secureKey
     * @param pageNow
     * @param pageCount
     * @param category
     * @param companyUid
     * @param nationPhoneCode
     */
    fun getCompanyJobList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<BoardData>>,
                          secureKey: String, pageNow: Int, pageCount: Int, category: Int, companyUid: Int, nationPhoneCode: String) {

        NagajaLog().d("wooks, API ========= getCompanyJobList : secureKey = $secureKey / pageNow = $pageNow / pageCount = $pageCount / category = $category" +
                " / companyUid = $companyUid / nationPhoneCode = $nationPhoneCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_JOB_LIST/$companyUid?pagenow=$pageNow&pagesz=$pageCount&cago=$category&nuid=$nationPhoneCode",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_JOB_LIST : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val totalCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val boardListData = ArrayList<BoardData>()
                        for (i in 0 until jsonArray.length()) {
                            val boardData = BoardData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                boardData.setTotalCount(totalCount)
                                boardData.setBoardUid(jsonObject.getInt("boardUID"))
                                boardData.setBoardStatus(jsonObject.getInt("boardStatus"))
                                boardData.setBoardSubject( if (!jsonObject.isNull("boardSubject")) jsonObject.getString("boardSubject") else "" )
                                boardData.setBoardContent( if (!jsonObject.isNull("boardContent")) jsonObject.getString("boardContent") else "" )
                                boardData.setViewCount(jsonObject.getInt("viewCount"))
                                boardData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                boardData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                boardData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                boardData.setMemberUid(jsonObject.getInt("memberUID"))
                                boardData.setMemberName( if (!jsonObject.isNull("memberNName")) jsonObject.getString("memberNName") else "" )
                                boardData.setCompanyUid(jsonObject.getInt("compUID"))
                                boardData.setCompanyName( if (!jsonObject.isNull("compName")) jsonObject.getString("compName") else "" )
                                boardData.setMemberImageOrigin( if (!jsonObject.isNull("memberImageOrigin")) jsonObject.getString("memberImageOrigin") else "" )
                                boardData.setMemberImageName( if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "" )
                                boardData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                boardData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                                boardData.setCategoryName( if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "" )
                                boardData.setRecommendCount(jsonObject.getInt("recommCount"))
                                boardData.setBookMarkCount(jsonObject.getInt("bookmarkCount"))
                                boardData.setCommentCount(jsonObject.getInt("commentCount"))
                                boardData.setIsBookMark(jsonObject.getBoolean("bookmarkYn"))
                                boardData.setIsRecommend(jsonObject.getBoolean("recommYn"))
                                boardData.setIsReport(jsonObject.getBoolean("reportYn"))
                                boardData.setIsPopularity(jsonObject.getBoolean("popularityYn"))
                                boardData.setIsStandBy(jsonObject.getBoolean("standByYn"))
                                boardData.setLocationNationUid(jsonObject.getInt("locationNationUID"))
                                boardData.setLocationMainAreaUid(jsonObject.getInt("locationSiDoUID"))
                                boardData.setLocationSubAreaUid(jsonObject.getInt("locationGuGunUID"))
                                boardData.setLocationMainAreaName( if (!jsonObject.isNull("locationSiDo")) jsonObject.getString("locationSiDo") else "" )
                                boardData.setLocationSubAreaName( if (!jsonObject.isNull("locationGuGun")) jsonObject.getString("locationGuGun") else "" )
                                boardData.setLocationDesc( if (!jsonObject.isNull("locationDesc")) jsonObject.getString("locationDesc") else "" )
                                boardData.setSnsDesc( if (!jsonObject.isNull("snsDesc")) jsonObject.getString("snsDesc") else "" )
                                boardData.setBeginDate( if (!jsonObject.isNull("beginDate")) jsonObject.getString("beginDate") else "" )
                                boardData.setEndDate( if (!jsonObject.isNull("endDate")) jsonObject.getString("endDate") else "" )
                                boardData.setMissingDate( if (!jsonObject.isNull("missingDate")) jsonObject.getString("missingDate") else "" )

                                val jsonBoardImageArray = jsonObject.getJSONArray("boardImg")
                                val usedMarketImageListData = ArrayList<UsedMarketImageData>()
                                for (k in 0 until jsonBoardImageArray.length()) {
                                    val usedMarketImageData = UsedMarketImageData()
                                    val jsonBoardImageObject = jsonBoardImageArray.getJSONObject(k)
                                    if (null != jsonBoardImageObject) {
                                        usedMarketImageData.setItemImageUid(jsonBoardImageObject.getInt("boardImageUID"))
                                        usedMarketImageData.setItemImageSort(jsonBoardImageObject.getInt("boardImageSort"))
                                        usedMarketImageData.setItemImageOrigin( if (!jsonBoardImageObject.isNull("boardImageOrigin")) jsonBoardImageObject.getString("boardImageOrigin") else "" )
                                        usedMarketImageData.setItemImageName( if (!jsonBoardImageObject.isNull("boardImageName")) jsonBoardImageObject.getString("boardImageName") else "" )
                                        usedMarketImageData.setIsUseYn(jsonBoardImageObject.getBoolean("useYn"))
                                        usedMarketImageData.setCreateDate( if (!jsonBoardImageObject.isNull("createDate")) jsonBoardImageObject.getString("createDate") else "" )
                                        usedMarketImageData.setUpdateDate( if (!jsonBoardImageObject.isNull("updateDate")) jsonBoardImageObject.getString("updateDate") else "" )
                                        usedMarketImageData.setItemUid(jsonBoardImageObject.getInt("boardUID"))
                                    }

                                    usedMarketImageListData.add(usedMarketImageData)
                                }

                                boardData.setBoardImageListData(usedMarketImageListData)

                                val jsonBoardFileArray = jsonObject.getJSONArray("boardFile")
                                val fileListData = ArrayList<FileData>()
                                for (k in 0 until jsonBoardFileArray.length()) {
                                    val fileData = FileData()
                                    val jsonBoardFileObject = jsonBoardFileArray.getJSONObject(k)
                                    if (null != jsonBoardFileObject) {
                                        fileData.setBoardFileUid(jsonBoardFileObject.getInt("boardFileUID"))
                                        fileData.setBoardFileSort(jsonBoardFileObject.getInt("boardFileSort"))
                                        fileData.setBoardFileOrigin(if (!jsonBoardFileObject.isNull("boardFileOrigin")) jsonBoardFileObject.getString("boardFileOrigin") else "")
                                        fileData.setBoardFileName(if (!jsonBoardFileObject.isNull("boardFileName")) jsonBoardFileObject.getString("boardFileName") else "")
                                        fileData.setIsUseYn(jsonBoardFileObject.getBoolean("useYn"))
                                        fileData.setCreateDate(if (!jsonBoardFileObject.isNull("createDate")) jsonBoardFileObject.getString("createDate") else "")
                                        fileData.setUpdateDate(if (!jsonBoardFileObject.isNull("updateDate")) jsonBoardFileObject.getString("updateDate") else "")
                                        fileData.setBoardUid(jsonBoardFileObject.getInt("boardUID"))
                                    }

                                    fileListData.add(fileData)
                                }

                                boardData.setBoardFileListData(fileListData)
                            }
                            boardListData.add(boardData)
                        }

                        listener?.onSuccess(boardListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener!!.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message = "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Modify Company Inforamtion (Multi-Part)
     * POST Array
     * @param secureKey
     * @param categoryUid
     * @param companyNameEnglish
     * @param address
     * @param addressDetail
     * @param latitude
     * @param longitude
     * @param managerName
     * @param companyNationPhoneCode
     * @param phoneNumber
     * @param email
     * @param facebook
     * @param kakao
     * @param line
     * @param nationUid
     * @param memberUid
     * @param locationNationUid
     * @param locationMainUid
     * @param locationSubUid
     * @param companyUid
     * @param deleteImageUid
     * @param companyNameEn
     * @param companyNameKr
     * @param companyNameJa
     * @param companyNameZh
     * @param companyNamePh
     * @param imageUploadListData
     * @param fileUploadListData
     */
    fun getJobModify(requestQueue: RequestQueue, listener: RequestListener<String>, context: Context,
                     secureKey: String, subject: String, content: String, categoryUid: Int, memberUid: Int,
                     nationUid: Int, mainAreaUid: Int, subAreaUid: Int, locationDesc: String, snsDesc: String, beginDate: String, endDate: String,
                     imageUploadListData: ArrayList<ImageUploadData>, fileUploadListData: ArrayList<FileUploadData>, companyUid: Int, latitude: String, longitude: String,
                     deleteImageUid: String, deleteFileUid: String, boardUid: Int) {

        NagajaLog().d("wooks, API ========= getJobModify : secureKey = $secureKey / subject = $subject / content = $content / categoryUid = $categoryUid  " +
                "/ memberUid = $memberUid / nationUid = $nationUid / mainAreaUid = $mainAreaUid / subAreaUid = $subAreaUid " +
                "/ locationDesc = $locationDesc / snsDesc = $snsDesc / beginDate = $beginDate / endDate = $endDate " +
                "/ imageUploadListData = ${imageUploadListData.size} / fileUploadListData = ${fileUploadListData.size}" +
                " / companyUid = $companyUid / latitude = $latitude / longitude = $longitude / deleteImageUid = $deleteImageUid / deleteFileUid = $deleteFileUid / boardUid = $boardUid")


        val volleyMultipartArrayRequest: VolleyMultipartArrayRequest = object : VolleyMultipartArrayRequest(
            if (companyUid > 0) Method.POST else Method.PUT,
//            "$API_DOMAIN$COMPANY_JOB_MODIFY/$boardUid",
            if (companyUid > 0) "$API_DOMAIN$COMPANY_JOB_MODIFY/$boardUid" else "$API_DOMAIN$BOARD_REGISTER_MODIFY/$boardUid",
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_JOB_MODIFY : $it")

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["boardsubject"] = subject
                params["boardcontent"] = content
//                params["useyn"] =
                params["cagouid"] = categoryUid.toString()
                params["memberuid"] = memberUid.toString()
                params["locationnationuid"] = nationUid.toString()
                params["locationsidouid"] = mainAreaUid.toString()
                params["locationgugunuid"] = subAreaUid.toString()
                params["mode"] = "job"
                params["locationdesc"] = locationDesc
                params["snsdesc"] = snsDesc
                params["begindate"] = beginDate
                params["enddate"] = endDate
                if (companyUid > 0) {
                    params["compuid"] = companyUid.toString()
                }
                params["locationlatitude"] = latitude
                params["locationlongitude"] = longitude
                params["delimageuid"] = deleteImageUid
                params["delfileuid"] = deleteFileUid
                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val params: MutableMap<String, ArrayList<DataPart>> = HashMap()

                if (imageUploadListData.size > 0) {
                    val dataPart: ArrayList<DataPart> = ArrayList()
                    for (i in 0 until imageUploadListData.size) {
                        val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                        dataPart.add(dp)
                    }
                    params["images"] = dataPart
                }

                if (fileUploadListData.size > 0) {
                    var iStream: InputStream? = null
                    var inputData: ByteArray? = null
                    val dataPartFile: ArrayList<DataPart> = ArrayList()
                    try {
                        for (i in 0 until fileUploadListData.size) {
                            iStream = context.contentResolver.openInputStream(fileUploadListData[i].getFileUri())
                            inputData = getBytes(iStream!!)

                            val dp = DataPart(fileUploadListData[i].getFileName(), inputData!!)
                            dataPartFile.add(dp)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    params["files"] = dataPartFile
                }

                return params
            }
        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)

    }

    /**
     * Get Company Product Currency List
     * GET
     * @param secureKey
     * @param companyUid
     */
    fun getCompanyProductCurrencyList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<CurrencyData>>,
                                      secureKey: String, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyProductCurrencyList : secureKey = $secureKey / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_PRODUCT_CURRENCY/$companyUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_PRODUCT_CURRENCY : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val currencyListData = ArrayList<CurrencyData>()

                        for (i in 0 until jsonArray.length()) {
                            val currencyData = CurrencyData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                currencyData.setCurrencyUid(jsonObject.getInt("currencyUID"))
                                currencyData.setCurrencyCode( if (!jsonObject.isNull("currencyCode")) jsonObject.getString("currencyCode") else "" )
                                currencyData.setCurrencyName( if (!jsonObject.isNull("currencyName")) jsonObject.getString("currencyName") else "" )
                                currencyData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                currencyData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                currencyData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                            }

                            currencyListData.add(currencyData)
                        }

                        listener.onSuccess(currencyListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Product Register
     * POST Array
     * @param secureKey
     * @param companyUid
     * @param title
     * @param content
     * @param starPoint
     * @param imageUploadListData
     */
    fun getCompanyProductRegister(requestQueue: RequestQueue, listener: RequestListener<String>,
                                  secureKey: String, itemSubject: String, itemName: String, itemContent: String, categoryUid: Int, itemCategoryUid: Int, companyUid: Int, memberUid: Int,
                                  currencyCodeKrw: String, currencyPriceKrw: String, currencyCodeUsd: String, currencyPriceUsd: String, currencyCodePhp: String, currencyPricePhp: String,
                                  currencyCodeCny: String, currencyPriceCny: String, currencyCodeJpy: String, currencyPriceJpy: String, isModify: Boolean, deleteImageUid: String, itemUid: Int,
                                  imageUploadListData: ArrayList<ImageUploadData>) {

        NagajaLog().d("wooks, API ========= getCompanyProductRegister : secureKey = $secureKey / itemSubject = $itemSubject / itemName = $itemName / itemContent = $itemContent" +
                " / categoryUid = $categoryUid / itemCategoryUid = $itemCategoryUid / companyUid = $companyUid / memberUid = $memberUid / currencyCodeKrw = $currencyCodeKrw" +
                " / currencyPriceKrw = $currencyPriceKrw / currencyCodeUsd = $currencyCodeUsd / currencyPriceUsd = $currencyPriceUsd / currencyCodePhp = $currencyCodePhp" +
                " / currencyPricePhp = $currencyPricePhp / currencyCodeCny = $currencyCodeCny / currencyPriceCny = $currencyPriceCny / currencyCodeJpy = $currencyCodeJpy" +
                " / currencyPriceJpy = $currencyPriceJpy / isModify = $isModify / deleteImageUid = $deleteImageUid / itemUid = $itemUid" +
                " / imageUploadListData = ${imageUploadListData.size}")

        val volleyMultipartArrayRequest: com.nagaja.app.android.Utils.VolleyMultipartArrayRequest = object : com.nagaja.app.android.Utils.VolleyMultipartArrayRequest(
            Method.POST,
            if (!isModify) API_DOMAIN + COMPANY_PRODUCT_SAVE
            else "$API_DOMAIN$COMPANY_PRODUCT_MODIFY/$itemUid",
            Response.Listener {
                if (null == it) {
                    return@Listener
                }

                if (!isModify) {
                    NagajaLog().d("wooks, API ========= COMPANY_PRODUCT_SAVE : $it")
                } else {
                    NagajaLog().d("wooks, API ========= COMPANY_PRODUCT_MODIFY : $it")
                }

                try {
                    val status = it.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener {
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (it is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (it is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (it is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (it is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["itemsubject"] = itemSubject
                params["itemname"] = itemName
                params["itemcontent"] = itemContent
                params["useyn"] = "true"
                params["cagouid"] = categoryUid.toString()
                params["itemcagouid"] = itemCategoryUid.toString()
                params["compuid"] = companyUid.toString()
                params["memberuid"] = memberUid.toString()
                params["currencycodekrw"] = currencyCodeKrw
                params["currencypricekrw"] = currencyPriceKrw
                params["currencycodeusd"] = currencyCodeUsd
                params["currencypriceusd"] = currencyPriceUsd
                params["currencycodephp"] = currencyCodePhp
                params["currencypricephp"] = currencyPricePhp
                params["currencycodecny"] = currencyCodeCny
                params["currencypricecny"] = currencyPriceCny
                params["currencycodejpy"] = currencyCodeJpy
                params["currencypricejpy"] = currencyPriceJpy
                if (isModify) {
                    params["delimageuid"] = deleteImageUid
                }
                return params
            }

            override fun getByteData(): MutableMap<String, ArrayList<DataPart>> {
                val imageList: MutableMap<String, ArrayList<DataPart>> = HashMap()
                val dataPart: ArrayList<DataPart> = ArrayList()
                for (i in 0 until imageUploadListData.size) {
                    val dp = DataPart(imageUploadListData[i].getFileName(), getFileDataFromDrawable(imageUploadListData[i].getImageBitmap())!!)
                    dataPart.add(dp)
                }
                imageList["images"] = dataPart
                return imageList
            }
        }

        volleyMultipartArrayRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(volleyMultipartArrayRequest)

    }

    /**
     * Get Store Category Data
     * GET
     * @param categoryUid
     * @param nationCode
     */
    fun getCompanyProductCategoryData(requestQueue: RequestQueue, listener: RequestListener<ArrayList<ProductCategoryData>>,
                                      secureKey: String, companyUid: Int, categoryUid: String, nationCode: String) {

        NagajaLog().d("wooks, API ========= getCompanyProductCategoryData : companyUid = $companyUid / categoryUid = $categoryUid / nationCode = $nationCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$COMPANY_PRODUCT_CATEGORY/$companyUid?cago=$categoryUid&nuid=$nationCode",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_PRODUCT_CATEGORY : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val productCategoryListData = ArrayList<ProductCategoryData>()

                        for (i in 0 until jsonArray.length()) {
                            val productCategoryData = ProductCategoryData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                productCategoryData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                productCategoryData.setCategoryDepth(jsonObject.getInt("cagoDepth"))
                                productCategoryData.setCategorySort(jsonObject.getInt("cagoSort"))
                                productCategoryData.setCategoryName( if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "" )
                                productCategoryData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                productCategoryData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                                productCategoryData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                                productCategoryData.setCompanyUid(jsonObject.getInt("compUID"))
                                productCategoryData.setBaseCategoryUid(jsonObject.getInt("baseCagoUID"))

                                productCategoryListData.add(productCategoryData)
                            }
                        }

                        listener.onSuccess(productCategoryListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Product Remove
     * POST
     * @param secureKey
     * @param itemUid
     * @param companyUid
     */
    fun getCompanyProductRemove(requestQueue: RequestQueue, listener: RequestListener<String>,
                                secureKey: String, itemUid: Int, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getCompanyProductRemove : secureKey = $secureKey / itemUid = $itemUid / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            "$API_DOMAIN$COMPANY_PRODUCT_REMOVE/$companyUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_PRODUCT_REMOVE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener?.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["itemuid"] = itemUid.toString()
                params["useyn"] = "false"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Notification List
     * GET
     * @param secureKey
     * @param pageNow
     * @param pageCount
     * @param selectType
     */
    fun getNotificationList(requestQueue: RequestQueue, listener: RequestListener<ArrayList<NotificationData>>,
                            secureKey: String, pageNow: Int, pageCount: Int, selectType: Int) {

        NagajaLog().d("wooks, API ========= getNotificationList : secureKey = $secureKey / pageNow = $pageNow / pageCount = $pageCount / selectType = $selectType")

        var url = "$API_DOMAIN$NOTIFICATION_LIST/?pagenow=$pageNow&pagesz=$pageCount"
        if (selectType > 0) {
            url = "$url&srstatus=0"
        }

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            url,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTIFICATION_LIST : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val notificationListData = ArrayList<NotificationData>()

                        for (i in 0 until jsonArray.length()) {
                            val notificationData = NotificationData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                notificationData.setTotalCount(json.getInt("tcount"))
                                notificationData.setPushUid(jsonObject.getInt("pushUID"))
                                notificationData.setPushTypeUid(jsonObject.getInt("pushTypeUID"))
                                notificationData.setPushClassUid(jsonObject.getInt("pushClassUID"))
                                notificationData.setPushStatus(jsonObject.getInt("pushStatus"))
                                notificationData.setPushSubject( if (!jsonObject.isNull("pushSubject")) jsonObject.getString("pushSubject") else "" )
                                notificationData.setPushMemo( if (!jsonObject.isNull("pushMemo")) jsonObject.getString("pushMemo") else "" )
                                notificationData.setMemberUid(jsonObject.getInt("memberUID"))
                                notificationData.setFirstTargetUid(jsonObject.getInt("firstTargetUID"))
                                notificationData.setSecondTargetUid(jsonObject.getInt("secondTargetUID"))
                                notificationData.setCompanyUid(jsonObject.getInt("compUID"))
                                notificationData.setPushSendDate( if (!jsonObject.isNull("pushSendDate")) jsonObject.getString("pushSendDate") else "" )
                                notificationData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                notificationData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                            }

                            notificationListData.add(notificationData)
                        }

                        listener.onSuccess(notificationListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Notification Confirm
     * GET
     * @param secureKey
     * @param pageNow
     * @param pageCount
     * @param selectType
     */
    fun getNotificationConfirm(requestQueue: RequestQueue, listener: RequestListener<String>,
                               secureKey: String, pushUid: Int) {

        NagajaLog().d("wooks, API ========= getNotificationConfirm : secureKey = $secureKey / pushUid = $pushUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$NOTIFICATION_CONFIRM/$pushUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= NOTIFICATION_CONFIRM : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Search
     * POST
     * @param pageNow
     * @param pageCount
     * @param searchType
     * @param searchWord
     * @param nationCode
     * @param subAreaUid
     */
    fun getSearch(requestQueue: RequestQueue, listener: RequestListener<ArrayList<SearchData>>,
                  pageNow: Int, pageCount: Int, searchType: Int, searchWord: String, nationCode: String, subAreaUid: Int) {

        NagajaLog().d("wooks, API ========= getSearch : pageNow = $pageNow / pageCount = $pageCount / searchType = $searchType / searchWord = $searchWord" +
                " / nationCode = $nationCode / subAreaUid = $subAreaUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + SEARCH,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= SEARCH : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    val totalCount = json.getInt("tcount")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val searchListData = ArrayList<SearchData>()

                        for (i in 0 until jsonArray.length()) {
                            val searchData = SearchData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                searchData.setTotalCount(totalCount)
                                searchData.setUid(jsonObject.getInt("uid"))
                                searchData.setRootCategoryUid(jsonObject.getInt("rootCagoUID"))
                                searchData.setRootCategoryName( if (!jsonObject.isNull("rootCagoName")) jsonObject.getString("rootCagoName") else "" )
                                searchData.setContent( if (!jsonObject.isNull("content")) jsonObject.getString("content") else "" )
                                searchData.setImageName( if (!jsonObject.isNull("imageName")) jsonObject.getString("imageName") else "" )
                                searchData.setImageOrigin( if (!jsonObject.isNull("imageOrigin")) jsonObject.getString("imageOrigin") else "" )
                                searchData.setReviewPointAverage(jsonObject.getDouble("reviewPointAverage"))
                                searchData.setRecommendCount(jsonObject.getInt("compRecommCount"))
                                searchData.setServiceBeginTime( if (!jsonObject.isNull("serviceBeginTime")) jsonObject.getString("serviceBeginTime") else "" )
                                searchData.setServiceEndTime( if (!jsonObject.isNull("serviceEndTime")) jsonObject.getString("serviceEndTime") else "" )
                                searchData.setItemStatus(jsonObject.getInt("itemStatus"))
                                searchData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )

                                val jsonCurrencyArray = jsonObject.getJSONArray("currencyList")
                                val searchCurrencyListData = ArrayList<SearchCurrencyData>()
                                for (k in 0 until jsonCurrencyArray.length()) {
                                    val searchCurrencyData = SearchCurrencyData()
                                    val jsonCurrencyObject = jsonCurrencyArray.getJSONObject(k)
                                    if (null != jsonCurrencyObject) {
                                        searchCurrencyData.setCode( if (!jsonCurrencyObject.isNull("currencyCode")) jsonCurrencyObject.getString("currencyCode") else "" )
                                        searchCurrencyData.setPrice(jsonCurrencyObject.getDouble("currencyPrice"))
                                    }

                                    searchCurrencyListData.add(searchCurrencyData)
                                }

                                searchData.setCurrencyListData(searchCurrencyListData)
                            }

                            searchListData.add(searchData)
                        }

                        listener.onSuccess(searchListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "Bearer $secureKey"
//                return params
//            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["pagenow"] = pageNow.toString()
                params["pagesz"] = pageCount.toString()
                params["sr"] = searchType.toString()
                params["srtxt"] = searchWord
                params["nuid"] = nationCode
                params["gugunuid"] = subAreaUid.toString()
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Logout
     * PUT
     * @param secureKey
     * @param String
     */
    fun getLogout(requestQueue: RequestQueue, listener: RequestListener<String>,
                  secureKey: String, email: String) {

        NagajaLog().d("wooks, API ========= getLogout : secureKey = $secureKey / email = $email")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.PUT,
            API_DOMAIN + LOGOUT,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= LOGOUT : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
//                params["Authorization"] = "bearer $secureKey"
                return params
            }

            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("emailid", email)
                jsonBody.put("securekey", secureKey)
                return jsonBody.toString().toByteArray()
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get SNS Email Check
     * POST
     * @param secureKey
     * @param itemUid
     * @param companyUid
     */
    fun getSnsEmailCheck(requestQueue: RequestQueue, listener: RequestListener<UserData>,
                         snsUserData: SnsUserData) {

        NagajaLog().d("wooks, API ========= getSnsEmailCheck : snsUserData.getLoginType() = ${snsUserData.getLoginType()} / snsUserData.getUserEmail() = ${snsUserData.getUserEmail()}" +
                " / snsUserData.getUserName() = ${snsUserData.getUserName()} / snsUserData.getUsedId() = ${snsUserData.getUsedId()}")

        var snsType = "google"
        when (snsUserData.getLoginType()) {
            LOGIN_TYPE_GOOGLE -> {
                snsType = "google"
            }

            LOGIN_TYPE_KAKAO -> {
                snsType = "kakao"
            }

            LOGIN_TYPE_NAVER -> {
                snsType = "naver"
            }

            LOGIN_TYPE_FACEBOOK -> {
                snsType = "facebook"
            }

        }

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            "$API_DOMAIN$SNS_EMAIL_CHECK/$snsType",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= SNS_EMAIL_CHECK : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val userData = UserData()
                        userData.setMemberUid(jsonObject.getInt("memberUID"))
                        userData.setMemberGrant(jsonObject.getInt("memGrant"))
                        userData.setMemberStatus(jsonObject.getInt("memStatus"))
                        userData.setMemberType(jsonObject.getInt("memType"))
                        userData.setMemberReferral(jsonObject.getString("memReferral"))
                        userData.setMemberId(jsonObject.getString("memberID"))
                        userData.setName(jsonObject.getString("memName"))
                        userData.setNickName(jsonObject.getString("memNName"))
                        userData.setEmailId(jsonObject.getString("memEmailID"))
                        userData.setNationPhone(jsonObject.getString("memNationPhone"))
                        userData.setPhoneNumber(jsonObject.getString("memPhone"))
                        userData.setNationUid(jsonObject.getInt("nationUID"))
                        userData.setSocialUid(jsonObject.getInt("socialUID"))
                        userData.setSocialSecureKey( if (!jsonObject.isNull("socialSecureKey")) jsonObject.getString("socialSecureKey") else "")
                        userData.setSecureToken( if (!jsonObject.isNull("secureToken")) jsonObject.getString("secureToken") else "")
                        userData.setSecureKey(jsonObject.getString("secureKey"))
                        userData.setRefreshKey(jsonObject.getString("refreshKey"))
                        userData.setIsEmailAuth(jsonObject.getBoolean("confirmEmailIDYn"))
                        userData.setIsPhoneAuth(jsonObject.getBoolean("confirmPhoneYn"))
                        userData.setIsNameAuth(jsonObject.getBoolean("confirmUserNameYn"))
                        userData.setLoginDate( if (!jsonObject.isNull("loginDate")) jsonObject.getString("loginDate") else "")
                        userData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "")
                        userData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "")
                        userData.setLocationUid(jsonObject.getInt("memLocationUID"))
                        userData.setLatitude(jsonObject.getString("memLatitude"))
                        userData.setLongitude(jsonObject.getString("memLongitude"))
                        userData.setAddressCode(jsonObject.getString("memAddressZipCode"))
                        userData.setAddress(jsonObject.getString("memAddress"))
                        userData.setAddressDetail( if (!jsonObject.isNull("memAddressDetail")) jsonObject.getString("memAddressDetail") else "" )
                        userData.setProfileUrl( if (!jsonObject.isNull("memberImageOrigin")) jsonObject.getString("memberImageOrigin") else "")
                        userData.setProfileName( if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "")

                        listener.onSuccess(userData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_CODE_NO_MEMBER,
                            ERROR_CODE_DIFFERENT_TYPE_SIGN_UP -> {
                                val jsonObject: JSONObject = json.getJSONObject("data")
                                val userData = UserData()
                                userData.setErrorStatus(errorCode)
                                userData.setDifferentUserType(jsonObject.getInt("socialUID"))
                                userData.setSocialUid(jsonObject.getInt("memberSocialUID"))

                                listener.onSuccess(userData)

                                return@Listener
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "Bearer $secureKey"
//                return params
//            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["id"] = snsUserData.getUsedId()!!
                params["name"] = snsUserData.getUserName()!!
                params["email"] = snsUserData.getUserEmail()!!
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Member Sign Up
     * POST
     * @param name
     * @param email
     * @param nickName
     * @param nationPhoneCode
     * @param phoneNumber
     * @param nationCode
     * @param referral
     * @param latitude
     * @param longitude
     * @param zipCode
     * @param address
     * @param addressDetail
     * @param phoneNumberAuthUid
     * @param phoneNumberAuthSecureCode
     * @param socialUid
     * @param socialConfirmAuthUid
     * @param isBenefitNotification
     * @param isServiceNotification
     */
    fun getMemberSignUp(requestQueue: RequestQueue, listener: RequestListener<String>,
                        name: String, email: String, nickName: String, nationPhoneCode: String, phoneNumber: String, nationCode: String, referral: String,
                        latitude: String, longitude: String, zipCode: String, address: String, addressDetail: String, phoneNumberAuthUid: String, phoneNumberAuthSecureCode: String,
                        socialUid: String, socialConfirmAuthUid: String, isBenefitNotification: Boolean, isServiceNotification: Boolean, isSocial: Boolean, password: String, memberLocationUid: String) {

        NagajaLog().d("wooks, API ========= getSocialMemberSignUp : name = $name / email = $email / nickName = $nickName / nationPhoneCode = $nationPhoneCode" +
                " / phoneNumber = $phoneNumber / nationCode = $nationCode / referral = $referral / latitude = $latitude / longitude = $longitude / zipCode = $zipCode / address = $address" +
                " / addressDetail = $addressDetail / phoneNumberAuthUid = $phoneNumberAuthUid / phoneNumberAuthSecureCode = $phoneNumberAuthSecureCode / socialUid = $socialUid / socialConfirmAuthUid = $socialConfirmAuthUid" +
                " / isBenefitNotification = $isBenefitNotification / isServiceNotification = $isServiceNotification / isSocial = $isSocial")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            if (isSocial) "$API_DOMAIN$SOCIAL_MEMBER_SIGN_UP" else "$API_DOMAIN$MEMBER_SIGN_UP",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                if (isSocial) {
                    NagajaLog().d("wooks, API ========= SOCIAL_MEMBER_SIGN_UP : $response")
                } else {
                    NagajaLog().d("wooks, API ========= MEMBER_SIGN_UP : $response")
                }

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_WRONG_FORMAT_PASSWORD -> {
                                errorCode = ERROR_CODE_WRONG_FORMAT_PASSWORD
                            }

                            ERROR_CODE_NO_RECOMMEND -> {
                                errorCode = ERROR_CODE_NO_RECOMMEND
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "Bearer $secureKey"
//                return params
//            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["memname"] = name
                params["mememailid"] = email
                params["memberid"] = email
                params["memnname"] = nickName
                params["memnationphone"] = nationPhoneCode
                params["memphone"] = phoneNumber
                params["nationuid"] = nationCode
                params["memreferral"] = referral
                params["memlatitude"] = latitude
                params["memlongitude"] = longitude
                params["memaddresszipcode"] = zipCode
                params["memaddress"] = address
                params["memaddressdetail"] = addressDetail
                params["confirmUID"] = phoneNumberAuthUid
                params["secureCode"] = phoneNumberAuthSecureCode
                params["agreetypeserviceyn"] = "1"
                params["agreetypepersonyn"] = "1"
                params["agreetype3rduseyn"] = "1"
                params["agreetypelocationyn"] = "1"
                params["agreetypealarmbenefitsyn"] = if (isBenefitNotification) "1" else "0"
                params["agreetypeservicealarmyn"] = if (isServiceNotification) "1" else "0"
                params["memlocationuid"] = memberLocationUid

                if (isSocial) {
                    params["socialuid"] = socialUid
                    params["membersocialuid"] = socialConfirmAuthUid
                } else {
                    params["socialuid"] = "1"
                    params["password"] = password
                }
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Sign Up Send Email
     * POST
     * @param email
     */
    fun getSignUpSendEmail(requestQueue: RequestQueue, listener: RequestListener<String>,
                           email: String) {

        NagajaLog().d("wooks, API ========= getSignUpSendEmail : email = $email")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            "$API_DOMAIN$SIGN_UP_SEND_EMAIL",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= SIGN_UP_SEND_EMAIL : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "Bearer $secureKey"
//                return params
//            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["memberid"] = email
                params["confirmtypeuid"] = "2"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Member Find Password Email Check
     * POST
     * @param email
     */
    fun getMemberFindPasswordEmailCheck(requestQueue: RequestQueue, listener: RequestListener<String>,
                                        email: String) {

        NagajaLog().d("wooks, API ========= getMemberFindPasswordEmailCheck : email = $email")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            "$API_DOMAIN$MEMBER_FIND_PASSWORD_EMAIL_CHECK",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= MEMBER_FIND_PASSWORD_EMAIL_CHECK : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_NOT_FOUND_ID -> {
                                errorCode = ERROR_CODE_NOT_FOUND_ID
                            }

                            ERROR_CODE_DIFFERENT_TYPE_SIGN_UP -> {
                                errorCode = ERROR_CODE_DIFFERENT_TYPE_SIGN_UP
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "Bearer $secureKey"
//                return params
//            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["memberid"] = email
                params["confirmtypeuid"] = "2"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Member Change Password
     * POST
     * @param email
     * @param phoneAuthCode
     * @param nationPhone
     * @param phoneNumber
     * @param password
     */
    fun getMemberChangePassword(requestQueue: RequestQueue, listener: RequestListener<String>,
                                email: String, phoneAuthCode: String, nationPhone: String, phoneNumber: String, password: String) {

        NagajaLog().d("wooks, API ========= getMemberChangePassword : email = $email / phoneAuthCode = $phoneAuthCode / nationPhone = $nationPhone" +
                " / phoneNumber = $phoneNumber / password = $password")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            "$API_DOMAIN$MEMBER_CHANGE_PASSWORD",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= MEMBER_CHANGE_PASSWORD : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_DO_NOT_MATCH_ID -> {
                                errorCode = ERROR_CODE_DO_NOT_MATCH_ID
                            }

                            ERROR_CODE_WRONG_FORMAT_PASSWORD -> {
                                errorCode = ERROR_CODE_WRONG_FORMAT_PASSWORD
                            }

                            ERROR_CODE_OLD_PASSWORD -> {
                                errorCode = ERROR_CODE_OLD_PASSWORD
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "Bearer $secureKey"
//                return params
//            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["memberid"] = email
                params["code"] = phoneAuthCode
                params["nationphone"] = nationPhone
                params["phone"] = phoneNumber
                params["mempassword"] = password
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Service Area Location
     * GET
     * @param locationUid
     * @param nationPhone
     */
    fun getServiceAreaLocation(requestQueue: RequestQueue, listener: RequestListener<ArrayList<ServiceAreaLocationData>>,
                               locationUid: Int, nationPhone: String) {

        NagajaLog().d("wooks, API ========= getServiceAreaLocation : locationUid = $locationUid / nationPhone = $nationPhone")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$SERVICE_AREA_LOCATION?puid=$locationUid&nuid=$nationPhone",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= SERVICE_AREA_LOCATION : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val serviceAreaLocationListData = ArrayList<ServiceAreaLocationData>()
                        for (i in 0 until jsonArray.length()) {
                            val serviceAreaLocationData = ServiceAreaLocationData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                serviceAreaLocationData.setCategoryUid(jsonObject.getInt("cagoUID"))
                                serviceAreaLocationData.setCategoryDepth(jsonObject.getInt("cagoDepth"))
                                serviceAreaLocationData.setCategorySort(jsonObject.getInt("cagoSort"))
                                serviceAreaLocationData.setCategoryStatus(jsonObject.getInt("cagoStatus"))
                                serviceAreaLocationData.setCategoryName( if (!jsonObject.isNull("cagoName")) jsonObject.getString("cagoName") else "" )
                                serviceAreaLocationData.setCategoryEnglishName( if (!jsonObject.isNull("cagoNameEng")) jsonObject.getString("cagoNameEng") else "" )
                                serviceAreaLocationData.setParentCategoryUid(jsonObject.getInt("parentCagoUID"))
                                serviceAreaLocationData.setParentCategoryName( if (!jsonObject.isNull("parentCagoName")) jsonObject.getString("parentCagoName") else "" )
                                serviceAreaLocationData.setCategoryLatitude(jsonObject.getDouble("cagoLatitude"))
                                serviceAreaLocationData.setCategoryLongitude(jsonObject.getDouble("cagoLongitude"))
                                serviceAreaLocationData.setRootCategoryUid(jsonObject.getInt("rootCagoUID"))
                                serviceAreaLocationData.setIsUseYn(jsonObject.getBoolean("useYn"))
                                serviceAreaLocationData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                            }
                            serviceAreaLocationListData.add(serviceAreaLocationData)
                        }

                        listener.onSuccess(serviceAreaLocationListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "Bearer $secureKey"
//                return params
//            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Service Area Location
     * GET
     * @param locationUid
     * @param nationPhone
     */
    fun getExchangeRate(requestQueue: RequestQueue, listener: RequestListener<ArrayList<ExchangeRateData>>) {

        NagajaLog().d("wooks, API ========= getExchangeRate : ")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            "$API_DOMAIN$EXCHANGE_RATE",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= EXCHANGE_RATE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val exchangeRateListData = ArrayList<ExchangeRateData>()
                        for (i in 0 until jsonArray.length()) {
                            val exchangeRateData = ExchangeRateData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                exchangeRateData.setCurrencyCode( if (!jsonObject.isNull("currencyCode")) jsonObject.getString("currencyCode") else "" )
                                exchangeRateData.setCurrencyDate( if (!jsonObject.isNull("currencyDate")) jsonObject.getString("currencyDate") else "" )
                                exchangeRateData.setCurrencyBegin(jsonObject.getDouble("currencyBegin"))
                                exchangeRateData.setCurrencyNow(jsonObject.getDouble("currencyNow"))
                                exchangeRateData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                                exchangeRateData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                            }
                            exchangeRateListData.add(exchangeRateData)
                        }

                        listener.onSuccess(exchangeRateListData)
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "Bearer $secureKey"
//                return params
//            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Member Change Password
     * POST
     * @param secureKey
     * @param locationUid
     * @param latitude
     * @param longitude
     * @param zipCode
     * @param address
     * @param addressDetail
     */
    fun getChangeAddress(requestQueue: RequestQueue, listener: RequestListener<String>,
                         secureKey: String, locationUid: String, latitude: String, longitude: String, zipCode: String, address: String, addressDetail: String) {

        NagajaLog().d("wooks, API ========= getChangeAddress : secureKey = $secureKey / locationUid = $locationUid / latitude = $latitude / longitude = $longitude / zipCode = $zipCode" +
                " / address = $address / addressDetail = $addressDetail")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            "$API_DOMAIN$CHANGE_ADDRESS",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= CHANGE_ADDRESS : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["memlocationuid"] = locationUid
                params["memlatitude"] = latitude
                params["memlongitude"] = longitude
                params["memaddresszipcode"] = zipCode
                params["memaddress"] = address
                params["memaddressdetail"] = addressDetail
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Sign Up Email And Nick Name Check
     * POST
     * @param checkString
     * @param isEmail
     */
    fun getSignUpEmailAndNickNameCheck(requestQueue: RequestQueue, listener: RequestListener<String>,
                                       checkString: String, isEmail: Boolean) {

        NagajaLog().d("wooks, API ========= getSignUpEmailAndNickNameCheck : checkString = $checkString / isEmail = $isEmail")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            if (isEmail) "$API_DOMAIN$SIGN_UP_EMAIL_CHECK" else "$API_DOMAIN$SIGN_UP_NICK_NAME_CHECK",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                if (isEmail) {
                    NagajaLog().d("wooks, API ========= SIGN_UP_EMAIL_CHECK : $response")
                } else {
                    NagajaLog().d("wooks, API ========= SIGN_UP_NICK_NAME_CHECK : $response")
                }

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_EXISTING_ID -> {
                                errorCode = ERROR_CODE_EXISTING_ID
                            }

                            ERROR_CODE_EXISTING_NICK_NAME -> {
                                errorCode = ERROR_CODE_EXISTING_NICK_NAME
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "Bearer $secureKey"
//                return params
//            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                if (isEmail) {
                    params["email"] = checkString
                } else {
                    params["nick"] = checkString
                }
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Login Success Device Information
     * POST
     * @param checkString
     * @param isEmail
     */
    fun getLoginSuccessDeviceInformation(requestQueue: RequestQueue, listener: RequestListener<String>,
                                         secureKey: String, uuid: String, os: String, fcmToken: String) {

        NagajaLog().d("wooks, API ========= getLoginSuccessDeviceInformation : secureKey = $secureKey / uuid = $uuid / fcmToken = $fcmToken")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + LOGIN_SUCCESS_DEVICE_INFORMATION,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= LOGIN_SUCCESS_DEVICE_INFORMATION : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_EXISTING_ID -> {
                                errorCode = ERROR_CODE_EXISTING_ID
                            }

                            ERROR_CODE_EXISTING_NICK_NAME -> {
                                errorCode = ERROR_CODE_EXISTING_NICK_NAME
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["uuid"] = uuid
                params["os"] = os
                params["fcmtoken"] = fcmToken
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get User Device Information
     * POST
     */
    fun getUserDeviceInformation(requestQueue: RequestQueue, listener: RequestListener<String?>?,
                                 uuid: String, os: String, model: String, version: String, location: String, fcmToken: String, language: String) {

        NagajaLog().d("wooks, API ========= getUserDeviceInformation : uuid = $uuid / os = $os / model = $model / version = $version / location = $location / fcmtoken = $fcmToken / language = $language")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + DEVICE_INFORMATION,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= DEVICE_INFORMATION : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener?.onSuccess("success")
                    } else {
                        var errorCode = 0x01
//                        when (status) {
//                            11 -> {
//                                errorCode = VERSION_CHECK_ERROR_REQUEST_OS_EMPTY_DATA
//                            }
//
//                            0 -> {
//                                errorCode = VERSION_CHECK_ERROR_REQUEST_ERROR
//                            }
//                        }
                        listener?.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String>? {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "aaaaaaaaaaaaaaaaaaaaaaaa"
//                return params
//            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["uuid"] = uuid
                params["os"] = os
                params["model"] = model
                params["version"] = version
                params["location"] = location
                params["fcmtoken"] = fcmToken
                params["language"] = language
                return params
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Company Reservation Status Change
     * POST
     */
    fun getCompanyReservationStatusChange(requestQueue: RequestQueue, listener: RequestListener<String>,
                                          secureKey: String, reservationUid: Int, companyUid: Int, status: Int) {

        NagajaLog().d("wooks, API ========= getCompanyReservationStatusChange : secureKey = $secureKey / reservationUid = $reservationUid / companyUid = $companyUid / status = $status")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + COMPANY_RESERVATION_STATUS_CHANGE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= COMPANY_RESERVATION_STATUS_CHANGE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener?.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["reservateuid"] = reservationUid.toString()
                params["compuid"] = companyUid.toString()
                params["reservatestatus"] = status.toString()
                return params
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Language Change
     * PUT
     * @param secureKey
     * @param nationUid
     */
    fun getLanguageChange(requestQueue: RequestQueue, listener: RequestListener<String>,
                          secureKey: String, nationUid: String) {

        NagajaLog().d("wooks, API ========= getLanguageChange : secureKey = $secureKey / nationUid = $nationUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + LANGUAGE_CHANGE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= LANGUAGE_CHANGE : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["nationuid"] = nationUid
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Note Receiver
     * GET
     * @param secureKey
     * @param noteUid
     * @param isCompanyNote
     * @param isSender
     * @param uid
     */
    fun getNoteReceiver(requestQueue: RequestQueue, listener: RequestListener<NoteReceiverData>,
                        secureKey: String, noteUid: Int, isCompanyNote: Boolean, uid: Int) {

        NagajaLog().d("wooks, API ========= getNoteReceiver : secureKey = $secureKey / noteUid = $noteUid / isCompanyNote = $isCompanyNote / uid = $uid")
        var url = ""
        if (isCompanyNote) {
            url = "$API_DOMAIN$NOTE_COMPANY_RECEIVER/$uid/$noteUid"
        } else {
            url = "$API_DOMAIN$NOTE_MEMBER_RECEIVER/$noteUid"
        }

        NagajaLog().d("wooks, API ========= getNoteReceiver : $url")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            url,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                if (uid > 0) {
                    NagajaLog().d("wooks, API ========= NOTE_COMPANY_RECEIVER : $response")
                } else {
                    NagajaLog().d("wooks, API ========= NOTE_MEMBER_RECEIVER : $response")
                }


                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val noteReceiverData = NoteReceiverData()
                        noteReceiverData.setNoteReceiveUid(jsonObject.getInt("noteReceiveUID"))
                        noteReceiverData.setNoteReceiveStatus(jsonObject.getInt("noteReceiveStatus"))
                        noteReceiverData.setReceiveMemberUid(jsonObject.getInt("receiveMemberUID"))
                        noteReceiverData.setReceiveMemberName( if (!jsonObject.isNull("receiveMemberNName")) jsonObject.getString("receiveMemberNName") else "" )
                        noteReceiverData.setReceiveCompanyUid(jsonObject.getInt("receiveCompUID"))
                        noteReceiverData.setReceiveCompanyName( if (!jsonObject.isNull("receiveCompName")) jsonObject.getString("receiveCompName") else "" )
                        noteReceiverData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                        noteReceiverData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                        noteReceiverData.setNoteUid(jsonObject.getInt("noteUID"))
                        noteReceiverData.setIsReceiveReport(jsonObject.getBoolean("receiveReportYn"))
                        noteReceiverData.setIsReceiveKeep(jsonObject.getBoolean("receiveKeepYn"))
                        noteReceiverData.setIsReceiveRegular(jsonObject.getBoolean("receiveRegularYn"))
                        noteReceiverData.setEmail( if (!jsonObject.isNull("email")) jsonObject.getString("email") else "" )
                        noteReceiverData.setImageOrigin( if (!jsonObject.isNull("imageOrigin")) jsonObject.getString("imageOrigin") else "" )
                        noteReceiverData.setImageName( if (!jsonObject.isNull("imageName")) jsonObject.getString("imageName") else "" )

                        listener.onSuccess(noteReceiverData)

                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Note Sender
     * GET
     * @param secureKey
     * @param noteUid
     * @param isCompanyNote
     * @param isSender
     * @param uid
     */
    fun getNoteSender(requestQueue: RequestQueue, listener: RequestListener<NoteSenderData>,
                        secureKey: String, noteUid: Int, isCompanyNote: Boolean, uid: Int) {

        NagajaLog().d("wooks, API ========= getNoteSender : secureKey = $secureKey / noteUid = $noteUid / isCompanyNote = $isCompanyNote / uid = $uid")
        var url = ""
        if (isCompanyNote) {
            url = "$API_DOMAIN$NOTE_COMPANY_SENDER/$uid/$noteUid"
        } else {
            url = "$API_DOMAIN$NOTE_MEMBER_SENDER/$noteUid"
        }

        NagajaLog().d("wooks, API ========= getNoteSender : $url")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            url,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                if (uid > 0) {
                    NagajaLog().d("wooks, API ========= NOTE_COMPANY_SENDER : $response")
                } else {
                    NagajaLog().d("wooks, API ========= NOTE_MEMBER_SENDER : $response")
                }


                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val noteSenderData = NoteSenderData()
                        noteSenderData.setNoteUid(jsonObject.getInt("noteUID"))
                        noteSenderData.setNoteStatus(jsonObject.getInt("noteStatus"))
                        noteSenderData.setMemberNickName( if (!jsonObject.isNull("memberNName")) jsonObject.getString("memberNName") else "" )
                        noteSenderData.setMemberUid(jsonObject.getInt("memberUID"))
                        noteSenderData.setCompanyName( if (!jsonObject.isNull("compName")) jsonObject.getString("compName") else "" )
                        noteSenderData.setCompanyUid(jsonObject.getInt("compUID"))
                        noteSenderData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                        noteSenderData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                        noteSenderData.setEmail( if (!jsonObject.isNull("email")) jsonObject.getString("email") else "" )
                        noteSenderData.setImageOrigin( if (!jsonObject.isNull("imageOrigin")) jsonObject.getString("imageOrigin") else "" )
                        noteSenderData.setImageName( if (!jsonObject.isNull("imageName")) jsonObject.getString("imageName") else "" )
//                        noteSenderData.setNoteType(jsonObject.getInt("noteTypeUID"))

                        listener.onSuccess(noteSenderData)

                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Member Profile
     * GET
     * @param secureKey
     */
    fun getMemberProfile(requestQueue: RequestQueue, listener: RequestListener<MemberProfileData>,
                         secureKey: String) {

        NagajaLog().d("wooks, API ========= getMemberProfile : secureKey = $secureKey")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            API_DOMAIN + MEMBER_PROFILE,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= MEMBER_PROFILE : $response")


                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val memberProfileData = MemberProfileData()
                        memberProfileData.setMemberUid(jsonObject.getInt("memberUID"))
                        memberProfileData.setMemberStatus(jsonObject.getInt("memStatus"))
                        memberProfileData.setMemberId( if (!jsonObject.isNull("memberID")) jsonObject.getString("memberID") else "" )
                        memberProfileData.setMemberEmail( if (!jsonObject.isNull("memEmailID")) jsonObject.getString("memEmailID") else "" )
                        memberProfileData.setMemberName( if (!jsonObject.isNull("memName")) jsonObject.getString("memName") else "" )
                        memberProfileData.setMemberNickName( if (!jsonObject.isNull("memNName")) jsonObject.getString("memNName") else "" )
                        memberProfileData.setNationUid(jsonObject.getInt("nationUID"))
                        memberProfileData.setCompanyCount(jsonObject.getInt("compCount"))
                        memberProfileData.setNationPhone( if (!jsonObject.isNull("memNationPhone")) jsonObject.getString("memNationPhone") else "" )
                        memberProfileData.setPhoneNumber( if (!jsonObject.isNull("memPhone")) jsonObject.getString("memPhone") else "" )
                        memberProfileData.setLocationUid(jsonObject.getInt("memLocationUID"))
                        memberProfileData.setLatitude( if (!jsonObject.isNull("memLatitude")) jsonObject.getString("memLatitude") else "" )
                        memberProfileData.setLongitude( if (!jsonObject.isNull("memLongitude")) jsonObject.getString("memLongitude") else "" )
                        memberProfileData.setAddressZipCode( if (!jsonObject.isNull("memAddressZipCode")) jsonObject.getString("memAddressZipCode") else "" )
                        memberProfileData.setAddress( if (!jsonObject.isNull("memAddress")) jsonObject.getString("memAddress") else "" )
                        memberProfileData.setAddressDetail( if (!jsonObject.isNull("memAddressDetail")) jsonObject.getString("memAddressDetail") else "" )
                        memberProfileData.setImageOrigin( if (!jsonObject.isNull("memberImageOrigin")) jsonObject.getString("memberImageOrigin") else "" )
                        memberProfileData.setImageName( if (!jsonObject.isNull("memberImageName")) jsonObject.getString("memberImageName") else "" )
                        memberProfileData.setIsPush(jsonObject.getBoolean("memberPushYn"))
                        memberProfileData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )
                        memberProfileData.setUpdateDate( if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "" )
                        memberProfileData.setLoginDate( if (!jsonObject.isNull("loginDate")) jsonObject.getString("loginDate") else "" )

                        listener.onSuccess(memberProfileData)

                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Change Push Status
     * POST
     * @param secureKey
     * @param status
     */
    fun getChangePushStatus(requestQueue: RequestQueue, listener: RequestListener<String>,
                            secureKey: String, status: Int) {

        NagajaLog().d("wooks, API ========= getChangePushStatus : secureKey = $secureKey / status = $status")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + CHANGE_PUSH_STATUS,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= CHANGE_PUSH_STATUS : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_WRONG_FORMAT_PASSWORD -> {
                                errorCode = ERROR_CODE_WRONG_FORMAT_PASSWORD
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["pushyn"] = status.toString()
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Change Push Status
     * POST
     * @param secureKey
     * @param mainAreaUid
     * @param subAreaUid
     */
    fun getMyLocationSetting(requestQueue: RequestQueue, listener: RequestListener<String>,
                             secureKey: String, mainAreaUid: Int, subAreaUid: Int) {

        NagajaLog().d("wooks, API ========= getMyLocationSetting : secureKey = $secureKey / mainAreaUid = $mainAreaUid / subAreaUid = $subAreaUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + MY_LOCATION_SETTING,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= MY_LOCATION_SETTING : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_WRONG_FORMAT_PASSWORD -> {
                                errorCode = ERROR_CODE_WRONG_FORMAT_PASSWORD
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["locationsidouid"] = mainAreaUid.toString()
                params["locationgugunuid"] = subAreaUid.toString()
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Used Market Pull Up Count
     * GET
     * @param secureKey
     * @param itemUid
     * @param companyUid
     */
    fun getUsedMarketPullUpCount(requestQueue: RequestQueue, listener: RequestListener<PullUpCountData>,
                                 secureKey: String, itemUid: Int, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getUsedMarketPullUpCount : secureKey = $secureKey / itemUid = $itemUid / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            if (companyUid <= 0) "$API_DOMAIN$USED_MARKET_MEMBER_PULL_UP_COUNT/$itemUid" else "$API_DOMAIN$USED_MARKET_COMPANY_PULL_UP_COUNT/$companyUid/$itemUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                if (companyUid <= 0) {
                    NagajaLog().d("wooks, API ========= USED_MARKET_MEMBER_PULL_UP_COUNT : $response")
                } else {
                    NagajaLog().d("wooks, API ========= USED_MARKET_COMPANY_PULL_UP_COUNT : $response")
                }


                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val pullUpCountData = PullUpCountData()
                        pullUpCountData.setItemUid(jsonObject.getInt("itemUID"))
                        pullUpCountData.setUpperCount(jsonObject.getInt("upperCount"))
                        pullUpCountData.setTotalPoint(jsonObject.getInt("totalPoint"))
                        pullUpCountData.setPointAmount(jsonObject.getInt("pointAmount"))
                        pullUpCountData.setPointBonus(jsonObject.getInt("pointBonus"))

                        listener.onSuccess(pullUpCountData)

                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Used Market Pull Up
     * GET
     * @param secureKey
     * @param itemUid
     * @param companyUid
     */
    fun getUsedMarketPullUp(requestQueue: RequestQueue, listener: RequestListener<String>,
                            secureKey: String, itemUid: Int, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getUsedMarketPullUp : secureKey = $secureKey / itemUid = $itemUid / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.PUT,
            if (companyUid <= 0) "$API_DOMAIN$USED_MARKET_MEMBER_PULL_UP/$itemUid" else "$API_DOMAIN$USED_MARKET_COMPANY_PULL_UP/$companyUid/$itemUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                if (companyUid <= 0) {
                    NagajaLog().d("wooks, API ========= USED_MARKET_MEMBER_PULL_UP : $response")
                } else {
                    NagajaLog().d("wooks, API ========= USED_MARKET_COMPANY_PULL_UP : $response")
                }


                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_WRONG_FORMAT_PASSWORD -> {
                                errorCode = ERROR_CODE_WRONG_FORMAT_PASSWORD
                            }

                            ERROR_CODE_NOT_ENOUGH_POINT -> {
                                errorCode = ERROR_CODE_NOT_ENOUGH_POINT
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Point
     * GET
     * @param secureKey
     * @param companyUid
     */
    fun getPoint(requestQueue: RequestQueue, listener: RequestListener<PointData>,
                 secureKey: String, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getPoint : secureKey = $secureKey / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            if (companyUid <= 0) "$API_DOMAIN$MEMBER_POINT" else "$API_DOMAIN$COMPANY_POINT/$companyUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                if (companyUid <= 0) {
                    NagajaLog().d("wooks, API ========= MEMBER_POINT : $response")
                } else {
                    NagajaLog().d("wooks, API ========= COMPANY_POINT : $response")
                }


                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val pointData = PointData()
                        pointData.setPointUid(jsonObject.getInt("pointUID"))
                        pointData.setMemberUid(jsonObject.getInt("memberUID"))
                        pointData.setCompanyUid(jsonObject.getInt("compUID"))
                        pointData.setPointAmount(jsonObject.getInt("pointAmount"))
                        pointData.setPointBonus(jsonObject.getInt("pointBonus"))
                        pointData.setTotalPoint(jsonObject.getInt("totalPoint"))
                        pointData.setCreateDate( if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "" )

                        listener.onSuccess(pointData)

                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Point History
     * GET
     * @param secureKey
     * @param companyUid
     */
    fun getPointHistory(requestQueue: RequestQueue, listener: RequestListener<ArrayList<PointHistoryData>>,
                        secureKey: String, companyUid: Int) {

        NagajaLog().d("wooks, API ========= getPoint : secureKey = $secureKey / companyUid = $companyUid")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.GET,
            if (companyUid <= 0) "$API_DOMAIN$MEMBER_POINT_HISTORY" else "$API_DOMAIN$COMPANY_POINT_HISTORY/$companyUid",
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                if (companyUid <= 0) {
                    NagajaLog().d("wooks, API ========= MEMBER_POINT_HISTORY : $response")
                } else {
                    NagajaLog().d("wooks, API ========= COMPANY_POINT_HISTORY : $response")
                }


                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        val jsonArray = json.getJSONArray("data")
                        val pointHistoryListData = ArrayList<PointHistoryData>()

                        for (i in 0 until jsonArray.length()) {
                            val pointHistoryData = PointHistoryData()
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (null != jsonObject) {
                                pointHistoryData.setPointLogUid(jsonObject.getInt("pointLogUID"))
                                pointHistoryData.setPointUid(jsonObject.getInt("pointUID"))
                                pointHistoryData.setPointAmount(jsonObject.getInt("pointAmount"))
                                pointHistoryData.setPointBonus(jsonObject.getInt("pointBonus"))
                                pointHistoryData.setPointStatus(jsonObject.getInt("pointStatus"))
                                pointHistoryData.setPointType(jsonObject.getInt("pointType"))
                                pointHistoryData.setCreateDate(if (!jsonObject.isNull("createDate")) jsonObject.getString("createDate") else "")
                                pointHistoryData.setUpdateDate(if (!jsonObject.isNull("updateDate")) jsonObject.getString("updateDate") else "")
                                pointHistoryData.setPointDesc(if (!jsonObject.isNull("pointDesc")) jsonObject.getString("pointDesc") else "")
                            }

                            pointHistoryListData.add(pointHistoryData)
                        }

                        listener.onSuccess(pointHistoryListData)

                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Change Push Status
     * POST
     * @param secureKey
     * @param nationPhoneCode
     * @param phoneNumber
     * @param authCode
     */
    fun getMemberWithdrawal(requestQueue: RequestQueue, listener: RequestListener<String>,
                            secureKey: String, nationPhoneCode: String, phoneNumber: String, authCode: String) {

        NagajaLog().d("wooks, API ========= getMemberWithdrawal : secureKey = $secureKey / nationPhoneCode = $nationPhoneCode / phoneNumber = $phoneNumber / authCode = $authCode")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + MEMBER_WITHDRAWAL,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= MEMBER_WITHDRAWAL : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_WRONG_FORMAT_PASSWORD -> {
                                errorCode = ERROR_CODE_WRONG_FORMAT_PASSWORD
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["nationphone"] = nationPhoneCode
                params["phone"] = phoneNumber
                params["code"] = authCode
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Get Member Withdrawal Send Email
     * POST
     * @param secureKey
     * @param email
     */
    fun getMemberWithdrawalSendEmail(requestQueue: RequestQueue, listener: RequestListener<String>,
                                     secureKey: String, email: String) {

        NagajaLog().d("wooks, API ========= getMemberWithdrawalSendEmail : secureKey = $secureKey / email = $email")

        val jsonObjectRequest: StringRequest = object : StringRequest(
            Method.POST,
            API_DOMAIN + SIGN_UP_SEND_EMAIL,
            Response.Listener { response ->
                if (null == response) {
                    return@Listener
                }

                NagajaLog().d("wooks, API ========= SIGN_UP_SEND_EMAIL : $response")

                try {
                    val json = JSONObject(response)
                    val status = json.getInt("status")
                    if (status == 0) {
                        listener.onSuccess("success")
                    } else {
                        var errorCode = status
                        when (status) {
                            ERROR_EXPIRED_TOKEN,
                            ERROR_INVALID_TOKEN,
                            ERROR_DO_NOT_EXIST_TOKEN-> {
                                errorCode = ERROR_EXPIRED_TOKEN
                            }

                            ERROR_CODE_WRONG_FORMAT_PASSWORD -> {
                                errorCode = ERROR_CODE_WRONG_FORMAT_PASSWORD
                            }
                        }
                        listener.onFail(errorCode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFail(999)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFail(999)
                }
            },
            Response.ErrorListener { error ->
                if (null != listener) {
                    var message: String? = null
                    val errorCode = 0
                    if (error is NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ServerError) {
                        message =
                            "The server could not be found. Please try again after some time!!"
                    } else if (error is AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is ParseError) {
                        message = "Parsing error! Please try again after some time!!"
                    } else if (error is NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!"
                    } else if (error is TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection."
                    }
                    listener.onFail(message)
                }
            }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: java.lang.Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $secureKey"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["memberid"] = email
                params["confirmtypeuid"] = "2"
                params["class"] = "withraw"
                return params
            }

        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    @Throws(IOException::class)
    open fun getBytes(inputStream: InputStream): ByteArray? {
        return try {
            val byteBuffer = ByteArrayOutputStream()
            val bufferSize = 1024
            val buffer = ByteArray(bufferSize)
            var len = 0
            while (inputStream.read(buffer).also { len = it } != -1) {
                byteBuffer.write(buffer, 0, len)
            }
            byteBuffer.toByteArray()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }
}


