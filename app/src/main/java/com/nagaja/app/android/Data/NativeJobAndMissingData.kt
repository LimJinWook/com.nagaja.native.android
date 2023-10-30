package com.nagaja.app.android.Data

import java.io.Serializable

class NativeJobAndMissingData : Serializable {
    private var mBoardUid: Int = 0
    private var mBoardStatus: Int = 0
    private var mBoardSubject: String = ""
    private var mBoardContent: String = ""
    private var mViewCount: Int = 0
    private var mIsUse: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mMemberUid: Int = 0
    private var mMemberName: String = ""
    private var mCategoryUid: Int = 0
    private var mParentCategoryUid: Int = 0
    private var mCategoryName: String = ""
    private var mParentCategoryName: String = ""
    private var mRecommendCount: Int = 0
    private var mBookMarkCount: Int = 0
    private var mCommentCount: Int = 0
    private var mIsBookMark: Boolean = false
    private var mIsRecommend: Boolean = false
    private var mIsPopularity: Boolean = false
    private var mIsStandBy: Boolean = false
    private var mBoardImageList: ArrayList<NativeBoardImageData> = ArrayList()
    private var mBoardFileList: ArrayList<NativeBoardFileData> = ArrayList()

    // Job And Missing
    private var mLocationUid: Int = 0               // 나라 고유번호
    private var mLocationStateUid: Int = 0          // 시도 고유번호
    private var mLocationState: String = ""         // 시도 명
    private var mLocationGuGunUid: Int = 0          // 구군 고유번호
    private var mLocationGuGun: String = ""         // 구군 명
    private var mLocationDesc: String = ""          // 지역 설명

    // Job
    private var mSnsDesc: String = ""               // SNS
    private var mBeginDate: String = ""             // 구인구직 시작인
    private var mEndDate: String = ""               // 구인구직 종료일

    // Missing
    private var mMissingDate: String = ""           // 실종 날짜


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

    fun setIsUse(isUse: Boolean) {
        mIsUse = isUse
    }

    fun getIsUse(): Boolean {
        return mIsUse
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

    fun setMemberName(memberNName: String) {
        mMemberName = memberNName
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

    fun setBoardImageList(boardImageList: ArrayList<NativeBoardImageData>) {
        mBoardImageList = boardImageList
    }

    fun getBoardImageList(): ArrayList<NativeBoardImageData> {
        return mBoardImageList
    }

    fun setBoardFileList(boardFileList: ArrayList<NativeBoardFileData>) {
        mBoardFileList = boardFileList
    }

    fun getBoardFileList(): ArrayList<NativeBoardFileData> {
        return mBoardFileList
    }

    /**
     * Jon And Missing
     * */
    fun setLocationUid(locationUid: Int) {
        mLocationUid = locationUid
    }

    fun getLocationUid(): Int {
        return mLocationUid
    }

    fun setLocationStateUid(locationStateUid: Int) {
        mLocationStateUid = locationStateUid
    }

    fun getLocationStateUid(): Int {
        return mLocationStateUid
    }

    fun setLocationState(locationState: String) {
        mLocationState = locationState
    }

    fun getLocationState(): String {
        return mLocationState
    }

    fun setLocationGuGunUid(locationGuGunUid: Int) {
        mLocationGuGunUid = locationGuGunUid
    }

    fun getLocationGuGunUid(): Int {
        return mLocationGuGunUid
    }

    fun setLocationGuGun(locationGuGun: String) {
        mLocationGuGun = locationGuGun
    }

    fun getLocationGuGun(): String {
        return mLocationGuGun
    }

    fun setLocationDesc(locationDesc: String) {
        mLocationDesc = locationDesc
    }

    fun getLocationDesc(): String {
        return mLocationDesc
    }

    /**
     * Job
     * */
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

    /**
     * Missing
     * */
    fun setMissingDate(missingDate: String) {
        mMissingDate = missingDate
    }

    fun getMissingDate(): String {
        return mMissingDate
    }
}