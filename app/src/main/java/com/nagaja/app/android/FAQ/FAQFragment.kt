package com.nagaja.app.android.FAQ

import android.app.Activity
import android.content.Context
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
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Adapter.FAQAdapter
import com.nagaja.app.android.Adapter.StoreCategoryAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.FAQ.FAQActivity.Companion.mMainMenuItemData
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_faq.view.*
import kotlinx.android.synthetic.main.fragment_full_screen_image.view.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.fragment_used_market_list.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class FAQFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mCategoryNextView: View
    private lateinit var mLoadingView: View
    private lateinit var mEmptyListView: View

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mSearchEditText: EditText

    private lateinit var mTotalCountTextView: TextView

    private lateinit var mSortTypeSpinner: PowerSpinnerView

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mCategoryRecyclerView: RecyclerView
    private lateinit var mFAQRecyclerView: RecyclerView

    private lateinit var mCategoryAdapter: StoreCategoryAdapter
    private lateinit var mFAQAdapter: FAQAdapter

    private lateinit var mLoadingLottieView: LottieAnimationView

    private var mFAQListData = ArrayList<NoticeData>()

    private lateinit var mListener: OnFAQFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    private var mPageCount = 1
    private var mCategoryUid = 0
    private var mItemTotalCount = 0
    private var mSortType = 1

    interface OnFAQFragmentListener {
        fun onBack()
        fun onFAQDetailScreen(data: NoticeData)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_CATEGORY_UID_DATA                = "args_category_uid_data"


        fun newInstance(categoryUid: Int): FAQFragment {
            val args = Bundle()
            val fragment = FAQFragment()
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
        mFAQAdapter = FAQAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContainer = inflater.inflate(R.layout.fragment_faq, container, false)

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
            setShareUrl(mActivity, COMPANY_TYPE_FAQ, COMPANY_TYPE_MAIN)
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
        titleTextView.text = mActivity.resources.getString(R.string.text_faq)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onBack()
        }

        // Scroll View
        mScrollView = mContainer.fragment_faq_scroll_view

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_faq_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mCategoryUid = getCategoryUid()
                mItemTotalCount = 0

                getStoreCategoryData(true)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Category Recycler View
        mCategoryRecyclerView = mContainer.fragment_faq_category_recycler_view
        mCategoryRecyclerView.setHasFixedSize(true)
        mCategoryRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)

        mCategoryAdapter.setOnItemClickListener(object : StoreCategoryAdapter.OnItemClickListener {
            override fun onClick(data: StoreCategoryData, position: Int) {
                mCategoryAdapter.setSelectCategory(position)

                mCategoryUid = data.getCategoryUid()
                mPageCount = 1
                mIsFirst = true
                mItemTotalCount = 0

                getNoticeList(true)
            }
        })
        mCategoryRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mCategoryRecyclerView.adapter = mCategoryAdapter

        // Category Next View
        mCategoryNextView = mContainer.fragment_faq_category_next_view

        // Search Edit Text
        mSearchEditText = mContainer.fragment_faq_search_edit_text
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
                    mIsEndOfScroll = true
                    mPageCount = 1
                    mItemTotalCount = 0

                    mIsFirst = true

                    getStoreCategoryData(true)
                    mFAQRecyclerView.scrollToPosition(0)

                    hideKeyboard(mActivity)
                    return true
                }
                return false
            }
        })

        // Search Confirm Text View
        val searchConfirmTextView = mContainer.fragment_faq_search_confirm_view
        searchConfirmTextView.setOnSingleClickListener {
            mIsEndOfScroll = true
            mPageCount = 1
            mItemTotalCount = 0

            mIsFirst = true

            getStoreCategoryData(true)
            mFAQRecyclerView.scrollToPosition(0)
        }

        // Total Count Text View
        mTotalCountTextView = mContainer.fragment_faq_total_count_text_view

        // Sort Type Spinner
        mSortTypeSpinner = mContainer.fragment_faq_sort_type_spinner
        mSortTypeSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mSortType = newIndex + 1

            mIsEndOfScroll = true
            mPageCount = 1
            mItemTotalCount = 0

            mIsFirst = true

            getStoreCategoryData(true)
            mFAQRecyclerView.scrollToPosition(0)
        })

        // FAQ Recycler View
        mFAQRecyclerView = mContainer.fragment_faq_recycler_view
        mFAQRecyclerView.setHasFixedSize(true)
        mFAQRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mFAQRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!mFAQRecyclerView.canScrollVertically(1) && !mFAQRecyclerView.canScrollVertically(-1)) {
                    return
                }

                if (!mFAQRecyclerView.canScrollVertically(1)) {
                    if (!mIsEndOfScroll) {
                        if (mFAQListData.size >= mItemTotalCount) {
                            return
                        }

                        mIsEndOfScroll = true
                        getNoticeList(false)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        mFAQAdapter.setOnFAQItemClickListener(object : FAQAdapter.OnFAQItemClickListener {
            override fun onClick(data: NoticeData) {
                mListener.onFAQDetailScreen(data)
            }
        })
        mFAQRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mFAQRecyclerView.adapter = mFAQAdapter

        // Move Top Image View
//        val moveTopImageView = mContainer.fragment_faq_move_top_image_view
//        moveTopImageView.setOnSingleClickListener {
//            mScrollView.post(Runnable { mScrollView.fullScroll(ScrollView.FOCUS_UP) })
//        }

        // Loading View
        mLoadingView = mContainer.fragment_faq_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_faq_loading_lottie_view

        // Empty List View
        mEmptyListView = mContainer.fragment_faq_list_empty_view




        mCategoryUid = getCategoryUid()

        mSortTypeSpinner.selectItemByIndex(0)




//        if (mIsShare) {
            if ((mMainMenuItemData.getShareBoardUid() > 0) && (mMainMenuItemData.getShareBoardUid() < COMPANY_TYPE_MAIN) ) {
                getFaqDetail()
            }
//            mIsShare = false
//        }

    }

    private fun setTotalCount(arrayList: ArrayList<FAQData>) {
        if (arrayList.size > 0) {
            mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), arrayList.size)
        } else {
            mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), 0)
        }
    }

//    private fun setCategoryItem() {
//        val faqListData = ArrayList<FAQData>()
//
//        if (mCategoryType == 0) {
//            mFAQAdapter.setData(mFAQListData)
//
//            if (mFAQListData.size > 0) {
//                mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), mFAQListData.size)
//            } else {
//                mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), 0)
//            }
//        } else {
//            for (i in 0 until mFAQListData.size) {
//                if (mCategoryType == mFAQListData[i].getTypeValue()) {
//                    faqListData.add(mFAQListData[i])
//                }
//            }
//
//            mFAQAdapter.setData(faqListData)
//            setTotalCount(faqListData)
//        }
//    }

//    private fun searchFAQList() {
//        val faqSearchListData = ArrayList<FAQData>()
//
//        NagajaLog().d("wooks, mSearchEditText.text.toString() = ${mSearchEditText.text.toString()}")
//
//        for (i in 0 until mFAQListData.size) {
//            if (mCategoryType == 0) {
//                if (mFAQListData[i].getTitle().contains(mSearchEditText.text.toString())) {
//                    faqSearchListData.add(mFAQListData[i])
//                }
//            } else {
//                if (mCategoryType == mFAQListData[i].getTypeValue()) {
//                    if (mFAQListData[i].getTitle().contains(mSearchEditText.text.toString())) {
//                        faqSearchListData.add(mFAQListData[i])
//                    }
//                }
//            }
//        }
//        mFAQAdapter.setData(faqSearchListData)
//        setTotalCount(faqSearchListData)
//    }

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

    fun spinnerCheck(): Boolean {
        var isSpinnerShow = false

        if (mSortTypeSpinner.isShowing) {
            mSortTypeSpinner.dismiss()

            isSpinnerShow = true
        }

        return isSpinnerShow
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
        if (context is OnFAQFragmentListener) {
            mActivity = context as Activity

            if (context is OnFAQFragmentListener) {
                mListener = context;
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnFAQFragmentListener"
                );
            }
        }
    }

    /*
         * Called when the fragment attaches to the context
         */
    protected fun onAttachToContext(context: Context) {
        if (context is OnFAQFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFAQFragmentListener"
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

                    getNoticeList(isRefresh)
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
     * API. Get Local News
     */
    private fun getNoticeList(isRefresh: Boolean) {

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
                        mFAQAdapter.setData(resultData)

                        mFAQRecyclerView.visibility = View.GONE
                        mEmptyListView.visibility = View.VISIBLE

                        return
                    } else {
                        mFAQRecyclerView.visibility = View.VISIBLE
                        mEmptyListView.visibility = View.GONE
                    }

                    if (isRefresh) {
                        mFAQListData.clear()
                        mFAQListData = resultData

                        mIsFirst = false
                    } else {
                        if (mFAQListData.size == 0) {
                            mFAQListData = resultData
                        } else {
                            mFAQListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mFAQListData[0].getTotalCount()

                    mIsEndOfScroll = false
                    mFAQAdapter.setData(mFAQListData)

                    mPageCount++

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
            mSearchEditText.text.toString(),
            mSortType,
            mCategoryUid,
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Faq Detail
     */
    private fun getFaqDetail() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoticeDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<NoticeData> {
                override fun onSuccess(resultData: NoticeData) {
                    mListener.onFAQDetailScreen(resultData)
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
            mMainMenuItemData.getShareBoardUid()
        )
    }
}