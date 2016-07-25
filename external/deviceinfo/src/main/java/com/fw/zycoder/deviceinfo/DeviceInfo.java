package com.fw.zycoder.deviceinfo;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by zhangyang131 on 16/7/25.
 */
public class DeviceInfo {
    public static String deviceId;
    public static float screenWidth;
    public static float screenHight;
    public static NetWorkType networkType;
    public static String operatorName;
    public static String brand;
    public static String line1Number;
    public static String deviceSoftwareVersion;
    public static String deviceName;
    public static String language;
    public static String osVersion;
    public static String deviceDesc;
    public static String phoneWifiMac;
    public static String routerMac;


    enum NetWorkType{
        TYPE_3G,TYPE_4G,TYPE_WIFI,TYPE_NONE,TYPE_UNKNOWN
    }

    /**
     * get mobile operators
     *
     * @param context
     * @return
     */
    public static String getMobileOperators(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String operatorString = telephonyManager.getSimOperator();
        if (operatorString == null) {
            return "未知";
        }
        if (operatorString.equals("46000") || operatorString.equals("46002")) {
            // 中国移动 1
            return "中国移动";
        } else if (operatorString.equals("46001")) {
            // 中国联通
            return "中国联通";
        } else if (operatorString.equals("46003")) {
            // 中国电信
            return "中国电信";
        }
        // error
        return "未知";
    }
}
