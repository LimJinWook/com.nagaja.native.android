package com.nagaja.app.android.Missing

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.*
import com.nagaja.app.android.Utils.NetworkProvider
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogCommentDelete
import com.nagaja.app.android.Job.JobFragment
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_missing.view.*
import kotlinx.android.synthetic.main.fragment_missing_detail.view.*
import kotlinx.android.synthetic.main.fragment_recommend_store_detail.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


class MissingDetailFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mCommentEditText: EditText

    private lateinit var mMissingTypeTextView: TextView
    private lateinit var mMissingTitleTextView: TextView
    private lateinit var mDateTextView: TextView
    private lateinit var mViewCountTextView: TextView
    private lateinit var mContentTextView: TextView
    private lateinit var mAddressTextView: TextView
    private lateinit var mLocationDescTextView: TextView
    private lateinit var mCommentCountTextView: TextView
    private lateinit var mDeleteTextView: TextView
    private lateinit var mModifyTextView: TextView
    private lateinit var mCommentWordCountTextView: TextView

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mImageRecyclerView: RecyclerView
    private lateinit var mCommentRecyclerView: RecyclerView

    private lateinit var mMainImageAdapter: StoreMainImageAdapter
    private lateinit var mCommentAdapter: MissingCommentAdapter

    private lateinit var mListener: OnMissingDetailFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private val mMainImageList = ArrayList<String>()
    private var mCommentListData = ArrayList<CommentData>()

    private lateinit var mBoardData: BoardData

    private var mPageCount = 1
    private var mItemTotalCount = 0

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    interface OnMissingDetailFragmentListener {
        fun onBack()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
        fun onModifyScreen(boardUid: Int)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_BOARD_UID_DATA        = "args_board_uid_data"

        fun newInstance(boardUid: Int): MissingDetailFragment {
            val args = Bundle()
            val fragment = MissingDetailFragment()
            args.putInt(ARGS_BOARD_UID_DATA, boardUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getBoardUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_BOARD_UID_DATA) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMainImageAdapter = StoreMainImageAdapter(mActivity)
        mCommentAdapter = MissingCommentAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_missing_detail, container, false)

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

        // Header Search Image View
        val headerSearchImageView = mContainer.layout_header_search_image_view
        headerSearchImageView.setOnSingleClickListener {
            mListener.onChangeLocationAndNotificationScreen(NagajaActivity.SELECT_HEADER_MENU_TYPE_SEARCH)
        }

        // Header Share Image View
        val headerShareImageView = mContainer.layout_header_clipboard_image_view
        headerShareImageView.setOnSingleClickListener {
            setShareUrl(mActivity, COMPANY_TYPE_REPORT_DISAPPEARANCE, mBoardData.getBoardUid())
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
            mListener.onBack()
        }

        // Scroll View
        mScrollView = mContainer.fragment_missing_detail_scroll_view
        mScrollView.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (!v.canScrollVertically(1)) {
                if (!mIsEndOfScroll) {
                    if (mCommentListData.size >= mItemTotalCount) {
                        return@OnScrollChangeListener
                    }

                    mIsEndOfScroll = true
                    getBoardComment(false)
                }
            }
        })

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_missing_detail_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mItemTotalCount = 0

                getBoardComment(true)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Missing Type Text View
        mMissingTypeTextView = mContainer.fragment_missing_detail_type_text_view

        // Missing Title Text View
        mMissingTitleTextView = mContainer.fragment_missing_detail_title_text_view

        // Date Text View
        mDateTextView = mContainer.fragment_missing_detail_date_text_view

        // View Count Text View
        mViewCountTextView = mContainer.fragment_missing_detail_view_count_text_view

        // Image Recycler View
        mImageRecyclerView = mContainer.fragment_missing_detail_image_recycler_view
        mImageRecyclerView.setHasFixedSize(true)
        mImageRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        mMainImageAdapter.setOnImageClickListener(object : StoreMainImageAdapter.OnImageClickListener {
            override fun onClick(position: Int) {
                mListener.onFullScreenImage(mMainImageList, position)
            }
        })
        mImageRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mImageRecyclerView.adapter = mMainImageAdapter

        // Content Text View
        mContentTextView = mContainer.fragment_missing_detail_content_text_view

        // Comment Recycler View
        mCommentRecyclerView = mContainer.fragment_missing_detail_comment_recycler_view
        mCommentRecyclerView.setHasFixedSize(false)
        mCommentRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        mCommentAdapter.setOnItemClickListener(object : MissingCommentAdapter.OnItemClickListener {

            override fun onDelete(commentUid: Int, position: Int) {
                showDeleteDialog(commentUid, position)
            }

            override fun onReport(commentUid: Int, position: Int) {
            }
        })
        mCommentRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mCommentRecyclerView.adapter = mCommentAdapter

        // Comment Count Text View
        mCommentCountTextView = mContainer.fragment_missing_detail_comment_list_count_text_view

        // Comment Edit Text
        mCommentEditText = mContainer.fragment_missing_detail_write_comment_edit_text
        mCommentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(mCommentEditText.text)) {
                    mCommentWordCountTextView.text = "0"
                } else {
                    mCommentWordCountTextView.text = mCommentEditText.text.length.toString()
                }
            }
        })

        // Comment Send Image View
        val commentSendImageView = mContainer.fragment_missing_detail_write_comment_send_image_view
        commentSendImageView.setOnSingleClickListener {

            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                if (TextUtils.isEmpty(mCommentEditText.text)) {
                    showToast(mActivity, mActivity.resources.getString(R.string.text_error_input_comment))
                    return@setOnSingleClickListener
                }

                getBoardCommentWrite()
            }
        }

        // Comment Word Count Text View
        mCommentWordCountTextView = mContainer.fragment_missing_detail_comment_word_count_text_view

        // Address Text View
        mAddressTextView = mContainer.fragment_missing_detail_address_text_view

        // Location Text View
        mLocationDescTextView = mContainer.fragment_missing_detail_location_text_view

        // Delete Text View
        mDeleteTextView = mContainer.fragment_missing_detail_delete_text_view
        mDeleteTextView.setOnClickListener {
            showBoardDeletePopup()
        }

        // Modify Text View
        mModifyTextView = mContainer.fragment_missing_detail_modify_text_view
        mModifyTextView.setOnClickListener {
            mListener.onModifyScreen(mBoardData.getBoardUid())
        }

        // Write Comment Text View
        val writeCommentTextView = mContainer.fragment_missing_detail_write_comment_text_view
        writeCommentTextView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    mScrollView.post(Runnable { mScrollView.fullScroll(ScrollView.FOCUS_DOWN) })
                }, 200)

                mCommentEditText.post(Runnable {
                    mCommentEditText.isFocusableInTouchMode = true
                    mCommentEditText.requestFocus()
                    val imm: InputMethodManager? = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm!!.showSoftInput(mCommentEditText, 0)
                })
            }
        }




        getBoardDetail()
    }

    private fun setImageListData() {
        mMainImageList.clear()
        for (i in mBoardData.getBoardImageListData().indices) {
            mMainImageList.add(NetworkProvider.DEFAULT_IMAGE_DOMAIN + mBoardData.getBoardImageListData()[i].getItemImageName())
        }
        mMainImageAdapter.setData(mMainImageList)
    }

    @SuppressLint("SetTextI18n")
    private fun displayBoardData(data: BoardData) {

        // Missing Type Text View
        mMissingTypeTextView.text = if (data.getParentCategoryUid() == 47) mActivity.resources.getString(R.string.text_report_missing) else mActivity.resources.getString(R.string.text_public_interest_reporting)

        // Missing Title Text View
        if (!TextUtils.isEmpty(data.getBoardSubject())) {
            mMissingTitleTextView.text = data.getBoardSubject()
        }

        // Date Text View
        if (!TextUtils.isEmpty(data.getCreateDate())) {
            if (!TextUtils.isEmpty(data.getCreateDate())) {
                var time = data.getCreateDate()
                val index: Int = time.indexOf("+")
                time = time.substring(0, index)

                val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
                val zone = ZoneId.of(Util().getTimeZoneID(mActivity))
                val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

                mDateTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
            }
        }

        // View Count Text View
        mViewCountTextView.text = if (data.getViewCount() > 0) String.format(mActivity.resources.getString(R.string.text_board_view_count), data.getViewCount().toString()) else String.format(mActivity.resources.getString(R.string.text_board_view_count), "0")

        // Content Text View
        if (!TextUtils.isEmpty(data.getBoardContent())) {
            mContentTextView.text = data.getBoardContent()
        }

        // Address Text View
        var locationMainArea = ""
        var locationSubArea = ""
        var locationDesc = ""

        if (!TextUtils.isEmpty(data.getLocationMainAreaName())) {
            locationMainArea = data.getLocationMainAreaName()
        }
        if (!TextUtils.isEmpty(data.getLocationSubAreaName())) {
            locationSubArea = data.getLocationSubAreaName()
        }
        if (!TextUtils.isEmpty(data.getLocationDesc())) {
            locationDesc = data.getLocationDesc()
        }
        mAddressTextView.text = "$locationMainArea $locationSubArea"
        mLocationDescTextView.text = locationDesc

        // Delete, Modify Text View
        if (MAPP.USER_DATA.getMemberUid() == data.getMemberUid()) {
            mDeleteTextView.visibility = View.VISIBLE
            mModifyTextView.visibility = View.VISIBLE
        } else {
            mDeleteTextView.visibility = View.GONE
            mModifyTextView.visibility = View.GONE
        }

        setImageListData()
    }

    private fun showBoardDeletePopup() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_delete_report),
            mActivity.resources.getString(R.string.text_message_delete_my_report),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
                getBoardDelete()
            }
        })
        customDialog.show()
    }

    private fun showDeleteDialog(commentUid: Int, position: Int) {
        val customDialogCommentDelete = CustomDialogCommentDelete(mActivity, true)
        customDialogCommentDelete.setOnCommentDeleteDialogListener(object : CustomDialogCommentDelete.OnCommentDeleteDialogListener {
            override fun onMoreView() {
                customDialogCommentDelete.dismiss()
            }

            override fun onDelete() {
                getBoardCommentDelete(commentUid, position)
                customDialogCommentDelete.dismiss()
            }

            override fun onCancel() {
                customDialogCommentDelete.dismiss()
            }

        })
        customDialogCommentDelete.show(requireActivity().supportFragmentManager, "Custom Dialog")
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
        if (context is OnMissingDetailFragmentListener) {
            mActivity = context as Activity

            if (context is OnMissingDetailFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnMissingDetailFragmentListener"
                )
            }
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        if (context is OnMissingDetailFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnMissingDetailFragmentListener"
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
     * API. Get Board Detail
     */
    fun getBoardDetail() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardMissingAndJobDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<BoardData> {
                override fun onSuccess(resultData: BoardData) {
                    mBoardData = resultData
                    displayBoardData(resultData)

                    getBoardComment(true)
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
            getBoardUid(),
            true
        )
    }

    /**
     * API. Get Board Comment
     */
    private fun getBoardComment(isRefresh: Boolean) {
        if (isRefresh) {
            mPageCount = 1
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardComment(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<CommentData>> {
                override fun onSuccess(resultData: ArrayList<CommentData>) {

                    mSwipeRefreshLayout.isRefreshing = false

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mCommentAdapter.setData(resultData)

                        mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), "0")
                        return
                    }

                    if (isRefresh) {
                        mCommentListData.clear()
                        mCommentListData = resultData

                        mIsFirst = false
                    } else {
                        if (mCommentListData.size == 0) {
                            mCommentListData = resultData
                        } else {
                            mCommentListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mCommentListData[0].getTotalCount()

                    mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), mItemTotalCount.toString())

                    mIsEndOfScroll = false
                    mCommentAdapter.setData(mCommentListData)

                    mPageCount++
                }

                override fun onFail(errorMsg: String?) {
                    mSwipeRefreshLayout.isRefreshing = false
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                    mSwipeRefreshLayout.isRefreshing = false
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mBoardData.getBoardUid(),
            mPageCount,
            20,
            true
        )
    }

    /**
     * API. Get Board Comment Delete
     */
    private fun getBoardDelete() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardDelete(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    showToast(mActivity, mActivity.resources.getString(R.string.text_message_success_delete))
                    mListener.onBack()
//                    mListener.onSuccess(false)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }

                    showToast(mActivity, errorCode.toString())
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mBoardData.getBoardUid()
        )
    }

    /**
     * API. Get Board Comment Write
     */
    private fun getBoardCommentWrite() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardCommentWrite(
            mRequestQueue,
            object : NagajaManager.RequestListener<CommentData> {
                override fun onSuccess(resultData: CommentData) {

                    mItemTotalCount++
                    mCommentListData.add(resultData)
                    mCommentAdapter.setData(mCommentListData)

                    mCommentEditText.setText("")

                    if (mItemTotalCount < 0) {
                        mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), "0")
                    } else {
                        mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), mItemTotalCount.toString())
                    }

                    hideKeyboard(mActivity)
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
            mCommentEditText.text.toString(),
            getBoardUid()
        )
    }

    /**
     * API. Get Board Comment Delete
     */
    private fun getBoardCommentDelete(commentUid: Int, position: Int) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardCommentDelete(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mItemTotalCount--
                    mCommentListData.removeAt(position)
                    mCommentAdapter.deleteItem(position)

                    if (mItemTotalCount < 0) {
                        mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), "0")
                    } else {
                        mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), mItemTotalCount.toString())
                    }

                    showToast(mActivity, mActivity.resources.getString(R.string.text_success_comment_remove))
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
            commentUid
        )
    }
}