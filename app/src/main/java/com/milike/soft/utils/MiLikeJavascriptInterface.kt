package com.milike.soft.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.milike.soft.BuildConfig
import com.milike.soft.activity.WebActivity
import org.json.JSONObject

class MiLikeJavascriptInterface(private val webView: WebView) {
    @JavascriptInterface
    fun JsToJavaShare(jsonStr: String) {
        try {
            val json = JSONObject(jsonStr)
            val platform = json.getString("platform")
            val title = json.getString("title")
            val content = json.getString("content")
            val imageUrl = json.getString("imageUrl")
            val linkUrl = json.getString("linkUrl")
            if (!platform.isNullOrEmpty() && !title.isNullOrEmpty() && !content.isNullOrEmpty() && !imageUrl.isNullOrEmpty() && !linkUrl.isNullOrEmpty()) {
                showShareDialog(webView, platform, title, content, imageUrl, linkUrl)
            }
        } catch (e: Exception) {
        }
    }

    //<button onclick="window.android.nextPage('htts://baidu.com')">跳转界面</button>
    //<button onclick="window.android.nextPage('tel:15528363539')">拨打电话</button>
    @JavascriptInterface
    fun nextPage(url: String) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            AppUtils.getApp().startActivity(Intent(AppUtils.getApp(), WebActivity::class.java).apply {
                putExtra("url", getWebUrlSuffix(url))
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } else {
            AppUtils.getApp().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    //<button onclick="window.android.goBack()">返回上一个url</button>
    @JavascriptInterface
    fun goBack() {
        webView?.post {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                val activity = webView.context
                if (activity is Activity) {
                    activity.finish()
                }
            }
        }
    }

    //<button onclick="window.android.finish()">关闭H5容器</button>
    @JavascriptInterface
    fun finish() {
        val activity = webView.context
        if (activity is Activity) {
            activity.finish()
        }
    }

    //<button onclick="window.android.share('json字符串')">分享</button>
    @JavascriptInterface
    fun share(jsonStr: String) {
        try {
            val json = JSONObject(jsonStr)
            val platform = json.getString("platform")
            val title = json.getString("title")
            val content = json.getString("content")
            val imageUrl = json.getString("imageUrl")
            val linkUrl = json.getString("linkUrl")
            if (!platform.isNullOrEmpty() && !title.isNullOrEmpty() && !content.isNullOrEmpty() && !imageUrl.isNullOrEmpty() && !linkUrl.isNullOrEmpty()) {
                showShareDialog(webView, platform, title, content, imageUrl, linkUrl)
            }
        } catch (e: Exception) {
        }
    }

    //<button onclick="window.android.login('token字符串')">登录</button>
    @JavascriptInterface
    fun login(token: String) {
        SPUtils.getInstance().put("token", token)
        val activity = webView.context
        if (activity is Activity) {
            activity.finish()
        }
        LocalBroadcastManager.getInstance(AppUtils.getApp()).sendBroadcast(Intent().apply {
            action = "${BuildConfig.APPLICATION_ID}.refresh"
        })
    }

    //<button onclick="window.android.logout()">退出登录</button>
    @JavascriptInterface
    fun logout() {
        SPUtils.getInstance().put("token", "")
        val activity = webView.context
        if (activity is Activity) {
            activity.finish()
        }
        LocalBroadcastManager.getInstance(AppUtils.getApp()).sendBroadcast(Intent().apply {
            action = "${BuildConfig.APPLICATION_ID}.refresh"
        })
    }

    //<button onclick="window.android.cityCode('sh')">退出登录</button>
    @JavascriptInterface
    fun cityCode(cityCode: String) {
        if (!cityCode.isEmpty()) {
            SPUtils.getInstance().put("cityCode", cityCode)
            LocalBroadcastManager.getInstance(AppUtils.getApp()).sendBroadcast(Intent().apply {
                action = "${BuildConfig.APPLICATION_ID}.refresh"
            })
        }
    }

    @JavascriptInterface
    fun getToken(): String {
        return SPUtils.getInstance().getString("token")
    }
}