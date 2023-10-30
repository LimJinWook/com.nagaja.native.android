package com.nagaja.app.android.Data

import java.io.Serializable

class StoreReviewData : Serializable {
    private var mProfileImageUrl: String = ""
    private var mName: String = ""
    private var mID: String = ""
    private var mStarRating: Int = 0
    private var mTimestamp: String = ""
    private var mComment: String = ""
    private var mUploadImageArrayList = ArrayList<String>()
    private var mIsMyWrite: Boolean = false

    fun setProfileImageUrl(profileImageUrl: String) {
        mProfileImageUrl = profileImageUrl
    }

    fun getProfileImageUrl(): String {
        return mProfileImageUrl
    }

    fun setName(name: String) {
        mName = name
    }

    fun getName(): String {
        return mName
    }

    fun setID(id: String) {
        mID = id
    }

    fun getID(): String {
        return mID
    }

    fun setStarRating(starRating: Int) {
        mStarRating = starRating
    }

    fun getStarRating(): Int {
        return mStarRating
    }

    fun setTimestamp(timestamp: String) {
        mTimestamp = timestamp
    }

    fun getTimestamp(): String {
        return mTimestamp
    }

    fun setComment(comment: String) {
        mComment = comment
    }

    fun getComment(): String {
        return mComment
    }

    fun setUploadImageArrayList(uploadImageArrayList: ArrayList<String>) {
        mUploadImageArrayList = uploadImageArrayList
    }

    fun getUploadImageArrayList(): ArrayList<String> {
        return mUploadImageArrayList
    }

    fun setIsMyWrite(isMyWrite: Boolean) {
        mIsMyWrite = isMyWrite
    }

    fun getIsMyWrite(): Boolean {
        return mIsMyWrite
    }
}