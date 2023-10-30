package com.nagaja.app.android.Data

import java.io.Serializable

class CompanyManagerData : Serializable {
    private var mCompanyManagerUid: Int = 0
    private var mCompanyManagerGrant: Int = 0       // 1: 관리자, 2: 매니저, 3: 프론트, 4: 웨이터, 5: 일반
    private var mMemberUid: Int = 0
    private var mCompanyUid: Int = 0
    private var mMemberName: String = ""
    private var mCreateDate: String = ""
    private var mUpdateDate: String = ""



    fun setCompanyManagerUid(companyManagerUid: Int) {
        mCompanyManagerUid = companyManagerUid
    }

    fun getCompanyManagerUid(): Int {
        return mCompanyManagerUid
    }

    fun setCompanyManagerGrant(companyManagerGrant: Int) {
        mCompanyManagerGrant = companyManagerGrant
    }

    fun getCompanyManagerGrant(): Int {
        return mCompanyManagerGrant
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

    fun setMemberName(memberName: String) {
        mMemberName = memberName
    }

    fun getMemberName(): String {
        return mMemberName
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
}