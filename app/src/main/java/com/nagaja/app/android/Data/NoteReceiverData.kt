package com.nagaja.app.android.Data

import java.io.Serializable

class NoteReceiverData : Serializable {
    private var mNoteReceiveUid: Int = 0
    private var mNoteReceiveStatus: Int = 0
    private var mReceiveMemberUid: Int = 0
    private var mReceiveMemberName: String = ""
    private var mReceiveCompanyUid: Int = 0
    private var mReceiveCompanyName: String = ""
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mNoteUid: Int = 0
    private var mIsReceiveReport: Boolean = false
    private var mIsReceiveKeep: Boolean = false
    private var mIsReceiveRegular: Boolean = false
    private var mEmail: String = ""
    private var mImageOrigin: String = ""
    private var mImageName: String = ""


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

    fun setNoteUid(noteUid: Int) {
        mNoteUid = noteUid
    }

    fun getNoteUid(): Int {
        return mNoteUid
    }

    fun setIsReceiveReport(isReceiveReport: Boolean) {
        mIsReceiveReport = isReceiveReport
    }

    fun getIsReceiveReport(): Boolean {
        return mIsReceiveReport
    }

    fun setIsReceiveKeep(isReceiveKeep: Boolean) {
        mIsReceiveKeep = isReceiveKeep
    }

    fun getIsReceiveKeep(): Boolean {
        return mIsReceiveKeep
    }

    fun setIsReceiveRegular(isReceiveRegular: Boolean) {
        mIsReceiveRegular = isReceiveRegular
    }

    fun getIsReceiveRegular(): Boolean {
        return mIsReceiveRegular
    }

    fun setEmail(email: String) {
        mEmail = email
    }

    fun getEmail(): String {
        return mEmail
    }

    fun setImageOrigin(imageOrigin: String) {
        mImageOrigin = imageOrigin
    }

    fun getImageOrigin(): String {
        return mImageOrigin
    }

    fun setImageName(imageName: String) {
        mImageName = imageName
    }

    fun getImageName(): String {
        return mImageName
    }

}