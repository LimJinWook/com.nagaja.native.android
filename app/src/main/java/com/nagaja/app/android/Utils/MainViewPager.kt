package com.nagaja.app.android.Utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.view.MotionEventCompat
import androidx.viewpager.widget.ViewPager

class MainViewPager : ViewPager {

    var mEnable = false

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (mEnable) {
            super.onInterceptTouchEvent(ev)
        } else {
            if (MotionEventCompat.getActionMasked(ev) != 2 && super.onInterceptTouchEvent(ev)) {
                super.onTouchEvent(ev)
            }
            false
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (mEnable) super.onTouchEvent(ev) else MotionEventCompat.getActionMasked(ev) != 2 && super.onTouchEvent(ev)
    }

    fun setPagingEnabled(enabled: Boolean) {
        this.mEnable = enabled
    }
}
