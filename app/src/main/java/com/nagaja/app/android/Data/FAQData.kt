package com.nagaja.app.android.Data

import java.io.Serializable

class FAQData : Serializable {
    private var mCategory: String = ""
    private var mTypeValue: Int = -1
    private var mTitle: String = ""
    private var mDate: String = ""
    private var mViewCount: Int = 0
    private var mDownloadUrl: String = ""
    private var mDownloadFileName: String = ""
    private var mExtension: String = ""
    private var mContent: String = ""

    fun setCategory(category: String) {
        mCategory = category
    }

    fun getCategory(): String {
        return mCategory
    }

    fun setTypeValue(typeValue: Int) {
        mTypeValue = typeValue
    }

    fun getTypeValue(): Int {
        return mTypeValue
    }

    fun setTitle(title: String) {
        mTitle = title
    }

    fun getTitle(): String {
        return mTitle
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

    fun setContent(content: String) {
        mContent = content
    }

    fun getContent(): String {
        return mContent
    }

}