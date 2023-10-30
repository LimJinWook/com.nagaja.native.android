package com.nagaja.app.android.Job

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
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
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Adapter.JobAdapter
import com.nagaja.app.android.Adapter.StoreCategoryAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Job.JobActivity.Companion.mMainMenuItemData
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_faq.view.*
import kotlinx.android.synthetic.main.fragment_job.view.*
import kotlinx.android.synthetic.main.fragment_missing.view.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.fragment_note.view.*
import kotlinx.android.synthetic.main.fragment_regular.view.*
import kotlinx.android.synthetic.main.fragment_setting_profile.view.*
import kotlinx.android.synthetic.main.fragment_store_list.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class JobFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mContainer: View
    private lateinit var mJobUnderLineView: View
    private lateinit var mJobSearchUnderLineView: View
    private lateinit var mSearchConfirmView: View
    private lateinit var mCategoryNextView: View
    private lateinit var mLoadingView: View
    private lateinit var mSearchView: View
    private lateinit var mEmptyListView: View

    private lateinit var mSearchEditText: EditText

    private lateinit var mJobTextView: TextView
    private lateinit var mJobSearchTextView: TextView
    private lateinit var mJobAndRecruitingTextView: TextView
    private lateinit var mTotalCountTextView: TextView

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mSortTypeSpinner: PowerSpinnerView

    private lateinit var mCategoryRecyclerView: RecyclerView
    private lateinit var mJobRecyclerView: RecyclerView

    private lateinit var mCategoryAdapter: StoreCategoryAdapter
    private lateinit var mJobAdapter: JobAdapter


    private var mJobListData = ArrayList<BoardData>()

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mListener: OnJobFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private var mSelectType = 40

    private var mIsPause = false
    private var mIsFirst = true
    private var mIsEndOfScroll = false
    private var mIsResumeSetting = false
    private var mIsShare = true

    private var mPageCount = 1
    private var mCategoryUid = 0
    private var mItemTotalCount = 0
    private var mSortType = 0

    interface OnJobFragmentListener {
        fun onFinish()
        fun onJobDetailScreen(boardUid: Int)
        fun onJobRegister(jobType: Int)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {
        const val SELECT_TYPE_JOB                        = 40
        const val SELECT_TYPE_JOB_SEARCH                 = 41

        const val ARGS_CATEGORY_UID_DATA                 = "args_category_uid_data"
        const val ARGS_IS_MY_JOB_DATA                    = "args_is_my_job_data"

        fun newInstance(categoryUid: Int, isMyJob: Boolean): JobFragment {
            val args = Bundle()
            val fragment = JobFragment()
            args.putInt(ARGS_CATEGORY_UID_DATA, categoryUid)
            args.putBoolean(ARGS_IS_MY_JOB_DATA, isMyJob)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getCategoryUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_CATEGORY_UID_DATA) as Int
    }

    private fun getIsMyJob(): Boolean {
        val arguments = arguments
        return arguments?.getBoolean(ARGS_IS_MY_JOB_DATA) as Boolean
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCategoryAdapter = StoreCategoryAdapter(mActivity)
        mJobAdapter = JobAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_job, container, false)

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
        headerShareImageView.visibility = if (getIsMyJob()) View.GONE else View.VISIBLE
        headerShareImageView.setOnSingleClickListener {
            setShareUrl(mActivity, COMPANY_TYPE_JOB_AND_JOB_SEARCH, COMPANY_TYPE_MAIN)
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
        titleTextView.text = mActivity.resources.getString(R.string.text_job_and_job_search)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Scroll View
        mScrollView = mContainer.fragment_job_scroll_view
        mScrollView.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (!v.canScrollVertically(1)) {
                if (!mIsEndOfScroll) {
                    if (mJobListData.size >= mItemTotalCount) {
                        return@OnScrollChangeListener
                    }

                    mIsEndOfScroll = true
                    getBoardList(mCategoryUid, false)
                }
            }
        })

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_job_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mItemTotalCount = 0

                mSelectType = SELECT_TYPE_JOB
                mCategoryUid = SELECT_TYPE_JOB
                mCategoryAdapter.setSelectCategory(0)
                mJobRecyclerView.scrollToPosition(0)
                setTabView(SELECT_TYPE_JOB, true)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Jon Text View
        mJobTextView = mContainer.fragment_job_text_view
        mJobTextView.setOnSingleClickListener {
            setTabView(SELECT_TYPE_JOB, false)
        }

        // Job Under-Line View
        mJobUnderLineView = mContainer.fragment_job_under_line_view

        // Job Search Text View
        mJobSearchTextView = mContainer.fragment_job_search_text_view
        mJobSearchTextView.setOnSingleClickListener {
            setTabView(SELECT_TYPE_JOB_SEARCH, false)
        }

        // Job Search Under-Line View
        mJobSearchUnderLineView = mContainer.fragment_job_search_under_line_view

        // Category Recycler View
        mCategoryRecyclerView = mContainer.fragment_job_category_recycler_view
        mCategoryRecyclerView.setHasFixedSize(true)
        mCategoryRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        mCategoryAdapter.setOnItemClickListener(object : StoreCategoryAdapter.OnItemClickListener {
            override fun onClick(data: StoreCategoryData, position: Int) {
                mCategoryAdapter.setSelectCategory(position)

                mCategoryUid = data.getCategoryUid()
                NagajaLog().e("wooks, mCategoryUid = $mCategoryUid")
                mPageCount = 1
                mIsFirst = true
                mItemTotalCount = 0

                getBoardList(mCategoryUid, true)
            }
        })
        mCategoryRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mCategoryRecyclerView.adapter = mCategoryAdapter

        // Category Next View
        mCategoryNextView = mContainer.fragment_job_category_next_view

        // Search View
        mSearchView = mContainer.fragment_job_search_view
        if (getIsMyJob()) {
            mSearchView.visibility = View.GONE
        }

        // Search Edit Text
        mSearchEditText = mContainer.fragment_job_search_edit_text
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
                    mIsEndOfScroll = true
                    mPageCount = 1
                    mItemTotalCount = 0

                    mIsFirst = true

                    getBoardList(mCategoryUid, true)
                    mJobRecyclerView.scrollToPosition(0)

                    hideKeyboard(mActivity)
                    return true
                }
                return false
            }
        })

        // Search Confirm View
        mSearchConfirmView = mContainer.fragment_job_search_confirm_view
        mSearchConfirmView.setOnSingleClickListener {
            mIsEndOfScroll = true
            mPageCount = 1
            mItemTotalCount = 0

            mIsFirst = true

            getBoardList(mCategoryUid, true)
            mJobRecyclerView.scrollToPosition(0)

            hideKeyboard(mActivity)
        }

        // Total Count Text View
        mTotalCountTextView = mContainer.fragment_job_total_count_text_view

        // Sort Type Spinner
        mSortTypeSpinner = mContainer.fragment_job_sort_type_spinner
        mSortTypeSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mSortType = newIndex

            mIsEndOfScroll = true
            mPageCount = 1
            mItemTotalCount = 0

            mIsFirst = true

            if (!mIsResumeSetting) {
                mCategoryAdapter.setSelectCategory(0)
                getStoreCategoryData(true)
            } else {
                getBoardList(mCategoryUid, true)
            }
            mJobRecyclerView.scrollToPosition(0)
        })

        // Job Recycler View
        mJobRecyclerView = mContainer.fragment_job_recycler_view
        mJobRecyclerView.setHasFixedSize(true)
        mJobRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mJobAdapter.setOnItemClickListener(object : JobAdapter.OnItemClickListener {
            override fun onClick(data: BoardData) {
                mListener.onJobDetailScreen(data.getBoardUid())
            }
        })
        mJobRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mJobRecyclerView.adapter = mJobAdapter

        // Job And Recruiting Text View
        mJobAndRecruitingTextView = mContainer.fragment_job_recruiting_text_view
        mJobAndRecruitingTextView.setOnClickListener {
            mListener.onJobRegister(mSelectType)
        }



        // Loading View
        mLoadingView = mContainer.fragment_job_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_job_loading_lottie_view

        // Empty List View
        mEmptyListView = mContainer.fragment_job_list_empty_view



        if (mIsPause) {
            mIsPause = false
            setTabView(mSelectType, false)
        } else {
            setTabView(SELECT_TYPE_JOB, true)
        }


        if (mIsShare) {
            if ((mMainMenuItemData.getShareBoardUid() > 0) && (mMainMenuItemData.getShareBoardUid() < COMPANY_TYPE_MAIN) ) {
                mListener.onJobDetailScreen(mMainMenuItemData.getShareBoardUid())
            }
            mIsShare = false
        }

    }

    private fun setTabView(selectType: Int, isFirstTime: Boolean) {

        when (selectType) {
            SELECT_TYPE_JOB -> {
                mJobTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mJobTextView.setTypeface(mJobTextView.typeface, Typeface.BOLD)
                mJobUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mJobSearchTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mJobSearchTextView.setTypeface(mJobSearchTextView.typeface, Typeface.NORMAL)
                mJobSearchUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mJobAndRecruitingTextView.text = mActivity.resources.getString(R.string.text_recruiting)
            }

            SELECT_TYPE_JOB_SEARCH -> {
                mJobTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mJobTextView.setTypeface(mJobTextView.typeface, Typeface.NORMAL)
                mJobUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mJobSearchTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mJobSearchTextView.setTypeface(mJobSearchTextView.typeface, Typeface.BOLD)
                mJobSearchUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mJobAndRecruitingTextView.text = mActivity.resources.getString(R.string.text_search_job)
            }
        }

        mPageCount = 1
        mSelectType = selectType

        if (isFirstTime) {
            mSortTypeSpinner.selectItemByIndex(0)
        } else {
            mCategoryAdapter.setSelectCategory(0)
            getStoreCategoryData(true)
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

        mIsPause = true

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
        if (context is OnJobFragmentListener) {
            mActivity = context as Activity

            if (context is OnJobFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnJobFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnJobFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnJobFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    // ==============================================================================================================================
    // API
    // ==============================================================================================================================

    /**
     * API. Get Store Category Data
     */
    fun getStoreCategoryData(isRefresh: Boolean) {

        if (isRefresh) {
            if (mSelectType == SELECT_TYPE_JOB) {
                mCategoryUid = SELECT_TYPE_JOB
            } else {
                mCategoryUid = SELECT_TYPE_JOB_SEARCH
            }
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreCategoryData(
            mRequestQueue,
            object : NagajaManager.RequestListener<java.util.ArrayList<StoreCategoryData>> {
                override fun onSuccess(resultData: java.util.ArrayList<StoreCategoryData>) {

                    mIsFirst = false
                    mIsResumeSetting = true

//                    if (resultData.isEmpty()) {
//                        return
//                    }


                    val storeCategoryData = StoreCategoryData()
                    storeCategoryData.setCategoryUid(mCategoryUid)
                    storeCategoryData.setCategoryDepth(2)
                    storeCategoryData.setCategorySort(1)
                    storeCategoryData.setCategoryName(mActivity.resources.getString(R.string.text_all))
                    storeCategoryData.setCategoryNameEnglish("All")
                    storeCategoryData.setCategoryUri("/free")
                    storeCategoryData.setIsUseYn(true)
                    storeCategoryData.setCreateDate("")
                    storeCategoryData.setParentCategoryUid(getCategoryUid())
                    storeCategoryData.setIsSelect(true)

                    resultData.add(0, storeCategoryData)

                    mCategoryAdapter.setData(resultData)

                    mCategoryNextView.visibility = if (resultData.size > 4) View.VISIBLE else View.GONE

                    getBoardList(mCategoryUid, isRefresh)
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
            mCategoryUid.toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * Get Board List
     * */
    private fun getBoardList(boardType: Int, isRefresh: Boolean) {

        if (getIsMyJob()) {
            getMyJobAndMissingList(boardType, isRefresh)
            return
        }

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardList(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<BoardData>> {
                override fun onSuccess(resultData: ArrayList<BoardData>) {

                    mSwipeRefreshLayout.isRefreshing = false

                    setLoadingView(false)

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mJobAdapter.setData(resultData)

                        mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), "0")

                        mJobRecyclerView.visibility = View.GONE
                        mEmptyListView.visibility = View.VISIBLE

                        return
                    } else {
                        mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), resultData[0].getTotalCount().toString())

                        mJobRecyclerView.visibility = View.VISIBLE
                        mEmptyListView.visibility = View.GONE
                    }

                    if (isRefresh) {
                        mJobListData = ArrayList<BoardData>()
                        mJobListData = resultData

                        mIsFirst = false
                    } else {
                        if (mJobListData.size == 0) {
                            mJobListData = resultData
                        } else {
                            mJobListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mJobListData[0].getTotalCount()

                    mIsEndOfScroll = false
                    mJobAdapter.setData(mJobListData)

                    mPageCount++

                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    mSwipeRefreshLayout.isRefreshing = false 
                    setLoadingView(false)
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                    mSwipeRefreshLayout.isRefreshing = false
                    setLoadingView(false)
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mPageCount,
            20,
            mSearchEditText.text.toString(),
            mSortType,
            boardType,
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            "job",
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * Get Board List
     * */
    private fun getMyJobAndMissingList(boardType: Int, isRefresh: Boolean) {

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getMyJobAndMissingList(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<BoardData>> {
                override fun onSuccess(resultData: ArrayList<BoardData>) {

                    mSwipeRefreshLayout.isRefreshing = false

                    setLoadingView(false)

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mJobAdapter.setData(resultData)

                        mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), "0")

                        mJobRecyclerView.visibility = View.GONE
                        mEmptyListView.visibility = View.VISIBLE

                        return
                    } else {
                        mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), resultData[0].getTotalCount().toString())

                        mJobRecyclerView.visibility = View.VISIBLE
                        mEmptyListView.visibility = View.GONE
                    }

                    if (isRefresh) {
                        mJobListData = ArrayList<BoardData>()
                        mJobListData = resultData

                        mIsFirst = false
                    } else {
                        if (mJobListData.size == 0) {
                            mJobListData = resultData
                        } else {
                            mJobListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mJobListData[0].getTotalCount()

                    mIsEndOfScroll = false
                    mJobAdapter.setData(mJobListData)

                    mPageCount++

                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    mSwipeRefreshLayout.isRefreshing = false
                    setLoadingView(false)
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                    mSwipeRefreshLayout.isRefreshing = false
                    setLoadingView(false)
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mPageCount,
            10,
            mSortType,
            boardType,
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            false
        )
    }
}