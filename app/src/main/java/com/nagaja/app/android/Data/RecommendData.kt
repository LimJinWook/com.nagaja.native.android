package com.nagaja.app.android.Data

import java.io.Serializable

class RecommendData : Serializable {
    private var mImageUrl: String = ""
    private var mTitle: String = ""
    private var mSubTitle: String = ""
    private var mIsPickUp: Boolean = false
    private var mIsDelivery: Boolean = false
    private var mIsReservation: Boolean = false
    private var mIsEvent: Boolean = false
    private var mEventMessage: String = ""

    fun setImageUrl(imageUrl: String) {
        mImageUrl = imageUrl
    }

    fun getImageUrl(): String {
        return mImageUrl
    }

    fun setTitle(title: String) {
        mTitle = title
    }

    fun getTitle(): String {
        return mTitle
    }

    fun setSubTitle(subTitle: String) {
        mSubTitle = subTitle
    }

    fun getSubTitle(): String {
        return mSubTitle
    }

    fun setIsPickUp(isPickUp: Boolean) {
        mIsPickUp = isPickUp
    }

    fun getIsPickUp(): Boolean {
        return mIsPickUp
    }

    fun setIsDelivery(isDelivery: Boolean) {
        mIsDelivery = isDelivery
    }

    fun getIsDelivery(): Boolean {
        return mIsDelivery
    }

    fun setIsReservation(isReservation: Boolean) {
        mIsReservation = isReservation
    }

    fun getIsReservation(): Boolean {
        return mIsReservation
    }

    fun setIsEvent(isEvent: Boolean) {
        mIsEvent = isEvent
    }

    fun getIsEvent(): Boolean {
        return mIsEvent
    }

    fun setEventMessage(eventMessage: String) {
        mEventMessage = eventMessage
    }

    fun getEventMessage(): String {
        return mEventMessage
    }
}