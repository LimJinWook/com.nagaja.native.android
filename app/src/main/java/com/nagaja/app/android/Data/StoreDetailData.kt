package com.nagaja.app.android.Data

import java.io.Serializable

class StoreDetailData : Serializable {
    private var mCompanyUid: Int = 0
    private var mStatus: Int = 0
    private var mCompanyName: String = ""
    private var mCompanyNameEnglish: String = ""
    private var mNationPhone: String = ""
    private var mEmail: String = ""
    private var mPhoneNumber: String = ""
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0
    private var mAddress: String = ""
    private var mAddressDetail: String = ""
    private var mDesc: String = ""
    private var mFacebookUrl: String = ""
    private var mKakaoUrl: String = ""
    private var mLineUrl: String = ""
    private var mCategoryUid: Int = 0
    private var mNationUid: Int = 0
    private var mMemberUid: Int = 0
    private var mIsPublicYn: Boolean = false
    private var mIsCertificationYn: Boolean = false
    private var mIsCertificationMarkYn: Boolean = false
    private var mCertificationBeginDate: String = ""
    private var mCertificationEndDate: String = ""
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mConfirmDate: String = ""
    private var mManagerName: String = ""
    private var mPayment: String = ""
    private var mMainImageUrl: String = ""
    private var mMainImageOrigin: String = ""
    private var mServiceUid: Int = 0
    private var mServiceStatus: Int = 0
    private var mServiceMonthOffDays: String = ""
    private var mServiceWeekOffDays: String = ""
    private var mServiceWeekDoubleOffDays: String = ""
    private var mServiceBeginTime: String = ""
    private var mServiceEndTime: String = ""
    private var mFirstBreakBeginTime: String = ""
    private var mFirstBreakEndTime: String = ""
    private var mSecondBreakBeginTime: String = ""
    private var mSecondBreakEndTime: String = ""
    private var mFirstRestBeginTime: String = ""
    private var mFirstRestEndTime: String = ""
    private var mSecondRestBeginTime: String = ""
    private var mSecondRestEndTime: String = ""
    private var mDeliveryBeginTime: String = ""
    private var mDeliveryEndTime: String = ""
    private var mReservationBeginTime: String = ""
    private var mReservationEndTime: String = ""
    private var mReservationDayLimit: Int = 0
    private var mReservationPersonLimit: Int = 0
    private var mReservationTeamLimit: Int = 0
    private var mIsDeliveryUseYn: Boolean = false
    private var mIsReservationUseYn: Boolean = false
    private var mIsPickUpUseYn: Boolean = false
    private var mIsParkingUseYn: Boolean = false
    private var mIsPetUseYn: Boolean = false
    private var mReviewCount: Int = 0
    private var mRecommendCount: Int = 0
    private var mRegularCount: Int = 0
    private var mRegularUid: Int = 0
    private var mContractStatus: Int = 0
    private var mIsRegularUseYn: Boolean = false
    private var mIsRecommendUseYn: Boolean = false
    private var mStoreDetailImageListData = ArrayList<StoreDetailImageData>()





    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setStatus(status: Int) {
        mStatus = status
    }

    fun getStatus(): Int {
        return mStatus
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

    fun setNationPhone(nationPhone: String) {
        mNationPhone = nationPhone
    }

    fun getNationPhone(): String {
        return mNationPhone
    }

    fun setEmail(email: String) {
        mEmail = email
    }

    fun getEmail(): String {
        return mEmail
    }

    fun setPhoneNumber(phoneNumber: String) {
        mPhoneNumber = phoneNumber
    }

    fun getPhoneNumber(): String {
        return mPhoneNumber
    }

    fun setLatitude(latitude: Double) {
        mLatitude = latitude
    }

    fun getLatitude(): Double {
        return mLatitude
    }

    fun setLongitude(longitude: Double) {
        mLongitude = longitude
    }

    fun getLongitude(): Double {
        return mLongitude
    }

    fun setAddress(address: String) {
        mAddress = address
    }

    fun getAddress(): String {
        return mAddress
    }

    fun setAddressDetail(addressDetail: String) {
        mAddressDetail = addressDetail
    }

    fun getAddressDetail(): String {
        return mAddressDetail
    }

    fun setDesc(desc: String) {
        mDesc = desc
    }

    fun getDesc(): String {
        return mDesc
    }

    fun setFacebookUrl(facebookUrl: String) {
        mFacebookUrl = facebookUrl
    }

    fun getFacebookUrl(): String {
        return mFacebookUrl
    }

    fun setKakaoUrl(kakaoUrl: String) {
        mKakaoUrl = kakaoUrl
    }

    fun getKakaoUrl(): String {
        return mKakaoUrl
    }

    fun setLineUrl(lineUrl: String) {
        mLineUrl = lineUrl
    }

    fun getLineUrl(): String {
        return mLineUrl
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

    fun setIsPublicYn(isPublicYn: Boolean) {
        mIsPublicYn = isPublicYn
    }

    fun getIsPublicYn(): Boolean {
        return mIsPublicYn
    }

    fun setIsCertificationYn(isCertificationYn: Boolean) {
        mIsCertificationYn = isCertificationYn
    }

    fun getIsCertificationYn(): Boolean {
        return mIsCertificationYn
    }

    fun setIsCertificationMarkYn(isCertificationMarkYn: Boolean) {
        mIsCertificationMarkYn = isCertificationMarkYn
    }

    fun getIsCertificationMarkYn(): Boolean {
        return mIsCertificationMarkYn
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

    fun setConfirmDate(confirmDate: String) {
        mConfirmDate = confirmDate
    }

    fun getConfirmDate(): String {
        return mConfirmDate
    }

    fun setManagerName(managerName: String) {
        mManagerName = managerName
    }

    fun getManagerName(): String {
        return mManagerName
    }

    fun setPayment(payment: String) {
        mPayment = payment
    }

    fun getPayment(): String {
        return mPayment
    }

    fun setMainImageUrl(mainImageUrl: String) {
        mMainImageUrl = mainImageUrl
    }

    fun getMainImageUrl(): String {
        return mMainImageUrl
    }

    fun setMainImageOrigin(mainImageOrigin: String) {
        mMainImageOrigin = mainImageOrigin
    }

    fun getMainImageOrigin(): String {
        return mMainImageOrigin
    }

    fun setServiceUid(serviceUid: Int) {
        mServiceUid = serviceUid
    }

    fun getServiceUid(): Int {
        return mServiceUid
    }

    fun setServiceStatus(serviceStatus: Int) {
        mServiceStatus = serviceStatus
    }

    fun getServiceStatus(): Int {
        return mServiceStatus
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

    fun setFirstBreakBeginTime(firstBreakBeginTime: String) {
        mFirstBreakBeginTime = firstBreakBeginTime
    }

    fun getFirstBreakBeginTime(): String {
        return mFirstBreakBeginTime
    }

    fun setFirstBreakEndTime(firstBreakEndTime: String) {
        mFirstBreakEndTime = firstBreakEndTime
    }

    fun getFirstBreakEndTime(): String {
        return mFirstBreakEndTime
    }

    fun setSecondBreakBeginTime(secondBreakBeginTime: String) {
        mSecondBreakBeginTime = secondBreakBeginTime
    }

    fun getSecondBreakBeginTime(): String {
        return mSecondBreakBeginTime
    }

    fun setSecondBreakEndTime(secondBreakEndTime: String) {
        mSecondBreakEndTime = secondBreakEndTime
    }

    fun getSecondBreakEndTime(): String {
        return mSecondBreakEndTime
    }

    fun setFirstRestBeginTime(firstRestBeginTime: String) {
        mFirstRestBeginTime = firstRestBeginTime
    }

    fun getFirstRestBeginTime(): String {
        return mFirstRestBeginTime
    }

    fun setFirstRestEndTime(firstRestEndTime: String) {
        mFirstRestEndTime = firstRestEndTime
    }

    fun getFirstRestEndTime(): String {
        return mFirstRestEndTime
    }

    fun setSecondRestBeginTime(secondRestBeginTime: String) {
        mSecondRestBeginTime = secondRestBeginTime
    }

    fun getSecondRestBeginTime(): String {
        return mSecondRestBeginTime
    }

    fun setSecondRestEndTime(secondRestEndTime: String) {
        mSecondRestEndTime = secondRestEndTime
    }

    fun getSecondRestEndTime(): String {
        return mSecondRestEndTime
    }

    fun setDeliveryBeginTime(deliveryBeginTime: String) {
        mDeliveryBeginTime = deliveryBeginTime
    }

    fun getDeliveryBeginTime(): String {
        return mDeliveryBeginTime
    }

    fun setDeliveryEndTime(deliveryEndTime: String) {
        mDeliveryEndTime = deliveryEndTime
    }

    fun getDeliveryEndTime(): String {
        return mDeliveryEndTime
    }

    fun setReservationBeginTime(reservationBeginTime: String) {
        mReservationBeginTime = reservationBeginTime
    }

    fun getReservationBeginTime(): String {
        return mReservationBeginTime
    }

    fun setReservationEndTime(reservationEndTime: String) {
        mReservationEndTime = reservationEndTime
    }

    fun getReservationEndTime(): String {
        return mReservationEndTime
    }

    fun setReservationDayLimit(reservationDayLimit: Int) {
        mReservationDayLimit = reservationDayLimit
    }

    fun getReservationDayLimit(): Int {
        return mReservationDayLimit
    }

    fun setReservationPersonLimit(reservationPersonLimit: Int) {
        mReservationPersonLimit = reservationPersonLimit
    }

    fun getReservationPersonLimit(): Int {
        return mReservationPersonLimit
    }

    fun setReservationTeamLimit(reservationTeamLimit: Int) {
        mReservationTeamLimit = reservationTeamLimit
    }

    fun getReservationTeamLimit(): Int {
        return mReservationTeamLimit
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

    fun setReviewCount(reviewCount: Int) {
        mReviewCount = reviewCount
    }

    fun getReviewCount(): Int {
        return mReviewCount
    }

    fun setRecommendCount(recommendCount: Int) {
        mRecommendCount = recommendCount
    }

    fun getRecommendCount(): Int {
        return mRecommendCount
    }

    fun setRegularCount(regularCount: Int) {
        mRegularCount = regularCount
    }

    fun getRegularCount(): Int {
        return mRegularCount
    }

    fun setRegularUid(regularUid: Int) {
        mRegularUid = regularUid
    }

    fun getRegularUid(): Int {
        return mRegularUid
    }

    fun setContractStatus(contractStatus: Int) {
        mContractStatus = contractStatus
    }

    fun getContractStatus(): Int {
        return mContractStatus
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

    fun setStoreDetailImageListData(storeDetailImageListData: ArrayList<StoreDetailImageData>) {
        mStoreDetailImageListData = storeDetailImageListData
    }

    fun getStoreDetailImageListData(): ArrayList<StoreDetailImageData> {
        return mStoreDetailImageListData
    }

}