package com.milike.soft.utils

import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.SPUtils

fun getWebUrlSuffix(url: String): String {
    val position = SPUtils.getInstance().getString("position")
    return url + (if (url.contains("?")) "&" else "?") + "flag=milike" + position + getUIDAndBasicInfo()
}

private fun getUIDAndBasicInfo(): String {
    val first = com.blankj.utilcode.util.SPUtils.getInstance().getInt("versionCode")
    return "&mid=${DeviceUtils.getAndroidID()}&tm=${System.currentTimeMillis()}&appPlatform=android&initial=${first == -1}&uuid=${SPUtils.getInstance().getString(
        "uuid"
    )}"
}