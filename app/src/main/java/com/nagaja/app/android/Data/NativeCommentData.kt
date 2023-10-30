package com.nagaja.app.android.Data

import java.io.Serializable

class NativeCommentData : Serializable {
    private var mCommentUid: Int = 0
    private var mCommentStatus: Int = 0
    private var mComment: String = ""
    private var mIsUse: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mMemberUid: Int = 0
    private var mMemberName: String = ""
    private var mBoardUid: Int = 0
    private var mMemberNName: String = ""

    fun setCommentUid(commentUid: Int) {
        mCommentUid = commentUid
    }

    fun getCommentUid(): Int {
        return mCommentUid
    }

    fun setCommentStatus(commentStatus: Int) {
        mCommentStatus = commentStatus
    }

    fun getCommentStatus(): Int {
        return mCommentStatus
    }

    fun setComment(comment: String) {
        mComment = comment
    }

    fun getComment(): String {
        return mComment
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

    fun setBoardUid(boardUid: Int) {
        mBoardUid = boardUid
    }

    fun getBoardUid(): Int {
        return mBoardUid
    }

    fun setMemberNName(memberNName: String) {
        mMemberNName = memberNName
    }

    fun getMemberNName(): String {
        return mMemberNName
    }
}