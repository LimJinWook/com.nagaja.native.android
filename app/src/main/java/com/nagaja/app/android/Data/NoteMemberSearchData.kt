package com.nagaja.app.android.Data

import java.io.Serializable

class NoteMemberSearchData : Serializable {
    private var mUid: Int = 0
    private var mName: String = ""


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

}