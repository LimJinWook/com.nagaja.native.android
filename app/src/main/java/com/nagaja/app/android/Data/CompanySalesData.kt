package com.nagaja.app.android.Data

import java.io.Serializable

class CompanySalesData : Serializable {
    private var mCompanyUid: Int = 0
    private var mCompanyServiceUid: Int = 0
    private var mServiceMonthOffDays: String = ""
    private var mServiceWeekOffDays: String = ""
    private var mServiceWeekDoubleOffDays: String = ""
    private var mServiceBeginTime: String = ""
    private var mServiceEndTime: String = ""
    private var mFirstBreakBeginTime: String = ""
    private var mFirstBreakEndTime: String = ""
    private var mSecondBreakBeginTime: String = ""
    private var mSecondBreakEndTime: String = ""
    private var mFirstRestBeginTime: String = ""
    private var mFirstRestEndTime: String = ""
    private var mSecondRestBeginTime: String = ""
    private var mSecondRestEndTime: String = ""
    private var mDeliveryBeginTime: String = ""
    private var mDeliveryEndTime: String = ""
    private var mReservationBeginTime: String = ""
    private var mReservationEndTime: String = ""
    private var mReservationDayLimit: Int = 0
    private var mReservationPersonLimit: Int = 0
    private var mReservationTeamLimit: Int = 0
    private var mIsCompanyDeliveryUseYn: Boolean = false
    private var mIsCompanyReservationUseYn: Boolean = false
    private var mIsCompanyPickupUseYn: Boolean = false
    private var mIsCompanyParkingUseYn: Boolean = false
    private var mIsCompanyPetUseYn: Boolean = false
    private var mCompanySalesCurrencyDataList = ArrayList<CompanySalesCurrencyData>()

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setCompanyServiceUid(companyServiceUid: Int) {
        mCompanyServiceUid = companyServiceUid
    }

    fun getCompanyServiceUid(): Int {
        return mCompanyServiceUid
    }

    fun setServiceMonthOffDays(serviceMonthOffDays: String) {
        mServiceMonthOffDays = serviceMonthOffDays
    }

    fun getServiceMonthOffDays(): String {
        return mServiceMonthOffDays
    }

    fun setServiceWeekOffDays(serviceWeekOffDays: String) {
        mServiceWeekOffDays = serviceWeekOffDays
    }

    fun getServiceWeekOffDays(): String {
        return mServiceWeekOffDays
    }

    fun setServiceWeekDoubleOffDays(serviceWeekDoubleOffDays: String) {
        mServiceWeekDoubleOffDays = serviceWeekDoubleOffDays
    }

    fun getServiceWeekDoubleOffDays(): String {
        return mServiceWeekDoubleOffDays
    }

    fun setServiceBeginTime(serviceBeginTime: String) {
        mServiceBeginTime = serviceBeginTime
    }

    fun getServiceBeginTime(): String {
        return mServiceBeginTime
    }

    fun setServiceEndTime(serviceEndTime: String) {
        mServiceEndTime = serviceEndTime
    }

    fun getServiceEndTime(): String {
        return mServiceEndTime
    }

    fun setFirstBreakBeginTime(firstBreakBeginTime: String) {
        mFirstBreakBeginTime = firstBreakBeginTime
    }

    fun getFirstBreakBeginTime(): String {
        return mFirstBreakBeginTime
    }

    fun setFirstBreakEndTime(firstBreakEndTime: String) {
        mFirstBreakEndTime = firstBreakEndTime
    }

    fun getFirstBreakEndTime(): String {
        return mFirstBreakEndTime
    }

    fun setSecondBreakBeginTime(secondBreakBeginTime: String) {
        mSecondBreakBeginTime = secondBreakBeginTime
    }

    fun getSecondBreakBeginTime(): String {
        return mSecondBreakBeginTime
    }

    fun setSecondBreakEndTime(secondBreakEndTime: String) {
        mSecondBreakEndTime = secondBreakEndTime
    }

    fun getSecondBreakEndTime(): String {
        return mSecondBreakEndTime
    }

    fun setFirstRestBeginTime(firstRestBeginTime: String) {
        mFirstRestBeginTime = firstRestBeginTime
    }

    fun getFirstRestBeginTime(): String {
        return mFirstRestBeginTime
    }

    fun setFirstRestEndTime(firstRestEndTime: String) {
        mFirstRestEndTime = firstRestEndTime
    }

    fun getFirstRestEndTime(): String {
        return mFirstRestEndTime
    }

    fun setSecondRestBeginTime(secondRestBeginTime: String) {
        mSecondRestBeginTime = secondRestBeginTime
    }

    fun getSecondRestBeginTime(): String {
        return mSecondRestBeginTime
    }

    fun setSecondRestEndTime(secondRestEndTime: String) {
        mSecondRestEndTime = secondRestEndTime
    }

    fun getSecondRestEndTime(): String {
        return mSecondRestEndTime
    }

    fun setDeliveryBeginTime(deliveryBeginTime: String) {
        mDeliveryBeginTime = deliveryBeginTime
    }

    fun getDeliveryBeginTime(): String {
        return mDeliveryBeginTime
    }

    fun setDeliveryEndTime(deliveryEndTime: String) {
        mDeliveryEndTime = deliveryEndTime
    }

    fun getDeliveryEndTime(): String {
        return mDeliveryEndTime
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

    fun setReservationDayLimit(reservationDayLimit: Int) {
        mReservationDayLimit = reservationDayLimit
    }

    fun getReservationDayLimit(): Int {
        return mReservationDayLimit
    }

    fun setReservationPersonLimit(reservationPersonLimit: Int) {
        mReservationPersonLimit = reservationPersonLimit
    }

    fun getReservationPersonLimit(): Int {
        return mReservationPersonLimit
    }

    fun setReservationTeamLimit(reservationTeamLimit: Int) {
        mReservationTeamLimit = reservationTeamLimit
    }

    fun getReservationTeamLimit(): Int {
        return mReservationTeamLimit
    }

    fun setIsCompanyDeliveryUseYn(isCompanyDeliveryUseYn: Boolean) {
        mIsCompanyDeliveryUseYn = isCompanyDeliveryUseYn
    }

    fun getIsCompanyDeliveryUseYn(): Boolean {
        return mIsCompanyDeliveryUseYn
    }

    fun setIsCompanyReservationUseYn(isCompanyReservationUseYn: Boolean) {
        mIsCompanyReservationUseYn = isCompanyReservationUseYn
    }

    fun getIsCompanyReservationUseYn(): Boolean {
        return mIsCompanyReservationUseYn
    }

    fun setIsCompanyPickupUseYn(isCompanyPickupUseYn: Boolean) {
        mIsCompanyPickupUseYn = isCompanyPickupUseYn
    }

    fun getIsCompanyPickupUseYn(): Boolean {
        return mIsCompanyPickupUseYn
    }

    fun setIsCompanyParkingUseYn(isCompanyParkingUseYn: Boolean) {
        mIsCompanyParkingUseYn = isCompanyParkingUseYn
    }

    fun getIsCompanyParkingUseYn(): Boolean {
        return mIsCompanyParkingUseYn
    }

    fun setIsCompanyPetUseYn(isCompanyPetUseYn: Boolean) {
        mIsCompanyPetUseYn = isCompanyPetUseYn
    }

    fun getIsCompanyPetUseYn(): Boolean {
        return mIsCompanyPetUseYn
    }

    fun setCompanySalesCurrencyDataList(companySalesCurrencyDataList: ArrayList<CompanySalesCurrencyData>) {
        mCompanySalesCurrencyDataList = companySalesCurrencyDataList
    }

    fun getCompanySalesCurrencyDataList(): ArrayList<CompanySalesCurrencyData> {
        return mCompanySalesCurrencyDataList
    }

}