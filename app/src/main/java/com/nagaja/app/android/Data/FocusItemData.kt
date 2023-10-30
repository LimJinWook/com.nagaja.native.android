package com.nagaja.app.android.Data

import java.io.Serializable

class FocusItemData : Serializable {
    private var mImage: Int = 0
    private var mTitle: String = ""

    fun setImage(image: Int) {
        mImage = image
    }

    fun getImage(): Int {
        return mImage
    }

    fun setTitle(title: String) {
        mTitle = title
    }

    fun getTitle(): String {
        return mTitle
    }
}