package com.nagaja.app.android.Data

import java.io.Serializable

class MemberInformationData : Serializable {
    private var mCompanyUid: Int = 0
    private var mName: String = ""
    private var mNationPhone: String = ""
    private var mPhoneNumber: String = ""
    private var mAddress: String = ""
    private var mAddressDetail: String = ""
    private var mImageOrigin: String = ""
    private var mImageName: String = ""
    private var mCategoryUid: Int = 0
    private var mNickName: String = ""



    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setName(name: String) {
        mName = name
    }

    fun getName(): String {
        return mName
    }

    fun setNationPhone(nationPhone: String) {
        mNationPhone = nationPhone
    }

    fun getNationPhone(): String {
        return mNationPhone
    }

    fun setPhoneNumber(phoneNumber: String) {
        mPhoneNumber = phoneNumber
    }

    fun getPhoneNumber(): String {
        return mPhoneNumber
    }

    fun setAddress(address: String) {
        mAddress = address
    }

    fun getAddress(): String {
        return mAddress
    }

    fun setAddressDetail(addressDetail: String) {
        mAddressDetail = addressDetail
    }

    fun getAddressDetail(): String {
        return mAddressDetail
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

    fun setCategoryUid(categoryUid: Int) {
        mCategoryUid = categoryUid
    }

    fun getCategoryUid(): Int {
        return mCategoryUid
    }

    fun setNickName(nickName: String) {
        mNickName = nickName
    }

    fun getNickName(): String {
        return mNickName
    }

}