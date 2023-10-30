package com.nagaja.app.android.Data

import java.io.Serializable

class NoteBoxData : Serializable {
    private var mIsNotice: Boolean = false
    private var mNoticeMessage: String = ""
    private var mNoteType: Int = 0          // 1 Send, 0 Receive
    private var mProfileUrl: String = ""
    private var mSenderReceiverName: String = ""
    private var mSenderReceiverEmail: String = ""
    private var mTimestamp: String = ""
    private var mMessage: String = ""
    private var mAddFileUrl: String = ""
    private var mAddFileName: String = ""
    private var mExtension: String = ""
    private var mImageList: ArrayList<String> = ArrayList()
    private var mIsChecked: Boolean = false

    fun setIsNotice(isNotice: Boolean) {
        mIsNotice = isNotice
    }

    fun getIsNotice(): Boolean {
        return mIsNotice
    }

    fun setNoticeMessage(noticeMessage: String) {
        mNoticeMessage = noticeMessage
    }

    fun getNoticeMessage(): String {
        return mNoticeMessage
    }

    fun setNoteType(noteType: Int) {
        mNoteType = noteType
    }

    fun getNoteType(): Int {
        return mNoteType
    }

    fun setProfileUrl(profileUrl: String) {
        mProfileUrl = profileUrl
    }

    fun getProfileUrl(): String {
        return mProfileUrl
    }

    fun setSenderReceiverName(senderReceiverName: String) {
        mSenderReceiverName = senderReceiverName
    }

    fun getSenderReceiverName(): String {
        return mSenderReceiverName
    }

    fun setSenderReceiverEmail(senderReceiverEmail: String) {
        mSenderReceiverEmail = senderReceiverEmail
    }

    fun getSenderReceiverEmail(): String {
        return mSenderReceiverEmail
    }

    fun setTimestamp(timeStamp: String) {
        mTimestamp = timeStamp
    }

    fun getTimestamp(): String {
        return mTimestamp
    }

    fun setMessage(message: String) {
        mMessage = message
    }

    fun getMessage(): String {
        return mMessage
    }

    fun setAddFileUrl(addFileUrl: String) {
        mAddFileUrl = addFileUrl
    }

    fun getAddFileUrl(): String {
        return mAddFileUrl
    }

    fun setAddFileName(addFileName: String) {
        mAddFileName = addFileName
    }

    fun getAddFileName(): String {
        return mAddFileName
    }

    fun setExtension(extension: String) {
        mExtension = extension
    }

    fun getExtension(): String {
        return mExtension
    }

    fun setImageList(imageList: ArrayList<String>) {
        mImageList = imageList
    }

    fun getImageList(): ArrayList<String> {
        return mImageList
    }

    fun setIsChecked(isChecked: Boolean) {
        mIsChecked = isChecked
    }

    fun getIsChecked(): Boolean {
        return mIsChecked
    }
}