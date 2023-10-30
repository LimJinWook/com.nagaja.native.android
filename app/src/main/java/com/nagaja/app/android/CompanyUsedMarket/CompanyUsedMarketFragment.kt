package com.nagaja.app.android.CompanyUsedMarket

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Adapter.UsedMarketAdapter
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_company_reservation.view.*
import kotlinx.android.synthetic.main.fragment_company_used_market.view.*
import kotlinx.android.synthetic.main.fragment_faq.view.*
import kotlinx.android.synthetic.main.fragment_full_screen_image.view.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.fragment_reservation.view.*
import kotlinx.android.synthetic.main.fragment_store_list.view.*
import kotlinx.android.synthetic.main.fragment_used_market_list.view.*
import kotlinx.android.synthetic.main.layout_reservation_information.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class CompanyUsedMarketFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View
    private lateinit var mEmptyListView: View

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mCategorySpinner: PowerSpinnerView

    private lateinit var mMarketRecyclerView: RecyclerView
    private lateinit var mUsedMarketAdapter: UsedMarketAdapter

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mListener: OnCompanyUsedMarketFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private var mUsedMarketListData = java.util.ArrayList<UsedMarketData>()

    private var mPageCount = 1
    private var mItemTotalCount = 0
    private var mSelectType = 99

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    interface OnCompanyUsedMarketFragmentListener {
        fun onFinish()
        fun onUsedMarketDetailScreen(itemUid: Int)
        fun onUsedMarketRegisterScreen(companyUid: Int)
    }

    companion object {

        const val SELECT_TYPE_ALL                          = 99
        const val SELECT_TYPE_REGISTER                     = 1
        const val SELECT_TYPE_ON_SALE                      = 7
        const val SELECT_TYPE_SALES_COMPLETE               = 9


        const val ARGS_COMPANY_DEFAULT_DATA                = "args_company_default_data"

        fun newInstance(data: CompanyDefaultData): CompanyUsedMarketFragment {
            val args = Bundle()
            val fragment = CompanyUsedMarketFragment()
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

        mUsedMarketAdapter = UsedMarketAdapter(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContainer = inflater.inflate(R.layout.fragment_company_used_market, container, false)

        init()

        return mContainer
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    private fun init() {

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_company_secondhand_sales)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_company_used_market_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mItemTotalCount = 0

                getCompanyUsedMarketList(true)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Category Sub Spinner
        mCategorySpinner = mContainer.fragment_company_used_market_category_spinner
        mCategorySpinner.selectItemByIndex(0)
        mCategorySpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->

            if (mIsFirst) {
                return@OnSpinnerItemSelectedListener
            }

            when (newIndex) {
                0 -> {
                    mSelectType = SELECT_TYPE_ALL
                }

                1 -> {
                    mSelectType = SELECT_TYPE_REGISTER
                }

                2 -> {
                    mSelectType = SELECT_TYPE_ON_SALE
                }

                3 -> {
                    mSelectType = SELECT_TYPE_SALES_COMPLETE
                }
            }

            getCompanyUsedMarketList(true)
        })

        // Market Recycler View
        mMarketRecyclerView = mContainer.fragment_company_used_market_item_recycler_view
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
                        getCompanyUsedMarketList(false)
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

        // Register Text View
        val registerTextView = mContainer.fragment_company_used_market_register_text_view
        registerTextView.setOnClickListener {
            mListener.onUsedMarketRegisterScreen(getCompanyDefaultData().getCompanyUid())
        }

        // Empty List View
        mEmptyListView = mContainer.fragment_company_used_market_list_empty_view





        // Loading View
        mLoadingView = mContainer.fragment_company_used_market_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_company_used_market_loading_lottie_view




        getCompanyUsedMarketList(true)
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
        if (context is OnCompanyUsedMarketFragmentListener) {
            mActivity = context as Activity

            if (context is OnCompanyUsedMarketFragmentListener) {
                mListener = context;
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnCompanyUsedMarketFragmentListener"
                );
            }
        }
    }

    /*
         * Called when the fragment attaches to the context
         */
    protected fun onAttachToContext(context: Context) {
        if (context is OnCompanyUsedMarketFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnCompanyUsedMarketFragmentListener"
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
     * API. Get Used Market List Data
     */
    fun getCompanyUsedMarketList(isRefresh: Boolean) {

        if (isRefresh) {
            mPageCount = 1
        }

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyUsedMarketList(
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
            getCompanyDefaultData().getCompanyUid(),
            MAPP.SELECT_LANGUAGE_CODE,
            mSelectType
        )
    }
}