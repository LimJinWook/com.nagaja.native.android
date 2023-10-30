package com.nagaja.app.android.Dialog

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.custom_dialog_comment_delete.view.*

class CustomDialogCommentDelete(context: Context, isComment: Boolean) : BottomSheetDialogFragment() {

    private lateinit var mContainer: View

    private lateinit var mActivity: Activity
    private lateinit var mContext: Context

    private lateinit var mMoreViewTextView: TextView
    private lateinit var mDeleteTextView: TextView
    private lateinit var mCancelTextView: TextView

    private var mIsComment = false

    private var mListener: OnCommentDeleteDialogListener? = null

    fun setOnCommentDeleteDialogListener(listener: OnCommentDeleteDialogListener?) {
        mListener = listener
    }

    init {
        this.mContext = context
        this.mIsComment = isComment
    }

    interface OnCommentDeleteDialogListener {
        fun onMoreView()
        fun onDelete()
        fun onCancel()
    }

    fun newInstance(context: Context, isComment: Boolean): CustomDialogCommentDelete {
        val fragment = CustomDialogCommentDelete(context, isComment)
        val args = Bundle()
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainer = inflater.inflate(R.layout.custom_dialog_comment_delete, container, false)
        init()
        return mContainer
    }

    private fun init() {

        // More View Text View
        mMoreViewTextView = mContainer.custom_dialog_comment_delete_more_comment_text_view
        if (!mIsComment) {
            mMoreViewTextView.text = mContext.resources.getString(R.string.text_delete_board)
        }
        mMoreViewTextView.setOnSingleClickListener {
            mListener!!.onMoreView()
        }

        // Delete Text View
        mDeleteTextView = mContainer.custom_dialog_comment_delete_text_view
        mDeleteTextView.setOnSingleClickListener {
            mListener!!.onDelete()
        }

        // Cancel Text VIew
        mCancelTextView = mContainer.custom_dialog_comment_delete_cancel_text_view
        mCancelTextView.setOnSingleClickListener {
            mListener!!.onCancel()
        }
    }
}