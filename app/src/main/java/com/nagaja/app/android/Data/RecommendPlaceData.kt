package com.nagaja.app.android.Data

import java.io.Serializable

class RecommendPlaceData : Serializable {
    private var mSuggestUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mMessage: String = ""
    private var mMessageNote: String = ""
    private var mBeginDate: String = ""
    private var mEndDate: String = ""
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mCategoryUid: Int = 0
    private var mCompanyName: String = ""
    private var mCompanyNameEnglish: String = ""
    private var mIsDeliveryAvailable: Boolean = false
    private var mIsReservationAvailable: Boolean = false
    private var mIsPickUpAvailable: Boolean = false
    private var mIsParkingAvailable: Boolean = false
    private var mIsPetAvailable: Boolean = false
    private var mCompanyAddress: String = ""
    private var mRecommendPlaceImageList = ArrayList<RecommendPlaceImageData>()



    fun setSuggestUid(suggestUid: Int) {
        mSuggestUid = suggestUid
    }

    fun getSuggestUid(): Int {
        return mSuggestUid
    }

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setMessage(message: String) {
        mMessage = message
    }

    fun getMessage(): String {
        return mMessage
    }

    fun setMessageNote(messageNote: String) {
        mMessageNote = messageNote
    }

    fun getMessageNote(): String {
        return mMessageNote
    }

    fun setBeginDate(beginDate: String) {
        mBeginDate = beginDate
    }

    fun getBeginDate(): String {
        return mBeginDate
    }

    fun setEndDate(endDate: String) {
        mEndDate = endDate
    }

    fun getEndDate(): String {
        return mEndDate
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

    fun setCategoryUid(categoryUid: Int) {
        mCategoryUid = categoryUid
    }

    fun getCategoryUid(): Int {
        return mCategoryUid
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

    fun setIsDeliveryAvailable(isDeliveryAvailable: Boolean) {
        mIsDeliveryAvailable = isDeliveryAvailable
    }

    fun getIsDeliveryAvailable(): Boolean {
        return mIsDeliveryAvailable
    }

    fun setIsReservationAvailable(isReservationAvailable: Boolean) {
        mIsReservationAvailable = isReservationAvailable
    }

    fun getIsReservationAvailable(): Boolean {
        return mIsReservationAvailable
    }

    fun setIsPickUpAvailable(isPickUpAvailable: Boolean) {
        mIsPickUpAvailable = isPickUpAvailable
    }

    fun getIsPickUpAvailable(): Boolean {
        return mIsPickUpAvailable
    }

    fun setIsParkingAvailable(isParkingAvailable: Boolean) {
        mIsParkingAvailable = isParkingAvailable
    }

    fun getIsParkingAvailable(): Boolean {
        return mIsParkingAvailable
    }

    fun setIsPetAvailable(isPetAvailable: Boolean) {
        mIsPetAvailable = isPetAvailable
    }

    fun getIsPetAvailable(): Boolean {
        return mIsPetAvailable
    }

    fun setCompanyAddress(companyAddress: String) {
        mCompanyAddress = companyAddress
    }

    fun getCompanyAddress(): String {
        return mCompanyAddress
    }

    fun setRecommendPlaceImageList(recommendPlaceImageList: ArrayList<RecommendPlaceImageData>) {
        mRecommendPlaceImageList = recommendPlaceImageList
    }

    fun getRecommendPlaceImageList(): ArrayList<RecommendPlaceImageData> {
        return mRecommendPlaceImageList
    }
}