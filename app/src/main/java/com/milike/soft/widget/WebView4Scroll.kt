package com.milike.soft.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView

class WebView4Scroll(context: Context, attrs: AttributeSet) : WebView(context, attrs) {
    private var onScrollTop: ((Int) -> Unit)? = null
    var startX: Float = -1f
    var startY: Float = -1f
    var endX: Float = -1f
    var endY: Float = -1f

    fun setOnScrollTop(onScrollTop: (Int) -> Unit) {
        this.onScrollTop = onScrollTop
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    startY = event.y
                }
                else -> {
                    endX = event.x - startX
                    endY = event.y - startY
                    if (onScrollTop != null && Math.abs(endY) > Math.abs(endX) && endY > 0)
                        evaluateJavascript("$(\".mf-content\").scrollTop()") { s ->
                            onScrollTop?.invoke(s.toIntOrNull() ?: 0)
                        } else {
                        onScrollTop?.invoke(10)
                    }
                }
            }

        }
        return super.onTouchEvent(event)
    }
}
