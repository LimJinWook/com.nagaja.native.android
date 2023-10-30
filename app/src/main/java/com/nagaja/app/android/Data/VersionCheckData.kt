package com.nagaja.app.android.Data

import java.io.Serializable

class VersionCheckData: Serializable {
    private var mVersion: String? = ""
    private var mIsServerCheck: Boolean? = false
    private var mServerCheckStartDate: String? = ""
    private var mServerCheckEndDate: String? = ""

    fun setVersion(version: String) {
        mVersion = version
    }

    fun getVersion(): String? {
        return mVersion
    }

    fun setIsServerCheck(isServerCheck: Boolean) {
        mIsServerCheck = isServerCheck
    }

    fun getIsServerCheck(): Boolean? {
        return mIsServerCheck
    }

    fun setServerCheckStartDate(serverCheckStartDate: String) {
        mServerCheckStartDate = serverCheckStartDate
    }

    fun getServerCheckStartDate(): String? {
        return mServerCheckStartDate
    }

    fun setServerCheckEndDate(serverCheckEndDate: String) {
        mServerCheckEndDate = serverCheckEndDate
    }

    fun getServerCheckEndDate(): String? {
        return mServerCheckEndDate
    }
}