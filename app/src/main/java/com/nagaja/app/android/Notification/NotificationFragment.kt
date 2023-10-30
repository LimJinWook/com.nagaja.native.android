package com.nagaja.app.android.Notification

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.Build
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
import com.nagaja.app.android.Adapter.NotificationAdapter
import com.nagaja.app.android.Adapter.NotificationAdapter.Companion.NOTICE_TYPE_CHAT
import com.nagaja.app.android.Adapter.NotificationAdapter.Companion.NOTICE_TYPE_EVENT
import com.nagaja.app.android.Adapter.NotificationAdapter.Companion.NOTICE_TYPE_NOTE
import com.nagaja.app.android.Adapter.NotificationAdapter.Companion.NOTICE_TYPE_NOTICE
import com.nagaja.app.android.Adapter.NotificationAdapter.Companion.NOTICE_TYPE_RESERVATION
import com.nagaja.app.android.Adapter.NotificationAdapter.Companion.NOTICE_TYPE_SERVER_CHECK
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.NotificationData
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_notification.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class NotificationFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mContainer: View
    private lateinit var mAllUnderLineView: View
    private lateinit var mUnidentifiedUnderLineView: View
    private lateinit var mLoadingView: View

    private lateinit var mLocationTextView: TextView
    private lateinit var mAllTextView: TextView
    private lateinit var mUnidentifiedTextView: TextView

    private lateinit var mNotificationRecyclerView: RecyclerView
    private lateinit var mNotificationAdapter: NotificationAdapter

    private lateinit var mListener: OnNoticeFragmentListener

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mRequestQueue: RequestQueue

    private var mNotificationListData = ArrayList<NotificationData>()

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    private var mPageCount = 1
    private var mItemTotalCount = 0
    private var mSelectType = 0

    interface OnNoticeFragmentListener {
        fun onFinish()
        fun onNoteScreen()
        fun onReservationScreen()
        fun onChatScreen(boardUid: Int)
        fun onChangeLocationAndNotificationScreen(isChangeLocation : Boolean)
        fun onPointHistoryScreen(companyUid: Int)
    }

    companion object {

        const val SELECT_TYPE_ALL                          = 0
        const val SELECT_TYPE_UNIDENTIFIED                 = 1

        fun newInstance(): NotificationFragment {
            val args = Bundle()
            val fragment = NotificationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mNotificationAdapter = NotificationAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_notification, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Header Location View
        val headerLocationView = mContainer.layout_header_location_view
        headerLocationView.setOnSingleClickListener {
            mListener.onChangeLocationAndNotificationScreen(true)
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
        headerSearchImageView.visibility = View.GONE

        // Header Share Image View
        val headerShareImageView = mContainer.layout_header_clipboard_image_view
        headerShareImageView.visibility = View.GONE

        // Header Notification Image View
        val headerNotificationImageView = mContainer.layout_header_notification_image_view

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_noti)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_notification_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mItemTotalCount = 0

                getNotificationList(true)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // All Text View
        mAllTextView = mContainer.fragment_notification_all_text_view
        mAllTextView.setOnClickListener {
            setTabView(SELECT_TYPE_ALL)
        }

        // All Under Line View
        mAllUnderLineView = mContainer.fragment_notification_all_under_line_view

        // Unidentified Text View
        mUnidentifiedTextView = mContainer.fragment_notification_unidentified_text_view
        mUnidentifiedTextView.setOnClickListener {
            setTabView(SELECT_TYPE_UNIDENTIFIED)
        }

        // Unidentified Under Line View
        mUnidentifiedUnderLineView = mContainer.fragment_notification_unidentified_under_line_view

        // Notification Recycler View
        mNotificationRecyclerView = mContainer.fragment_notification_recycler_view
        mNotificationRecyclerView.setHasFixedSize(true)
        mNotificationRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mNotificationRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!mNotificationRecyclerView.canScrollVertically(1) && !mNotificationRecyclerView.canScrollVertically(-1)) {
                    return
                }

                if (!mNotificationRecyclerView.canScrollVertically(1)) {
                    if (!mIsEndOfScroll) {
                        if (mNotificationListData.size >= mItemTotalCount) {
                            return
                        }

                        mIsEndOfScroll = true
                        getNotificationList(false)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        mNotificationAdapter.setOnItemClickListener(object : NotificationAdapter.OnItemClickListener {
            override fun onClick(data: NotificationData, position: Int) {
                getNotificationConfirm(data, position)
            }
        })
        mNotificationRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mNotificationRecyclerView.adapter = mNotificationAdapter



        // Loading View
        mLoadingView = mContainer.fragment_notification_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_notification_loading_lottie_view



        mSelectType = SELECT_TYPE_ALL
        getNotificationList(true)
    }

    private fun setTabView(type: Int) {

        when (type) {
            SELECT_TYPE_ALL -> {
                mAllTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mAllTextView.setTypeface(mAllTextView.typeface, Typeface.BOLD)
                mAllUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mUnidentifiedTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mUnidentifiedTextView.setTypeface(mUnidentifiedTextView.typeface, Typeface.NORMAL)
                mUnidentifiedUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

            }

            SELECT_TYPE_UNIDENTIFIED -> {
                mAllTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mAllTextView.setTypeface(mAllTextView.typeface, Typeface.NORMAL)
                mAllUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mUnidentifiedTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mUnidentifiedTextView.setTypeface(mUnidentifiedTextView.typeface, Typeface.BOLD)
                mUnidentifiedUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
            }
        }

        mPageCount = 1
        mSelectType = type

        getNotificationList(true)

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

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        if (null != mLocationTextView) {
            if (!TextUtils.isEmpty(MAPP.SELECT_NATION_NAME)) {
                mLocationTextView.text = MAPP.SELECT_NATION_NAME
            }
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
        if (context is OnNoticeFragmentListener) {
            mActivity = context as Activity

            if (context is OnNoticeFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnNoticeFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnNoticeFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnNoticeFragmentListener"
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
     * API. Get Notification List
     */
    private fun getNotificationList(isRefresh: Boolean) {

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNotificationList(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<NotificationData>> {
                override fun onSuccess(resultData: ArrayList<NotificationData>) {

                    mSwipeRefreshLayout.isRefreshing = false

                    setLoadingView(false)

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mNotificationAdapter.setData(resultData)
                        return
                    }

                    if (isRefresh) {
                        mNotificationListData.clear()
                        mNotificationListData = resultData

                        mIsFirst = false
                    } else {
                        if (mNotificationListData.size == 0) {
                            mNotificationListData = resultData
                        } else {
                            mNotificationListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mNotificationListData[0].getTotalCount()

                    NagajaLog().d("wooks, mItemTotalCount = $mItemTotalCount")

                    mIsEndOfScroll = false
                    mNotificationAdapter.setData(mNotificationListData)

                    mPageCount++
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    setLoadingView(false)
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                    setLoadingView(false)
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mPageCount,
            50,
            mSelectType
        )
    }

    /**
     * API. Get Notification Confirm
     */
    private fun getNotificationConfirm(data: NotificationData, position: Int) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNotificationConfirm(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    when (data.getPushTypeUid()) {
                        NOTICE_TYPE_RESERVATION -> {
                            mListener.onReservationScreen()
                        }

                        NOTICE_TYPE_CHAT -> {
                            mListener.onChatScreen(data.getFirstTargetUid())
                        }

                        NOTICE_TYPE_NOTICE -> {

                        }

                        NOTICE_TYPE_EVENT -> {

                        }

                        NOTICE_TYPE_SERVER_CHECK -> {

                        }

                        NOTICE_TYPE_NOTE -> {
                            mListener.onNoteScreen()
                        }

                    }

                    mNotificationListData[position].setPushStatus(1)
                    mNotificationAdapter.setItemConfirm(position)


                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    setLoadingView(false)
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                    setLoadingView(false)
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            data.getPushUid()
        )
    }
}