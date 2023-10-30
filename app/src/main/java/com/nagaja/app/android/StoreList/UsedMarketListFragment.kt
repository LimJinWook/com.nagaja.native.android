package com.nagaja.app.android.StoreList

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
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
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.*
import com.kenilt.loopingviewpager.scroller.AutoScroller
import com.kenilt.loopingviewpager.widget.LoopingViewPager
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Notice.NoticeActivity
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_used_market_list.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import java.util.*


class UsedMarketListFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mContainer: View
    private lateinit var mCategoryNextView: View
    private lateinit var mLoadingView: View
    private lateinit var mSearchEmptyView: View

    private lateinit var mSearchEditText: EditText

    private lateinit var mBannerCountTextView: TextView

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mCategoryRecyclerView: RecyclerView
    private lateinit var mMarketRecyclerView: RecyclerView

    private lateinit var mBannerViewPager: LoopingViewPager

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mEventPagerAdapter: EventPagerAdapter
    private lateinit var mStoreCategoryAdapter: StoreCategoryAdapter
    private lateinit var mUsedMarketAdapter: UsedMarketAdapter

    private lateinit var mListener: OnUsedMarketListFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private var mBannerSize = 0

    private var mPageCount = 1
    private var mCategoryUid = 0
    private var mItemTotalCount = 0
    private var mClickPosition = -1

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    private var mUsedMarketListData = ArrayList<UsedMarketData>()

    interface OnUsedMarketListFragmentListener {
        fun onFinish()
        fun onUsedMarketDetailScreen(itemUid: Int)
        fun onUsedMarketRegisterScreen()
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_STORE_ITEM_DATA        = "args_store_item_data"

        fun newInstance(data: MainMenuItemData): UsedMarketListFragment {
            val args = Bundle()
            val fragment = UsedMarketListFragment()
            args.putSerializable(ARGS_STORE_ITEM_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getStoreItemData(): MainMenuItemData? {
        val arguments = arguments
        return arguments?.getSerializable(ARGS_STORE_ITEM_DATA) as MainMenuItemData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mStoreCategoryAdapter = StoreCategoryAdapter(mActivity)
        mUsedMarketAdapter = UsedMarketAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_used_market_list, container, false)

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
            setShareUrl(mActivity, getStoreItemData()!!.getCategoryUid(), COMPANY_TYPE_MAIN)
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

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_used_market_list_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mCategoryUid = getStoreItemData()!!.getCategoryUid()
                mItemTotalCount = 0

                getStoreCategoryData(true)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Search Edit Text
        mSearchEditText = mContainer.fragment_used_market_list_search_edit_text
        mSearchEditText.filters = arrayOf(Util().blankSpaceFilter)
        mSearchEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        mSearchEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    mPageCount = 1

                    mIsFirst = true

                    mItemTotalCount = 0

                    getUsedMarketListData(true)
                    hideKeyboard(mActivity)
                    return true
                }
                return false
            }
        })

        // Search View
        val searchView = mContainer.fragment_used_market_list_search_view
        searchView.setOnClickListener {
            mPageCount = 1

            mIsFirst = true

            mItemTotalCount = 0

            getUsedMarketListData(true)

            hideKeyboard(mActivity)
        }


        // Category Recycler View
        mCategoryRecyclerView = mContainer.fragment_used_market_list_category_recycler_view
        mCategoryRecyclerView.setHasFixedSize(true)
        mCategoryRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        mStoreCategoryAdapter.setOnItemClickListener(object : StoreCategoryAdapter.OnItemClickListener {
            override fun onClick(data: StoreCategoryData, position: Int) {
                mStoreCategoryAdapter.setSelectCategory(position)

                mCategoryUid = data.getCategoryUid()

                mPageCount = 1

                mIsFirst = true

                mCategoryUid = data.getCategoryUid()
                mItemTotalCount = 0

                getUsedMarketListData(true)
            }
        })
        mCategoryRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mCategoryRecyclerView.adapter = mStoreCategoryAdapter

        // Category Next View
        mCategoryNextView = mContainer.fragment_used_market_list_category_next_view

        // Banner View Pager
        mBannerViewPager = mContainer.fragment_used_market_list_banner_view_pager
        mBannerViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                mBannerCountTextView.text = (position + 1).toString() + "/" + mBannerSize.toString()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        val autoScroller = AutoScroller(mBannerViewPager, lifecycle, 5000)
        autoScroller.isAutoScroll = true

        // Banner Count Text View
        mBannerCountTextView = mContainer.fragment_used_market_list_banner_count_text_view

        mScrollView = mContainer.fragment_used_market_list_scroll_view
        mScrollView.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (!v.canScrollVertically(1)) {
                if (!mIsEndOfScroll) {
                    if (mUsedMarketListData.size >= mItemTotalCount) {
                        return@OnScrollChangeListener
                    }

                    mIsEndOfScroll = true
                    getUsedMarketListData(false)
                }
            }
        })

        // Market Recycler View
        mMarketRecyclerView = mContainer.fragment_used_market_list_recycler_view
        mMarketRecyclerView.setHasFixedSize(true)
        mMarketRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mUsedMarketAdapter.setOnItemClickListener(object : UsedMarketAdapter.OnItemClickListener {
            override fun onClick(itemUid: Int, companyUid: Int, position: Int) {
                mListener.onUsedMarketDetailScreen(itemUid)
                mClickPosition = position
            }
        })
        mMarketRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mMarketRecyclerView.adapter = mUsedMarketAdapter

        // Loading View
        mLoadingView = mContainer.fragment_used_market_list_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_used_market_list_loading_lottie_view

        // Used Market Register Text View
        val usedMarketRegisterTextView = mContainer.fragment_used_market_list_register_text_view
        usedMarketRegisterTextView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                mListener.onUsedMarketRegisterScreen()
            }
        }

        // Search Empty View
        mSearchEmptyView = mContainer.fragment_used_market_empty_view




        mCategoryUid = getStoreItemData()!!.getCategoryUid()

        if (MAPP.MAIN_BANNER_EVENT_LIST_DATA.isNotEmpty()) {
            displayBannerListData()
        } else {
            getBannerEvent()
        }

        getStoreCategoryData(false)

        if ((getStoreItemData()!!.getShareBoardUid() > 0) && (getStoreItemData()!!.getShareBoardUid() < COMPANY_TYPE_MAIN) ) {
            mListener.onUsedMarketDetailScreen(getStoreItemData()!!.getShareBoardUid())
        }
    }

    private fun displayBannerListData() {
        mBannerSize = MAPP.MAIN_BANNER_EVENT_LIST_DATA.size

        mBannerViewPager.clipToPadding = false
        mBannerViewPager.setPadding(0, 0, 0, 0)
        mBannerViewPager.pageMargin = 50
        mEventPagerAdapter = EventPagerAdapter(requireActivity(), MAPP.MAIN_BANNER_EVENT_LIST_DATA, null)
        mBannerViewPager.adapter = mEventPagerAdapter
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
                    intent.putExtra(NagajaActivity.INTENT_DATA_STORE_LIST_DATA, mainMenuItemData)
                    startActivity(intent)
                }
            }
        })
        mBannerViewPager.adapter = mEventPagerAdapter
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

//        CoroutineScope(Dispatchers.Main).launch {
//            if (isLoading) {
//                mLoadingView.visibility = View.VISIBLE
//                mLoadingLottieView.speed = 2f
//                mLoadingLottieView.playAnimation()
//            } else {
//                Handler(Looper.getMainLooper()).postDelayed({
//                    mLoadingView.visibility = View.GONE
//                    mLoadingLottieView.pauseAnimation()
//                }, 500)
//            }
//        }
    }

    fun deleteItem(position: Int) {
        mUsedMarketAdapter.deleteItem(position)
        mUsedMarketListData.removeAt(position)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        if (mClickPosition > -1) {
            getUsedMarketDetailData(mUsedMarketListData[mClickPosition].getItemUid(), mClickPosition)
            mClickPosition = -1
        }
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
        if (context is OnUsedMarketListFragmentListener) {
            mActivity = context as Activity

            if (context is OnUsedMarketListFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnUsedMarketListFragmentListener"
                )
            }
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        if (context is OnUsedMarketListFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnUsedMarketListFragmentListener"
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
     * API. Get Store Category Data
     */
    fun getStoreCategoryData(isRefresh: Boolean) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreCategoryData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<StoreCategoryData>> {
                override fun onSuccess(resultData: ArrayList<StoreCategoryData>) {

                    mIsFirst = false

                    if (resultData.isEmpty()) {
                        return
                    }

                    val storeCategoryData = StoreCategoryData()
                    storeCategoryData.setCategoryUid(getStoreItemData()!!.getCategoryUid())
                    storeCategoryData.setCategoryDepth(2)
                    storeCategoryData.setCategorySort(1)
                    storeCategoryData.setCategoryName(mActivity.resources.getString(R.string.text_all))
                    storeCategoryData.setCategoryNameEnglish("All")
                    storeCategoryData.setCategoryUri("/free")
                    storeCategoryData.setIsUseYn(true)
                    storeCategoryData.setCreateDate("")
                    storeCategoryData.setParentCategoryUid(getStoreItemData()!!.getCategoryUid())
                    storeCategoryData.setIsSelect(true)

                    resultData.add(0, storeCategoryData)

                    mStoreCategoryAdapter.setData(resultData)

                    mCategoryNextView.visibility = if (resultData.size > 4) View.VISIBLE else View.GONE

                    getUsedMarketListData(isRefresh)
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
            getStoreItemData()!!.getCategoryUid().toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Banner Event
     */
    private fun getBannerEvent() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBannerEvent(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<BannerEventData>> {
                override fun onSuccess(resultData: ArrayList<BannerEventData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    MAPP.MAIN_BANNER_EVENT_LIST_DATA = resultData

                    displayBannerListData()
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
            "1",
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Used Market List Data
     */
    private fun getUsedMarketListData(isRefresh: Boolean) {

        if (isRefresh) {
            mPageCount = 1
            mScrollView.post(Runnable { mScrollView.fullScroll(ScrollView.FOCUS_UP) })
        }

        setLoadingView(true)

        val latitude = SharedPreferencesUtil().getSaveLocation(requireActivity())!!.substring(0, SharedPreferencesUtil().getSaveLocation(requireActivity())!!.indexOf(","))
        val longitude = SharedPreferencesUtil().getSaveLocation(requireActivity())!!.substring(SharedPreferencesUtil().getSaveLocation(requireActivity())!!.indexOf(",")
                + 1, SharedPreferencesUtil().getSaveLocation(requireActivity())!!.length)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getUsedMarketListData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<UsedMarketData>> {
                override fun onSuccess(resultData: ArrayList<UsedMarketData>) {

                    mSwipeRefreshLayout.isRefreshing = false

                    setLoadingView(false)

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mUsedMarketAdapter.setData(resultData)

                        mMarketRecyclerView.visibility = View.GONE
                        mSearchEmptyView.visibility = View.VISIBLE

                        return
                    } else {
                        mMarketRecyclerView.visibility = View.VISIBLE
                        mSearchEmptyView.visibility = View.GONE
                    }

                    if (isRefresh) {
                        mUsedMarketListData.clear()
                        mUsedMarketListData = resultData

                        mIsFirst = false
                    } else {
                        if (mUsedMarketListData.size == 0) {
                            mUsedMarketListData = resultData
                        } else {
                            mUsedMarketListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mUsedMarketListData[0].getTotalCount()

                    mIsEndOfScroll = false
                    mUsedMarketAdapter.setData(mUsedMarketListData)

                    mPageCount++
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    mSwipeRefreshLayout.isRefreshing = false
                    setLoadingView(false)
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                    mSwipeRefreshLayout.isRefreshing = false
                    setLoadingView(false)
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mPageCount,
            20,
            if (TextUtils.isEmpty(mSearchEditText.text)) 0 else 1,
            mSearchEditText.text.toString(),
            MAPP.SELECT_LANGUAGE_CODE,
            mCategoryUid,
            latitude,
            longitude,
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity)
        )
    }

    /**
     * API. Get Review Recommend Save
     */
    private fun getUsedMarketDetailData(itemUid: Int, position: Int) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getUsedMarketDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<UsedMarketData> {
                override fun onSuccess(resultData: UsedMarketData) {
                    mUsedMarketListData[position] = resultData
                    mUsedMarketAdapter.replaceItem(resultData, position)
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
            itemUid,
            MAPP.SELECT_LANGUAGE_CODE
        )
    }
}