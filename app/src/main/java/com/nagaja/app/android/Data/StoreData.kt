package com.nagaja.app.android.Data

import java.io.Serializable

class StoreData : Serializable {
    private var mTotalCount: Int = 0
    private var mCompanyUid: Int = 0
    private var mCompanyStatus: Int = 0
    private var mCompanyName: String = ""
    private var mCompanyNameEnglish: String = ""
    private var mCompanyNationPhoneCode: String = ""
    private var mCompanyEmail: String = ""
    private var mCompanyPhoneNumber: String = ""
    private var mCompanyLatitude: Double = 0.0
    private var mCompanyLongitude: Double = 0.0
    private var mCompanyAddress: String = ""
    private var mCompanyAddressDetail: String = ""
    private var mCompanyLicenseImageOrigin: String = ""
    private var mCompanyLicenseImage: String = ""
    private var mCompanyDesc: String = ""
    private var mCompanyFacebookUrl: String = ""
    private var mCompanyKakaoUrl: String = ""
    private var mCompanyLineUrl: String = ""
    private var mCompanyManagerLimit: Int = 0
    private var mCategoryUid: Int = 0
    private var mNationUid: Int = 0
    private var mMemberUid: Int = 0
    private var mIsCompanyPublicYn: Boolean = false
    private var mIsCompanyCertificationYn: Boolean = false
    private var mIsCompanyCertificationMarkYn: Boolean = false
    private var mCertificationBeginDate: String = ""
    private var mCertificationEndDate: String = ""
    private var mIsUseYn: Boolean = false
    private var mConfirmDate: String = ""
    private var mCompanyManagerName: String = ""
    private var mCompanyMainImage: String = ""
    private var mCompanyMainImageOrigin: String = ""
    private var mCompanyServiceUid: Int = 0
    private var mServiceStatusUid: Int = 0
    private var mServiceMonthOffDays: String = ""
    private var mServiceWeekOffDays: String = ""
    private var mServiceWeekDoubleOffDays: String = ""
    private var mServiceBeginTime: String = ""
    private var mServiceEndTime: String = ""
    private var mIsDeliveryUseYn: Boolean = false
    private var mIsReservationUseYn: Boolean = false
    private var mIsPickUpUseYn: Boolean = false
    private var mIsParkingUseYn: Boolean = false
    private var mIsPetUseYn: Boolean = false
    private var mRegularUid: Int = 0
    private var mIsRegularUseYn: Boolean = false
    private var mIsRecommendUseYn: Boolean = false
    private var mCompanyReviewCount: Int = 0
    private var mCompanyRecommendCount: Int = 0
    private var mCompanyRegularCount: Int = 0
    private var mCompanyReviewPointAverage: Double = 0.0
    private var mIsSelect: Boolean = false


    fun setTotalCount(totalCount: Int) {
        mTotalCount = totalCount
    }

    fun getTotalCount(): Int {
        return mTotalCount
    }

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

    fun setCompanyName(companyName: String) {
        mCompanyName = companyName
    }

    fun getCompanyName(): String {
        return mCompanyName
    }

    fun setCompanyNameEnglish(companyNameEnglish: String) {
        mCompanyNameEnglish = companyNameEnglish
    }

    fun getCompanyNameEnglish(): String {
        return mCompanyNameEnglish
    }

    fun setCompanyNationPhoneCode(companyNationPhoneCode: String) {
        mCompanyNationPhoneCode = companyNationPhoneCode
    }

    fun getCompanyNationPhoneCode(): String {
        return mCompanyNationPhoneCode
    }

    fun setCompanyEmail(companyEmail: String) {
        mCompanyEmail = companyEmail
    }

    fun getCompanyEmail(): String {
        return mCompanyEmail
    }

    fun setCompanyPhoneNumber(companyPhoneNumber: String) {
        mCompanyPhoneNumber = companyPhoneNumber
    }

    fun getCompanyPhoneNumber(): String {
        return mCompanyPhoneNumber
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

    fun setCompanyLicenseImage(companyLicenseImage: String) {
        mCompanyLicenseImage = companyLicenseImage
    }

    fun getCompanyLicenseImage(): String {
        return mCompanyLicenseImage
    }

    fun setCompanyDesc(companyDesc: String) {
        mCompanyDesc = companyDesc
    }

    fun getCompanyDesc(): String {
        return mCompanyDesc
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

    fun setIsCompanyPublicYn(isCompanyPublicYn: Boolean) {
        mIsCompanyPublicYn = isCompanyPublicYn
    }

    fun getIsCompanyPublicYn(): Boolean {
        return mIsCompanyPublicYn
    }

    fun setIsCompanyCertificationYn(isCompanyCertificationYn: Boolean) {
        mIsCompanyCertificationYn = isCompanyCertificationYn
    }

    fun getIsCompanyCertificationYn(): Boolean {
        return mIsCompanyCertificationYn
    }

    fun setIsCompanyCertificationMarkYn(isCompanyCertificationMarkYn: Boolean) {
        mIsCompanyCertificationMarkYn = isCompanyCertificationMarkYn
    }

    fun getIsCompanyCertificationMarkYn(): Boolean {
        return mIsCompanyCertificationMarkYn
    }

    fun setCertificationBeginDate(certificationBeginDate: String) {
        mCertificationBeginDate = certificationBeginDate
    }

    fun getCertificationBeginDate(): String {
        return mCertificationBeginDate
    }

    fun setCertificationEndDate(certificationEndDate: String) {
        mCertificationEndDate = certificationEndDate
    }

    fun getCertificationEndDate(): String {
        return mCertificationEndDate
    }

    fun setIsUseYn(isUseYn: Boolean) {
        mIsUseYn = isUseYn
    }

    fun getIsUseYn(): Boolean {
        return mIsUseYn
    }

    fun setConfirmDate(confirmDate: String) {
        mConfirmDate = confirmDate
    }

    fun getConfirmDate(): String {
        return mConfirmDate
    }

    fun setCompanyManagerName(companyManagerName: String) {
        mCompanyManagerName = companyManagerName
    }

    fun getCompanyManagerName(): String {
        return mCompanyManagerName
    }

    fun setCompanyMainImage(companyMainImage: String) {
        mCompanyMainImage = companyMainImage
    }

    fun getCompanyMainImage(): String {
        return mCompanyMainImage
    }

    fun setCompanyMainImageOrigin(companyMainImageOrigin: String) {
        mCompanyMainImageOrigin = companyMainImageOrigin
    }

    fun getCompanyMainImageOrigin(): String {
        return mCompanyMainImageOrigin
    }

    fun setCompanyServiceUid(companyServiceUid: Int) {
        mCompanyServiceUid = companyServiceUid
    }

    fun getCompanyServiceUid(): Int {
        return mCompanyServiceUid
    }

    fun setServiceStatusUid(serviceStatusUid: Int) {
        mServiceStatusUid = serviceStatusUid
    }

    fun getServiceStatusUid(): Int {
        return mServiceStatusUid
    }

    fun setServiceMonthOffDays(serviceMonthOffDays: String) {
        mServiceMonthOffDays = serviceMonthOffDays
    }

    fun getServiceMonthOffDays(): String {
        return mServiceMonthOffDays
    }

    fun setServiceWeekOffDays(serviceWeekOffDays: String) {
        mServiceWeekOffDays = serviceWeekOffDays
    }

    fun getServiceWeekOffDays(): String {
        return mServiceWeekOffDays
    }

    fun setServiceWeekDoubleOffDays(serviceWeekDoubleOffDays: String) {
        mServiceWeekDoubleOffDays = serviceWeekDoubleOffDays
    }

    fun getServiceWeekDoubleOffDays(): String {
        return mServiceWeekDoubleOffDays
    }

    fun setServiceBeginTime(serviceBeginTime: String) {
        mServiceBeginTime = serviceBeginTime
    }

    fun getServiceBeginTime(): String {
        return mServiceBeginTime
    }

    fun setServiceEndTime(serviceEndTime: String) {
        mServiceEndTime = serviceEndTime
    }

    fun getServiceEndTime(): String {
        return mServiceEndTime
    }

    fun setIsDeliveryUseYn(isDeliveryUseYn: Boolean) {
        mIsDeliveryUseYn = isDeliveryUseYn
    }

    fun getIsDeliveryUseYn(): Boolean {
        return mIsDeliveryUseYn
    }

    fun setIsReservationUseYn(isReservationUseYn: Boolean) {
        mIsReservationUseYn = isReservationUseYn
    }

    fun getIsReservationUseYn(): Boolean {
        return mIsReservationUseYn
    }

    fun setIsPickUpUseYn(isPickUpUseYn: Boolean) {
        mIsPickUpUseYn = isPickUpUseYn
    }

    fun getIsPickUpUseYn(): Boolean {
        return mIsPickUpUseYn
    }

    fun setIsParkingUseYn(isParkingUseYn: Boolean) {
        mIsParkingUseYn = isParkingUseYn
    }

    fun getIsParkingUseYn(): Boolean {
        return mIsParkingUseYn
    }

    fun setIsPetUseYn(isPetUseYn: Boolean) {
        mIsPetUseYn = isPetUseYn
    }

    fun getIsPetUseYn(): Boolean {
        return mIsPetUseYn
    }

    fun setRegularUid(regularUid: Int) {
        mRegularUid = regularUid
    }

    fun getRegularUid(): Int {
        return mRegularUid
    }

    fun setIsRegularUseYn(isRegularUseYn: Boolean) {
        mIsRegularUseYn = isRegularUseYn
    }

    fun getIsRegularUseYn(): Boolean {
        return mIsRegularUseYn
    }

    fun setIsRecommendUseYn(isRecommendUseYn: Boolean) {
        mIsRecommendUseYn = isRecommendUseYn
    }

    fun getIsRecommendUseYn(): Boolean {
        return mIsRecommendUseYn
    }

    fun setCompanyReviewCount(companyReviewCount: Int) {
        mCompanyReviewCount = companyReviewCount
    }

    fun getCompanyReviewCount(): Int {
        return mCompanyReviewCount
    }

    fun setCompanyRecommendCount(companyRecommendCount: Int) {
        mCompanyRecommendCount = companyRecommendCount
    }

    fun getCompanyRecommendCount(): Int {
        return mCompanyRecommendCount
    }

    fun setCompanyRegularCount(companyRegularCount: Int) {
        mCompanyRegularCount = companyRegularCount
    }

    fun getCompanyRegularCount(): Int {
        return mCompanyRegularCount
    }

    fun setCompanyReviewPointAverage(companyReviewPointAverage: Double) {
        mCompanyReviewPointAverage = companyReviewPointAverage
    }

    fun getCompanyReviewPointAverage(): Double {
        return mCompanyReviewPointAverage
    }

    fun setIsSelect(isSelect: Boolean) {
        mIsSelect = isSelect
    }

    fun getIsSelect(): Boolean {
        return mIsSelect
    }

}