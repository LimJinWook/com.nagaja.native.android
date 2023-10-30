package com.nagaja.app.android.Data

import java.io.Serializable

class LocationNationData : Serializable {
    private var mLocationUid: Int = 0
    private var mLocationName: String = ""
    private var mNationUid: Int = 0

    fun setLocationUid(locationUid: Int) {
        mLocationUid = locationUid
    }

    fun getLocationUid(): Int {
        return mLocationUid
    }

    fun setLocationName(locationName: String) {
        mLocationName = locationName
    }

    fun getLocationName(): String {
        return mLocationName
    }

    fun setNationUid(nationUid: Int) {
        mNationUid = nationUid
    }

    fun getNationUid(): Int {
        return mNationUid
    }

}