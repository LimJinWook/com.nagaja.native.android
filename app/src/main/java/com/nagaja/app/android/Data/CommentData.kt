package com.nagaja.app.android.Data

import java.io.Serializable

class CommentData : Serializable {
    private var mTotalCount: Int = 0
    private var mCommentUid: Int = 0
    private var mCommentStatus: Int = 0
    private var mComment: String = ""
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mMemberUid: Int = 0
    private var mMemberName: String = ""
    private var mMemberNickName: String = ""
    private var mNoticeUid: Int = 0
    private var mBoardUid: Int = 0
    private var mMemberImageOrigin: String = ""
    private var mMemberImageName: String = ""


    fun setTotalCount(totalCount: Int) {
        mTotalCount = totalCount
    }

    fun getTotalCount(): Int {
        return mTotalCount
    }

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

    fun setMemberNickName(memberNickName: String) {
        mMemberNickName = memberNickName
    }

    fun getMemberNickName(): String {
        return mMemberNickName
    }

    fun setNoticeUid(noticeUid: Int) {
        mNoticeUid = noticeUid
    }

    fun getNoticeUid(): Int {
        return mNoticeUid
    }

    fun setBoardUid(boardUid: Int) {
        mBoardUid = boardUid
    }

    fun getBoardUid(): Int {
        return mBoardUid
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

}