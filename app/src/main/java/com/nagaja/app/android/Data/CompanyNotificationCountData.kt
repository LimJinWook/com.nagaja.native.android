package com.nagaja.app.android.Data

import java.io.Serializable

class CompanyNotificationCountData : Serializable {
    private var mCompanyUid: Int = 0
    private var mNoteCount: Int = 0
    private var mRegularCount: Int = 0
    private var mReservationCount: Int = 0
    private var mDeclareCount: Int = 0

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setNoteCount(noteCount: Int) {
        mNoteCount = noteCount
    }

    fun getNoteCount(): Int {
        return mNoteCount
    }

    fun setRegularCount(regularCount: Int) {
        mRegularCount = regularCount
    }

    fun getRegularCount(): Int {
        return mRegularCount
    }

    fun setReservationCount(reservationCount: Int) {
        mReservationCount = reservationCount
    }

    fun getReservationCount(): Int {
        return mReservationCount
    }

    fun setDeclareCount(declareCount: Int) {
        mDeclareCount = declareCount
    }

    fun getDeclareCount(): Int {
        return mDeclareCount
    }
}