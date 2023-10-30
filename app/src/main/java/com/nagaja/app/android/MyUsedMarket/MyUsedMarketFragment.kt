package com.nagaja.app.android.MyUsedMarket

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.*
import com.nagaja.app.android.Adapter.UsedMarketAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.UsedMarketData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_my_used_market.view.*
import kotlinx.android.synthetic.main.fragment_used_market_list.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.util.*


class MyUsedMarketFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View
    private lateinit var mEmptyListView: View

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mCategorySpinner: PowerSpinnerView

    private lateinit var mMarketRecyclerView: RecyclerView

    private lateinit var mUsedMarketAdapter: UsedMarketAdapter

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mListener: OnMyUsedMarketListener

    private lateinit var mRequestQueue: RequestQueue

    private var mUsedMarketListData = ArrayList<UsedMarketData>()

    private var mSelectCategoryType = 99
    private var mPageCount = 1
    private var mItemTotalCount = 0

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    interface OnMyUsedMarketListener {
        fun onFinish()
        fun onUsedMarketDetailScreen(itemUid: Int)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val CATEGORY_TYPE_ALL                     = 99
        const val CATEGORY_TYPE_REGISTER                = 1
        const val CATEGORY_TYPE_ON_SALE                 = 7
        const val CATEGORY_TYPE_SALES_COMPLETE          = 9

        fun newInstance(): MyUsedMarketFragment {
            val args = Bundle()
            val fragment = MyUsedMarketFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mUsedMarketAdapter = UsedMarketAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_my_used_market, container, false)

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
        titleTextView.text = mActivity.resources.getString(R.string.text_secondhand_sales)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_my_used_market_list_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mSelectCategoryType = CATEGORY_TYPE_ALL
                mItemTotalCount = 0

                getMyUsedMarketList(true)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Category Spinner
        mCategorySpinner = mContainer.fragment_my_used_market_category_spinner
        mCategorySpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->

            when (newIndex) {
                0 -> {
                    mSelectCategoryType = CATEGORY_TYPE_ALL
                }

                1 -> {
                    mSelectCategoryType = CATEGORY_TYPE_REGISTER
                }

                2 -> {
                    mSelectCategoryType = CATEGORY_TYPE_ON_SALE
                }

                3 -> {
                    mSelectCategoryType = CATEGORY_TYPE_SALES_COMPLETE
                }
            }

            mPageCount = 1
            getMyUsedMarketList(true)
        })

        // Market Recycler View
        mMarketRecyclerView = mContainer.fragment_my_used_market_recycler_view
        mMarketRecyclerView.setHasFixedSize(true)
        mMarketRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mMarketRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!mMarketRecyclerView.canScrollVertically(1) && !mMarketRecyclerView.canScrollVertically(-1)) {
                    return
                }

                if (!mMarketRecyclerView.canScrollVertically(1)) {
                    if (!mIsEndOfScroll) {
                        if (mUsedMarketListData.size >= mItemTotalCount) {
                            return
                        }

                        mIsEndOfScroll = true
                        getMyUsedMarketList(false)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        mUsedMarketAdapter.setOnItemClickListener(object : UsedMarketAdapter.OnItemClickListener {
            override fun onClick(itemUid: Int, companyUid: Int, position: Int) {
                mListener.onUsedMarketDetailScreen(itemUid)
            }
        })
        mMarketRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mMarketRecyclerView.adapter = mUsedMarketAdapter


        // Loading View
        mLoadingView = mContainer.fragment_my_used_market_list_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_my_used_market_list_loading_lottie_view

        // Empty List View
        mEmptyListView = mContainer.fragment_my_used_market_list_empty_view




        mCategorySpinner.selectItemByIndex(0)
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
        if (context is OnMyUsedMarketListener) {
            mActivity = context as Activity

            if (context is OnMyUsedMarketListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnMyUsedMarketListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnMyUsedMarketListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnMyUsedMarketListener"
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
     * API. Get Regular List
     */
    fun getMyUsedMarketList(isRefresh: Boolean) {

        if (isRefresh) {
            mPageCount = 1
        }

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getMyUsedMarketList(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<UsedMarketData>> {
                override fun onSuccess(resultData: ArrayList<UsedMarketData>) {

                    mSwipeRefreshLayout.isRefreshing = false

                    setLoadingView(false)

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mUsedMarketAdapter.setData(resultData)

                        mMarketRecyclerView.visibility = View.GONE
                        mEmptyListView.visibility = View.VISIBLE

                        return
                    } else {
                        mMarketRecyclerView.visibility = View.VISIBLE
                        mEmptyListView.visibility = View.GONE
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
                    setLoadingView(false)
                    mSwipeRefreshLayout.isRefreshing = false
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                    setLoadingView(false)
                    mSwipeRefreshLayout.isRefreshing = false
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mPageCount,
            10,
            MAPP.SELECT_LANGUAGE_CODE,
            mSelectCategoryType
        )
    }
}