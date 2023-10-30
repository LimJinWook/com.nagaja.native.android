package com.nagaja.app.android.Data

import java.io.Serializable

class JwtTokenRefreshData: Serializable {
    private var mSecureKey: String? = ""
    private var mRefreshKey: String? = ""

    fun setSecureKey(secureKey: String) {
        mSecureKey = secureKey
    }

    fun getSecureKey(): String? {
        return mSecureKey
    }

    fun setRefreshKey(refreshKey: String) {
        mRefreshKey = refreshKey
    }

    fun getRefreshKey(): String? {
        return mRefreshKey
    }
}