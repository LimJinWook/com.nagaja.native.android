package com.nagaja.app.android.Data

import java.io.Serializable

class SearchCurrencyData : Serializable {
    private var mCode: String = ""
    private var mPrice: Double = 0.0

    fun setCode(code: String) {
        mCode = code
    }

    fun getCode(): String {
        return mCode
    }

    fun setPrice(price: Double) {
        mPrice = price
    }

    fun getPrice(): Double {
        return mPrice
    }

}