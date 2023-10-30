package com.nagaja.app.android.Data

import java.io.Serializable

class StoreMenuData : Serializable {
    private var mMenuImageUrl: String = ""
    private var mName: String = ""
    private var mMessage: String = ""
    private var mPriceWon: String = ""
    private var mPriceDollar: String = ""
    private var mPricePeso: String = ""



    fun setMenuImageUrl(menuImageUrl: String) {
        mMenuImageUrl = menuImageUrl
    }

    fun getMenuImageUrl(): String {
        return mMenuImageUrl
    }

    fun setName(name: String) {
        mName = name
    }

    fun getName(): String {
        return mName
    }

    fun setMessage(message: String) {
        mMessage = message
    }

    fun getMessage(): String {
        return mMessage
    }

    fun setPriceWon(priceWon: String) {
        mPriceWon = priceWon
    }

    fun getPriceWon(): String {
        return mPriceWon
    }

    fun setPriceDollar(priceDollar: String) {
        mPriceDollar = priceDollar
    }

    fun getPriceDollar(): String {
        return mPriceDollar
    }

    fun setPricePeso(pricePeso: String) {
        mPricePeso = pricePeso
    }

    fun getPricePeso(): String {
        return mPricePeso
    }
}