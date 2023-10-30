package com.nagaja.app.android.Data

import java.io.Serializable

class ReservationScheduleTimeData : Serializable {
    private var mScheduleTimeUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mCompanyDays: String = ""
    private var mBeginTime: String = ""
    private var mEndTime: String = ""
    private var mReservationTimeCount: Int = 0
    private var mReservationTimeLimitCount: Int = 0
    private var mReservationTimeTeamCount: Int = 0
    private var mReservationTimeTeamLimitCount: Int = 0
    private var mIsRestTimeYn: Boolean = false
    private var mScheduleDaysUid: Int = 0
    private var mIsSelect: Boolean = false


    fun setScheduleTimeUid(scheduleTimeUid: Int) {
        mScheduleTimeUid = scheduleTimeUid
    }

    fun getScheduleTimeUid(): Int {
        return mScheduleTimeUid
    }

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setCompanyDays(companyDays: String) {
        mCompanyDays = companyDays
    }

    fun getCompanyDays(): String {
        return mCompanyDays
    }

    fun setBeginTime(beginTime: String) {
        mBeginTime = beginTime
    }

    fun getBeginTime(): String {
        return mBeginTime
    }

    fun setEndTime(endTime: String) {
        mEndTime = endTime
    }

    fun getEndTime(): String {
        return mEndTime
    }

    fun setReservationTimeCount(reservationTimeCount: Int) {
        mReservationTimeCount = reservationTimeCount
    }

    fun getReservationTimeCount(): Int {
        return mReservationTimeCount
    }

    fun setReservationTimeLimitCount(reservationTimeLimitCount: Int) {
        mReservationTimeLimitCount = reservationTimeLimitCount
    }

    fun getReservationTimeLimitCount(): Int {
        return mReservationTimeLimitCount
    }

    fun setReservationTimeTeamCount(reservationTimeTeamCount: Int) {
        mReservationTimeTeamCount = reservationTimeTeamCount
    }

    fun getReservationTimeTeamCount(): Int {
        return mReservationTimeTeamCount
    }

    fun setReservationTimeTeamLimitCount(reservationTimeTeamLimitCount: Int) {
        mReservationTimeTeamLimitCount = reservationTimeTeamLimitCount
    }

    fun getReservationTimeTeamLimitCount(): Int {
        return mReservationTimeTeamLimitCount
    }

    fun setIsRestTimeYn(isRestTimeYn: Boolean) {
        mIsRestTimeYn = isRestTimeYn
    }

    fun getIsRestTimeYn(): Boolean {
        return mIsRestTimeYn
    }

    fun setScheduleDaysUid(scheduleDaysUid: Int) {
        mScheduleDaysUid = scheduleDaysUid
    }

    fun getScheduleDaysUid(): Int {
        return mScheduleDaysUid
    }

    fun setIsSelect(isSelect: Boolean) {
        mIsSelect = isSelect
    }

    fun getIsSelect(): Boolean {
        return mIsSelect
    }
}