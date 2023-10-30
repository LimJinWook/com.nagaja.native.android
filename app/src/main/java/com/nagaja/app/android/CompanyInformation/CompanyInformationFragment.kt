package com.nagaja.app.android.CompanyInformation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.CompanyInformation.CompanyDefaultFragment.Companion.TAB_TYPE_DEFAULT
import com.nagaja.app.android.CompanyInformation.CompanyDefaultFragment.Companion.TAB_TYPE_PRODUCT
import com.nagaja.app.android.CompanyInformation.CompanyDefaultFragment.Companion.TAB_TYPE_SALES
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_company_information.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.util.*
import kotlin.collections.ArrayList

class CompanyInformationFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mCompanyNoteView: View
    private lateinit var mCompanyRegularView: View
    private lateinit var mCompanyReservationView: View
    private lateinit var mCompanyUsedMarketView: View
    private lateinit var mCompanyJobView: View

    private lateinit var mCompanyImageView: SimpleDraweeView

    private lateinit var mCompanyNameTextView: TextView
    private lateinit var mCompanyTypeTextView: TextView
    private lateinit var mManagerTextView: TextView
    private lateinit var mAddressTextView: TextView
    private lateinit var mMasterNameTextView: TextView
    private lateinit var mManagerNameTextView: TextView
    private lateinit var mFrontCountTextView: TextView
    private lateinit var mServingNameTextView: TextView
    private lateinit var mGPTextView: TextView

    private lateinit var mStatusReady: TextView
    private lateinit var mStatusOpen: TextView
    private lateinit var mStatusRest: TextView
    private lateinit var mStatusClose: TextView
    private lateinit var mStatusFinish: TextView

    private lateinit var mNoteCountTextView: TextView
    private lateinit var mRegularCountTextView: TextView
    private lateinit var mReservationCountTextView: TextView
    private lateinit var mDeclareCountTextView: TextView

    private lateinit var mListener: OnCompanyInformationFragmentListener

    private lateinit var mCompanyDefaultData: CompanyDefaultData

    private lateinit var mRequestQueue: RequestQueue

    interface OnCompanyInformationFragmentListener {
        fun onBack()
        fun onCompanyDefaultScreen(data: CompanyDefaultData, selectType: Int)
        fun onCompanyNoteScreen(data: CompanyDefaultData)
        fun onCompanyRegularScreen(data: CompanyDefaultData)
        fun onCompanyReservationScreen(data: CompanyDefaultData)
        fun onCompanyUsedMarketScreen(data: CompanyDefaultData)
        fun onCompanyJobScreen(data: CompanyDefaultData)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
        fun onPointHistoryScreen(companyUid: Int)
    }

    companion object {
        // 1: 영업준비중, 3: 오픈준비중, 5: 영업중, 7: 휴식시간, 8: 마감중, 9: 영업종료
        const val STATUS_READ               = 0x01
        const val STATUS_OPEN_READ          = 0x03
        const val STATUS_OPEN               = 0x05
        const val STATUS_REST               = 0x07
        const val STATUS_CLOSE              = 0x08
        const val STATUS_FINISH             = 0x09

        private const val ARG_COMPANY_MEMBER_DATA              = "ARG_COMPANY_MEMBER_DATA"

        fun newInstance(companyMemberData: CompanyMemberData): CompanyInformationFragment {
            val args = Bundle()
            val fragment = CompanyInformationFragment()
            args.putSerializable(ARG_COMPANY_MEMBER_DATA, companyMemberData)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getCompanyMemberData(): CompanyMemberData {
        val arguments = arguments
        return arguments?.getSerializable(ARG_COMPANY_MEMBER_DATA) as CompanyMemberData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContainer = inflater.inflate(R.layout.fragment_company_information, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Header Location View
        val headerLocationView = mContainer.layout_header_location_view
        headerLocationView.setOnSingleClickListener {
            mListener.onChangeLocationAndNotificationScreen(NagajaActivity.SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION)
        }

        // Header Location Text View
        val locationTextView = mContainer.layout_header_location_text_view
        if (!TextUtils.isEmpty(MAPP.SELECT_NATION_NAME)) {
            locationTextView.text = MAPP.SELECT_NATION_NAME
        }

        // Header Select Language Image View
        val selectLanguageImageView = mContainer.layout_header_select_language_image_view
        selectLanguageImageView.setImageResource(getLanguageIcon(mActivity))
        selectLanguageImageView.setOnClickListener {
            showSelectLanguageCustomDialog(mActivity)
        }

        // Header Search Image View
        val headerSearchImageView = mContainer.layout_header_search_image_view
        headerSearchImageView.setOnSingleClickListener {
            mListener.onChangeLocationAndNotificationScreen(NagajaActivity.SELECT_HEADER_MENU_TYPE_SEARCH)
        }

        // Header Share Image View
        val headerShareImageView = mContainer.layout_header_clipboard_image_view
        headerShareImageView.visibility = View.GONE
//        headerShareImageView.setOnSingleClickListener {
//        }

        // Header Notification Image View
        val headerNotificationImageView = mContainer.layout_header_notification_image_view
        headerNotificationImageView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                mListener.onChangeLocationAndNotificationScreen(NagajaActivity.SELECT_HEADER_MENU_TYPE_NOTIFICATION)
            }
        }

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_company_information)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onBack()
        }

        // Company Image View
        mCompanyImageView = mContainer.fragment_company_information_company_image_view

        // Company Name Text View
        mCompanyNameTextView = mContainer.fragment_company_information_company_name_text_view

        // Company Setting Image View
        val companySettingImageView = mContainer.fragment_company_information_company_setting_image_view
        companySettingImageView.setOnClickListener {
            mListener.onCompanyDefaultScreen(mCompanyDefaultData, TAB_TYPE_DEFAULT)
        }

        // Company Type Text View
        mCompanyTypeTextView = mContainer.fragment_company_information_company_type_text_view

        // Manager Text View
        mManagerTextView = mContainer.fragment_company_information_company_manager_text_view

        // Address Text View
        mAddressTextView = mContainer.fragment_company_information_company_address_text_view

        // GP Point View
        val pointView = mContainer.fragment_company_information_company_point_view
        pointView.setOnSingleClickListener {
            mListener.onPointHistoryScreen(getCompanyMemberData().getCompanyUid())
        }

        // GP Text View
        mGPTextView = mContainer.fragment_company_information_company_gp_text_view

        // Master Name Text View
        mMasterNameTextView = mContainer.fragment_company_information_master_name_text_view

        // Manager Name Text View
        mManagerNameTextView = mContainer.fragment_company_information_manager_name_text_view

        // Front Count Text View
        mFrontCountTextView = mContainer.fragment_company_information_front_count_text_view

        // Serving Name Text View
        mServingNameTextView = mContainer.fragment_company_information_serving_name_text_view








        // Status Ready Text View
        mStatusReady = mContainer.fragment_company_information_status_ready_text_view
        mStatusReady.setOnClickListener {
            getCompanyChangeServiceStatus(STATUS_READ)
        }

        // Status Open Text View
        mStatusOpen = mContainer.fragment_company_information_status_open_text_view
        mStatusOpen.setOnClickListener {
            getCompanyChangeServiceStatus(STATUS_OPEN)
        }

        // Status Rest Text View
        mStatusRest = mContainer.fragment_company_information_status_rest_text_view
        mStatusRest.setOnClickListener {
            getCompanyChangeServiceStatus(STATUS_REST)
        }

        // Status Close Text View
        mStatusClose = mContainer.fragment_company_information_status_close_text_view
        mStatusClose.setOnClickListener {
            getCompanyChangeServiceStatus(STATUS_CLOSE)
        }

        // Status Finish Text View
        mStatusFinish = mContainer.fragment_company_information_status_finish_text_view
        mStatusFinish.setOnClickListener {
            getCompanyChangeServiceStatus(STATUS_FINISH)
        }

        // Sales Management Text View
        val salesManagementTextView = mContainer.fragment_company_information_status_sales_management_text_view
        salesManagementTextView.setOnClickListener {
            mListener.onCompanyDefaultScreen(mCompanyDefaultData, TAB_TYPE_SALES)
        }

        // Product Management Text View
        val productManagementTextView = mContainer.fragment_company_information_status_product_management_text_view
        productManagementTextView.setOnClickListener {
            mListener.onCompanyDefaultScreen(mCompanyDefaultData, TAB_TYPE_PRODUCT)
        }




        // Company Note View
        mCompanyNoteView = mContainer.fragment_company_information_note_view
        mCompanyNoteView.setOnSingleClickListener {
            mListener.onCompanyNoteScreen(mCompanyDefaultData)
        }

        // Note Count Text View
        mNoteCountTextView = mContainer.fragment_company_information_note_count_text_view

        // Company Regular View
        mCompanyRegularView = mContainer.fragment_company_information_regular_view
        mCompanyRegularView.setOnSingleClickListener {
            mListener.onCompanyRegularScreen(mCompanyDefaultData)
        }

        // Regular Count Text View
        mRegularCountTextView = mContainer.fragment_company_information_regular_count_text_view

        // Company Reservation View
        mCompanyReservationView = mContainer.fragment_company_information_reservation_view
        mCompanyReservationView.setOnSingleClickListener {
            mListener.onCompanyReservationScreen(mCompanyDefaultData)
        }

        // Company Used Market View
        mCompanyUsedMarketView = mContainer.fragment_company_information_used_market_view
        mCompanyUsedMarketView.setOnSingleClickListener {
            mListener.onCompanyUsedMarketScreen(mCompanyDefaultData)
        }

        // Company Job View
        mCompanyJobView = mContainer.fragment_company_information_job_view
        mCompanyJobView.setOnSingleClickListener {
            mListener.onCompanyJobScreen(mCompanyDefaultData)
        }

        // Reservation Count Text View
        mReservationCountTextView = mContainer.fragment_company_information_reservation_count_text_view

        // Declare Count Text View
        mDeclareCountTextView = mContainer.fragment_company_information_declare_count_text_view


        getCompanyDefaultData()
        getPoint()
    }

    private fun setCompanyStatusUpdate(status: Int) {

        when (status) {
            STATUS_READ, STATUS_OPEN_READ -> {
                mStatusReady.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mStatusReady.setBackgroundResource(R.drawable.bg_status_left)

                mStatusOpen.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusOpen.setBackgroundResource(R.drawable.bg_status_disable)
                mStatusOpen.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))

                mStatusRest.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusRest.setBackgroundResource(R.drawable.bg_status_disable)

                mStatusClose.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusClose.setBackgroundResource(R.drawable.bg_status_disable)

                mStatusFinish.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusFinish.setBackgroundResource(R.drawable.bg_status_right_disable)
            }

            STATUS_OPEN -> {
                mStatusReady.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusReady.setBackgroundResource(R.drawable.bg_status_left_disable)

                mStatusOpen.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mStatusOpen.setBackgroundResource(R.drawable.bg_status_enable)

                mStatusRest.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusRest.setBackgroundResource(R.drawable.bg_status_disable)

                mStatusClose.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusClose.setBackgroundResource(R.drawable.bg_status_disable)

                mStatusFinish.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusFinish.setBackgroundResource(R.drawable.bg_status_right_disable)
            }

            STATUS_REST -> {
                mStatusReady.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusReady.setBackgroundResource(R.drawable.bg_status_left_disable)

                mStatusOpen.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusOpen.setBackgroundResource(R.drawable.bg_status_disable)

                mStatusRest.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mStatusRest.setBackgroundResource(R.drawable.bg_status_enable)

                mStatusClose.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusClose.setBackgroundResource(R.drawable.bg_status_disable)

                mStatusFinish.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusFinish.setBackgroundResource(R.drawable.bg_status_right_disable)
            }

            STATUS_CLOSE -> {
                mStatusReady.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusReady.setBackgroundResource(R.drawable.bg_status_left_disable)

                mStatusOpen.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusOpen.setBackgroundResource(R.drawable.bg_status_disable)

                mStatusRest.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusRest.setBackgroundResource(R.drawable.bg_status_disable)

                mStatusClose.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mStatusClose.setBackgroundResource(R.drawable.bg_status_enable)

                mStatusFinish.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusFinish.setBackgroundResource(R.drawable.bg_status_right_disable)
            }

            STATUS_FINISH -> {
                mStatusReady.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusReady.setBackgroundResource(R.drawable.bg_status_left_disable)

                mStatusOpen.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusOpen.setBackgroundResource(R.drawable.bg_status_disable)

                mStatusRest.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusRest.setBackgroundResource(R.drawable.bg_status_disable)

                mStatusClose.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bebebe))
                mStatusClose.setBackgroundResource(R.drawable.bg_status_disable)

                mStatusFinish.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mStatusFinish.setBackgroundResource(R.drawable.bg_status_right)
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun displayCompanyDefaultData(data: CompanyDefaultData) {
        mCompanyDefaultData = data

        mCompanyImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getCompanyMainImageUrl()))

        if (!TextUtils.isEmpty(data.getCompanyName())) {
            mCompanyNameTextView.text = data.getCompanyName()
        } else {
            mCompanyNameTextView.text = data.getCompanyNameEnglish()
        }

        if (data.getCategoryUid() > 0) {
            mCompanyTypeTextView.text = Util().getCompanyType(mActivity, data.getCategoryUid())
        }

        if (!TextUtils.isEmpty(data.getManageName())) {
            mManagerTextView.text = data.getManageName()
        }

        if (!TextUtils.isEmpty(data.getCompanyAddress())) {
            mAddressTextView.text = data.getCompanyAddress() + " " + data.getCompanyAddressDetail()
        }

        when (data.getServiceStatus().toInt()) {
            STATUS_READ, STATUS_OPEN_READ -> {
                setCompanyStatusUpdate(STATUS_READ)
            }

            STATUS_OPEN -> {
                setCompanyStatusUpdate(STATUS_OPEN)
            }

            STATUS_REST -> {
                setCompanyStatusUpdate(STATUS_REST)
            }

            STATUS_CLOSE -> {
                setCompanyStatusUpdate(STATUS_CLOSE)
            }

            STATUS_FINISH -> {
                setCompanyStatusUpdate(STATUS_FINISH)
            }
        }
    }

    private fun displayCompanyManagerData(dataList: ArrayList<CompanyManagerData>) {

        var masterName = ""
        var managerName = ""
        var frontCount = ""
        var servingName = ""

        for (i in dataList.indices) {
            if (dataList[i].getCompanyManagerGrant() == 1) {
                masterName = if (TextUtils.isEmpty(masterName)) {
                    dataList[i].getMemberName()
                } else {
                    masterName + ", " + dataList[i].getMemberName()
                }
            } else if (dataList[i].getCompanyManagerGrant() == 2) {
                managerName = if (TextUtils.isEmpty(managerName)) {
                    dataList[i].getMemberName()
                } else {
                    managerName + ", " + dataList[i].getMemberName()
                }
            }  else if (dataList[i].getCompanyManagerGrant() == 3) {
                servingName = if (TextUtils.isEmpty(servingName)) {
                    dataList[i].getMemberName()
                } else {
                    servingName + ", " + dataList[i].getMemberName()
                }
            }
        }

        mMasterNameTextView.text = masterName
        mManagerNameTextView.text = managerName
        mFrontCountTextView.text = String.format(mActivity.resources.getString(R.string.text_number_of_people), dataList.size)
        mServingNameTextView.text = servingName
    }

    private fun displayNotificationCountData(data: CompanyNotificationCountData) {
        mNoteCountTextView.text = String.format(mActivity.resources.getString(R.string.text_note_count), data.getNoteCount())
        mRegularCountTextView.text = String.format(mActivity.resources.getString(R.string.text_regular_count), data.getRegularCount())
        mReservationCountTextView.text = String.format(mActivity.resources.getString(R.string.text_note_count), data.getReservationCount())
        mDeclareCountTextView.text = String.format(mActivity.resources.getString(R.string.text_note_count), data.getDeclareCount())

    }

    private fun showLoginGuideCustomDialog() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_noti),
            mActivity.resources.getString(R.string.text_message_login_guide),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
                mListener.onDefaultLoginScreen()
            }
        })
        customDialog.show()
    }

    override fun onPause() {
        super.onPause()
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
        if (context is OnCompanyInformationFragmentListener) {
            mActivity = context as Activity

            if (context is OnCompanyInformationFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnCompanyInformationFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnCompanyInformationFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnCompanyInformationFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    // ==========================================================================================
    // API
    // ==========================================================================================

    /**
     * API. Get Company Member Data
     */
    private fun getCompanyDefaultData() {
        if (TextUtils.isEmpty(MAPP.USER_DATA.getSecureKey())) {
            return
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyDefaultData(
            mRequestQueue,
            object : NagajaManager.RequestListener<CompanyDefaultData> {
                override fun onSuccess(resultData: CompanyDefaultData) {

                    displayCompanyDefaultData(resultData)

                    getCompanyManagerData()
                    getCompanyNotificationCount()
                }

                override fun onFail(errorMsg: String?) {
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            getCompanyMemberData().getCompanyUid()
        )
    }

    /**
     * API. Get Company Manager Data
     */
    private fun getCompanyManagerData() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyManagerData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<CompanyManagerData>> {
                override fun onSuccess(resultData: ArrayList<CompanyManagerData>) {
                    if (resultData.isNotEmpty()) {
                        displayCompanyManagerData(resultData)
                    }
                }

                override fun onFail(errorMsg: String?) {
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            getCompanyMemberData().getCompanyUid(),
        )
    }

    /**
     * API. Get Company Notification Count
     */
    private fun getCompanyNotificationCount() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyNotificationCount(
            mRequestQueue,
            object : NagajaManager.RequestListener<CompanyNotificationCountData> {
                override fun onSuccess(resultData: CompanyNotificationCountData) {
                    displayNotificationCountData(resultData)
                }

                override fun onFail(errorMsg: String?) {
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            getCompanyMemberData().getCompanyUid()
        )
    }

    /**
     * API. Get Company Change Service Status
     */
    private fun getCompanyChangeServiceStatus(changeStatus: Int) {
        val secureKey = MAPP.USER_DATA.getSecureKey()
        if (TextUtils.isEmpty(secureKey)) {
            return
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyChangeServiceStatus(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    setCompanyStatusUpdate(changeStatus)
                }

                override fun onFail(errorMsg: String?) {
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            secureKey,
            mCompanyDefaultData.getCompanyServiceUid(),
            changeStatus,
            mCompanyDefaultData.getCompanyUid()
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
            getCompanyMemberData().getCompanyUid()
        )
    }
}