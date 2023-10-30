package com.nagaja.app.android.Data

import java.io.Serializable

class CompanyMemberData : Serializable {
    private var mCompanyManagerUid: Int = 0
    private var mCompanyManagerGrant: Int = 0       // 1: 최고관리자(마스터), 2: 매니저
    private var mMemberUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mMemberId: Int = 0
    private var mCompanyName: String = ""
    private var mCompanyNameEnglish: String = ""
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mChatCount: Int = 0
    private var mNoteCount: Int = 0
    private var mReservationCount: Int = 0
    private var mRegularCount: Int = 0
    private var mCompanyStatus: Int = 0

    fun setCompanyManagerUid(companyManagerUid: Int) {
        mCompanyManagerUid = companyManagerUid
    }

    fun getCompanyManagerUid(): Int {
        return mCompanyManagerUid
    }

    fun setCompanyManagerGrant(companyManagerGrant: Int) {
        mCompanyManagerGrant = companyManagerGrant
    }

    fun getCompanyManagerGrant(): Int {
        return mCompanyManagerGrant
    }

    fun setMemberUid(memberUid: Int) {
        mMemberUid = memberUid
    }

    fun getMemberUid(): Int {
        return mMemberUid
    }

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setMemberId(memberId: Int) {
        mMemberId = memberId
    }

    fun getMemberId(): Int {
        return mMemberId
    }

    fun setCompanyName(companyName: String) {
        mCompanyName = companyName
    }

    fun getCompanyName(): String {
        return mCompanyName
    }

    fun setCompanyNameEnglish(companyNameEnglish: String) {
        mCompanyNameEnglish = companyNameEnglish
    }

    fun getCompanyNameEnglish(): String {
        return mCompanyNameEnglish
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

    fun setChatCount(chatCount: Int) {
        mChatCount = chatCount
    }

    fun getChatCount(): Int {
        return mChatCount
    }

    fun setNoteCount(noteCount: Int) {
        mNoteCount = noteCount
    }

    fun getNoteCount(): Int {
        return mNoteCount
    }

    fun setReservationCount(reservationCount: Int) {
        mReservationCount = reservationCount
    }

    fun getReservationCount(): Int {
        return mReservationCount
    }

    fun setRegularCount(regularCount: Int) {
        mRegularCount = regularCount
    }

    fun getRegularCount(): Int {
        return mRegularCount
    }

    fun setCompanyStatus(companyStatus: Int) {
        mCompanyStatus = companyStatus
    }

    fun getCompanyStatus(): Int {
        return mCompanyStatus
    }
}