package com.nagaja.app.android.Main

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.facebook.AccessToken
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.login.LoginManager
import com.github.angads25.toggle.widget.LabeledSwitch
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Adapter.CompanyMemberAdapter
import com.nagaja.app.android.ApplicationCompanyMember.ApplicationCompanyMemberActivity
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_COMPANY_APPLICATION_REQUEST_CODE
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_COMPANY_UID_DATA
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_IMAGE_PATH_LIST_DATA
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_IMAGE_PATH_POSITION
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_IS_MY_JOB_DATA
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_IS_MY_MISSING_DATA
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_MY_COMPANY_DATA
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_STORE_LIST_DATA
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_MEMBER_WITHDRAWAL_REQUEST_CODE
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.CompanyInformation.CompanyInformationActivity
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.FullScreenImage.FullScreenImageActivity
import com.nagaja.app.android.Job.JobActivity
import com.nagaja.app.android.KeyWord.KeyWordActivity
import com.nagaja.app.android.Login.LoginActivity
import com.nagaja.app.android.Missing.MissingActivity
import com.nagaja.app.android.MyUsedMarket.MyUsedMarketActivity
import com.nagaja.app.android.Note.NoteActivity
import com.nagaja.app.android.PointHistory.PointHistoryActivity
import com.nagaja.app.android.Regular.RegularActivity
import com.nagaja.app.android.Reservation.ReservationActivity
import com.nagaja.app.android.SettingProfile.SettingProfileActivity
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.nhn.android.naverlogin.OAuthLogin
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_my_page.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class MyPageFragment : NagajaFragment() {

    private lateinit var mContainer: View
    private lateinit var mIdView: View
    private lateinit var mApplicationCompanyMemberView: View

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mAddressTextView: TextView
    private lateinit var mCompanyMemberTextView: TextView
    private lateinit var mPhoneNumberTextView: TextView
    private lateinit var mGPTextView: TextView

    private lateinit var mProfileImageView: SimpleDraweeView

    private lateinit var mSelectLanguageSpinner: PowerSpinnerView

    private lateinit var mNotificationSwitch: LabeledSwitch

    private lateinit var mCompanyRecyclerView: RecyclerView

    private lateinit var mCompanyMemberAdapter: CompanyMemberAdapter

    private lateinit var mRequestQueue: RequestQueue

    // Google Login
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private var mIsCompanyMember = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCompanyMemberAdapter = CompanyMemberAdapter(requireActivity())

        mRequestQueue = Volley.newRequestQueue(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContainer = inflater.inflate(R.layout.fragment_my_page, container, false)

        init()

        return mContainer
    }

    @SuppressLint("SetTextI18n")
    private fun init() {

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = requireActivity().resources.getString(R.string.text_my_page)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Scroll View
        mScrollView = mContainer.fragment_my_page_scroll_view

        // Profile Image View
        mProfileImageView = mContainer.fragment_my_page_profile_image_view
        mProfileImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + MAPP.USER_DATA.getProfileName()))
        mProfileImageView.setOnClickListener {
            if (!TextUtils.isEmpty(MAPP.USER_DATA.getProfileName())) {
                val intent = Intent(requireActivity(), FullScreenImageActivity::class.java)

                val imageList = ArrayList<String>()
                imageList.add(DEFAULT_IMAGE_DOMAIN + MAPP.USER_DATA.getProfileName())

                intent.putExtra(INTENT_DATA_IMAGE_PATH_LIST_DATA, imageList)
                intent.putExtra(INTENT_DATA_IMAGE_PATH_POSITION, 0)

                startActivity(intent)
            }
        }

        // ID View
        mIdView = mContainer.fragment_my_page_id_view
//        mIdView.setOnClickListener {
//            mIsCompanyMember = !mIsCompanyMember
//
//            if (mIsCompanyMember) {
//                mCompanyMemberTextView.visibility = View.VISIBLE
//                mCompanySettingExpandableLayout.visibility = View.VISIBLE
//                mApplicationCompanyMemberView.visibility = View.GONE
//            } else {
//                mCompanyMemberTextView.visibility = View.GONE
//                mCompanySettingExpandableLayout.visibility = View.GONE
//                mApplicationCompanyMemberView.visibility = View.VISIBLE
//            }
//        }

        // Email Text View
        val emailTextView = mContainer.fragment_my_page_email_text_view
        if (!TextUtils.isEmpty(MAPP.USER_DATA.getMemberId())) {
            emailTextView.text = MAPP.USER_DATA.getMemberId()
        }

        // Name Text View
        val nameTextView = mContainer.fragment_my_page_name_text_view
        if (!TextUtils.isEmpty(MAPP.USER_DATA.getName())) {
            nameTextView.text = MAPP.USER_DATA.getName()
        }

        // Phone Number Text View
        mPhoneNumberTextView = mContainer.fragment_my_page_phone_number_text_view
        if (!TextUtils.isEmpty(MAPP.USER_DATA.getPhoneNumber())) {
            mPhoneNumberTextView.text = MAPP.USER_DATA.getPhoneNumber()
        }

        // Address Text View
        mAddressTextView = mContainer.fragment_my_page_address_text_view
        if (!TextUtils.isEmpty(MAPP.USER_DATA.getAddress())) {
            mAddressTextView.text = MAPP.USER_DATA.getAddress() + MAPP.USER_DATA.getAddressDetail()
        }

        // Company Member Text View
        mCompanyMemberTextView = mContainer.fragment_my_page_company_member_text_view
        mCompanyMemberTextView.text = if (MAPP.USER_DATA.getMemberType() == 1) {
            String.format(requireActivity().resources.getString(R.string.text_member_type), requireActivity().resources.getString(R.string.text_general_member))
        } else {
            String.format(requireActivity().resources.getString(R.string.text_member_type), requireActivity().resources.getString(R.string.text_enterprise_member))
        }

        // Select Language Spinner
        mSelectLanguageSpinner = mContainer.fragment_my_page_select_language_spinner
        mSelectLanguageSpinner.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(
                arrayListOf(
                    IconSpinnerItem(text = requireActivity().resources.getString(R.string.text_korean), iconRes = R.drawable.flag_korean),
                    IconSpinnerItem(text = requireActivity().resources.getString(R.string.text_english), iconRes = R.drawable.flag_english),
//                    IconSpinnerItem(text = requireActivity().resources.getString(R.string.text_filipino), iconRes = R.drawable.flag_filipino),
                    IconSpinnerItem(text = requireActivity().resources.getString(R.string.text_chinese), iconRes = R.drawable.flag_chinese),
                    IconSpinnerItem(text = requireActivity().resources.getString(R.string.text_japanese), iconRes = R.drawable.flag_japanese)
                ))
            getSpinnerRecyclerView().layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

            var selectLanguage = 0
            if (SharedPreferencesUtil().getSelectLanguage(requireActivity()) == SELECT_LANGUAGE_KO) {
                selectLanguage = 0
            } else if (SharedPreferencesUtil().getSelectLanguage(requireActivity()) == SELECT_LANGUAGE_EN) {
                selectLanguage = 1
            } else if (SharedPreferencesUtil().getSelectLanguage(requireActivity()) == SELECT_LANGUAGE_ZH) {
                selectLanguage = 2
            } else if (SharedPreferencesUtil().getSelectLanguage(requireActivity()) == SELECT_LANGUAGE_JA) {
                selectLanguage = 3
            }

            selectItemByIndex(selectLanguage)
            lifecycleOwner = this@MyPageFragment
        }
        mSelectLanguageSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<IconSpinnerItem?> { oldIndex, oldItem, newIndex, newItem ->
            when (newIndex) {
                0 -> {
                    SharedPreferencesUtil().setSelectLanguage(requireActivity(), SELECT_LANGUAGE_KO)
                }

                1 -> {
                    SharedPreferencesUtil().setSelectLanguage(requireActivity(), SELECT_LANGUAGE_EN)
                }

                2 -> {
                    SharedPreferencesUtil().setSelectLanguage(requireActivity(), SELECT_LANGUAGE_ZH)
                }

                3 -> {
                    SharedPreferencesUtil().setSelectLanguage(requireActivity(), SELECT_LANGUAGE_JA)
                }

                else -> {
                    SharedPreferencesUtil().setSelectLanguage(requireActivity(), SELECT_LANGUAGE_EN)
                }
            }

            selectLanguage(requireActivity(), SharedPreferencesUtil().getSelectLanguage(requireActivity())!!)
        })

        // Modify Personal Information View
        val modifyPersonalInformationView = mContainer.fragment_my_page_modify_personal_information_view
        modifyPersonalInformationView.setOnSingleClickListener {
            val intent = Intent(requireActivity(), SettingProfileActivity::class.java)
//            startActivity(intent)
            startActivityForResult(intent, INTENT_MEMBER_WITHDRAWAL_REQUEST_CODE)
        }

        // Logout Text View
        val logoutTextView = mContainer.fragment_my_page_logout_text_view
        logoutTextView.setOnSingleClickListener {
            showLogoutCustomDialog()
        }

        // Point History View
        val pointHistoryView = mContainer.fragment_my_page_point_history_view
        pointHistoryView.setOnSingleClickListener {
            val intent = Intent(requireActivity(), PointHistoryActivity::class.java)
            intent.putExtra(INTENT_DATA_COMPANY_UID_DATA, 0)
            startActivity(intent)
        }

        // GP Text View
        mGPTextView = mContainer.fragment_my_page_gp_text_view

        // Note View
        val noteView = mContainer.fragment_my_page_note_view
        noteView.setOnSingleClickListener {
            val intent = Intent(requireActivity(), NoteActivity::class.java)
            startActivity(intent)
        }

        // Regular View
        val regularView = mContainer.fragment_my_page_regular_view
        regularView.setOnSingleClickListener {
            val intent = Intent(requireActivity(), RegularActivity::class.java)
            startActivity(intent)
        }

        // Reservation View
        val reservationView = mContainer.fragment_my_page_reservation_view
        reservationView.setOnSingleClickListener {
//            val activity: Activity = requireActivity()
//            if (activity is MainActivity) {
//                val mainActivity: MainActivity = activity
//                mainActivity.showView(SHOW_TYPE_RESERVATION)
//            }
            val intent = Intent(requireActivity(), ReservationActivity::class.java)
            startActivity(intent)
        }

        // Used Market List View
        val usedMarketListView = mContainer.fragment_my_page_used_market_list_view
        usedMarketListView.setOnSingleClickListener {
            val intent = Intent(requireActivity(), MyUsedMarketActivity::class.java)
            startActivity(intent)
        }

        // Job View
        val jobView = mContainer.fragment_my_page_job_view
        jobView.setOnSingleClickListener {
            val mainMenuItemData = MainMenuItemData()
            mainMenuItemData.setCategoryUid(COMPANY_TYPE_JOB_AND_JOB_SEARCH)

            val intent = Intent(requireActivity(), JobActivity::class.java)
            intent.putExtra(INTENT_DATA_STORE_LIST_DATA, mainMenuItemData)
            intent.putExtra(INTENT_DATA_IS_MY_JOB_DATA, true)
            startActivity(intent)
        }

        // Key Word View
        val keyWordView = mContainer.fragment_my_page_key_word_view
        keyWordView.setOnSingleClickListener {
            val intent = Intent(requireActivity(), KeyWordActivity::class.java)
            startActivity(intent)
        }

        // Report View
        val reportView = mContainer.fragment_my_page_report_view
        reportView.setOnSingleClickListener {
            val mainMenuItemData = MainMenuItemData()
            mainMenuItemData.setCategoryUid(COMPANY_TYPE_REPORT_DISAPPEARANCE)

            val intent = Intent(requireActivity(), MissingActivity::class.java)
            intent.putExtra(INTENT_DATA_STORE_LIST_DATA, mainMenuItemData)
            intent.putExtra(INTENT_DATA_IS_MY_MISSING_DATA, true)
            startActivity(intent)
        }

        // Switch View
        mNotificationSwitch = mContainer.fragment_my_page_notification_switch_view
//        mNotificationSwitch.isOn = SharedPreferencesUtil().getIsNotification(requireActivity())
        mNotificationSwitch.setOnToggledListener { toggleableView, isOn ->
//            getChangePush(isOn)
            getChangePushStatus(isOn)
        }

        // Company Setting Expandable View
//        mCompanySettingExpandableLayout = mContainer.fragment_my_page_setting_company_expandable_view
//        mCompanySettingExpandableLayout.setOnClickListener {
//            mCompanySettingExpandableLayout.toggleLayout()
//            if (!mCompanySettingExpandableLayout.isExpanded) {
//                Handler(Looper.getMainLooper()).postDelayed({
//                    mScrollView.fullScroll(ScrollView.FOCUS_DOWN)
//                }, 200)
//            }
//        }



        // Expandable Sub Recycler View
        mCompanyRecyclerView = mContainer.fragment_my_page_company_recycler_view
        mCompanyRecyclerView.setHasFixedSize(true)
        mCompanyRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        mCompanyMemberAdapter.setOnItemClickListener(object : CompanyMemberAdapter.OnItemClickListener {
            override fun onClick(data: CompanyMemberData) {

                if (data.getCompanyManagerGrant() == COMPANY_GRANT_MASTER) {
                    val intent = Intent(requireActivity(), CompanyInformationActivity ::class.java)
                    intent.putExtra(INTENT_DATA_MY_COMPANY_DATA, data)
                    startActivity(intent)
                } else {
                    showDoNotHaveAccessCustomDialog()
                }

            }
        })
        mCompanyRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mCompanyRecyclerView.adapter = mCompanyMemberAdapter


        // Application Company Member View
        mApplicationCompanyMemberView = mContainer.fragment_my_page_application_company_member_view
        mApplicationCompanyMemberView.setOnSingleClickListener {
            val intent = Intent(requireActivity(), ApplicationCompanyMemberActivity ::class.java)
            startActivityForResult(intent, INTENT_DATA_COMPANY_APPLICATION_REQUEST_CODE)
        }

//        showCompanyMember()
        getCompanyMemberData()
        getMemberProfile()


    }

//    private fun showCompanyMember() {
//        if (MAPP.USER_DATA.getMemberType() == 1) {
//            mCompanySettingExpandableLayout.visibility = View.GONE
//        } else {
//            mCompanySettingExpandableLayout.visibility = View.VISIBLE
//        }
//    }

    private fun setCompanyManagementData() {
        if (MAPP.USER_DATA.getCompanyMemberListData().isEmpty()) {
            mCompanyRecyclerView.visibility = View.GONE
        } else {
            mCompanyRecyclerView.visibility = View.VISIBLE
            mCompanyMemberAdapter.setData(MAPP.USER_DATA.getCompanyMemberListData())
        }
    }

    private fun showDoNotHaveAccessCustomDialog() {
        val customDialog = CustomDialog(
            requireActivity(),
            DIALOG_TYPE_NO_CANCEL,
            requireActivity().resources.getString(R.string.text_noti),
            requireActivity().resources.getString(R.string.text_message_do_not_have_access),
            "",
            requireActivity().resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
            }

            override fun onConfirm() {
                customDialog.dismiss()
            }
        })
        customDialog.show()
    }

    private fun showLogoutCustomDialog() {
        val customDialog = CustomDialog(
            requireActivity(),
            DIALOG_TYPE_NORMAL,
            requireActivity().resources.getString(R.string.text_logout),
            requireActivity().resources.getString(R.string.text_message_logout),
            requireActivity().resources.getString(R.string.text_cancel),
            requireActivity().resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
                getLogout()
            }
        })
        customDialog.show()
    }

    private fun logoutInitInformation() {
        SharedPreferencesUtil().setSaveLocation(requireActivity(), "")
        SharedPreferencesUtil().setLocationName(requireActivity(), "")
        SharedPreferencesUtil().setSecureKey(requireActivity(), "")
        SharedPreferencesUtil().setRefreshKey(requireActivity(), "")
        SharedPreferencesUtil().setSaveNationCode(requireActivity(), 0)
        SharedPreferencesUtil().setSaveMainAreaCode(requireActivity(), 0)
        SharedPreferencesUtil().setSaveSubAreaCode(requireActivity(), 0)
        SharedPreferencesUtil().setDoNotWatchToday(requireActivity(), "")
        SharedPreferencesUtil().setLoginType(requireActivity(), "")

        MAPP.USER_DATA = UserData()
        MAPP.USER_FCM_TOKEN = ""
        MAPP.COMPANY_SALES_DATA = CompanySalesData()
        MAPP.SELECT_NATION_NAME = ""
        MAPP.PUSH_DATA = null
        MAPP.IS_NON_MEMBER_SERVICE = true

        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()

        if (null != mPhoneNumberTextView) {
            if (!TextUtils.isEmpty(MAPP.USER_DATA.getPhoneNumber())) {
                mPhoneNumberTextView.text = MAPP.USER_DATA.getPhoneNumber()
            }
        }

        if (null != mProfileImageView) {
            mProfileImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + MAPP.USER_DATA.getProfileName()))
        }

        if (!TextUtils.isEmpty(MAPP.USER_DATA.getAddress())) {
            mAddressTextView.text = MAPP.USER_DATA.getAddress() + MAPP.USER_DATA.getAddressDetail()
        }

        getPoint()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                INTENT_MEMBER_WITHDRAWAL_REQUEST_CODE -> {
                    requireActivity().finishAffinity()
                }

                INTENT_DATA_COMPANY_APPLICATION_REQUEST_CODE -> {
                    getCompanyMemberData()
                }
            }
        }
    }

    // ==========================================================================================
    // API
    // ==========================================================================================

    /**
     * API. Get Company Member Data
     */
    private fun getCompanyMemberData() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyMember(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<CompanyMemberData>> {
                override fun onSuccess(resultData: ArrayList<CompanyMemberData>) {
                    if (resultData.isNotEmpty()) {
                        MAPP.USER_DATA.setCompanyMemberListData(resultData)
                    }
                    setCompanyManagementData()
//                    mCompanyMemberAdapter.setData(MAPP.USER_DATA.getCompanyMemberListData())
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            MAPP.SELECT_LANGUAGE_CODE,
            MAPP.USER_DATA.getSecureKey()
        )
    }

    /**
     * API. Get Member Profile
     */
    private fun getMemberProfile() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getMemberProfile(
            mRequestQueue,
            object : NagajaManager.RequestListener<MemberProfileData> {
                override fun onSuccess(resultData: MemberProfileData) {
                    MAPP.MEMBER_PROFILE_DATA = resultData
                    SharedPreferencesUtil().setIsNotification(requireActivity(), MAPP.MEMBER_PROFILE_DATA.getIsPush())
                    mNotificationSwitch.isOn = SharedPreferencesUtil().getIsNotification(requireActivity())
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey()
        )
    }

    /**
     * API. Get Change Push Status
     */
    private fun getChangePushStatus(isStatus: Boolean) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getChangePushStatus(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    SharedPreferencesUtil().setIsNotification(requireActivity(), isStatus)
                    NagajaLog().e("wooks, Push = ${SharedPreferencesUtil().getIsNotification(requireActivity())}")
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            if (isStatus) 1 else 0
        )
    }

    /**
     * API. Get Company Member Data
     */
    private fun getLogout() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getLogout(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    when (SharedPreferencesUtil().getLoginType(requireActivity())) {
                        LOGIN_TYPE_KAKAO -> {
                            UserApiClient.instance.logout { error ->
                                NagajaLog().e("wooks, Kakao Logout")
                                logoutInitInformation()
                            }
                        }

                        LOGIN_TYPE_NAVER -> {
                            val oAuthLoginModule = OAuthLogin.getInstance()
                            oAuthLoginModule.init(requireActivity(), getString(R.string.naver_client_id), getString(R.string.naver_client_secret), getString(R.string.naver_client_name))
                            oAuthLoginModule.logout(requireActivity())

                            NagajaLog().e("wooks, Naver Logout")
                            logoutInitInformation()
                        }

                        LOGIN_TYPE_GOOGLE -> {
                            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .build()
                            mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                            mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity(), OnCompleteListener<Void?> {
                                NagajaLog().d("wooks, Google Logout")
                                logoutInitInformation()
                            })
                        }

                        LOGIN_TYPE_FACEBOOK -> {
                            val accessToken = AccessToken.getCurrentAccessToken()
                            val isLoggedIn = accessToken != null && !accessToken.isExpired
                            if (isLoggedIn) {
                                FirebaseAuth.getInstance().signOut()
                                LoginManager.getInstance().logOut()
                                NagajaLog().e("wooks, 페이스북 로그아웃")
                                logoutInitInformation()
                            } else {
                                NagajaLog().e("wooks, 페이스북 세션 없음")
                            }
                        }

                        LOGIN_TYPE_EMAIL -> {
                            logoutInitInformation()
                        }
                    }
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            MAPP.USER_DATA.getMemberId()
        )
    }

    /**
     * API. Get Point
     */
    private fun getPoint() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getPoint(
            mRequestQueue,
            object : NagajaManager.RequestListener<PointData> {
                override fun onSuccess(resultData: PointData) {
                    mGPTextView.text = Util().convertToComma(resultData.getTotalPoint())
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            0
        )
    }
}