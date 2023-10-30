package com.nagaja.app.android.Data

import java.io.Serializable

class StoreDetailImageData : Serializable {
    private var mCompanyImageUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mCompanyImageSort: Int = 0
    private var mCompanyOrigin: String = ""
    private var mCompanyImageName: String = ""
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""


    fun setCompanyImageUid(companyImageUid: Int) {
        mCompanyImageUid = companyImageUid
    }

    fun getCompanyImageUid(): Int {
        return mCompanyImageUid
    }

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setCompanyImageSort(companyImageSort: Int) {
        mCompanyImageSort = companyImageSort
    }

    fun getCompanyImageSort(): Int {
        return mCompanyImageSort
    }

    fun setCompanyOrigin(companyOrigin: String) {
        mCompanyOrigin = companyOrigin
    }

    fun getCompanyOrigin(): String {
        return mCompanyOrigin
    }

    fun setCompanyImageName(companyImageName: String) {
        mCompanyImageName = companyImageName
    }

    fun getCompanyImageName(): String {
        return mCompanyImageName
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

    fun setUpdateDate(updateDate: String) {
        mUpdateDate = updateDate
    }

    fun getUpdateDate(): String {
        return mUpdateDate
    }

}