package com.nagaja.app.android.Dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.custom_dialog_bottom.view.*

class CustomDialogBottom(context: Context, isImage: Boolean, isProfileModify: Boolean) : BottomSheetDialogFragment() {

    private lateinit var mContainer: View

    private var mContext: Context

    private var mIsImage = false
    private var mIsProfileModify = false

    private var mListener: OnCustomDialogBottomListener? = null

    fun setOnCustomDialogBottomListener(listener: OnCustomDialogBottomListener?) {
        mListener = listener
    }

    init {
        this.mContext = context
        this.mIsImage = isImage
        this.mIsProfileModify = isProfileModify
    }

    interface OnCustomDialogBottomListener {
        fun onCamera()
        fun onGallery()
        fun onSelectFile()
        fun onDeleteProfileImage()
        fun onCancel()
    }

    fun newInstance(context: Context, isImage: Boolean, isProfileModify: Boolean): CustomDialogBottom {
        val fragment = CustomDialogBottom(context, isImage, isProfileModify)
        val args = Bundle()
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainer = inflater.inflate(R.layout.custom_dialog_bottom, container, false)
        init()
        return mContainer
    }

    private fun init() {

        // Title Text View
        val titleTextView = mContainer.custom_dialog_bottom_title_text_view
        titleTextView.text = if (mIsImage) mContext.resources.getString(R.string.text_select_image) else mContext.resources.getString(R.string.text_file_upload)

        // Camera Text View
        val cameraTextView = mContainer.custom_dialog_bottom_camera_text_view
        cameraTextView.setOnClickListener {
            mListener!!.onCamera()
        }

        // Gallery Text View
        val galleryTextView = mContainer.custom_dialog_bottom_gallery_text_view
//        galleryTextView.text = if (mIsImage) mContext.resources.getString(R.string.text_gallery) else mContext.resources.getString(R.string.text_select_file)
        galleryTextView.text = mContext.resources.getString(R.string.text_gallery)
        galleryTextView.setOnClickListener {
            mListener!!.onGallery()
        }

        val selectFileView = mContainer.custom_dialog_bottom_select_file_view
        selectFileView.visibility = if (mIsImage) View.GONE else View.VISIBLE
        selectFileView.setOnClickListener {
            mListener!!.onSelectFile()
        }

        // Cancel Text View
        val cancelTextView = mContainer.custom_dialog_bottom_cancel_text_view
        cancelTextView.setOnClickListener {
            mListener!!.onCancel()
        }

        // Delete Profile Image View
        val deleteProfileImageView = mContainer.custom_dialog_bottom_delete_profile_view
        deleteProfileImageView.visibility = if (mIsProfileModify) View.VISIBLE else View.GONE
        deleteProfileImageView.setOnClickListener {
            mListener!!.onDeleteProfileImage()
        }
    }
}