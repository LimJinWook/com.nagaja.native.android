package com.nagaja.app.android.Missing

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.location.Geocoder
import android.location.Location
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
import com.google.android.gms.maps.model.*
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Missing.MissingActivity.Companion.mMainMenuItemData
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_faq.view.*
import kotlinx.android.synthetic.main.fragment_job.view.*
import kotlinx.android.synthetic.main.fragment_missing.view.*
import kotlinx.android.synthetic.main.fragment_notice.view.*
import kotlinx.android.synthetic.main.fragment_used_market_list.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.util.*
import kotlin.collections.ArrayList


class MissingFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mContainer: View
    private lateinit var mMissingReportUnderLineView: View
    private lateinit var mMissingPersonUnderLineView: View
    private lateinit var mLoadingView: View
    private lateinit var mSearchView: View
    private lateinit var mEmptyListView: View

    private lateinit var mSearchEditText: EditText

    private lateinit var mMissingReportTextView: TextView
    private lateinit var mMissingPersonTextView: TextView
    private lateinit var mTotalCountTextView: TextView
    private lateinit var mMissingReportWriteTextView: TextView

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mSortTypeSpinner: PowerSpinnerView

    private lateinit var mMissingRecyclerView: RecyclerView

    private lateinit var mMissingAdapter: MissingAdapter

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mListener: OnMissingFragmentListener

    private var mMissingListData = ArrayList<BoardData>()

    private var mSelectType = 46

    private var mIsPause = false

    private lateinit var mRequestQueue: RequestQueue

    private var mIsFirst = true
    private var mIsEndOfScroll = false
    private var mIsRegister = false

    private var mPageCount = 1
    private var mItemTotalCount = 0
    private var mSortType = 0

    interface OnMissingFragmentListener {
        fun onFinish()
        fun onMissingDetailScreen(boardUid: Int)
        fun onMissingWriteScreen(type: Int, isModify: Boolean)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val SELECT_TYPE_MISSING_REPORT                 = 46
        const val SELECT_TYPE_MISSING_PERSON                 = 47

        const val ARGS_CATEGORY_UID_DATA                     = "args_category_uid_data"
        const val ARGS_IS_MY_MISSING_DATA                    = "args_is_my_missing_data"

        fun newInstance(categoryUid: Int, isMyMissing: Boolean): MissingFragment {
            val args = Bundle()
            val fragment = MissingFragment()
            args.putInt(ARGS_CATEGORY_UID_DATA, categoryUid)
            args.putBoolean(ARGS_IS_MY_MISSING_DATA, isMyMissing)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getCategoryUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_CATEGORY_UID_DATA) as Int
    }

    private fun getIsMyMissing(): Boolean {
        val arguments = arguments
        return arguments?.getBoolean(ARGS_IS_MY_MISSING_DATA) as Boolean
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMissingAdapter = MissingAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_missing, container, false)

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

//        // Header Select Language Spinner
//        mSelectLanguageSpinner = mContainer.layout_header_select_language_spinner
//        mSelectLanguageSpinner.apply {
//            setSpinnerAdapter(IconSpinnerAdapter(this))
//            setItems(
//                arrayListOf(
//                    IconSpinnerItem("", iconRes = R.drawable.flag_korean_2),
////                    IconSpinnerItem(text = requireActivity().resources.getString(R.string.text_filipino), iconRes = R.drawable.flag_filipino_2),
//                    IconSpinnerItem("", iconRes = R.drawable.flag_english_2),
//                    IconSpinnerItem("", iconRes = R.drawable.flag_chinese_2),
//                    IconSpinnerItem("", iconRes = R.drawable.flag_japanese_2)
//                ))
//            getSpinnerRecyclerView().layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
//
//            var selectLanguage = 0
//            if (SharedPreferencesUtil().getSelectLanguage(mActivity) == SELECT_LANGUAGE_KO) {
//                selectLanguage = 0
//            } else if (SharedPreferencesUtil().getSelectLanguage(mActivity) == SELECT_LANGUAGE_EN) {
//                selectLanguage = 1
//            } else if (SharedPreferencesUtil().getSelectLanguage(mActivity) == SELECT_LANGUAGE_ZH) {
//                selectLanguage = 2
//            } else if (SharedPreferencesUtil().getSelectLanguage(mActivity) == SELECT_LANGUAGE_JA) {
//                selectLanguage = 3
//            }
//            selectItemByIndex(selectLanguage)
//            lifecycleOwner = this@MissingFragment
//        }
//        mSelectLanguageSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<IconSpinnerItem?> { oldIndex, oldItem, newIndex, newItem ->
//
//            when (newIndex) {
//                0 -> {
//                    SharedPreferencesUtil().setSelectLanguage(mActivity, SELECT_LANGUAGE_KO)
//                }
//
//                1 -> {
//                    SharedPreferencesUtil().setSelectLanguage(mActivity, SELECT_LANGUAGE_EN)
//                }
//
//                2 -> {
//                    SharedPreferencesUtil().setSelectLanguage(mActivity, SELECT_LANGUAGE_ZH)
//                }
//
//                3 -> {
//                    SharedPreferencesUtil().setSelectLanguage(mActivity, SELECT_LANGUAGE_JA)
//                }
//
//                else -> {
//                    SharedPreferencesUtil().setSelectLanguage(mActivity, SELECT_LANGUAGE_EN)
//                }
//            }
//
//            selectLanguage(mActivity, SharedPreferencesUtil().getSelectLanguage(mActivity)!!)
//        })

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
        titleTextView.text = mActivity.resources.getString(R.string.text_report_and_missing_person)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_missing_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mItemTotalCount = 0

                setTabView(mSelectType, true)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Scroll View
        mScrollView = mContainer.fragment_missing_scroll_view
        mScrollView.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (!v.canScrollVertically(1)) {
                if (!mIsEndOfScroll) {
                    if (mMissingListData.size >= mItemTotalCount) {
                        return@OnScrollChangeListener
                    }

                    mIsEndOfScroll = true
                    getBoardList(mSelectType, false)
                }
            }
        })

        // Missing Report Text View
        mMissingReportTextView = mContainer.fragment_missing_report_text_view
        mMissingReportTextView.setOnClickListener {
            setTabView(SELECT_TYPE_MISSING_REPORT, false)
        }

        // Missing Report Under-Line View
        mMissingReportUnderLineView = mContainer.fragment_missing_report_under_line_view

        // Missing Person Text View
        mMissingPersonTextView = mContainer.fragment_missing_person_text_view
        mMissingPersonTextView.setOnClickListener {
            setTabView(SELECT_TYPE_MISSING_PERSON, false)
        }

        // Missing Person Under-Line View
        mMissingPersonUnderLineView = mContainer.fragment_missing_person_under_line_view

        // Search View
        mSearchView = mContainer.fragment_missing_search_view
        if (getIsMyMissing()) {
            mSearchView.visibility = View.GONE
        }

        // Search Edit Text
        mSearchEditText = mContainer.fragment_missing_search_edit_text
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

                    getBoardList(mSelectType, true)
                    mMissingRecyclerView.scrollToPosition(0)

                    hideKeyboard(mActivity)
                    return true
                }
                return false
            }
        })

        // Search Confirm Text View
        val searchConfirmTextView = mContainer.fragment_missing_search_confirm_view
        searchConfirmTextView.setOnSingleClickListener {
            mIsEndOfScroll = true
            mPageCount = 1
            mItemTotalCount = 0

            mIsFirst = true

            getBoardList(mSelectType, true)
            mMissingRecyclerView.scrollToPosition(0)

            hideKeyboard(mActivity)
        }

        // Total Count Text View
        mTotalCountTextView = mContainer.fragment_missing_total_count_text_view

        // Sort Type Spinner
        mSortTypeSpinner = mContainer.fragment_missing_sort_type_spinner
        mSortTypeSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mSortType = newIndex

            mIsEndOfScroll = true
            mPageCount = 1
            mItemTotalCount = 0

            mIsFirst = true

            getBoardList(mSelectType, true)

            mMissingRecyclerView.scrollToPosition(0)
        })

        // Missing Recycler View
        mMissingRecyclerView = mContainer.fragment_missing_recycler_view
        mMissingRecyclerView.setHasFixedSize(true)
        mMissingRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mMissingAdapter.setOnMissingItemClickListener(object : MissingAdapter.OnMissingItemClickListener {
            override fun onClick(data: BoardData) {
                mListener.onMissingDetailScreen(data.getBoardUid())

                if (mSortTypeSpinner.isShowing) {
                    mSortTypeSpinner.dismiss()
                }
            }
        })
        mMissingRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mMissingRecyclerView.adapter = mMissingAdapter

        // Missing Report Text View
        mMissingReportWriteTextView = mContainer.fragment_missing_note_report_text_view
        mMissingReportWriteTextView.setOnClickListener {

            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                mIsRegister = true
                mListener.onMissingWriteScreen(mSelectType, false)
            }
        }

        // Loading View
        mLoadingView = mContainer.fragment_missing_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_missing_loading_lottie_view

        // Empty List View
        mEmptyListView = mContainer.fragment_missing_list_empty_view





        if (mIsPause) {
            mIsPause = false
            setTabView(mSelectType, false)
        } else {
            setTabView(SELECT_TYPE_MISSING_REPORT, true)
        }



//        if (mIsShare) {
            if ((mMainMenuItemData.getShareBoardUid() > 0) && (mMainMenuItemData.getShareBoardUid() < COMPANY_TYPE_MAIN) ) {
                mListener.onMissingDetailScreen(mMainMenuItemData.getShareBoardUid())
            }
//            mIsShare = false
//        }



//        NagajaLog().d("wooks, latitude = ${geoCoding("세부 시티마트").latitude}")
//        NagajaLog().d("wooks, longitude = ${geoCoding("세부 시티마트").longitude}")
    }

    private fun geoCoding(address: String): Location {
        return try {
            Geocoder(mActivity, Locale.KOREA).getFromLocationName(address, 1)?.let{
                Location("").apply {
                    latitude =  it[0].latitude
                    longitude = it[0].longitude
                }
            }?: Location("").apply {
                latitude = 0.0
                longitude = 0.0
            }
        } catch (e:Exception) {
            e.printStackTrace()
            geoCoding(address)
        }
    }

    private fun setTabView(type: Int, isFirstTime: Boolean) {

        when (type) {
            SELECT_TYPE_MISSING_REPORT -> {
                mMissingReportTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mMissingReportTextView.setTypeface(mMissingReportTextView.typeface, Typeface.BOLD)
                mMissingReportUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mMissingPersonTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mMissingPersonTextView.setTypeface(mMissingPersonTextView.typeface, Typeface.NORMAL)
                mMissingPersonUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mMissingReportWriteTextView.visibility = View.VISIBLE
            }

            SELECT_TYPE_MISSING_PERSON -> {
                mMissingReportTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mMissingReportTextView.setTypeface(mMissingReportTextView.typeface, Typeface.NORMAL)
                mMissingReportUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mMissingPersonTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mMissingPersonTextView.setTypeface(mMissingPersonTextView.typeface, Typeface.BOLD)
                mMissingPersonUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

//                if (MAPP.USER_DATA.getMemberGrant() == 3 || MAPP.USER_DATA.getMemberGrant() == 7 || MAPP.USER_DATA.getMemberGrant() == 9) {
//                    mMissingReportWriteTextView.visibility = View.VISIBLE
//                } else {
//                    mMissingReportWriteTextView.visibility = View.GONE
//                }

                mMissingReportWriteTextView.visibility = View.GONE
             }
        }

        if (getIsMyMissing()) {
            mMissingReportWriteTextView.visibility = View.GONE
        }

        mPageCount = 1
        mSelectType = type

        if (isFirstTime) {
            mSortTypeSpinner.selectItemByIndex(mSortType)
        } else {
            getBoardList(type, true)
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

    override fun onResume() {
        super.onResume()

        if (mIsRegister) {
            init()
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
        if (context is OnMissingFragmentListener) {
            mActivity = context as Activity

            if (context is OnMissingFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnMissingFragmentListener"
                )
            }
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        if (context is OnMissingFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnMissingFragmentListener"
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
     * Get Board List
     * */
    private fun getBoardList(boardType: Int, isRefresh: Boolean) {

        if (getIsMyMissing()) {
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
                        mMissingAdapter.setData(resultData)

                        mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), "0")

                        mMissingRecyclerView.visibility = View.GONE
                        mEmptyListView.visibility = View.VISIBLE

                        return
                    } else {
                        mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), resultData[0].getTotalCount().toString())

                        mMissingRecyclerView.visibility = View.VISIBLE
                        mEmptyListView.visibility = View.GONE
                    }

                    if (isRefresh) {
                        mMissingListData = ArrayList<BoardData>()
                        mMissingListData = resultData

                        mIsFirst = false
                    } else {
                        if (mMissingListData.size == 0) {
                            mMissingListData = resultData
                        } else {
                            mMissingListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mMissingListData[0].getTotalCount()

                    mIsEndOfScroll = false
                    mMissingAdapter.setData(mMissingListData)

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
            mSearchEditText.text.toString(),
            mSortType,
            boardType,
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            "missing",
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
                        mMissingAdapter.setData(resultData)

                        mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), "0")

                        mMissingRecyclerView.visibility = View.GONE
                        mEmptyListView.visibility = View.VISIBLE

                        return
                    } else {
                        mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), resultData[0].getTotalCount().toString())

                        mMissingRecyclerView.visibility = View.VISIBLE
                        mEmptyListView.visibility = View.GONE
                    }

                    if (isRefresh) {
                        mMissingListData = ArrayList<BoardData>()
                        mMissingListData = resultData

                        mIsFirst = false
                    } else {
                        if (mMissingListData.size == 0) {
                            mMissingListData = resultData
                        } else {
                            mMissingListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mMissingListData[0].getTotalCount()

                    mIsEndOfScroll = false
                    mMissingAdapter.setData(mMissingListData)

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
            mSortType,
            boardType,
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            true
        )
    }
}