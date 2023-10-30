package com.nagaja.app.android.Data

import java.io.Serializable

class StoreRecommendMenuData : Serializable {
    private var mMenuImageUrl: String = ""
    private var mName: String = ""
    private var mPrice: String = ""


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

    fun setPrice(price: String) {
        mPrice = price
    }

    fun getPrice(): String {
        return mPrice
    }

}