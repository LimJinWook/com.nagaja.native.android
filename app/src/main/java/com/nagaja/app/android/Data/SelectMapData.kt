package com.nagaja.app.android.Data

import java.io.Serializable

class SelectMapData : Serializable {
    private var mMainAddress: String = ""
    private var mSubAddress: String = ""
    private var mLocation: String = ""

    fun setMainAddress(mainAddress: String) {
        mMainAddress = mainAddress
    }

    fun getMainAddress(): String {
        return mMainAddress
    }

    fun setSubAddress(subAddress: String) {
        mSubAddress = subAddress
    }

    fun getSubAddress(): String {
        return mSubAddress
    }

    fun setLocation(location: String) {
        mLocation = location
    }

    fun getLocation(): String {
        return mLocation
    }
}