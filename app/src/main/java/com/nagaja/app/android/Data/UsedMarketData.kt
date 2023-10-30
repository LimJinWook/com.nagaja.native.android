package com.nagaja.app.android.Data

import java.io.Serializable

class UsedMarketData : Serializable {
    private var mTotalCount: Int = 0
    private var mItemUid: Int = 0
    private var mItemStatus: Int = 0
    private var mItemSubject: String = ""
    private var mItemName: String = ""
    private var mItemContent: String = ""
    private var mItemCurrencyCode: String = ""
    private var mItemPrice: Double = 0.0
    private var mItemViewCount: Int = 0
    private var mItemChatCount: Int = 0
    private var mItemInterestCount: Int = 0
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mCategoryUid: Int = 0
    private var mCategoryName: String = ""
    private var mCompanyUid: Int = 0
    private var mMemberUid: Int = 0
    private var mMemberEmail: String = ""
    private var mItemLocationUid: Int = 0
    private var mLatitude: String = ""
    private var mLongitude: String = ""
    private var mSiDoUid: Int = 0
    private var mSiDoName: String = ""
    private var mGuGunUid: Int = 0
    private var mGuGunName: String = ""
    private var mLocationDetail: String = ""
    private var mSellerImageOrigin: String = ""
    private var mSellerImageName: String = ""
    private var mSellerName: String = ""
    private var mIsRecommendYn: Boolean = false
    private var mImageListData = ArrayList<UsedMarketImageData>()
    private var mCurrencyListData = ArrayList<UsedMarketCurrencyData>()


    fun setTotalCount(totalCount: Int) {
        mTotalCount = totalCount
    }

    fun getTotalCount(): Int {
        return mTotalCount
    }

    fun setItemUid(itemUid: Int) {
        mItemUid = itemUid
    }

    fun getItemUid(): Int {
        return mItemUid
    }

    fun setItemStatus(itemStatus: Int) {
        mItemStatus = itemStatus
    }

    fun getItemStatus(): Int {
        return mItemStatus
    }

    fun setItemSubject(itemSubject: String) {
        mItemSubject = itemSubject
    }

    fun getItemSubject(): String {
        return mItemSubject
    }

    fun setItemName(itemName: String) {
        mItemName = itemName
    }

    fun getItemName(): String {
        return mItemName
    }

    fun setItemContent(itemContent: String) {
        mItemContent = itemContent
    }

    fun getItemContent(): String {
        return mItemContent
    }

    fun setItemCurrencyCode(itemCurrencyCode: String) {
        mItemCurrencyCode = itemCurrencyCode
    }

    fun getItemCurrencyCode(): String {
        return mItemCurrencyCode
    }

    fun setItemPrice(itemPrice: Double) {
        mItemPrice = itemPrice
    }

    fun getItemPrice(): Double {
        return mItemPrice
    }

    fun setItemViewCount(itemViewCount: Int) {
        mItemViewCount = itemViewCount
    }

    fun getItemViewCount(): Int {
        return mItemViewCount
    }

    fun setItemChatCount(itemChatCount: Int) {
        mItemChatCount = itemChatCount
    }

    fun getItemChatCount(): Int {
        return mItemChatCount
    }

    fun setItemInterestCount(itemInterestCount: Int) {
        mItemInterestCount = itemInterestCount
    }

    fun getItemInterestCount(): Int {
        return mItemInterestCount
    }

    fun setIsUseYn(isUseYn: Boolean) {
        mIsUseYn = isUseYn
    }

    fun metIsUseYn(): Boolean {
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

    fun setCategoryName(categoryName: String) {
        mCategoryName = categoryName
    }

    fun getCategoryName(): String {
        return mCategoryName
    }

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setMemberUid(memberUid: Int) {
        mMemberUid = memberUid
    }

    fun getMemberUid(): Int {
        return mMemberUid
    }

    fun setMemberEmail(memberEmail: String) {
        mMemberEmail = memberEmail
    }

    fun getMemberEmail(): String {
        return mMemberEmail
    }

    fun setItemLocationUid(itemLocationUid: Int) {
        mItemLocationUid = itemLocationUid
    }

    fun getItemLocationUid(): Int {
        return mItemLocationUid
    }

    fun setLatitude(latitude: String) {
        mLatitude = latitude
    }

    fun getLatitude(): String {
        return mLatitude
    }

    fun setLongitude(longitude: String) {
        mLongitude = longitude
    }

    fun getLongitude(): String {
        return mLongitude
    }

    fun setSiDoUid(siDoUid: Int) {
        mSiDoUid = siDoUid
    }

    fun getSiDoUid(): Int {
        return mSiDoUid
    }

    fun setSiDoName(siDoName: String) {
        mSiDoName = siDoName
    }

    fun getSiDoName(): String {
        return mSiDoName
    }

    fun setGuGunUid(guGunUid: Int) {
        mGuGunUid = guGunUid
    }

    fun getGuGunUid(): Int {
        return mGuGunUid
    }

    fun setGuGunName(guGunName: String) {
        mGuGunName = guGunName
    }

    fun getGuGunName(): String {
        return mGuGunName
    }

    fun setLocationDetail(locationDetail: String) {
        mLocationDetail = locationDetail
    }

    fun getLocationDetail(): String {
        return mLocationDetail
    }

    fun setSellerImageOrigin(sellerImageOrigin: String) {
        mSellerImageOrigin = sellerImageOrigin
    }

    fun getSellerImageOrigin(): String {
        return mSellerImageOrigin
    }

    fun setSellerImageName(sellerImageName: String) {
        mSellerImageName = sellerImageName
    }

    fun getSellerImageName(): String {
        return mSellerImageName
    }

    fun setSellerName(sellerName: String) {
        mSellerName = sellerName
    }

    fun getSellerName(): String {
        return mSellerName
    }

    fun setIsRecommendYn(isRecommendYn: Boolean) {
        mIsRecommendYn = isRecommendYn
    }

    fun getIsRecommendYn(): Boolean {
        return mIsRecommendYn
    }

    fun setImageListData(imageListData: ArrayList<UsedMarketImageData>) {
        mImageListData = imageListData
    }

    fun getImageListData(): ArrayList<UsedMarketImageData> {
        return mImageListData
    }

    fun setCurrencyListData(currencyListData: ArrayList<UsedMarketCurrencyData>) {
        mCurrencyListData = currencyListData
    }

    fun getCurrencyListData(): ArrayList<UsedMarketCurrencyData> {
        return mCurrencyListData
    }
}