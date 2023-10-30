package com.nagaja.app.android.Data

import java.io.Serializable

class UsedMarketCurrencyData : Serializable {
    private var mItemUid: Int = 0
    private var mCurrencyCode: String = ""
    private var mCurrencyPrice: Double = 0.0
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mCurrencyPriceString: String = ""



    fun setItemUid(itemUid: Int) {
        mItemUid = itemUid
    }

    fun getItemUid(): Int {
        return mItemUid
    }

    fun setCurrencyCode(currencyCode: String) {
        mCurrencyCode = currencyCode
    }

    fun getCurrencyCode(): String {
        return mCurrencyCode
    }

    fun setCurrencyPrice(currencyPrice: Double) {
        mCurrencyPrice = currencyPrice
    }

    fun getCurrencyPrice(): Double {
        return mCurrencyPrice
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

    fun setCurrencyPriceString(currencyPriceString: String) {
        mCurrencyPriceString = currencyPriceString
    }

    fun getCurrencyPriceString(): String {
        return mCurrencyPriceString
    }

}