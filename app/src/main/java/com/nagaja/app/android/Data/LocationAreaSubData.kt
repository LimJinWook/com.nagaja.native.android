package com.nagaja.app.android.Data

import java.io.Serializable

class LocationAreaSubData : Serializable {
    private var mCategoryUid: Int = 0
    private var mCategoryDepth: Int = 0
    private var mCategorySort: Int = 0
    private var mCategoryName: String = ""
    private var mCategoryNameEnglish: String = ""
    private var mCategoryUri: String = ""
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mParentCategoryUid: Int = 0
    private var mCategoryLatitude: Double = 0.0
    private var mCategoryLongitude: Double = 0.0
    private var mIsSelect: Boolean = false
    private var mIsCategoryStatusEnable: Boolean = false


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

    fun setCategoryUri(categoryUri: String) {
        mCategoryUri = categoryUri
    }

    fun getCategoryUri(): String {
        return mCategoryUri
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

    fun setParentCategoryUid(parentCategoryUid: Int) {
        mParentCategoryUid = parentCategoryUid
    }

    fun getParentCategoryUid(): Int {
        return mParentCategoryUid
    }

    fun setCategoryLatitude(categoryLatitude: Double) {
        mCategoryLatitude = categoryLatitude
    }

    fun getCategoryLatitude(): Double {
        return mCategoryLatitude
    }

    fun setIsSelect(isSelect: Boolean) {
        mIsSelect = isSelect
    }

    fun getIsSelect(): Boolean {
        return mIsSelect
    }

    fun setCategoryLongitude(categoryLongitude: Double) {
        mCategoryLongitude = categoryLongitude
    }

    fun getCategoryLongitude(): Double {
        return mCategoryLongitude
    }

    fun setIsCategoryStatusEnable(isCategoryStatusEnable: Boolean) {
        mIsCategoryStatusEnable = isCategoryStatusEnable
    }

    fun getIsCategoryStatusEnable(): Boolean {
        return mIsCategoryStatusEnable
    }

}