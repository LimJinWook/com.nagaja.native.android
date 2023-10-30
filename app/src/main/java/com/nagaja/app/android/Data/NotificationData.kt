package com.nagaja.app.android.Data

import java.io.Serializable

class NotificationData : Serializable {
    private var mPushUid: Int = 0
    private var mPushTypeUid: Int = 0
    private var mPushClassUid: Int = 0
    private var mPushStatus: Int = 0
    private var mPushSubject: String = ""
    private var mPushMemo: String = ""
    private var mMemberUid: Int = 0
    private var mFirstTargetUid: Int = 0
    private var mSecondTargetUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mPushSendDate: String = ""
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mTotalCount: Int = 0



    fun setPushUid(pushUid: Int) {
        mPushUid = pushUid
    }

    fun getPushUid(): Int {
        return mPushUid
    }

    fun setPushTypeUid(pushTypeUid: Int) {
        mPushTypeUid = pushTypeUid
    }

    fun getPushTypeUid(): Int {
        return mPushTypeUid
    }

    fun setPushClassUid(pushClassUid: Int) {
        mPushClassUid = pushClassUid
    }

    fun getPushClassUid(): Int {
        return mPushClassUid
    }

    fun setPushStatus(pushStatus: Int) {
        mPushStatus = pushStatus
    }

    fun getPushStatus(): Int {
        return mPushStatus
    }

    fun setPushSubject(pushSubject: String) {
        mPushSubject = pushSubject
    }

    fun getPushSubject(): String {
        return mPushSubject
    }

    fun setPushMemo(pushMemo: String) {
        mPushMemo = pushMemo
    }

    fun getPushMemo(): String {
        return mPushMemo
    }

    fun setMemberUid(memberUid: Int) {
        mMemberUid = memberUid
    }

    fun getMemberUid(): Int {
        return mMemberUid
    }

    fun setFirstTargetUid(firstTargetUid: Int) {
        mFirstTargetUid = firstTargetUid
    }

    fun getFirstTargetUid(): Int {
        return mFirstTargetUid
    }

    fun setSecondTargetUid(secondTargetUid: Int) {
        mSecondTargetUid = secondTargetUid
    }

    fun getSecondTargetUid(): Int {
        return mSecondTargetUid
    }

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setPushSendDate(pushSendDate: String) {
        mPushSendDate = pushSendDate
    }

    fun getPushSendDate(): String {
        return mPushSendDate
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

    fun setTotalCount(totalCount: Int) {
        mTotalCount = totalCount
    }

    fun getTotalCount(): Int {
        return mTotalCount
    }

}