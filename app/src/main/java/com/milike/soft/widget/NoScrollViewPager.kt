package com.milike.soft.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Created by Administrator on 2018/1/2.
 */
class NoScrollViewPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent?) = false
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?) = false

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, false)
    }
}