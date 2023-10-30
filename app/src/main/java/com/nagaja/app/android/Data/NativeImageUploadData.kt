package com.nagaja.app.android.Data

import android.net.Uri
import java.io.Serializable

class NativeImageUploadData : Serializable {
    private var mViewType: Int = -1
    private lateinit var mImageUri: Uri
    private var mIsDeviceImage: Boolean = false
    private var mImageUrl: String = ""
    private var mImageUid: Int = 0

    fun setViewType(viewType: Int) {
        mViewType = viewType
    }

    fun getViewType(): Int {
        return mViewType
    }

    fun setImageUri(imageUri: Uri) {
        mImageUri = imageUri
    }

    fun getImageUri(): Uri {
        return mImageUri
    }

    fun setIsDeviceImage(isDeviceImage: Boolean) {
        mIsDeviceImage = isDeviceImage
    }

    fun getIsDeviceImage(): Boolean {
        return mIsDeviceImage
    }

    fun setImageUrl(imageUrl: String) {
        mImageUrl = imageUrl
    }

    fun getImageUrl(): String {
        return mImageUrl
    }

    fun setImageUid(imageUid: Int) {
        mImageUid = imageUid
    }

    fun getImageUid(): Int {
        return mImageUid
    }
}