package com.milike.soft.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.blankj.utilcode.util.SPUtils
import com.milike.soft.BuildConfig.DNS
import com.milike.soft.R
import com.milike.soft.base.BaseFragment
import com.milike.soft.utils.MiLikeJavascriptInterface
import com.milike.soft.utils.MiLikeWebViewClient
import com.milike.soft.utils.getWebUrlSuffix
import com.milike.soft.utils.initWebViewSetting
import kotlinx.android.synthetic.main.layout_web_view.*

class HomeFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.layout_web_view, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.settings.initWebViewSetting()
        webView.webViewClient = object : MiLikeWebViewClient() {
            override fun onStart(view: WebView, url: String) {
                loading?.visibility = View.VISIBLE
            }

            override fun onEnd(view: WebView, url: String) {
                loading?.visibility = View.GONE
            }

            override fun interceptUrlLoading(view: WebView, url: String): Boolean {
                return arrayListOf(
                    "/link",
                    "/infmt/index/zx",
                    "/estate/-pt-0",
                    "/yfzzf/1",
                    "/houseList/-pn-1-ps-6.htm",
                    "/map/",
                    "/estate/-pt-1",
                    "/theme/",
                    "/xprt/",
                    "/estate/detail/index/",
                    "/search",
                    "/mysub.htm",
                    "js://jstojava?cityCode=",
                    "/artifact/"
                ).any { s -> url.contains(s) }
            }
        }

        webView.addJavascriptInterface(MiLikeJavascriptInterface(webView), "android")
        webView.setOnScrollTop {
            swipeRefreshLayout.isEnabled = it == 0
        }
        loadUrl()
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FADB28"), Color.parseColor("#FFDA00"))
        swipeRefreshLayout.setOnRefreshListener {
            loadUrl()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun loadUrl() {
        val cityCode = SPUtils.getInstance().getString("cityCode", "sz")
        webView.loadUrl(getWebUrlSuffix("$DNS$cityCode/"))
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