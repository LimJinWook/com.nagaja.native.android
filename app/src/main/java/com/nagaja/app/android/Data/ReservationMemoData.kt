package com.nagaja.app.android.Data

import java.io.Serializable

class ReservationMemoData : Serializable {
    private var mReservationCommentUid: Int = 0
    private var mReservationComment: String = ""
    private var mCreateDate: String = ""
    private var mReservationUid: Int = 0


    fun setReservationCommentUid(reservationCommentUid: Int) {
        mReservationCommentUid = reservationCommentUid
    }

    fun getReservationCommentUid(): Int {
        return mReservationCommentUid
    }

    fun setReservationComment(reservationComment: String) {
        mReservationComment = reservationComment
    }

    fun getReservationComment(): String {
        return mReservationComment
    }

    fun setCreateDate(createDate: String) {
        mCreateDate = createDate
    }

    fun getCreateDate(): String {
        return mCreateDate
    }

    fun setReservationUid(reservationUid: Int) {
        mReservationUid = reservationUid
    }

    fun getReservationUid(): Int {
        return mReservationUid
    }

}