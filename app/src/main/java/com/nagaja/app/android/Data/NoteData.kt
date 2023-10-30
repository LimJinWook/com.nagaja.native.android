package com.nagaja.app.android.Data

import java.io.Serializable

class NoteData: Serializable {
    private var mTotalCount: Int = 0
    private var mNoteUid: Int = 0
    private var mNoteStatus: Int = 0
    private var mNoteMessage: String = ""
    private var mSendMemberName: String = ""
    private var mSendMemberUid: Int = 0
    private var mCompanyName: String = ""
    private var mCompanyUid: Int = 0
    private var mSendCreateDate: String = ""
    private var mSendUpdateDate: String = ""
    private var mNoteReceiveUid: Int = 0
    private var mNoteReceiveStatus: Int = 0
    private var mReceiveMemberUid: Int = 0
    private var mReceiveMemberName: String = ""
    private var mReceiveCompanyUid: Int = 0
    private var mReceiveCompanyName: String = ""
    private var mReceiveCreateDate: String = ""
    private var mReceiveUpdateDate: String = ""
    private var mNoteTypeUid: Int = 0
    private var mIsCheck: Boolean = false
    private var mIsSender: Boolean = false



    fun setTotalCount(totalCount: Int) {
        mTotalCount = totalCount
    }

    fun getTotalCount(): Int {
        return mTotalCount
    }

    fun setNoteUid(noteUid: Int) {
        mNoteUid = noteUid
    }

    fun getNoteUid(): Int {
        return mNoteUid
    }

    fun setNoteStatus(noteStatus: Int) {
        mNoteStatus = noteStatus
    }

    fun getNoteStatus(): Int {
        return mNoteStatus
    }

    fun setNoteMessage(noteMessage: String) {
        mNoteMessage = noteMessage
    }

    fun getNoteMessage(): String {
        return mNoteMessage
    }

    fun setSendMemberName(sendMemberName: String) {
        mSendMemberName = sendMemberName
    }

    fun getSendMemberName(): String {
        return mSendMemberName
    }

    fun setSendMemberUid(sendMemberUid: Int) {
        mSendMemberUid = sendMemberUid
    }

    fun getSendMemberUid(): Int {
        return mSendMemberUid
    }

    fun setCompanyName(companyName: String) {
        mCompanyName = companyName
    }

    fun getCompanyName(): String {
        return mCompanyName
    }

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setSendCreateDate(sendCreateDate: String) {
        mSendCreateDate = sendCreateDate
    }

    fun getSendCreateDate(): String {
        return mSendCreateDate
    }

    fun setSendUpdateDate(sendUpdateDate: String) {
        mSendUpdateDate = sendUpdateDate
    }

    fun getSendUpdateDate(): String {
        return mSendUpdateDate
    }

    fun setNoteReceiveUid(noteReceiveUid: Int) {
        mNoteReceiveUid = noteReceiveUid
    }

    fun getNoteReceiveUid(): Int {
        return mNoteReceiveUid
    }

    fun setNoteReceiveStatus(noteReceiveStatus: Int) {
        mNoteReceiveStatus = noteReceiveStatus
    }

    fun getNoteReceiveStatus(): Int {
        return mNoteReceiveStatus
    }

    fun setReceiveMemberUid(receiveMemberUid: Int) {
        mReceiveMemberUid = receiveMemberUid
    }

    fun getReceiveMemberUid(): Int {
        return mReceiveMemberUid
    }

    fun setReceiveMemberName(receiveMemberName: String) {
        mReceiveMemberName = receiveMemberName
    }

    fun getReceiveMemberName(): String {
        return mReceiveMemberName
    }

    fun setReceiveCompanyUid(receiveCompanyUid: Int) {
        mReceiveCompanyUid = receiveCompanyUid
    }

    fun getReceiveCompanyUid(): Int {
        return mReceiveCompanyUid
    }

    fun setReceiveCompanyName(receiveCompanyName: String) {
        mReceiveCompanyName = receiveCompanyName
    }

    fun getReceiveCompanyName(): String {
        return mReceiveCompanyName
    }

    fun setReceiveCreateDate(receiveCreateDate: String) {
        mReceiveCreateDate = receiveCreateDate
    }

    fun getReceiveCreateDate(): String {
        return mReceiveCreateDate
    }

    fun setReceiveUpdateDate(receiveUpdateDate: String) {
        mReceiveUpdateDate = receiveUpdateDate
    }

    fun getReceiveUpdateDate(): String {
        return mReceiveUpdateDate
    }

    fun setNoteTypeUid(noteTypeUid: Int) {
        mNoteTypeUid = noteTypeUid
    }

    fun getNoteTypeUid(): Int {
        return mNoteTypeUid
    }

    fun setIsCheck(isCheck: Boolean) {
        mIsCheck = isCheck
    }

    fun getIsCheck(): Boolean {
        return mIsCheck
    }

    fun setIsSender(isSender: Boolean) {
        mIsSender = isSender
    }

    fun getIsSender(): Boolean {
        return mIsSender
    }

}