package com.nagaja.app.android.Dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.nagaja.app.android.Base.NagajaFragment.Companion.DIALOG_TYPE_NORMAL
import com.nagaja.app.android.Base.NagajaFragment.Companion.DIALOG_TYPE_NO_CANCEL
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.custom_dialog.*

class CustomDialog(context: Context, dialogType: Int, title: String, message: String, cancelType: String, confirmType: String) : Dialog(context) {

    private var mContext: Context = context

    private lateinit var mImageView: View
    private lateinit var mCancelView: View

    private lateinit var mTitleTextView: TextView
    private lateinit var mMessageTextView: TextView
    private lateinit var mCancelTextView: TextView
    private lateinit var mConfirmTextView: TextView

    private var mDialogType = 0x01

    private var mTitle: String
    private var mMessage: String
    private var mCancelType: String
    private var mConfirmType: String

    private lateinit var mListener: OnCustomDialogListener

    fun setOnCustomDialogListener(listener: OnCustomDialogListener) {
        mListener = listener
    }

    interface OnCustomDialogListener {
        fun onCancel()
        fun onConfirm()
    }

    init {
        this.mDialogType = dialogType
        this.mTitle = title
        this.mMessage = message
        this.mCancelType = cancelType
        this.mConfirmType = confirmType
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = window!!.attributes
        val wm = mContext.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        lp.width = (wm.defaultDisplay.width * 0.9).toInt()
        window!!.attributes = lp
        window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        setContentView(R.layout.custom_dialog)

        init()
    }

    private fun init() {
        // Title Text View
        mTitleTextView = custom_dialog_title_text_view

        // Message Text View
        mMessageTextView = custom_dialog_message_text_view

        // Cancel View
        mCancelView = custom_dialog_cancel_view

        // Cancel Text View
        mCancelTextView = custom_dialog_cancel_text_view
        mCancelTextView.setOnClickListener {
            mListener.onCancel()
        }

        // Confirm Text View
        mConfirmTextView = custom_dialog_confirm_text_view
        mConfirmTextView.setOnClickListener {
            mListener.onConfirm()
        }

        setDialogType()
    }

    private fun setDialogType() {
        mTitleTextView.text = mTitle
        mMessageTextView.text = mMessage
        mCancelTextView.text = mCancelType
        mConfirmTextView.text = mConfirmType

        when (mDialogType) {
            DIALOG_TYPE_NORMAL -> {

            }

            DIALOG_TYPE_NO_CANCEL -> {
                mCancelView.visibility = View.GONE
            }
        }
    }
}