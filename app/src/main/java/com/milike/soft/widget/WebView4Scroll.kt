package com.milike.soft.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView

class WebView4Scroll(context: Context, attrs: AttributeSet) : WebView(context, attrs) {
    private var onScrollTop: ((Int) -> Unit)? = null

    fun setOnScrollTop(onScrollTop: (Int) -> Unit) {
        this.onScrollTop = onScrollTop
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (onScrollTop != null) {
            evaluateJavascript("$(\".mf-content\").scrollTop()") { s ->
                onScrollTop?.invoke(s.toIntOrNull() ?: 0)
            }
        }
        return super.onTouchEvent(event)
    }
}
