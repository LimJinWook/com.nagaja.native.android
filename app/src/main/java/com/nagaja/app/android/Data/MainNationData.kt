package com.nagaja.app.android.Data

import java.io.Serializable

class MainNationData : Serializable {
    private var mId: Int = 0
    private var mMainNation: String = ""
    private var mIsSelect: Boolean = false

    fun setId(id: Int) {
        mId = id
    }

    fun getId(): Int {
        return mId
    }

    fun setMainNation(mainNation: String) {
        mMainNation = mainNation
    }

    fun getMainNation(): String {
        return mMainNation
    }

    fun setIsSelect(isSelect: Boolean) {
        mIsSelect = isSelect
    }

    fun getIsSelect(): Boolean {
        return mIsSelect
    }
}