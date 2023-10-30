package com.nagaja.app.android.Notice

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
import com.nagaja.app.android.Adapter.NoticeAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.NoticeData
import com.nagaja.app.android.Data.StoreCategoryData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Notice.NoticeActivity.Companion.mMainMenuItemData
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_notice.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class NoticeFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View
    private lateinit var mNoticeUnderLineView: View
    private lateinit var mEventUnderLineView: View
    private lateinit var mEmptyListView: View

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mTotalCountTextView: TextView
    private lateinit var mNoticeTextView: TextView
    private lateinit var mEventTextView: TextView

    private lateinit var mSortTypeSpinner: PowerSpinnerView

    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mNoticeAdapter: NoticeAdapter

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mListener: OnNoticeFragmentListener

    private var mNoticeListData = ArrayList<NoticeData>()
    private var mCategoryListData = ArrayList<StoreCategoryData>()

    private lateinit var mRequestQueue: RequestQueue

    private var mIsFirst = true
    private var mIsEndOfScroll = false
    private var mIsInit = false

    private var mPageCount = 1
    private var mItemTotalCount = 0
    private var mSortType = 0

    private var mSelectCategoryUid = 0

    interface OnNoticeFragmentListener {
        fun onFinish()
        fun onNoticeDetailScreen(data: NoticeData)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val SELECT_NOTICE_TYPE_NOTICE                  = 0x01
        const val SELECT_NOTICE_TYPE_EVENT                   = 0x02

        const val NOTICE_UID                                 = 247
        const val EVENT_UID                                  = 248

        const val ARGS_CATEGORY_UID_DATA                     = "args_category_uid_data"
        const val ARGS_CATEGORY_IS_EVENT_DATA                = "args_category_is_event_data"
        const val ARGS_CATEGORY_EVENT_CATEGORY_UID_DATA      = "args_category_event_category_uid_data"
        const val ARGS_CATEGORY_EVENT_TARGET_UID_DATA        = "args_category_event_target_uid_data"

        fun newInstance(categoryUid: Int, isEvent: Boolean, eventCategoryUid: Int, eventTargetUid: Int): NoticeFragment {
            val args = Bundle()
            val fragment = NoticeFragment()
            args.putInt(ARGS_CATEGORY_UID_DATA, categoryUid)
            args.putBoolean(ARGS_CATEGORY_IS_EVENT_DATA, isEvent)
            args.putInt(ARGS_CATEGORY_EVENT_CATEGORY_UID_DATA, eventCategoryUid)
            args.putInt(ARGS_CATEGORY_EVENT_TARGET_UID_DATA, eventTargetUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getCategoryUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_CATEGORY_UID_DATA) as Int
    }

    private fun getIsEvent(): Boolean {
        val arguments = arguments
        return arguments?.getBoolean(ARGS_CATEGORY_IS_EVENT_DATA) as Boolean
    }

    private fun getEventCategoryUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_CATEGORY_EVENT_CATEGORY_UID_DATA) as Int
    }

    private fun getEventTargetUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_CATEGORY_EVENT_TARGET_UID_DATA) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mNoticeAdapter = NoticeAdapter(mActivity, false)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_notice, container, false)

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
            setShareUrl(mActivity, COMPANY_TYPE_NOTICE, COMPANY_TYPE_MAIN)
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
        titleTextView.text = mActivity.resources.getString(R.string.text_notice)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_notice_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mItemTotalCount = 0

                getNoticeList(true, false)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Notice Text View
        mNoticeTextView = mContainer.fragment_notice_text_view
        mNoticeTextView.setOnClickListener {
            setTabView(SELECT_NOTICE_TYPE_NOTICE, false)
        }

        // Notice Under Line View
        mNoticeUnderLineView = mContainer.fragment_notice_under_line_view

        // Event Text View
        mEventTextView = mContainer.fragment_notice_event_text_view
        mEventTextView.setOnClickListener {
            setTabView(SELECT_NOTICE_TYPE_EVENT, false)
        }

        // Event Under Line View
        mEventUnderLineView = mContainer.fragment_notice_event_under_line_view

        // Total Count Text View
        mTotalCountTextView = mContainer.fragment_notice_total_count_text_view

        // Sort Type Spinner
        mSortTypeSpinner = mContainer.fragment_notice_sort_type_spinner
        mSortTypeSpinner.selectItemByIndex(0)
        mSortTypeSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->

            if (!mIsInit) {
                mIsInit = true
                return@OnSpinnerItemSelectedListener
            }

            mSortType = newIndex

            mIsEndOfScroll = true
            mPageCount = 1
            mItemTotalCount = 0

            mIsFirst = true

            getNoticeList(true, false)
            mRecyclerView.scrollToPosition(0)
        })

        // Recycler View
        mRecyclerView = mContainer.fragment_notice_recycler_view
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

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
                        getNoticeList(false, false)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        mNoticeAdapter.setOnNewsItemClickListener(object : NoticeAdapter.OnNewsItemClickListener {
            override fun onClick(itemUid: Int) {
            }

            override fun onClick(data: NoticeData) {
                mListener.onNoticeDetailScreen(data)
            }
        })
        mRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mRecyclerView.adapter = mNoticeAdapter

        // Loading View
        mLoadingView = mContainer.fragment_notice_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_notice_loading_lottie_view

        // Empty List View
        mEmptyListView = mContainer.fragment_notice_list_empty_view



        getStoreCategoryData()



//        if (mIsShare) {
            if ((mMainMenuItemData.getShareBoardUid() > 0) && (mMainMenuItemData.getShareBoardUid() < COMPANY_TYPE_MAIN) ) {
                getNoticeDetail(mMainMenuItemData.getShareBoardUid())
            }
//            mIsShare = false
//        }

    }

    private fun setTabView(selectType: Int, isEvent: Boolean) {

        when (selectType) {
            SELECT_NOTICE_TYPE_NOTICE -> {
                mNoticeTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_0d4d97))
                mNoticeTextView.setTypeface(mNoticeTextView.typeface, Typeface.BOLD)
                mNoticeUnderLineView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_0d4d97))

                mEventTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.text_color_bbbbbb))
                mEventTextView.setTypeface(mEventTextView.typeface, Typeface.NORMAL)
                mEventUnderLineView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_e2e7ee))

                mSelectCategoryUid = mCategoryListData[0].getCategoryUid()
            }

            SELECT_NOTICE_TYPE_EVENT -> {
                mNoticeTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.text_color_bbbbbb))
                mNoticeTextView.setTypeface(mNoticeTextView.typeface, Typeface.NORMAL)
                mNoticeUnderLineView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_e2e7ee))

                mEventTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_0d4d97))
                mEventTextView.setTypeface(mEventTextView.typeface, Typeface.BOLD)
                mEventUnderLineView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_0d4d97))

                mSelectCategoryUid = mCategoryListData[1].getCategoryUid()
            }
        }

        mIsEndOfScroll = true
        mPageCount = 1
        mIsFirst = true
        mItemTotalCount = 0

        getNoticeList(true, isEvent)
    }

    private fun eventCheck() {
        if (getIsEvent()) {
            when (getEventCategoryUid()) {
                NOTICE_UID -> {
                    setTabView(SELECT_NOTICE_TYPE_NOTICE, true)
                }

                EVENT_UID -> {
                    setTabView(SELECT_NOTICE_TYPE_EVENT, true)
                }
            }
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
     * API. Get Store Category Data
     */
    fun getStoreCategoryData() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreCategoryData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<StoreCategoryData>> {
                override fun onSuccess(resultData: ArrayList<StoreCategoryData>) {

                    if (resultData.isEmpty()) {
                        return
                    }

                    mCategoryListData = resultData
                    mSelectCategoryUid = mCategoryListData[0].getCategoryUid()

                    if (getIsEvent()) {
                        eventCheck()
                    } else {
                        getNoticeList(true, false)
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
            getCategoryUid().toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Notice List
     */
    private fun getNoticeList(isRefresh: Boolean, isEvent: Boolean) {

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
                        mNoticeAdapter.setData(resultData)

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
                    mNoticeAdapter.setData(mNoticeListData)

                    mPageCount++

                    if (isEvent && getEventTargetUid() > 0) {
                        getNoticeDetail(getEventTargetUid())
                    }
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
//                    setLoadingView(false)
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
//                    setLoadingView(false)
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mPageCount,
            20,
            "",
            mSortType,
            mSelectCategoryUid,
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Local News Detail
     */
    private fun getNoticeDetail(boardUid: Int) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoticeDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<NoticeData> {
                override fun onSuccess(resultData: NoticeData) {
                    NagajaLog().e("wooks, m,zxcnnxcnzxc,nc,zxnzxc,nzxcm,nzxcm,n")
                    mListener.onNoticeDetailScreen(resultData)
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
            boardUid
        )
    }
}