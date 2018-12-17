package com.milike.soft.utils

import android.webkit.WebView
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.tencent.qzone.QZone
import cn.sharesdk.wechat.friends.Wechat
import cn.sharesdk.wechat.moments.WechatMoments
import java.util.*

fun showShareDialog(
    webView: WebView,
    platform: String,
    title: String,
    content: String,
    imageUrl: String,
    linkUrl: String
) {
    val sp = Platform.ShareParams()
    sp.imageUrl = imageUrl
    sp.title = title
    sp.text = content
    sp.shareType = Platform.SHARE_WEBPAGE
    val share = when (platform) {
        "wxmoments" -> {
            sp.url = linkUrl
            ShareSDK.getPlatform(WechatMoments.NAME)
        }
        "qq" -> {
            sp.titleUrl = linkUrl
            ShareSDK.getPlatform(QQ.NAME)
        }
        "qqzone" -> {
            sp.titleUrl = linkUrl
            ShareSDK.getPlatform(QZone.NAME)
        }
        else -> {
            sp.url = linkUrl
            ShareSDK.getPlatform(Wechat.NAME)
        }
    }
    share.platformActionListener = object : PlatformActionListener {
        override fun onError(arg0: Platform, arg1: Int, arg2: Throwable) {
            webView.loadUrl("javascript:shareResult('false')")
        }

        override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {
            webView.loadUrl("javascript:shareResult('true')")
        }

        override fun onCancel(arg0: Platform, arg1: Int) {
            webView.loadUrl("javascript:shareResult('false')")
        }
    }
    share.share(sp)
}