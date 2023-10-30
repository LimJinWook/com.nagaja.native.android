package com.nagaja.app.android.View

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.gms.maps.MapView

class CustomMapView(context: Context, attrs: AttributeSet?): MapView(context, attrs) {

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        this.performClick()
        when (ev.action) {
            MotionEvent.ACTION_DOWN ->         // Disallow ScrollView to intercept touch events.
                this.parent.requestDisallowInterceptTouchEvent(true)
            MotionEvent.ACTION_UP ->         // Allow ScrollView to intercept touch events.
                this.parent.requestDisallowInterceptTouchEvent(false)
        }

        // Handle MapView's touch events.
        super.dispatchTouchEvent(ev)
        return true
    }
}