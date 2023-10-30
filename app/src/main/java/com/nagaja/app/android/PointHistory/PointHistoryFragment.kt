package com.nagaja.app.android.PointHistory

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
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Adapter.PointHistoryAdapter
import com.nagaja.app.android.Adapter.RegularAdapter
import com.nagaja.app.android.Adapter.StoreCategoryAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaActivity.Companion.SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION
import com.nagaja.app.android.Base.NagajaActivity.Companion.SELECT_HEADER_MENU_TYPE_NOTIFICATION
import com.nagaja.app.android.Base.NagajaActivity.Companion.SELECT_HEADER_MENU_TYPE_SEARCH
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R
import com.nagaja.app.android.Util.ErrorRequest
import kotlinx.android.synthetic.main.fragment_company_used_market.view.*
import kotlinx.android.synthetic.main.fragment_notice.view.*

import kotlinx.android.synthetic.main.fragment_playground.view.*
import kotlinx.android.synthetic.main.fragment_point_history.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class PointHistoryFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mEmptyListView: View

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mPointHistoryRecyclerView: RecyclerView

    private lateinit var mPointHistoryAdapter: PointHistoryAdapter

    private lateinit var mListener: OnPointHistoryFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    interface OnPointHistoryFragmentListener {
        fun onFinish()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_COMPANY_UID_DATA                = "args_company_uid_data"

        fun newInstance(companyUid: Int): PointHistoryFragment {
            val args = Bundle()
            val fragment = PointHistoryFragment()
            args.putInt(ARGS_COMPANY_UID_DATA, companyUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getCompanyUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_COMPANY_UID_DATA) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)
        mPointHistoryAdapter = PointHistoryAdapter(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_point_history, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Header Location View
        val headerLocationView = mContainer.layout_header_location_view
        headerLocationView.setOnSingleClickListener {
            mListener.onChangeLocationAndNotificationScreen(SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION)
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
            mListener.onChangeLocationAndNotificationScreen(SELECT_HEADER_MENU_TYPE_SEARCH)
        }

        // Header Share Image View
        val headerShareImageView = mContainer.layout_header_clipboard_image_view
        headerShareImageView.visibility = View.GONE
//        headerShareImageView.setOnSingleClickListener {
//        }

        // Header Notification Image View
        val headerNotificationImageView = mContainer.layout_header_notification_image_view
        headerNotificationImageView.setOnSingleClickListener {
            mListener.onChangeLocationAndNotificationScreen(SELECT_HEADER_MENU_TYPE_NOTIFICATION)
        }

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_title_gp_history)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_point_history_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                getPointHistory()
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Point History Recycler View
        mPointHistoryRecyclerView = mContainer.fragment_point_history_recycler_view
        mPointHistoryRecyclerView.setHasFixedSize(true)
        mPointHistoryRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mPointHistoryAdapter.setOnItemClickListener(object : PointHistoryAdapter.OnItemClickListener {
            override fun onClick(data: PointHistoryData) {
            }
        })
        mPointHistoryRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mPointHistoryRecyclerView.adapter = mPointHistoryAdapter

        // Empty List View
        mEmptyListView = mContainer.fragment_point_history_list_empty_view

        getPointHistory()
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
        if (context is OnPointHistoryFragmentListener) {
            mActivity = context as Activity

            if (context is OnPointHistoryFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnPointHistoryFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnPointHistoryFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnPointHistoryFragmentListener"
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
     * API. Get Point History
     */
    private fun getPointHistory() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getPointHistory(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<PointHistoryData>> {
                override fun onSuccess(resultData: ArrayList<PointHistoryData>) {

                    mSwipeRefreshLayout.isRefreshing = false

                    if (resultData.isEmpty()) {
                        mPointHistoryRecyclerView.visibility = View.GONE
                        mEmptyListView.visibility = View.VISIBLE
                    } else {
                        mPointHistoryRecyclerView.visibility = View.VISIBLE
                        mEmptyListView.visibility = View.GONE
                        mPointHistoryAdapter.setData(resultData)
                    }
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    mSwipeRefreshLayout.isRefreshing = false
                }

                override fun onFail(errorCode: Int?) {
                    mSwipeRefreshLayout.isRefreshing = false
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            getCompanyUid()
        )
    }
}