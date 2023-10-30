package com.nagaja.app.android.Data

import java.io.Serializable

class EventData: Serializable {
    private var mPopupUid: Int = 0
    private var mPopupSubject: String? = ""
    private var mPopupContent: String? = ""
    private var mPopupImageName: String? = ""
    private var mPopupImageOrigin: String? = ""
    private var mPopupTargetUrl: String? = ""
    private var mNationUid: Int = 0
    private var mPopupUseYn: Boolean = false

    fun setPopupUid(popupUid: Int) {
        mPopupUid = popupUid
    }

    fun getPopupUid(): Int {
        return mPopupUid
    }

    fun setPopupSubject(popupSubject: String) {
        mPopupSubject = popupSubject
    }

    fun getPopupSubject(): String? {
        return mPopupSubject
    }

    fun setPopupContent(popupContent: String) {
        mPopupContent = popupContent
    }

    fun getPopupContent(): String? {
        return mPopupContent
    }

    fun setPopupImageName(popupImageName: String) {
        mPopupImageName = popupImageName
    }

    fun getPopupImageName(): String? {
        return mPopupImageName
    }

    fun setPopupImageOrigin(popupImageOrigin: String) {
        mPopupImageOrigin = popupImageOrigin
    }

    fun getPopupImageOrigin(): String? {
        return mPopupImageOrigin
    }

    fun setPopupTargetUrl(popupTargetUrl: String) {
        mPopupTargetUrl = popupTargetUrl
    }

    fun getPopupTargetUrl(): String? {
        return mPopupTargetUrl
    }

    fun setNationUid(nationUid: Int) {
        mNationUid = nationUid
    }

    fun getNationUid(): Int {
        return mNationUid
    }

    fun setPopupUseYn(popupUseYn: Boolean) {
        mPopupUseYn = popupUseYn
    }

    fun getPopupUseYn(): Boolean {
        return mPopupUseYn
    }

}