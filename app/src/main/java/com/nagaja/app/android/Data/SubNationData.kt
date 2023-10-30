package com.nagaja.app.android.Data

import java.io.Serializable

class SubNationData : Serializable {
    private var mId: Int = 0
    private var mSubNation: String = ""
    private var mIsSelect: Boolean = false

    fun setId(id: Int) {
        mId = id
    }

    fun getId(): Int {
        return mId
    }

    fun setSubNation(subNation: String) {
        mSubNation = subNation
    }

    fun getSubNation(): String {
        return mSubNation
    }

    fun setIsSelect(isSelect: Boolean) {
        mIsSelect = isSelect
    }

    fun getIsSelect(): Boolean {
        return mIsSelect
    }
}