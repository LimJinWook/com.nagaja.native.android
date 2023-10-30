package com.nagaja.app.android.BoardDetail

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialogCommentDelete
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.NagajaManager
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_board.view.*
import kotlinx.android.synthetic.main.fragment_board_detail.view.*
import kotlinx.android.synthetic.main.layout_title.view.*


class NativeBoardDetailFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mWriteCommentEditText: EditText

    private lateinit var mDeleteTextView: TextView
    private lateinit var mModifyTextView: TextView
    private lateinit var mListTextView: TextView
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

    private lateinit var mImageRecyclerView: RecyclerView
    private lateinit var mCommentRecyclerView: RecyclerView

    private lateinit var mImageAdapter: UploadImageAdapter
    private lateinit var mNativeCommentAdapter: NativeCommentAdapter

    private lateinit var mListener: OnBoardFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    interface OnBoardFragmentListener {
        fun onFinish()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
        fun onDelete(selectType: Int, position: Int)
        fun onWriteBoardScreen(boardData: BoardData)
    }

    companion object {
        const val ARGS_BOARD_UID                  = "args_board_uid"

        fun newInstance(boardUid: Int): NativeBoardDetailFragment {
            val args = Bundle()
            val fragment = NativeBoardDetailFragment()
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

        mImageAdapter = UploadImageAdapter(mActivity)
        mNativeCommentAdapter = NativeCommentAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_board_detail, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.VISIBLE
        backImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_board)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

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
        mImageRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
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

        // Comment List Count Text View
        mCommentListCountTextView = mContainer.fragment_board_detail_comment_list_count_text_view

        // Comment Recycler View
        mCommentRecyclerView = mContainer.fragment_board_detail_comment_recycler_view
        mCommentRecyclerView.setHasFixedSize(false)
        mCommentRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        mNativeCommentAdapter.setOnNewsItemClickListener(object : NativeCommentAdapter.OnNewsItemClickListener {
            override fun onClick(data: NativeCommentData, isDelete: Boolean, position: Int) {
                if (isDelete) {
                    showDeleteDialog(true, position)
                }
            }
        })
        mCommentRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mCommentRecyclerView.adapter = mNativeCommentAdapter

        // Write Comment Edit Text
        mWriteCommentEditText = mContainer.fragment_board_detail_write_comment_edit_text
        mWriteCommentEditText.filters = arrayOf(InputFilter.LengthFilter(200))
        mWriteCommentEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        // Comment Send Image View
        val commentSendImageView = mContainer.fragment_board_detail_write_comment_send_image_view
        commentSendImageView.setOnSingleClickListener {
            if (TextUtils.isEmpty(mWriteCommentEditText.text.toString())) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_input_comment))
                return@setOnSingleClickListener
            }

//            val commentListData = getBoardData().getBoardCommentListData()
//
//            val commentData = CommentData()
//            commentData.setNickName("NaGaJa")
//            commentData.setComment(mWriteCommentEditText.text.toString())
//            commentData.setDate(System.currentTimeMillis().toString())
//            commentData.setIsMyComment(true)
//
//            commentListData.add(commentData)
//
//            mCommentAdapter.setData(commentListData)
//            mWriteCommentEditText.setText("")

            hideKeyboard(mActivity)
        }


        // Delete Text View
        mDeleteTextView = mContainer.fragment_board_detail_delete_text_view
        mDeleteTextView.setOnSingleClickListener {
            showDeleteDialog(false, 0)
        }

        // Modify Text View
        mModifyTextView = mContainer.fragment_board_detail_modify_text_view
        mModifyTextView.setOnSingleClickListener {
//            mListener.onWriteBoardScreen(getBoardData())
        }

        // List Text View
//        mListTextView = mContainer.fragment_board_detail_list_text_view
//        mListTextView.setOnSingleClickListener {
//            mListener.onFinish()
//        }


//        mCommentAdapter.setData(getBoardData().getBoardCommentListData())

//        if (getBoardData().getIsMyBoard()) {
//            mDeleteTextView.visibility = View.VISIBLE
//            mModifyTextView.visibility = View.VISIBLE
//        } else {
//            mDeleteTextView.visibility = View.GONE
//            mModifyTextView.visibility = View.GONE
//        }

        getBoardData()
    }

    private fun showDeleteDialog(isComment: Boolean, position: Int) {
        val customDialogCommentDelete = CustomDialogCommentDelete(mActivity, isComment)
        customDialogCommentDelete.setOnCommentDeleteDialogListener(object : CustomDialogCommentDelete.OnCommentDeleteDialogListener {
            override fun onMoreView() {
                customDialogCommentDelete.dismiss()
            }

            override fun onDelete() {
//                if (isComment) {
//                    getBoardData().getBoardCommentListData().removeAt(position)
//                    mCommentAdapter.deleteItem(position)
//                } else {
//                    mListener.onDelete(getSelectType(), getBoardDataPosition())
//                }

                customDialogCommentDelete.dismiss()
            }

            override fun onCancel() {
                customDialogCommentDelete.dismiss()
            }

        })
        customDialogCommentDelete.show(requireActivity().supportFragmentManager, "Custom Dialog")
    }

    private fun getImageList(imageList: ArrayList<NativeBoardImageData>): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0 until imageList.size) {
            val url = "https://the330-nagaja.s3.ap-northeast-2.amazonaws.com"
            list.add(url + imageList[i].getBoardImageName())
        }

        return list
    }

    private fun showNativeBoardDetail(data: NativeBoardData) {

        if (!TextUtils.isEmpty(data.getCategoryName())) {
            mCategoryTextView.text = data.getCategoryName()
        }

        if (!TextUtils.isEmpty(data.getBoardSubject())) {
            mBoardTitleTextView.text = data.getBoardSubject()
        }

        if (!TextUtils.isEmpty(data.getMemberName())) {
            mNickNameTextView.text = data.getMemberName()
        }

        if (!TextUtils.isEmpty(data.getCreateDate())) {
            mDateTextView.text = data.getCreateDate()
        }

        if (data.getViewCount() > 0) {
            mViewCountTextView.text = String.format(mActivity.resources.getString(R.string.text_board_view_count), data.getViewCount().toString())
        } else {
            mViewCountTextView.text = String.format(mActivity.resources.getString(R.string.text_board_view_count), "0")
        }

        mImageAdapter.setData(getImageList(data.getBoardImageList()))
        mImageAdapter.setOnImageClickListener(object : UploadImageAdapter.OnImageClickListener {
            override fun onClick(position: Int) {
                mListener.onFullScreenImage(getImageList(data.getBoardImageList()), position)
            }
        })

        if (!TextUtils.isEmpty(data.getBoardContent())) {
            mContentTextView.text = data.getBoardContent()
        }

        if (data.getRecommendCount() > 0) {
            mGoodCountTextView.text = String.format(mActivity.resources.getString(R.string.text_good_count), data.getRecommendCount().toString())
        } else {
            mGoodCountTextView.text = String.format(mActivity.resources.getString(R.string.text_good_count), "0")
        }

        if (data.getCommentCount() > 0) {
            mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), data.getCommentCount().toString())
        } else {
            mCommentCountTextView.text = String.format(mActivity.resources.getString(R.string.text_comment_count), "0")
        }

        if (data.getBookMarkCount() > 0) {
            mBookmarkCountTextView.text = String.format(mActivity.resources.getString(R.string.text_bookmark_count), data.getBookMarkCount().toString())
        } else {
            mBookmarkCountTextView.text = String.format(mActivity.resources.getString(R.string.text_bookmark_count), "0")
        }

        if (data.getCommentCount() > 0) {
            mCommentListCountTextView.text = data.getCommentCount().toString()
        } else {
            mCommentListCountTextView.text = "0"
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

    // ======================================================================================
    // API
    // ======================================================================================

    private fun getBoardData() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardData(
            mRequestQueue,
            object : NagajaManager.RequestListener<NativeBoardData> {
                override fun onSuccess(resultData: NativeBoardData) {
                    if (null == resultData) {
                        return
                    }

                    showNativeBoardDetail(resultData)

                    getCommentData()
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
            getBoardUid()
        )
    }

    private fun getCommentData() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardCommentData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<NativeCommentData>> {
                override fun onSuccess(resultData: ArrayList<NativeCommentData>) {
                    if (null == resultData) {
                        return
                    }

                    mNativeCommentAdapter.setData(resultData)
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
            getBoardUid()
        )
    }
}