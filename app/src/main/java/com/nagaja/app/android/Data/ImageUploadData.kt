package com.nagaja.app.android.Data

import android.graphics.Bitmap
import java.io.Serializable

class ImageUploadData: Serializable {
    private var mFileName: String? = ""
    private lateinit var mImageBitmap: Bitmap
    private var mFilePath: String? = ""

    fun setFileName(fileName: String) {
        mFileName = fileName
    }

    fun getFileName(): String? {
        return mFileName
    }

    fun setImageBitmap(imageBitmap: Bitmap) {
        mImageBitmap = imageBitmap
    }

    fun getImageBitmap(): Bitmap {
        return mImageBitmap
    }

    fun setFilePath(filePath: String) {
        mFilePath = filePath
    }

    fun getFilePath(): String? {
        return mFilePath
    }
}