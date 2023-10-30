package com.nagaja.app.android.Data

import java.io.Serializable

class CategoryData : Serializable {
    private var mTitle: String = ""
    private var mTypeValue: Int = -1
    private var mIsSelect: Boolean = false

    fun setTitle(title: String) {
        mTitle = title
    }

    fun getTitle(): String {
        return mTitle
    }

    fun setTypeValue(typeValue: Int) {
        mTypeValue = typeValue
    }

    fun getTypeValue(): Int {
        return mTypeValue
    }

    fun setIsSelect(isSelect: Boolean) {
        mIsSelect = isSelect
    }

    fun getIsSelect(): Boolean {
        return mIsSelect
    }
}