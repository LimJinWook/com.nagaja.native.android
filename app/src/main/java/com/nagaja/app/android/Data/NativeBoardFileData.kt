package com.nagaja.app.android.Data

import java.io.Serializable

class NativeBoardFileData : Serializable {
    private var mBoardFileUid: Int = 0
    private var mBoardFileSort: Int = 0
    private var mBoardFileOrigin: String = ""
    private var mBoardFileName: String = ""
    private var mIsUseYN: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mBoardUid: Int = 0

    fun setBoardFileUid(boardFileUid: Int) {
        mBoardFileUid = boardFileUid
    }

    fun getBoardFileUid(): Int {
        return mBoardFileUid
    }

    fun setBoardFileSort(boardFileSort: Int) {
        mBoardFileSort = boardFileSort
    }

    fun getBoardFileSort(): Int {
        return mBoardFileSort
    }

    fun setBoardFileOrigin(boardFileOrigin: String) {
        mBoardFileOrigin = boardFileOrigin
    }

    fun getBoardFileOrigin(): String {
        return mBoardFileOrigin
    }

    fun setBoardFileName(boardFileName: String) {
        mBoardFileName = boardFileName
    }

    fun getBoardFileName(): String {
        return mBoardFileName
    }

    fun setIsUseYN(isUseYN: Boolean) {
        mIsUseYN = isUseYN
    }

    fun getIsUseYN(): Boolean {
        return mIsUseYN
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

    fun setBoardUid(boardUid: Int) {
        mBoardUid = boardUid
    }

    fun getBoardUid(): Int {
        return mBoardUid
    }
}