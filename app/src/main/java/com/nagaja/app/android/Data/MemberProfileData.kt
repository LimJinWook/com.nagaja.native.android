package com.nagaja.app.android.Data

import java.io.Serializable

class MemberProfileData : Serializable {
    private var mMemberUid: Int = 0
    private var mMemberStatus: Int = 0
    private var mMemberId: String = ""
    private var mMemberEmail: String = ""
    private var mMemberName: String = ""
    private var mMemberNickName: String = ""
    private var mNationUid: Int = 0
    private var mCompanyCount: Int = 0
    private var mNationPhone: String = ""
    private var mPhoneNumber: String = ""
    private var mLocationUid: Int = 0
    private var mLatitude: String = ""
    private var mLongitude: String = ""
    private var mAddressZipCode: String = ""
    private var mAddress: String = ""
    private var mAddressDetail: String = ""
    private var mImageOrigin: String = ""
    private var mImageName: String = ""
    private var mIsPush: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mLoginDate: String = ""



    fun setMemberUid(memberUid: Int) {
        mMemberUid = memberUid
    }

    fun getMemberUid(): Int {
        return mMemberUid
    }

    fun setMemberStatus(memberStatus: Int) {
        mMemberStatus = memberStatus
    }

    fun getMemberStatus(): Int {
        return mMemberStatus
    }

    fun setMemberId(memberId: String) {
        mMemberId = memberId
    }

    fun getMemberId(): String {
        return mMemberId
    }

    fun setMemberEmail(memberEmail: String) {
        mMemberEmail = memberEmail
    }

    fun getMemberEmail(): String {
        return mMemberEmail
    }

    fun setMemberName(memberName: String) {
        mMemberName = memberName
    }

    fun getMemberName(): String {
        return mMemberName
    }

    fun setMemberNickName(memberNickName: String) {
        mMemberNickName = memberNickName
    }

    fun getMemberNickName(): String {
        return mMemberNickName
    }

    fun setNationUid(nationUid: Int) {
        mNationUid = nationUid
    }

    fun getNationUid(): Int {
        return mNationUid
    }

    fun setCompanyCount(companyCount: Int) {
        mCompanyCount = companyCount
    }

    fun getCompanyCount(): Int {
        return mCompanyCount
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

    fun setLocationUid(locationUid: Int) {
        mLocationUid = locationUid
    }

    fun getLocationUid(): Int {
        return mLocationUid
    }

    fun setLatitude(latitude: String) {
        mLatitude = latitude
    }

    fun getLatitude(): String {
        return mLatitude
    }

    fun setLongitude(longitude: String) {
        mLongitude = longitude
    }

    fun getLongitude(): String {
        return mLongitude
    }

    fun setAddressZipCode(addressZipCode: String) {
        mAddressZipCode = addressZipCode
    }

    fun getAddressZipCode(): String {
        return mAddressZipCode
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

    fun setIsPush(isPush: Boolean) {
        mIsPush = isPush
    }

    fun getIsPush(): Boolean {
        return mIsPush
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

    fun setLoginDate(loginDate: String) {
        mLoginDate = loginDate
    }

    fun getLoginDate(): String {
        return mLoginDate
    }

}