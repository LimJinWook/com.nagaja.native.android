package com.nagaja.app.android.Data

import java.io.Serializable

class UserData : Serializable {
    private var mMemberUid: Int = 0
    private var mMemberGrant: Int = 0
    private var mMemberStatus: Int = 0              // 0(대기), 1(정상), 3(정지), 7(휴먼), 8(탈퇴요청), 9(탈퇴)
    private var mMemberType: Int = 1
    private var mMemberReferral: String = ""        // 추천인(Email)
    private var mMemberId: String = ""
    private var mName: String = ""
    private var mNickName: String = ""
    private var mEmailId: String = ""
    private var mNationPhone: String = ""
    private var mPhoneNumber: String = ""
    private var mNationUid: Int = 0
    private var mSocialUid: Int = 0
    private var mSocialSecureKey: String = ""
    private var mSecureToken: String = ""
    private var mSecureKey: String = ""
    private var mRefreshKey: String = ""
    private var mIsEmailAuth: Boolean = false       // Email 인증
    private var mIsPhoneAuth: Boolean = false       // 전화번호 인증
    private var mIsNameAuth: Boolean = false        // 전화번호 인증
    private var mLoginDate: String = ""
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mLocationUid: Int = 0
    private var mLatitude: String = ""
    private var mLongitude: String = ""
    private var mAddressCode: String = ""
    private var mAddress: String = ""
    private var mAddressDetail: String = ""
    private var mProfileUrl: String = ""
    private var mProfileName: String = ""
    private var mPoint: Int = 0
    private var mCompanyMemberListData = ArrayList<CompanyMemberData>()
    private var mDifferentUserType: Int = 0
    private var mErrorStatus: Int = 0
    private var mMemberLocationUid: Int = 0

    fun setMemberUid(memberUid: Int) {
        mMemberUid = memberUid
    }

    fun getMemberUid(): Int {
        return mMemberUid
    }

    fun setMemberGrant(memberGrant: Int) {
        mMemberGrant = memberGrant
    }

    fun getMemberGrant(): Int {
        return mMemberGrant
    }

    fun setMemberStatus(memberStatus: Int) {
        mMemberStatus = memberStatus
    }

    fun getMemberStatus(): Int {
        return mMemberStatus
    }

    fun setMemberType(memberType: Int) {
        mMemberType = memberType
    }

    fun getMemberType(): Int {
        return mMemberType
    }

    fun setMemberReferral(memberReferral: String) {
        mMemberReferral = memberReferral
    }

    fun getMemberReferral(): String {
        return mMemberReferral
    }

    fun setMemberId(memberId: String) {
        mMemberId = memberId
    }

    fun getMemberId(): String {
        return mMemberId
    }

    fun setName(name: String) {
        mName = name
    }

    fun getName(): String {
        return mName
    }

    fun setNickName(nickName: String) {
        mNickName = nickName
    }

    fun getNickName(): String {
        return mNickName
    }

    fun setEmailId(emailId: String) {
        mEmailId = emailId
    }

    fun getEmailId(): String {
        return mEmailId
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

    fun setNationUid(nationUid: Int) {
        mNationUid = nationUid
    }

    fun getNationUid(): Int {
        return mNationUid
    }

    fun setSocialUid(socialUid: Int) {
        mSocialUid = socialUid
    }

    fun getSocialUid(): Int {
        return mSocialUid
    }

    fun setSocialSecureKey(socialSecureKey: String) {
        mSocialSecureKey = socialSecureKey
    }

    fun getSocialSecureKey(): String {
        return mSocialSecureKey
    }

    fun setSecureToken(secureToken: String) {
        mSecureToken = secureToken
    }

    fun getSecureToken(): String {
        return mSecureToken
    }

    fun setSecureKey(secureKey: String) {
        mSecureKey = secureKey
    }

    fun getSecureKey(): String {
        return mSecureKey
    }

    fun setRefreshKey(refreshKey: String) {
        mRefreshKey = refreshKey
    }

    fun getRefreshKey(): String {
        return mRefreshKey
    }

    fun setIsEmailAuth(isEmailAuth: Boolean) {
        mIsEmailAuth = isEmailAuth
    }

    fun getIsEmailAuth(): Boolean {
        return mIsEmailAuth
    }

    fun setIsPhoneAuth(isPhoneAuth: Boolean) {
        mIsPhoneAuth = isPhoneAuth
    }

    fun getIsPhoneAuth(): Boolean {
        return mIsPhoneAuth
    }

    fun setIsNameAuth(isNameAuth: Boolean) {
        mIsNameAuth = isNameAuth
    }

    fun getIsNameAuth(): Boolean {
        return mIsNameAuth
    }

    fun setLoginDate(loginDate: String) {
        mLoginDate = loginDate
    }

    fun getLoginDate(): String {
        return mLoginDate
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

    fun setAddressCode(addressCode: String) {
        mAddressCode = addressCode
    }

    fun getAddressCode(): String {
        return mAddressCode
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

    fun setProfileUrl(profileUrl: String) {
        mProfileUrl = profileUrl
    }

    fun getProfileUrl(): String {
        return mProfileUrl
    }

    fun setProfileName(profileName: String) {
        mProfileName = profileName
    }

    fun getProfileName(): String {
        return mProfileName
    }

    fun setPoint(point: Int) {
        mPoint = point
    }

    fun getPoint(): Int {
        return mPoint
    }

    fun setCompanyMemberListData(companyMemberListData: ArrayList<CompanyMemberData>) {
        mCompanyMemberListData = companyMemberListData
    }

    fun getCompanyMemberListData(): ArrayList<CompanyMemberData> {
        return mCompanyMemberListData
    }

    fun setDifferentUserType(differentUserType: Int) {
        mDifferentUserType = differentUserType
    }

    fun getDifferentUserType(): Int {
        return mDifferentUserType
    }

    fun setErrorStatus(errorStatus: Int) {
        mErrorStatus = errorStatus
    }

    fun getErrorStatus(): Int {
        return mErrorStatus
    }

    fun setMemberLocationUid(memberLocationUid: Int) {
        mMemberLocationUid = memberLocationUid
    }

    fun getMemberLocationUid(): Int {
        return mMemberLocationUid
    }
}