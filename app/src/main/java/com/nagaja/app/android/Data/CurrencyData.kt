package com.nagaja.app.android.Data

import java.io.Serializable

class CurrencyData : Serializable {
    private var mCurrencyUid: Int = 0
    private var mCurrencyCode: String = ""
    private var mCurrencyName: String = ""
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""


    fun setCurrencyUid(currencyUid: Int) {
        mCurrencyUid = currencyUid
    }

    fun getCurrencyUid(): Int {
        return mCurrencyUid
    }

    fun setCurrencyCode(currencyCode: String) {
        mCurrencyCode = currencyCode
    }

    fun getCurrencyCode(): String {
        return mCurrencyCode
    }

    fun setCurrencyName(currencyName: String) {
        mCurrencyName = currencyName
    }

    fun getCurrencyName(): String {
        return mCurrencyName
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