package com.nagaja.app.android.Data

import java.io.Serializable

class NativeBoardImageData : Serializable {
    private var mBoardImageUid: Int = 0
    private var mBoardImageSort: Int = 0
    private var mBoardImageOrigin: String = ""
    private var mBoardImageName: String = ""
    private var mIsUseYN: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mBoardUid: Int = 0

    fun setBoardImageUid(boardImageUid: Int) {
        mBoardImageUid = boardImageUid
    }

    fun getBoardImageUid(): Int {
        return mBoardImageUid
    }

    fun setBoardImageSort(boardImageSort: Int) {
        mBoardImageSort = boardImageSort
    }

    fun getBoardImageSort(): Int {
        return mBoardImageSort
    }

    fun setBoardImageOrigin(boardImageOrigin: String) {
        mBoardImageOrigin = boardImageOrigin
    }

    fun getBoardImageOrigin(): String {
        return mBoardImageOrigin
    }

    fun setBoardImageName(boardImageName: String) {
        mBoardImageName = boardImageName
    }

    fun getBoardImageName(): String {
        return mBoardImageName
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