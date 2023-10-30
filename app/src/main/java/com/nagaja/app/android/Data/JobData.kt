package com.nagaja.app.android.Data

import java.io.Serializable

class JobData : Serializable {
    private var mType: Int = 0     // 0:구인 1:구직
    private var mTitle: String = ""
    private var mContent: String = ""
    private var mDate: String = ""
    private var mViewCount: Int = 0
    private var mDownloadUrl: String = ""
    private var mDownloadFileName: String = ""
    private var mExtension: String = ""
    private var mImageList = ArrayList<String>()

    fun setTitle(title: String) {
        mTitle = title
    }

    fun getTitle(): String {
        return mTitle
    }

    fun setType(type: Int) {
        mType = type
    }

    fun getType(): Int {
        return mType
    }

    fun setContent(content: String) {
        mContent = content
    }

    fun getContent(): String {
        return mContent
    }

    fun setDate(date: String) {
        mDate = date
    }

    fun getDate(): String {
        return mDate
    }

    fun setViewCount(viewCount: Int) {
        mViewCount = viewCount
    }

    fun getViewCount(): Int {
        return mViewCount
    }

    fun setDownloadUrl(downloadUrl: String) {
        mDownloadUrl = downloadUrl
    }

    fun getDownloadUrl(): String {
        return mDownloadUrl
    }

    fun setDownloadFileName(downloadFileName: String) {
        mDownloadFileName = downloadFileName
    }

    fun getDownloadFileName(): String {
        return mDownloadFileName
    }

    fun setExtension(extension: String) {
        mExtension = extension
    }

    fun getExtension(): String {
        return mExtension
    }

    fun setImageList(imageList: ArrayList<String>) {
        mImageList = imageList
    }

    fun getImageList(): ArrayList<String> {
        return mImageList
    }
}