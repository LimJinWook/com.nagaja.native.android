package com.nagaja.app.android.CompanyJob

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.*
import android.widget.TextView
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
import com.nagaja.app.android.Job.JobFragment.Companion.SELECT_TYPE_JOB
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_company_job.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class CompanyJobFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mCategoryNextView: View
    private lateinit var mLoadingView: View
    private lateinit var mEmptyListView: View

    private lateinit var mJobAndRecruitingTextView: TextView

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mCategoryRecyclerView: RecyclerView
    private lateinit var mJobRecyclerView: RecyclerView

    private lateinit var mCategoryAdapter: StoreCategoryAdapter
    private lateinit var mJobAdapter: JobAdapter


    private var mJobListData = ArrayList<BoardData>()

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mListener: OnCompanyJobFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private var mSelectType = 40

    private var mIsPause = false
    private var mIsFirst = true
    private var mIsEndOfScroll = false
    private var mIsResumeSetting = false

    private var mPageCount = 1
    private var mItemTotalCount = 0
    private var mSortType = 0

    interface OnCompanyJobFragmentListener {
        fun onFinish()
        fun onJobDetailScreen(boardUid: Int, companyUid: Int)
        fun onJobRegister(jobType: Int, companyUid: Int)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_COMPANY_DEFAULT_DATA                = "args_company_default_data"
        const val ARGS_COMPANY_CATEGORY_UID                = "args_company_category_uid"

        fun newInstance(data: CompanyDefaultData, categoryUid: Int): CompanyJobFragment {
            val args = Bundle()
            val fragment = CompanyJobFragment()
            args.putSerializable(ARGS_COMPANY_DEFAULT_DATA, data)
            args.putInt(ARGS_COMPANY_CATEGORY_UID, categoryUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getCompanyDefaultData(): CompanyDefaultData {
        val arguments = arguments
        return arguments?.getSerializable(ARGS_COMPANY_DEFAULT_DATA) as CompanyDefaultData
    }

    private fun getCategoryUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_COMPANY_CATEGORY_UID) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)

        mCategoryAdapter = StoreCategoryAdapter(mActivity)
        mJobAdapter = JobAdapter(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContainer = inflater.inflate(R.layout.fragment_company_job, container, false)

        init()

        return mContainer
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
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
        titleTextView.text = mActivity.resources.getString(R.string.text_company_job_offer)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_company_job_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mItemTotalCount = 0

                mSelectType = getCategoryUid()
                mCategoryAdapter.setSelectCategory(0)
                mJobRecyclerView.scrollToPosition(0)

                getStoreCategoryData(true)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Category Recycler View
        mCategoryRecyclerView = mContainer.fragment_company_job_category_recycler_view
        mCategoryRecyclerView.setHasFixedSize(true)
        mCategoryRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)

        mCategoryRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!mCategoryRecyclerView.canScrollVertically(1) && !mCategoryRecyclerView.canScrollVertically(-1)) {
                    return
                }

                if (!mCategoryRecyclerView.canScrollVertically(1)) {
                    if (!mIsEndOfScroll) {
                        if (mJobListData.size >= mItemTotalCount) {
                            return
                        }

                        mIsEndOfScroll = true
                        getCompanyJobList(false)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        mCategoryAdapter.setOnItemClickListener(object : StoreCategoryAdapter.OnItemClickListener {
            override fun onClick(data: StoreCategoryData, position: Int) {
                mCategoryAdapter.setSelectCategory(position)

                mSelectType = data.getCategoryUid()
                mPageCount = 1
                mIsFirst = true
                mItemTotalCount = 0

                getCompanyJobList(true)
            }
        })
        mCategoryRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mCategoryRecyclerView.adapter = mCategoryAdapter

        // Category Next View
        mCategoryNextView = mContainer.fragment_company_job_category_next_view

        // Job Recycler View
        mJobRecyclerView = mContainer.fragment_company_job_recycler_view
        mJobRecyclerView.setHasFixedSize(true)
        mJobRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mJobAdapter.setOnItemClickListener(object : JobAdapter.OnItemClickListener {
            override fun onClick(data: BoardData) {
                mListener.onJobDetailScreen(data.getBoardUid(), getCompanyDefaultData().getCompanyUid())
            }
        })
        mJobRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mJobRecyclerView.adapter = mJobAdapter

        // Job And Recruiting Text View
        mJobAndRecruitingTextView = mContainer.fragment_company_job_recruiting_text_view
        mJobAndRecruitingTextView.setOnClickListener {
            mListener.onJobRegister(mSelectType, getCompanyDefaultData().getCompanyUid())
        }



        // Loading View
        mLoadingView = mContainer.fragment_company_job_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_company_job_loading_lottie_view

        // Empty List View
        mEmptyListView = mContainer.fragment_company_job_list_empty_view



        if (mIsPause) {
            mIsPause = false
        } else {
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
        if (context is OnCompanyJobFragmentListener) {
            mActivity = context as Activity

            if (context is OnCompanyJobFragmentListener) {
                mListener = context;
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnCompanyJobFragmentListener"
                );
            }
        }
    }

    /*
         * Called when the fragment attaches to the context
         */
    protected fun onAttachToContext(context: Context) {
        if (context is OnCompanyJobFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnCompanyJobFragmentListener"
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
            object : NagajaManager.RequestListener<java.util.ArrayList<StoreCategoryData>> {
                override fun onSuccess(resultData: java.util.ArrayList<StoreCategoryData>) {

                    mIsFirst = false
                    mIsResumeSetting = true

//                    if (resultData.isEmpty()) {
//                        return
//                    }


                    val storeCategoryData = StoreCategoryData()
                    storeCategoryData.setCategoryUid(40)
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

                    getCompanyJobList(isRefresh)
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
            SELECT_TYPE_JOB.toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * Get Company Job List
     * */
    fun getCompanyJobList(isRefresh: Boolean) {

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyJobList(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<BoardData>> {
                override fun onSuccess(resultData: ArrayList<BoardData>) {

                    mSwipeRefreshLayout.isRefreshing = false

                    setLoadingView(false)

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mJobAdapter.setData(resultData)

                        mJobRecyclerView.visibility = View.GONE
                        mEmptyListView.visibility = View.VISIBLE

                        return
                    } else {
                        mJobRecyclerView.visibility = View.VISIBLE
                        mEmptyListView.visibility = View.GONE
                    }

                    if (isRefresh) {
                        mJobListData.clear()
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
            mSelectType,
            getCompanyDefaultData().getCompanyUid(),
            SharedPreferencesUtil().getSaveNationCode(mActivity).toString()
        )
    }
}