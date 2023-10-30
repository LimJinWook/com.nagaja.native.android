package com.nagaja.app.android.StoreDetail

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Utils.NetworkProvider.Companion.IMAGE_THUMB
import com.nagaja.app.android.Adapter.StoreMainImageAdapter
import com.nagaja.app.android.Adapter.StoreMenuAdapter
import com.nagaja.app.android.Adapter.StoreReviewAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.GpsTracker
import com.nagaja.app.android.Store.StoreFragment
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R
import com.nagaja.app.android.StoreDetail.StoreDetailActivity.Companion.mChangeStatus
import com.nagaja.app.android.StoreDetail.StoreDetailActivity.Companion.mIsStatus

import kotlinx.android.synthetic.main.fragment_store_detail.view.*
import kotlinx.android.synthetic.main.fragment_store_list.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_store_menu_view.view.*
import kotlinx.android.synthetic.main.layout_store_review_view.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class StoreDetailFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mContainer: View
    private lateinit var mGoodView: View
    private lateinit var mFindWay: View
    private lateinit var mNavigationView: View
    private lateinit var mCopyAddressView: View
    private lateinit var mCallGrabView: View
    private lateinit var mCallGrabTestView: View
    private lateinit var mPhoneCallView: View
    private lateinit var mMenuView: View
    private lateinit var mReViewView: View
    private lateinit var mMenuUnderLineView: View
    private lateinit var mReViewUnderLineView: View
    private lateinit var mMenuLayout: View
    private lateinit var mReViewLayout: View
    private lateinit var mBottomView: View
    private lateinit var mWriteReviewView: View
    private lateinit var mReservationView: View
    private lateinit var mDescriptionView: View
    private lateinit var mReviewEmptyView: View


    private lateinit var mTitleTextView: TextView
    private lateinit var mRecommendCountTextView: TextView
    private lateinit var mReviewCountTextView: TextView
    private lateinit var mStoreNameTextView: TextView
    private lateinit var mDeliveryAvailabilityTextView: TextView
    private lateinit var mPickUpAvailabilityTextView: TextView
    private lateinit var mReservationAvailabilityTextView: TextView
    private lateinit var mReservationPersonLimitTextView: TextView
    private lateinit var mBusinessTimeTextView: TextView
    private lateinit var mBreakTimeTextView: TextView
    private lateinit var mPaymentMethodTextView: TextView
    private lateinit var mAddressTextView: TextView
    private lateinit var mLocationEmptyTextView: TextView
    private lateinit var mMenuTextView: TextView
    private lateinit var mReViewTextView: TextView
    private lateinit var mReviewLayoutCountTextView: TextView
    private lateinit var mDeadLineTextView: TextView
    private lateinit var mReservationTextView: TextView
    private lateinit var mChatTextView: TextView
    private lateinit var mDescriptionTextView: TextView


    private lateinit var mGoodImageView: ImageView
    private lateinit var mCertificationIconImageView: ImageView
    private lateinit var mDescriptionExpandIconImageView: ImageView

    private lateinit var mMainImageRecyclerView: RecyclerView
    private lateinit var mStoreMenuRecyclerView: RecyclerView
    private lateinit var mStoreReviewRecyclerView: RecyclerView

    private lateinit var mStoreMainImageAdapter: StoreMainImageAdapter
    private lateinit var mStoreMenuAdapter: StoreMenuAdapter
    private lateinit var mStoreReviewAdapter: StoreReviewAdapter

    private lateinit var mStoreDetailData: StoreDetailData

    private lateinit var mMapFragment: SupportMapFragment
    private var mMap: GoogleMap? = null
    private val mLatLng: LatLng? = null
    private var mMapMarker: Marker? = null

    private lateinit var mListener: OnStoreDetailFragmentListener

    private val mMainImageList = ArrayList<String>()
    private val mOriginImageList = ArrayList<String>()
    private var mReviewListData = ArrayList<StoreDetailReviewData>()

    private var mClipboardManager: ClipboardManager? = null
    private var mClipData: ClipData? = null

    private var mIsEndOfScroll = false
    private var mIsTabTypeReview = false
    private var mIsDescriptionExpand = false

    private var mTotalReviewCount = 0
    private var mPageCount = 1

    private lateinit var mRequestQueue: RequestQueue

    interface OnStoreDetailFragmentListener {
        fun onFinish()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
        fun onMapScreen(location: String, address: String, storeName: String)
        fun onWriteReviewScreen(writeReviewData: WriteReviewData)
        fun onWriteReviewScreen(data: StoreDetailReviewData)
        fun onReportScreen(reportType: Int, reviewUid: Int, storeName: String)
        fun onStoreReservationScreen(data: StoreDetailData)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
        fun onChatViewScreen(chatClass: String, chatData: String)
    }

    companion object {

        const val ARGS_COMPANY_UID                  = "args_company_uid"

        const val TAB_TYPE_MENU                     = 0x01
        const val TAB_TYPE_REVIEW                   = 0x02

        fun newInstance(companyUid: Int): StoreDetailFragment {
            val args = Bundle()
            val fragment = StoreDetailFragment()
            args.putInt(ARGS_COMPANY_UID, companyUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getCompanyUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_COMPANY_UID) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mClipboardManager = mActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        mStoreMainImageAdapter = StoreMainImageAdapter(mActivity)
        mStoreMenuAdapter = StoreMenuAdapter(mActivity)
        mStoreReviewAdapter = StoreReviewAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_store_detail, container, false)

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
        headerShareImageView.setOnSingleClickListener {
            setShareUrl(mActivity, mStoreDetailData.getCategoryUid(), getCompanyUid())
        }

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
        mTitleTextView = mContainer.layout_title_text_view

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Scroll View
        mScrollView = mContainer.fragment_store_detail_scroll_view
        mScrollView.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (!v.canScrollVertically(1)) {
                if (!mIsEndOfScroll) {
                    if (mReviewListData.size >= mTotalReviewCount) {
                        return@OnScrollChangeListener
                    }

                    if (!mIsTabTypeReview) {
                        return@OnScrollChangeListener
                    }

                    mIsEndOfScroll = true
                    getStoreReviewData(false)
                }
            }
        })

        // Main Image Recycler View
        mMainImageRecyclerView = mContainer.fragment_store_detail_main_image_recycler_view
        mMainImageRecyclerView.setHasFixedSize(true)
        mMainImageRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        mStoreMainImageAdapter.setOnImageClickListener(object : StoreMainImageAdapter.OnImageClickListener {
            override fun onClick(position: Int) {
                mListener.onFullScreenImage(mOriginImageList, position)
            }
        })
        mMainImageRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mMainImageRecyclerView.adapter = mStoreMainImageAdapter

        // Recommend Count Text View
        mRecommendCountTextView = mContainer.fragment_store_detail_recommend_count_text_view

        // Good View
        mGoodView = mContainer.fragment_store_detail_good_view

        // Good Image View
        mGoodImageView = mContainer.fragment_store_detail_good_image_view

        // Review Count Text View
        mReviewCountTextView = mContainer.fragment_store_detail_review_count_text_view

        // String Name Text View
        mStoreNameTextView = mContainer.fragment_store_detail_store_name_text_view

        // Certification Icon Image View
        mCertificationIconImageView = mContainer.fragment_store_detail_certification_image_view

        // Delivery Availability Text View
        mDeliveryAvailabilityTextView = mContainer.fragment_store_detail_delivery_availability_text_view

        // Pick Up And Drop Availability Text View
        mPickUpAvailabilityTextView = mContainer.fragment_store_detail_pick_up_availability_text_view

        // Reservation Availability Text View
        mReservationAvailabilityTextView = mContainer.fragment_store_detail_reservation_availability_text_view

        // Reservation Person Limit Text View
        mReservationPersonLimitTextView = mContainer.fragment_store_detail_reservation_person_limit_text_view

        // Business Time Text View
        mBusinessTimeTextView = mContainer.fragment_store_detail_reservation_business_time_text_view

        // Break Time Text View
        mBreakTimeTextView = mContainer.fragment_store_detail_reservation_break_time_text_view

        // Payment Method Text View
        mPaymentMethodTextView = mContainer.fragment_store_detail_payment_method_text_view

        // Address Text View
        mAddressTextView = mContainer.fragment_store_detail_address_text_view

        // Google Map View
        mMapFragment = childFragmentManager.findFragmentById(R.id.fragment_store_detail_map_view) as SupportMapFragment
        mMapFragment.getMapAsync {
            val seoul = LatLng(37.494870, 126.960763)
            val location = CameraUpdateFactory.newLatLngZoom(seoul, 17f)
            it.moveCamera(location)
            it.animateCamera(location)
        }

        // Location Empty Text View
        mLocationEmptyTextView = mContainer.fragment_store_detail_location_empty_text_view

        // Find Way View
        mFindWay = mContainer.fragment_store_detail_find_way_view

        // Navigation View
        mNavigationView = mContainer.fragment_store_detail_navigation_view

        // Copy Address View
        mCopyAddressView = mContainer.fragment_store_detail_copy_address_view

        // Call Grab View
        mCallGrabView = mContainer.fragment_store_detail_call_grab_view

        // Call Grab View
        mCallGrabTestView = mContainer.fragment_store_detail_test_call_grab_view

        // Phone Call View
        mPhoneCallView = mContainer.fragment_store_detail_phone_call_view

        // Store Menu Recycler View
        mStoreMenuRecyclerView = mContainer.layout_store_menu_view_menu_recycler_view
        mStoreMenuRecyclerView.setHasFixedSize(true)
        mStoreMenuRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mStoreMenuAdapter.setOnItemClickListener(object : StoreMenuAdapter.OnItemClickListener {
            override fun onClick(data: StoreDetailMenuData) {
                TODO("Not yet implemented")
            }

            override fun onImageClick(imageList: ArrayList<String>, position: Int) {
                mListener.onFullScreenImage(imageList, position)
            }

        })
        mStoreMenuRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mStoreMenuRecyclerView.adapter = mStoreMenuAdapter

        // Menu View
        mMenuView = mContainer.fragment_store_detail_menu_view
        mMenuView.setOnClickListener {
            setTab(TAB_TYPE_MENU)
        }

        // Menu Text View
        mMenuTextView = mContainer.fragment_store_detail_menu_text_view

        // Menu Under Line View
        mMenuUnderLineView = mContainer.fragment_store_detail_menu_under_line_view

        // ReView View
        mReViewView = mContainer.fragment_store_detail_review_view
        mReViewView.setOnClickListener {
            setTab(TAB_TYPE_REVIEW)
        }

        // ReView Text View
        mReViewTextView = mContainer.fragment_store_detail_review_text_view

        // ReView Under Line View
        mReViewUnderLineView = mContainer.fragment_store_detail_review_under_line_view

        // Menu Layout
        mMenuLayout = mContainer.layout_store_detail_menu

        // ReView Layout
        mReViewLayout = mContainer.layout_store_detail_review

        // Store Review Recycler View
        mStoreReviewRecyclerView = mContainer.layout_store_review_recycler_view
        mStoreReviewRecyclerView.setHasFixedSize(true)
        mStoreReviewRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)

        mStoreReviewAdapter.setOnItemClickListener(object : StoreReviewAdapter.OnItemClickListener {
            override fun onImageClick(imageList: ArrayList<String>, position: Int) {
                mListener.onFullScreenImage(imageList, position)
            }

            override fun onClick(data: StoreDetailReviewData) {
//                TODO("Not yet implemented")
            }

            override fun onModify(data: StoreDetailReviewData, position: Int) {
                data.setCompanyName(mStoreNameTextView.text.toString())
                mListener.onWriteReviewScreen(data)
            }

            override fun onDelete(data: StoreDetailReviewData, position: Int) {
                showReviewDeletePopup(data, position)
            }

            override fun onReport(reportUid: Int) {
                mListener.onReportScreen(REPORT_TYPE_REVIEW, reportUid, "")
            }

            override fun onRecommend(reviewUid: Int, isLike: Boolean, position: Int) {
                getReviewRecommendSave(reviewUid, isLike, position)
            }

        })
        mStoreReviewRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mStoreReviewRecyclerView.adapter = mStoreReviewAdapter

        // Review Layout Count Text View
        mReviewLayoutCountTextView = mContainer.layout_store_review_count_text_view

        // Review Empty View
        mReviewEmptyView = mContainer.layout_store_review_empty_view

        // Bottom View
        mBottomView = mContainer.fragment_store_detail_bottom_view


        // Write Review View
        mWriteReviewView = mContainer.fragment_store_detail_write_review_view
        mWriteReviewView.setOnSingleClickListener {

            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                val writeReviewData = WriteReviewData()
                writeReviewData.setCompanyUid(getCompanyUid())
                writeReviewData.setStoreName(mStoreNameTextView.text.toString())
                writeReviewData.setIsModify(false)

                mListener.onWriteReviewScreen(writeReviewData)
            }
        }

        // Report Image View
        val reportImageView = mContainer.layout_store_menu_view_menu_report_image_view
        reportImageView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                mListener.onReportScreen(REPORT_TYPE_STORE, getCompanyUid(), mTitleTextView.text.toString())
            }
        }

        // Reservation View
        mReservationView = mContainer.fragment_store_detail_reservation_view
        mReservationView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                mListener.onStoreReservationScreen(mStoreDetailData)
            }
        }

        // Dead Line Text View
        mDeadLineTextView = mContainer.fragment_store_detail_dead_line_text_view

        // Reservation Text View
        mReservationTextView = mContainer.fragment_store_detail_reservation_text_view

        // Chat Text View
        mChatTextView = mContainer.fragment_store_detail_chat_text_view
        mChatTextView.setOnSingleClickListener {

            if (mStoreDetailData.getContractStatus() == 1) {
                if (MAPP.IS_NON_MEMBER_SERVICE) {
                    showLoginGuideCustomDialog()
                } else {
                    if (mStoreDetailData.getCompanyUid() > 0) {
                        val chatData = "[{${MAPP.USER_DATA.getMemberUid()}, 0}, {${mStoreDetailData.getMemberUid()}, ${mStoreDetailData.getCompanyUid()}}]"
                        if (!TextUtils.isEmpty(chatData)) {
                            mListener.onChatViewScreen(CHAT_CLASS_COMPANY, chatData)
                        }
                    }
                }
            } else {
                showDisableChatCustomPopup()
            }

        }

        // Description View
        mDescriptionView = mContainer.fragment_store_detail_description_view

        // Description Expand Icon Image View
        mDescriptionExpandIconImageView = mContainer.fragment_store_detail_description_expand_image_view
        mDescriptionExpandIconImageView.setOnClickListener {
            mIsDescriptionExpand = !mIsDescriptionExpand

            if (mIsDescriptionExpand) {
                mDescriptionExpandIconImageView.setImageResource(R.drawable.ic_baseline_expand_less_24)
                mDescriptionTextView.visibility = View.VISIBLE
            } else {
                mDescriptionExpandIconImageView.setImageResource(R.drawable.ic_baseline_expand_more_24)
                mDescriptionTextView.visibility = View.GONE
            }
        }

        // Description Text View
        mDescriptionTextView = mContainer.fragment_store_detail_description_text_view


        setTab(TAB_TYPE_MENU)
        getStoreDetail()
    }

    private fun setTab(type: Int) {
        when (type) {
            TAB_TYPE_MENU -> {
                mMenuTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mMenuUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mReViewTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mReViewUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mMenuLayout.visibility = View.VISIBLE
                mReViewLayout.visibility = View.GONE
                mBottomView.visibility = View.VISIBLE
                mWriteReviewView.visibility = View.GONE

                mIsTabTypeReview = false
            }

            TAB_TYPE_REVIEW -> {
                mMenuTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mMenuUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mReViewTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mReViewUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mMenuLayout.visibility = View.GONE
                mReViewLayout.visibility = View.VISIBLE
                mBottomView.visibility = View.GONE
                mWriteReviewView.visibility = View.VISIBLE

                mIsTabTypeReview = true
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayStoreData(data: StoreDetailData) {

        mStoreDetailData = data

        // Title Text View
        var storeName = ""
        storeName = if (!TextUtils.isEmpty(data.getCompanyName())) {
            data.getCompanyName()
        } else {
            data.getCompanyNameEnglish()
        }
        mTitleTextView.text = storeName
        mStoreNameTextView.text = storeName
        mStoreNameTextView.isSelected = true


        // Recommend Count Text View
        mRecommendCountTextView.text = String.format(mActivity.resources.getString(R.string.text_recommend_count), Util().convertToComma(data.getRecommendCount()))

        // Good View
        mGoodView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
//                getStoreRegularSave()
                getStoreRecommendSave()
            }
        }

        // Good Image View
        mGoodImageView.setImageResource( if (data.getIsRecommendUseYn()) R.drawable.icon_store_regula else R.drawable.icon_store_regula_2)
        mIsStatus = data.getIsRecommendUseYn()

        // Review Count Text View
        mReviewCountTextView.text = String.format(mActivity.resources.getString(R.string.text_review_count), Util().convertToComma(data.getReviewCount()))

        // Review Layout Count Text View
        mReviewLayoutCountTextView.text = String.format(mActivity.resources.getString(R.string.text_review_count_desc), Util().convertToComma(data.getReviewCount()))

        // Certification Icon Image View
        mCertificationIconImageView.visibility = if (data.getIsCertificationYn()) View.VISIBLE else View.GONE

        // Delivery Availability Text View
        mDeliveryAvailabilityTextView.text = if (data.getIsDeliveryUseYn()) mActivity.resources.getString(R.string.text_possibility) else mActivity.resources.getString(R.string.text_company_inquiry)

        // Pick Up And Drop Availability Text View
        mPickUpAvailabilityTextView.text = if (data.getIsPickUpUseYn()) mActivity.resources.getString(R.string.text_possibility) else mActivity.resources.getString(R.string.text_company_inquiry)

        // Reservation Availability Text View
        var reservation = ""
        var deliveryTime = ""
        if (data.getIsReservationUseYn()) {
            reservation = mActivity.resources.getString(R.string.text_possibility)
            if (!TextUtils.isEmpty(data.getDeliveryBeginTime()) && !TextUtils.isEmpty(data.getDeliveryEndTime())) {
                var beginTime = data.getDeliveryBeginTime()
                var endTime = data.getDeliveryEndTime()

                if (beginTime.length > 5) {
                    beginTime = beginTime.substring(0, 5)
                }

                if (endTime.length > 5) {
                    endTime = endTime.substring(0, 5)
                }

                deliveryTime = "$beginTime ~ $endTime"
            } else {
                deliveryTime = mActivity.resources.getString(R.string.text_unregistered)
            }
        } else {
            reservation = mActivity.resources.getString(R.string.text_company_inquiry)
            deliveryTime = mActivity.resources.getString(R.string.text_unregistered)
        }
//        mReservationAvailabilityTextView.text = "$reservation / $deliveryTime"
        mReservationAvailabilityTextView.text = reservation

        // Reservation Person Limit Text View
        mReservationPersonLimitTextView.text = String.format(mActivity.resources.getString(R.string.text_reservation_person_limit_count), Util().convertToComma(data.getReservationPersonLimit()))

        // Business Time Text View
        if (!TextUtils.isEmpty(data.getServiceBeginTime()) && !TextUtils.isEmpty(data.getServiceEndTime())) {
            mBusinessTimeTextView.text = data.getServiceBeginTime() + " ~ " + data.getServiceEndTime()
        } else {
            mBusinessTimeTextView.text = mActivity.resources.getString(R.string.text_company_inquiry)
        }

        // Break Time Text View
        var breakTime = ""
        if (!TextUtils.isEmpty(data.getFirstBreakBeginTime()) && !TextUtils.isEmpty(data.getFirstBreakEndTime())) {
            breakTime = data.getFirstBreakBeginTime()  + " ~ " + data.getFirstBreakEndTime()
        }

        if (!TextUtils.isEmpty(data.getSecondBreakBeginTime()) && !TextUtils.isEmpty(data.getSecondBreakEndTime())) {
            if (!TextUtils.isEmpty(breakTime)) {
                breakTime += "\n" + data.getSecondBreakBeginTime() + " ~ " + data.getSecondBreakEndTime()
            } else {
                breakTime = data.getSecondBreakBeginTime() + " ~ " + data.getSecondBreakEndTime()
            }
        }

        if (TextUtils.isEmpty(breakTime)) {
            breakTime = mActivity.resources.getString(R.string.text_company_inquiry)
        }
        mBreakTimeTextView.text = breakTime


        // Payment Method Text View
        mPaymentMethodTextView.text = if (!TextUtils.isEmpty(data.getPayment())) data.getPayment() else mActivity.resources.getString(R.string.text_unregistered)

        // Description View
        if (!TextUtils.isEmpty(data.getDesc())) {
            mDescriptionView.visibility = View.VISIBLE
            mDescriptionTextView.text = data.getDesc()
        } else {
            mDescriptionView.visibility = View.GONE
        }

        // Address Text View
        mAddressTextView.text = data.getAddress() + " " + data.getAddressDetail()

        // Find Way View
        mFindWay.setOnClickListener {
            if (TextUtils.isEmpty(data.getLatitude().toString()) || TextUtils.isEmpty(data.getLongitude().toString())) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_empty_location))
            } else {
                findWay(data.getLatitude().toString(), data.getLongitude().toString())
            }
        }

        // Navigation View
        mNavigationView.setOnClickListener {
            if (TextUtils.isEmpty(data.getLatitude().toString()) || TextUtils.isEmpty(data.getLongitude().toString())) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_empty_location))
            } else {
                navigation(data.getLatitude().toString(), data.getLongitude().toString())
            }
        }

        // Copy Address View
        mCopyAddressView.setOnClickListener {
            copyAddress(data.getAddress() + " " + data.getAddressDetail())
        }

        // Call Grab View
        mCallGrabView.setOnClickListener {
            if (TextUtils.isEmpty(data.getLatitude().toString()) || TextUtils.isEmpty(data.getLongitude().toString())) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_empty_location_information))
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(getUserLocation())) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_empty_location))
                return@setOnClickListener
            }

            val currentLatitude = getUserLocation().substring(0, getUserLocation().indexOf(","))
            val currentLongitude = getUserLocation().substring(getUserLocation().indexOf(",") + 1, getUserLocation().length)

            val dropOffAddress = getAddress(data.getLatitude(), data.getLongitude())
            val pickUpAddress = getAddress(currentLatitude.toDouble(), currentLongitude.toDouble())

            try {
                val grabOpenUrl = Uri.parse("https://grab.onelink.me/2695613898?af_dp=grab://open?dropOffAddress=$storeName" +
                        "$dropOffAddress&dropOffLatitude=${data.getLatitude()}&dropOffLongitude=${data.getLongitude()}" +
                        "&pickUpAddress=$pickUpAddress&pickUpLatitude=$currentLatitude&pickUpLongitude=$currentLongitude&screenType=BOOKING&sourceID=&taxiTypeId=227&c=&pid=")
                val intent = Intent(Intent.ACTION_VIEW, grabOpenUrl)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                NagajaLog().e("You don't have any browser to open web page")
            }
        }

        mCallGrabTestView.setOnClickListener {
            if (TextUtils.isEmpty(data.getLatitude().toString()) || TextUtils.isEmpty(data.getLongitude().toString()) || TextUtils.isEmpty(SharedPreferencesUtil().getSaveLocation(mActivity))) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_empty_location_information))
                return@setOnClickListener
            }

            // Test
            val currentLatitude = "10.31568605"
            val currentLongitude = "123.8854393"

            NagajaLog().e("wooks, data.getLatitude() = ${data.getLatitude()}")
            NagajaLog().e("wooks, data.getLongitude() = ${data.getLongitude()}")


            val dropOffAddress = getAddress(data.getLatitude(), data.getLongitude())
            val pickUpAddress = getAddress(currentLatitude.toDouble(), currentLongitude.toDouble())

            try {
                val grabOpenUrl = Uri.parse("https://grab.onelink.me/2695613898?af_dp=grab://open?dropOffAddress=$storeName" +
                        "$dropOffAddress&dropOffLatitude=${data.getLatitude()}&dropOffLongitude=${data.getLongitude()}" +
                        "&pickUpAddress=$pickUpAddress&pickUpLatitude=$currentLatitude&pickUpLongitude=$currentLongitude&screenType=BOOKING&sourceID=&taxiTypeId=227&c=&pid=")
                val intent = Intent(Intent.ACTION_VIEW, grabOpenUrl)
                startActivity(intent)

                NagajaLog().e("wooks, grabOpenUrl = $grabOpenUrl")
            } catch (e: ActivityNotFoundException) {
                NagajaLog().e("You don't have any browser to open web page")
            }
        }

        // Phone Call View
        mPhoneCallView.setOnClickListener {
            if (TextUtils.isEmpty(data.getPhoneNumber())) {
                mActivity.resources.getString(R.string.text_error_empty_phone_number)
            } else {
                phoneCall(data.getPhoneNumber())
            }
        }

        // Location Empty Text View
        if (data.getLatitude() == 0.0 || data.getLongitude() == 0.0 || TextUtils.isEmpty(data.getLatitude().toString()) || TextUtils.isEmpty(data.getLongitude().toString())) {
            mLocationEmptyTextView.visibility = View.VISIBLE
        }


        // Store Main Image List
        if (data.getStoreDetailImageListData().isEmpty()) {
            if (!TextUtils.isEmpty(data.getMainImageUrl())) {
                mMainImageList.add(DEFAULT_IMAGE_DOMAIN + IMAGE_THUMB + data.getMainImageUrl())
                mOriginImageList.add(DEFAULT_IMAGE_DOMAIN + data.getMainImageUrl())
            }
        } else {
            for (i in data.getStoreDetailImageListData().indices) {
                mMainImageList.add(DEFAULT_IMAGE_DOMAIN + IMAGE_THUMB+ data.getStoreDetailImageListData()[i].getCompanyImageName())
                mOriginImageList.add(DEFAULT_IMAGE_DOMAIN + data.getStoreDetailImageListData()[i].getCompanyImageName())
            }
        }
        mStoreMainImageAdapter.setData(mMainImageList)

        if (data.getIsReservationUseYn()) {
            mDeadLineTextView.visibility = View.VISIBLE
            mReservationTextView.text = mActivity.resources.getString(R.string.text_make_reservation)
            mReservationTextView.setTextSize(Dimension.SP, 17f)
            mReservationView.isEnabled = true
        } else {
            mDeadLineTextView.visibility = View.GONE
            mReservationTextView.text = mActivity.resources.getString(R.string.text_reservation_not_allow) + "\n" + mActivity.resources.getString(R.string.text_company_inquiry)
            mReservationTextView.setTextSize(Dimension.SP, 14f)
            mReservationView.isEnabled = false
        }

        setMap(data)
    }

    private fun getUserLocation(): String {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                StoreFragment.PERMISSION_REQUEST_CODE
            )

            NagajaLog().e("wooks, Location Permission Denied")

            return ""
        } else {
            val gpsTracker = GpsTracker(mActivity)
            val latitude = gpsTracker.getLatitude()
            val longitude = gpsTracker.getLongitude()

            return "$latitude,$longitude"
        }
    }

    private fun setMap(data: StoreDetailData) {
        mMapFragment.getMapAsync(OnMapReadyCallback { googleMap ->

            try {
                mMap = googleMap

                mMap!!.uiSettings.isScrollGesturesEnabled = false
                mMap!!.uiSettings.isZoomGesturesEnabled = false
                mMap!!.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = false

                mMap!!.setOnMapClickListener {
                    mListener.onMapScreen(data.getLatitude().toString() + "," + data.getLongitude().toString(), data.getAddress() + " " + data.getAddressDetail(), data.getCompanyName())
                }

                if (null != mMapMarker) {
                    mMapMarker!!.remove()
                    mMapMarker = null
                }

                if (TextUtils.isEmpty(data.getLatitude().toString()) || TextUtils.isEmpty(data.getLongitude().toString())) {
                    showToast(mActivity, mActivity.resources.getString(R.string.text_error_empty_location))
                    return@OnMapReadyCallback
                }

                val marker = LatLng(data.getLatitude(), data.getLongitude())
                val location = CameraUpdateFactory.newLatLngZoom(marker, 17f)

                mMapMarker = mMap!!.addMarker(
                    MarkerOptions()
                        .position(marker)
                        .title(data.getCompanyName())
                        .icon(bitmapDescriptorFromVector(mActivity, R.drawable.icon_map_marker_4))
                )
                mMapMarker!!.showInfoWindow()

                val gpsTracker = GpsTracker(mActivity)
                val currentMarker = LatLng(gpsTracker.getLatitude()!!, gpsTracker.getLongitude()!!)
//            val currentMarker = LatLng(10.3135861, 123.8809065)


                mMap!!.addMarker(
                    MarkerOptions()
                        .position(currentMarker)
                        .title(mActivity.resources.getString(R.string.text_my_location))
                        .icon(bitmapDescriptorFromVector(mActivity, R.drawable.icon_map_marker_2))
                )

//            mMapMarker!!.showInfoWindow()
                mMap!!.moveCamera(location)
                mMap!!.animateCamera(location)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    private fun getAddress(lan: Double, lon: Double): String {

        var address = ""
        val geocoder = Geocoder(mActivity, Locale.US)
        (mActivity).runOnUiThread {
            try {
                val addressList = geocoder.getFromLocation(lan, lon, 1)
                if (addressList!!.size != 0) {
                    address = addressList[0].getAddressLine(0)
                    if (address.substring(0, 4) == "대한민국") {
                        address = address.substring(5)
                    }
                }

//                val nation = addressList!![0].countryName
//                val adminArea = addressList[0].adminArea
//                val locality = addressList[0].locality
//                val subLocality = addressList[0].subLocality
//                val thoroughfare = addressList[0].thoroughfare
//                val subThoroughfare = addressList[0].subThoroughfare
//                val featureName = addressList[0].featureName
//                val address = addressList[0].getAddressLine(0).toString()
//
//                NagajaLog().d("wooks, Nation (국가명) = $nation")
//                NagajaLog().d("wooks, AdminArea (시) = $adminArea")
//                NagajaLog().d("wooks, Locality (구 메인) = $locality")
//                NagajaLog().d("wooks, SubLocality (구 서브) = $subLocality")
//                NagajaLog().d("wooks, Thoroughfare (동) = $thoroughfare")
//                NagajaLog().d("wooks, SubThoroughfare (번지) = $subThoroughfare")
//                NagajaLog().d("wooks, FeatureName (번지) = $featureName")
//                NagajaLog().d("wooks, Address (국가명 시 군 구 동 번지) = $address")

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return address
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun findWay(latitude: String, longitude: String) {
        val installApp = mActivity.packageManager.getLaunchIntentForPackage("com.google.android.apps.maps")
        if (null != installApp) {

            val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$latitude,$longitude&travelmode=transit")
            val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            intent.setPackage("com.google.android.apps.maps")
            try {
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                try {
                    val unrestrictedIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    startActivity(unrestrictedIntent)
                } catch (innerEx: ActivityNotFoundException) {
                    showToast(mActivity, mActivity.resources.getString(R.string.text_error_install_google_map))
                }
            }
        } else {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_install_google_map))
        }
    }

    private fun navigation(latitude: String, longitude: String) {
        val installApp = mActivity.packageManager.getLaunchIntentForPackage("com.google.android.apps.maps")
        if (null != installApp) {
            val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude&mode=d")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        } else {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_install_google_map))
        }
    }

    private fun copyAddress(address: String) {
        mClipData = ClipData.newPlainText("label", address)
        mClipboardManager!!.setPrimaryClip(mClipData!!)

        showToast(mActivity, mActivity.resources.getString(R.string.text_copy_address))
    }

    private fun phoneCall(phoneNumber: String) {
        var number = phoneNumber.replace(" ", "")
        number = number.replace("-", "")
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(intent)
    }

    private fun showReviewDeletePopup(data: StoreDetailReviewData, position: Int) {

        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_delete_review),
            mActivity.resources.getString(R.string.text_message_delete_review),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
                getStoreReviewDelete(data.getReviewUid(), data.getCompanyUid(), position)
            }
        })
        customDialog.show()
    }

    private fun showDisableChatCustomPopup() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NO_CANCEL,
            mActivity.resources.getString(R.string.text_noti),
            mActivity.resources.getString(R.string.text_message_disable_chat),
            "",
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
            }
        })
        customDialog.show()
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
        if (context is OnStoreDetailFragmentListener) {
            mActivity = context as Activity

            if (context is OnStoreDetailFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnStoreDetailFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnStoreDetailFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnStoreDetailFragmentListener"
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
     * API. Get Store Detail
     */
    private fun getStoreDetail() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<StoreDetailData> {
                override fun onSuccess(resultData: StoreDetailData) {

                    displayStoreData(resultData)
                    getStoreDetailMenuData(resultData.getCategoryUid())
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
            getCompanyUid().toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Store Detail Menu Data
     */
    private fun getStoreDetailMenuData(categoryUid: Int) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreDetailMenuData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<StoreDetailMenuData>> {
                override fun onSuccess(resultData: ArrayList<StoreDetailMenuData>) {
                    mStoreMenuAdapter.setData(resultData)

                    getStoreReviewData(true)
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
            getCompanyUid().toString(),
            categoryUid.toString()
        )
    }

    /**
     * API. Get Store Detail Menu Data
     */
    fun getStoreReviewData(isRefresh: Boolean) {

        if (isRefresh) {
            mPageCount = 1
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreReviewData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<StoreDetailReviewData>> {
                override fun onSuccess(resultData: ArrayList<StoreDetailReviewData>) {

                    if (resultData.isEmpty()) {
                        mTotalReviewCount = 0

                        mStoreReviewRecyclerView.visibility = View.GONE
                        mReviewEmptyView.visibility = View.VISIBLE

                        return
                    } else {
                        mTotalReviewCount = resultData[0].getCount()

                        mStoreReviewRecyclerView.visibility = View.VISIBLE
                        mReviewEmptyView.visibility = View.GONE
                    }

                    if (isRefresh) {
                        mReviewListData.clear()
                        mReviewListData = resultData
                    } else {
                        if (mReviewListData.size == 0) {
                            mReviewListData = resultData
                        } else {
                            mReviewListData.addAll(resultData)
                        }
                    }

                    mReviewCountTextView.text = String.format(mActivity.resources.getString(R.string.text_review_count), Util().convertToComma(mTotalReviewCount))
                    mReviewLayoutCountTextView.text = String.format(mActivity.resources.getString(R.string.text_review_count_desc), Util().convertToComma(mTotalReviewCount))

                    mIsEndOfScroll = false
                    mStoreReviewAdapter.setData(mReviewListData)

                    mPageCount++
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
            mPageCount,
            10,
            getCompanyUid().toString()
        )
    }

    /**
     * API. Get Store Review Delete
     */
    private fun getStoreReviewDelete(reviewUid: Int, companyUid: Int, position: Int) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreReviewDelete(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    mStoreReviewAdapter.deleteItem(position)

                    mReviewCountTextView.text = String.format(mActivity.resources.getString(R.string.text_review_count), Util().convertToComma(mTotalReviewCount - 1))
                    mReviewLayoutCountTextView.text = String.format(mActivity.resources.getString(R.string.text_review_count_desc), Util().convertToComma(mTotalReviewCount - 1))

                    mTotalReviewCount--
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
            reviewUid,
            companyUid
        )
    }

    /**
     * API. Get Store List Data
     */
    private fun getStoreRegularSave() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreRegularSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    mStoreDetailData.setIsRecommendUseYn(!mStoreDetailData.getIsRecommendUseYn())

                    mGoodImageView.setImageResource( if (mStoreDetailData.getIsRecommendUseYn()) R.drawable.icon_store_regula else R.drawable.icon_store_regula_2)
                    if (mStoreDetailData.getIsRecommendUseYn()) {
                        mStoreDetailData.setRecommendCount(mStoreDetailData.getRecommendCount() + 1)
                        mRecommendCountTextView.text = String.format(mActivity.resources.getString(R.string.text_recommend_count), Util().convertToComma(mStoreDetailData.getRecommendCount()))
                    } else {
                        if (mStoreDetailData.getRecommendCount() == 0) {
                            mRecommendCountTextView.text = String.format(mActivity.resources.getString(R.string.text_recommend_count), "0")
                        } else {
                            mStoreDetailData.setRecommendCount(mStoreDetailData.getRecommendCount() - 1)
                            mRecommendCountTextView.text = String.format(mActivity.resources.getString(R.string.text_recommend_count), Util().convertToComma(mStoreDetailData.getRecommendCount()))
                        }
                    }
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
            mStoreDetailData.getCompanyUid(),
            if (!mStoreDetailData.getIsRegularUseYn()) 0 else 1
        )
    }

    private fun getStoreRecommendSave() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreRecommendSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    mStoreDetailData.setIsRecommendUseYn(!mStoreDetailData.getIsRecommendUseYn())

                    mGoodImageView.setImageResource( if (mStoreDetailData.getIsRecommendUseYn()) R.drawable.icon_store_regula else R.drawable.icon_store_regula_2)
                    if (mStoreDetailData.getIsRecommendUseYn()) {
                        mStoreDetailData.setRecommendCount(mStoreDetailData.getRecommendCount() + 1)
                        mRecommendCountTextView.text = String.format(mActivity.resources.getString(R.string.text_recommend_count), Util().convertToComma(mStoreDetailData.getRecommendCount()))
                    } else {
                        if (mStoreDetailData.getRecommendCount() == 0) {
                            mRecommendCountTextView.text = String.format(mActivity.resources.getString(R.string.text_recommend_count), "0")
                        } else {
                            mStoreDetailData.setRecommendCount(mStoreDetailData.getRecommendCount() - 1)
                            mRecommendCountTextView.text = String.format(mActivity.resources.getString(R.string.text_recommend_count), Util().convertToComma(mStoreDetailData.getRecommendCount()))
                        }
                    }

                    mChangeStatus = true
                    mIsStatus = mStoreDetailData.getIsRecommendUseYn()
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
            mStoreDetailData.getCompanyUid(),
            if (mStoreDetailData.getIsRecommendUseYn()) 0 else 1
        )
    }

    /**
     * API. Get Review Recommend Save
     */
    private fun getReviewRecommendSave(reviewUid: Int, isRecommend: Boolean, position: Int) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getReviewRecommendSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mStoreReviewAdapter.setRecommend(isRecommend, position)
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
            reviewUid,
            if (isRecommend) 1 else 0
        )
    }
}