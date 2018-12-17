package com.milike.soft.utils;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import androidx.core.app.ActivityCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AndroidUtil {
    public static String UUID = null;//;"http://192.168.1.85:8080/appweb/push/query.htm?UI="+AndroidUtil.getUUID(MainActivityTest.class);

    public static String getUUID(Context ctx) {
        //1. The IMEI: 仅仅只对Android手机有效:
        TelephonyManager TelephonyMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String m_szImei = TelephonyMgr.getDeviceId();
//			return TODO;
        }
        String m_szImei = TelephonyMgr.getDeviceId();

        //2. Pseudo-Unique ID, 这个在任何Android手机中都有效
        String m_szDevIDShort = getDevIdShort();

        //3. The Android ID
        String m_szAndroidID = getAndroid(ctx);

        //4. The WLAN MAC Address string
        String m_szWLANMAC = getWlanmmac(ctx);

        //5. The BT MAC Address string
        String m_szBTMAC = getBtmac(ctx);

        String m_szLongID = getValue(m_szImei) + getValue(m_szDevIDShort) + getValue(m_szAndroidID) + getValue(m_szWLANMAC) + getValue(m_szBTMAC);

        // compute md5
        String m_szUniqueID = new String();
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
            // get md5 bytes
            byte p_md5Data[] = m.digest();
            // create a hex string
            for (int i = 0; i < p_md5Data.length; i++) {
                int b = (0xFF & p_md5Data[i]);
                // if it is a single digit, make sure it have 0 in front (proper
                // padding)
                if (b <= 0xF)
                    m_szUniqueID += "0";
                // add number to string
                m_szUniqueID += Integer.toHexString(b);
            } // hex string to uppercase
            m_szUniqueID = m_szUniqueID.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            m_szUniqueID = new String();
        }

        UUID = m_szUniqueID;

        return m_szUniqueID;
    }

    private static String getBtmac(Context ctx) {
        try {
            BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();     // Local Bluetooth adapter
            return m_BluetoothAdapter.getAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getWlanmmac(Context ctx) {
        try {
            WifiManager wm = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            return wm.getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getDevIdShort() {
        try {
            //13 digits
            return "35" + //we make this look like a valid IMEI
                    Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +
                    Build.CPU_ABI.length() % 10 +
                    Build.DEVICE.length() % 10 +
                    Build.DISPLAY.length() % 10 +
                    Build.HOST.length() % 10 +
                    Build.ID.length() % 10 +
                    Build.MANUFACTURER.length() % 10 +
                    Build.MODEL.length() % 10 +
                    Build.PRODUCT.length() % 10 +
                    Build.TAGS.length() % 10 +
                    Build.TYPE.length() % 10 +
                    Build.USER.length() % 10;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getAndroid(Context ctx) {
        try {
            return Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getValue(String str) {
        return str == null ? "" : str.trim();
    }
}