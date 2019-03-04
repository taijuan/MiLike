package com.milike.soft.utils;

public class BarUtils {
    public static int getBarHeightToDP() {
        try {
            int resourceId = AppUtils.getApp().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return AppUtils.getApp().getResources().getDimensionPixelSize(resourceId);
            } else {
                return toPixFrom24Dip();
            }
        } catch (Exception e) {
            return toPixFrom24Dip();
        }
    }

    private static float getDensity() {
        return AppUtils.getApp().getResources().getDisplayMetrics().density;
    }

    private static int toPixFrom24Dip() {
        float scale = getDensity();
        return (int) (24 * scale + 0.5f);
    }
}
