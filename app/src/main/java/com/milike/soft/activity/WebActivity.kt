package com.milike.soft.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import com.milike.soft.R
import com.milike.soft.base.BaseActivity
import com.milike.soft.utils.MiLikeJavascriptInterface
import com.milike.soft.utils.MiLikeWebViewClient
import com.milike.soft.utils.initWebViewSetting
import kotlinx.android.synthetic.main.layout_web_view.*

class WebActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_web_view)
        webView.settings.initWebViewSetting()
        webView.webViewClient = object : MiLikeWebViewClient(intent.getBooleanExtra("isLogin", false)) {
            override fun interceptUrlLoading(view: WebView, url: String): Boolean {
                Log.e("zuiwengxxxxxxx", url)
                return arrayListOf(
                    "js://jstojava?goBack",
                    "js://jstojava?platform"
                ).any { s -> url.contains(s) }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                loading.visibility = View.GONE
                super.onPageFinished(view, url)
            }
        }
        webView.addJavascriptInterface(MiLikeJavascriptInterface(webView), "android")
        loadUrl(intent.getStringExtra("url"))
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FADB28"), Color.parseColor("#FFDA00"))
        swipeRefreshLayout.isEnabled = false
        swipeRefreshLayout.setOnRefreshListener {
            webView.reload()
            swipeRefreshLayout.isRefreshing = false
            loading.visibility = View.VISIBLE
        }
    }

    private fun loadUrl(data: String) {
        loading.visibility = View.VISIBLE
        webView.loadUrl(data)
    }
}