package com.nagaja.app.android.Data

import java.io.Serializable

class CompanyDefaultData : Serializable {
    private var mCompanyUid: Int = 0
    private var mCategoryUid: Int = 0
    private var mCategoryName: String = ""
    private var mCompanyNameEnglish: String = ""
    private var mCompanyName: String = ""
    private var mManageName: String = ""
    private var mCompanyAddress: String = ""
    private var mCompanyAddressDetail: String = ""
    private var mCompanyMainImageUrl: String = ""
    private var mCompanyMainOrigin: String = ""
    private var mCompanyServiceUid: Int = 0
    private var mServiceStatus: String = ""         // 1: 영업준비중, 3: 오픈준비중, 5: 영업중, 7: 휴식시간, 8: 마감중, 9: 영업종료



    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
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

    fun setCompanyNameEnglish(companyNameEnglish: String) {
        mCompanyNameEnglish = companyNameEnglish
    }

    fun getCompanyNameEnglish(): String {
        return mCompanyNameEnglish
    }

    fun setCompanyName(companyName: String) {
        mCompanyName = companyName
    }

    fun getCompanyName(): String {
        return mCompanyName
    }

    fun setManageName(manageName: String) {
        mManageName = manageName
    }

    fun getManageName(): String {
        return mManageName
    }

    fun setCompanyAddress(companyAddress: String) {
        mCompanyAddress = companyAddress
    }

    fun getCompanyAddress(): String {
        return mCompanyAddress
    }

    fun setCompanyAddressDetail(companyAddressDetail: String) {
        mCompanyAddressDetail = companyAddressDetail
    }

    fun getCompanyAddressDetail(): String {
        return mCompanyAddressDetail
    }

    fun setCompanyMainImageUrl(companyMainImageUrl: String) {
        mCompanyMainImageUrl = companyMainImageUrl
    }

    fun getCompanyMainImageUrl(): String {
        return mCompanyMainImageUrl
    }

    fun setCompanyMainOrigin(companyMainOrigin: String) {
        mCompanyMainOrigin = companyMainOrigin
    }

    fun getCompanyMainOrigin(): String {
        return mCompanyMainOrigin
    }

    fun setCompanyServiceUid(companyServiceUid: Int) {
        mCompanyServiceUid = companyServiceUid
    }

    fun getCompanyServiceUid(): Int {
        return mCompanyServiceUid
    }

    fun setServiceStatus(serviceStatus: String) {
        mServiceStatus = serviceStatus
    }

    fun getServiceStatus(): String {
        return mServiceStatus
    }
}