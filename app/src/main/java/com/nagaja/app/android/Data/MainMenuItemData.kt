package com.nagaja.app.android.Data

import java.io.Serializable

class MainMenuItemData : Serializable {
    private var mBaseMenuUid: Int = 0
    private var mMenuName: String = ""
    private var mMenuNameEnglish: String = ""
    private var mMenuImageOrigin: String = ""
    private var mMenuImageName: String = ""
    private var mMenuGroup: Int = 0
    private var mMenuSort: Int = 0
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mLastUpdateDate: String = ""
    private var mCategoryUid: Int = 0
    private var mCategoryUri: String = ""
    private var mShareBoardUid: Int = 0
    private var mIsEvent: Boolean = false
    private var mEventCategoryUid: Int = 0
    private var mEventTargetUid: Int = 0


    fun setBaseMenuUid(baseMenuUid: Int) {
        mBaseMenuUid = baseMenuUid
    }

    fun getBaseMenuUid(): Int {
        return mBaseMenuUid
    }

    fun setMenuName(menuName: String) {
        mMenuName = menuName
    }

    fun getMenuName(): String {
        return mMenuName
    }

    fun setMenuNameEnglish(menuNameEnglish: String) {
        mMenuNameEnglish = menuNameEnglish
    }

    fun getMenuNameEnglish(): String {
        return mMenuNameEnglish
    }

    fun setMenuImageOrigin(menuImageOrigin: String) {
        mMenuImageOrigin = menuImageOrigin
    }

    fun getMenuImageOrigin(): String {
        return mMenuImageOrigin
    }

    fun setMenuImageName(menuImageName: String) {
        mMenuImageName = menuImageName
    }

    fun getMenuImageName(): String {
        return mMenuImageName
    }

    fun setMenuGroup(menuGroup: Int) {
        mMenuGroup = menuGroup
    }

    fun getMenuGroup(): Int {
        return mMenuGroup
    }

    fun setMenuSort(menuSort: Int) {
        mMenuSort = menuSort
    }

    fun getMenuSort(): Int {
        return mMenuSort
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

    fun setLastUpdateDate(lastUpdateDate: String) {
        mLastUpdateDate = lastUpdateDate
    }

    fun getLastUpdateDate(): String {
        return mLastUpdateDate
    }

    fun setCategoryUid(categoryUid: Int) {
        mCategoryUid = categoryUid
    }

    fun getCategoryUid(): Int {
        return mCategoryUid
    }

    fun setCategoryUri(categoryUri: String) {
        mCategoryUri = categoryUri
    }

    fun getCategoryUri(): String {
        return mCategoryUri
    }

    fun setShareBoardUid(shareBoardUid: Int) {
        mShareBoardUid = shareBoardUid
    }

    fun getShareBoardUid(): Int {
        return mShareBoardUid
    }

    fun setIsEvent(isEvent: Boolean) {
        mIsEvent = isEvent
    }

    fun getIsEvent(): Boolean {
        return mIsEvent
    }

    fun setEventCategoryUid(eventCategoryUid: Int) {
        mEventCategoryUid = eventCategoryUid
    }

    fun getEventCategoryUid(): Int {
        return mEventCategoryUid
    }

    fun setEventTargetUid(eventTargetUid: Int) {
        mEventTargetUid = eventTargetUid
    }

    fun getEventTargetUid(): Int {
        return mEventTargetUid
    }
}