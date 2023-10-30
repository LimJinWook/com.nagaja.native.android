package com.nagaja.app.android.Data

import java.io.Serializable

class NewsData : Serializable {
    private var mNewsImageUrl: String = ""
    private var mTitle: String = ""
    private var mDate: String = ""
    private var mViewCount: Int = 0
    private var mContent: String = ""
    private var mCommentListData = ArrayList<CommentData>()

    fun setNewsImageUrl(newsImageUrl: String) {
        mNewsImageUrl = newsImageUrl
    }

    fun getNewsImageUrl(): String {
        return mNewsImageUrl
    }

    fun setTitle(title: String) {
        mTitle = title
    }

    fun getTitle(): String {
        return mTitle
    }

    fun setDate(date: String) {
        mDate = date
    }

    fun getDate(): String {
        return mDate
    }

    fun setViewCount(viewCount: Int) {
        mViewCount = viewCount
    }

    fun getViewCount(): Int {
        return mViewCount
    }

    fun setContent(content: String) {
        mContent = content
    }

    fun getContent(): String {
        return mContent
    }

    fun setCommentListData(commentListData: ArrayList<CommentData>) {
        mCommentListData = commentListData
    }

    fun getCommentListData(): ArrayList<CommentData> {
        return mCommentListData
    }
}