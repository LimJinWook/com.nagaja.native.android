package com.nagaja.app.android.Data

import java.io.Serializable

class SearchData : Serializable {
    private var mTotalCount: Int = 0
    private var mUid: Int = 0
    private var mRootCategoryUid: Int = 0
    private var mRootCategoryName: String = ""
    private var mContent: String = ""
    private var mImageName: String = ""
    private var mImageOrigin: String = ""
    private var mReviewPointAverage: Double = 0.0
    private var mRecommendCount: Int = 0
    private var mServiceBeginTime: String = ""
    private var mServiceEndTime: String = ""
    private var mItemStatus: Int = 0
    private var mCreateDate: String = ""
    private var mCurrencyListData = ArrayList<SearchCurrencyData>()


    fun setTotalCount(totalCount: Int) {
        mTotalCount = totalCount
    }

    fun getTotalCount(): Int {
        return mTotalCount
    }

    fun setUid(uid: Int) {
        mUid = uid
    }

    fun getUid(): Int {
        return mUid
    }

    fun setRootCategoryUid(rootCategoryUid: Int) {
        mRootCategoryUid = rootCategoryUid
    }

    fun getRootCategoryUid(): Int {
        return mRootCategoryUid
    }

    fun setRootCategoryName(rootCategoryName: String) {
        mRootCategoryName = rootCategoryName
    }

    fun getRootCategoryName(): String {
        return mRootCategoryName
    }

    fun setContent(content: String) {
        mContent = content
    }

    fun getContent(): String {
        return mContent
    }

    fun setImageName(imageName: String) {
        mImageName = imageName
    }

    fun getImageName(): String {
        return mImageName
    }

    fun setImageOrigin(imageOrigin: String) {
        mImageOrigin = imageOrigin
    }

    fun getImageOrigin(): String {
        return mImageOrigin
    }

    fun setReviewPointAverage(reviewPointAverage: Double) {
        mReviewPointAverage = reviewPointAverage
    }

    fun getReviewPointAverage(): Double {
        return mReviewPointAverage
    }

    fun setRecommendCount(recommendCount: Int) {
        mRecommendCount = recommendCount
    }

    fun getRecommendCount(): Int {
        return mRecommendCount
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

    fun setItemStatus(itemStatus: Int) {
        mItemStatus = itemStatus
    }

    fun getItemStatus(): Int {
        return mItemStatus
    }

    fun setCreateDate(createDate: String) {
        mCreateDate = createDate
    }

    fun getCreateDate(): String {
        return mCreateDate
    }

    fun setCurrencyListData(currencyListData: ArrayList<SearchCurrencyData>) {
        mCurrencyListData = currencyListData
    }

    fun getCurrencyListData(): ArrayList<SearchCurrencyData> {
        return mCurrencyListData
    }


}