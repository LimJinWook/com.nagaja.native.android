package com.nagaja.app.android.Data

import java.io.Serializable

class NoticeData : Serializable {
    private var mTotalCount: Int = 0
    private var mNoticeUid: Int = 0
    private var mNoticeStatus: Int = 0
    private var mNoticeSubject: String = ""
    private var mNoticeContent: String = ""
    private var mViewCount: Int = 0
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mMemberUid: Int = 0
    private var mMemberName: String = ""
    private var mCategoryUid: Int = 0
    private var mParentCategoryUid: Int = 0
    private var mCategoryName: String = ""
    private var mParentCategoryName: String = ""
    private var mRecommendCount: Int = 0
    private var mCommentCount: Int = 0
    private var mLocationNationUid: Int = 0
    private var mMainAreaUid: Int = 0
    private var mSubAreaUid: Int = 0
    private var mMainAreaName: String = ""
    private var mSubAreaName: String = ""
    private var mNoticeImageListData = ArrayList<UsedMarketImageData>()


    fun setTotalCount(totalCount: Int) {
        mTotalCount = totalCount
    }

    fun getTotalCount(): Int {
        return mTotalCount
    }

    fun setNoticeUid(noticeUid: Int) {
        mNoticeUid = noticeUid
    }

    fun getNoticeUid(): Int {
        return mNoticeUid
    }

    fun setNoticeStatus(noticeStatus: Int) {
        mNoticeStatus = noticeStatus
    }

    fun getNoticeStatus(): Int {
        return mNoticeStatus
    }

    fun setNoticeSubject(noticeSubject: String) {
        mNoticeSubject = noticeSubject
    }

    fun getNoticeSubject(): String {
        return mNoticeSubject
    }

    fun setNoticeContent(noticeContent: String) {
        mNoticeContent = noticeContent
    }

    fun getNoticeContent(): String {
        return mNoticeContent
    }

    fun setViewCount(viewCount: Int) {
        mViewCount = viewCount
    }

    fun getViewCount(): Int {
        return mViewCount
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

    fun setMemberUid(memberUid: Int) {
        mMemberUid = memberUid
    }

    fun getMemberUid(): Int {
        return mMemberUid
    }

    fun setMemberName(memberName: String) {
        mMemberName = memberName
    }

    fun getMemberName(): String {
        return mMemberName
    }

    fun setCategoryUid(categoryUid: Int) {
        mCategoryUid = categoryUid
    }

    fun getCategoryUid(): Int {
        return mCategoryUid
    }

    fun setParentCategoryUid(parentCategoryUid: Int) {
        mParentCategoryUid = parentCategoryUid
    }

    fun getParentCategoryUid(): Int {
        return mParentCategoryUid
    }

    fun setCategoryName(categoryName: String) {
        mCategoryName = categoryName
    }

    fun getCategoryName(): String {
        return mCategoryName
    }

    fun setParentCategoryName(parentCategoryName: String) {
        mParentCategoryName = parentCategoryName
    }

    fun getParentCategoryName(): String {
        return mParentCategoryName
    }

    fun setRecommendCount(recommendCount: Int) {
        mRecommendCount = recommendCount
    }

    fun getRecommendCount(): Int {
        return mRecommendCount
    }

    fun setCommentCount(commentCount: Int) {
        mCommentCount = commentCount
    }

    fun getCommentCount(): Int {
        return mCommentCount
    }

    fun setLocationNationUid(locationNationUid: Int) {
        mLocationNationUid = locationNationUid
    }

    fun getLocationNationUid(): Int {
        return mLocationNationUid
    }

    fun setMainAreaUid(mainAreaUid: Int) {
        mMainAreaUid = mainAreaUid
    }

    fun getMainAreaUid(): Int {
        return mMainAreaUid
    }

    fun setSubAreaUid(subAreaUid: Int) {
        mSubAreaUid = subAreaUid
    }

    fun getSubAreaUid(): Int {
        return mSubAreaUid
    }

    fun setMainAreaName(mainAreaName: String) {
        mMainAreaName = mainAreaName
    }

    fun getMainAreaName(): String {
        return mMainAreaName
    }

    fun setSubAreaName(subAreaName: String) {
        mSubAreaName = subAreaName
    }

    fun getSubAreaName(): String {
        return mSubAreaName
    }

    fun setNoticeImageListData(noticeImageListData: ArrayList<UsedMarketImageData>) {
        mNoticeImageListData = noticeImageListData
    }

    fun getNoticeImageListData(): ArrayList<UsedMarketImageData> {
        return mNoticeImageListData
    }
}