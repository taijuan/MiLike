package com.milike.soft.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.milike.soft.BuildConfig
import com.milike.soft.activity.WebActivity

abstract class MiLikeWebViewClient(private val isLogon: Boolean = false) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url.toString()
        Log.e("zuiwengxxxxxxx", url)
        return if (view != null && url.isNotEmpty()) {
            when {
                interceptDefUrlLoading(url) || interceptUrlLoading(view, url) -> {
                    filterAction(url, view)
                    true
                }
                else -> super.shouldOverrideUrlLoading(view, request)
            }
        } else {
            super.shouldOverrideUrlLoading(view, request)
        }
    }

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        Log.e("zuiwengonPa", url.toString())
        super.onPageCommitVisible(view, url)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        Log.e("zuiwengxxxxxxx start", url)
        if (view != null && !url.isNullOrEmpty()) {
            when {
                interceptDefUrlLoading(url) || interceptUrlLoading(view, url) -> {
                    view.pauseTimers()
                    view.stopLoading()
                    view.resumeTimers()
                    filterAction(url, view)
                }
                else -> onStart(view, url)
            }
        }
    }


    override fun onPageFinished(view: WebView?, url: String?) {
        Log.e("zuiwengxxxxxxx Finish", url.toString())
        if (view != null && !url.isNullOrEmpty()) {
            onEnd(view, url)
        }
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        Log.e("zuiwengxxxxxxx Error", request?.url.toString())
        if (view != null && request != null) {
            val url = request.url.toString()
            if (!interceptDefUrlLoading(url) && !interceptUrlLoading(view, url) && request.isForMainFrame) {
                if (NetworkUtils.isConnected()) {
                    view.loadUrl("file:///android_asset/404.html")
                } else {
                    view.loadUrl("file:///android_asset/network_error.html")
                }
            }
        }
    }

    private fun interceptDefUrlLoading(url: String): Boolean {
        return when {
            url == "js://refreshUrl" -> {
                true
            }
            url.contains("/login_code.htm") && !isLogon -> {
                true
            }
            url.startsWith("tel:") || url.startsWith("sms:") || url.startsWith("smsto:") ||
                    url.startsWith("mmsto:") || url.startsWith("geo:") -> {
                true
            }
            else -> false
        }
    }

    abstract fun onStart(view: WebView, url: String)
    abstract fun interceptUrlLoading(view: WebView, url: String): Boolean
    abstract fun onEnd(view: WebView, url: String)
    private fun filterAction(url: String, view: WebView) {
        if (url.contains("js://jstojava?goBack")) {
            val goBack = Uri.parse(url).getBooleanQueryParameter("goBack", true)
            val refresh = Uri.parse(url).getBooleanQueryParameter("refresh", false)
            if (refresh) {
                LocalBroadcastManager.getInstance(AppUtils.getApp()).sendBroadcast(Intent().apply {
                    action = "${BuildConfig.APPLICATION_ID}.refresh"
                })
            }
            if (goBack) {
                (view.context as? Activity)?.finish()
            }
        } else if (url.contains("js://jstojava?platform")) {
            val uri = Uri.parse(url)
            val platform = uri.getQueryParameter("platform")
            val title = uri.getQueryParameter("title")
            val content = uri.getQueryParameter("content")
            val imageUrl = uri.getQueryParameter("imageUrl")
            val linkUrl = uri.getQueryParameter("linkUrl")
            if (!platform.isNullOrEmpty() && !title.isNullOrEmpty() && !content.isNullOrEmpty() && !imageUrl.isNullOrEmpty() && !linkUrl.isNullOrEmpty()) {
                showShareDialog(view, platform, title, content, imageUrl, linkUrl)
            }
        } else if (url.contains("js://jstojava?cityCode=")) {
            val cityCode = Uri.parse(url).getQueryParameter("cityCode")
            if (!cityCode.isNullOrEmpty()) {
                SPUtils.getInstance().put("cityCode", cityCode)
                LocalBroadcastManager.getInstance(AppUtils.getApp()).sendBroadcast(Intent().apply {
                    action = "${BuildConfig.APPLICATION_ID}.refresh"
                })
            }
        } else if (url == "js://refreshUrl") {
            view.goBack()
        } else if (url.contains("/login_code.htm") && !isLogon) {
            AppUtils.getApp().startActivity(Intent(AppUtils.getApp(), WebActivity::class.java).apply {
                putExtra("url", url)
                putExtra("isLogin", true)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } else if (url.startsWith("tel:") || url.startsWith("sms:")
            || url.startsWith("smsto:") || url.startsWith("mmsto:") || url.startsWith("geo:")
        ) {
            AppUtils.getApp().startActivity(Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
        } else {
            AppUtils.getApp().startActivity(Intent(view.context, WebActivity::class.java).apply {
                putExtra("url", url)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

}