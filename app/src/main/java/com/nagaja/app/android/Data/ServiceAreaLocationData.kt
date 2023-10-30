package com.nagaja.app.android.Data

import java.io.Serializable

class ServiceAreaLocationData : Serializable {
    private var mCategoryUid: Int = 0
    private var mCategoryDepth: Int = 0
    private var mCategorySort: Int = 0
    private var mCategoryStatus: Int = 0
    private var mCategoryName: String = ""
    private var mCategoryEnglishName: String = ""
    private var mParentCategoryUid: Int = 0
    private var mParentCategoryName: String = ""
    private var mCategoryLatitude: Double = 0.0
    private var mCategoryLongitude: Double = 0.0
    private var mRootCategoryUid: Int = 0
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mIsSelect: Boolean = false


    fun setCategoryUid(categoryUid: Int) {
        mCategoryUid = categoryUid
    }

    fun getCategoryUid(): Int {
        return mCategoryUid
    }

    fun setCategoryDepth(categoryDepth: Int) {
        mCategoryDepth = categoryDepth
    }

    fun getCategoryDepth(): Int {
        return mCategoryDepth
    }

    fun setCategorySort(categorySort: Int) {
        mCategorySort = categorySort
    }

    fun getCategorySort(): Int {
        return mCategorySort
    }

    fun setCategoryStatus(categoryStatus: Int) {
        mCategoryStatus = categoryStatus
    }

    fun getCategoryStatus(): Int {
        return mCategoryStatus
    }

    fun setCategoryName(categoryName: String) {
        mCategoryName = categoryName
    }

    fun getCategoryName(): String {
        return mCategoryName
    }

    fun setCategoryEnglishName(categoryEnglishName: String) {
        mCategoryEnglishName = categoryEnglishName
    }

    fun getCategoryEnglishName(): String {
        return mCategoryEnglishName
    }

    fun setParentCategoryUid(parentCategoryUid: Int) {
        mParentCategoryUid = parentCategoryUid
    }

    fun getParentCategoryUid(): Int {
        return mParentCategoryUid
    }

    fun setParentCategoryName(parentCategoryName: String) {
        mParentCategoryName = parentCategoryName
    }

    fun getParentCategoryName(): String {
        return mParentCategoryName
    }

    fun setCategoryLongitude(categoryLongitude: Double) {
        mCategoryLongitude = categoryLongitude
    }

    fun getCategoryLongitude(): Double {
        return mCategoryLongitude
    }

    fun setCategoryLatitude(categoryLatitude: Double) {
        mCategoryLatitude = categoryLatitude
    }

    fun getCategoryLatitude(): Double {
        return mCategoryLatitude
    }

    fun setRootCategoryUid(rootCategoryUid: Int) {
        mRootCategoryUid = rootCategoryUid
    }

    fun getRootCategoryUid(): Int {
        return mRootCategoryUid
    }

    fun setIsUseYn(isUseYn: Boolean) {
        mIsUseYn = isUseYn
    }

    fun getIsUseYn(): Boolean {
        return mIsUseYn
    }

    fun setCreateDate(createDate: String) {
        mCreateDate = createDate
    }

    fun getCreateDate(): String {
        return mCreateDate
    }

    fun setIsSelect(isSelect: Boolean) {
        mIsSelect = isSelect
    }

    fun getIsSelect(): Boolean {
        return mIsSelect
    }

}