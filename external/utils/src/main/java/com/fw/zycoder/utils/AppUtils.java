package com.fw.zycoder.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.util.List;

/**
 * 跟App相关的辅助类
 */
public class AppUtils {

  private AppUtils() {
    /* cannot be instantiated */
    throw new UnsupportedOperationException("cannot be instantiated");

  }

  /**
   * 获取应用程序名称
   */
  public static String getAppName(Context context) {
    try {
      PackageManager packageManager = context.getPackageManager();
      PackageInfo packageInfo = packageManager.getPackageInfo(
          context.getPackageName(), 0);
      int labelRes = packageInfo.applicationInfo.labelRes;
      return context.getResources().getString(labelRes);
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static int getVersionCode(Context context) {
    try {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context
          .getPackageName(), PackageManager.GET_ACTIVITIES);
      return packageInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static String getVersionName(Context context) {
    try {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context
          .getPackageName(), PackageManager.GET_ACTIVITIES);
      return packageInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static boolean isBackground(Context context) {
    ActivityManager activityManager = (ActivityManager) context
            .getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
            .getRunningAppProcesses();
    for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
      if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
        Log.i(context.getPackageName(), "此appimportace ="
                + appProcess.importance
                + ",context.getClass().getName()="
                + context.getClass().getName());
        if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
          Log.i(context.getPackageName(), "处于后台"
                  + appProcess.processName);
          return true;
        } else {
          Log.i(context.getPackageName(), "处于前台"
                  + appProcess.processName);
          return false;
        }
      }
    }
    return false;
  }

}
