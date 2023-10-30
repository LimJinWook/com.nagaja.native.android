package com.nagaja.app.android.Data

import java.io.Serializable

class NativeBoardPopularityData : Serializable {
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
}