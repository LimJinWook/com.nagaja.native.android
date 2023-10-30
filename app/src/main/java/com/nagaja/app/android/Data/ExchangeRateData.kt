package com.nagaja.app.android.Data

import java.io.Serializable

class ExchangeRateData : Serializable {
    private var mCurrencyCode: String = ""
    private var mCurrencyDate: String = ""
    private var mCurrencyBegin: Double = 0.0
    private var mCurrencyNow: Double = 0.0
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""

    fun setCurrencyCode(currencyCode: String) {
        mCurrencyCode = currencyCode
    }

    fun getCurrencyCode(): String {
        return mCurrencyCode
    }

    fun setCurrencyDate(currencyDate: String) {
        mCurrencyDate = currencyDate
    }

    fun getCurrencyDate(): String {
        return mCurrencyDate
    }

    fun setCurrencyBegin(currencyBegin: Double) {
        mCurrencyBegin = currencyBegin
    }

    fun getCurrencyBegin(): Double {
        return mCurrencyBegin
    }

    fun setCurrencyNow(currencyNow: Double) {
        mCurrencyNow = currencyNow
    }

    fun getCurrencyNow(): Double {
        return mCurrencyNow
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