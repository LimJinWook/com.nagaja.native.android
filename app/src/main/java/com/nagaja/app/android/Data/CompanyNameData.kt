package com.nagaja.app.android.Data

import java.io.Serializable

class CompanyNameData : Serializable {
    private var mCompanyNameUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mNationUid: Int = 0
    private var mCompanyName: String = ""
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""

    fun setCompanyNameUid(companyNameUid: Int) {
        mCompanyNameUid = companyNameUid
    }

    fun getCompanyNameUid(): Int {
        return mCompanyNameUid
    }

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setNationUid(nationUid: Int) {
        mNationUid = nationUid
    }

    fun getNationUid(): Int {
        return mNationUid
    }

    fun setCompanyName(companyName: String) {
        mCompanyName = companyName
    }

    fun getCompanyName(): String {
        return mCompanyName
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