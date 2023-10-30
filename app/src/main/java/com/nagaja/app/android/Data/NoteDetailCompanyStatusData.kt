package com.nagaja.app.android.Data

import java.io.Serializable

class NoteDetailCompanyStatusData : Serializable {
    private var mNoteUid: Int = 0
    private var mIsKeepYn: Boolean = false
    private var mIsReportYn: Boolean = false
    private var mIsRegularYn: Boolean = false



    fun setNoteUid(noteUid: Int) {
        mNoteUid = noteUid
    }

    fun getNoteUid(): Int {
        return mNoteUid
    }

    fun setIsKeepYn(isKeepYn: Boolean) {
        mIsKeepYn = isKeepYn
    }

    fun getIsKeepYn(): Boolean {
        return mIsKeepYn
    }

    fun setIsReportYn(isReportYn: Boolean) {
        mIsReportYn = isReportYn
    }

    fun getIsReportYn(): Boolean {
        return mIsReportYn
    }

    fun setIsRegularYn(isRegularYn: Boolean) {
        mIsRegularYn = isRegularYn
    }

    fun getIsRegularYn(): Boolean {
        return mIsRegularYn
    }


}