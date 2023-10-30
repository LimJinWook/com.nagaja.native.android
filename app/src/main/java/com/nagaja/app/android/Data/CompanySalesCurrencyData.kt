package com.nagaja.app.android.Data

import java.io.Serializable

class CompanySalesCurrencyData : Serializable {
    private var mCompanyUid: Int = 0
    private var mCurrencyUid: Int = 0
    private var mIsCurrencyBase: Boolean = false
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setCurrencyUid(currencyUid: Int) {
        mCurrencyUid = currencyUid
    }

    fun getCurrencyUid(): Int {
        return mCurrencyUid
    }

    fun setIsCurrencyBase(isCurrencyBase: Boolean) {
        mIsCurrencyBase = isCurrencyBase
    }

    fun getIsCurrencyBase(): Boolean {
        return mIsCurrencyBase
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