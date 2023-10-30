package com.nagaja.app.android.Main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.events.calendar.views.EventsCalendar
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Dialog.CustomDialogReservationCalendar
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Adapter.ReservationAdapter
import com.nagaja.app.android.Adapter.ReservationCategoryAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.ReservationData
import com.nagaja.app.android.Data.StoreCategoryData
import com.nagaja.app.android.Data.StoreDetailData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Job.JobFragment
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.StoreDetail.StoreDetailActivity
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_reservation.view.*
import kotlinx.android.synthetic.main.layout_reservation_information.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class ReservationFragment : NagajaFragment() {

    private lateinit var mContainer: View
    private lateinit var mCategoryNextView: View
    private lateinit var mReservationInformationView: View
    private lateinit var mLoadingView: View
    private lateinit var mReservationInformationChatView: View
    private lateinit var mReservationInformationPhoneCallView: View
    private lateinit var mReservationInformationBottomView: View

    private lateinit var mAllTextView: TextView
    private lateinit var mReservationStatusTextView: TextView
    private lateinit var mCancelStatusTextView: TextView
    private lateinit var mDateSearchTextView: TextView
    private lateinit var mEmptyTextView: TextView
    private lateinit var mReservationInformationStoreNameTextView: TextView
    private lateinit var mReservationInformationStoreAddressTextView: TextView
    private lateinit var mReservationInformationStorePhoneNumberTextView: TextView
    private lateinit var mReservationDateTextView: TextView
    private lateinit var mReservationTimeTextView: TextView
    private lateinit var mReservationPersonCountTextView: TextView
    private lateinit var mReservationNameTextView: TextView
    private lateinit var mReservationPhoneNumberTextView: TextView
    private lateinit var mReservationRequestTextView: TextView
    private lateinit var mReservationRecordeTimeTextView: TextView
    private lateinit var mReservationRecordeStatusTextView: TextView


    private lateinit var mReservationInformationStoreImageView: SimpleDraweeView

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mReservationCategoryAdapter: ReservationCategoryAdapter
    private lateinit var mReservationAdapter: ReservationAdapter

    private lateinit var mCategoryRecyclerView: RecyclerView
    private lateinit var mReservationRecyclerView: RecyclerView

    private var mReservationListData: ArrayList<ReservationData> = ArrayList()

    private lateinit var mCalendarView: EventsCalendar

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mRequestQueue: RequestQueue

    private lateinit var mReservationData: ReservationData

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    private var mPageCount = 1
    private var mCategoryUid = 3
    private var mItemTotalCount = 0
    private var mCompanyUid = 0
    private var mReservationStatusType = 0
    private var mSelectCompanyUid = 0

    private var mStorePhoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mReservationCategoryAdapter = ReservationCategoryAdapter(requireActivity())
        mReservationAdapter = ReservationAdapter(requireActivity())

        mRequestQueue = Volley.newRequestQueue(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContainer = inflater.inflate(R.layout.fragment_reservation, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = requireActivity().resources.getString(R.string.text_reservation)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE
        cancelImageView.setOnClickListener {
            mReservationInformationView.visibility = View.GONE
            cancelImageView.visibility = View.GONE
        }

        // Category Recycler View
        mCategoryRecyclerView = mContainer.fragment_reservation_category_recycler_view
        mCategoryRecyclerView.setHasFixedSize(true)
        mCategoryRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        mReservationCategoryAdapter.setOnItemClickListener(object : ReservationCategoryAdapter.OnItemClickListener {
            override fun onClick(data: StoreCategoryData, position: Int) {
                mReservationCategoryAdapter.setSelectCategory(position)
                mCategoryUid = data.getCategoryUid()
//                getReservationList(true, "", "")

                setTabView(RESERVATION_TYPE_ALL, false)
            }
        })
        mCategoryRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mCategoryRecyclerView.adapter = mReservationCategoryAdapter

        // Category Next View
        mCategoryNextView = mContainer.fragment_reservation_category_next_view

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_reservation_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mItemTotalCount = 0

                getStoreCategoryData(true)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(requireActivity(), requireActivity().resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // All View
        mAllTextView = mContainer.fragment_reservation_all_text_view
        mAllTextView.setOnClickListener {
            setTabView(RESERVATION_TYPE_ALL, false)
        }

        // Reservation Status Text View
        mReservationStatusTextView = mContainer.fragment_reservation_status_text_view
        mReservationStatusTextView.setOnClickListener {
            setTabView(RESERVATION_TYPE_RESERVATION_STATUS, false)
        }

        // Cancel Status Text View
        mCancelStatusTextView = mContainer.fragment_reservation_cancel_status_text_view
        mCancelStatusTextView.setOnClickListener {
            setTabView(RESERVATION_TYPE_CANCEL, false)
        }

        // Date Search Text View
        mDateSearchTextView = mContainer.fragment_reservation_date_search_text_view
        mDateSearchTextView.setOnClickListener {
            setTabView(RESERVATION_TYPE_DATE_SEARCH, false)
        }


        // Reservation Recycler View
        mReservationRecyclerView = mContainer.fragment_reservation_status_recycler_view
        mReservationRecyclerView.setHasFixedSize(true)
        mReservationRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

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

        mReservationAdapter.setOnReservationClickListener(object : ReservationAdapter.OnReservationClickListener {
            override fun onClick(data: ReservationData, isDelete: Boolean) {
                if (isDelete) {
                    showReservationDeleteDialog(data, false)
                } else {
                    mReservationData = data
                    getStoreDetail(data)
                }
            }
        })
        mReservationRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mReservationRecyclerView.adapter = mReservationAdapter

        // Empty Text View
        mEmptyTextView = mContainer.fragment_reservation_empty_text_view

        // Reservation Information View
        mReservationInformationView = mContainer.fragment_reservation_information_view

        // Calendar View
//        mCalendarParentView = mContainer.fragment_reservation_calendar_parent_view
//        val calendarView = layoutInflater.inflate(R.layout.layout_calendar_view, mCalendarParentView as LinearLayout?, true)
//        mCalendarParentView.visibility = View.GONE
//        mCalendarView = calendarView.findViewById(R.id.layout_calendar)


        // Reservation Information Cancel Image View
        val reservationInformationCancelImageView = mContainer.layout_reservation_information_cancel_image_view
        reservationInformationCancelImageView.setOnClickListener {
            if (mReservationInformationView.visibility == View.VISIBLE) {
                mReservationInformationView.visibility = View.GONE
            }
        }

        // Reservation Information Store Image View
        mReservationInformationStoreImageView = mContainer.layout_reservation_information_store_image_view
        mReservationInformationStoreImageView.setOnClickListener {
            val intent = Intent(requireActivity(), StoreDetailActivity::class.java)
            intent.putExtra(NagajaActivity.INTENT_DATA_STORE_UID_DATA, mSelectCompanyUid)
            startActivity(intent)
        }

        // Reservation Information Store Name Text View
        mReservationInformationStoreNameTextView = mContainer.layout_reservation_information_store_name_text_view
        mReservationInformationStoreNameTextView.setOnClickListener {
            val intent = Intent(requireActivity(), StoreDetailActivity::class.java)
            intent.putExtra(NagajaActivity.INTENT_DATA_STORE_UID_DATA, mSelectCompanyUid)
            startActivity(intent)
        }

        // Reservation Information Store Address Text View
        mReservationInformationStoreAddressTextView = mContainer.layout_reservation_information_store_address_text_view

        // Reservation Information Store Phone Number Text View
        mReservationInformationStorePhoneNumberTextView = mContainer.layout_reservation_information_store_phone_number_text_view

        // Reservation Information Chat View
        mReservationInformationChatView = mContainer.layout_reservation_information_chat_view
        mReservationInformationChatView.setOnClickListener {
            // TODO: Implement Chat View Screen
            showToast(requireActivity(), "// TODO: Implement Chat View Screen")
        }

        // Reservation Information Phone Call View
        mReservationInformationPhoneCallView = mContainer.layout_reservation_information_phone_call_view
        mReservationInformationPhoneCallView.setOnClickListener {
            if (!TextUtils.isEmpty(mStorePhoneNumber)) {
                phoneCall()
            } else {
                showToast(requireActivity(), requireActivity().resources.getString(R.string.text_error_empty_phone_number))
            }
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

        // Reservation Complete Bottom View
        mReservationInformationBottomView = mContainer.layout_reservation_information_reservation_bottom_view

        // Reservation Completion Text View
        val reservationCompletionTextView = mContainer.layout_reservation_information_reservation_status_change_text_view
        reservationCompletionTextView.setOnClickListener {
            mReservationInformationView.visibility = View.GONE
        }

        // Reservation Cancellation Text View
        val reservationCancellationTextView = mContainer.layout_reservation_information_reservation_cancellation_text_view
        reservationCancellationTextView.setOnClickListener {
            showReservationDeleteDialog(mReservationData, true)
        }



        // Loading View
        mLoadingView = mContainer.fragment_reservation_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_reservation_loading_lottie_view


        setTabView(RESERVATION_TYPE_ALL, true)
//        setCalendar()

    }

    private fun setTabView(type: Int, isFirst: Boolean) {

        when (type) {
            RESERVATION_TYPE_ALL -> {
                mAllTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))
                mAllTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))

                mReservationStatusTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))
                mReservationStatusTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))

                mCancelStatusTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))
                mCancelStatusTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))

                mDateSearchTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))
                mDateSearchTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))

//                mCalendarParentView.visibility = View.GONE

                mReservationStatusType = 0
                if (isFirst) {
                    getStoreCategoryData(true)
                } else {
                    getReservationList(true, "", "")
                }
            }

            RESERVATION_TYPE_RESERVATION_STATUS -> {
                mAllTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))
                mAllTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))

                mReservationStatusTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))
                mReservationStatusTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))

                mCancelStatusTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))
                mCancelStatusTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))

                mDateSearchTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))
                mDateSearchTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))

//                mCalendarParentView.visibility = View.GONE

                mReservationStatusType = 1
                getReservationList(true, "", "")
            }

            RESERVATION_TYPE_CANCEL -> {
                mAllTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))
                mAllTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))

                mReservationStatusTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))
                mReservationStatusTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))

                mCancelStatusTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))
                mCancelStatusTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))

                mDateSearchTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))
                mDateSearchTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))

//                mCalendarParentView.visibility = View.GONE

                mReservationStatusType = 2
                getReservationList(true, "", "")
            }

            RESERVATION_TYPE_DATE_SEARCH -> {
                mAllTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))
                mAllTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))

                mReservationStatusTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))
                mReservationStatusTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))

                mCancelStatusTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))
                mCancelStatusTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))

                mDateSearchTextView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_000000))
                mDateSearchTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.text_color_ffffff))

//                mCalendarParentView.visibility = View.VISIBLE

                showCalendarDialog()
            }
        }

    }

    private fun showCalendarDialog() {
        val customDialogReservationCalendar = CustomDialogReservationCalendar(requireActivity())

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

    private fun showReservationDeleteDialog(data: ReservationData, isDetail: Boolean) {

        val companyName = if (!TextUtils.isEmpty(data.getCompanyName())) data.getCompanyName() else data.getCompanyNameEnglish()

        val customDialog = CustomDialog(
            requireActivity(),
            DIALOG_TYPE_NORMAL,
            requireActivity().resources.getString(R.string.text_reservation_cancellation),
            companyName + "\n" + requireActivity().resources.getString(R.string.text_message_reservation_cancel),
            requireActivity().resources.getString(R.string.text_close),
            requireActivity().resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                getReservationChangeStatus(data.getReservationUid(), isDetail)
                customDialog.dismiss()
            }
        })
        customDialog.show()
    }

    private fun setReservationList() {
        if (mReservationListData.isEmpty()) {
            return
        }

        val reservationCancelListData = ArrayList<ReservationData>()
        for (i in mReservationListData.indices) {
            if (mReservationListData[i].getReservationStatus() == TYPE_RESERVATION_CANCEL) {

                reservationCancelListData.add(mReservationListData[i])
            }
        }

        for (i in reservationCancelListData.indices) {
            NagajaLog().e("wooks, !!!!!!! = ${reservationCancelListData[i].getCompanyName()}")
        }
    }

    private fun phoneCall() {
        var number = mStorePhoneNumber.replace(" ", "")
        number = number.replace("-", "")
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(intent)
    }

    private fun displayReservationDetail(reservationData: ReservationData, storeDetailData: StoreDetailData) {

        mReservationInformationView.visibility = View.VISIBLE
        mSelectCompanyUid = storeDetailData.getCompanyUid()

        if (!TextUtils.isEmpty(storeDetailData.getMainImageUrl())) {
            mReservationInformationStoreImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + storeDetailData.getMainImageUrl()))
        }

        if (!TextUtils.isEmpty(storeDetailData.getCompanyName())) {
            mReservationInformationStoreNameTextView.text = storeDetailData.getCompanyName()
        } else {
            mReservationInformationStoreNameTextView.text = storeDetailData.getCompanyNameEnglish()
        }

        if (!TextUtils.isEmpty(storeDetailData.getAddress())) {
            mReservationInformationStoreAddressTextView.text = storeDetailData.getAddress()
        }

        if (!TextUtils.isEmpty(storeDetailData.getPhoneNumber())) {
            mStorePhoneNumber = storeDetailData.getPhoneNumber()
            mReservationInformationStorePhoneNumberTextView.text = storeDetailData.getPhoneNumber()
        }

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

        mReservationPersonCountTextView.text = String.format(requireActivity().resources.getString(R.string.text_number_of_people), reservationData.getReservationPersonCount())


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
            val zone = ZoneId.of(Util().getTimeZoneID(requireActivity()))
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            mReservationRecordeTimeTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
        }

        if (reservationData.getReservationStatus() >= 0) {

            var status = ""
            when (reservationData.getReservationStatus()) {

                TYPE_RESERVATION_APPLICATION -> {
                    status = requireActivity().resources.getString(R.string.text_application_reservation)
                    mReservationInformationBottomView.visibility = View.VISIBLE
                }

                TYPE_RESERVATION_CONFIRMATION -> {
                    status = requireActivity().resources.getString(R.string.text_reservation_confirmation)
                    mReservationInformationBottomView.visibility = View.VISIBLE
                }

                TYPE_RESERVATION_COMPLETE -> {
                    status = requireActivity().resources.getString(R.string.text_reservation_complete_2)
                    mReservationInformationBottomView.visibility = View.GONE
                }

                TYPE_RESERVATION_USE_COMPLETE -> {
                    status = requireActivity().resources.getString(R.string.text_reservation_use_complete)
                    mReservationInformationBottomView.visibility = View.GONE
                }

                TYPE_RESERVATION_CANCEL -> {
                    status = requireActivity().resources.getString(R.string.text_reservation_cancellation)
                    mReservationInformationBottomView.visibility = View.GONE
                }
            }

            mReservationRecordeStatusTextView.text = status
        }

    }

    override fun onPause() {
        super.onPause()

        mPageCount = 1
    }

    // ==========================================================================================
    // API
    // ==========================================================================================

    /**
     * API. Get Store Category Data
     */
    fun getStoreCategoryData(isRefresh: Boolean) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreCategoryData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<StoreCategoryData>> {
                override fun onSuccess(resultData: ArrayList<StoreCategoryData>) {

                    mIsFirst = false

                    if (isRefresh) {
                        mPageCount = 1
                    }

                    if (resultData.isEmpty()) {
                        return
                    }

                    val storeCategoryData = StoreCategoryData()
                    storeCategoryData.setCategoryUid(3)
                    storeCategoryData.setCategoryDepth(2)
                    storeCategoryData.setCategorySort(1)
                    storeCategoryData.setCategoryName(requireActivity().resources.getString(R.string.text_all))
                    storeCategoryData.setCategoryNameEnglish("All")
                    storeCategoryData.setCategoryUri("/free")
                    storeCategoryData.setIsUseYn(true)
                    storeCategoryData.setCreateDate("")
                    storeCategoryData.setParentCategoryUid(3)
                    storeCategoryData.setIsSelect(true)

                    resultData.add(0, storeCategoryData)

                    mReservationCategoryAdapter.setData(resultData)

                    mCategoryNextView.visibility = if (resultData.size > 4) View.VISIBLE else View.GONE

                    getReservationList(isRefresh, "", "")
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    mSwipeRefreshLayout.isRefreshing = false
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                    mSwipeRefreshLayout.isRefreshing = false
                }
            },
            "3",
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

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
                        mReservationAdapter.setData(resultData)
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
                    mReservationAdapter.setData(mReservationListData)

                    mPageCount++
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    setLoadingView(false)
                    mSwipeRefreshLayout.isRefreshing = false
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    } else if (errorCode == ErrorRequest.ERROR_CODE_RESERVATION_DATE_SELECT_ERROR) {
                        showToast(requireActivity(), requireActivity().resources.getString(R.string.text_date_select_error))
                    }

                    setLoadingView(false)
                    mSwipeRefreshLayout.isRefreshing = false
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mPageCount,
            20,
            MAPP.SELECT_LANGUAGE_CODE,
            mCategoryUid,
            mCompanyUid,
            mReservationStatusType,
            startDate,
            endDate,
            SharedPreferencesUtil().getSaveNationCode(requireActivity()),
            false
        )
    }

    /**
     * API. Get Reservation Change Status
     */
    fun getReservationChangeStatus(reservationUid: Int, isDetail: Boolean) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getReservationChangeStatus(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    if (isDetail) {
                        mReservationInformationView.visibility = View.GONE
                    }

                    getReservationList(true, "", "")
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
            reservationUid,
            3
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

                    displayReservationDetail(data, resultData)
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
            data.getCompanyUid().toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }
}