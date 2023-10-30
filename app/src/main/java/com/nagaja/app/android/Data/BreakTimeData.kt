package com.nagaja.app.android.Data

import java.io.Serializable

class BreakTimeData : Serializable {
    private var mStartTime: String = ""
    private var mEndTime: String = ""

    fun setStartTime(startTime: String) {
        mStartTime = startTime
    }

    fun getStartTime(): String {
        return mStartTime
    }

    fun setEndTime(endTime: String) {
        mEndTime = endTime
    }

    fun getEndTime(): String {
        return mEndTime
    }
}