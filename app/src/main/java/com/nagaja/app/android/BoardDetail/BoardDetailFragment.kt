package com.nagaja.app.android.BoardDetail

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Utils.NetworkProvider.Companion.IMAGE_VIEW
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.BoardDetail.BoardDetailActivity.Companion.mIsBookmarkChange
import com.nagaja.app.android.BoardDetail.BoardDetailActivity.Companion.mIsLikeBookmarkChange
import com.nagaja.app.android.BoardDetail.BoardDetailActivity.Companion.mIsLikeChange
import com.nagaja.app.android.Data.BoardData
import com.nagaja.app.android.Data.CommentData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogCommentDelete
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_board.view.*
import kotlinx.android.synthetic.main.fragment_board_detail.view.*
import kotlinx.android.synthetic.main.fragment_recommend_store_detail.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class BoardDetailFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View
    private lateinit var mGoodView: View
    private lateinit var mBookmarkView: View
    private lateinit var mReportView: View

    private lateinit var mScrollView: NestedScrollView


    private lateinit var mWriteCommentEditText: EditText

    private lateinit var mCategoryTextView: TextView
    private lateinit var mBoardTitleTextView: TextView
    private lateinit var mNickNameTextView: TextView
    private lateinit var mDateTextView: TextView
    private lateinit var mViewCountTextView: TextView
    private lateinit var mContentTextView: TextView
    private lateinit var mGoodCountTextView: TextView
    private lateinit var mCommentCountTextView: TextView
    private lateinit var mBookmarkCountTextView: TextView
    private lateinit var mCommentListCountTextView: TextView
    private lateinit var mDeleteTextView: TextView
    private lateinit var mModifyTextView: TextView
    private lateinit var mCommentWriteTextView: TextView
    private lateinit var mCommentWordCountTextView: TextView

    private lateinit var mGoodImageView: ImageView
    private lateinit var mBookmarkImageView: ImageView
    private lateinit var mReportImageView: ImageView

    private lateinit var mImageRecyclerView: RecyclerView
    private lateinit var mCommentRecyclerView: RecyclerView

    private lateinit var mImageAdapter: StoreMainImageAdapter
    private lateinit var mCommentAdapter: CommentAdapter

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mListener: OnBoardFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private lateinit var mBoardData: BoardData

    private val mMainImageList = ArrayList<String>()
    private val mOriginImageList = ArrayList<String>()
    private var mCommentListData = ArrayList<CommentData>()

    private var mPageCount = 1
    private var mItemTotalCount = 0

    private var mIsEndOfScroll = false

    interface OnBoardFragmentListener {
        fun onFinish()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
        fun onWriteBoardScreen(categoryUid: Int, boardUid: Int)
        fun onReportScreen(reportType: Int, reviewUid: Int, storeName: String)
        fun onSuccessDelete()
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {
        const val ARGS_BOARD_UID                    = "args_board_uid"

        fun newInstance(boardUid: Int): BoardDetailFragment {
            val args = Bundle()
            val fragment = BoardDetailFragment()
            args.putInt(ARGS_BOARD_UID, boardUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getBoardUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_BOARD_UID) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mImageAdapter = StoreMainImageAdapter(mActivity)
        mCommentAdapter = CommentAdapter(mActivity, false)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_board_detail, container, false)

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
            NagajaLog().e("wooks, mBoardData.getCategoryUid() = ${mBoardData.getCategoryUid()}")
            NagajaLog().e("wooks, getBoardUid() = ${getBoardUid()}")
            setShareUrl(mActivity, COMPANY_TYPE_TALK_ROOM, getBoardUid())
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
        backImageView.visibility = View.VISIBLE
        backImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_board_category_talking_room)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        mScrollView = mContainer.fragment_board_detail_scroll_view
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

        // Category Text View
        mCategoryTextView = mContainer.fragment_board_detail_category_view

        // Board Title Text View
        mBoardTitleTextView = mContainer.fragment_board_detail_title_text_view

        // Nick Name Text View
        mNickNameTextView = mContainer.fragment_board_detail_nick_name_text_view

        // Date Text View
        mDateTextView = mContainer.fragment_board_detail_date_text_view

        // View Count Text View
        mViewCountTextView = mContainer.fragment_board_detail_view_count_text_view

        // Image Recycler View
        mImageRecyclerView = mContainer.fragment_board_detail_image_recycler_view
        mImageRecyclerView.setHasFixedSize(true)
        mImageRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        mImageAdapter.setOnImageClickListener(object : StoreMainImageAdapter.OnImageClickListener {
            override fun onClick(position: Int) {
                mListener.onFullScreenImage(mOriginImageList, position)
            }
        })
        mImageRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mImageRecyclerView.adapter = mImageAdapter

        // Content Text View
        mContentTextView = mContainer.fragment_board_detail_content_text_view

        // Good Count Text View
        mGoodCountTextView = mContainer.fragment_board_detail_good_count_text_view

        // Comment Count Text View
        mCommentCountTextView = mContainer.fragment_board_detail_comment_count_text_view

        // Bookmark Count Text View
        mBookmarkCountTextView = mContainer.fragment_board_detail_bookmark_count_text_view

        // Good View
        mGoodView = mContainer.fragment_board_detail_good_view
        mGoodView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                getRecommendSave()
            }
        }

        mGoodImageView = mContainer.fragment_board_detail_good_image_view

        // Bookmark View
        mBookmarkView = mContainer.fragment_board_detail_bookmark_view
        mBookmarkView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                getBookmarkSave()
            }
        }

        mBookmarkImageView = mContainer.fragment_board_detail_bookmark_image_view

        // Report View
        mReportView = mContainer.fragment_board_detail_report_view
        mReportView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                mListener.onReportScreen(REPORT_TYPE_BOARD, mBoardData.getBoardUid(), "")
            }
        }

        mReportImageView = mContainer.fragment_board_detail_report_image_view





        // Comment List Count Text View
        mCommentListCountTextView = mContainer.fragment_board_detail_comment_list_count_text_view

        // Comment Recycler View
        mCommentRecyclerView = mContainer.fragment_board_detail_comment_recycler_view
        mCommentRecyclerView.setHasFixedSize(false)
        mCommentRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mCommentAdapter.setOnNewsItemClickListener(object : CommentAdapter.OnNewsItemClickListener {
            override fun onDelete(commentUid: Int, position: Int) {
                showDeleteDialog(true, commentUid, position)
            }

            override fun onReport(commentUid: Int, position: Int) {
                if (MAPP.IS_NON_MEMBER_SERVICE) {
                    showLoginGuideCustomDialog()
                } else {
                    mListener.onReportScreen(REPORT_TYPE_COMMENT, commentUid, "")
                }
            }

        })
        mCommentRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mCommentRecyclerView.adapter = mCommentAdapter

        // Write Comment Edit Text
        mWriteCommentEditText = mContainer.fragment_board_detail_write_comment_edit_text
        mWriteCommentEditText.filters = arrayOf(InputFilter.LengthFilter(200))
        mWriteCommentEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (TextUtils.isEmpty(mWriteCommentEditText.text)) {
                    mCommentWordCountTextView.text = "0"
                } else {
                    mCommentWordCountTextView.text = mWriteCommentEditText.text.length.toString()
                }
            }
        })
//        mWriteCommentEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
//            if (hasFocus) {
//                Handler(Looper.getMainLooper()).postDelayed({
//                    mScrollView.post(Runnable { mScrollView.fullScroll(ScrollView.FOCUS_DOWN) })
//                }, 200)
//            }
//        }

        // Comment Send Image View
        val commentSendImageView = mContainer.fragment_board_detail_write_comment_send_image_view
        commentSendImageView.setOnSingleClickListener {
            if (TextUtils.isEmpty(mWriteCommentEditText.text)) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_input_comment))
                return@setOnSingleClickListener
            }

            getBoardCommentWrite()
        }


        // Delete Text View
        mDeleteTextView = mContainer.fragment_board_detail_delete_text_view
        mDeleteTextView.setOnSingleClickListener {
            showDeleteDialog(false, 0, 0)
        }

        // Modify Text View
        mModifyTextView = mContainer.fragment_board_detail_modify_text_view
        mModifyTextView.setOnSingleClickListener {
            mListener.onWriteBoardScreen(COMPANY_TYPE_FREE_BOARD, mBoardData.getBoardUid())
        }

        // Comment Write Text View
        mCommentWriteTextView = mContainer.fragment_board_detail_comment_write_text_view
        mCommentWriteTextView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    mScrollView.post(Runnable { mScrollView.fullScroll(ScrollView.FOCUS_DOWN) })
                }, 100)

                Handler(Looper.getMainLooper()).postDelayed({
                    mWriteCommentEditText.post(Runnable {
                        mWriteCommentEditText.isFocusableInTouchMode = true
                        mWriteCommentEditText.requestFocus()
                        val imm: InputMethodManager? = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                        imm!!.showSoftInput(mWriteCommentEditText, 0)
                    })
                }, 200)
            }
        }

        // Comment Word Count Text View
        mCommentWordCountTextView = mContainer.fragment_board_detail_comment_word_count_text_view

        // Loading View
        mLoadingView = mContainer.fragment_board_detail_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_board_detail_loading_lottie_view




        getBoardDetail()
    }

    private fun displayBoardData(data: BoardData) {

        // Category Text View
        if (!TextUtils.isEmpty(data.getCategoryName())) {
            mCategoryTextView.text = data.getCategoryName()
        }

        // Board Title Text View
        if (!TextUtils.isEmpty(data.getBoardSubject())) {
            mBoardTitleTextView.text = data.getBoardSubject()
        }

        // Nick Name Text View
        if (!TextUtils.isEmpty(data.getMemberName())) {
            mNickNameTextView.text = data.getMemberName()
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
        if (data.getViewCount() > 0) {
            mViewCountTextView.text = String.format(mActivity.resources.getString(R.string.text_board_view_count), data.getViewCount().toString())
        } else {
            mViewCountTextView.text = String.format(mActivity.resources.getString(R.string.text_board_view_count), "0")
        }

        // Main Image List
        mMainImageList.clear()
        mOriginImageList.clear()
        for (i in data.getBoardImageListData().indices) {
            mMainImageList.add(DEFAULT_IMAGE_DOMAIN + IMAGE_VIEW + data.getBoardImageListData()[i].getItemImageName())
            mOriginImageList.add(DEFAULT_IMAGE_DOMAIN + data.getBoardImageListData()[i].getItemImageName())
        }
        mImageAdapter.setData(mMainImageList)

        // Content Text View
        if (!TextUtils.isEmpty(data.getBoardContent())) {
            mContentTextView.text = data.getBoardContent()
        }

        // Good Count Text View
        if (data.getRecommendCount() > 0) {
            mGoodCountTextView.text = String.format(mActivity.resources.getString(R.string.text_good_count), data.getRecommendCount().toString())
        } else {
            mGoodCountTextView.text = String.format(mActivity.resources.getString(R.string.text_good_count), "0")
        }

        // Comment Count Text View
        if (data.getCommentCount() > 0) {
            mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), data.getCommentCount().toString())
        } else {
            mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), "0")
        }

        // Bookmark Count Text View
        if (data.getBookMarkCount() > 0) {
            mBookmarkCountTextView.text = String.format(mActivity.resources.getString(R.string.text_bookmark_count), data.getBookMarkCount().toString())
        } else {
            mBookmarkCountTextView.text = String.format(mActivity.resources.getString(R.string.text_bookmark_count), "0")
        }

        // Good Image View
        mIsLikeChange = data.getIsRecommend()
        mGoodImageView.setImageResource(if (data.getIsRecommend()) R.drawable.icon_store_regula else R.drawable.icon_store_regula_2)

        // Bookmark Image View
        mIsBookmarkChange = data.getIsBookMark()
        mBookmarkImageView.setImageResource(if (data.getIsBookMark()) R.drawable.icon_bookmark_star_disable else R.drawable.icon_bookmark_star)

        // Report Image View
        mReportImageView.setImageResource( if (data.getIsReport()) R.drawable.icon_report_enable else R.drawable.icon_note_report )

        // Comment List Count Text View
        if (data.getCommentCount() > 0) {
            mCommentListCountTextView.text = data.getCommentCount().toString()
        } else {
            mCommentListCountTextView.text = "0"
        }

        if (MAPP.USER_DATA.getMemberUid() == data.getMemberUid()) {
            mDeleteTextView.visibility = View.VISIBLE
            mModifyTextView.visibility = View.VISIBLE
        } else {
            mDeleteTextView.visibility = View.GONE
            mModifyTextView.visibility = View.GONE
        }
    }

    private fun showDeleteDialog(isComment: Boolean, commentUid: Int, position: Int) {
        val customDialogCommentDelete = CustomDialogCommentDelete(mActivity, isComment)
        customDialogCommentDelete.setOnCommentDeleteDialogListener(object : CustomDialogCommentDelete.OnCommentDeleteDialogListener {
            override fun onMoreView() {
                customDialogCommentDelete.dismiss()
            }

            override fun onDelete() {
                if (isComment) {
                    getBoardCommentDelete(commentUid, position)
                    customDialogCommentDelete.dismiss()
                } else {
                    getBoardDelete()
                }

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

    fun successReport() {
        mReportImageView.setImageResource(R.drawable.icon_report_enable)
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

    // =================================================================================
    // API
    // =================================================================================

    /**
     * API. Get Board Detail
     */
    fun getBoardDetail() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardDetail(
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
            MAPP.SELECT_LANGUAGE_CODE
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

                    setLoadingView(false)

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mCommentAdapter.setData(resultData)

                        mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), "0")
                        return
                    }

                    if (isRefresh) {
                        mCommentListData.clear()
                        mCommentListData = resultData
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
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            getBoardUid(),
            mPageCount,
            10,
            false
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

                    mWriteCommentEditText.setText("")
                    hideKeyboard(mActivity)

                    mBoardData.setCommentCount(mBoardData.getCommentCount() + 1)
                    mCommentListCountTextView.text = mBoardData.getCommentCount().toString()

                    mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), mItemTotalCount.toString())
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
            mWriteCommentEditText.text.toString(),
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
                    if (mItemTotalCount < 0) {
                        mItemTotalCount = 0
                    }
                    mCommentListData.removeAt(position)
                    mCommentAdapter.deleteItem(position)

                    if (mItemTotalCount < 0) {
                        mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), "0")
                        mCommentListCountTextView.text = "0"
                    } else {
                        mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), mItemTotalCount.toString())
                        mCommentListCountTextView.text = mItemTotalCount.toString()
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

    /**
     * API. Get Recommend Save
     */
    private fun getRecommendSave() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getRecommendSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mBoardData.setIsRecommend(!mBoardData.getIsRecommend())
                    mGoodImageView.setImageResource(if (mBoardData.getIsRecommend()) R.drawable.icon_store_regula else R.drawable.icon_store_regula_2)

                    if (!mBoardData.getIsRecommend()) {
                        mBoardData.setRecommendCount(mBoardData.getRecommendCount() - 1)
                    } else {
                        mBoardData.setRecommendCount(mBoardData.getRecommendCount() + 1)
                    }

                    if (mBoardData.getRecommendCount() > 0) {
                        mGoodCountTextView.text = String.format(mActivity.resources.getString(R.string.text_good_count), mBoardData.getRecommendCount().toString())
                    } else {
                        mGoodCountTextView.text = String.format(mActivity.resources.getString(R.string.text_good_count), "0")
                    }

                    mIsLikeBookmarkChange = true
                    mIsLikeChange = mBoardData.getIsRecommend()

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
            getBoardUid(),
            !mBoardData.getIsRecommend()
        )
    }

    /**
     * API. Get Bookmark Save
     */
    private fun getBookmarkSave() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBookmarkSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mBoardData.setIsBookMark(!mBoardData.getIsBookMark())
                    mBookmarkImageView.setImageResource( if (mBoardData.getIsBookMark()) R.drawable.icon_bookmark_star_disable else R.drawable.icon_bookmark_star )

                    if (!mBoardData.getIsBookMark()) {
                        mBoardData.setBookMarkCount(mBoardData.getBookMarkCount() - 1)
                    } else {
                        mBoardData.setBookMarkCount(mBoardData.getBookMarkCount() + 1)
                    }

                    if (mBoardData.getBookMarkCount() > 0) {
                        mBookmarkCountTextView.text = String.format(mActivity.resources.getString(R.string.text_bookmark_count), mBoardData.getBookMarkCount().toString())
                    } else {
                        mBookmarkCountTextView.text = String.format(mActivity.resources.getString(R.string.text_bookmark_count), "0")
                    }

                    mIsLikeBookmarkChange = true
                    mIsBookmarkChange = mBoardData.getIsBookMark()
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
            getBoardUid(),
            !mBoardData.getIsBookMark()
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
                    mListener.onSuccessDelete()
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
            getBoardUid()
        )
    }
}