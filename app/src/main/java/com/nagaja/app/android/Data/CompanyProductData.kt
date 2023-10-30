package com.nagaja.app.android.Data

import java.io.Serializable

class CompanyProductData : Serializable {
    private var mItemUid: Int = 0
    private var mItemStatus: Int = 0
    private var mItemName: String = ""
    private var mItemContent: String = ""
    private var mItemCurrencyCode: String = ""
    private var mItemPrice: String = ""
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mCategoryUid: Int = 0
    private var mItemCategoryUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mMemberUid: Int = 0
    private var mImageListData = ArrayList<CompanyProductImageData>()
    private var mCurrencyListData = ArrayList<CompanyProductCurrencyData>()

    fun setItemUid(itemUid: Int) {
        mItemUid = itemUid
    }

    fun getItemUid(): Int {
        return mItemUid
    }

    fun setItemStatus(itemStatus: Int) {
        mItemStatus = itemStatus
    }

    fun getItemStatus(): Int {
        return mItemStatus
    }

    fun setItemName(itemName: String) {
        mItemName = itemName
    }

    fun getItemName(): String {
        return mItemName
    }

    fun setItemContent(itemContent: String) {
        mItemContent = itemContent
    }

    fun getItemContent(): String {
        return mItemContent
    }

    fun setItemCurrencyCode(itemCurrencyCode: String) {
        mItemCurrencyCode = itemCurrencyCode
    }

    fun getItemCurrencyCode(): String {
        return mItemCurrencyCode
    }

    fun setItemPrice(itemPrice: String) {
        mItemPrice = itemPrice
    }

    fun getItemPrice(): String {
        return mItemPrice
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

    fun setCategoryUid(categoryUid: Int) {
        mCategoryUid = categoryUid
    }

    fun getCategoryUid(): Int {
        return mCategoryUid
    }

    fun setItemCategoryUid(itemCategoryUid: Int) {
        mItemCategoryUid = itemCategoryUid
    }

    fun getItemCategoryUid(): Int {
        return mItemCategoryUid
    }

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setMemberUid(memberUid: Int) {
        mMemberUid = memberUid
    }

    fun getMemberUid(): Int {
        return mMemberUid
    }

    fun setImageListData(imageListData: ArrayList<CompanyProductImageData>) {
        mImageListData = imageListData
    }

    fun getImageListData(): ArrayList<CompanyProductImageData> {
        return mImageListData
    }

    fun setCurrencyListData(currencyListData: ArrayList<CompanyProductCurrencyData>) {
        mCurrencyListData = currencyListData
    }

    fun getCurrencyListData(): ArrayList<CompanyProductCurrencyData> {
        return mCurrencyListData
    }
}