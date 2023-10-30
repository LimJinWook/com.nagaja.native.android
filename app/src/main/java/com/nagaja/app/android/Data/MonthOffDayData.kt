package com.nagaja.app.android.Data

import java.io.Serializable

class MonthOffDayData : Serializable {
    private var mDate: String = ""
    private var mIsSelect: Boolean = false

    fun setDate(date: String) {
        mDate = date
    }

    fun getDate(): String {
        return mDate
    }

    fun setIsSelect(isSelect: Boolean) {
        mIsSelect = isSelect
    }

    fun getIsSelect(): Boolean {
        return mIsSelect
    }
}