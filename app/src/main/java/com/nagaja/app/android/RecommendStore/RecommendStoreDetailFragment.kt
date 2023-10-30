package com.nagaja.app.android.RecommendStore

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
import com.nagaja.app.android.Utils.NetworkProvider
import com.nagaja.app.android.Utils.NetworkProvider.Companion.IMAGE_VIEW
import com.nagaja.app.android.Adapter.CommentAdapter
import com.nagaja.app.android.Adapter.StoreMainImageAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.BoardData
import com.nagaja.app.android.Data.CommentData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogCommentDelete
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_faq.view.*
import kotlinx.android.synthetic.main.fragment_news_detail.view.*
import kotlinx.android.synthetic.main.fragment_recommend_store.view.*
import kotlinx.android.synthetic.main.fragment_recommend_store_detail.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class RecommendStoreDetailFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View
    private lateinit var mReportView: View

    private lateinit var mCommentEditText: EditText

    private lateinit var mCategoryTextView: TextView
    private lateinit var mTitleTextView: TextView
    private lateinit var mNameTextView: TextView
    private lateinit var mDateTextView: TextView
    private lateinit var mViewCountTextView: TextView
    private lateinit var mContentTextView: TextView
    private lateinit var mRecommendCountTextView: TextView
    private lateinit var mLikeCountTextView: TextView
    private lateinit var mCommentCountTextView: TextView
    private lateinit var mDeleteTextView: TextView
    private lateinit var mModifyTextView: TextView
    private lateinit var mCommentCount2TextView: TextView
    private lateinit var mCommentWordCountTextView: TextView

    private lateinit var mRecommendImageView: ImageView
    private lateinit var mBookmarkImageView: ImageView
    private lateinit var mReportImageView: ImageView

    private lateinit var mImageRecyclerView: RecyclerView
    private lateinit var mCommentRecyclerView: RecyclerView

    private lateinit var mStoreMainImageAdapter: StoreMainImageAdapter
    private lateinit var mCommentAdapter: CommentAdapter

    private lateinit var mListener: OnRecommendStoreFragmentListener

    private lateinit var mBoardData: BoardData

    private val mMainImageList = ArrayList<String>()
    private val mOriginImageList = ArrayList<String>()
    private var mCommentListData = ArrayList<CommentData>()

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mRequestQueue: RequestQueue

    private var mPageCount = 1
    private var mItemTotalCount = 0

    private var mIsFirst = true
    private var mIsEndOfScroll = false

    interface OnRecommendStoreFragmentListener {
        fun onBack()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
        fun onReportScreen(reportType: Int, reviewUid: Int, storeName: String)
        fun onRecommendStoreWriteScreen(categoryUid: Int, boardUid: Int, isModify: Boolean)
        fun onSuccess(isDetailScreen: Boolean)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_CATEGORY_UID_DATA                = "args_category_uid_data"
        const val ARGS_BOARD_UID_DATA                   = "args_board_uid_data"

        fun newInstance(categoryUid: Int, boardUid: Int): RecommendStoreDetailFragment {
            val args = Bundle()
            val fragment = RecommendStoreDetailFragment()
            args.putInt(ARGS_CATEGORY_UID_DATA, categoryUid)
            args.putInt(ARGS_BOARD_UID_DATA, boardUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getCategoryUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_CATEGORY_UID_DATA) as Int
    }

    private fun getBoardUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_BOARD_UID_DATA) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mStoreMainImageAdapter = StoreMainImageAdapter(mActivity)
        mCommentAdapter = CommentAdapter(mActivity, true)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_recommend_store_detail, container, false)

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
            setShareUrl(mActivity, getCategoryUid(), getBoardUid())
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
        titleTextView.text = mActivity.resources.getString(R.string.text_recommend_store)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onBack()
        }

        mScrollView = mContainer.fragment_recommend_store_detail_scroll_view
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
        mCategoryTextView = mContainer.fragment_recommend_store_detail_category_text_view

        // Title Text View
        mTitleTextView = mContainer.fragment_recommend_store_detail_title_text_view

        // Name Text View
        mNameTextView = mContainer.fragment_recommend_store_detail_name_text_view

        // Date Text View
        mDateTextView = mContainer.fragment_recommend_store_detail_date_text_view

        // View Count Text View
        mViewCountTextView = mContainer.fragment_recommend_store_detail_view_count_text_view

        // Image Recycler View
        mImageRecyclerView = mContainer.fragment_recommend_store_detail_image_recycler_view
        mImageRecyclerView.setHasFixedSize(true)
        mImageRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        mStoreMainImageAdapter.setOnImageClickListener(object : StoreMainImageAdapter.OnImageClickListener {
            override fun onClick(position: Int) {
                mListener.onFullScreenImage(mOriginImageList, position)
            }
        })
        mImageRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mImageRecyclerView.adapter = mStoreMainImageAdapter

        // Content Text View
        mContentTextView = mContainer.fragment_recommend_store_detail_content_text_view

        // Recommend Count Text View
        mRecommendCountTextView = mContainer.fragment_recommend_store_detail_recommend_count_text_view

        // Like Count Text View
        mLikeCountTextView = mContainer.fragment_recommend_store_detail_like_count_text_view

        // Comment Count Text View
        mCommentCountTextView = mContainer.fragment_recommend_store_detail_comment_count_text_view

        // Recommend View
        val recommendView = mContainer.fragment_recommend_store_detail_recommend_view
        recommendView.setOnClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                getRecommendSave()
            }
        }

        // Recommend Image View
        mRecommendImageView = mContainer.fragment_recommend_store_detail_recommend_image_view

        // Bookmark View
        val bookmarkView = mContainer.fragment_recommend_store_detail_bookmark_view
        bookmarkView.setOnClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                getBookmarkSave()
            }
        }

        // Bookmark Image View
        mBookmarkImageView = mContainer.fragment_recommend_store_detail_bookmark_image_view

        // Report View
        mReportView = mContainer.fragment_recommend_store_detail_report_view
        mReportView.setOnClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                mListener.onReportScreen(REPORT_TYPE_STORE_RECOMMEND, getBoardUid(), "")
            }
        }

        // Report Image View
        mReportImageView = mContainer.fragment_recommend_store_detail_report_image_view

        // Comment Count 2 Text View
        mCommentCount2TextView = mContainer.fragment_recommend_store_detail_comment_count_2_text_view

        // Recycler View
        mCommentRecyclerView = mContainer.fragment_recommend_store_detail_comment_recycler_view
        mCommentRecyclerView.setHasFixedSize(false)
        mCommentRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        mCommentAdapter.setOnNewsItemClickListener(object : CommentAdapter.OnNewsItemClickListener {

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
        mCommentRecyclerView.adapter = mCommentAdapter

        // Comment Edit Text
        mCommentEditText = mContainer.fragment_recommend_store_detail_comment_edit_text
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
        val commentSendImageView = mContainer.fragment_recommend_store_detail_comment_send_image_view
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
        mCommentWordCountTextView = mContainer.fragment_recommend_store_detail_comment_word_count_text_view

        // Delete Text View
        mDeleteTextView = mContainer.fragment_recommend_store_detail_delete_text_view
        mDeleteTextView.setOnClickListener {
            showBoardDeletePopup()
        }

        // Modify Text View
        mModifyTextView = mContainer.fragment_recommend_store_detail_modify_text_view
        mModifyTextView.setOnClickListener {
            mListener.onRecommendStoreWriteScreen(getCategoryUid(), getBoardUid(), true)
        }

        // List Text View
        val listTextView = mContainer.fragment_recommend_store_detail_write_comment_text_view
        listTextView.setOnClickListener {
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


        // Loading View
        mLoadingView = mContainer.fragment_recommend_store_detail_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_recommend_store_detail_loading_lottie_view


        getBoardDetail()
    }

    private fun displayBoardData(data: BoardData) {

        // Category Text View
        if (!TextUtils.isEmpty(data.getCategoryName())) {
            mCategoryTextView.text = data.getCategoryName()
        }

        // Title Text View
        if (!TextUtils.isEmpty(data.getBoardSubject())) {
            mTitleTextView.text = data.getBoardSubject()
        }

        // Name Text View
        if (!TextUtils.isEmpty(data.getMemberName())) {
            mNameTextView.text = data.getMemberName()
        }

        // Date Text View
        if (!TextUtils.isEmpty(data.getCreateDate())) {
            var time = data.getCreateDate()
            val index: Int = time.indexOf("+")
            time = time.substring(0, index)

            val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
            val zone = ZoneId.of(Util().getTimeZoneID(mActivity))
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            mDateTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
        }

        // View Count Text View
        mViewCountTextView.text = data.getViewCount().toString()

        // Main Image List
        mMainImageList.clear()
        mOriginImageList.clear()
        for (i in data.getBoardImageListData().indices) {
            mMainImageList.add(NetworkProvider.DEFAULT_IMAGE_DOMAIN + IMAGE_VIEW + data.getBoardImageListData()[i].getItemImageName())
            mOriginImageList.add(NetworkProvider.DEFAULT_IMAGE_DOMAIN + data.getBoardImageListData()[i].getItemImageName())
        }
        mStoreMainImageAdapter.setData(mMainImageList)

        // Content Text View
        if (!TextUtils.isEmpty(data.getBoardContent())) {
            mContentTextView.text = data.getBoardContent()
        }

        // Recommend Count Text View
        if (data.getViewCount() > 0) {
            mRecommendCountTextView.text = String.format(mActivity.resources.getString(R.string.text_recommendation_2), data.getViewCount().toString())
        } else {
            mRecommendCountTextView.text = String.format(mActivity.resources.getString(R.string.text_recommendation_2), "0")
        }

        // Like Count Text View
        if (data.getBookMarkCount() > 0) {
            mLikeCountTextView.text = String.format(mActivity.resources.getString(R.string.text_good_count_2), data.getBookMarkCount().toString())
        } else {
            mLikeCountTextView.text = String.format(mActivity.resources.getString(R.string.text_good_count_2), "0")
        }

        // Comment Count Text View
        if (data.getCommentCount() > 0) {
            mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), data.getCommentCount().toString())
            mCommentCount2TextView.text = data.getCommentCount().toString()
        } else {
            mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), "0")
            mCommentCount2TextView.text = "0"
        }

        // Recommend Image View
        mRecommendImageView.setImageResource( if (data.getIsRecommend()) R.drawable.icon_recommend_enable else R.drawable.icon_recommend_2 )

        // Bookmark Image View
        mBookmarkImageView.setImageResource( if (data.getIsBookMark()) R.drawable.icon_bookmark_star_disable else R.drawable.icon_bookmark_star )

        // Report View
        mReportView.visibility = if (MAPP.USER_DATA.getMemberUid() == data.getMemberUid()) View.GONE else View.VISIBLE

        // Report Image View
        mReportImageView.setImageResource( if (data.getIsReport()) R.drawable.icon_report_enable else R.drawable.icon_note_report )

        // Delete, Modify Text View
        if (MAPP.USER_DATA.getMemberUid() == data.getMemberUid()) {
            mDeleteTextView.visibility = View.VISIBLE
            mModifyTextView.visibility = View.VISIBLE
        } else {
            mDeleteTextView.visibility = View.GONE
            mModifyTextView.visibility = View.GONE
        }



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

    private fun showBoardDeletePopup() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_recommend_store) + " " + mActivity.resources.getString(R.string.text_delete),
            mActivity.resources.getString(R.string.text_message_delete_board_recommend),
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
        if (context is OnRecommendStoreFragmentListener) {
            mActivity = context as Activity

            if (context is OnRecommendStoreFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnRecommendStoreFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnRecommendStoreFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnRecommendStoreFragmentListener"
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
            20,
            false
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
                        mCommentCount2TextView.text = "0"
                    } else {
                        mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), mItemTotalCount.toString())
                        mCommentCount2TextView.text = mItemTotalCount.toString()
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
     * API. Get Board Comment Write
     */
    private fun getBoardCommentWrite() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardCommentWrite(
            mRequestQueue,
            object : NagajaManager.RequestListener<CommentData> {
                override fun onSuccess(resultData: CommentData) {

                    mCommentListData.add(resultData)
                    mCommentAdapter.setData(mCommentListData)

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
            getBoardUid()
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
                    mRecommendImageView.setImageResource( if (mBoardData.getIsRecommend()) R.drawable.icon_recommend_enable else R.drawable.icon_recommend_2 )
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
                    mListener.onBack()
                    mListener.onSuccess(false)
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