package com.nagaja.app.android.Data

import java.io.Serializable

class WriteReviewData : Serializable {
    private var mCompanyUid: Int = 0
    private var mReviewPoint: Int = 0
    private var mStoreName: String = ""
    private var mIsModify: Boolean = false


    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setReviewPoint(reviewPoint: Int) {
        mReviewPoint = reviewPoint
    }

    fun getReviewPoint(): Int {
        return mReviewPoint
    }

    fun setStoreName(storeName: String) {
        mStoreName = storeName
    }

    fun getStoreName(): String {
        return mStoreName
    }

    fun setIsModify(isModify: Boolean) {
        mIsModify = isModify
    }

    fun getIsModify(): Boolean {
        return mIsModify
    }

}