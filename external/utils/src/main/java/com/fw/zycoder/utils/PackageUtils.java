package com.fw.zycoder.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 */
public class PackageUtils {

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static ApkPackageInfo getApkPakcageInfo(Context context, String apkPath) {
    ApkPackageInfo apkInfo = new ApkPackageInfo();
    try {
      PackageInfo pkgInfo = context.getPackageManager().getPackageArchiveInfo(apkPath, 0);
      if (pkgInfo == null) {
        return null;
      }
      apkInfo.packageName = pkgInfo.packageName;
      apkInfo.versionCode = pkgInfo.versionCode;
      apkInfo.versionName = pkgInfo.versionName;
      CharSequence nameCharSeq = pkgInfo.applicationInfo.loadLabel(context.getPackageManager());
      apkInfo.name = nameCharSeq == null ? "" : nameCharSeq.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return apkInfo;
  }

  public static String readApkAssetFile(String apkPath, String filePath) {
    BufferedInputStream input = null;
    try {
      String classAssetManager = "android.content.res.AssetManager";
      Class assetMgrCls = Class.forName(classAssetManager);
      Constructor assetMgrCt = assetMgrCls.getConstructor((Class[]) null);
      AssetManager assetMgr = (AssetManager) assetMgrCt.newInstance();
      Method addAssetPath =
          assetMgrCls.getDeclaredMethod("addAssetPath", new Class[] {String.class});
      addAssetPath.invoke(assetMgr, new Object[] {apkPath});
      input = new BufferedInputStream(assetMgr.open(filePath));
      byte[] buffer = new byte[input.available()];
      int count = input.read(buffer);
      return new String(buffer, 0, count);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (input != null) {
          input.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return "";
  }

  public static class ApkPackageInfo {
    public String name;
    public String packageName;
    public int versionCode = -1;
    public String versionName;
  }

  public static String getVersion(Context context) {
    String version = "1.0.0";

    PackageManager packageManager = context.getPackageManager();
    try {
      PackageInfo packageInfo = packageManager.getPackageInfo(
              context.getPackageName(), 0);
      version = packageInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return version;
  }

  public static int getVersionCode(Context context) {
    int version = 1000;
    PackageManager packageManager = context.getPackageManager();
    try {
      PackageInfo packageInfo = packageManager.getPackageInfo(
          context.getPackageName(), 0);
      version = packageInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return version;
  }

  public static String getPackageName(Context context) {
    String packageName = "com.wanda.sdk";
    PackageManager packageManager = context.getPackageManager();
    try {
      PackageInfo packageInfo = packageManager.getPackageInfo(
          context.getPackageName(), 0);
      packageName = packageInfo.packageName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return packageName;
  }

  public static PackageInfo getPackageInfo(Context context) {
    try {
      PackageManager pm = context.getPackageManager();
      return pm.getPackageInfo(context.getPackageName(), 0);
    } catch (Exception e) {
    }

    return null;
  }

  /*
   * require <uses-permission android:name="android.permission.GET_TASKS" />
   */
  public static boolean isMyPackageRunningOnTop(Context context) {
    ActivityManager am =
        (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    if (am == null) {
      return false;
    }
    List<ActivityManager.RunningTaskInfo> infos = am.getRunningTasks(1);
    if (infos != null && !infos.isEmpty()) {
      ActivityManager.RunningTaskInfo info = infos.get(0);
      ComponentName componentName = info.topActivity;
      if (componentName != null
          && componentName.getPackageName().equals(context.getPackageName())) {
        return true;
      }
    }
    return false;
  }

}
