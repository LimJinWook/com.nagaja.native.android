package com.nagaja.app.android.StoreList

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
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
import android.widget.TextView
import androidx.core.content.ContextCompat
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
import com.nagaja.app.android.Base.NagajaActivity.Companion.SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION
import com.nagaja.app.android.Base.NagajaActivity.Companion.SELECT_HEADER_MENU_TYPE_NOTIFICATION
import com.nagaja.app.android.Base.NagajaActivity.Companion.SELECT_HEADER_MENU_TYPE_SEARCH
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Notice.NoticeActivity
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_select_map.view.*
import kotlinx.android.synthetic.main.fragment_store_list.view.*
import kotlinx.android.synthetic.main.fragment_used_market_list.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.util.*


class StoreListFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mContainer: View
    private lateinit var mSearchLayoutView: View
    private lateinit var mCategoryNextView: View
    private lateinit var mLoadingView: View
    private lateinit var mCategoryView: View
    private lateinit var mMartView: View
    private lateinit var mKoreanMartUnderLineView: View
    private lateinit var mLocalMartUnderLineView: View
    private lateinit var mEmptyListView: View

    private lateinit var mSearchEditText: EditText

    private lateinit var mLocationTextView: TextView
    private lateinit var mBannerCountTextView: TextView
    private lateinit var mKoreanMartTextView: TextView
    private lateinit var mLocalMartTextView: TextView

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mCategoryRecyclerView: RecyclerView
    private lateinit var mStoreRecyclerView: RecyclerView

    private lateinit var mCategoryMainSpinner: PowerSpinnerView
    private lateinit var mCategorySubSpinner: PowerSpinnerView

    private lateinit var mBannerViewPager: LoopingViewPager

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mEventPagerAdapter: EventPagerAdapter
    private lateinit var mStoreCategoryAdapter: StoreCategoryAdapter
    private lateinit var mStoreAdapter: StoreAdapter

    private lateinit var mListener: OnStoreListFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private var mBannerSize = 0

    private var mPageCount = 1
    private var mCategoryUid = 0
    private var mMainType = 0
    private var mSubType = 0
    private var mStoreTotalCount = 0

    private var mSelectMartPosition = 0x01

    private var mCurrentLocationName = ""

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    private var mStoreListData = ArrayList<StoreData>()
    private var mStoreCategoryListData = ArrayList<StoreCategoryData>()

    interface OnStoreListFragmentListener {
        fun onFinish()
        fun onLocationScreen()
        fun onStoreDetailScreen(companyUid: Int)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_STORE_ITEM_DATA        = "args_store_item_data"

        const val SELECT_KOREAN_MART          = 0x01
        const val SELECT_LOCAL_MART           = 0x02

        fun newInstance(data: MainMenuItemData): StoreListFragment {
            val args = Bundle()
            val fragment = StoreListFragment()
            args.putSerializable(ARGS_STORE_ITEM_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getStoreItemData(): MainMenuItemData {
        val arguments = arguments
        return arguments?.getSerializable(ARGS_STORE_ITEM_DATA) as MainMenuItemData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mStoreCategoryAdapter = StoreCategoryAdapter(mActivity)
        mStoreAdapter = StoreAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_store_list, container, false)

        init()

        return mContainer
    }

    @SuppressLint("SetTextI18n")
    private fun init() {

        // Header Location View
        val headerLocationView = mContainer.layout_header_location_view
        headerLocationView.setOnSingleClickListener {
            mListener.onChangeLocationAndNotificationScreen(SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION)
        }

        // Header Location Text View
        mLocationTextView = mContainer.layout_header_location_text_view
        if (!TextUtils.isEmpty(MAPP.SELECT_NATION_NAME)) {
            mLocationTextView.text = MAPP.SELECT_NATION_NAME
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
            mListener.onChangeLocationAndNotificationScreen(SELECT_HEADER_MENU_TYPE_SEARCH)
        }

        // Header Share Image View
        val headerShareImageView = mContainer.layout_header_clipboard_image_view
        headerShareImageView.setOnSingleClickListener {
            setShareUrl(mActivity, getStoreItemData().getCategoryUid(), COMPANY_TYPE_MAIN)
        }

        // Header Notification Image View
        val headerNotificationImageView = mContainer.layout_header_notification_image_view
        headerNotificationImageView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                mListener.onChangeLocationAndNotificationScreen(SELECT_HEADER_MENU_TYPE_NOTIFICATION)
            }
        }

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_store_list_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(mActivity)) {
                if (getStoreItemData().getCategoryUid() == COMPANY_TYPE_USED_MART) {
                    setMartTab(mSelectMartPosition)
                } else {
                    mIsEndOfScroll = true
                    mPageCount = 1

                    mIsFirst = true

                    mCategoryUid = getStoreItemData()!!.getCategoryUid()
                    mMainType = 0
                    mSubType = 0
                    mStoreTotalCount = 0

                    mCategoryMainSpinner.selectItemByIndex(0)
                    mCategorySubSpinner.selectItemByIndex(0)

                    getStoreCategoryData(true)
                }
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Search Layout View
        mSearchLayoutView = mContainer.fragment_store_list_search_layout_view

        // Search Edit Text
        mSearchEditText = mContainer.fragment_store_list_search_edit_text
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

                    mMainType = 0
                    mSubType = 0
                    mStoreTotalCount = 0

                    mCategoryMainSpinner.selectItemByIndex(0)
                    mCategorySubSpinner.selectItemByIndex(0)

                    getStoreListData(true)
                    hideKeyboard(mActivity)
                    return true
                }
                return false
            }
        })

        // Search View
        val searchView = mContainer.fragment_store_list_search_view
        searchView.setOnClickListener {
            mPageCount = 1

            mIsFirst = true

            mMainType = 0
            mSubType = 0
            mStoreTotalCount = 0

            mCategoryMainSpinner.selectItemByIndex(0)
            mCategorySubSpinner.selectItemByIndex(0)

            getStoreListData(true)

            hideKeyboard(mActivity)
        }

        // Category View
        mCategoryView = mContainer.fragment_store_list_category_view
        mCategoryView.visibility = if (getStoreItemData().getCategoryUid() == COMPANY_TYPE_USED_MART) View.GONE else View.VISIBLE

        // Category Recycler View
        mCategoryRecyclerView = mContainer.fragment_store_list_category_recycler_view
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
                mMainType = 0
                mSubType = 0
                mStoreTotalCount = 0

                mCategoryMainSpinner.selectItemByIndex(0)
                mCategorySubSpinner.selectItemByIndex(0)

                getStoreListData(true)
            }
        })
        mCategoryRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mCategoryRecyclerView.adapter = mStoreCategoryAdapter

        // Category Next View
        mCategoryNextView = mContainer.fragment_store_list_category_next_view

        // Mart View
        mMartView = mContainer.fragment_store_list_mart_view
        mMartView.visibility = if (getStoreItemData().getCategoryUid() == COMPANY_TYPE_USED_MART) View.VISIBLE else View.GONE
        if (getStoreItemData().getCategoryUid() == COMPANY_TYPE_USED_MART) {
            // Korean Mart Text View
            mKoreanMartTextView = mContainer.fragment_store_list_korean_mart_text_view
            mKoreanMartTextView.setOnClickListener {
                setMartTab(SELECT_KOREAN_MART)
            }

            // Korean Mart Under Line View
            mKoreanMartUnderLineView = mContainer.fragment_store_list_korean_mart_under_line_view

            // Local Mart Text View
            mLocalMartTextView = mContainer.fragment_store_list_local_mart_text_view
            mLocalMartTextView.setOnClickListener {
                setMartTab(SELECT_LOCAL_MART)
            }

            // Local Mart Under Line View
            mLocalMartUnderLineView = mContainer.fragment_store_list_local_mart_under_line_view

        }

        // Banner View Pager
        mBannerViewPager = mContainer.fragment_store_list_banner_view_pager
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
        mBannerCountTextView = mContainer.fragment_store_list_banner_count_text_view

        // Category Main Spinner
        mCategoryMainSpinner = mContainer.fragment_store_list_category_main_spinner
        mCategoryMainSpinner.selectItemByIndex(0)
        mCategoryMainSpinner.visibility = if (getStoreItemData().getCategoryUid() == COMPANY_TYPE_USED_MART) View.GONE else View.VISIBLE
        mCategoryMainSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->

            if (mIsFirst) {
                return@OnSpinnerItemSelectedListener
            }

            mMainType = newIndex

            getStoreListData(true)
        })

        // Category Sub Spinner
        mCategorySubSpinner = mContainer.fragment_store_list_category_sub_spinner
        mCategorySubSpinner.selectItemByIndex(0)
        mCategorySubSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->

            if (mIsFirst) {
                return@OnSpinnerItemSelectedListener
            }

            mSubType = newIndex

            getStoreListData(true)
        })

        mScrollView = mContainer.fragment_store_list_scroll_view
        mScrollView.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (!v.canScrollVertically(1)) {
                if (!mIsEndOfScroll) {
                    if (mStoreListData.size >= mStoreTotalCount) {
                        return@OnScrollChangeListener
                    }

                    mIsEndOfScroll = true
                    getStoreListData(false)
                }
            }
        })

        // Store Recycler View
        mStoreRecyclerView = mContainer.fragment_store_list_recycler_view
        mStoreRecyclerView.setHasFixedSize(true)
        mStoreRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        mStoreAdapter.setOnItemClickListener(object : StoreAdapter.OnItemClickListener {
            override fun onClick(data: StoreData, position: Int) {
                mListener.onStoreDetailScreen(data.getCompanyUid())
            }

            override fun onRecommend(position: Int, companyUid: Int, isLike: Boolean) {
                if (MAPP.IS_NON_MEMBER_SERVICE) {
                    showLoginGuideCustomDialog()
                } else {
                    getStoreRecommendSave(position, companyUid, isLike)
                }
            }

            override fun onRegular(position: Int, companyUid: Int, isRegular: Boolean) {
                if (MAPP.IS_NON_MEMBER_SERVICE) {
                    showLoginGuideCustomDialog()
                } else {
                    getStoreRegularSave(position, companyUid, isRegular)
                }
            }
        })
        mStoreRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mStoreRecyclerView.adapter = mStoreAdapter

        // Loading View
        mLoadingView = mContainer.fragment_store_list_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_store_list_loading_lottie_view

        // Empty List View
        mEmptyListView = mContainer.fragment_store_list_empty_view








        mCategoryUid = getStoreItemData()!!.getCategoryUid()


        if (MAPP.MAIN_BANNER_EVENT_LIST_DATA.isNotEmpty()) {
            displayBannerListData()
        } else {
            getBannerEvent()
        }


        if (mCategoryUid == COMPANY_TYPE_HOSPITAL) {
            mSearchLayoutView.visibility = View.GONE
            mCategoryMainSpinner.visibility = View.GONE
        }

        getStoreCategoryData(false)

        if ((getStoreItemData().getShareBoardUid() > 0) && (getStoreItemData().getShareBoardUid() < COMPANY_TYPE_MAIN) ) {
            mListener.onStoreDetailScreen(getStoreItemData().getShareBoardUid())
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

    private fun setMartTab(selectType: Int) {

        when (selectType) {
            SELECT_KOREAN_MART -> {
                mKoreanMartTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mKoreanMartTextView.setTypeface(mKoreanMartTextView.typeface, Typeface.BOLD)
                mKoreanMartUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mLocalMartTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mLocalMartTextView.setTypeface(mLocalMartTextView.typeface, Typeface.NORMAL)
                mLocalMartUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))
            }

            SELECT_LOCAL_MART -> {
                mKoreanMartTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mKoreanMartTextView.setTypeface(mKoreanMartTextView.typeface, Typeface.NORMAL)
                mKoreanMartUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mLocalMartTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mLocalMartTextView.setTypeface(mLocalMartTextView.typeface, Typeface.BOLD)
                mLocalMartUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
            }
        }

        if (selectType == SELECT_KOREAN_MART) {
            mCategoryUid = mStoreCategoryListData[0].getCategoryUid()
        } else {
            mCategoryUid = mStoreCategoryListData[1].getCategoryUid()
        }

        mSelectMartPosition = selectType

        mPageCount = 1

        mIsFirst = true

        mMainType = 0
        mSubType = 0
        mStoreTotalCount = 0

        mCategoryMainSpinner.selectItemByIndex(0)
        mCategorySubSpinner.selectItemByIndex(0)

        getStoreListData(true)

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
            }, 1000)
        }
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

    fun spinnerCheck(): Boolean {
        var isSpinnerShow = false

        if (mCategoryMainSpinner.isShowing) {
            mCategoryMainSpinner.dismiss()

            isSpinnerShow = true
        }

        if (mCategorySubSpinner.isShowing) {
            mCategorySubSpinner.dismiss()

            isSpinnerShow = true
        }

        return isSpinnerShow
    }

    fun changeLocation() {
        if (!TextUtils.isEmpty(MAPP.SELECT_NATION_NAME)) {
            mLocationTextView.text = MAPP.SELECT_NATION_NAME
            mCurrentLocationName = mLocationTextView.text.toString()
        }

        getStoreListData(true)
    }

    fun onItemUpdate(companyUid: Int, isStatus: Boolean) {
        NagajaLog().e("wooks, companyUid = $companyUid")
        NagajaLog().e("wooks, isStatus = $isStatus")

        if (mStoreListData.isNotEmpty()) {
            for (i in mStoreListData.indices) {
                if (mStoreListData[i].getCompanyUid() == companyUid) {
                    mStoreListData[i].setIsRecommendUseYn(isStatus)
                    mStoreAdapter.notifyItemChanged(i)
                    break
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        if (mCategoryMainSpinner.isShowing) {
            mCategoryMainSpinner.dismiss()
        }

        if (mCategorySubSpinner.isShowing) {
            mCategorySubSpinner.dismiss()
        }
    }

//    override fun onResume() {
//        super.onResume()
//
//        if (null != mLocationTextView) {
//            if (!TextUtils.isEmpty(MAPP.SELECT_NATION_NAME)) {
//                mLocationTextView.text = MAPP.SELECT_NATION_NAME
//                mCurrentLocationName = mLocationTextView.text.toString()
//            }
//        }
//    }

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
        if (context is OnStoreListFragmentListener) {
            mActivity = context as Activity

            if (context is OnStoreListFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnStoreListFragmentListener"
                )
            }
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        if (context is OnStoreListFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnStoreListFragmentListener"
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
    private fun getStoreCategoryData(isRefresh: Boolean) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreCategoryData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<StoreCategoryData>> {
                override fun onSuccess(resultData: ArrayList<StoreCategoryData>) {

                    mIsFirst = false

                    if (resultData.isEmpty()) {
                        return
                    }

                    if (getStoreItemData().getCategoryUid() == COMPANY_TYPE_USED_MART) {
                        mCategoryUid = resultData[0].getCategoryUid()
                        mStoreCategoryListData = resultData
                    } else {
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
                    }

                    getStoreListData(isRefresh)
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
     * API. Get Store List Data
     */
    private fun getStoreListData(isRefresh: Boolean) {

        if (isRefresh) {
            mPageCount = 1
        }

        setLoadingView(true)

        val latitude = SharedPreferencesUtil().getSaveLocation(requireActivity())!!.substring(0, SharedPreferencesUtil().getSaveLocation(requireActivity())!!.indexOf(","))
        val longitude = SharedPreferencesUtil().getSaveLocation(requireActivity())!!.substring(SharedPreferencesUtil().getSaveLocation(requireActivity())!!.indexOf(",")
                + 1, SharedPreferencesUtil().getSaveLocation(requireActivity())!!.length)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreListData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<StoreData>> {
                override fun onSuccess(resultData: ArrayList<StoreData>) {

                    mSwipeRefreshLayout.isRefreshing = false

                    setLoadingView(false)

                    if (resultData.isEmpty()) {
                        mStoreTotalCount = 0
                        mStoreAdapter.setData(resultData)

                        mStoreRecyclerView.visibility = View.GONE
                        mEmptyListView.visibility = View.VISIBLE

                        return
                    } else {
                        mStoreRecyclerView.visibility = View.VISIBLE
                        mEmptyListView.visibility = View.GONE
                    }

                    if (isRefresh) {
                        mStoreListData.clear()
                        mStoreListData = resultData

                        mIsFirst = false
                    } else {
                        if (mStoreListData.size == 0) {
                            mStoreListData = resultData
                        } else {
                            mStoreListData.addAll(resultData)
                        }
                    }

                    mStoreTotalCount = mStoreListData[0].getTotalCount()

                    mIsEndOfScroll = false
                    mStoreAdapter.setData(mStoreListData)

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
            mMainType,
            mSubType,
            MAPP.SELECT_LANGUAGE_CODE,
            mCategoryUid,
            latitude,
            longitude,
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity)
        )
    }

    /**
     * API. Get Store List Data
     */
    private fun getStoreRecommendSave(position: Int, companyUid: Int, isLike: Boolean) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreRecommendSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    mStoreAdapter.setSelectData(position, true)
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
            companyUid,
            if (isLike) 1 else 0
        )
    }

    /**
     * API. Get Store List Data
     */
    private fun getStoreRegularSave(position: Int, companyUid: Int, isRegular: Boolean) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreRegularSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    mStoreAdapter.setSelectData(position, false)
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
            companyUid,
            if (isRegular) 1 else 0
        )
    }
}