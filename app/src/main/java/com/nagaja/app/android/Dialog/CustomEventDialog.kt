package com.nagaja.app.android.Dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Utils.SharedPreferencesUtil
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.custom_event_dialog.*
import java.text.SimpleDateFormat
import java.util.*

class CustomEventDialog(context: Context, imageUrl: String, eventLink:String) : Dialog(context) {

    private var mContext: Context = context

    private lateinit var mEventImageView: SimpleDraweeView
    private lateinit var mCloseImageView: ImageView
    private lateinit var mCheckImageView: ImageView

    private var mImageUrl: String
    private var mEventLink: String

    private var mIsCheck = false

    private lateinit var mListener: OnCustomEventDialogListener

    fun setOnCustomEventDialogListener(listener: OnCustomEventDialogListener) {
        mListener = listener
    }

    interface OnCustomEventDialogListener {
        fun onCancel(isTodayClose: Boolean)
        fun onConfirm(eventLink: String)
    }

    init {
        this.mImageUrl = imageUrl
        this.mEventLink = eventLink
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

        setContentView(R.layout.custom_event_dialog)

        init()
    }

    @SuppressLint("SimpleDateFormat")
    private fun init() {

        // Close Image View
        mCloseImageView = custom_event_dialog_close_image_view
        mCloseImageView.setOnClickListener {
            mListener.onCancel(mIsCheck)
        }

        // Event Image View
        mEventImageView = custom_event_dialog_image_view
        if (!TextUtils.isEmpty(mImageUrl)) {
            mEventImageView.setImageURI(Uri.parse(mImageUrl))
            mEventImageView.setOnClickListener {
                mListener.onConfirm(mEventLink)
            }
        }

        // Check Image View
        mCheckImageView = custom_event_dialog_check_image_view
        mCheckImageView.setOnClickListener {
            mIsCheck = !mIsCheck

            mCheckImageView.setImageResource(if (mIsCheck) R.drawable.ic_baseline_check_box_24_white else R.drawable.ic_baseline_check_box_outline_blank_24_white)
        }

        // Confirm Text View
        val confirmTextView = custom_event_dialog_confirm_text_view
        confirmTextView.setOnClickListener {
            if (mIsCheck) {

                val currentTime = System.currentTimeMillis()
                val todayDate = Date(currentTime)
                val date = SimpleDateFormat("dd")

                SharedPreferencesUtil().setDoNotWatchToday(mContext, date.format(todayDate))
            }

            dismiss()
        }
    }

}