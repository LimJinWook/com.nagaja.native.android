package com.nagaja.app.android.Playground

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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Adapter.RegularAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.RegularData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_playground.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class PlaygroundFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View
    private lateinit var mCompanyInformationView: View

    private lateinit var mCompanyImageView: SimpleDraweeView

    private lateinit var mCompanyNameTextView: TextView
    private lateinit var mCompanyTypeTextView: TextView
    private lateinit var mCompanyManagerTextView: TextView
    private lateinit var mCompanyAddressTextView: TextView

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mRegularAdapter: RegularAdapter

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mListener: OnPlaygroundFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private var mRegularListData = ArrayList<RegularData>()

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    private var mPageCount = 1
    private var mItemTotalCount = 0
    private var mCompanyUid = 0

    interface OnPlaygroundFragmentListener {
        fun onFinish()
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_CATEGORY_UID_DATA                = "args_category_uid_data"

        fun newInstance(categoryUid: Int): PlaygroundFragment {
            val args = Bundle()
            val fragment = PlaygroundFragment()
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

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_playground, container, false)

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
        titleTextView.text = mActivity.resources.getString(R.string.text_playground)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
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

//        if (mLoadingView.visibility == View.VISIBLE) {
//            mLoadingView.visibility = View.GONE
//        }
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
        if (context is OnPlaygroundFragmentListener) {
            mActivity = context as Activity

            if (context is OnPlaygroundFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnPlaygroundFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnPlaygroundFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnPlaygroundFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    // =================================================================================
    // API
    // =================================================================================


}