package com.nagaja.app.android.Data

import java.io.Serializable

class PointData: Serializable {
    private var mPointUid: Int = 0
    private var mMemberUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mPointAmount: Int = 0
    private var mPointBonus: Int = 0
    private var mTotalPoint: Int = 0
    private var mCreateDate: String = ""


    fun setPointUid(pointUid: Int) {
        mPointUid = pointUid
    }

    fun getPointUid(): Int {
        return mPointUid
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

    fun setPointAmount(pointAmount: Int) {
        mPointAmount = pointAmount
    }

    fun getPointAmount(): Int {
        return mPointAmount
    }

    fun setPointBonus(pointBonus: Int) {
        mPointBonus = pointBonus
    }

    fun getPointBonus(): Int {
        return mPointBonus
    }

    fun setTotalPoint(totalPoint: Int) {
        mTotalPoint = totalPoint
    }

    fun getTotalPoint(): Int {
        return mTotalPoint
    }

    fun setCreateDate(createDate: String) {
        mCreateDate = createDate
    }

    fun getCreateDate(): String {
        return mCreateDate
    }

}