package com.nagaja.app.android.Utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.view.MotionEventCompat
import androidx.viewpager.widget.ViewPager

class CustomViewPager : ViewPager {

    var mEnable = false

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        try {
//            return if (mEnable) {
//                super.onInterceptTouchEvent(ev)
//            } else {
//                if (MotionEventCompat.getActionMasked(ev) != 2 && super.onInterceptTouchEvent(ev)) {
//                    super.onTouchEvent(ev)
//                }
//                false
//            }
//        } catch (ex: IllegalArgumentException) {
//            ex.printStackTrace()
//        }
//
//        return false
//    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return try {
            super.dispatchTouchEvent(ev)
        }catch (e: Exception){
            e.printStackTrace()
            true
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        try {
            return if (mEnable) super.onTouchEvent(ev) else MotionEventCompat.getActionMasked(ev) != 2 && super.onTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }

    fun setPagingEnabled(enabled: Boolean) {
        this.mEnable = enabled
    }
}
