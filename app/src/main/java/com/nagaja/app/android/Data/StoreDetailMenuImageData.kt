package com.nagaja.app.android.Data

import java.io.Serializable

class StoreDetailMenuImageData : Serializable {
    private var mItemImageUid: Int = 0
    private var mItemImageSort: Int = 0
    private var mItemImageOrigin: String = ""
    private var mItemImageName: String = ""
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mItemUid: Int = 0


    fun setItemImageUid(itemImageUid: Int) {
        mItemImageUid = itemImageUid
    }

    fun getItemImageUid(): Int {
        return mItemImageUid
    }

    fun setItemImageSort(itemImageSort: Int) {
        mItemImageSort = itemImageSort
    }

    fun getItemImageSort(): Int {
        return mItemImageSort
    }

    fun setItemImageOrigin(itemImageOrigin: String) {
        mItemImageOrigin = itemImageOrigin
    }

    fun getItemImageOrigin(): String {
        return mItemImageOrigin
    }

    fun setItemImageName(itemImageName: String) {
        mItemImageName = itemImageName
    }

    fun getItemImageName(): String {
        return mItemImageName
    }

    fun setIsUseYn(isUseYn: Boolean) {
        mIsUseYn = isUseYn
    }

    fun getIsUseYn(): Boolean {
        return mIsUseYn
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

    fun setItemUid(itemUid: Int) {
        mItemUid = itemUid
    }

    fun getItemUid(): Int {
        return mItemUid
    }

}