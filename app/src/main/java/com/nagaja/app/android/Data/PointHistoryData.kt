package com.nagaja.app.android.Data

import java.io.Serializable

class PointHistoryData: Serializable {
    private var mPointLogUid: Int = 0
    private var mPointUid: Int = 0
    private var mPointAmount: Int = 0
    private var mPointBonus: Int = 0
    private var mPointStatus: Int = 0
    private var mPointType: Int = 0
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mPointDesc: String = ""


    fun setPointLogUid(pointLogUid: Int) {
        mPointLogUid = pointLogUid
    }

    fun getPointLogUid(): Int {
        return mPointLogUid
    }

    fun setPointUid(pointUid: Int) {
        mPointUid = pointUid
    }

    fun getPointUid(): Int {
        return mPointUid
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

    fun setPointStatus(pointStatus: Int) {
        mPointStatus = pointStatus
    }

    fun getPointStatus(): Int {
        return mPointStatus
    }

    fun setPointType(pointType: Int) {
        mPointType = pointType
    }

    fun getPointType(): Int {
        return mPointType
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

    fun setPointDesc(pointDesc: String) {
        mPointDesc = pointDesc
    }

    fun getPointDesc(): String {
        return mPointDesc
    }

}