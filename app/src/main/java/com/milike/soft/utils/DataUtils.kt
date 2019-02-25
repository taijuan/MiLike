package com.milike.soft.utils

import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.SPUtils

fun getWebUrlSuffix(url: String): String {
    return StringBuilder(url).append(if (url.contains("?")) "&" else "?")
        .append("flag").append("=").append("milike").append("&")
        .append("mid").append("=").append(DeviceUtils.getAndroidID()).append("&")
        .append("tm").append("=").append(System.currentTimeMillis()).append("&")
        .append("appPlatform").append("=").append("android").append("&")
        .append("initial").append("=").append(SPUtils.getInstance().getInt("versionCode") == -1).append("&")
        .append("uuid").append("=").append(SPUtils.getInstance().getString("uuid")).append("&")
        .append("token").append("=").append(SPUtils.getInstance().getString("token"))
        .toString()
}
