package com.nagaja.app.android.Data

import java.io.Serializable

class BannerEventData : Serializable {
    private var mBannerUid: Int = 0
    private var mBannerSubject: String = ""
    private var mBannerContent: String = ""
    private var mBannerImageName: String = ""
    private var mBannerImageOrigin: String = ""
    private var mIsUseYn: Boolean = false
    private var mBannerTargetUri: String = ""
    private var mBannerGroup : Int = 0
    private var mBeginDate: String = ""
    private var mEndDate: String = ""
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mNationUid: Int = 0
    private var mCategoryUid: Int = 0
    private var mTargetUid: Int = 0

    fun setBannerUid(bannerUid: Int) {
        mBannerUid = bannerUid
    }

    fun getBannerUid(): Int {
        return mBannerUid
    }

    fun setBannerSubject(bannerSubject: String) {
        mBannerSubject = bannerSubject
    }

    fun getBannerSubject(): String {
        return mBannerSubject
    }

    fun setBannerContent(bannerContent: String) {
        mBannerContent = bannerContent
    }

    fun getBannerContent(): String {
        return mBannerContent
    }

    fun setBannerImageName(bannerImageName: String) {
        mBannerImageName = bannerImageName
    }

    fun getBannerImageName(): String {
        return mBannerImageName
    }

    fun setBannerImageOrigin(bannerImageOrigin: String) {
        mBannerImageOrigin = bannerImageOrigin
    }

    fun getBannerImageOrigin(): String {
        return mBannerImageOrigin
    }

    fun setIsUseYn(isUseYn: Boolean) {
        mIsUseYn = isUseYn
    }

    fun getIsUseYn(): Boolean {
        return mIsUseYn
    }

    fun setBannerTargetUri(bannerTargetUri: String) {
        mBannerTargetUri = bannerTargetUri
    }

    fun getBannerTargetUri(): String {
        return mBannerTargetUri
    }

    fun setBannerGroup(bannerGroup: Int) {
        mBannerGroup = bannerGroup
    }

    fun getBannerGroup(): Int {
        return mBannerGroup
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

    fun setNationUid(nationUid: Int) {
        mNationUid = nationUid
    }

    fun getNationUid(): Int {
        return mNationUid
    }

    fun setCategoryUid(categoryUid: Int) {
        mCategoryUid = categoryUid
    }

    fun getCategoryUid(): Int {
        return mCategoryUid
    }

    fun setTargetUid(targetUid: Int) {
        mTargetUid = targetUid
    }

    fun getTargetUid(): Int {
        return mTargetUid
    }

}