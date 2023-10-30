package com.nagaja.app.android.Data

import java.io.Serializable

class ReservationScheduleDayData : Serializable {
    private var mScheduleDaysUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mCompanyDays: String = ""
    private var mReservationCount: Int = 0
    private var mIsRestDayYn: Boolean = false


    fun setScheduleDaysUid(scheduleDaysUid: Int) {
        mScheduleDaysUid = scheduleDaysUid
    }

    fun getScheduleDaysUid(): Int {
        return mScheduleDaysUid
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

    fun setReservationCount(reservationCount: Int) {
        mReservationCount = reservationCount
    }

    fun getReservationCount(): Int {
        return mReservationCount
    }

    fun setIsRestDayYn(isRestDayYn: Boolean) {
        mIsRestDayYn = isRestDayYn
    }

    fun getIsRestDayYn(): Boolean {
        return mIsRestDayYn
    }

}