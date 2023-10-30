package com.nagaja.app.android.Search

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Adapter.SearchAdapter
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.SearchData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_faq.view.*
import kotlinx.android.synthetic.main.fragment_notice.view.*
import kotlinx.android.synthetic.main.fragment_notification.view.*
import kotlinx.android.synthetic.main.fragment_report.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.util.*

class SearchFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View
    private lateinit var mSearchEmptyView: View

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mSearchEditText: EditText

    private lateinit var mLocationTextView: TextView

    private lateinit var mSelectTypeSpinner: PowerSpinnerView

    private lateinit var mSearchRecyclerView: RecyclerView
    private lateinit var mSearchAdapter: SearchAdapter

    private lateinit var mListener: OnSearchFragmentListener

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mRequestQueue: RequestQueue

    private var mSearchListData = ArrayList<SearchData>()

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    private var mPageCount = 1
    private var mItemTotalCount = 0
    private var mSortType = 1
    private var mSelectType = -1

    interface OnSearchFragmentListener {
        fun onFinish()
        fun onItemDetailScreen(searchData: SearchData)
        fun onChangeLocationAndNotificationScreen(isChangeLocation : Boolean)
        fun onDefaultLoginScreen()
    }

    companion object {

        fun newInstance(): SearchFragment {
            val args = Bundle()
            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSearchAdapter = SearchAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_search, container, false)

        init()

        return mContainer
    }

    @SuppressLint("ClickableViewAccessibility")
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
        headerSearchImageView.setOnSingleClickListener {
//            val intent = Intent(mActivity, SearchActivity::class.java)
//            startActivity(intent)
        }

        // Header Share Image View
        val headerShareImageView = mContainer.layout_header_clipboard_image_view
        headerShareImageView.visibility = View.GONE
//        headerShareImageView.setOnSingleClickListener {
//        }

        // Header Notification Image View
        val headerNotificationImageView = mContainer.layout_header_notification_image_view
        headerNotificationImageView.visibility = View.GONE
        headerNotificationImageView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                mListener.onChangeLocationAndNotificationScreen(false)
            }
        }

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_search)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_search_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mItemTotalCount = 0

                getSearch(true)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Select Type Spinner
        mSelectTypeSpinner = mContainer.fragment_search_select_spinner
        mSelectTypeSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mSelectType = newIndex
        })

        // Search Edit Text
        mSearchEditText = mContainer.fragment_search_edit_text
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
                    if (mSelectType < 0) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_select_category))
                        return false
                    } else if (TextUtils.isEmpty(mSearchEditText.text)) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_search))
                        return false
                    }/* else if (mSearchEditText.text.length < 3) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_search_input_3_words))
                        return false
                    }*/

                    getSearch(true)
                    hideKeyboard(mActivity)
                    return true
                }
                return false
            }
        })

        // Search Confirm View
        val searchConfirmView = mContainer.fragment_search_confirm_view
        searchConfirmView.setOnClickListener {
            if (mSelectType < 0) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_select_category))
                return@setOnClickListener
            } else if (TextUtils.isEmpty(mSearchEditText.text)) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_search))
                return@setOnClickListener
            }/* else if (mSearchEditText.text.length < 3) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_search_input_3_words))
                return@setOnClickListener
            }*/

            getSearch(true)
            hideKeyboard(mActivity)
        }



        // Search Recycler View
        mSearchRecyclerView = mContainer.fragment_search_recycler_view
        mSearchRecyclerView.setHasFixedSize(true)
        mSearchRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mSearchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!mSearchRecyclerView.canScrollVertically(1) && !mSearchRecyclerView.canScrollVertically(-1)) {
                    return
                }

                if (!mSearchRecyclerView.canScrollVertically(1)) {
                    if (!mIsEndOfScroll) {
                        if (mSearchListData.size >= mItemTotalCount) {
                            return
                        }

                        mIsEndOfScroll = true
                        getSearch(false)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        mSearchAdapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onClick(data: SearchData) {
                mListener.onItemDetailScreen(data)
            }
        })
        mSearchRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mSearchRecyclerView.adapter = mSearchAdapter


        // Search Empty View
        mSearchEmptyView = mContainer.fragment_search_empty_view




        // Loading View
        mLoadingView = mContainer.fragment_search_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_search_loading_lottie_view

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

        if (mSelectTypeSpinner.isShowing) {
            mSelectTypeSpinner.dismiss()
        }
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
        if (context is OnSearchFragmentListener) {
            mActivity = context as Activity

            if (context is OnSearchFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnSearchFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnSearchFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnSearchFragmentListener"
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
     * API. Get Board Comment Delete
     */
    private fun getSearch(isRefresh: Boolean) {

        setLoadingView(true)

        if (isRefresh) {
            mPageCount = 1
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getSearch(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<SearchData>> {
                override fun onSuccess(resultData: ArrayList<SearchData>) {

                    setLoadingView(false)

                    mSwipeRefreshLayout.isRefreshing = false

                    if (resultData.isEmpty()) {
                        mSearchRecyclerView.visibility = View.GONE
                        mSearchEmptyView.visibility = View.VISIBLE

                        return
                    } else {
                        mSearchRecyclerView.visibility = View.VISIBLE
                        mSearchEmptyView.visibility = View.GONE
                    }

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mSearchAdapter.setData(resultData)
                        return
                    }

                    if (isRefresh) {
                        mSearchListData.clear()
                        mSearchListData = resultData

                        mIsFirst = false
                    } else {
                        if (mSearchListData.size == 0) {
                            mSearchListData = resultData
                        } else {
                            mSearchListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mSearchListData[0].getTotalCount()

                    mIsEndOfScroll = false
                    mSearchAdapter.setData(mSearchListData)

                    mPageCount++
                }

                override fun onFail(errorMsg: String?) {
                    setLoadingView(false)
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    setLoadingView(false)
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            mPageCount,
            20,
            mSelectType,
            mSearchEditText.text.toString(),
            MAPP.SELECT_LANGUAGE_CODE,
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity)
        )
    }

}