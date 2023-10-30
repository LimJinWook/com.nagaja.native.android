package com.nagaja.app.android.Main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.kenilt.loopingviewpager.scroller.AutoScroller
import com.kenilt.loopingviewpager.widget.LoopingViewPager
import com.nagaja.app.android.Data.EventData
import com.nagaja.app.android.Dialog.CustomEventDialog
import com.nagaja.app.android.Dialog.CustomExchangeRateDialog
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_STORE_LIST_DATA
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Notice.NoticeActivity
import com.nagaja.app.android.StoreDetail.StoreDetailActivity
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.NagajaManager
import com.nagaja.app.android.Utils.SharedPreferencesUtil
import com.nagaja.app.android.R
import com.nagaja.app.android.SignUp.SignUpFragment

import com.serte.horizontalscrollbar.HorizontalScrollBarView
import com.skydoves.expandablelayout.ExpandableLayout
import kotlinx.android.synthetic.main.expandable_layout_company_header.view.*
import kotlinx.android.synthetic.main.expandable_layout_company_sub.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : NagajaFragment() {

    private lateinit var mContainer: View

    private lateinit var mMainActivity: MainActivity

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mBannerEventCountTextView: TextView
    private lateinit var mServiceNameTextView: TextView
    private lateinit var mServiceMasterTextView: TextView
    private lateinit var mServiceLicenseNumberTextView: TextView
    private lateinit var mServiceMailOrderBusinessNumberTextView: TextView
    private lateinit var mServiceAddressTextView: TextView
    private lateinit var mServicePhoneNumberTextView: TextView
    private lateinit var mServiceEmailTextView: TextView
    private lateinit var mKrwCurrencyTextView: TextView
    private lateinit var mPhpCurrencyTextView: TextView

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mItemRecyclerView: RecyclerView
    private lateinit var mRecommendRecyclerView: RecyclerView

    private lateinit var mViewPager: LoopingViewPager
    private lateinit var mSubBannerViewPager: LoopingViewPager

    private lateinit var mCompanyExpandableLayout: ExpandableLayout

    private lateinit var mEventPagerAdapter: EventPagerAdapter
    private lateinit var mSubBannerEventPagerAdapter: EventPagerAdapter
//    private lateinit var mFocusAdapter: FocusAdapter
//    private lateinit var mItemAdapter: ItemAdapter
    private lateinit var mRecommendAdapter: RecommendAdapter

    private lateinit var mMainMenuAdapter: MainMenuAdapter
    private lateinit var mItemMainMenuAdapter: MainMenuAdapter

    private lateinit var mFocusScrollBar: HorizontalScrollBarView
    private lateinit var mItemScrollBar: HorizontalScrollBarView

    private var mBannerSize = 0

    private var mFocusMainMenuListData = ArrayList<MainMenuItemData>()
    private var mSubItemListData = ArrayList<MainMenuItemData>()

    private lateinit var mRequestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity: Activity = requireActivity()
        if (activity is MainActivity) {
            mMainActivity = activity
        }

        mRecommendAdapter = RecommendAdapter(requireActivity())

        mMainMenuAdapter = MainMenuAdapter(requireActivity(), true)
        mItemMainMenuAdapter = MainMenuAdapter(requireActivity(), false)

        mRequestQueue = Volley.newRequestQueue(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContainer = inflater.inflate(R.layout.fragment_home, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Scroll View
        mScrollView = mContainer.fragment_home_scroll_view

        // View Pager
        mViewPager = mContainer.fragment_home_banner_view_pager
        mViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                mBannerEventCountTextView.text = (position + 1).toString() + "/" + mBannerSize.toString()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        val autoScroller = AutoScroller(mViewPager, lifecycle, 5000)
        autoScroller.isAutoScroll = true

        // Sub Banner View Pager
        mSubBannerViewPager = mContainer.fragment_home_sub_banner_view_pager
        mSubBannerViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                mBannerEventCountTextView.text = (position + 1).toString() + "/" + mBannerSize.toString()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        val subBannerAutoScroller = AutoScroller(mSubBannerViewPager, lifecycle, 5000)
        subBannerAutoScroller.isAutoScroll = true

        // Banner Event Count Text View
        mBannerEventCountTextView = mContainer.fragment_home_banner_count_text_view

        // Recycler View
        mRecyclerView = mContainer.fragment_home_focus_recycler_view
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        mMainMenuAdapter.setOnItemClickListener(object : MainMenuAdapter.OnItemClickListener {
            override fun onClick(data: MainMenuItemData) {
//                if (data.getCategoryUid() == COMPANY_TYPE_USED_MART) {
//                    showToast(requireActivity(), requireActivity().resources.getString(R.string.text_service_prepared))
//                } else {
//                    mMainActivity.showStoreListScreen(data)
//                }

                mMainActivity.showStoreListScreen(data)
            }
        })
        mRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mRecyclerView.adapter = mMainMenuAdapter

        mFocusScrollBar = mContainer.fragment_home_focus_scroll_bar_view
        mFocusScrollBar.setRecyclerView(mRecyclerView)





        mItemRecyclerView = mContainer.fragment_home_item_recycler_view
        mItemRecyclerView.setHasFixedSize(true)
        mItemRecyclerView.layoutManager = GridLayoutManager(
            requireActivity(),
            2,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        mItemMainMenuAdapter.setOnItemClickListener(object : MainMenuAdapter.OnItemClickListener {
            override fun onClick(data: MainMenuItemData) {
                mMainActivity.showStoreListScreen(data)

            }
        })
        mItemRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mItemRecyclerView.adapter = mItemMainMenuAdapter

        mItemScrollBar = mContainer.fragment_home_item_menu_scroll_bar_view
        mItemScrollBar.setRecyclerView(mItemRecyclerView)

        // KRW Currency Text View
        mKrwCurrencyTextView = mContainer.fragment_home_krw_currency_text_view

        // PHP Currency Text View
        mPhpCurrencyTextView = mContainer.fragment_home_php_currency_text_view


        // Exchange Rate View
        val exchangeRateView = mContainer.fragment_home_exchange_rate_view
        exchangeRateView.setOnClickListener {
            getExchangeRate(false)
        }




        // Recommend Recycler View
        mRecommendRecyclerView = mContainer.fragment_home_recommend_recycler_view
        mRecommendRecyclerView.setHasFixedSize(true)
        mRecommendRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        mRecommendAdapter.setOnRecommendClickListener(object : RecommendAdapter.OnRecommendClickListener {
            override fun onClick(companyUid: Int) {
                val intent = Intent(requireActivity(), StoreDetailActivity::class.java)
                intent.putExtra(NagajaActivity.INTENT_DATA_STORE_UID_DATA, companyUid)
                startActivity(intent)
            }

        })
        mRecommendRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mRecommendRecyclerView.adapter = mRecommendAdapter

        // Company Expandable View
        mCompanyExpandableLayout = mContainer.fragment_home_company_expandable_view
        mCompanyExpandableLayout.setOnClickListener {
            mCompanyExpandableLayout.toggleLayout()
        }
        mCompanyExpandableLayout.setOnExpandListener {
            if (!it) {
                mScrollView.post(Runnable { mScrollView.fullScroll(ScrollView.FOCUS_DOWN) })
            }
        }

        // Service Name Text View
        mServiceNameTextView = mContainer.expandable_layout_company_header_service_name_text_view

        // Service Master Text View
        mServiceMasterTextView = mContainer.expandable_layout_company_sub_master_text_view

        // Service License Number Text View
        mServiceLicenseNumberTextView = mContainer.expandable_layout_company_sub_service_license_number_text_view

        // Service Mail Order Business Number Text View
        mServiceMailOrderBusinessNumberTextView = mContainer.expandable_layout_company_sub_mail_order_business_number_text_view

        // Service Address Text View
        mServiceAddressTextView = mContainer.expandable_layout_company_sub_address_text_view

        // Service Phone Number Text View
        mServicePhoneNumberTextView = mContainer.expandable_layout_company_sub_service_phone_number_text_view

        // Service Email Text VIew
        mServiceEmailTextView = mContainer.expandable_layout_company_sub_service_email_text_view

        // Service Terms Text View
        val serviceTermsTextView = mContainer.expandable_layout_company_sub_terms_text_view
        serviceTermsTextView.setOnClickListener {
            showHtmlFile(SignUpFragment.AGREE_TYPE_TERMS)
        }

        // Service Terms Text View
        val servicePolicyTextView = mContainer.expandable_layout_company_sub_policy_text_view
        servicePolicyTextView.setOnClickListener {
            showHtmlFile(SignUpFragment.AGREE_TYPE_POLICY)
        }

        getBannerEvent(1)
        getMainMenuItem(1)
        getRecommendPlace()
        getServiceInformation()

        getExchangeRate(true)


        if (mMainActivity.mIsFirst) {
            mMainActivity.mIsFirst = false
            getEventData()
        }
    }

    private fun getAddress(lan: Double, lon: Double): String {
        var address = ""
        val geocoder = Geocoder(requireActivity(), Locale.ENGLISH)

        (requireActivity() as Activity).runOnUiThread {
            try {
                val addressList = geocoder.getFromLocation(lan, lon, 1)
                if (addressList!!.size != 0) {
                    address = addressList[0].getAddressLine(0)
                    if (address.substring(0, 4) == "대한민국") {
                        address = address.substring(5)
                    }
                }

                /*val nation = addressList!![0].countryName
                val adminArea = addressList[0].adminArea
                val locality = addressList[0].locality
                val subLocality = addressList[0].subLocality
                val thoroughfare = addressList[0].thoroughfare
                val subThoroughfare = addressList[0].subThoroughfare
                val featureName = addressList[0].featureName
                val address = addressList[0].getAddressLine(0).toString()

                NagajaLog().d("wooks, Nation (국가명) = $nation")
                NagajaLog().d("wooks, AdminArea (시) = $adminArea")
                NagajaLog().d("wooks, Locality (구 메인) = $locality")
                NagajaLog().d("wooks, SubLocality (구 서브) = $subLocality")
                NagajaLog().d("wooks, Thoroughfare (동) = $thoroughfare")
                NagajaLog().d("wooks, SubThoroughfare (번지) = $subThoroughfare")
                NagajaLog().d("wooks, FeatureName (번지) = $featureName")
                NagajaLog().d("wooks, Address (국가명 시 군 구 동 번지) = $address")*/

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return address
    }

    private fun getResId(resName: String?): Int {
        return try {
//            val idField: Field = HomeFragment::class.java.getDeclaredField(resName!!)
//            idField.getInt(idField)
            return requireActivity().resources.getIdentifier( resName, "string", null );
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    private fun displayBannerListData() {
        if (MAPP.MAIN_BANNER_EVENT_LIST_DATA.isNotEmpty()) {
            mBannerSize = MAPP.MAIN_BANNER_EVENT_LIST_DATA.size

            mViewPager.clipToPadding = false
//            mViewPager.setPadding(80, 0, 80, 0)
            mViewPager.setPadding(0, 0, 0, 0)
            mViewPager.pageMargin = 50
            mEventPagerAdapter = EventPagerAdapter(requireActivity(), MAPP.MAIN_BANNER_EVENT_LIST_DATA, null)
            mViewPager.adapter = mEventPagerAdapter
//            mViewPagerIndicator.setViewPager(mViewPager)
            mEventPagerAdapter.setOnEventPagerClickListener(object : EventPagerAdapter.OnEventPagerClickListener {
                override fun onClick(linkUrl: String?) {
                    if (TextUtils.isEmpty(linkUrl)) {
                        return
                    }
                    try {
                        val uri = Uri.parse(linkUrl)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        NagajaLog().e("wooks, You don't have any browser to open web page")
                    }
                }

                override fun onEventShow(categoryUid: Int, targetUid: Int) {
                    if (categoryUid > 0 && targetUid > 0) {
                        val mainMenuItemData = MainMenuItemData()
                        mainMenuItemData.setIsEvent(true)
                        mainMenuItemData.setCategoryUid(COMPANY_TYPE_NOTICE)
                        mainMenuItemData.setEventCategoryUid(categoryUid)
                        mainMenuItemData.setEventTargetUid(targetUid)

                        val intent = Intent(requireActivity(), NoticeActivity::class.java)
                        intent.putExtra(INTENT_DATA_STORE_LIST_DATA, mainMenuItemData)
                        startActivity(intent)
                    }
                }
            })
            mViewPager.adapter = mEventPagerAdapter
        }

        if (MAPP.SUB_BANNER_EVENT_LIST_DATA.isNotEmpty()) {
            if (!MAPP.SUB_BANNER_EVENT_LIST_DATA[0].getIsUseYn()) {
                mSubBannerViewPager.visibility = View.GONE
            } else {
                mSubBannerViewPager.clipToPadding = false
//            mViewPager.setPadding(80, 0, 80, 0)
                mSubBannerViewPager.setPadding(0, 0, 0, 0)
                mSubBannerViewPager.pageMargin = 50
                mSubBannerEventPagerAdapter = EventPagerAdapter(requireActivity(), MAPP.SUB_BANNER_EVENT_LIST_DATA, null)
                mSubBannerViewPager.adapter = mSubBannerEventPagerAdapter
                mSubBannerEventPagerAdapter.setOnEventPagerClickListener(object : EventPagerAdapter.OnEventPagerClickListener {
                    override fun onClick(linkUrl: String?) {
                        if (TextUtils.isEmpty(linkUrl)) {
                            return
                        }
                        try {
                            val uri = Uri.parse(linkUrl)
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            NagajaLog().e("wooks, You don't have any browser to open web page")
                        }
                    }

                    override fun onEventShow(categoryUid: Int, targetUid: Int) {
//                        TODO("Not yet implemented")
                    }
                })
                mSubBannerViewPager.adapter = mSubBannerEventPagerAdapter
            }
        }
    }

    private fun displayRecommendListData(listData: ArrayList<RecommendPlaceData>) {
        if (listData.isEmpty()) {
            return
        }

        mRecommendAdapter.setData(listData)
    }

    private fun displayMenuItemData() {

        if (mFocusMainMenuListData.isNotEmpty()) {
            mMainMenuAdapter.setData(mFocusMainMenuListData)
            mFocusScrollBar.visibility = View.VISIBLE
        }

        if (mSubItemListData.isNotEmpty()) {
            mItemMainMenuAdapter.setData(mSubItemListData)
            mItemScrollBar.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayServiceInformation(data: ServiceInformationData) {

        if (SharedPreferencesUtil().getSelectLanguage(requireActivity()) == SELECT_LANGUAGE_KO) {
            if (!TextUtils.isEmpty(data.getServiceName())) {
                mServiceNameTextView.text = data.getServiceName()
            }

            if (!TextUtils.isEmpty(data.getServiceMasterName())) {
                mServiceMasterTextView.text = data.getServiceMasterName()
            }

            if (!TextUtils.isEmpty(data.getServiceNumber())) {
                mServiceLicenseNumberTextView.text = data.getServiceNumber()
            }

            if (!TextUtils.isEmpty(data.getServiceNumberEx())) {
                mServiceMailOrderBusinessNumberTextView.text = data.getServiceNumberEx()
            }

            if (!TextUtils.isEmpty(data.getServiceAddress())) {
                mServiceAddressTextView.text = data.getServiceAddress()/* + " " + data.getServiceAddressDetail()*/
            }

            if (!TextUtils.isEmpty(data.getServicePhone())) {
                mServicePhoneNumberTextView.text = data.getServicePhone()
            }

            if (!TextUtils.isEmpty(data.getServiceEmail())) {
                mServiceEmailTextView.text = data.getServiceEmail()
            }

        } else {
            if (!TextUtils.isEmpty(data.getServiceNameEnglish())) {
                mServiceNameTextView.text = data.getServiceNameEnglish()
            }

            if (!TextUtils.isEmpty(data.getServiceMasterNameEnglish())) {
                mServiceMasterTextView.text = data.getServiceMasterNameEnglish()
            }

            if (!TextUtils.isEmpty(data.getServiceNumber())) {
                mServiceLicenseNumberTextView.text = data.getServiceNumber()
            }

            if (!TextUtils.isEmpty(data.getServiceNumberEx())) {
                mServiceMailOrderBusinessNumberTextView.text = data.getServiceNumberEx()
            }

            if (!TextUtils.isEmpty(data.getServiceAddressEnglish())) {
                mServiceAddressTextView.text = data.getServiceAddressEnglish()/* + " " + data.getServiceAddressDetailEnglish()*/
            }

            if (!TextUtils.isEmpty(data.getServicePhone())) {
                mServicePhoneNumberTextView.text = data.getServicePhone()
            }

            if (!TextUtils.isEmpty(data.getServiceEmail())) {
                mServiceEmailTextView.text = data.getServiceEmail()
            }
        }
    }

    private fun showHtmlFile(type: Int) {

        var webUrlLocal = ""
        when (type) {
            SignUpFragment.AGREE_TYPE_TERMS -> {
                webUrlLocal = "file:///android_asset/terms.html"
            }

            SignUpFragment.AGREE_TYPE_POLICY -> {
                webUrlLocal = "file:///android_asset/policy.html"
            }
        }

        mMainActivity.showTermsAndPolicyScreen(type, webUrlLocal)
    }

    private fun showEventPopup(imageUrl: String, eventLink: String) {
        val customEventDialog = CustomEventDialog(
            requireActivity(),
            imageUrl,
            eventLink
        )

        customEventDialog.setOnCustomEventDialogListener(object : CustomEventDialog.OnCustomEventDialogListener {
            override fun onCancel(isTodayClose: Boolean) {
                if (isTodayClose) {
                    showToast(requireActivity(), "// TODO: Implement API > Today Do Not Watch Again")
                }
                customEventDialog.dismiss()
            }

            override fun onConfirm(eventLink: String) {
                if (TextUtils.isEmpty(eventLink)) {
                    return
                }

                try {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(eventLink)
                    i.addCategory(Intent.CATEGORY_BROWSABLE)
                    if (requireActivity().intent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivity(i)
                    }
                } catch (e: ActivityNotFoundException) {
                    NagajaLog().e("wooks, You don't have any browser to open web page")
                }

                customEventDialog.dismiss()
            }
        })
        customEventDialog.show()
    }

    override fun onPause() {
        super.onPause()

        if (mCompanyExpandableLayout.isExpanded) {
            mCompanyExpandableLayout.collapse()
        }
    }

    override fun onResume() {
        super.onResume()

    }

    // =================================================================================
    // API
    // =================================================================================


    /**
     * API. Get Location Name
     */
    private fun getLocationName(location: String) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getLocationName(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    SharedPreferencesUtil().setLocationName(requireActivity(), resultData)

                    mMainActivity.displayCurrentLocation(resultData)
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
            SharedPreferencesUtil().getSelectLanguage(requireActivity())!!,
            location
        )
    }

    /**
     * API. Get Main Menu Item
     */
    private fun getMainMenuItem(groupId: Int) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getMainMenuItem(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<MainMenuItemData>> {
                override fun onSuccess(resultData: ArrayList<MainMenuItemData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    if (groupId == 1) {
                        mFocusMainMenuListData.clear()
                        mFocusMainMenuListData = resultData
                        getMainMenuItem(2)
                    } else if (groupId == 2) {
                        mSubItemListData.clear()
                        mSubItemListData = resultData

                        displayMenuItemData()
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
            groupId.toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Main Menu Item
     */
    private fun getRecommendPlace() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getRecommendPlace(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<RecommendPlaceData>> {
                override fun onSuccess(resultData: ArrayList<RecommendPlaceData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    displayRecommendListData(resultData)
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
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Banner Event
     */
    private fun getBannerEvent(groupId: Int) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBannerEvent(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<BannerEventData>> {
                override fun onSuccess(resultData: ArrayList<BannerEventData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    if (groupId == 1) {
                        MAPP.MAIN_BANNER_EVENT_LIST_DATA.clear()
                        MAPP.MAIN_BANNER_EVENT_LIST_DATA = resultData
                        getBannerEvent(2)
                    } else if (groupId == 2) {
                        MAPP.SUB_BANNER_EVENT_LIST_DATA.clear()
                        MAPP.SUB_BANNER_EVENT_LIST_DATA = resultData

                        displayBannerListData()
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
            groupId.toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Service Information
     */
    private fun getServiceInformation() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getServiceInformation(
            mRequestQueue,
            object : NagajaManager.RequestListener<ServiceInformationData> {
                override fun onSuccess(resultData: ServiceInformationData) {
                    displayServiceInformation(resultData)
                }

                override fun onFail(errorMsg: String?) {
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            }
        )
    }

    private fun getEventData() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getEventData(
            mRequestQueue,
            object : NagajaManager.RequestListener<EventData> {
                override fun onSuccess(resultData: EventData) {
                    val currentTime = System.currentTimeMillis()
                    val todayDate = Date(currentTime)
                    val date = SimpleDateFormat("dd")

                    Handler(Looper.getMainLooper()).postDelayed({
                        if (resultData.getPopupUseYn()) {
                            if (TextUtils.isEmpty(SharedPreferencesUtil().getDoNotWatchToday(requireActivity()))) {
                                showEventPopup(DEFAULT_IMAGE_DOMAIN + resultData.getPopupImageName(), resultData.getPopupTargetUrl()!!)
                            } else {
                                if((Integer.parseInt(date.format(todayDate)) - Integer.parseInt(SharedPreferencesUtil().getDoNotWatchToday(requireActivity()))) != 0) {
                                    showEventPopup(DEFAULT_IMAGE_DOMAIN + resultData.getPopupImageName(), resultData.getPopupTargetUrl()!!)
                                }
                            }
                        }
                    }, 3000)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().d("wooks, errorMsg = $errorMsg")
                    showToast(requireActivity(), requireActivity().resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                    showToast(requireActivity(), requireActivity().resources.getString(R.string.text_please_try_again_later))
                }

            },
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Exchagne Rate
     */
    private fun getExchangeRate(isHome: Boolean) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getExchangeRate(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<ExchangeRateData>> {
                @SuppressLint("SetTextI18n")
                override fun onSuccess(resultData: ArrayList<ExchangeRateData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    if (isHome) {
                        for (i in resultData.indices) {
                            if (resultData[i].getCurrencyCode() == "KRW") {
                                mKrwCurrencyTextView.text = resultData[i].getCurrencyNow().toString() + "￦"
                            } else if (resultData[i].getCurrencyCode() == "PHP") {
                                mPhpCurrencyTextView.text = resultData[i].getCurrencyNow().toString() + "₱"
                            }
                        }
                    } else {
                        val exchangeRateDialog = CustomExchangeRateDialog(requireActivity(), resultData)
                        exchangeRateDialog.show()
                    }
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().d("wooks, errorMsg = $errorMsg")
                    showToast(requireActivity(), requireActivity().resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            }
        )
    }
}