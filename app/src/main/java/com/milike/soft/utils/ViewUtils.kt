@file:Suppress("DEPRECATION")

package com.milike.soft.utils

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebSettings
import androidx.viewpager.widget.ViewPager
import com.milike.soft.BuildConfig

fun View.onClick(interval: Long = 500L, body: () -> Unit) {
    var current = 0L
    this.setOnClickListener {
        val time = System.currentTimeMillis()
        if (time - current >= interval) {
            current = time
            body.invoke()
        }
    }
}

fun ViewPager.onPageScrolled(body: (Int, Float) -> Unit) {
    this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            body.invoke(position, positionOffset)
        }

        override fun onPageSelected(position: Int) {

        }
    })
}


@SuppressLint("SetJavaScriptEnabled")
fun WebSettings.initWebViewSetting() {
    this.javaScriptCanOpenWindowsAutomatically = true
    this.builtInZoomControls = true
    this.useWideViewPort = true
    this.loadWithOverviewMode = true
    this.setGeolocationEnabled(true)
    this.domStorageEnabled = true
    this.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
    this.blockNetworkImage = false
    this.javaScriptEnabled = true
    this.userAgentString = "$userAgentString ${BuildConfig.APPLICATION_ID}"
    this.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
}