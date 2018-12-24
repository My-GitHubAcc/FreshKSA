package com.nibrasco.freshksa.Model

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class CustomPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {


    internal var enabled: Boolean = false

    init {
        this.enabled = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.enabled) {
            super.onTouchEvent(event)
        } else false

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.enabled) {
            super.onInterceptTouchEvent(event)
        } else false

    }

    fun setPagingEnabled(enabled: Boolean) {
        this.enabled = enabled
    }
}
