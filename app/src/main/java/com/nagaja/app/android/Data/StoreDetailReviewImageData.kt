package com.nagaja.app.android.Data

import java.io.Serializable

class StoreDetailReviewImageData : Serializable {
    private var mReviewImageUid: Int = 0
    private var mCompanyReviewUid: Int = 0
    private var mReviewSort: Int = 0
    private var mReviewImageOrigin: String = ""
    private var mReviewImageName: String = ""
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""


    fun setReviewImageUid(reviewImageUid: Int) {
        mReviewImageUid = reviewImageUid
    }

    fun getReviewImageUid(): Int {
        return mReviewImageUid
    }

    fun setCompanyReviewUid(companyReviewUid: Int) {
        mCompanyReviewUid = companyReviewUid
    }

    fun getCompanyReviewUid(): Int {
        return mCompanyReviewUid
    }

    fun setReviewSort(reviewSort: Int) {
        mReviewSort = reviewSort
    }

    fun getReviewSort(): Int {
        return mReviewSort
    }

    fun setReviewImageOrigin(reviewImageOrigin: String) {
        mReviewImageOrigin = reviewImageOrigin
    }

    fun getReviewImageOrigin(): String {
        return mReviewImageOrigin
    }

    fun setReviewImageName(reviewImageName: String) {
        mReviewImageName = reviewImageName
    }

    fun getReviewImageName(): String {
        return mReviewImageName
    }

    fun setIsUseYn(isUseYn: Boolean) {
        mIsUseYn = isUseYn
    }

    fun getIsUseYn(): Boolean {
        return mIsUseYn
    }

    fun setCreateDate(createDate: String) {
        mCreateDate = createDate
    }

    fun getCreateDate(): String {
        return mCreateDate
    }

    fun setUpdateDate(updateDate: String) {
        mUpdateDate = updateDate
    }

    fun getUpdateDate(): String {
        return mUpdateDate
    }

}