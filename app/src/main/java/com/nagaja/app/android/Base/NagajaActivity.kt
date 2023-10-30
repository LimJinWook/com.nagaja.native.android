package com.nagaja.app.android.Base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.nagaja.app.android.BuildConfig
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogSelectLanguage
import com.nagaja.app.android.Login.LoginActivity
import com.nagaja.app.android.Main.MainActivity
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.SharedPreferencesUtil
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.NagajaManager

import java.util.*
import kotlin.system.exitProcess


abstract class NagajaActivity : AppCompatActivity() {

    companion object {

        const val INTENT_DATA_PHONE_AUTH_FIND_ID                        = "intent_data_phone_auth_find_id"
        const val INTENT_DATA_PHONE_AUTH_FIND_PASSWORD                  = "intent_data_phone_auth_find_password"
        const val INTENT_DATA_IS_SERVER_CHECK                           = "intent_data_is_server_check"
        const val INTENT_DATA_SERVER_CHECK_START_TIME                   = "intent_data_server_check_start_time"
        const val INTENT_DATA_SERVER_CHECK_END_TIME                     = "intent_data_server_check_end_time"
        const val INTENT_DATA_IS_PUSH_DATA                              = "intent_data_is_push_data"
        const val INTENT_DATA_PUSH_TYPE_DATA                            = "intent_data_push_type_data"
        const val INTENT_DATA_PUSH_RESERVATE_TYPE_DATA                  = "intent_data_push_reservate_type_data"
        const val INTENT_DATA_PUSH_RESERVATE_COMPANY_UID_DATA           = "intent_data_push_reservate_company_uid_data"
        const val INTENT_DATA_PUSH_WEB_LINK_DATA                        = "intent_data_push_web_link_data"
        const val INTENT_DATA_PUSH_TOPIC_CATEGORY_DATA                  = "intent_data_push_topic_category_data"
        const val INTENT_DATA_PUSH_TOPIC_BOARD_DATA                     = "intent_data_push_topic_board_data"
        const val INTENT_DATA_PUSH_TOPIC_COMPANY_UID_DATA               = "intent_data_push_topic_company_uid_data"
        const val INTENT_DATA_IS_KOREA_SELECT_DATA                      = "intent_data_is_korea_select_data"
        const val INTENT_DATA_IS_APPLICATION_COMPANY_SELECT_DATA        = "intent_data_is_application_company_select_data"
        const val INTENT_DATA_ADDRESS_DATA                              = "intent_data_address_data"
        const val INTENT_DATA_IS_SNS_LOGIN_DATA                         = "intent_data_is_sns_login_data"
        const val INTENT_DATA_USED_MARKET_DATA                          = "intent_data_used_market_data"
        const val INTENT_DATA_USED_MARKET_POSITION_DATA                 = "intent_data_used_market_position_data"
        const val INTENT_DATA_IMAGE_PATH_LIST_DATA                      = "intent_data_image_path_list_data"
        const val INTENT_DATA_IMAGE_PATH_POSITION                       = "intent_data_image_path_position"
        const val INTENT_DATA_BOARD_DATA                                = "intent_data_board_data"
        const val INTENT_DATA_PHONE_AUTH_TYPE_DATA                      = "intent_data_phone_auth_type_data"
        const val INTENT_DATA_PHONE_NUMBER_CHANGE_DATA                  = "intent_data_phone_number_change_data"
        const val INTENT_DATA_PHONE_AUTH_DATA                           = "intent_data_phone_auth_data"
        const val INTENT_DATA_LOCATION                                  = "intent_data_location"
        const val INTENT_DATA_MISSING_WRITE_TYPE_DATA                   = "intent_data_missing_write_type_data"
        const val INTENT_DATA_LOCATION_SELECT_DATA                      = "intent_data_location_select_data"
        const val INTENT_DATA_SELECT_LATITUDE_DATA                      = "intent_data_select_latitude_data"
        const val INTENT_DATA_SELECT_LONGITUDE_DATA                     = "intent_data_select_longitude_data"
        const val INTENT_DATA_MY_COMPANY_DATA                           = "intent_data_my_company_data"
        const val INTENT_DATA_COMPANY_DEFAULT_DATA                      = "intent_data_company_default_data"
        const val INTENT_DATA_SELECT_COMPANY_MANAGEMENT_TYPE_DATA       = "intent_data_select_company_management_type_data"
        const val INTENT_DATA_STORE_LIST_DATA                           = "intent_data_store_list_data"
        const val INTENT_DATA_STORE_UID_DATA                            = "intent_data_store_uid_data"
        const val INTENT_DATA_STORE_POSITION_DATA                       = "intent_data_store_position_data"
        const val INTENT_DATA_IS_STORE_STATUS_DATA                      = "intent_data_is_store_status_data"
        const val INTENT_DATA_STORE_NAME_DATA                           = "intent_data_store_name_data"
        const val INTENT_DATA_WRITE_REVIEW_DATA                         = "intent_data_write_review_data"
        const val INTENT_DATA_WRITE_REVIEW_IS_MODIFY_DATA               = "intent_data_write_review_is_modify_data"
        const val INTENT_DATA_REPORT_TYPE_DATA                          = "intent_data_report_type_data"
        const val INTENT_DATA_REPORT_UID_DATA                           = "intent_data_report_uid_data"
        const val INTENT_DATA_REPORT_STORE_NAME_DATA                    = "intent_data_report_store_name_data"
        const val INTENT_DATA_MY_USED_MARKET_MODIFY_DATA                = "intent_data_my_used_market_modify_data"
        const val INTENT_DATA_IS_MISSING_DATA                           = "intent_data_is_missing_data"
        const val INTENT_DATA_BOARD_UID_DATA                            = "intent_data_board_uid_data"
        const val INTENT_DATA_BOARD_DELETE_SUCCESS                      = "intent_data_board_delete_success"
        const val INTENT_DATA_DETAIL_BOARD_SELECT_TYPE_DATA             = "intent_data_detail_board_select_type_data"
        const val INTENT_DATA_DETAIL_BOARD_SELECT_POSITION_DATA         = "intent_data_detail_board_select_position_data"
        const val INTENT_DATA_BOARD_MODIFY_DATA                         = "intent_data_board_modify_data"
        const val INTENT_DATA_BOARD_LIKE_BOOKMARK_CHANGE_DATA           = "intent_data_board_like_bookmark_change_data"
        const val INTENT_DATA_BOARD_MODIFY_LIKE_DATA                    = "intent_data_board_modify_like_data"
        const val INTENT_DATA_BOARD_MODIFY_BOOKMARK_DATA                = "intent_data_board_modify_bookmark_data"
        const val INTENT_DATA_BOARD_CATEGORY_DATA                       = "intent_data_board_category_data"
        const val INTENT_DATA_BOARD_PLAYGROUND_DATA                     = "intent_data_board_playground_data"
        const val INTENT_DATA_STORE_DETAIL_DATA                         = "intent_data_store_detail_data"
        const val INTENT_DATA_CAMERA_IS_FILE_DATA                       = "intent_data_camera_is_file_data"
        const val INTENT_DATA_IS_MY_JOB_DATA                            = "intent_data_is_my_job_data"
        const val INTENT_DATA_IS_MY_MISSING_DATA                        = "intent_data_is_my_missing_data"
        const val INTENT_DATA_IS_COMPANY_NOTE_DATA                      = "intent_data_is_company_note_data"
        const val INTENT_DATA_COMPANY_UID_DATA                          = "intent_data_company_uid_data"
        const val INTENT_DATA_COMPANY_JOB_UID_DATA                      = "intent_data_company_job_uid_data"
        const val INTENT_DATA_FROM_NON_MEMBER_TO_MEMBER_DATA            = "intent_data_from_non_member_to_member_data"
        const val INTENT_DATA_SELECT_TERMS_TYPE_DATA                    = "intent_data_select_terms_type_data"
        const val INTENT_DATA_SELECT_TERMS_URL_DATA                     = "intent_data_select_terms_url_data"
        const val INTENT_DATA_NOTE_BOARD_UID_DATA                       = "intent_data_note_board_uid_data"
        const val INTENT_DATA_CHAT_CLASS_DATA                           = "intent_data_chat_class_data"
        const val INTENT_DATA_CHAT_DATA                                 = "intent_data_chat_data"





        const val INTENT_PHONE_AUTH_SIGN_UP_REQUEST_CODE                = 0x01
        const val INTENT_PHONE_AUTH_FIND_ID_REQUEST_CODE                = 0x02
        const val INTENT_PHONE_AUTH_FIND_PASSWORD_REQUEST_CODE          = 0x03
        const val INTENT_PHONE_AUTH_CHANGE_PHONE_NUMBER_REQUEST_CODE    = 0x04
        const val INTENT_PHONE_AUTH_MEMBER_WITHDRAWAL_REQUEST_CODE      = 0x05
        const val INTENT_ADDRESS_REQUEST_CODE                           = 0x06
        const val INTENT_MEMBER_WITHDRAWAL_REQUEST_CODE                 = 0x07
        const val INTENT_BOARD_REQUEST_CODE                             = 0x08
        const val INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE               = 0x09
        const val INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE              = 0x10
        const val INTENT_SELECT_MAP_REQUEST_CODE                        = 0x11
        const val INTENT_SHOW_LOCATION_SCREEN_REQUEST_CODE              = 0x12
        const val INTENT_WRITE_REVIEW_SUCCESS_REQUEST_CODE              = 0x13
        const val INTENT_USED_MARKET_REQUEST_CODE                       = 0x14
        const val INTENT_REPORT_REQUEST_CODE                            = 0x15
        const val INTENT_FILE_UPLOAD_REQUEST_CODE                       = 0x16
        const val INTENT_BOARD_WRITE_REQUEST_CODE                       = 0x17
        const val INTENT_DATA_COMPANY_APPLICATION_REQUEST_CODE          = 0x18
        const val INTENT_DATA_FROM_NON_MEMBER_TO_MEMBER_REQUEST_CODE    = 0x19
        const val INTENT_DATA_STORE_LIST_REQUEST_CODE                   = 0x20

        const val INTENT_DATA_CHANGE_LOCATION_REQUEST_CODE              = 0x101
        const val INTENT_DATA_SEARCH_REQUEST_CODE                       = 0x102
        const val INTENT_DATA_NOTIFICATION_REQUEST_CODE                 = 0x103







        const val NATION_CODE_KOREA                                     = 82
        const val NATION_CODE_PHILIPPINES                               = 63
        const val NATION_CODE_AMERICA                                   = 1
        const val NATION_CODE_CHINA                                     = 86
        const val NATION_CODE_JAPAN                                     = 81




        const val FCM_TYPE_SERVER_CHECK                                 = "FCM_TYPE_SERVER_CHECK"
        const val FCM_TYPE_CHAT                                         = "FCM_TYPE_CHAT"
        const val FCM_TYPE_CHAT_JOB                                     = "FCM_TYPE_CHAT_JOB"
        const val FCM_TYPE_RESERVATION_LIST                             = "FCM_TYPE_RESERVATION_LIST"
        const val FCM_TYPE_RESERVATION_DETAIL                           = "FCM_TYPE_RESERVATION_DETAIL"
        const val FCM_TYPE_RESERVATION_CANCEL                           = "FCM_TYPE_RESERVATION_CANCEL"
        const val FCM_TYPE_JOB_AND_JOB_SEARCH                           = "FCM_TYPE_JOB_AND_JOB_SEARCH"
        const val FCM_TYPE_HOUSE_OF_CALL                                = "FCM_TYPE_HOUSE_OF_CALL"
        const val FCM_TYPE_POINT                                        = "FCM_TYPE_POINT"


        // Current Location
        const val CURRENT_LOCATION_EN                                   = "10.31568605,123.8854393"        // 세부
        const val CURRENT_LOCATION_KO                                   = "37.566535,126.977969"        // 서울시청
        const val CURRENT_LOCATION_FIL                                  = "10.31568605,123.8854393"       // 세부
        const val CURRENT_LOCATION_ZH                                   = "39.909122,116.397531"        // 베이징
        const val CURRENT_LOCATION_JA                                   = "35.690287,139.689740"        // 도쿄


        // Select Header Menu Type
        const val SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION               = 0x01
        const val SELECT_HEADER_MENU_TYPE_SEARCH                        = 0x02
        const val SELECT_HEADER_MENU_TYPE_NOTIFICATION                  = 0x03

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(context: Context, message: Int) {
        Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show()
    }

    fun defaultLoginScreen() {
        val intent = Intent(this@NagajaActivity, LoginActivity::class.java)
        intent.putExtra(INTENT_DATA_FROM_NON_MEMBER_TO_MEMBER_DATA, true)
        startActivity(intent)
    }

    fun disConnectUserToken(context: Context) {
        val customDialog = CustomDialog(
            context,
            NagajaFragment.DIALOG_TYPE_NO_CANCEL,
            context.resources.getString(R.string.text_expired_tokens),
            context.resources.getString(R.string.text_message_expired_tokens),
            "",
            context.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
            }

            override fun onConfirm() {
                customDialog.dismiss()

                finishAffinity()
                exitProcess(0)
            }
        })
        customDialog.show()
    }

    fun getLanguageIcon(): Int {

        NagajaLog().e("wooks, SharedPreferencesUtil().getSelectLanguage(this@NagajaActivity) = ${SharedPreferencesUtil().getSelectLanguage(this@NagajaActivity)}")

        val icon = if (SharedPreferencesUtil().getSelectLanguage(this@NagajaActivity) == NagajaFragment.SELECT_LANGUAGE_EN) {
            R.drawable.flag_english_2
        } else if (SharedPreferencesUtil().getSelectLanguage(this@NagajaActivity) == NagajaFragment.SELECT_LANGUAGE_KO) {
            R.drawable.flag_korean_2
        } else if (SharedPreferencesUtil().getSelectLanguage(this@NagajaActivity) == NagajaFragment.SELECT_LANGUAGE_FIL) {
            R.drawable.flag_filipino_2
        } else if (SharedPreferencesUtil().getSelectLanguage(this@NagajaActivity) == NagajaFragment.SELECT_LANGUAGE_ZH) {
            R.drawable.flag_chinese_2
        } else if (SharedPreferencesUtil().getSelectLanguage(this@NagajaActivity) == NagajaFragment.SELECT_LANGUAGE_JA) {
            R.drawable.flag_japanese_2
        } else {
            R.drawable.flag_english_2
        }

        return icon
    }

    fun showSelectLanguageCustomDialog() {
        val customDialogSelectLanguage = CustomDialogSelectLanguage(this@NagajaActivity)

        customDialogSelectLanguage.setOnCustomDialogListener(object : CustomDialogSelectLanguage.OnCustomDialogSelectLanguage {
            override fun onSelectLanguage(selectLanguage: String) {
                if (TextUtils.isEmpty(MAPP.USER_DATA.getSecureKey()) || MAPP.IS_NON_MEMBER_SERVICE) {
                    SharedPreferencesUtil().setSelectLanguage(this@NagajaActivity, selectLanguage)
                    selectLanguage(selectLanguage, true)
                } else {
                    getLanguageChange(selectLanguage, true)
                }

                customDialogSelectLanguage.dismiss()
            }
        })
        customDialogSelectLanguage.show()
    }

    fun selectLanguage(selectLanguage: String, isRefresh: Boolean) {

        var locale: Locale? = null
        var language = ""

        if (selectLanguage.equals(NagajaFragment.SELECT_LANGUAGE_EN, ignoreCase = true)) {
            language = NagajaFragment.SELECT_LANGUAGE_EN
            locale = Locale.ENGLISH
            SharedPreferencesUtil().setNationPhoneCode(this@NagajaActivity, "1")
            MAPP.SELECT_LANGUAGE_CODE = "1"
        } else if (selectLanguage.equals(NagajaFragment.SELECT_LANGUAGE_KO, ignoreCase = true)) {
            language = NagajaFragment.SELECT_LANGUAGE_KO
            locale = Locale.KOREA
            SharedPreferencesUtil().setNationPhoneCode(this@NagajaActivity, "82")
            MAPP.SELECT_LANGUAGE_CODE = "82"
        } else if (selectLanguage.equals(NagajaFragment.SELECT_LANGUAGE_FIL, ignoreCase = true)) {
            language = NagajaFragment.SELECT_LANGUAGE_FIL
            locale = Locale("fil", "PH")
            SharedPreferencesUtil().setNationPhoneCode(this@NagajaActivity, "63")
            MAPP.SELECT_LANGUAGE_CODE = "63"
        } else if (selectLanguage.equals(NagajaFragment.SELECT_LANGUAGE_ZH, ignoreCase = true)) {
            language = NagajaFragment.SELECT_LANGUAGE_ZH
            locale = Locale.CHINA
            SharedPreferencesUtil().setNationPhoneCode(this@NagajaActivity, "86")
            MAPP.SELECT_LANGUAGE_CODE = "86"
        } else if (selectLanguage.equals(NagajaFragment.SELECT_LANGUAGE_JA, ignoreCase = true)) {
            language = NagajaFragment.SELECT_LANGUAGE_JA
            locale = Locale.JAPAN
            SharedPreferencesUtil().setNationPhoneCode(this@NagajaActivity, "81")
            MAPP.SELECT_LANGUAGE_CODE = "81"
        } else {
            language = NagajaFragment.SELECT_LANGUAGE_EN
            locale = Locale.ENGLISH
            SharedPreferencesUtil().setNationPhoneCode(this@NagajaActivity, "1")
            MAPP.SELECT_LANGUAGE_CODE = "1"
        }


        val cfg = Configuration()
        cfg.locale = Locale(language)
        cfg.setLocale(locale)
        this@NagajaActivity.resources.updateConfiguration(cfg, null)

        if (isRefresh) {
            finish()

            val intent = Intent(this@NagajaActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
//            this@NagajaActivity.resources.updateConfiguration(cfg, null)
            this@NagajaActivity.overridePendingTransition(0, 0)
        }



//        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
//        if (intent != null) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            context.startActivity(intent)
//            Runtime.getRuntime().exit(0)
//        }
    }

    fun setShareUrl(categoryUid: Int, boardUid: Int) {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(MainActivity.DYNAMIC_LINK_URL + categoryUid + "/" + boardUid))
            .setDomainUriPrefix(MainActivity.DYNAMIC_PREFIX)
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder(BuildConfig.APPLICATION_ID)
                    .setFallbackUrl(Uri.parse("https://www.nagaja.com"))
                    .setMinimumVersion(1)
                    .build()
            )
            .setIosParameters(
                DynamicLink.IosParameters.Builder(MainActivity.IOS_PACKAGE)
                    .setAppStoreId(MainActivity.IOS_APP_STORE_ID)
                    .setMinimumVersion("1.1.1")
                    .setFallbackUrl(Uri.parse("https://www.nagaja.com"))
                    .build()
            )
            .setSocialMetaTagParameters(
                DynamicLink.SocialMetaTagParameters.Builder()
                    .setTitle(this@NagajaActivity.resources.getString(R.string.app_name))
                    .setDescription(this@NagajaActivity.resources.getString(R.string.text_share_desc))
                    .setImageUrl(Uri.parse(MainActivity.SHARE_LINK_IMAGE_URL))
                    .build())
            .buildShortDynamicLink()
            .addOnSuccessListener {
                val shortLink = it.shortLink!!
                NagajaLog().d("wooks, ShareUrl shortLink = $shortLink")
                createShareSheet(shortLink)

            }
            .addOnFailureListener {
                it.toString()
                NagajaLog().d("wooks, e : ${it.toString()}")
            }
    }

    private fun createShareSheet(dynamicLink: Uri){
        NagajaLog().d("wooks, dynamicLink = $dynamicLink")
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, dynamicLink.toString())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    /**
     * API. Get Reservation Memo Save
     */
    private fun getLanguageChange(languageCode: String, isRefresh: Boolean) {

        val requestQueue = Volley.newRequestQueue(this@NagajaActivity)

        var nationUid = ""
        if (languageCode.equals(NagajaFragment.SELECT_LANGUAGE_EN, ignoreCase = true)) {
            nationUid = "1"
        } else if (languageCode.equals(NagajaFragment.SELECT_LANGUAGE_KO, ignoreCase = true)) {
            nationUid = "82"
        } else if (languageCode.equals(NagajaFragment.SELECT_LANGUAGE_FIL, ignoreCase = true)) {
            nationUid = "63"
        } else if (languageCode.equals(NagajaFragment.SELECT_LANGUAGE_ZH, ignoreCase = true)) {
            nationUid = "86"
        } else if (languageCode.equals(NagajaFragment.SELECT_LANGUAGE_JA, ignoreCase = true)) {
            nationUid = "81"
        } else {
            nationUid = "1"
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getLanguageChange(
            requestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    SharedPreferencesUtil().setSelectLanguage(this@NagajaActivity, languageCode)
                    selectLanguage(languageCode, isRefresh)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            nationUid
        )
    }
}
