package com.nagaja.app.android.Data

import java.io.Serializable

class DefaultSettingAreaData : Serializable {
    private var mVirtualUid: Int = 0
    private var mLocationUid: Int = 0
    private var mLocationName: String = ""
    private var mCategoryUid: Int = 0
    private var mCategoryName: String = ""
    private var mCategoryNameEnglish: String = ""
    private var mCategoryAreaUid: Int = 0
    private var mCategoryAreaName: String = ""
    private var mCategoryAreaNameEnglish: String = ""
    private var mVirtualLatitude: Double = 0.0
    private var mVirtualLongitude: Double = 0.0

    fun setVirtualUid(virtualUid: Int) {
        mVirtualUid = virtualUid
    }

    fun getVirtualUid(): Int {
        return mVirtualUid
    }

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

    fun setCategoryUid(categoryUid: Int) {
        mCategoryUid = categoryUid
    }

    fun getCategoryUid(): Int {
        return mCategoryUid
    }

    fun setCategoryName(categoryName: String) {
        mCategoryName = categoryName
    }

    fun getCategoryName(): String {
        return mCategoryName
    }

    fun setCategoryNameEnglish(categoryNameEnglish: String) {
        mCategoryNameEnglish = categoryNameEnglish
    }

    fun getCategoryNameEnglish(): String {
        return mCategoryNameEnglish
    }

    fun setCategoryAreaUid(categoryAreaUid: Int) {
        mCategoryAreaUid = categoryAreaUid
    }

    fun getCategoryAreaUid(): Int {
        return mCategoryAreaUid
    }

    fun setCategoryAreaName(categoryAreaName: String) {
        mCategoryAreaName = categoryAreaName
    }

    fun getCategoryAreaName(): String {
        return mCategoryAreaName
    }

    fun setCategoryAreaNameEnglish(categoryAreaNameEnglish: String) {
        mCategoryAreaNameEnglish = categoryAreaNameEnglish
    }

    fun getCategoryAreaNameEnglish(): String {
        return mCategoryAreaNameEnglish
    }

    fun setVirtualLatitude(virtualLatitude: Double) {
        mVirtualLatitude = virtualLatitude
    }

    fun getVirtualLatitude(): Double {
        return mVirtualLatitude
    }

    fun setVirtualLongitude(virtualLongitude: Double) {
        mVirtualLongitude = virtualLongitude
    }

    fun getVirtualLongitude(): Double {
        return mVirtualLongitude
    }

}