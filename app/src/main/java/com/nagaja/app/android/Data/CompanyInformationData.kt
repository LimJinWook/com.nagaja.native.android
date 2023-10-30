package com.nagaja.app.android.Data

import java.io.Serializable

class CompanyInformationData : Serializable {
    private var mCompanyUid: Int = 0
    private var mCompanyStatus: Int = 0
    private var mCompanyNameEnglish: String = ""
    private var mCompanyNationPhone: String = ""
    private var mCompanyEmail: String = ""
    private var mCompanyPhone: String = ""
    private var mCompanyLatitude: Double = 0.0
    private var mCompanyLongitude: Double = 0.0
    private var mCompanyAddress: String = ""
    private var mCompanyAddressDetail: String = ""
    private var mCompanyLicenseImageOrigin: String = ""
    private var mCompanyLicenseImageUrl: String = ""
    private var mCompanyLicenseName: String = ""
    private var mCompanyLicenseMasterName: String = ""
    private var mCompanyLicenseNumber: String = ""
    private var mCompanyDescription: String = ""
    private var mCompanyFacebookUrl: String = ""
    private var mCompanyKakaoUrl: String = ""
    private var mCompanyLineUrl: String = ""
    private var mCompanyManagerLimit: Int = 0
    private var mCompanyManagerName: String = ""
    private var mCategoryUid: Int = 0
    private var mNationUid: Int = 0
    private var mMemberUid: Int = 0
    private var mIsCompanyPublic: Boolean = false
    private var mIsCompanyCertification: Boolean = false
    private var mIsUseYn: Boolean = false
    private var mCompanyPayment: String = ""
    private var mCompanyNameListData = ArrayList<CompanyNameData>()
    private var mCompanyImageListData = ArrayList<CompanyInformationImageData>()



    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setCompanyStatus(companyStatus: Int) {
        mCompanyStatus = companyStatus
    }

    fun getCompanyStatus(): Int {
        return mCompanyStatus
    }

    fun setCompanyNameEnglish(companyNameEnglish: String) {
        mCompanyNameEnglish = companyNameEnglish
    }

    fun getCompanyNameEnglish(): String {
        return mCompanyNameEnglish
    }

    fun setCompanyNationPhone(companyNationPhone: String) {
        mCompanyNationPhone = companyNationPhone
    }

    fun getCompanyNationPhone(): String {
        return mCompanyNationPhone
    }

    fun setCompanyEmail(companyEmail: String) {
        mCompanyEmail = companyEmail
    }

    fun getCompanyEmail(): String {
        return mCompanyEmail
    }

    fun setCompanyPhone(companyPhone: String) {
        mCompanyPhone = companyPhone
    }

    fun getCompanyPhone(): String {
        return mCompanyPhone
    }

    fun setCompanyLatitude(companyLatitude: Double) {
        mCompanyLatitude = companyLatitude
    }

    fun getCompanyLatitude(): Double {
        return mCompanyLatitude
    }

    fun setCompanyLongitude(companyLongitude: Double) {
        mCompanyLongitude = companyLongitude
    }

    fun getCompanyLongitude(): Double {
        return mCompanyLongitude
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

    fun setCompanyLicenseImageOrigin(companyLicenseImageOrigin: String) {
        mCompanyLicenseImageOrigin = companyLicenseImageOrigin
    }

    fun getCompanyLicenseImageOrigin(): String {
        return mCompanyLicenseImageOrigin
    }

    fun setCompanyLicenseImageUrl(companyLicenseImageUrl: String) {
        mCompanyLicenseImageUrl = companyLicenseImageUrl
    }

    fun getCompanyLicenseImageUrl(): String {
        return mCompanyLicenseImageUrl
    }

    fun setCompanyLicenseName(companyLicenseName: String) {
        mCompanyLicenseName = companyLicenseName
    }

    fun getCompanyLicenseName(): String {
        return mCompanyLicenseName
    }

    fun setCompanyLicenseMasterName(companyLicenseMasterName: String) {
        mCompanyLicenseMasterName = companyLicenseMasterName
    }

    fun getCompanyLicenseMasterName(): String {
        return mCompanyLicenseMasterName
    }

    fun setCompanyLicenseNumber(companyLicenseNumber: String) {
        mCompanyLicenseNumber = companyLicenseNumber
    }

    fun getCompanyLicenseNumber(): String {
        return mCompanyLicenseNumber
    }

    fun setCompanyDescription(companyDescription: String) {
        mCompanyDescription = companyDescription
    }

    fun getCompanyDescription(): String {
        return mCompanyDescription
    }

    fun setCompanyFacebookUrl(companyFacebookUrl: String) {
        mCompanyFacebookUrl = companyFacebookUrl
    }

    fun getCompanyFacebookUrl(): String {
        return mCompanyFacebookUrl
    }

    fun setCompanyKakaoUrl(companyKakaoUrl: String) {
        mCompanyKakaoUrl = companyKakaoUrl
    }

    fun getCompanyKakaoUrl(): String {
        return mCompanyKakaoUrl
    }

    fun setCompanyLineUrl(companyLineUrl: String) {
        mCompanyLineUrl = companyLineUrl
    }

    fun getCompanyLineUrl(): String {
        return mCompanyLineUrl
    }

    fun setCompanyManagerLimit(companyManagerLimit: Int) {
        mCompanyManagerLimit = companyManagerLimit
    }

    fun getCompanyManagerLimit(): Int {
        return mCompanyManagerLimit
    }

    fun setCompanyManagerName(companyManagerName: String) {
        mCompanyManagerName = companyManagerName
    }

    fun getCompanyManagerName(): String {
        return mCompanyManagerName
    }

    fun setCategoryUid(categoryUid: Int) {
        mCategoryUid = categoryUid
    }

    fun getCategoryUid(): Int {
        return mCategoryUid
    }

    fun setNationUid(nationUid: Int) {
        mNationUid = nationUid
    }

    fun getNationUid(): Int {
        return mNationUid
    }

    fun setMemberUid(memberUid: Int) {
        mMemberUid = memberUid
    }

    fun getMemberUid(): Int {
        return mMemberUid
    }

    fun setIsCompanyPublic(isCompanyPublic: Boolean) {
        mIsCompanyPublic = isCompanyPublic
    }

    fun getIsCompanyPublic(): Boolean {
        return mIsCompanyPublic
    }

    fun setIsCompanyCertification(isCompanyCertification: Boolean) {
        mIsCompanyCertification = isCompanyCertification
    }

    fun getIsCompanyCertification(): Boolean {
        return mIsCompanyCertification
    }

    fun setIsUseYn(isUseYn: Boolean) {
        mIsUseYn = isUseYn
    }

    fun getIsUseYn(): Boolean {
        return mIsUseYn
    }

    fun setCompanyPayment(companyPayment: String) {
        mCompanyPayment = companyPayment
    }

    fun getCompanyPayment(): String {
        return mCompanyPayment
    }

    fun setCompanyNameListData(companyNameListData: ArrayList<CompanyNameData>) {
        mCompanyNameListData = companyNameListData
    }

    fun getCompanyNameListData(): ArrayList<CompanyNameData> {
        return mCompanyNameListData
    }

    fun setCompanyImageListData(companyImageListData: ArrayList<CompanyInformationImageData>) {
        mCompanyImageListData = companyImageListData
    }

    fun getCompanyImageListData(): ArrayList<CompanyInformationImageData> {
        return mCompanyImageListData
    }

}