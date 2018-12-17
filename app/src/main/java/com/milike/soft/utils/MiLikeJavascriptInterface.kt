package com.milike.soft.utils

import android.content.Intent
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.blankj.utilcode.util.Utils
import com.milike.soft.activity.WebActivity
import org.json.JSONObject

class MiLikeJavascriptInterface(val webView: WebView) {
    @JavascriptInterface
    fun JsToJavaShare(jsonStr: String) {
        Log.e("zuiweng", jsonStr)
        val json = JSONObject(jsonStr)
        val platform = json.getString("platform")
        val title = json.getString("title")
        val content = json.getString("content")
        val imageUrl = json.getString("imageUrl")
        val linkUrl = json.getString("linkUrl")
        if (!platform.isNullOrEmpty() && !title.isNullOrEmpty() && !content.isNullOrEmpty() && !imageUrl.isNullOrEmpty() && !linkUrl.isNullOrEmpty()) {
            showShareDialog(webView, platform, title, content, imageUrl, linkUrl)
        }
    }

    @JavascriptInterface
    fun startNewPage(url: String) {
        Utils.getApp().startActivity(Intent(Utils.getApp(), WebActivity::class.java).apply {
            putExtra("url", url)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    @JavascriptInterface
    fun refresh() {
        if (webView.canGoBack()) {
            webView.goBack()
        }
    }
}