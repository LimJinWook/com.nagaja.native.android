package com.nagaja.app.android.Data

import java.io.Serializable

class AddressData: Serializable {
    private var mPostalCode: String? = ""
    private var mAddress: String? = ""
    private var mExtAddress: String? = ""
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0

    fun setPostalCode(postalCode: String) {
        mPostalCode = postalCode
    }

    fun getPostalCode(): String? {
        return mPostalCode
    }

    fun setAddress(address: String) {
        mAddress = address
    }

    fun getAddress(): String? {
        return mAddress
    }

    fun setExtAddress(extAddress: String) {
        mExtAddress = extAddress
    }

    fun getExtAddress(): String? {
        return mExtAddress
    }

    fun setLatitude(latitude: Double) {
        mLatitude = latitude
    }

    fun getLatitude(): Double {
        return mLatitude
    }

    fun setLongitude(longitude: Double) {
        mLongitude = longitude
    }

    fun getLongitude(): Double {
        return mLongitude
    }
}