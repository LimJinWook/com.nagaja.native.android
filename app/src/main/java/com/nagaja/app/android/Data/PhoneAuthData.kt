package com.nagaja.app.android.Data

import java.io.Serializable

class PhoneAuthData : Serializable {
    private var mConfirmUid: Int = 0
    private var mMemberUid: Int = 0
    private var mMemberId: String = ""
    private var mPhoneNumber: String = ""
    private var mPhoneNationCode: String = ""
    private var mSecureCode: String = ""
    private var mConfirmLimitDate: String = ""
    private var mUserLimitDate: String = ""
    private var mConfirmTypeUid: Int = 0


    fun setConfirmUid(confirmUid: Int) {
        mConfirmUid = confirmUid
    }

    fun getConfirmUid(): Int {
        return mConfirmUid
    }

    fun setMemberUid(memberUid: Int) {
        mMemberUid = memberUid
    }

    fun getMemberUid(): Int {
        return mMemberUid
    }

    fun setMemberId(memberId: String) {
        mMemberId = memberId
    }

    fun getMemberId(): String {
        return mMemberId
    }

    fun setPhoneNumber(phoneNumber: String) {
        mPhoneNumber = phoneNumber
    }

    fun getPhoneNumber(): String {
        return mPhoneNumber
    }

    fun setPhoneNationCode(phoneNationCode: String) {
        mPhoneNationCode = phoneNationCode
    }

    fun getPhoneNationCode(): String {
        return mPhoneNationCode
    }

    fun setSecureCode(secureCode: String) {
        mSecureCode = secureCode
    }

    fun getSecureCode(): String {
        return mSecureCode
    }

    fun setConfirmLimitDate(confirmLimitDate: String) {
        mConfirmLimitDate = confirmLimitDate
    }

    fun getConfirmLimitDate(): String {
        return mConfirmLimitDate
    }

    fun setUserLimitDate(userLimitDate: String) {
        mUserLimitDate = userLimitDate
    }

    fun getUserLimitDate(): String {
        return mUserLimitDate
    }

    fun setConfirmTypeUid(confirmTypeUid: Int) {
        mConfirmTypeUid = confirmTypeUid
    }

    fun getConfirmTypeUid(): Int {
        return mConfirmTypeUid
    }

}