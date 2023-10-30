package com.nagaja.app.android.View

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.R


class ViewDotsIndicator : LinearLayout {
    private var drawableResSelected = 0
    private var drawableResBackground = 0

    private var mIsFocus = false

    @Nullable
    private var layoutManager: LinearLayoutManager? = null
    private var savedPos = 0

    @Nullable
    private var listener: ViewDotsIndicatorListener? = null

    constructor(@NonNull context: Context?) : super(context) {
        initView(null)
    }

    constructor(@NonNull context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs)
    }

    constructor(@NonNull context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        savedPos = -1
        initDrawables(attrs)
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        updateDots(0)
    }

    private fun initDrawables(attrs: AttributeSet?) {
        if (attrs == null) {
            drawableResSelected = R.drawable.indicator_enable
            drawableResBackground = R.drawable.indicator_disable
        } else {
            val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.dotsindicator,
                0, 0
            )
            drawableResSelected = a.getResourceId(R.styleable.dotsindicator_drawableSelected, R.drawable.indicator_enable)
            drawableResBackground = a.getResourceId(R.styleable.dotsindicator_drawableBackground, R.drawable.indicator_disable)
            a.recycle()
        }
    }

    fun init(recyclerView: RecyclerView, isFocus: Boolean) {
        if (recyclerView.layoutManager !is LinearLayoutManager) {
            Log.e("dotsindicator", "only linear layout manager supported")
            return
        }

        mIsFocus = isFocus

        recyclerView.addOnScrollListener(ScrollListener())
        layoutManager = recyclerView.layoutManager as LinearLayoutManager?
        if (recyclerView.adapter != null) updateData(recyclerView.adapter!!.itemCount, 0)
    }

    fun updateData(itemCount: Int, currentPos: Int) {
        setDots(itemCount)
        savedPos = -1
        updateDots(currentPos)
    }

    private fun setDots(itemCount: Int) {
        if (itemCount <= 0) return
        removeAllViews()
        val margin = context.resources.getDimension(R.dimen.dot_photo_list_margin).toInt()
        val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(5, margin, 5, margin)
        for (i in 0 until itemCount) {
            val imageView = ImageView(context)
            imageView.setImageResource(drawableResSelected)
            imageView.layoutParams = params
            addView(imageView)
        }
    }

    private fun updateDots(pos: Int) {
        if (savedPos == pos) {
            return
        }

        notifyPos(pos)
        savedPos = pos
        for (i in 0 until childCount) (getChildAt(i) as ImageView).setImageResource(if (i == pos) drawableResSelected else drawableResBackground)
    }

    fun setListener(listener: ViewDotsIndicatorListener?) {
        this.listener = listener
    }

    private fun notifyPos(pos: Int) {
        if (listener == null) return
        listener!!.onDotActive(pos)
    }

    private inner class ScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (layoutManager == null) return
            var pos = layoutManager!!.findFirstVisibleItemPosition()
            val view = layoutManager!!.findViewByPosition(pos) ?: return
            if (view.right < context.resources.displayMetrics.widthPixels / 2) pos += 1

            var value = 0

            if (mIsFocus) {
                if (pos < 4) {
                    value = 0
                } else if (pos in 4..7) {
                    value = 1
                } else if (pos in 8..11) {
                    value = 2
                } else {
                    value = 3
                }
            } else {
                if (pos < 3) {
                    value = 0
                } else {
                    value = 1
                }
            }


            updateDots(value)
        }
    }
}