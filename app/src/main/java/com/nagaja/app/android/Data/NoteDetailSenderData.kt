package com.nagaja.app.android.Data

import java.io.Serializable

class NoteDetailSenderData : Serializable {
    private var mNoteUid: Int = 0
    private var mNoteStatus: Int = 0
    private var mMemberNickName: String = ""
    private var mMemberUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mCompanyName: String = ""
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mEmail: String = ""
    private var mImageOrigin: String = ""
    private var mImageName: String = ""



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

    fun setMemberNickName(memberNickName: String) {
        mMemberNickName = memberNickName
    }

    fun getMemberNickName(): String {
        return mMemberNickName
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

    fun setCompanyName(companyName: String) {
        mCompanyName = companyName
    }

    fun getCompanyName(): String {
        return mCompanyName
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