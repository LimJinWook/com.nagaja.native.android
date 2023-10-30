package com.nagaja.app.android.Data

import java.io.Serializable

class PullUpCountData: Serializable {
    private var mItemUid: Int = 0
    private var mUpperCount: Int = 0
    private var mTotalPoint: Int = 0
    private var mPointAmount: Int = 0
    private var mPointBonus: Int = 0


    fun setItemUid(itemUid: Int) {
        mItemUid = itemUid
    }

    fun getItemUid(): Int {
        return mItemUid
    }

    fun setUpperCount(upperCount: Int) {
        mUpperCount = upperCount
    }

    fun getUpperCount(): Int {
        return mUpperCount
    }

    fun setTotalPoint(totalPoint: Int) {
        mTotalPoint = totalPoint
    }

    fun getTotalPoint(): Int {
        return mTotalPoint
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


}