package com.nagaja.app.android.Board

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
import android.widget.ScrollView
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
import com.kenilt.loopingviewpager.scroller.AutoScroller
import com.kenilt.loopingviewpager.widget.LoopingViewPager
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Adapter.BoardVerticalAdapter.Companion.SELECT_TYPE_BOOKMARK
import com.nagaja.app.android.Adapter.BoardVerticalAdapter.Companion.SELECT_TYPE_COMMENT
import com.nagaja.app.android.Adapter.BoardVerticalAdapter.Companion.SELECT_TYPE_GOOD
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Board.BoardActivity.Companion.mMainMenuItemData
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.R
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*

import kotlinx.android.synthetic.main.fragment_board.view.*
import kotlinx.android.synthetic.main.fragment_missing.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*


class BoardFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View
    private lateinit var mCategoryNextView: View
    private lateinit var mEmptyListView: View

    private lateinit var mSearchEditText: EditText

    private lateinit var mScrollView: NestedScrollView


    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mSortUpdateTextView: TextView
    private lateinit var mSortLatestTextView: TextView
    private lateinit var mSortFavoriteTextView: TextView

    private lateinit var mCategoryRecyclerView: RecyclerView
    private lateinit var mPopularityRecyclerView: RecyclerView
    private lateinit var mStandByRecyclerView: RecyclerView
    private lateinit var mBoardRecyclerView: RecyclerView

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mViewPager: LoopingViewPager

    private lateinit var mBoardEventPagerAdapter: BoardEventPagerAdapter

    private lateinit var mPopularityAdapter: BoardHorizontalAdapter
    private lateinit var mStandByAdapter: BoardHorizontalAdapter
    private lateinit var mBoardVerticalAdapter: BoardVerticalAdapter
    private lateinit var mCategoryAdapter: StoreCategoryAdapter

    private lateinit var mListener: OnBoardFragmentListener

    private var mPopularityListData = ArrayList<BoardData>()
    private var mStandByListData = ArrayList<BoardData>()
    private var mVerticalBoardListData = ArrayList<BoardData>()
    private var mCategoryListData = ArrayList<StoreCategoryData>()

    private lateinit var mRequestQueue: RequestQueue

    private var mSelectVerticalItemPosition = 0

    private var mBannerSize = 0
    private var mSelectCategory = 30

    private var mPageCount = 1
    private var mSortType = 4
    private var mItemTotalCount = 0
    private var mIsEndOfScroll = false

    private var mPopularityTotalCount = 1
    private var mPopularityCount = 1
    private var mIsPopularityEndOfScroll = false

    private var mStandByTotalCount = 1
    private var mStandByCount = 1
    private var mIsStandByEndOfScroll = false

    private var mIsPlaygroundFirstCheck = false

    interface OnBoardFragmentListener {
        fun onFinish()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
        fun onBoardDetailScreen(boardUid: Int, selectBoardType: Int, position: Int)
        fun onWriteBoardScreen(categoryUid: Int)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val BOARD_DETAIL_TYPE_POPULARITY     = 0x01
        const val BOARD_DETAIL_TYPE_STANDBY        = 0x02
        const val BOARD_DETAIL_TYPE_VERTICAL       = 0x03

//        const val SELECT_SORT_TYPE_UPDATE          = 4
        const val SELECT_SORT_TYPE_LATEST          = 0
        const val SELECT_SORT_TYPE_POPULARITY      = 3
        const val SELECT_SORT_TYPE_VIEW_COUNT      = 1

        const val BOARD_LIST_POPULARITY            = "popularity"
        const val BOARD_LIST_STANDBY               = "standby"

        const val ARGS_CATEGORY_UID_DATA           = "args_category_uid_data"
        const val ARGS_PLAYGROUND_DATA             = "args_playground_data"

        fun newInstance(categoryUid: Int, isPlayground: Boolean): BoardFragment {
            val args = Bundle()
            val fragment = BoardFragment()
            args.putInt(ARGS_CATEGORY_UID_DATA, categoryUid)
            args.putBoolean(ARGS_PLAYGROUND_DATA, isPlayground)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getCategoryUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_CATEGORY_UID_DATA) as Int
    }

    private fun getIsPlayground(): Boolean {
        val arguments = arguments
        return arguments?.getBoolean(ARGS_PLAYGROUND_DATA) as Boolean
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCategoryAdapter = StoreCategoryAdapter(mActivity)
        mPopularityAdapter = BoardHorizontalAdapter(mActivity)
        mStandByAdapter = BoardHorizontalAdapter(mActivity)
        mBoardVerticalAdapter = BoardVerticalAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_board, container, false)

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
        titleTextView.text = mActivity.resources.getString(R.string.text_board_category_talking_room)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Scroll View
        mScrollView = mContainer.fragment_board_scroll_view
        mScrollView.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (!v.canScrollVertically(1)) {
                if (!mIsEndOfScroll) {
                    if (mVerticalBoardListData.size >= mItemTotalCount) {
                        return@OnScrollChangeListener
                    }

                    mIsEndOfScroll = true
                    getBoardList(false)
                }
            }
        })

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_board_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(mActivity)) {
                setRefresh()
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Category Recycler View
        mCategoryRecyclerView = mContainer.fragment_board_category_recycler_view
        mCategoryRecyclerView.setHasFixedSize(true)
        mCategoryRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        mCategoryAdapter.setOnItemClickListener(object : StoreCategoryAdapter.OnItemClickListener {
            override fun onClick(data: StoreCategoryData, position: Int) {
                mCategoryAdapter.setSelectCategory(position)
                mSelectCategory = data.getCategoryUid()

                mPageCount = 1
                NagajaLog().e("woosk, mSelectCategory = $mSelectCategory")

//                getBoardList(true)

                mPopularityCount = 1
                mStandByCount = 1
                getBoardHorizontalList(BOARD_LIST_POPULARITY, true)
            }
        })
        mCategoryRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mCategoryRecyclerView.adapter = mCategoryAdapter

        // Category Next View
        mCategoryNextView = mContainer.fragment_board_category_next_view

        // Search Edit Text
        mSearchEditText = mContainer.fragment_board_search_edit_text
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
                    mPopularityCount = 1
                    mStandByCount = 1
                    mSortType = SELECT_SORT_TYPE_LATEST

                    mItemTotalCount = 0

                    getBoardList(true)
                    mBoardRecyclerView.scrollToPosition(0)

                    hideKeyboard(mActivity)
                    return true
                }
                return false
            }
        })

        // Search Confirm View
        val searchConfirmView = mContainer.fragment_job_search_confirm_view
        searchConfirmView.setOnClickListener {
            mIsEndOfScroll = true
            mPageCount = 1
            mPopularityCount = 1
            mStandByCount = 1
            mSortType = SELECT_SORT_TYPE_LATEST

            mItemTotalCount = 0

            getBoardList(true)
            mBoardRecyclerView.scrollToPosition(0)

            hideKeyboard(mActivity)
        }

        // View Pager
        mViewPager = mContainer.fragment_board_banner_view_pager
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        val autoScroller = AutoScroller(mViewPager, lifecycle, 5000)
        autoScroller.isAutoScroll = true

        // Real Time Recycler View
        mPopularityRecyclerView = mContainer.fragment_board_real_time_recycler_view
        mPopularityRecyclerView.setHasFixedSize(true)
        mPopularityRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        mPopularityRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!mPopularityRecyclerView.canScrollVertically(1) && !mPopularityRecyclerView.canScrollVertically(-1)) {
                    return
                }

                if (!mPopularityRecyclerView.canScrollVertically(1)) {
                    if (!mIsPopularityEndOfScroll) {
                        if (mPopularityListData.size >= mPopularityTotalCount) {
                            return
                        }

                        mIsPopularityEndOfScroll = true
                        getBoardHorizontalList(BOARD_LIST_POPULARITY, false)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        mPopularityAdapter.setOnBoardItemClickListener(object : BoardHorizontalAdapter.OnBoardItemClickListener {
            override fun onClick(data: BoardData, position: Int) {
                mListener.onBoardDetailScreen(data.getBoardUid(), BOARD_DETAIL_TYPE_POPULARITY, position)
            }
        })
        mPopularityRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mPopularityRecyclerView.adapter = mPopularityAdapter


        // Waiting Comment Board Recycler View
        mStandByRecyclerView = mContainer.fragment_board_waiting_comment_recycler_view
        mStandByRecyclerView.setHasFixedSize(true)
        mStandByRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        mStandByAdapter.setOnBoardItemClickListener(object : BoardHorizontalAdapter.OnBoardItemClickListener {
            override fun onClick(data: BoardData, position: Int) {
                mListener.onBoardDetailScreen(data.getBoardUid(), BOARD_DETAIL_TYPE_STANDBY, position)
            }
        })
        mStandByRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mStandByRecyclerView.adapter = mStandByAdapter

        // Sort Update Text View
        mSortUpdateTextView = mContainer.fragment_board_sort_update_text_view
        mSortUpdateTextView.setOnSingleClickListener {
            sortBoard(SELECT_SORT_TYPE_LATEST, false)
        }

        // Sort Latest Text View
        mSortLatestTextView = mContainer.fragment_board_sort_latest_text_view
        mSortLatestTextView.setOnSingleClickListener {
            sortBoard(SELECT_SORT_TYPE_POPULARITY, false)
        }

        // Sort Favorite Text View
        mSortFavoriteTextView = mContainer.fragment_board_sort_favorite_text_view
        mSortFavoriteTextView.setOnSingleClickListener {
            sortBoard(SELECT_SORT_TYPE_VIEW_COUNT, false)
        }

        // Board Recycler View
        mBoardRecyclerView = mContainer.fragment_board_recycler_view
        mBoardRecyclerView.setHasFixedSize(false)
        mBoardRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.VERTICAL,
            false
        )

        mBoardVerticalAdapter.setOnBoardItemClickListener(object : BoardVerticalAdapter.OnBoardItemClickListener {
            override fun onImageClick(imageList: ArrayList<String>, position: Int) {
                mListener.onFullScreenImage(imageList, position)
            }

            override fun onClick(data: BoardData, position: Int) {
                mSelectVerticalItemPosition = position
                mListener.onBoardDetailScreen(data.getBoardUid(), BOARD_DETAIL_TYPE_VERTICAL, position)
            }

            override fun onSelectItem(data: BoardData, position: Int, selectType: Int) {

                when (selectType) {
                    SELECT_TYPE_GOOD -> {
                        if (MAPP.IS_NON_MEMBER_SERVICE) {
                            showLoginGuideCustomDialog()
                        } else {
                            getRecommendSave(data, position, selectType)
                        }
                    }

                    SELECT_TYPE_COMMENT -> {

                    }

                    SELECT_TYPE_BOOKMARK -> {
                        if (MAPP.IS_NON_MEMBER_SERVICE) {
                            showLoginGuideCustomDialog()
                        } else {
                            getBookmarkSave(data, position, selectType)
                        }
                        getBookmarkSave(data, position, selectType)
                    }
                }
            }
        })
        mBoardRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mBoardRecyclerView.adapter = mBoardVerticalAdapter

        // Write Board Text View
        val writeBoardTextView = mContainer.fragment_board_write_text_view
        writeBoardTextView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                mListener.onWriteBoardScreen(getCategoryUid())
            }
        }

        // Loading View
        mLoadingView = mContainer.fragment_board_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_board_loading_lottie_view

        // Empty List View
        mEmptyListView = mContainer.fragment_board_empty_view



        sortBoard(SELECT_SORT_TYPE_LATEST, true)



        getBoardCategoryData()



        if ((mMainMenuItemData.getShareBoardUid() > 0) && (mMainMenuItemData.getShareBoardUid() < COMPANY_TYPE_MAIN) ) {
            mListener.onBoardDetailScreen(mMainMenuItemData.getShareBoardUid(), BOARD_DETAIL_TYPE_VERTICAL, -1)
        }

    }

    private fun sortBoard(sortType: Int, isFirst: Boolean) {

        when (sortType) {
            SELECT_SORT_TYPE_LATEST -> {
                mSortUpdateTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_222222))
                mSortUpdateTextView.setTypeface(mSortUpdateTextView.typeface, Typeface.BOLD)

                mSortLatestTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_9f9f9f))
                mSortLatestTextView.setTypeface(mSortUpdateTextView.typeface, Typeface.NORMAL)

                mSortFavoriteTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_9f9f9f))
                mSortFavoriteTextView.setTypeface(mSortUpdateTextView.typeface, Typeface.NORMAL)
            }

            SELECT_SORT_TYPE_POPULARITY -> {
                mSortUpdateTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_9f9f9f))
                mSortUpdateTextView.setTypeface(mSortUpdateTextView.typeface, Typeface.NORMAL)

                mSortLatestTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_222222))
                mSortLatestTextView.setTypeface(mSortUpdateTextView.typeface, Typeface.BOLD)

                mSortFavoriteTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_9f9f9f))
                mSortFavoriteTextView.setTypeface(mSortUpdateTextView.typeface, Typeface.NORMAL)
            }

            SELECT_SORT_TYPE_VIEW_COUNT -> {
                mSortUpdateTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_9f9f9f))
                mSortUpdateTextView.setTypeface(mSortUpdateTextView.typeface, Typeface.NORMAL)

                mSortLatestTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_9f9f9f))
                mSortLatestTextView.setTypeface(mSortUpdateTextView.typeface, Typeface.NORMAL)

                mSortFavoriteTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_222222))
                mSortFavoriteTextView.setTypeface(mSortUpdateTextView.typeface, Typeface.BOLD)
            }
        }

        mSortType = sortType

        if (!isFirst) {
            mPageCount = 1
            getBoardList(true)
        }
    }

    private fun displayBannerListData() {

        mBannerSize = MAPP.SUB_BANNER_EVENT_LIST_DATA.size

        mViewPager.clipToPadding = false
//            mViewPager.setPadding(80, 0, 80, 0)
        mViewPager.setPadding(0, 0, 0, 0)
        mViewPager.pageMargin = 50
        mBoardEventPagerAdapter = BoardEventPagerAdapter(mActivity, MAPP.SUB_BANNER_EVENT_LIST_DATA, null)
        mViewPager.adapter = mBoardEventPagerAdapter
//            mViewPagerIndicator.setViewPager(mViewPager)
        mBoardEventPagerAdapter.setOnEventPagerClickListener(object : BoardEventPagerAdapter.OnEventPagerClickListener {
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
        })
        mViewPager.adapter = mBoardEventPagerAdapter
//            mViewPagerIndicator.setViewPager(mViewPager)

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

    fun setRefresh() {
        mIsEndOfScroll = true
        mPageCount = 1
        mPopularityCount = 1
        mStandByCount = 1
        mSortType = SELECT_SORT_TYPE_LATEST

        mItemTotalCount = 0

        mSelectCategory = COMPANY_TYPE_TALK_ROOM
        mCategoryAdapter.setSelectCategory(0)

        sortBoard(SELECT_SORT_TYPE_LATEST, true)
        getBoardHorizontalList(BOARD_LIST_POPULARITY, true)

        mScrollView.post(Runnable { mScrollView.fullScroll(ScrollView.FOCUS_UP) })
    }

    fun successDelete(selectType: Int, position: Int) {
        when (selectType) {
            BOARD_DETAIL_TYPE_POPULARITY -> {
                Handler(Looper.getMainLooper()).postDelayed({
                    mPopularityListData.removeAt(position)
                    mPopularityAdapter.deleteItem(position)
                }, 200)
            }

            BOARD_DETAIL_TYPE_STANDBY -> {
                Handler(Looper.getMainLooper()).postDelayed({
                    mStandByListData.removeAt(position)
                    mStandByAdapter.deleteItem(position)
                }, 200)
            }

            BOARD_DETAIL_TYPE_VERTICAL -> {
                Handler(Looper.getMainLooper()).postDelayed({
                    mVerticalBoardListData.removeAt(position)
                    mBoardVerticalAdapter.deleteItem(position)
                }, 200)
            }
        }
    }

    fun changeClickData(isLike: Boolean, isBookMark: Boolean) {
        mBoardVerticalAdapter.selectLikeBookmark(isLike, mSelectVerticalItemPosition, true)
        mBoardVerticalAdapter.selectLikeBookmark(isBookMark, mSelectVerticalItemPosition, false)
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
        if (context is OnBoardFragmentListener) {
            mActivity = context as Activity

            if (context is OnBoardFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnBoardFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnBoardFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnBoardFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    // ======================================================================================
    // API
    // ======================================================================================

    /**
     * API. Get Store Category Data
     */
    private fun getBoardCategoryData() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreCategoryData(
            mRequestQueue,
            object : NagajaManager.RequestListener<java.util.ArrayList<StoreCategoryData>> {
                override fun onSuccess(resultData: java.util.ArrayList<StoreCategoryData>) {

                    val storeCategoryData = StoreCategoryData()
                    storeCategoryData.setCategoryUid(COMPANY_TYPE_TALK_ROOM)
                    storeCategoryData.setCategoryDepth(2)
                    storeCategoryData.setCategorySort(1)
                    storeCategoryData.setCategoryName(mActivity.resources.getString(R.string.text_all))
                    storeCategoryData.setCategoryNameEnglish("All")
                    storeCategoryData.setCategoryUri("/free")
                    storeCategoryData.setIsUseYn(true)
                    storeCategoryData.setCreateDate("")
                    storeCategoryData.setParentCategoryUid(COMPANY_TYPE_TALK_ROOM)
                    storeCategoryData.setIsSelect(true)

                    resultData.add(0, storeCategoryData)

                    mCategoryAdapter.setData(resultData)

                    mCategoryListData = resultData

                    mCategoryNextView.visibility = if (resultData.size > 4) View.VISIBLE else View.GONE

                    displayBannerListData()

                    mSelectCategory = mCategoryListData[0].getCategoryUid()
                    getBoardHorizontalList(BOARD_LIST_POPULARITY, true)
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
     * Get Board List
     * */
    private fun getBoardHorizontalList(boardType: String, isRefresh: Boolean) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardHorizontalList(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<BoardData>> {
                override fun onSuccess(resultData: ArrayList<BoardData>) {

                    setLoadingView(false)

                    if (boardType == BOARD_LIST_POPULARITY) {
                        if (resultData.isEmpty()) {
                            mPopularityAdapter.setData(resultData)
                            return
                        }

                        if (isRefresh) {
                            mPopularityListData.clear()
                            mPopularityListData = resultData

                        } else {
                            if (mPopularityListData.size == 0) {
                                mPopularityListData = resultData
                            } else {
                                mPopularityListData.addAll(resultData)
                            }
                        }

                        mPopularityTotalCount = mPopularityListData[0].getTotalCount()

                        mIsPopularityEndOfScroll = false
                        mPopularityAdapter.setData(mPopularityListData)

                        mPopularityRecyclerView.scrollToPosition(0)

                        mPopularityCount++

                        if (isRefresh) {
                            getBoardHorizontalList(BOARD_LIST_STANDBY, true)
                        }
                    } else {
                        if (resultData.isEmpty()) {
                            mStandByAdapter.setData(resultData)
                            return
                        }

                        if (isRefresh) {
                            mStandByListData.clear()
                            mStandByListData = resultData

                        } else {
                            if (mStandByListData.size == 0) {
                                mStandByListData = resultData
                            } else {
                                mStandByListData.addAll(resultData)
                            }
                        }

                        mStandByTotalCount = mStandByListData[0].getTotalCount()

                        mIsStandByEndOfScroll = false
                        mStandByAdapter.setData(mStandByListData)

                        mStandByRecyclerView.scrollToPosition(0)

                        mStandByCount++

                        getBoardList(isRefresh)
                    }

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
            if (boardType == BOARD_LIST_POPULARITY) mPopularityCount else mStandByCount,
            20,
            mSelectCategory,
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            boardType,
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * Get Board List
     * */
    private fun getBoardList(isRefresh: Boolean) {

        setLoadingView(true)

        if (getIsPlayground() && !mIsPlaygroundFirstCheck) {
            mIsPlaygroundFirstCheck = true
            mSelectCategory = COMPANY_TYPE_PLAYGROUND

            for (i in mCategoryListData.indices) {
                if (mCategoryListData[i].getCategoryUid() == COMPANY_TYPE_PLAYGROUND) {
                    mCategoryAdapter.setSelectCategory(i)
                    break
                }
            }
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardList(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<BoardData>> {
                override fun onSuccess(resultData: ArrayList<BoardData>) {

                    mSwipeRefreshLayout.isRefreshing = false

                    setLoadingView(false)

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mBoardVerticalAdapter.setData(resultData)

                        mBoardRecyclerView.visibility = View.GONE
                        mEmptyListView.visibility = View.VISIBLE

                        return
                    } else {
                        mBoardRecyclerView.visibility = View.VISIBLE
                        mEmptyListView.visibility = View.GONE
                    }

                    if (isRefresh) {
                        mVerticalBoardListData = ArrayList<BoardData>()
                        mVerticalBoardListData = resultData
                    } else {
                        if (mVerticalBoardListData.size == 0) {
                            mVerticalBoardListData = resultData
                        } else {
                            mVerticalBoardListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mVerticalBoardListData[0].getTotalCount()

                    mIsEndOfScroll = false
                    mBoardVerticalAdapter.setData(mVerticalBoardListData)

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
            mSelectCategory,
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            "",
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Recommend Save
     */
    private fun getRecommendSave(data: BoardData, position: Int, selectType: Int) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getRecommendSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mVerticalBoardListData[position].setIsRecommend(!data.getIsRecommend())
                    mBoardVerticalAdapter.setItemSelect(position, selectType)


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
            data.getBoardUid(),
            !data.getIsRecommend()
        )
    }

    /**
     * API. Get Bookmark Save
     */
    private fun getBookmarkSave(data: BoardData, position: Int, selectType: Int) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBookmarkSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mVerticalBoardListData[position].setIsBookMark(!data.getIsBookMark())
                    mBoardVerticalAdapter.setItemSelect(position, selectType)
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
            data.getBoardUid(),
            !data.getIsBookMark()
        )
    }
}