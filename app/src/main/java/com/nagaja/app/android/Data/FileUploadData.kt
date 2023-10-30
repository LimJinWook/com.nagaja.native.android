package com.nagaja.app.android.Data

import android.net.Uri
import java.io.Serializable

class FileUploadData : Serializable {
    private var mFileName: String = ""
    private lateinit var mFileUri: Uri
    private var mFileUid = 0
    private var mIsUploadImage = true

    fun setFileName(fileName: String) {
        mFileName = fileName
    }

    fun getFileName(): String {
        return mFileName
    }

    fun setFileUri(fileUri: Uri) {
        mFileUri = fileUri
    }

    fun getFileUri(): Uri {
        return mFileUri
    }

    fun setFileUid(fileUid: Int) {
        mFileUid = fileUid
    }

    fun getFileUid(): Int {
        return mFileUid
    }

    fun setIsUploadImage(isUploadImage: Boolean) {
        mIsUploadImage = isUploadImage
    }

    fun getIsUploadImage(): Boolean {
        return mIsUploadImage
    }

}