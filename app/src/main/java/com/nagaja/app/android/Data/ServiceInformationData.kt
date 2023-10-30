package com.nagaja.app.android.Data

import java.io.Serializable

class ServiceInformationData : Serializable {
    private var mServiceSite: String = ""
    private var mServiceName: String = ""
    private var mServiceNameEnglish: String = ""
    private var mServiceMasterName: String = ""
    private var mServiceMasterNameEnglish: String = ""
    private var mServiceNumber: String = ""
    private var mServiceNumberEx: String = ""
    private var mServiceAddress: String = ""
    private var mServiceAddressEnglish: String = ""
    private var mServiceAddressDetail: String = ""
    private var mServiceAddressDetailEnglish: String = ""
    private var mServicePhone: String = ""
    private var mServiceEmail: String = ""

    fun setServiceSite(serviceSite: String) {
        mServiceSite = serviceSite
    }

    fun getServiceSite(): String {
        return mServiceSite
    }

    fun setServiceName(serviceName: String) {
        mServiceName = serviceName
    }

    fun getServiceName(): String {
        return mServiceName
    }

    fun setServiceNameEnglish(serviceNameEnglish: String) {
        mServiceNameEnglish = serviceNameEnglish
    }

    fun getServiceNameEnglish(): String {
        return mServiceNameEnglish
    }

    fun setServiceMasterName(serviceMasterName: String) {
        mServiceMasterName = serviceMasterName
    }

    fun getServiceMasterName(): String {
        return mServiceMasterName
    }

    fun setServiceMasterNameEnglish(serviceMasterNameEnglish: String) {
        mServiceMasterNameEnglish = serviceMasterNameEnglish
    }

    fun getServiceMasterNameEnglish(): String {
        return mServiceMasterNameEnglish
    }

    fun setServiceNumber(serviceNumber: String) {
        mServiceNumber = serviceNumber
    }

    fun getServiceNumber(): String {
        return mServiceNumber
    }

    fun setServiceNumberEx(serviceNumberEx: String) {
        mServiceNumberEx = serviceNumberEx
    }

    fun getServiceNumberEx(): String {
        return mServiceNumberEx
    }

    fun setServiceAddress(serviceAddress: String) {
        mServiceAddress = serviceAddress
    }

    fun getServiceAddress(): String {
        return mServiceAddress
    }

    fun setServiceAddressEnglish(serviceAddressEnglish: String) {
        mServiceAddressEnglish = serviceAddressEnglish
    }

    fun getServiceAddressEnglish(): String {
        return mServiceAddressEnglish
    }

    fun setServiceAddressDetail(serviceAddressDetail: String) {
        mServiceAddressDetail = serviceAddressDetail
    }

    fun getServiceAddressDetail(): String {
        return mServiceAddressDetail
    }

    fun setServiceAddressDetailEnglish(serviceAddressDetailEnglish: String) {
        mServiceAddressDetailEnglish = serviceAddressDetailEnglish
    }

    fun getServiceAddressDetailEnglish(): String {
        return mServiceAddressDetailEnglish
    }

    fun setServicePhone(servicePhone: String) {
        mServicePhone = servicePhone
    }

    fun getServicePhone(): String {
        return mServicePhone
    }

    fun setServiceEmail(serviceEmail: String) {
        mServiceEmail = serviceEmail
    }

    fun getServiceEmail(): String {
        return mServiceEmail
    }


}