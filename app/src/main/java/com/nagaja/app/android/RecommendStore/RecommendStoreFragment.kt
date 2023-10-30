package com.nagaja.app.android.RecommendStore

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
import com.nagaja.app.android.Adapter.BoardAdapter
import com.nagaja.app.android.Adapter.StoreCategoryAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.BoardData
import com.nagaja.app.android.Data.StoreCategoryData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.RecommendStore.RecommendStoreActivity.Companion.mMainMenuItemData
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_faq.view.*
import kotlinx.android.synthetic.main.fragment_recommend_store.view.*
import kotlinx.android.synthetic.main.fragment_store_list.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class RecommendStoreFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mCategoryNextView: View
    private lateinit var mLoadingView: View
    private lateinit var mEmptyListView: View

    private lateinit var mTotalCountTextView: TextView

    private lateinit var mSortTypeSpinner: PowerSpinnerView

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mCategoryRecyclerView: RecyclerView
    private lateinit var mRecommendStoreRecyclerView: RecyclerView

    private lateinit var mCategoryAdapter: StoreCategoryAdapter
    private lateinit var mRecommendStoreAdapter: BoardAdapter

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mListener: OnRecommendStoreFragmentListener

    private var mBoardListData = ArrayList<BoardData>()

    private lateinit var mRequestQueue: RequestQueue

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    private var mPageCount = 1
    private var mCategoryUid = 0
    private var mItemTotalCount = 0
    private var mSortType = 1

    interface OnRecommendStoreFragmentListener {
        fun onFinish()
        fun onRecommendStoreDetailScreen(categoryUid: Int, boardUid: Int)
        fun onRecommendStoreWriteScreen(categoryUid: Int, boardUid: Int, isModify: Boolean)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_CATEGORY_UID_DATA                = "args_category_uid_data"

        fun newInstance(categoryUid: Int): RecommendStoreFragment {
            val args = Bundle()
            val fragment = RecommendStoreFragment()
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

        mCategoryAdapter = StoreCategoryAdapter(mActivity)
        mRecommendStoreAdapter = BoardAdapter(mActivity, false)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_recommend_store, container, false)

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
            setShareUrl(mActivity, getCategoryUid(), COMPANY_TYPE_MAIN)
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
        titleTextView.text = mActivity.resources.getString(R.string.text_recommend_store)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_recommend_store_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mCategoryUid = getCategoryUid()
                mItemTotalCount = 0
                mSortType = 0

                getStoreCategoryData(true)

                mCategoryRecyclerView.scrollToPosition(0)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Category Recycler View
        mCategoryRecyclerView = mContainer.fragment_recommend_store_category_recycler_view
        mCategoryRecyclerView.setHasFixedSize(true)
        mCategoryRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        // Category Next View
        mCategoryNextView = mContainer.fragment_recommend_store_category_next_view

        mCategoryAdapter.setOnItemClickListener(object : StoreCategoryAdapter.OnItemClickListener {
            override fun onClick(data: StoreCategoryData, position: Int) {
                mCategoryAdapter.setSelectCategory(position)

                mCategoryUid = data.getCategoryUid()
                mPageCount = 1
                mIsFirst = true
                mItemTotalCount = 0

                getBoardList(true)
            }
        })
        mCategoryRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mCategoryRecyclerView.adapter = mCategoryAdapter

        // Category Next View
        mCategoryNextView = mContainer.fragment_recommend_store_category_next_view

        // Total Count Text View
        mTotalCountTextView = mContainer.fragment_recommend_store_total_count_text_view

        // Sort Type Spinner
        mSortTypeSpinner = mContainer.fragment_recommend_store_sort_type_spinner
        mSortTypeSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mSortType = newIndex

            mIsEndOfScroll = true
            mPageCount = 1
            mItemTotalCount = 0

            mIsFirst = true

            getStoreCategoryData(true)
//            mCategoryRecyclerView.scrollToPosition(0)
        })

        // Recommend Store Recycler View
        mRecommendStoreRecyclerView = mContainer.fragment_recommend_store_recycler_view
        mRecommendStoreRecyclerView.setHasFixedSize(true)
        mRecommendStoreRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mRecommendStoreRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!mRecommendStoreRecyclerView.canScrollVertically(1) && !mRecommendStoreRecyclerView.canScrollVertically(-1)) {
                    return
                }

                if (!mRecommendStoreRecyclerView.canScrollVertically(1)) {
                    if (!mIsEndOfScroll) {
                        if (mBoardListData.size >= mItemTotalCount) {
                            return
                        }

                        mIsEndOfScroll = true
                        getBoardList(false)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        mRecommendStoreAdapter.setOnNewsItemClickListener(object : BoardAdapter.OnNewsItemClickListener {
            override fun onClick(boardUid: Int) {
                mListener.onRecommendStoreDetailScreen(getCategoryUid(), boardUid)

                if (mSortTypeSpinner.isShowing) {
                    mSortTypeSpinner.dismiss()
                }
            }
        })
        mRecommendStoreRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mRecommendStoreRecyclerView.adapter = mRecommendStoreAdapter

        // Recommend Register Text View
        val recommendRegisterTextView = mContainer.fragment_recommend_store_write_text_view
        recommendRegisterTextView.setOnClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                mListener.onRecommendStoreWriteScreen(getCategoryUid(), 0, false)
            }
        }


        // Loading View
        mLoadingView = mContainer.fragment_recommend_store_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_recommend_store_loading_lottie_view

        // Empty List View
        mEmptyListView = mContainer.fragment_recommend_store_list_empty_view





        mCategoryUid = getCategoryUid()

        mSortTypeSpinner.selectItemByIndex(0)


//        if (mIsShare) {
            if ((mMainMenuItemData.getShareBoardUid() > 0) && (mMainMenuItemData.getShareBoardUid() < COMPANY_TYPE_MAIN) ) {
                mListener.onRecommendStoreDetailScreen(getCategoryUid(), mMainMenuItemData.getShareBoardUid())
            }
//            mIsShare = false
//        }


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
        if (context is OnRecommendStoreFragmentListener) {
            mActivity = context as Activity

            if (context is OnRecommendStoreFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnRecommendStoreFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnRecommendStoreFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnRecommendStoreFragmentListener"
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
                    storeCategoryData.setCategoryUid(getCategoryUid())
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

                    getBoardList(isRefresh)
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
            mCategoryUid.toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * Get Board List
     * */
    private fun getBoardList(isRefresh: Boolean) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardList(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<BoardData>> {
                override fun onSuccess(resultData: ArrayList<BoardData>) {

                    mSwipeRefreshLayout.isRefreshing = false

                    setLoadingView(false)

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mRecommendStoreAdapter.setData(resultData)

                        mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), "0")

                        mRecommendStoreRecyclerView.visibility = View.GONE
                        mEmptyListView.visibility = View.VISIBLE

                        return
                    } else {
                        mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), resultData[0].getTotalCount().toString())

                        mRecommendStoreRecyclerView.visibility = View.VISIBLE
                        mEmptyListView.visibility = View.GONE
                    }

                    if (isRefresh) {
                        mBoardListData.clear()
                        mBoardListData = resultData

                        mIsFirst = false
                    } else {
                        if (mBoardListData.size == 0) {
                            mBoardListData = resultData
                        } else {
                            mBoardListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mBoardListData[0].getTotalCount()

                    mIsEndOfScroll = false
                    mRecommendStoreAdapter.setData(mBoardListData)

                    mPageCount++

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
            mPageCount,
            20,
            "",
            mSortType,
            mCategoryUid,
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            "",
            MAPP.SELECT_LANGUAGE_CODE
        )
    }
}