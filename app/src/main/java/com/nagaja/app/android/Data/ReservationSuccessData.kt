package com.nagaja.app.android.Data

import java.io.Serializable

class ReservationSuccessData : Serializable {
    private var mReservationUid: Int = 0
    private var mReservationStatus: Int = 0
    private var mReservationClass: Int = 0
    private var mReservationBeginTime: String = ""
    private var mReservationEndTime: String = ""
    private var mReservationPersonCount: Int = 0
    private var mReservationName: String = ""
    private var mReservationNationPhone: String = ""
    private var mReservationPhoneNumber: String = ""
    private var mReservationMemo: String = ""
    private var mReservationDesc: String = ""
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mMemberUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mScheduleTimeUid: Int = 0



    fun setReservationUid(reservationUid: Int) {
        mReservationUid = reservationUid
    }

    fun getReservationUid(): Int {
        return mReservationUid
    }

    fun setReservationStatus(reservationStatus: Int) {
        mReservationStatus = reservationStatus
    }

    fun getReservationStatus(): Int {
        return mReservationStatus
    }

    fun setReservationClass(reservationClass: Int) {
        mReservationClass = reservationClass
    }

    fun getReservationClass(): Int {
        return mReservationClass
    }

    fun setReservationBeginTime(reservationBeginTime: String) {
        mReservationBeginTime = reservationBeginTime
    }

    fun getReservationBeginTime(): String {
        return mReservationBeginTime
    }

    fun setReservationEndTime(reservationEndTime: String) {
        mReservationEndTime = reservationEndTime
    }

    fun getReservationEndTime(): String {
        return mReservationEndTime
    }

    fun setReservationPersonCount(reservationPersonCount: Int) {
        mReservationPersonCount = reservationPersonCount
    }

    fun getReservationPersonCount(): Int {
        return mReservationPersonCount
    }

    fun setReservationName(reservationName: String) {
        mReservationName = reservationName
    }

    fun getReservationName(): String {
        return mReservationName
    }

    fun setReservationNationPhone(reservationNationPhone: String) {
        mReservationNationPhone = reservationNationPhone
    }

    fun getReservationNationPhone(): String {
        return mReservationNationPhone
    }

    fun setReservationPhoneNumber(reservationPhoneNumber: String) {
        mReservationPhoneNumber = reservationPhoneNumber
    }

    fun getReservationPhoneNumber(): String {
        return mReservationPhoneNumber
    }

    fun setReservationMemo(reservationMemo: String) {
        mReservationMemo = reservationMemo
    }

    fun getReservationMemo(): String {
        return mReservationMemo
    }

    fun setReservationDesc(reservationDesc: String) {
        mReservationDesc = reservationDesc
    }

    fun getReservationDesc(): String {
        return mReservationDesc
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

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setScheduleTimeUid(scheduleTimeUid: Int) {
        mScheduleTimeUid = scheduleTimeUid
    }

    fun getScheduleTimeUid(): Int {
        return mScheduleTimeUid
    }

}