package com.milike.soft.utils

import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.SPUtils

fun getWebUrlSuffix(url: String): String {
    val position = SPUtils.getInstance().getString("position")
    return url + (if (url.contains("?")) "&" else "?") + "flag=milike" + position + getUIDAndBasicInfo()
}

private fun getUIDAndBasicInfo(): String {
    val first = com.blankj.utilcode.util.SPUtils.getInstance().getInt("versionCode")
    return if (first == 0) {
        "&mid=" + DeviceUtils.getAndroidID() + "&tm=" + System.currentTimeMillis() + "&appPlatform=android&initial=true"
    } else {
        "&mid=" + DeviceUtils.getAndroidID() + "&tm=" + System.currentTimeMillis() + "&appPlatform=android&initial=false"
    }

}