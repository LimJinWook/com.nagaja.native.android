package com.nagaja.app.android.Data

import com.nagaja.app.android.Data.*
import java.io.Serializable

class NoteDetailData: Serializable {
    private var mNoteUid: Int = 0
    private var mNoteStatus: Int = 0
    private var mNoteMessage: String = ""
    private var mMemberUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mNoteTypeUid: Int = 0
    private var mNoteImageList = ArrayList<UsedMarketImageData>()
    private var mNoteFileList = ArrayList<FileData>()
    private var mNoteDetailSenderData = NoteDetailSenderData()
    private var mNoteDetailCompanyStatusData = NoteDetailCompanyStatusData()
    private var mNoteReceiverData = NoteReceiverData()
    private var mNoteSenderData = NoteSenderData()



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

    fun setNoteTypeUid(noteTypeUid: Int) {
        mNoteTypeUid = noteTypeUid
    }

    fun getNoteTypeUid(): Int {
        return mNoteTypeUid
    }

    fun setNoteImageList(noteImageList: ArrayList<UsedMarketImageData>) {
        mNoteImageList = noteImageList
    }

    fun getNoteImageList(): ArrayList<UsedMarketImageData> {
        return mNoteImageList
    }

    fun setNoteFileList(noteFileList: ArrayList<FileData>) {
        mNoteFileList = noteFileList
    }

    fun getNoteFileList(): ArrayList<FileData> {
        return mNoteFileList
    }

//    fun setNoteDetailSenderData(noteDetailSenderData: NoteDetailSenderData) {
//        mNoteDetailSenderData = noteDetailSenderData
//    }
//
//    fun getNoteDetailSenderData(): NoteDetailSenderData {
//        return mNoteDetailSenderData
//    }
//
//    fun setNoteDetailCompanyStatusData(noteDetailCompanyStatusData: NoteDetailCompanyStatusData) {
//        mNoteDetailCompanyStatusData = noteDetailCompanyStatusData
//    }
//
//    fun getNoteDetailCompanyStatusData(): NoteDetailCompanyStatusData {
//        return mNoteDetailCompanyStatusData
//    }

    fun setNoteReceiverData(noteReceiverData: NoteReceiverData) {
        mNoteReceiverData = noteReceiverData
    }

    fun getNoteReceiverData(): NoteReceiverData {
        return mNoteReceiverData
    }

    fun setNoteSenderData(noteSenderData: NoteSenderData) {
        mNoteSenderData = noteSenderData
    }

    fun getNoteSenderData(): NoteSenderData {
        return mNoteSenderData
    }

}