package com.nagaja.app.android.CompanyReservation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Dialog.CustomDialogReservationCalendar
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Adapter.CompanyReservationAdapter
import com.nagaja.app.android.Adapter.ReservationMemoAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_company_reservation.view.*
import kotlinx.android.synthetic.main.fragment_faq.view.*
import kotlinx.android.synthetic.main.fragment_full_screen_image.view.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.fragment_reservation.view.*
import kotlinx.android.synthetic.main.fragment_used_market_list.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_reservation_information.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class CompanyReservationFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mReservationListView: View
    private lateinit var mReservationInformationView: View
    private lateinit var mLoadingView: View
    private lateinit var mReservationCompanyMemoView: View
    private lateinit var mReservationBottomView: View
    private lateinit var mReservationRecordeTimeView: View
    private lateinit var mReservationMemoView: View

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mReservationMemoEditText: EditText

    private lateinit var mAllTextView: TextView
    private lateinit var mReservationStatusTextView: TextView
    private lateinit var mCancelStatusTextView: TextView
    private lateinit var mDateSearchTextView: TextView
    private lateinit var mEmptyTextView: TextView
    private lateinit var mReservationStatusChangeTextView: TextView

    private lateinit var mReservationDateTextView: TextView
    private lateinit var mReservationTimeTextView: TextView
    private lateinit var mReservationPersonCountTextView: TextView
    private lateinit var mReservationNameTextView: TextView
    private lateinit var mReservationPhoneNumberTextView: TextView
    private lateinit var mReservationRequestTextView: TextView
    private lateinit var mReservationRecordeTimeTextView: TextView
    private lateinit var mReservationRecordeStatusTextView: TextView
    private lateinit var mReservationMemoSaveTextView: TextView



    private lateinit var mReservationInformationBackImageView: ImageView

    private lateinit var mReservationRecyclerView: RecyclerView
    private lateinit var mCompanyReservationAdapter: CompanyReservationAdapter

    private lateinit var mReservationMemoRecyclerView: RecyclerView
    private lateinit var mReservationMemoAdapter: ReservationMemoAdapter

    private lateinit var mListener: OnCompanyReservationFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private lateinit var mLoadingLottieView: LottieAnimationView

    private var mReservationListData = ArrayList<ReservationData>()
    private lateinit var mReservationData: ReservationData

    private var mReservationStatusType = 0
    private var mPageCount = 1
    private var mItemTotalCount = 0

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    interface OnCompanyReservationFragmentListener {
        fun onFinish()
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
        fun onChatViewScreen(chatClass: String, chatData: String)
    }

    companion object {

        const val ARGS_COMPANY_DEFAULT_DATA                = "args_company_default_data"

        fun newInstance(data: CompanyDefaultData): CompanyReservationFragment {
            val args = Bundle()
            val fragment = CompanyReservationFragment()
            args.putSerializable(ARGS_COMPANY_DEFAULT_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getCompanyDefaultData(): CompanyDefaultData {
        val arguments = arguments
        return arguments?.getSerializable(ARGS_COMPANY_DEFAULT_DATA) as CompanyDefaultData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)

        mCompanyReservationAdapter = CompanyReservationAdapter(mActivity)
        mReservationMemoAdapter = ReservationMemoAdapter(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContainer = inflater.inflate(R.layout.fragment_company_reservation, container, false)

        init()

        return mContainer
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
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
        titleTextView.text = mActivity.resources.getString(R.string.text_company_reservation_list)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_company_reservation_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(mActivity)) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mItemTotalCount = 0

                getReservationList(true, "", "")
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Company Image View
        val companyImageView = mContainer.fragment_company_reservation_image_view
        if (!TextUtils.isEmpty(getCompanyDefaultData().getCompanyMainImageUrl())) {
            companyImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + getCompanyDefaultData().getCompanyMainImageUrl()))
        }

        // Company Name Text View
        val companyNameTextView = mContainer.fragment_company_reservation_name_text_view
        if (!TextUtils.isEmpty(getCompanyDefaultData().getCompanyName())) {
            companyNameTextView.text = getCompanyDefaultData().getCompanyName()
        }

        // Company Service Type Text View
        val companyServiceTypeTextView = mContainer.fragment_company_reservation_type_text_view
        if (getCompanyDefaultData().getCategoryUid() > 0) {
            companyServiceTypeTextView.text = Util().getCompanyType(mActivity, getCompanyDefaultData().getCategoryUid())
        }

        // Company Manager Text View
        val companyManagerTextView = mContainer.fragment_company_reservation_manager_text_view
        if (!TextUtils.isEmpty(getCompanyDefaultData().getManageName())) {
            companyManagerTextView.text = getCompanyDefaultData().getManageName()
        }

        // Company Address Text View
        val companyAddressTextView = mContainer.fragment_company_reservation_address_text_view
        if (!TextUtils.isEmpty(getCompanyDefaultData().getCompanyAddress())) {
            companyAddressTextView.text = getCompanyDefaultData().getCompanyAddress() + " " + getCompanyDefaultData().getCompanyAddressDetail()
        }

        // Reservation List View
        mReservationListView = mContainer.fragment_company_reservation_view

        // All View
        mAllTextView = mContainer.fragment_company_reservation_all_text_view
        mAllTextView.setOnClickListener {
            setTabView(RESERVATION_TYPE_ALL)
        }

        // Reservation Status Text View
        mReservationStatusTextView = mContainer.fragment_company_reservation_status_text_view
        mReservationStatusTextView.setOnClickListener {
            setTabView(RESERVATION_TYPE_RESERVATION_STATUS)
        }

        // Cancel Status Text View
        mCancelStatusTextView = mContainer.fragment_company_reservation_cancel_status_text_view
        mCancelStatusTextView.setOnClickListener {
            setTabView(RESERVATION_TYPE_CANCEL)
        }

        // Date Search Text View
        mDateSearchTextView = mContainer.fragment_company_reservation_date_search_text_view
        mDateSearchTextView.setOnClickListener {
            setTabView(RESERVATION_TYPE_DATE_SEARCH)
        }

        // Reservation Recycler View
        mReservationRecyclerView = mContainer.fragment_company_reservation_status_recycler_view
        mReservationRecyclerView.setHasFixedSize(true)
        mReservationRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mReservationRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!mReservationRecyclerView.canScrollVertically(1) && !mReservationRecyclerView.canScrollVertically(-1)) {
                    return
                }

                if (!mReservationRecyclerView.canScrollVertically(1)) {
                    if (!mIsEndOfScroll) {
                        if (mReservationListData.size >= mItemTotalCount) {
                            return
                        }

                        mIsEndOfScroll = true
                        getReservationList(false, "", "")
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        mCompanyReservationAdapter.setOnReservationClickListener(object : CompanyReservationAdapter.OnReservationClickListener {
            override fun onClick(data: ReservationData) {
                mReservationData = data

                getReservationMemoList(data)
                displayReservationDetail(data)
            }

            override fun onChat(data: ReservationData) {
                if (data.getMemberUid() > 0) {
                    val chatData = "[{${MAPP.USER_DATA.getMemberUid()}, ${data.getCompanyUid()}}, {${data.getMemberUid()}, 0}]"
                    if (!TextUtils.isEmpty(chatData)) {
                        mListener.onChatViewScreen(CHAT_CLASS_COMPANY, chatData)
                    }
                }
            }

            override fun onPhoneCall(phoneNumber: String) {
                if (TextUtils.isEmpty(phoneNumber)) {
                    showToast(mActivity, mActivity.resources.getString(R.string.text_error_empty_phone_number))
                    return
                }

                var number = phoneNumber.replace(" ", "")
                number = number.replace("-", "")
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                startActivity(intent)
            }
        })
        mReservationRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mReservationRecyclerView.adapter = mCompanyReservationAdapter


        // Empty Text View
        mEmptyTextView = mContainer.fragment_company_reservation_empty_text_view

        // Reservation Information View
        mReservationInformationView = mContainer.fragment_company_reservation_information_view

        val companyInformationView = mContainer.layout_reservation_information_view
        companyInformationView.visibility = View.GONE

        mReservationInformationBackImageView = mContainer.layout_reservation_information_back_image_view
        mReservationInformationBackImageView.visibility = View.VISIBLE
        mReservationInformationBackImageView.setOnClickListener {
            mReservationInformationView.visibility = View.GONE
            mReservationListView.visibility = View.VISIBLE
        }

        // Reservation Date Text View
        mReservationDateTextView = mContainer.layout_reservation_information_reservation_date_text_view

        // Reservation Time Text View
        mReservationTimeTextView = mContainer.layout_reservation_information_reservation_time_text_view

        // Reservation Person Count Text View
        mReservationPersonCountTextView = mContainer.layout_reservation_information_reservation_person_count_text_view

        // Reservation Name Text View
        mReservationNameTextView = mContainer.layout_reservation_information_reservation_name_text_view

        // Reservation Phone Number Text View
        mReservationPhoneNumberTextView = mContainer.layout_reservation_information_reservation_phone_number_text_view

        // Reservation Request Text View
        mReservationRequestTextView = mContainer.layout_reservation_information_reservation_request_text_view

        // Reservation Recorde Time Text View
        mReservationRecordeTimeTextView = mContainer.layout_reservation_information_reservation_recorde_time_text_view

        // Reservation Recorde Status Text View
        mReservationRecordeStatusTextView = mContainer.layout_reservation_information_reservation_recorde_text_view

        // Reservation Company Memo View
        mReservationCompanyMemoView = mContainer.layout_reservation_information_reservation_company_memo_view
        mReservationCompanyMemoView.visibility = View.VISIBLE

        // Reservation Memo View
        mReservationMemoView = mContainer.layout_reservation_information_reservation_memo_view
        mReservationMemoView.visibility = if (getIsMaster()) View.VISIBLE else View.GONE

        // Reservation Memo Edit Text
        mReservationMemoEditText = mContainer.layout_reservation_information_reservation_memo_edit_text
        mReservationMemoEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        mReservationMemoEditText.setOnTouchListener(View.OnTouchListener { v, event ->
            if (v.id == R.id.layout_reservation_information_reservation_memo_edit_text) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })
//        mReservationMemoEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener{
//            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
//                if (actionId == EditorInfo.IME_ACTION_DONE){
//                    if (TextUtils.isEmpty(mReservationMemoEditText.text)) {
//                        showToast(mActivity, mActivity.resources.getString(R.string.text_message_input_memo))
//                        return false
//                    }
//
//                    getReservationMemoSave(mReservationMemoEditText.text.toString(), mReservationData)
//                    return true
//                }
//                return false
//            }
//        })

        // Reservation Memo Save Text View
        mReservationMemoSaveTextView = mContainer.layout_reservation_information_reservation_memo_save_text_view
        mReservationMemoSaveTextView.setOnClickListener {
            if (TextUtils.isEmpty(mReservationMemoEditText.text)) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_input_memo))
                return@setOnClickListener
            }

            getReservationMemoSave(mReservationMemoEditText.text.toString(), mReservationData)
        }

        // Reservation Recorde Time View
        mReservationRecordeTimeView = mContainer.layout_reservation_information_reservation_recorde_time_view
        mReservationRecordeTimeView.visibility = View.GONE

        // Reservation Bottom View
        mReservationBottomView = mContainer.layout_reservation_information_reservation_bottom_view

        // Reservation Memo Recycler View
        mReservationMemoRecyclerView = mContainer.layout_reservation_information_reservation_memo_list_recycler_view
        mReservationMemoRecyclerView.setHasFixedSize(true)
        mReservationMemoRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mReservationMemoAdapter.setOnItemClickListener(object : ReservationMemoAdapter.OnItemClickListener {
            override fun onClick(data: ReservationMemoData) {
                // TODO("Not yet implemented")
            }
        })
        mReservationMemoRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mReservationMemoRecyclerView.adapter = mReservationMemoAdapter

        // Reservation Status Change Text View
        mReservationStatusChangeTextView = mContainer.layout_reservation_information_reservation_status_change_text_view
        mReservationStatusChangeTextView.setOnClickListener {
            var status = 0
            if (mReservationData.getReservationStatus() == TYPE_RESERVATION_APPLICATION) {
                status = TYPE_RESERVATION_CONFIRMATION
            } else if (mReservationData.getReservationStatus() == TYPE_RESERVATION_CONFIRMATION) {
                status = TYPE_RESERVATION_COMPLETE
            } else if (mReservationData.getReservationStatus() == TYPE_RESERVATION_COMPLETE) {
                status = TYPE_RESERVATION_USE_COMPLETE
            }

            getCompanyReservationStatusChange(status)
        }

        // Reservation Cancel Text View
        val reservationCancelTextView = mContainer.layout_reservation_information_reservation_cancellation_text_view
        reservationCancelTextView.setOnClickListener {
            showCustomDialogReservationCancel()
        }




        // Loading View
        mLoadingView = mContainer.fragment_company_reservation_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_company_reservation_loading_lottie_view




        getReservationList(true, "", "")
    }

    private fun setTabView(type: Int) {

        when (type) {
            RESERVATION_TYPE_ALL -> {
                mAllTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))
                mAllTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))

                mReservationStatusTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mReservationStatusTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))

                mCancelStatusTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mCancelStatusTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))

                mDateSearchTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mDateSearchTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))

                mReservationStatusType = 0
                getReservationList(true, "", "")
            }

            RESERVATION_TYPE_RESERVATION_STATUS -> {
                mAllTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mAllTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))

                mReservationStatusTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))
                mReservationStatusTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))

                mCancelStatusTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mCancelStatusTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))

                mDateSearchTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mDateSearchTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))

                mReservationStatusType = 1
                getReservationList(true, "", "")
            }

            RESERVATION_TYPE_CANCEL -> {
                mAllTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mAllTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))

                mReservationStatusTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mReservationStatusTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))

                mCancelStatusTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))
                mCancelStatusTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))

                mDateSearchTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mDateSearchTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))

                mReservationStatusType = 2
                getReservationList(true, "", "")
            }

            RESERVATION_TYPE_DATE_SEARCH -> {
                mAllTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mAllTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))

                mReservationStatusTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mReservationStatusTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))

                mCancelStatusTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
                mCancelStatusTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))

                mDateSearchTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_000000))
                mDateSearchTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))

                showCalendarDialog()
            }
        }

    }

    private fun getIsMaster(): Boolean {
        if (MAPP.USER_DATA.getCompanyMemberListData().size > 0) {
            for (i in MAPP.USER_DATA.getCompanyMemberListData().indices) {
                if (MAPP.USER_DATA.getCompanyMemberListData()[i].getCompanyUid() == getCompanyDefaultData().getCompanyUid()) {
                    if (MAPP.USER_DATA.getCompanyMemberListData()[i].getCompanyManagerGrant() == COMPANY_GRANT_MASTER) {
                        return true
                    }
                }
            }
        }

        return false
    }

    private fun setLoadingView(isLoading: Boolean) {
        if (isLoading) {
            mLoadingView.visibility = View.VISIBLE
            mLoadingLottieView.speed = 2f
            mLoadingLottieView.playAnimation()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                mLoadingView.visibility = View.GONE
                mLoadingLottieView.pauseAnimation()
            }, 500)
        }
    }

    private fun showCalendarDialog() {
        val customDialogReservationCalendar = CustomDialogReservationCalendar(mActivity)

        customDialogReservationCalendar.setOnCustomEventDialogListener(object : CustomDialogReservationCalendar.OnCustomEventDialogListener {
            override fun onCancel() {
                customDialogReservationCalendar.dismiss()
            }

            override fun onConfirm(startDate: String, endDate: String) {
                customDialogReservationCalendar.dismiss()

                getReservationList(true, startDate, endDate)
            }
        })
        customDialogReservationCalendar.show()
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

    private fun showCustomDialogReservationCancel() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_reservation_cancellation),
            mReservationNameTextView.text.toString() + "\n" + mActivity.resources.getString(R.string.text_message_reservation_cancel),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
                getCompanyReservationStatusChange(TYPE_RESERVATION_CANCEL)
            }
        })
        customDialog.show()
    }

    private fun displayReservationDetail(reservationData: ReservationData) {

        mReservationInformationView.visibility = View.VISIBLE
        mReservationListView.visibility = View.GONE

//        mSwipeRefreshLayout.isEnabled = false

        if (!TextUtils.isEmpty(reservationData.getReservationBeginTime())) {
            val date = reservationData.getReservationBeginTime().substring(0, reservationData.getReservationBeginTime().indexOf(" "))
            val time = reservationData.getReservationBeginTime().substring(reservationData.getReservationBeginTime().indexOf(" ") + 1, reservationData.getReservationBeginTime().length)

            if (!TextUtils.isEmpty(date)) {
                mReservationDateTextView.text = date
            }

            if (!TextUtils.isEmpty(time)) {
                mReservationTimeTextView.text = time
            }
        }

        mReservationPersonCountTextView.text = String.format(mActivity.resources.getString(R.string.text_number_of_people), reservationData.getReservationPersonCount())

        if (!TextUtils.isEmpty(reservationData.getReservationName())) {
            mReservationNameTextView.text = reservationData.getReservationName()
        }

        if (!TextUtils.isEmpty(reservationData.getReservationPhoneNumber())) {
            mReservationPhoneNumberTextView.text = reservationData.getReservationPhoneNumber()
        }

        if (!TextUtils.isEmpty(reservationData.getReservationMemo())) {
            mReservationRequestTextView.text = reservationData.getReservationMemo()
        }

        if (!TextUtils.isEmpty(reservationData.getCreateDate())) {
            var time = reservationData.getCreateDate()
            val index: Int = time.indexOf("+")
            time = time.substring(0, index)

            val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
            val zone = ZoneId.of(Util().getTimeZoneID(mActivity))
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            mReservationRecordeTimeTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
        }

        if (reservationData.getReservationStatus() >= 0) {

            var status = ""
            when (reservationData.getReservationStatus()) {

                TYPE_RESERVATION_APPLICATION -> {
                    status = mActivity.resources.getString(R.string.text_reservation_application_confirm)
                }

                TYPE_RESERVATION_CONFIRMATION -> {
                    status = mActivity.resources.getString(R.string.text_reservation_complete_2)
                }

                TYPE_RESERVATION_COMPLETE -> {
                    status = mActivity.resources.getString(R.string.text_reservation_use_complete)
                }

//                TYPE_RESERVATION_USE_COMPLETE -> {
//                    status = mActivity.resources.getString(R.string.text_reservation_progress_complete)
//                }
//
//                TYPE_RESERVATION_CANCEL -> {
//                    status = mActivity.resources.getString(R.string.text_reservation_cancellation)
//                }
            }

            mReservationRecordeStatusTextView.text = status
            mReservationStatusChangeTextView.text = status
        }

        if (getIsMaster()) {
            if (mReservationData.getReservationStatus() == TYPE_RESERVATION_USE_COMPLETE || mReservationData.getReservationStatus() == TYPE_RESERVATION_CANCEL) {
                mReservationBottomView.visibility = View.GONE
            } else {
                mReservationBottomView.visibility = View.VISIBLE
            }
        } else {
            mReservationBottomView.visibility = View.GONE
        }
    }

    fun isCheckInformationView(): Boolean {
        return mReservationInformationView.visibility == View.VISIBLE
    }

    fun hideInformationView() {
        mReservationInformationView.visibility = View.GONE
        mReservationListView.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()

        mPageCount = 1
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
        if (context is OnCompanyReservationFragmentListener) {
            mActivity = context as Activity

            if (context is OnCompanyReservationFragmentListener) {
                mListener = context;
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnCompanyReservationFragmentListener"
                );
            }
        }
    }

    /*
         * Called when the fragment attaches to the context
         */
    protected fun onAttachToContext(context: Context) {
        if (context is OnCompanyReservationFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnCompanyReservationFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    // =================================================================================
    // API
    // =================================================================================

    /**
     * API. Get Reservation List
     */
    fun getReservationList(isRefresh: Boolean, startDate: String, endDate: String) {

        setLoadingView(true)

        if (isRefresh) {
            mPageCount = 1
        }

        if (!TextUtils.isEmpty(startDate) || !TextUtils.isEmpty(endDate)) {
            mReservationStatusType = 0
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getReservationList(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<ReservationData>> {
                override fun onSuccess(resultData: ArrayList<ReservationData>) {

                    mSwipeRefreshLayout.isRefreshing = false

                    setLoadingView(false)

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mCompanyReservationAdapter.setData(resultData)
                        mEmptyTextView.visibility = View.VISIBLE
                        mReservationRecyclerView.visibility = View.GONE
                        return
                    }

                    mEmptyTextView.visibility = View.GONE
                    mReservationRecyclerView.visibility = View.VISIBLE

                    if (isRefresh) {
                        mReservationListData.clear()
                        mReservationListData = resultData

                        mIsFirst = false
                    } else {
                        if (mReservationListData.size == 0) {
                            mReservationListData = resultData
                        } else {
                            mReservationListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mReservationListData[0].getTotalCount()

                    mIsEndOfScroll = false
                    mCompanyReservationAdapter.setData(mReservationListData)

                    mPageCount++

                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    setLoadingView(false)
                    mSwipeRefreshLayout.isRefreshing = false
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    } else if (errorCode == ErrorRequest.ERROR_CODE_RESERVATION_DATE_SELECT_ERROR) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_date_select_error))
                    }

                    setLoadingView(false)
                    mSwipeRefreshLayout.isRefreshing = false
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mPageCount,
            20,
            MAPP.SELECT_LANGUAGE_CODE,
            0,
            getCompanyDefaultData().getCompanyUid(),
            mReservationStatusType,
            startDate,
            endDate,
            SharedPreferencesUtil().getSaveNationCode(mActivity),
            true
        )
    }

    /**
     * API. Get Store Detail
     */
    private fun getStoreDetail(data: ReservationData) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<StoreDetailData> {
                override fun onSuccess(resultData: StoreDetailData) {

//                    displayReservationDetail(data, resultData)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            data.getCompanyUid().toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    private fun getCompanyReservationDetail(data: ReservationData) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyReservationDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<ReservationData> {
                override fun onSuccess(resultData: ReservationData) {

//                    displayReservationDetail(data, resultData)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            data.getCompanyUid(),
            data.getReservationUid()
        )
    }

    /**
     * API. Get Reservation Memo Save
     */
    private fun getReservationMemoSave(comment: String, data: ReservationData) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getReservationMemoSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

//                    displayReservationDetail(data, resultData)
                    mReservationMemoEditText.setText("")
                    getReservationMemoList(data)

                    hideKeyboard(mActivity)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            comment,
            data.getReservationUid(),
            data.getCompanyUid()
        )
    }

    /**
     * API. Get Reservation Memo Save
     */
    private fun getReservationMemoList(data: ReservationData) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getReservationMemoList(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<ReservationMemoData>> {
                override fun onSuccess(resultData: ArrayList<ReservationMemoData>) {

                    mReservationMemoAdapter.setData(resultData)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            data.getCompanyUid(),
            data.getReservationUid()
        )
    }

    /**
     * API. Get Reservation Memo Save
     */
    private fun getCompanyReservationStatusChange(status: Int) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyReservationStatusChange(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    hideInformationView()
                    getReservationList(true, "", "")

                    showToast(mActivity, mActivity.resources.getString(R.string.text_message_success_reservation_status_change))
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mReservationData.getReservationUid(),
            mReservationData.getCompanyUid(),
            status
        )
    }
}