package com.nagaja.app.android.Regular

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Adapter.RegularAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.RegularData
import com.nagaja.app.android.Data.StoreDetailData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_faq.view.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.fragment_note.view.*
import kotlinx.android.synthetic.main.fragment_regular.view.*
import kotlinx.android.synthetic.main.fragment_setting_profile.view.*
import kotlinx.android.synthetic.main.fragment_store_list.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class RegularFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mRegularActivity: RegularActivity

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View
    private lateinit var mCompanyInformationView: View
    private lateinit var mEmptyListView: View

    private lateinit var mCompanyImageView: SimpleDraweeView

    private lateinit var mCompanyNameTextView: TextView
    private lateinit var mCompanyTypeTextView: TextView
    private lateinit var mCompanyManagerTextView: TextView
    private lateinit var mCompanyAddressTextView: TextView

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mRegularAdapter: RegularAdapter

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mListener: OnRegularFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private var mRegularListData = ArrayList<RegularData>()

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    private var mPageCount = 1
    private var mItemTotalCount = 0
    private var mCompanyUid = 0

    interface OnRegularFragmentListener {
        fun onFinish()
        fun onStoreScreen(companyUid: Int)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
        fun onChatViewScreen(chatClass: String, chatData: String)
    }

    companion object {
        fun newInstance(): RegularFragment {
            val args = Bundle()
            val fragment = RegularFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity: Activity = requireActivity()
        if (activity is RegularActivity) {
            mRegularActivity = activity
        }

        mRegularAdapter = RegularAdapter(mActivity, mRegularActivity.mIsCompanyNote)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_regular, container, false)

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
        titleTextView.text = if (mRegularActivity.mIsCompanyNote) mActivity.resources.getString(R.string.text_company_regular) else mActivity.resources.getString(R.string.text_regular)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        mScrollView = mContainer.fragment_regular_scroll_view
        mScrollView.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (!v.canScrollVertically(1)) {
                if (!mIsEndOfScroll) {
                    if (mRegularListData.size >= mItemTotalCount) {
                        return@OnScrollChangeListener
                    }

                    mIsEndOfScroll = true
                    getRegularList(false)
                }
            }
        })

        // Company Information View
        mCompanyInformationView = mContainer.fragment_regular_company_information_view
        mCompanyInformationView.visibility = if (mRegularActivity.mIsCompanyNote) View.VISIBLE else View.GONE

        // Company Image View
        mCompanyImageView = mContainer.fragment_regular_company_image_view

        // Company Name Text View
        mCompanyNameTextView = mContainer.fragment_regular_company_name_text_view

        // Company Type Txt View
        mCompanyTypeTextView = mContainer.fragment_regular_company_type_text_view

        // Company Manager Text View
        mCompanyManagerTextView = mContainer.fragment_regular_company_manager_text_view

        // Company Address Text View
        mCompanyAddressTextView = mContainer.fragment_regular_company_address_text_view

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_regular_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mItemTotalCount = 0

                getRegularList(true)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Recycler View
        mRecyclerView = mContainer.fragment_regular_recycler_view
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

//        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                if (!mRecyclerView.canScrollVertically(1) && !mRecyclerView.canScrollVertically(-1)) {
//                    return
//                }
//
//                if (!mRecyclerView.canScrollVertically(1)) {
//                    if (!mIsEndOfScroll) {
//                        if (mRegularListData.size >= mItemTotalCount) {
//                            return
//                        }
//
//                        mIsEndOfScroll = true
//                        getRegularList(false)
//                    }
//                }
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//            }
//        })

        mRegularAdapter.setOnRegularClickListener(object : RegularAdapter.OnRegularClickListener {
            override fun onClick(data: RegularData, position: Int) {
                showRegularDeletePopup(data, position)
            }

            override fun onShowStore(companyUid: Int) {
                mListener.onStoreScreen(companyUid)
            }

            override fun onChat(data: RegularData) {
                if (mRegularActivity.mIsCompanyNote) {
                    if (data.getMemberUid() > 0) {
                        val chatData = "[{${MAPP.USER_DATA.getMemberUid()}, ${data.getCompanyUid()}}, {${data.getMemberUid()}, 0}]"
                        if (!TextUtils.isEmpty(chatData)) {
                            mListener.onChatViewScreen(CHAT_CLASS_COMPANY, chatData)
                        }
                    }
                } else {
                    if (data.getCompanyUid() > 0) {
                        getStoreDetail(data.getCompanyUid())
                    }
                }
            }

            override fun onPhoneCall(phoneNumber: String) {
                if (TextUtils.isEmpty(phoneNumber)) {
                    showToast(mActivity, mActivity.resources.getString(R.string.text_error_empty_phone_number))
                    return
                }

                var number = phoneNumber.replace(" ", "")
                number = number.replace("-", "")
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                startActivity(intent)
            }
        })
        mRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mRecyclerView.adapter = mRegularAdapter

//        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
//        itemTouchHelper.attachToRecyclerView(mRecyclerView)

        // Loading View
        mLoadingView = mContainer.fragment_regular_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_regular_loading_lottie_view

        // Empty List View
        mEmptyListView = mContainer.fragment_regular_list_empty_view




        if (mRegularActivity.mIsCompanyNote) {
            setCompanyData()
        }

        getRegularList(true)
    }

    @SuppressLint("SetTextI18n")
    private fun setCompanyData() {
        if (!TextUtils.isEmpty(mRegularActivity.mCompanyDefaultData.getCompanyMainImageUrl())) {
            mCompanyImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + mRegularActivity.mCompanyDefaultData.getCompanyMainImageUrl()))
        }

        if (!TextUtils.isEmpty(mRegularActivity.mCompanyDefaultData.getCompanyName())) {
            mCompanyNameTextView.text = mRegularActivity.mCompanyDefaultData.getCompanyName()
        }

        if (mRegularActivity.mCompanyDefaultData.getCategoryUid() > 0) {
            mCompanyTypeTextView.text = Util().getCompanyType(mActivity, mRegularActivity.mCompanyDefaultData.getCategoryUid())
        }

        if (!TextUtils.isEmpty(mRegularActivity.mCompanyDefaultData.getManageName())) {
            mCompanyManagerTextView.text = mRegularActivity.mCompanyDefaultData.getManageName()
        }

        if (!TextUtils.isEmpty(mRegularActivity.mCompanyDefaultData.getCompanyAddress())) {
            mCompanyAddressTextView.text = mRegularActivity.mCompanyDefaultData.getCompanyAddress() + " " + mRegularActivity.mCompanyDefaultData.getCompanyAddressDetail()
        }

        mCompanyUid = mRegularActivity.mCompanyDefaultData.getCompanyUid()
    }

    var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            showToast(mActivity, "On Move")
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            showToast(mActivity, "On Swiped")
            val position = viewHolder.adapterPosition
//            arrayList.remove(position)
//            adapter.notifyDataSetChanged()
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

    private fun showRegularDeletePopup(data: RegularData, position: Int) {
        var companyName = ""
        if (mRegularActivity.mIsCompanyNote) {
            companyName = data.getMemberNickName()
        } else {
            if (!TextUtils.isEmpty(data.getCompanyName())) {
                companyName = data.getCompanyName()
            } else {
                companyName = data.getCompanyNameEnglish()
            }
        }

        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_delete_regular),
            companyName + "\n\n" + mActivity.resources.getString(R.string.text_message_regular_delete),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()

                getStoreRegularSave(data, position)
            }
        })
        customDialog.show()
    }

    private fun showDisableChatCustomPopup() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NO_CANCEL,
            mActivity.resources.getString(R.string.text_noti),
            mActivity.resources.getString(R.string.text_message_disable_chat),
            "",
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
            }
        })
        customDialog.show()
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

        if (mLoadingView.visibility == View.VISIBLE) {
            mLoadingView.visibility = View.GONE
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
        if (context is OnRegularFragmentListener) {
            mActivity = context as Activity

            if (context is OnRegularFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnRegularFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnRegularFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnRegularFragmentListener"
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
     * API. Get Regular List
     */
    private fun getRegularList(isRefresh: Boolean) {

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getRegularList(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<RegularData>> {
                override fun onSuccess(resultData: ArrayList<RegularData>) {

                    mSwipeRefreshLayout.isRefreshing = false

                    setLoadingView(false)

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mRegularAdapter.setData(resultData)

                        mRecyclerView.visibility = View.GONE
                        mEmptyListView.visibility = View.VISIBLE

                        return
                    } else {
                        mRecyclerView.visibility = View.VISIBLE
                        mEmptyListView.visibility = View.GONE
                    }

                    if (isRefresh) {
                        mRegularListData.clear()
                        mRegularListData = resultData

                        mIsFirst = false
                    } else {
                        if (mRegularListData.size == 0) {
                            mRegularListData = resultData
                        } else {
                            mRegularListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mRegularListData[0].getTotalCount()

                    mIsEndOfScroll = false
                    mRegularAdapter.setData(mRegularListData)

                    mPageCount++
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    setLoadingView(false)
                    mSwipeRefreshLayout.isRefreshing = false
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                    setLoadingView(false)
                    mSwipeRefreshLayout.isRefreshing = false
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            MAPP.SELECT_LANGUAGE_CODE,
            mPageCount,
            10,
            mCompanyUid,
            mRegularActivity.mIsCompanyNote
        )
    }

    /**
     * API. Get Store Regular Save
     */
    private fun getStoreRegularSave(data: RegularData, position: Int) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreRegularSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mRegularListData.removeAt(position)
                    mRegularAdapter.deleteItem(position)
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
            data.getCompanyUid(),
            0
        )
    }

    /**
     * API. Get Store Detail
     */
    private fun getStoreDetail(getCompanyUid: Int) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<StoreDetailData> {
                override fun onSuccess(resultData: StoreDetailData) {

                    if (resultData.getContractStatus() == 1) {
                        if (MAPP.IS_NON_MEMBER_SERVICE) {
                            showLoginGuideCustomDialog()
                        } else {
                            if (resultData.getCompanyUid() > 0) {
                                val chatData = "[{${MAPP.USER_DATA.getMemberUid()}, 0}, {${resultData.getMemberUid()}, ${resultData.getCompanyUid()}}]"
                                if (!TextUtils.isEmpty(chatData)) {
                                    mListener.onChatViewScreen(CHAT_CLASS_COMPANY, chatData)
                                }
                            }
                        }
                    } else {
                        showDisableChatCustomPopup()
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
            getCompanyUid.toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }
}