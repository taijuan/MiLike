package com.milike.soft.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.milike.soft.R
import com.milike.soft.base.BaseActivity
import com.milike.soft.utils.MiLikeJavascriptInterface
import com.milike.soft.utils.MiLikeWebViewClient
import com.milike.soft.utils.getWebUrlSuffix
import com.milike.soft.utils.initWebViewSetting
import kotlinx.android.synthetic.main.layout_web_view.*

class WebActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_web_view)
        webView.settings.initWebViewSetting()
        webView.webViewClient = object : MiLikeWebViewClient(intent.getBooleanExtra("isLogin", false)) {
            override fun onStart(view: WebView, url: String) {
                loading?.visibility = View.VISIBLE
            }

            override fun onEnd(view: WebView, url: String) {
                loading?.visibility = View.GONE
            }

            override fun interceptUrlLoading(view: WebView, url: String): Boolean {
                return arrayListOf(
                    "js://jstojava?goBack",
                    "js://jstojava?platform"
                ).any { s -> url.contains(s) }
            }
        }
        webView.addJavascriptInterface(MiLikeJavascriptInterface(webView), "android")
        swipeRefreshLayout.isEnabled = false
        loadUrl(intent.getStringExtra("url") ?: "")
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FADB28"), Color.parseColor("#FFDA00"))
        swipeRefreshLayout.setOnRefreshListener {
            webView.reload()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun loadUrl(url: String) {
        if (url.startsWith("http") || url.startsWith("https") || url.startsWith("file:///android_asset/")) {
            webView.loadUrl(url)
        } else {
            webView.loadData(url, "text/html", "UTF-8")
        }
    }

    override fun loadUrl() {
        webView.loadUrl(getWebUrlSuffix(webView.url))
    }

    override fun onResume() {
        webView.onResume()
        super.onResume()
    }

    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        webView?.also {
            webViewContent?.removeView(it)
            it.removeAllViews()
            it.destroy()
        }
        super.onDestroy()
    }
}