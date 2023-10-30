package com.nagaja.app.android.News

import android.annotation.SuppressLint
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
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.*
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.MyUsedMarket.MyUsedMarketFragment
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.News.NewsActivity.Companion.mMainMenuItemData
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.fragment_store.view.*
import kotlinx.android.synthetic.main.fragment_store_list.view.*
import kotlinx.android.synthetic.main.fragment_used_market.view.*
import kotlinx.android.synthetic.main.fragment_used_market_detail.view.*
import kotlinx.android.synthetic.main.fragment_used_market_list.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_store_menu_view.view.*
import kotlinx.android.synthetic.main.layout_store_review_view.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.util.*


class NewsFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View
    private lateinit var mEmptyListView: View

    private lateinit var mTotalCountTextView: TextView

    private lateinit var mSortTypeSpinner: PowerSpinnerView

    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mNewsAdapter: NoticeAdapter

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mListener: OnNewsFragmentListener

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mRequestQueue: RequestQueue

    private var mNoticeListData = ArrayList<NoticeData>()

    private var mPageCount = 1
    private var mItemTotalCount = 0
    private var mSortType = 0

    private var mIsFirst = true
    private var mIsEndOfScroll = false
    private var mIsShare = true

    interface OnNewsFragmentListener {
        fun onFinish()
        fun onNewsDetailScreen(itemUid: Int)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_CATEGORY_UID_DATA                = "args_category_uid_data"

        fun newInstance(categoryUid: Int): NewsFragment {
            val args = Bundle()
            val fragment = NewsFragment()
            args.putInt(ARGS_CATEGORY_UID_DATA, categoryUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getCategoryUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_CATEGORY_UID_DATA) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mNewsAdapter = NoticeAdapter(mActivity, true)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_news, container, false)

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
            setShareUrl(mActivity, COMPANY_TYPE_NEWS, COMPANY_TYPE_MAIN)
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
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_news)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_news_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1
                mItemTotalCount = 0

                mIsFirst = true

                getLocalNews(true)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Total Count Text View
        mTotalCountTextView = mContainer.fragment_news_total_count_text_view

        // Sort Type Spinner
        mSortTypeSpinner = mContainer.fragment_news_sort_type_spinner
        mSortTypeSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mSortType = newIndex

            mIsEndOfScroll = true
            mPageCount = 1
            mItemTotalCount = 0

            mIsFirst = true

            getLocalNews(true)
            mRecyclerView.scrollToPosition(0)
        })

        // Recycler View
        mRecyclerView = mContainer.fragment_news_recycler_view
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!mRecyclerView.canScrollVertically(1) && !mRecyclerView.canScrollVertically(-1)) {
                    return
                }

                if (!mRecyclerView.canScrollVertically(1)) {
                    if (!mIsEndOfScroll) {
                        if (mNoticeListData.size >= mItemTotalCount) {
                            return
                        }

                        mIsEndOfScroll = true
                        getLocalNews(false)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })


        mNewsAdapter.setOnNewsItemClickListener(object : NoticeAdapter.OnNewsItemClickListener {
            override fun onClick(itemUid: Int) {
                mListener.onNewsDetailScreen(itemUid)
            }

            override fun onClick(data: NoticeData) {
            }
        })
        mRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mRecyclerView.adapter = mNewsAdapter

        // Loading View
        mLoadingView = mContainer.fragment_news_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_news_loading_lottie_view

        // Empty List View
        mEmptyListView = mContainer.fragment_news_list_empty_view



        mSortTypeSpinner.selectItemByIndex(0)




        if (mIsShare) {
            if ((mMainMenuItemData.getShareBoardUid() > 0) && (mMainMenuItemData.getShareBoardUid() < COMPANY_TYPE_MAIN) ) {
                mListener.onNewsDetailScreen(mMainMenuItemData.getShareBoardUid())
            }
            mIsShare = false
        }

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

        if (mSortTypeSpinner.isShowing) {
            mSortTypeSpinner.dismiss()
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
        if (context is OnNewsFragmentListener) {
            mActivity = context as Activity

            if (context is OnNewsFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnNewsFragmentListener"
                )
            }
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        if (context is OnNewsFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnNewsFragmentListener"
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
     * API. Get Local News
     */
    private fun getLocalNews(isRefresh: Boolean) {

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoticeList(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<NoticeData>> {
                override fun onSuccess(resultData: ArrayList<NoticeData>) {

                    mSwipeRefreshLayout.isRefreshing = false

                    setLoadingView(false)

                    mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), resultData[0].getTotalCount().toString())

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mNewsAdapter.setData(resultData)

                        mRecyclerView.visibility = View.GONE
                        mEmptyListView.visibility = View.VISIBLE

                        return
                    } else {
                        mRecyclerView.visibility = View.VISIBLE
                        mEmptyListView.visibility = View.GONE
                    }

                    if (isRefresh) {
                        mNoticeListData.clear()
                        mNoticeListData = resultData

                        mIsFirst = false
                    } else {
                        if (mNoticeListData.size == 0) {
                            mNoticeListData = resultData
                        } else {
                            mNoticeListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mNoticeListData[0].getTotalCount()

                    mIsEndOfScroll = false
                    mNewsAdapter.setData(mNoticeListData)

                    mPageCount++

                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    setLoadingView(false)
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                    setLoadingView(false)
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mPageCount,
            10,
            "",
            mSortType,
            getCategoryUid(),
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }
}