package com.nagaja.app.android.Data

import java.io.Serializable

class ProductCategoryData : Serializable {
    private var mCategoryUid: Int = 0
    private var mCategoryDepth: Int = 0
    private var mCategorySort: Int = 0
    private var mCategoryName: String = ""
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mParentCategoryUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mBaseCategoryUid: Int = 0

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

    fun setCreateDate(createDate: String) {
        mCreateDate = createDate
    }

    fun getCreateDate(): String {
        return mCreateDate
    }

    fun setUpdateDate(updateDate: String) {
        mUpdateDate = updateDate
    }

    fun getUpdateDate(): String {
        return mUpdateDate
    }

    fun setParentCategoryUid(parentCategoryUid: Int) {
        mParentCategoryUid = parentCategoryUid
    }

    fun getParentCategoryUid(): Int {
        return mParentCategoryUid
    }

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setBaseCategoryUid(baseCategoryUid: Int) {
        mBaseCategoryUid = baseCategoryUid
    }

    fun getBaseCategoryUid(): Int {
        return mBaseCategoryUid
    }
}