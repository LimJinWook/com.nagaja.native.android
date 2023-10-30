package com.nagaja.app.android.Data

import java.io.Serializable

class StoreDetailReviewData : Serializable {
    private var mReviewUid: Int = 0
    private var mReviewStatus: Int = 0
    private var mReviewSubject: String = ""
    private var mReviewContent: String = ""
    private var mCount: Int = 0
    private var mReviewViewCount: Int = 0
    private var mReviewViewPoint: Int = 0
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mCompanyUid: Int = 0
    private var mMemberUid: Int = 0
    private var mMemberName: String = ""
    private var mMemberEmail: String = ""
    private var mProfileImageUrl: String = ""
    private var mIsRecommendYn: Boolean = false
    private var mIsReportYn: Boolean = false
    private var mCompanyName: String = ""
    private var mReviewImageListData = ArrayList<StoreDetailReviewImageData>()



    fun setReviewUid(reviewUid: Int) {
        mReviewUid = reviewUid
    }

    fun getReviewUid(): Int {
        return mReviewUid
    }

    fun setReviewStatus(reviewStatus: Int) {
        mReviewStatus = reviewStatus
    }

    fun getReviewStatus(): Int {
        return mReviewStatus
    }

    fun setReviewSubject(reviewSubject: String) {
        mReviewSubject = reviewSubject
    }

    fun getReviewSubject(): String {
        return mReviewSubject
    }

    fun setReviewContent(reviewContent: String) {
        mReviewContent = reviewContent
    }

    fun getReviewContent(): String {
        return mReviewContent
    }

    fun setCount(count: Int) {
        mCount = count
    }

    fun getCount(): Int {
        return mCount
    }

    fun setReviewViewCount(reviewViewCount: Int) {
        mReviewViewCount = reviewViewCount
    }

    fun getReviewViewCount(): Int {
        return mReviewViewCount
    }

    fun setReviewViewPoint(reviewViewPoint: Int) {
        mReviewViewPoint = reviewViewPoint
    }

    fun getReviewViewPoint(): Int {
        return mReviewViewPoint
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

    fun setMemberName(memberName: String) {
        mMemberName = memberName
    }

    fun getMemberName(): String {
        return mMemberName
    }

    fun setMemberEmail(memberEmail: String) {
        mMemberEmail = memberEmail
    }

    fun getMemberEmail(): String {
        return mMemberEmail
    }

    fun setProfileImageUrl(profileImageUrl: String) {
        mProfileImageUrl = profileImageUrl
    }

    fun getProfileImageUrl(): String {
        return mProfileImageUrl
    }

    fun setIsRecommendYn(isRecommendYn: Boolean) {
        mIsRecommendYn = isRecommendYn
    }

    fun getIsRecommendYn(): Boolean {
        return mIsRecommendYn
    }

    fun setIsReportYn(isReportYn: Boolean) {
        mIsReportYn = isReportYn
    }

    fun getIsReportYn(): Boolean {
        return mIsReportYn
    }

    fun setCompanyName(companyName: String) {
        mCompanyName = companyName
    }

    fun getCompanyName(): String {
        return mCompanyName
    }

    fun setReviewImageListData(reviewImageListData: ArrayList<StoreDetailReviewImageData>) {
        mReviewImageListData = reviewImageListData
    }

    fun getReviewImageListData(): ArrayList<StoreDetailReviewImageData> {
        return mReviewImageListData
    }

}