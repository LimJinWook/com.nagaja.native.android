package com.nagaja.app.android.Data

import android.net.Uri
import java.io.Serializable

class CompanyInformationImageData : Serializable {
    private var mCompanyImageUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mCompanyImageSort: Int = 0
    private var mCompanyImageOrigin: String = ""
    private var mCompanyImageUrl: String = ""
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mViewType: Int = -1
    private var mIsDeviceImage = false
    private lateinit var mDeviceImageUri: Uri

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

    fun setCompanyImageOrigin(companyImageOrigin: String) {
        mCompanyImageOrigin = companyImageOrigin
    }

    fun getCompanyImageOrigin(): String {
        return mCompanyImageOrigin
    }

    fun setCompanyImageUrl(companyImageUrl: String) {
        mCompanyImageUrl = companyImageUrl
    }

    fun getCompanyImageUrl(): String {
        return mCompanyImageUrl
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

    fun setViewType(viewType: Int) {
        mViewType = viewType
    }

    fun getViewType(): Int {
        return mViewType
    }

    fun setIsDeviceImage(isDeviceImage: Boolean) {
        mIsDeviceImage = isDeviceImage
    }

    fun getIsDeviceImage(): Boolean {
        return mIsDeviceImage
    }

    fun setDeviceImageUri(deviceImageUri: Uri) {
        mDeviceImageUri = deviceImageUri
    }

    fun getDeviceImageUri(): Uri {
        return mDeviceImageUri
    }

}