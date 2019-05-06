package com.rondaugherty.weatherappcodechallenge.Utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class CustomViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private var hasEnabled: Boolean = false

    init {
        hasEnabled = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (hasEnabled) {
            super.onTouchEvent(event)
        } else false

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (hasEnabled) {
            super.onInterceptTouchEvent(event)
        } else false

    }

    fun setPagingEnabled(enabled: Boolean) {
        hasEnabled = enabled
    }
}