package com.nagaja.app.android.News

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.*
import android.text.Editable
import android.text.InputFilter
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
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Utils.NetworkProvider
import com.nagaja.app.android.Adapter.CommentAdapter
import com.nagaja.app.android.Adapter.StoreMainImageAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.CommentData
import com.nagaja.app.android.Data.NoticeData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogCommentDelete
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_board_detail.view.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.fragment_news_detail.view.*
import kotlinx.android.synthetic.main.fragment_note.view.*
import kotlinx.android.synthetic.main.fragment_note_detail.view.*
import kotlinx.android.synthetic.main.fragment_setting_profile.view.*
import kotlinx.android.synthetic.main.fragment_store_detail.view.*
import kotlinx.android.synthetic.main.fragment_used_market_list.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class NewsDetailFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View

    private lateinit var mCommentEditText: EditText

    private lateinit var mNewsTitleTextView: TextView
    private lateinit var mDateTextView: TextView
    private lateinit var mViewCountTextView: TextView
    private lateinit var mContentTextView: TextView
    private lateinit var mCommentCountTextView: TextView
    private lateinit var mCommentWordCountTextView: TextView


    private lateinit var mImageRecyclerView: RecyclerView
    private lateinit var mCommentRecyclerView: RecyclerView

    private lateinit var mStoreMainImageAdapter: StoreMainImageAdapter
    private lateinit var mNewsCommentAdapter: CommentAdapter

    private var mCommentListData = ArrayList<CommentData>()

    private val mMainImageList = ArrayList<String>()

    private lateinit var mListener: OnNoteDetailFragmentListener

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mRequestQueue: RequestQueue

    private var mPageCount = 1
    private var mItemTotalCount = 0

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    interface OnNoteDetailFragmentListener {
        fun onBack()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
        fun onReportScreen(reportType: Int, reviewUid: Int, storeName: String)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {
        const val ARGS_NOTICE_ITEM_UID_DATA              = "args_notice_item_uid_data"

        fun newInstance(itemUid: Int): NewsDetailFragment {
            val args = Bundle()
            val fragment = NewsDetailFragment()
            args.putInt(ARGS_NOTICE_ITEM_UID_DATA, itemUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getItemUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_NOTICE_ITEM_UID_DATA) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mStoreMainImageAdapter = StoreMainImageAdapter(mActivity)
        mNewsCommentAdapter = CommentAdapter(mActivity, true)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_news_detail, container, false)

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
            setShareUrl(mActivity, COMPANY_TYPE_NEWS, getItemUid())
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
        backImageView.setOnSingleClickListener {
            mListener.onBack()
        }

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_news)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        mScrollView = mContainer.fragment_news_detail_scroll_view
        mScrollView.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (!v.canScrollVertically(1)) {
                if (!mIsEndOfScroll) {
                    if (mCommentListData.size >= mItemTotalCount) {
                        return@OnScrollChangeListener
                    }

                    mIsEndOfScroll = true
                    getNoticeComment(false)
                }
            }
        })

        // News Title Text View
        mNewsTitleTextView = mContainer.fragment_news_detail_title_text_view

        // Date Text View
        mDateTextView = mContainer.fragment_news_detail_date_text_view

        // View Count Text View
        mViewCountTextView = mContainer.fragment_news_detail_view_count_text_view

        // Image Recycler View
        mImageRecyclerView = mContainer.fragment_news_detail_image_recycler_view
        mImageRecyclerView.setHasFixedSize(true)
        mImageRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        mStoreMainImageAdapter.setOnImageClickListener(object : StoreMainImageAdapter.OnImageClickListener {
            override fun onClick(position: Int) {
                mListener.onFullScreenImage(mMainImageList, position)
            }
        })
        mImageRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mImageRecyclerView.adapter = mStoreMainImageAdapter

        // Content Text View
        mContentTextView = mContainer.fragment_news_detail_content_text_view

        // Comment Count Text View
        mCommentCountTextView = mContainer.fragment_news_detail_comment_count_text_view

        // Recycler View
        mCommentRecyclerView = mContainer.fragment_news_detail_comment_recycler_view
        mCommentRecyclerView.setHasFixedSize(false)
        mCommentRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        mNewsCommentAdapter.setOnNewsItemClickListener(object : CommentAdapter.OnNewsItemClickListener {

            override fun onDelete(commentUid: Int, position: Int) {
                showDeleteDialog(commentUid, position)
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
        mCommentRecyclerView.adapter = mNewsCommentAdapter


        // Comment Edit Text
        mCommentEditText = mContainer.fragment_news_detail_comment_edit_text
        mCommentEditText.filters = arrayOf(InputFilter.LengthFilter(200))
        mCommentEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (TextUtils.isEmpty(mCommentEditText.text)) {
                    mCommentWordCountTextView.text = "0"
                } else {
                    mCommentWordCountTextView.text = mCommentEditText.text.length.toString()
                }
            }
        })

        // Comment Send Image View
        val commentSendImageView = mContainer.fragment_news_detail_comment_send_image_view
        commentSendImageView.setOnSingleClickListener {

            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                if (TextUtils.isEmpty(mCommentEditText.text)) {
                    showToast(mActivity, mActivity.resources.getString(R.string.text_error_input_comment))
                    return@setOnSingleClickListener
                }

                getNoticeCommentWrite()
            }
        }

        // Comment Word Count Text View
        mCommentWordCountTextView = mContainer.fragment_news_detail_comment_word_count_text_view

        // Comment Write Text View
        val commentWriteTextView = mContainer.fragment_news_detail_comment_write_text_view
        commentWriteTextView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    mScrollView.post(Runnable { mScrollView.fullScroll(ScrollView.FOCUS_DOWN) })
                }, 100)

                Handler(Looper.getMainLooper()).postDelayed({
                    mCommentEditText.post(Runnable {
                        mCommentEditText.isFocusableInTouchMode = true
                        mCommentEditText.requestFocus()
                        val imm: InputMethodManager? = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                        imm!!.showSoftInput(mCommentEditText, 0)
                    })
                }, 200)
            }
        }


        // Loading View
        mLoadingView = mContainer.fragment_news_detail_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_news_detail_loading_lottie_view

        getLocalNewsDetail()
    }

    private fun displayNewsData(data: NoticeData) {

        // News Title
        if (!TextUtils.isEmpty(data.getNoticeSubject())) {
            mNewsTitleTextView.text = data.getNoticeSubject()
        }

        // Date Text
        if (!TextUtils.isEmpty(data.getCreateDate())) {
            var time = data.getCreateDate()
            val index: Int = time.indexOf("+")
            time = time.substring(0, index)

            val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
            val zone = ZoneId.of(Util().getTimeZoneID(mActivity))
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            mDateTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
        }

        // View Count
        mViewCountTextView.text = if (data.getViewCount() > 0) data.getViewCount().toString() else "0"

        // Content
        if (!TextUtils.isEmpty(data.getNoticeContent())) {
            mContentTextView.text = data.getNoticeContent()
        }

        // Comment Count
        mCommentCountTextView.text = if (data.getCommentCount() > 0) data.getCommentCount().toString() else "0"

        // Image List
        for (i in data.getNoticeImageListData().indices) {
            mMainImageList.add(NetworkProvider.DEFAULT_IMAGE_DOMAIN + data.getNoticeImageListData()[i].getItemImageName())
        }
        mStoreMainImageAdapter.setData(mMainImageList)
    }

    private fun showDeleteDialog(commentUid: Int, position: Int) {
        val customDialogCommentDelete = CustomDialogCommentDelete(mActivity, true)
        customDialogCommentDelete.setOnCommentDeleteDialogListener(object : CustomDialogCommentDelete.OnCommentDeleteDialogListener {
            override fun onMoreView() {
                customDialogCommentDelete.dismiss()
            }

            override fun onDelete() {
                getNoticeCommentDelete(commentUid, position)
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
        if (context is OnNoteDetailFragmentListener) {
            mActivity = context as Activity

            if (context is OnNoteDetailFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnNoteDetailFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnNoteDetailFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnNoteDetailFragmentListener"
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
     * API. Get Local News Detail
     */
    private fun getLocalNewsDetail() {

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoticeDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<NoticeData> {
                override fun onSuccess(resultData: NoticeData) {
                    displayNewsData(resultData)
                    getNoticeComment(true)
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
            getItemUid()
        )
    }

    /**
     * API. Get Notice Comment
     */
    private fun getNoticeComment(isRefresh: Boolean) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoticeComment(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<CommentData>> {
                override fun onSuccess(resultData: ArrayList<CommentData>) {

                    setLoadingView(false)

//                    mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), resultData[0].getTotalCount().toString())

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mNewsCommentAdapter.setData(resultData)
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

//                    if (mCommentListData.size == 0) {
//                        mCommentListData = resultData
//                    } else {
//                        mCommentListData.addAll(resultData)
//                    }

                    mItemTotalCount = mCommentListData[0].getTotalCount()

                    mIsEndOfScroll = false
                    mNewsCommentAdapter.setData(mCommentListData)

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
            getItemUid(),
            mPageCount,
            10
        )
    }

    /**
     * API. Get Notice Comment Write
     */
    private fun getNoticeCommentWrite() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoticeCommentWrite(
            mRequestQueue,
            object : NagajaManager.RequestListener<CommentData> {
                override fun onSuccess(resultData: CommentData) {

                    mCommentListData.add(resultData)
                    mNewsCommentAdapter.setData(mCommentListData)

                    mCommentEditText.setText("")
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
            getItemUid()
        )
    }

    /**
     * API. Get Notice Comment Delete
     */
    private fun getNoticeCommentDelete(commentUid: Int, position: Int) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoticeCommentDelete(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mItemTotalCount--
                    mCommentListData.removeAt(position)
                    mNewsCommentAdapter.deleteItem(position)

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