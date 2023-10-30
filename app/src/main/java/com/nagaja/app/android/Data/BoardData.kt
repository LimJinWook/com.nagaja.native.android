package com.nagaja.app.android.Data

import java.io.Serializable

class BoardData : Serializable {
    private var mTotalCount: Int = 0
    private var mBoardUid: Int = 0
    private var mBoardStatus: Int = 0
    private var mBoardSubject: String = ""
    private var mBoardContent: String = ""
    private var mViewCount: Int = 0
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mMemberUid: Int = 0
    private var mMemberName: String = ""
    private var mCompanyUid: Int = 0
    private var mCompanyName: String = ""
    private var mMemberImageOrigin: String = ""
    private var mMemberImageName: String = ""
    private var mCategoryUid: Int = 0
    private var mParentCategoryUid: Int = 0
    private var mCategoryName: String = ""
    private var mParentCategoryName: String = ""
    private var mRecommendCount: Int = 0
    private var mBookMarkCount: Int = 0
    private var mCommentCount: Int = 0
    private var mIsBookMark: Boolean = false
    private var mIsRecommend: Boolean = false
    private var mIsReport: Boolean = false
    private var mIsPopularity: Boolean = false
    private var mIsStandBy: Boolean = false
    private var mLocationNationUid: Int = 0
    private var mLocationMainAreaUid: Int = 0
    private var mLocationSubAreaUid: Int = 0
    private var mLocationMainAreaName: String = ""
    private var mLocationSubAreaName: String = ""
    private var mLocationDesc: String = ""
    private var mSnsDesc: String = ""
    private var mBeginDate: String = ""
    private var mEndDate: String = ""
    private var mMissingDate: String = ""
    private var mLatitude: String = ""
    private var mLongitude: String = ""
    private var mBoardImageListData = ArrayList<UsedMarketImageData>()
    private var mBoardFileListData = ArrayList<FileData>()



    fun setTotalCount(totalCount: Int) {
        mTotalCount = totalCount
    }

    fun getTotalCount(): Int {
        return mTotalCount
    }

    fun setBoardUid(boardUid: Int) {
        mBoardUid = boardUid
    }

    fun getBoardUid(): Int {
        return mBoardUid
    }

    fun setBoardStatus(boardStatus: Int) {
        mBoardStatus = boardStatus
    }

    fun getBoardStatus(): Int {
        return mBoardStatus
    }

    fun setBoardSubject(boardSubject: String) {
        mBoardSubject = boardSubject
    }

    fun getBoardSubject(): String {
        return mBoardSubject
    }

    fun setBoardContent(boardContent: String) {
        mBoardContent = boardContent
    }

    fun getBoardContent(): String {
        return mBoardContent
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

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setCompanyName(companyName: String) {
        mCompanyName = companyName
    }

    fun getCompanyName(): String {
        return mCompanyName
    }

    fun setMemberImageOrigin(memberImageOrigin: String) {
        mMemberImageOrigin = memberImageOrigin
    }

    fun getMemberImageOrigin(): String {
        return mMemberImageOrigin
    }

    fun setMemberImageName(memberImageName: String) {
        mMemberImageName = memberImageName
    }

    fun getMemberImageName(): String {
        return mMemberImageName
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

    fun setBookMarkCount(bookMarkCount: Int) {
        mBookMarkCount = bookMarkCount
    }

    fun getBookMarkCount(): Int {
        return mBookMarkCount
    }

    fun setCommentCount(commentCount: Int) {
        mCommentCount = commentCount
    }

    fun getCommentCount(): Int {
        return mCommentCount
    }

    fun setIsBookMark(isBookMark: Boolean) {
        mIsBookMark = isBookMark
    }

    fun getIsBookMark(): Boolean {
        return mIsBookMark
    }

    fun setIsRecommend(isRecommend: Boolean) {
        mIsRecommend = isRecommend
    }

    fun getIsRecommend(): Boolean {
        return mIsRecommend
    }

    fun setIsReport(isReport: Boolean) {
        mIsReport = isReport
    }

    fun getIsReport(): Boolean {
        return mIsReport
    }

    fun setIsPopularity(isPopularity: Boolean) {
        mIsPopularity = isPopularity
    }

    fun getIsPopularity(): Boolean {
        return mIsPopularity
    }

    fun setIsStandBy(isStandBy: Boolean) {
        mIsStandBy = isStandBy
    }

    fun getIsStandBy(): Boolean {
        return mIsStandBy
    }

    fun setLocationNationUid(locationNationUid: Int) {
        mLocationNationUid = locationNationUid
    }

    fun getLocationNationUid(): Int {
        return mLocationNationUid
    }

    fun setLocationMainAreaUid(locationMainAreaUid: Int) {
        mLocationMainAreaUid = locationMainAreaUid
    }

    fun getLocationMainAreaUid(): Int {
        return mLocationMainAreaUid
    }

    fun setLocationSubAreaUid(locationSubAreaUid: Int) {
        mLocationSubAreaUid = locationSubAreaUid
    }

    fun getLocationSubAreaUid(): Int {
        return mLocationSubAreaUid
    }

    fun setLocationMainAreaName(locationMainAreaName: String) {
        mLocationMainAreaName = locationMainAreaName
    }

    fun getLocationMainAreaName(): String {
        return mLocationMainAreaName
    }

    fun setLocationSubAreaName(locationSubAreaName: String) {
        mLocationSubAreaName = locationSubAreaName
    }

    fun getLocationSubAreaName(): String {
        return mLocationSubAreaName
    }

    fun setLocationDesc(locationDesc: String) {
        mLocationDesc = locationDesc
    }

    fun getLocationDesc(): String {
        return mLocationDesc
    }

    fun setSnsDesc(snsDesc: String) {
        mSnsDesc = snsDesc
    }

    fun getSnsDesc(): String {
        return mSnsDesc
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

    fun setMissingDate(missingDate: String) {
        mMissingDate = missingDate
    }

    fun getMissingDate(): String {
        return mMissingDate
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

    fun setBoardImageListData(boardImageListData: ArrayList<UsedMarketImageData>) {
        mBoardImageListData = boardImageListData
    }

    fun getBoardImageListData(): ArrayList<UsedMarketImageData> {
        return mBoardImageListData
    }

    fun setBoardFileListData(boardFileListData: ArrayList<FileData>) {
        mBoardFileListData = boardFileListData
    }

    fun getBoardFileListData(): ArrayList<FileData> {
        return mBoardFileListData
    }

}