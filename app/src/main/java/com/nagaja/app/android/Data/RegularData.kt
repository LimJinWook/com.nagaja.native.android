package com.nagaja.app.android.Data

import java.io.Serializable

class RegularData : Serializable {
    private var mTotalCount = 0
    private var mRegularUid: Int = 0
    private var mRegularMemo: String = ""
    private var mIsUseYn: Boolean = false
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""
    private var mMemberUid: Int = 0
    private var mMemberNickName: String = ""
    private var mMemberName: String = ""
    private var mCompanyUid: Int = 0
    private var mCompanyImageOrigin: String = ""
    private var mCompanyImageName: String = ""
    private var mCompanyName: String = ""
    private var mCompanyNameEnglish: String = ""
    private var mCompanyNationPhone: String = ""
    private var mCompanyPhoneNumber: String = ""
    private var mCompanyAddress: String = ""
    private var mCompanyAddressDetail: String = ""

    // Company Regular
    private var mMemberImageName: String = ""
    private var mMemberImageOrigin: String = ""
    private var mMemberNationPhone: String = ""
    private var mMemberPhoneNumber: String = ""


    fun setTotalCount(totalCount: Int) {
        mTotalCount = totalCount
    }

    fun getTotalCount(): Int {
        return mTotalCount
    }

    fun setRegularUid(regularUid: Int) {
        mRegularUid = regularUid
    }

    fun getRegularUid(): Int {
        return mRegularUid
    }

    fun setRegularMemo(regularMemo: String) {
        mRegularMemo = regularMemo
    }

    fun getRegularMemo(): String {
        return mRegularMemo
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

    fun setMemberUid(memberUid: Int) {
        mMemberUid = memberUid
    }

    fun getMemberUid(): Int {
        return mMemberUid
    }

    fun setMemberNickName(memberNickName: String) {
        mMemberNickName = memberNickName
    }

    fun getMemberNickName(): String {
        return mMemberNickName
    }

    fun setMemberName(memberName: String) {
        mMemberName = memberName
    }

    fun getMemberName(): String {
        return mMemberName
    }

    fun setCompanyUid(companyUid: Int) {
        mCompanyUid = companyUid
    }

    fun getCompanyUid(): Int {
        return mCompanyUid
    }

    fun setCompanyImageOrigin(companyImageOrigin: String) {
        mCompanyImageOrigin = companyImageOrigin
    }

    fun getCompanyImageOrigin(): String {
        return mCompanyImageOrigin
    }

    fun setCompanyImageName(companyImageName: String) {
        mCompanyImageName = companyImageName
    }

    fun getCompanyImageName(): String {
        return mCompanyImageName
    }

    fun setCompanyName(companyName: String) {
        mCompanyName = companyName
    }

    fun getCompanyName(): String {
        return mCompanyName
    }

    fun setCompanyNameEnglish(companyNameEnglish: String) {
        mCompanyNameEnglish = companyNameEnglish
    }

    fun getCompanyNameEnglish(): String {
        return mCompanyNameEnglish
    }

    fun setCompanyNationPhone(companyNationPhone: String) {
        mCompanyNationPhone = companyNationPhone
    }

    fun getCompanyNationPhone(): String {
        return mCompanyNationPhone
    }

    fun setCompanyPhoneNumber(companyPhoneNumber: String) {
        mCompanyPhoneNumber = companyPhoneNumber
    }

    fun getCompanyPhoneNumber(): String {
        return mCompanyPhoneNumber
    }

    fun setCompanyAddress(companyAddress: String) {
        mCompanyAddress = companyAddress
    }

    fun getCompanyAddress(): String {
        return mCompanyAddress
    }

    fun setCompanyAddressDetail(companyAddressDetail: String) {
        mCompanyAddressDetail = companyAddressDetail
    }

    fun getCompanyAddressDetail(): String {
        return mCompanyAddressDetail
    }

    fun setMemberImageName(memberImageName: String) {
        mMemberImageName = memberImageName
    }

    fun getMemberImageName(): String {
        return mMemberImageName
    }

    fun setMemberImageOrigin(memberImageOrigin: String) {
        mMemberImageOrigin = memberImageOrigin
    }

    fun getMemberImageOrigin(): String {
        return mMemberImageOrigin
    }

    fun setMemberNationPhone(memberNationPhone: String) {
        mMemberNationPhone = memberNationPhone
    }

    fun getMemberNationPhone(): String {
        return mMemberNationPhone
    }

    fun setMemberPhoneNumber(memberPhoneNumber: String) {
        mMemberPhoneNumber = memberPhoneNumber
    }

    fun getMemberPhoneNumber(): String {
        return mMemberPhoneNumber
    }

}