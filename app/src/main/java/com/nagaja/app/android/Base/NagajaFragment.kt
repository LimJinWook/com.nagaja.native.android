package com.nagaja.app.android.Base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.nagaja.app.android.BuildConfig
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogSelectLanguage
import com.nagaja.app.android.Main.MainActivity
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.SharedPreferencesUtil
import com.nagaja.app.android.R
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.NagajaManager

import java.util.Locale
import kotlin.system.exitProcess

abstract class NagajaFragment : Fragment() {

    companion object {

        // Select Language
        const val SELECT_LANGUAGE_EN : String                   = "EN"
        const val SELECT_LANGUAGE_KO : String                   = "KO"
        const val SELECT_LANGUAGE_FIL : String                  = "FIL"
        const val SELECT_LANGUAGE_ZH : String                   = "ZH"
        const val SELECT_LANGUAGE_JA : String                   = "JA"

        // Phone Auth Type
        const val PHONE_AUTH_TYPE_SIGN_UP : Int                 = 0x01
        const val PHONE_AUTH_TYPE_FIND_ID : Int                 = 0x02
        const val PHONE_AUTH_TYPE_FIND_PASSWORD : Int           = 0x03
        const val PHONE_AUTH_TYPE_CHANGE_PHONE_NUMBER : Int     = 0x04
        const val PHONE_AUTH_TYPE_MEMBER_WITHDRAWAL : Int       = 0x05

        // Custom Dialog Type
        const val DIALOG_TYPE_NORMAL : Int                      = 0x01
        const val DIALOG_TYPE_NO_CANCEL : Int                   = 0x02
        const val DIALOG_TYPE_SERVER_CHECK : Int                = 0x03
        const val DIALOG_TYPE_SELECT_LANGUAGE : Int             = 0x04

        // Login Type
        const val LOGIN_TYPE_KAKAO                              = "login_type_kakao"
        const val LOGIN_TYPE_NAVER                              = "login_type_naver"
        const val LOGIN_TYPE_GOOGLE                             = "login_type_google"
        const val LOGIN_TYPE_FACEBOOK                           = "login_type_facebook"
        const val LOGIN_TYPE_EMAIL                              = "login_type_email"
        const val LOGIN_TYPE_APPLE                              = "login_type_apple"

        // FCM Push Type
        const val PUSH_TYPE_CHAT                                = "chat"
        const val PUSH_TYPE_RESERVATION                         = "reservate"
        const val PUSH_TYPE_JOB_AND_JON_SEARCH                  = "job"
        const val PUSH_TYPE_GO_TO_PLACE                         = "place"
        const val PUSH_TYPE_POINT                               = "point"
        const val PUSH_TYPE_NOTICE                              = "notice"
        const val PUSH_TYPE_EVENT                               = "event"
        const val PUSH_TYPE_SERVER_CHECK                        = "server_check"
        const val PUSH_TYPE_REPORT_MISSING                      = "report_missing"
        const val PUSH_TYPE_NOTE                                = "note"

        // FCM Push Topic Type
        const val PUSH_TOPIC_TYPE_EVENT                         = "topic_event"
        const val PUSH_TOPIC_TYPE_NOTICE                        = "topic_notice"
        const val PUSH_TOPIC_TYPE_SERVER_CHECK                  = "topic_server_check"

        // Reservation Status Tab Type
        const val RESERVATION_TYPE_ALL                          = 0x101
        const val RESERVATION_TYPE_RESERVATION_STATUS           = 0x01
        const val RESERVATION_TYPE_CANCEL                       = 0x02
        const val RESERVATION_TYPE_DATE_SEARCH                  = 0x03


        // Company Type
        const val COMPANY_TYPE_USED_MARKET                      = 1       // 중고장터
        const val COMPANY_TYPE_USED_MART                        = 244     // 마트
        const val COMPANY_TYPE_HOSPITAL                         = 22      // 병원
        const val COMPANY_TYPE_FACILITIES                       = 23      // 편의시설
        const val COMPANY_TYPE_RENTAL_CAR_CHILDMINDER_GUIDE     = 24      // 렌트카 / 보모 / 가이드
        const val COMPANY_TYPE_TRAVEL_AGENCY                    = 25      // 여행사 패키지
        const val COMPANY_TYPE_RESTAURANT                       = 26      // 음식점
        const val COMPANY_TYPE_LODGING                          = 27      // 숙소(풀빌라)
        const val COMPANY_TYPE_LEISURE_ATTRACTIONS              = 28      // 레저 / 관광명소
        const val COMPANY_TYPE_MASSAGE_AESTHETICS               = 29      // 마사지 / 에스테틱
        const val COMPANY_TYPE_TALK_ROOM                        = 30      // 수다방
        const val COMPANY_TYPE_FREE_BOARD                       = 33      // 자유게시판
        const val COMPANY_TYPE_JOB_AND_JOB_SEARCH               = 35      // 구인구직
        const val COMPANY_TYPE_NEWS                             = 39      // 현지뉴스
        const val COMPANY_TYPE_PLAYGROUND                       = 31      // 놀이터
        const val COMPANY_TYPE_STORE_RECOMMEND                  = 34      // 업체추천
        const val COMPANY_TYPE_REPORT_DISAPPEARANCE             = 36      // 신고 / 실종
        const val COMPANY_TYPE_NOTICE                           = 37      // 공지사항
        const val COMPANY_TYPE_FAQ                              = 38      // FAQ
        const val COMPANY_TYPE_MAIN                             = 99999   // MAIN
        const val COMPANY_TYPE_MAIN_BOARD                       = 100000  // Main Board




        // Report Type
        const val REPORT_TYPE_USER                              = 11         // 유저 신고
        const val REPORT_TYPE_USER_RESERVATION                  = 17         // 회원 예약
        const val REPORT_TYPE_JOB_SEARCH                        = 21         // 구직 신고
        const val REPORT_TYPE_JOB_OFFER                         = 23         // 구인글 신고
        const val REPORT_TYPE_NOTE                              = 31         // 쪽지 신고
        const val REPORT_TYPE_BOARD                             = 41         // 자유게시판 신고
        const val REPORT_TYPE_REVIEW                            = 43         // 리뷰 신고
        const val REPORT_TYPE_COMMENT                           = 45         // 댓글 신고
        const val REPORT_TYPE_STORE                             = 51         // 기업 신고
        const val REPORT_TYPE_STORE_RECOMMEND                   = 53         // 업체추천 신고
        const val REPORT_TYPE_COMPANY_RESERVATION               = 57         // 기업 예약
        const val REPORT_TYPE_USED_MARKET                       = 71         // 중고마켓 신고
        const val REPORT_TYPE_MART                              = 81         // 마트



        // Used Market Status
        const val USED_MARKET_STATUS_REGISTER                   = 1         // 등록
        const val USED_MARKET_STATUS_CANCEL                     = 2         // 등록 취소
        const val USED_MARKET_STATUS_PROGRESS                   = 5         // 판매준비중
        const val USED_MARKET_STATUS_COMPLETE                   = 7         // 판매중
        const val USED_MARKET_STATUS_RETURN                     = 9         // 매진



        // Currency Type
        const val CURRENCY_TYPE_USD                             = "USD"
        const val CURRENCY_TYPE_KRW                             = "KRW"
        const val CURRENCY_TYPE_PHP                             = "PHP"
        const val CURRENCY_TYPE_CNY                             = "CNY"
        const val CURRENCY_TYPE_JPY                             = "JPY"


        // Time Zone ID List
        const val PHILIPPINES_MANILA                            = "Asia/Manila"
        const val KOREA_SEOUL                                   = "Asia/Seoul"
        const val AMERICA_LA                                    = "America/Los_Angeles"
        const val AMERICA_NEW_YORK                              = "America/New_York"

        // Nation Uid Code
        const val AMERICA_UID_CODE                              = 1
        const val KOREA_UID_CODE                                = 2
        const val PHILIPPINES_UID_CODE                          = 3

        // Company Grant
        //1:마스터 2:매니져 3:프론트 4:일반
        const val COMPANY_GRANT_MASTER                          = 1
        const val COMPANY_GRANT_MANAGER                         = 2
        const val COMPANY_GRANT_FRONT                           = 3
        const val COMPANY_GRANT_NORMAL                          = 4


        // Reservation Status
        const val TYPE_RESERVATION_APPLICATION                         = 0          // 예약 신청
        const val TYPE_RESERVATION_CONFIRMATION                        = 1          // 예약 확인
        const val TYPE_RESERVATION_COMPLETE                            = 5          // 예약 완료
        const val TYPE_RESERVATION_USE_COMPLETE                        = 7          // 이용 완료
        const val TYPE_RESERVATION_CANCEL                              = 9          // 예약 취소


        // Currency UID
        const val CURRENCY_UID_CNY                              = 156
        const val CURRENCY_UID_JPY                              = 392
        const val CURRENCY_UID_EUR                              = 978
        const val CURRENCY_UID_PHP                              = 608
        const val CURRENCY_UID_KRW                              = 410
        const val CURRENCY_UID_USD                              = 840

        // Current Location
        const val CURRENT_LOCATION_EN                           = "10.293003,123.901703"        // 세부
        const val CURRENT_LOCATION_KO                           = "37.566535,126.977969"        // 서울시청
        const val CURRENT_LOCATION_FIL                          = "10.293003,123.901703"       // 세부
        const val CURRENT_LOCATION_ZH                           = "39.909122,116.397531"        // 베이징
        const val CURRENT_LOCATION_JA                           = "35.690287,139.689740"        // 도쿄


        // Chat Class
        const val CHAT_CLASS_USED_MARKET                        = "1"        // 중고마켓
        const val CHAT_CLASS_COMPANY                            = "3"        // 가맹점
        const val CHAT_CLASS_JOB                                = "5"        // 구인구직
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(context: Context, message: Int) {
        Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show()
    }

    fun disConnectUserToken(context: Context) {
        val customDialog = CustomDialog(
            context,
            DIALOG_TYPE_NO_CANCEL,
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

                activity!!.finishAffinity()
                exitProcess(0)
            }
        })
        customDialog.show()
    }

    open fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun selectLanguage(activity: Activity, selectLanguage: String) {

        var locale: Locale? = null
        var language = ""

        if (selectLanguage.equals(SELECT_LANGUAGE_EN, ignoreCase = true)) {
            language = SELECT_LANGUAGE_EN
            locale = Locale.ENGLISH
            SharedPreferencesUtil().setNationPhoneCode(activity, "1")
            MAPP.SELECT_LANGUAGE_CODE = "1"
        } else if (selectLanguage.equals(SELECT_LANGUAGE_KO, ignoreCase = true)) {
            language = SELECT_LANGUAGE_KO
            locale = Locale.KOREA
            SharedPreferencesUtil().setNationPhoneCode(activity, "82")
            MAPP.SELECT_LANGUAGE_CODE = "82"
        } else if (selectLanguage.equals(SELECT_LANGUAGE_FIL, ignoreCase = true)) {
            language = SELECT_LANGUAGE_FIL
            locale = Locale("fil", "PH")
            SharedPreferencesUtil().setNationPhoneCode(activity, "63")
            MAPP.SELECT_LANGUAGE_CODE = "63"
        } else if (selectLanguage.equals(SELECT_LANGUAGE_ZH, ignoreCase = true)) {
            language = SELECT_LANGUAGE_ZH
            locale = Locale.CHINA
            SharedPreferencesUtil().setNationPhoneCode(activity, "86")
            MAPP.SELECT_LANGUAGE_CODE = "86"
        } else if (selectLanguage.equals(SELECT_LANGUAGE_JA, ignoreCase = true)) {
            language = SELECT_LANGUAGE_JA
            locale = Locale.JAPAN
            SharedPreferencesUtil().setNationPhoneCode(activity, "81")
            MAPP.SELECT_LANGUAGE_CODE = "81"
        } else {
            language = SELECT_LANGUAGE_EN
            locale = Locale.ENGLISH
            SharedPreferencesUtil().setNationPhoneCode(activity, "1")
            MAPP.SELECT_LANGUAGE_CODE = "1"
        }


        val cfg = Configuration()
        cfg.locale = Locale(language)
        cfg.setLocale(locale)
        activity.resources.updateConfiguration(cfg, null)

//        activity.finish()
        ActivityCompat.finishAffinity(activity)

        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)

//        activity.resources.updateConfiguration(cfg, null)
        activity.overridePendingTransition(0, 0)

//        val intent = activity.packageManager.getLaunchIntentForPackage(activity.packageName)
//        if (intent != null) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            activity.startActivity(intent)
//            Runtime.getRuntime().exit(0)
//        }
    }

    fun getLanguageIcon(context: Context): Int {
        val icon = if (SharedPreferencesUtil().getSelectLanguage(context) == SELECT_LANGUAGE_EN) {
            R.drawable.flag_english_2
        } else if (SharedPreferencesUtil().getSelectLanguage(context) == SELECT_LANGUAGE_KO) {
            R.drawable.flag_korean_2
        } else if (SharedPreferencesUtil().getSelectLanguage(context) == SELECT_LANGUAGE_FIL) {
            R.drawable.flag_filipino_2
        } else if (SharedPreferencesUtil().getSelectLanguage(context) == SELECT_LANGUAGE_ZH) {
            R.drawable.flag_chinese_2
        } else if (SharedPreferencesUtil().getSelectLanguage(context) == SELECT_LANGUAGE_JA) {
            R.drawable.flag_japanese_2
        } else {
            R.drawable.flag_english_2
        }

        return icon
    }

    fun showSelectLanguageCustomDialog(context: Activity) {
        val customDialogSelectLanguage = CustomDialogSelectLanguage(context)

        customDialogSelectLanguage.setOnCustomDialogListener(object : CustomDialogSelectLanguage.OnCustomDialogSelectLanguage {
            override fun onSelectLanguage(selectLanguage: String) {
                if (TextUtils.isEmpty(MAPP.USER_DATA.getSecureKey()) || MAPP.IS_NON_MEMBER_SERVICE) {
                    SharedPreferencesUtil().setSelectLanguage(context, selectLanguage)
                    selectLanguage(context, selectLanguage)
                } else {
                    getLanguageChange(context, selectLanguage)
                }

                customDialogSelectLanguage.dismiss()
            }
        })
        customDialogSelectLanguage.show()
    }

    fun setShareUrl(context: Context, categoryUid: Int, boardUid: Int) {
        NagajaLog().e("wooks, setShareUrl : categoryUid = $categoryUid, boardUi = $boardUid")
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
                    .setTitle(context.resources.getString(R.string.app_name))
                    .setDescription(context.resources.getString(R.string.text_share_desc))
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
    private fun getLanguageChange(activity: Activity, languageCode: String) {

        val requestQueue = Volley.newRequestQueue(context)

        var nationUid = ""
        if (languageCode.equals(SELECT_LANGUAGE_EN, ignoreCase = true)) {
            nationUid = "1"
        } else if (languageCode.equals(SELECT_LANGUAGE_KO, ignoreCase = true)) {
            nationUid = "82"
        } else if (languageCode.equals(SELECT_LANGUAGE_FIL, ignoreCase = true)) {
            nationUid = "63"
        } else if (languageCode.equals(SELECT_LANGUAGE_ZH, ignoreCase = true)) {
            nationUid = "86"
        } else if (languageCode.equals(SELECT_LANGUAGE_JA, ignoreCase = true)) {
            nationUid = "81"
        } else {
            nationUid = "1"
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getLanguageChange(
            requestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    SharedPreferencesUtil().setSelectLanguage(activity, languageCode)
                    selectLanguage(activity, languageCode)
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