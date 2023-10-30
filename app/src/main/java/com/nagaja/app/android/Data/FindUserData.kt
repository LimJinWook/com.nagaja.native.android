package com.nagaja.app.android.Data

import java.io.Serializable

class FindUserData : Serializable {
    private var mUid: Int = 0
    private var mName: String = ""
    private var mIsSelect: Boolean = false

    fun setUid(uid: Int) {
        mUid = uid
    }

    fun getUid(): Int {
        return mUid
    }

    fun setName(name: String) {
        mName = name
    }

    fun getName(): String {
        return mName
    }

    fun setIsSelect(isSelect: Boolean) {
        mIsSelect = isSelect
    }

    fun getIsSelect(): Boolean {
        return mIsSelect
    }
}