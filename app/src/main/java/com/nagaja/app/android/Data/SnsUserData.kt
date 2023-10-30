package com.nagaja.app.android.Data

import java.io.Serializable

class SnsUserData: Serializable {
    private var mUserEmail: String? = ""
    private var mUserProfileUrl: String? = ""
    private var mUserName: String? = ""
    private var mUsedId: String? = ""
    private var mLoginType: String? = ""
    private var mSocialUid: Int = 0
    private var mMemberSocialUid: Int = 0

    fun setUserEmail(userEmail: String) {
        mUserEmail = userEmail
    }

    fun getUserEmail(): String? {
        return mUserEmail
    }

    fun setUserProfileUrl(userProfileUrl: String) {
        mUserProfileUrl = userProfileUrl
    }

    fun getUserProfileUrl(): String? {
        return mUserProfileUrl
    }

    fun setUserName(userName: String) {
        mUserName = userName
    }

    fun getUserName(): String? {
        return mUserName
    }

    fun setUsedId(usedId: String) {
        mUsedId = usedId
    }

    fun getUsedId(): String? {
        return mUsedId
    }

    fun setLoginType(loginType: String) {
        mLoginType = loginType
    }

    fun getLoginType(): String? {
        return mLoginType
    }

    fun setSocialUid(socialUid: Int) {
        mSocialUid = socialUid
    }

    fun getSocialUid(): Int {
        return mSocialUid
    }

    fun setMemberSocialUid(memberSocialUid: Int) {
        mMemberSocialUid = memberSocialUid
    }

    fun getMemberSocialUid(): Int {
        return mMemberSocialUid
    }
}