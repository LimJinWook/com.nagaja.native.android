package com.nagaja.app.android.Data

import java.io.Serializable

class ModifyProfileData: Serializable {
    private var mMemberNickName: String = ""
    private var mProfileImageName: String = ""
    private var mProfileImageOrigin: String = ""


    fun setMemberNickName(memberNickName: String) {
        mMemberNickName = memberNickName
    }

    fun getMemberNickName(): String {
        return mMemberNickName
    }

    fun setProfileImageName(profileImageName: String) {
        mProfileImageName = profileImageName
    }

    fun getProfileImageName(): String {
        return mProfileImageName
    }

    fun setProfileImageOrigin(profileImageOrigin: String) {
        mProfileImageOrigin = profileImageOrigin
    }

    fun getProfileImageOrigin(): String {
        return mProfileImageOrigin
    }

}