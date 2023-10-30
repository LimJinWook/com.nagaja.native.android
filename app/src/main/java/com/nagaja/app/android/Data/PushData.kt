package com.nagaja.app.android.Data

import java.io.Serializable

class PushData: Serializable {
    private var mType: String? = ""
    private var mTypeIndex: Int = 0
    private var mReservateType: Int = -1
    private var mReservateCompanyUid: Int = 0
    private var mImageUrl: String? = ""
    private var mWebLinkUrl: String? = ""
    private var mTopicCategoryUid: Int = 0
    private var mTopicBoardUid: Int = 0
    private var mCompanyUid: Int = 0

    fun setType(type: String) {
        mType = type
    }

    fun getType(): String? {
        return mType
    }

    fun setTypeIndex(typeIndex: Int) {
        mTypeIndex = typeIndex
    }

    fun getTypeIndex(): Int {
        return mTypeIndex
    }

    fun setReservateType(reservateType: Int) {
        mReservateType = reservateType
    }

    fun getReservateType(): Int {
        return mReservateType
    }

    fun setReservateCompanyUid(reservateCompanyUid: Int) {
        mReservateCompanyUid = reservateCompanyUid
    }

    fun getReservateCompanyUid(): Int {
        return mReservateCompanyUid
    }

    fun setImageUrl(imageUrl: String) {
        mImageUrl = imageUrl
    }

    fun getImageUrl(): String? {
        return mImageUrl
    }

    fun setWebLinkUrl(webLinkUrl: String) {
        mWebLinkUrl = webLinkUrl
    }

    fun getWebLinkUrl(): String? {
        return mWebLinkUrl
    }

    fun setTopicCategoryUid(topicCategoryUid: Int) {
        mTopicCategoryUid = topicCategoryUid
    }

    fun getTopicCategoryUid(): Int {
        return mTopicCategoryUid
    }

    fun setTopicBoardUid(topicBoardUid: Int) {
        mTopicBoardUid = topicBoardUid
    }

    fun getTopicBoardUid(): Int {
        return mTopicBoardUid
    }

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

}