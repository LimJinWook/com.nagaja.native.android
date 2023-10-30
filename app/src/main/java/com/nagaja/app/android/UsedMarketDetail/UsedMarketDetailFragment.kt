package com.nagaja.app.android.UsedMarketDetail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kenilt.loopingviewpager.widget.LoopingViewPager
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Adapter.UsedMarketDetailImagePagerAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.PullUpCountData
import com.nagaja.app.android.Data.UsedMarketData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.android.synthetic.main.fragment_used_market.view.*
import kotlinx.android.synthetic.main.fragment_used_market_detail.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class UsedMarketDetailFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mAllAddressView: View
    private lateinit var mCurrency2View: View
    private lateinit var mCurrency3View: View
    private lateinit var mCurrency4View: View
    private lateinit var mCurrency5View: View
    private lateinit var mMyMarketView: View



    private lateinit var mProfileImageView: SimpleDraweeView

    private lateinit var mSellerNameTextView: TextView
    private lateinit var mAreaTextView: TextView
    private lateinit var mContentsTitleTextView: TextView
    private lateinit var mCategoryTextView: TextView
    private lateinit var mDateTextView: TextView
    private lateinit var mContentsTextView: TextView
    private lateinit var mChatCountTextView: TextView
    private lateinit var mSearchCountTextView: TextView
    private lateinit var mFavoriteCountTextView: TextView
    private lateinit var mAddAddressTextView: TextView
    private lateinit var mChatTextView: TextView
    private lateinit var mReportTextView: TextView
    private lateinit var mMyMarketModifyTextView: TextView
    private lateinit var mRemoveRegisterTextView: TextView
    private lateinit var mPullUpTextView: TextView


    private lateinit var mCurrency1TextView: TextView
    private lateinit var mCurrency2TextView: TextView
    private lateinit var mCurrency3TextView: TextView
    private lateinit var mCurrency4TextView: TextView
    private lateinit var mCurrency5TextView: TextView
    private lateinit var mPrice1TextView: TextView
    private lateinit var mPrice2TextView: TextView
    private lateinit var mPrice3TextView: TextView
    private lateinit var mPrice4TextView: TextView
    private lateinit var mPrice5TextView: TextView

    private lateinit var mRecommendIconImageView: ImageView

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mViewPager: LoopingViewPager

    private lateinit var mViewPagerIndicator: WormDotsIndicator

    private lateinit var mMapFragment: SupportMapFragment
    private var mMap: GoogleMap? = null
    private val mLatLng: LatLng? = null
    private var mMapMarker: Marker? = null

    private lateinit var mUsedMarketDetailImagePagerAdapter: UsedMarketDetailImagePagerAdapter

    private lateinit var mListener: OnUsedMarketFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private lateinit var mUsedMarketData: UsedMarketData

    interface OnUsedMarketFragmentListener {
        fun onFinish()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
        fun onMapScreen(location: String)
        fun onReportScreen(reportType: Int, reviewUid: Int, storeName: String)
        fun onModifyScreen(itemUid: Int)
        fun onSuccessDelete()
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
        fun onSuccessPullUp()
        fun onChatViewScreen(chatClass: String, chatData: String)
    }

    companion object {

        const val ARGS_USED_MARKET_DATA               = "args_used_market_data"

        fun newInstance(itemUid: Int): UsedMarketDetailFragment {
            val args = Bundle()
            val fragment = UsedMarketDetailFragment()
            args.putInt(ARGS_USED_MARKET_DATA, itemUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getItemUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_USED_MARKET_DATA) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_used_market_detail, container, false)

        init()

        return mContainer
    }

    @SuppressLint("SetTextI18n")
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
            setShareUrl(mActivity, COMPANY_TYPE_USED_MARKET, getItemUid())
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

        // Scroll View
        mScrollView = mContainer.fragment_used_market_detail_scroll_view

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.VISIBLE
        backImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_used_market)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // View Pager
        mViewPager = mContainer.fragment_used_market_detail_image_view_pager

        // View Pager Dot Indicator
        mViewPagerIndicator = mContainer.fragment_used_market_detail_view_pager_dot_indicator

        // Profile Image View
        mProfileImageView = mContainer.fragment_used_market_detail_profile_image_view

        // Name Text View
        mSellerNameTextView = mContainer.fragment_used_market_detail_name_text_view

        // Area Text View
        mAreaTextView = mContainer.fragment_used_market_detail_address_text_view

        // Level Text View
//        val levelTextView = mContainer.fragment_used_market_detail_level_text_view
//        if (!TextUtils.isEmpty(getUsedMarketData().getUserLevel())) {
//            levelTextView.text = getUsedMarketData().getUserLevel()
//        }

        // Title Text View
        mContentsTitleTextView = mContainer.fragment_used_market_detail_title_text_view

        // Category Text View
        mCategoryTextView = mContainer.fragment_used_market_detail_category_text_view

        // Date Text View
        mDateTextView = mContainer.fragment_used_market_detail_date_text_view

        // Contents Text View
        mContentsTextView = mContainer.fragment_used_market_detail_contents_text_view

        // Chat Count Text View
        mChatCountTextView = mContainer.fragment_used_market_detail_chat_count_text_view

        // Search Count Text View
        mSearchCountTextView = mContainer.fragment_used_market_detail_search_count_text_view

        // Favorite Count Text View
        mFavoriteCountTextView = mContainer.fragment_used_market_detail_favorite_count_text_view

        // Google Map View
        mMapFragment = childFragmentManager.findFragmentById(R.id.fragment_used_market_detail_map_view) as SupportMapFragment
        mMapFragment.getMapAsync {
            val seoul = LatLng(37.494870, 126.960763)
            val location = CameraUpdateFactory.newLatLngZoom(seoul, 17f)
            it.moveCamera(location)
            it.animateCamera(location)
        }

        // All Address View
        mAllAddressView = mContainer.fragment_used_market_detail_all_address_view

        // All Address Text View
        mAddAddressTextView = mContainer.fragment_used_market_detail_all_address_text_view

        // Report Text View
        mReportTextView = mContainer.fragment_used_market_detail_report_text_view
        mReportTextView.setOnClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                mListener.onReportScreen(REPORT_TYPE_USED_MARKET, mUsedMarketData.getItemUid(), "")
            }
        }

        // My Market View
        mMyMarketView = mContainer.fragment_used_market_detail_my_market_view

        // My Market Modify Text View
        mMyMarketModifyTextView = mContainer.fragment_used_market_detail_my_market_modify_text_view
        mMyMarketModifyTextView.setOnClickListener {
            mListener.onModifyScreen(mUsedMarketData.getItemUid())
        }

        // Remove Register Text View
        mRemoveRegisterTextView = mContainer.fragment_used_market_detail_my_market_delete_text_view
        mRemoveRegisterTextView.setOnClickListener {
            showMyUsedMarketDeletePopup()
        }

        // Recommend Icon Image View
        mRecommendIconImageView = mContainer.fragment_used_market_detail_recommend_image_view
        mRecommendIconImageView.setOnClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                getUsedMarketRecommendSave(!mUsedMarketData.getIsRecommendYn())
            }
        }

        // Currency 1 Text View
        mCurrency1TextView = mContainer.fragment_used_market_detail_currency_1_text_view

        // Price 1 Text View
        mPrice1TextView = mContainer.fragment_used_market_detail_price_1_text_view



        // Currency 2 View
        mCurrency2View = mContainer.fragment_used_market_detail_currency_2_view

        // Currency 2 Text View
        mCurrency2TextView = mContainer.fragment_used_market_detail_currency_2_text_view

        // Price 2 Text View
        mPrice2TextView = mContainer.fragment_used_market_detail_price_2_text_view



        // Currency 3 View
        mCurrency3View = mContainer.fragment_used_market_detail_currency_3_view

        // Currency 3 Text View
        mCurrency3TextView = mContainer.fragment_used_market_detail_currency_3_text_view

        // Price 3 Text View
        mPrice3TextView = mContainer.fragment_used_market_detail_price_3_text_view





        // Currency 4 View
        mCurrency4View = mContainer.fragment_used_market_detail_currency_4_view

        // Currency 4 Text View
        mCurrency4TextView = mContainer.fragment_used_market_detail_currency_4_text_view

        // Price 4 Text View
        mPrice4TextView = mContainer.fragment_used_market_detail_price_4_text_view




        // Currency 5 View
        mCurrency5View = mContainer.fragment_used_market_detail_currency_5_view

        // Currency 5 Text View
        mCurrency5TextView = mContainer.fragment_used_market_detail_currency_5_text_view

        // Price 5 Text View
        mPrice5TextView = mContainer.fragment_used_market_detail_price_5_text_view



        // Chat Text View
        mChatTextView = mContainer.fragment_used_market_detail_chat_text_view


        getUsedMarketDetailData(false)
    }

    @SuppressLint("SetTextI18n")
    private fun displayUsedMarketDetailData(isRefresh: Boolean) {

        if (null == mUsedMarketData) {
            return
        }

        // Profile Image View
        if (!TextUtils.isEmpty(mUsedMarketData.getSellerImageName())) {
            mProfileImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + mUsedMarketData.getSellerImageName()))
            mProfileImageView.setOnSingleClickListener {
                val imageList = ArrayList<String>()
                imageList.add(DEFAULT_IMAGE_DOMAIN + mUsedMarketData.getSellerImageName())

                mListener.onFullScreenImage(imageList, 0)
            }
        }

        // Name Text View
        if (!TextUtils.isEmpty(mUsedMarketData.getSellerName()) && !TextUtils.isEmpty(mUsedMarketData.getMemberEmail())) {
            mSellerNameTextView.text = mUsedMarketData.getSellerName() + " (" + Util().getEmailMasking(mUsedMarketData.getMemberEmail()) + ")"
        }

        if (!TextUtils.isEmpty(mUsedMarketData.getSellerName())) {
            mSellerNameTextView.text = mUsedMarketData.getSellerName()

            if (!TextUtils.isEmpty(mUsedMarketData.getMemberEmail())) {
                mSellerNameTextView.text = mSellerNameTextView.text.toString() + " (" + Util().getEmailMasking(mUsedMarketData.getMemberEmail()) + ")"
            }
        }

        // Area Text View
        var mainArea = ""
        var subArea = ""
        if (!TextUtils.isEmpty(mUsedMarketData.getSiDoName())) {
            mainArea = mUsedMarketData.getSiDoName()
        }
        if (!TextUtils.isEmpty(mUsedMarketData.getGuGunName())) {
            subArea = mUsedMarketData.getGuGunName()
        }
        mAreaTextView.text = "$mainArea $subArea"

        // Title Text View
        if (!TextUtils.isEmpty(mUsedMarketData.getItemSubject())) {
            mContentsTitleTextView.text = mUsedMarketData.getItemSubject()
        }

        // Category Text View
        if (!TextUtils.isEmpty(mUsedMarketData.getCategoryName())) {
            mCategoryTextView.text = mUsedMarketData.getCategoryName()
        }

        // Date Text View
        if (!TextUtils.isEmpty(mUsedMarketData.getCreateDate())) {
            mDateTextView.text = Util().getStringToTime(mUsedMarketData.getCreateDate())
        }

        // Contents Text View
        if (!TextUtils.isEmpty(mUsedMarketData.getItemContent())) {
            mContentsTextView.text = mUsedMarketData.getItemContent()
        }

        // Chat Count Text View
        mChatCountTextView.text = if (mUsedMarketData.getItemChatCount() > 0) mUsedMarketData.getItemChatCount().toString() else "0"

        // Search Count Text View
        mSearchCountTextView.text = if (mUsedMarketData.getItemViewCount() > 0) mUsedMarketData.getItemViewCount().toString() else "0"

        // Favorite Count Text View
        mFavoriteCountTextView.text = if (mUsedMarketData.getItemInterestCount() > 0) mUsedMarketData.getItemInterestCount().toString() else "0"

        // All Address Text View
        mAddAddressTextView.text = "$mainArea $subArea\n${mUsedMarketData.getLocationDetail()}"

        // Recommend Icon Image View
        mRecommendIconImageView.setImageResource( if (mUsedMarketData.getIsRecommendYn()) R.drawable.icon_store_regula else R.drawable.icon_store_regula_2)

        if (mUsedMarketData.getMemberUid() == MAPP.USER_DATA.getMemberUid()) {
            mReportTextView.visibility = View.GONE
            mMyMarketView.visibility = View.VISIBLE
        } else {
            mReportTextView.visibility = View.VISIBLE
            mMyMarketView.visibility = View.GONE
        }

        if (mUsedMarketData.getItemStatus() == USED_MARKET_STATUS_CANCEL || mUsedMarketData.getItemStatus() == USED_MARKET_STATUS_RETURN) {
            mPrice1TextView.paintFlags = mPrice1TextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            mPrice2TextView.paintFlags = mPrice1TextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            mPrice3TextView.paintFlags = mPrice1TextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            mPrice4TextView.paintFlags = mPrice1TextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            mPrice5TextView.paintFlags = mPrice1TextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            mChatTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e1e1e1))
            mChatTextView.isEnabled = false
        }

        if (isRefresh) {
            mScrollView.post(Runnable { mScrollView.fullScroll(ScrollView.FOCUS_UP) })
        }

        // Chat Text View
//        mChatTextView.text = if (mUsedMarketData.getMemberUid() == MAPP.USER_DATA.getMemberUid()) mActivity.resources.getString(R.string.text_used_market_pause)
//        else mActivity.resources.getString(R.string.text_chatting)

        var itemStatusValue = ""
        if (mUsedMarketData.getMemberUid() == MAPP.USER_DATA.getMemberUid()) {
            if (mUsedMarketData.getItemStatus() == USED_MARKET_STATUS_PROGRESS) {
                itemStatusValue = mActivity.resources.getString(R.string.text_resale)
            } else {
                itemStatusValue = mActivity.resources.getString(R.string.text_supension)
            }
        } else {
            itemStatusValue = mActivity.resources.getString(R.string.text_chatting)
        }
        mChatTextView.text = itemStatusValue

        mChatTextView.setOnClickListener {
            if (mUsedMarketData.getMemberUid() == MAPP.USER_DATA.getMemberUid()) {
                if (mUsedMarketData.getItemStatus() == 1 || mUsedMarketData.getItemStatus() == 5 || mUsedMarketData.getItemStatus() == 7) {
                    showChangeStatusCustomPopup(mUsedMarketData.getItemStatus())
                }
            } else {
                if (MAPP.IS_NON_MEMBER_SERVICE) {
                    showLoginGuideCustomDialog()
                } else {

                    var isMemberChat = true
                    var targetUid = 0

                    if (mUsedMarketData.getMemberUid() > 0 && mUsedMarketData.getCompanyUid() == 0) {
                        isMemberChat = true
                        targetUid = mUsedMarketData.getMemberUid()
                    } else if (mUsedMarketData.getCompanyUid() > 0) {
                        isMemberChat = false
                        targetUid = mUsedMarketData.getCompanyUid()
                    }

                    NagajaLog().e("wooks, isMemberChat = $isMemberChat")
                    NagajaLog().e("wooks, targetUid = $targetUid")

                    val chatData = "[{${MAPP.USER_DATA.getMemberUid()}, 0}, {${if (isMemberChat) "$targetUid, 0" else "${mUsedMarketData.getMemberUid()}, $targetUid"}}]"
                    if (!TextUtils.isEmpty(chatData)) {
                        mListener.onChatViewScreen(CHAT_CLASS_USED_MARKET, chatData)
                    }
                }
            }
        }

        // Pull Up Text View
        mPullUpTextView = mContainer.fragment_used_market_detail_pull_up_text_view
        mPullUpTextView.visibility = if (mUsedMarketData.getMemberUid() == MAPP.USER_DATA.getMemberUid()) View.VISIBLE else View.GONE
        mPullUpTextView.setOnClickListener {
            getUsedMarketPullUpCount()
        }

        setImageArrayList()
        setPrice()
    }

    private fun setImageArrayList() {

        var isEmptyImage = false

        val imageList = ArrayList<String>()
        for (i in mUsedMarketData.getImageListData().indices) {
            imageList.add(DEFAULT_IMAGE_DOMAIN + mUsedMarketData.getImageListData()[i].getItemImageName())
        }

        if (mUsedMarketData.getImageListData().size == 0) {
            imageList.add("")
            isEmptyImage = true
        }

        mViewPager.clipToPadding = false
        mViewPager.setPadding(80, 0, 80, 0)
//        mViewPager.setPadding(0, 0, 0, 0)
        mViewPager.pageMargin = 50
        mUsedMarketDetailImagePagerAdapter = UsedMarketDetailImagePagerAdapter(mActivity, imageList, null)
        mViewPager.adapter = mUsedMarketDetailImagePagerAdapter
        mViewPagerIndicator.setViewPager(mViewPager)
        mUsedMarketDetailImagePagerAdapter.setOnImagePagerClickListener(object : UsedMarketDetailImagePagerAdapter.OnImagePagerClickListener {
            override fun onClick(position: Int) {
                if (isEmptyImage) {
                    return
                }

                mListener.onFullScreenImage(imageList, position)
            }
        })
        mViewPager.adapter = mUsedMarketDetailImagePagerAdapter
        mViewPagerIndicator.setViewPager(mViewPager)

        setMap()

    }

    private fun getCurrencyCode(code: String): String {
        var currencyCode = "$"
        when (code) {
            CURRENCY_TYPE_USD -> {
                currencyCode = "$"
            }

            CURRENCY_TYPE_KRW -> {
                currencyCode = "￦"
            }

            CURRENCY_TYPE_PHP -> {
                currencyCode = "₱"
            }

            CURRENCY_TYPE_CNY -> {
                currencyCode = "元"
            }

            CURRENCY_TYPE_JPY -> {
                currencyCode = "¥"
            }
        }
        return  currencyCode
    }

    private fun setPrice() {

        if (!TextUtils.isEmpty(mUsedMarketData.getItemCurrencyCode())) {
            mCurrency1TextView.text = getCurrencyCode(mUsedMarketData.getItemCurrencyCode())
            mPrice1TextView.text = Util().getTwoDecimalPlaces(mUsedMarketData.getItemPrice())
            mPrice1TextView.isSelected = true
        }


        for (i in mUsedMarketData.getCurrencyListData().indices) {
            try {
                if (i == 0) {
                    if (mUsedMarketData.getCurrencyListData()[i].getCurrencyPrice() == 0.0) {
                        return
                    }

                    mCurrency2View.visibility = View.VISIBLE
                    mCurrency2TextView.text = getCurrencyCode(mUsedMarketData.getCurrencyListData()[i].getCurrencyCode())
                    mPrice2TextView.text = Util().getTwoDecimalPlaces(mUsedMarketData.getCurrencyListData()[i].getCurrencyPrice())
                    mPrice2TextView.isSelected = true
                } else if (i == 1) {
                    if (mUsedMarketData.getCurrencyListData()[i].getCurrencyPrice() == 0.0) {
                        return
                    }

                    mCurrency3View.visibility = View.VISIBLE
                    mCurrency3TextView.text = getCurrencyCode(mUsedMarketData.getCurrencyListData()[i].getCurrencyCode())
                    mPrice3TextView.text = Util().getTwoDecimalPlaces(mUsedMarketData.getCurrencyListData()[i].getCurrencyPrice())
                    mPrice3TextView.isSelected = true
                } else if (i == 2) {
                    if (mUsedMarketData.getCurrencyListData()[i].getCurrencyPrice() == 0.0) {
                        return
                    }

                    mCurrency4View.visibility = View.VISIBLE
                    mCurrency4TextView.text = getCurrencyCode(mUsedMarketData.getCurrencyListData()[i].getCurrencyCode())
                    mPrice4TextView.text = Util().getTwoDecimalPlaces(mUsedMarketData.getCurrencyListData()[i].getCurrencyPrice())
                    mPrice4TextView.isSelected = true
                } else if (i == 3) {
                    if (mUsedMarketData.getCurrencyListData()[i].getCurrencyPrice() == 0.0) {
                        return
                    }

                    mCurrency5View.visibility = View.VISIBLE
                    mCurrency5TextView.text = getCurrencyCode(mUsedMarketData.getCurrencyListData()[i].getCurrencyCode())
                    mPrice5TextView.text = Util().getTwoDecimalPlaces(mUsedMarketData.getCurrencyListData()[i].getCurrencyPrice())
                    mPrice5TextView.isSelected = true
                } /*else if (i == 4) {
                    mCurrency5View.visibility = View.VISIBLE
                    mCurrency5TextView.text = getCurrencyCode(mUsedMarketData.getCurrencyListData()[i].getCurrencyCode())
                    mPrice5TextView.text = Util().getTwoDecimalPlaces(mUsedMarketData.getCurrencyListData()[i].getCurrencyPrice())
                    mPrice5TextView.isSelected = true
                }*/
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setMap() {
        mMapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
            mMap = googleMap

            if (null != mMapMarker) {
                mMapMarker!!.remove()
                mMapMarker = null
            }

            mMap!!.uiSettings.isScrollGesturesEnabled = false
            mMap!!.uiSettings.isZoomGesturesEnabled = false
            mMap!!.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = false

            if (TextUtils.isEmpty(mUsedMarketData.getLatitude()) || TextUtils.isEmpty(mUsedMarketData.getLongitude())) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_empty_location))
                return@OnMapReadyCallback
            }

            val lat = mUsedMarketData.getLatitude()
            val lon = mUsedMarketData.getLongitude()

            val marker = LatLng(lat.toDouble(), lon.toDouble())
            val location = CameraUpdateFactory.newLatLngZoom(marker, 17f)

            mMapMarker = mMap!!.addMarker(
                MarkerOptions()
                    .position(marker)
                    .title(mActivity.resources.getString(R.string.text_place_of_transaction))
                    .icon(bitmapDescriptorFromVector(mActivity, R.drawable.icon_map_marker_4))
            )
            mMapMarker!!.showInfoWindow()
            mMap!!.moveCamera(location)
            mMap!!.animateCamera(location)

            mMap!!.setOnMapClickListener {
                mListener.onMapScreen(mUsedMarketData.getLatitude() + "," + mUsedMarketData.getLongitude())
            }
        })
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun showMyUsedMarketDeletePopup() {

        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_title_delete_used_market),
            mUsedMarketData.getItemSubject() + "\n" + mActivity.resources.getString(R.string.text_message_delete_my_used_market),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
                getDeleteMyUsedMarket()
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

    private fun showChangeStatusCustomPopup(itemStatus: Int) {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            if (itemStatus == USED_MARKET_STATUS_PROGRESS) mActivity.resources.getString(R.string.text_resale) else mActivity.resources.getString(R.string.text_supension),
            if (itemStatus == USED_MARKET_STATUS_PROGRESS) mActivity.resources.getString(R.string.text_message_resale) else mActivity.resources.getString(R.string.text_message_pause_sales),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        getString(R.string.text_message_resale)
        getString(R.string.text_message_pause_sales)

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
                getUsedMarketStatusChange(itemStatus)
            }
        })
        customDialog.show()
    }

    private fun showPullUpCustomPopup(pullUpCountData: PullUpCountData) {

        if (pullUpCountData.getUpperCount() <= 0) {
            val customDialog = CustomDialog(
                mActivity,
                DIALOG_TYPE_NORMAL,
                mActivity.resources.getString(R.string.text_pull_up),
                getString(R.string.text_message_remaining_count_2),
                mActivity.resources.getString(R.string.text_cancel),
                mActivity.resources.getString(R.string.text_confirm)
            )

            customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
                override fun onCancel() {
                    customDialog.dismiss()
                }

                override fun onConfirm() {
                    customDialog.dismiss()
                    getUsedMarketPullUp()
                }
            })
            customDialog.show()
        } else {
            val customDialog = CustomDialog(
                mActivity,
                DIALOG_TYPE_NORMAL,
                mActivity.resources.getString(R.string.text_pull_up),
                mUsedMarketData.getItemName() + "\n\n" + mActivity.resources.getString(R.string.text_message_pull_up)
                        + "\n\n" + mActivity.resources.getString(R.string.text_remaining_count) + " : " + pullUpCountData.getUpperCount() + " / 3"
                        + "\n" + mActivity.resources.getString(R.string.text_message_remaining_count),
                mActivity.resources.getString(R.string.text_cancel),
                mActivity.resources.getString(R.string.text_confirm)
            )

            customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
                override fun onCancel() {
                    customDialog.dismiss()
                }

                override fun onConfirm() {
                    customDialog.dismiss()
                    getUsedMarketPullUp()
                }
            })
            customDialog.show()
        }
    }

    private fun getAddress(lan: Double, lon: Double): String {
        var address = ""
        val geocoder = Geocoder(mActivity, Locale.getDefault())

        (mActivity).runOnUiThread {
            try {
                val addressList = geocoder.getFromLocation(lan, lon, 1)
                if (addressList!!.size != 0) {
                    address = addressList[0].getAddressLine(0)
                    if (address.substring(0, 4) == "대한민국") {
                        address = address.substring(5)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return address
    }

    fun dispatchTouchEvent() {
        mScrollView.requestDisallowInterceptTouchEvent(true)
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
        if (context is OnUsedMarketFragmentListener) {
            mActivity = context as Activity

            if (context is OnUsedMarketFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnUsedMarketFragmentListener"
                )
            }
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        if (context is OnUsedMarketFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnUsedMarketFragmentListener"
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
     * API. Get Review Recommend Save
     */
    fun getUsedMarketDetailData(isRefresh: Boolean) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getUsedMarketDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<UsedMarketData> {
                override fun onSuccess(resultData: UsedMarketData) {
                    mUsedMarketData = resultData

                    displayUsedMarketDetailData(isRefresh)
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
            getItemUid(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Review Recommend Save
     */
    private fun getUsedMarketRecommendSave(isRecommend: Boolean) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getUsedMarketRecommendSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mUsedMarketData.setIsRecommendYn(isRecommend)
                    mRecommendIconImageView.setImageResource( if (isRecommend) R.drawable.icon_store_regula else R.drawable.icon_store_regula_2)
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
            mUsedMarketData.getItemUid(),
            if (isRecommend) 1 else 0
        )
    }

    /**
     * API. Get Review Recommend Save
     */
    private fun getDeleteMyUsedMarket() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getDeleteMyUsedMarket(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    showToast(mActivity, mActivity.resources.getString(R.string.text_message_success_delete))
                    mListener.onSuccessDelete()
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
            mUsedMarketData.getItemUid(),
            mUsedMarketData.getCompanyUid()
        )
    }

    /**
     * API. Get Used Market Status Change
     */
    private fun getUsedMarketStatusChange(itemStatus: Int) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getUsedMarketStatusChange(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    showToast(mActivity, mActivity.resources.getString(R.string.text_change_status))

                    mUsedMarketData.setItemStatus(if (itemStatus == USED_MARKET_STATUS_PROGRESS) USED_MARKET_STATUS_COMPLETE else USED_MARKET_STATUS_PROGRESS)

                    mChatTextView.text = if (mUsedMarketData.getItemStatus() == USED_MARKET_STATUS_PROGRESS) mActivity.resources.getString(R.string.text_resale) else mActivity.resources.getString(R.string.text_supension)
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
            if (itemStatus == USED_MARKET_STATUS_PROGRESS) USED_MARKET_STATUS_COMPLETE else USED_MARKET_STATUS_PROGRESS,
            mUsedMarketData.getItemUid(),
            mUsedMarketData.getCompanyUid()
        )
    }

    /**
     * API. Get Used Market Pull Up Count
     */
    private fun getUsedMarketPullUpCount() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getUsedMarketPullUpCount(
            mRequestQueue,
            object : NagajaManager.RequestListener<PullUpCountData> {
                override fun onSuccess(resultData: PullUpCountData) {
                    showPullUpCustomPopup(resultData)
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
            mUsedMarketData.getItemUid(),
            mUsedMarketData.getCompanyUid()
        )
    }

    /**
     * API. Get Used Market Pull Up
     */
    private fun getUsedMarketPullUp() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getUsedMarketPullUp(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mListener.onSuccessPullUp()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    } else if (errorCode == ErrorRequest.ERROR_CODE_NOT_ENOUGH_POINT) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_note_enough_points))
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mUsedMarketData.getItemUid(),
            mUsedMarketData.getCompanyUid()
        )
    }
}