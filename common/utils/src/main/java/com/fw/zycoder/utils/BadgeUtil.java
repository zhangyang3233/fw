package com.fw.zycoder.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.util.Iterator;

public class BadgeUtil {

  /**
   * Set badge count
   * 针对 Samsung / xiaomi / sony 手机有效
   *
   * @param context The context of the application package.
   * @param count Badge count to be set
   */
  public static void setBadgeCount(Context context, int count) {
    if (count < 0) {
      return;
    }

    if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
      sendToXiaoMi(context, count);
    } else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
      sendToSony(context, count);
    } else if (Build.MANUFACTURER.toLowerCase().contains("samsung")) {
      sendToSamsumg(context, count);
    } else if (Build.MANUFACTURER.toLowerCase().contains("huawei")) {
      sendToHuawei(context, count);
    } else {

    }
  }


  /**
   * 向小米手机发送未读消息数广播
   *
   * @param count
   */
  private static void sendToXiaoMi(Context context, int count) {
    try {
      Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
      Object miuiNotification = miuiNotificationClass.newInstance();
      Field field = miuiNotification.getClass().getDeclaredField("messageCount");
      field.setAccessible(true);
      field.set(miuiNotification, String.valueOf(count == 0 ? "" : count)); // 设置信息数-->这种发送必须是miui
                                                                            // 6才行
    } catch (Exception e) {
      e.printStackTrace();
      // miui 6之前的版本
      Intent localIntent = new Intent(
          "android.intent.action.APPLICATION_MESSAGE_UPDATE");
      localIntent.putExtra(
          "android.intent.extra.update_application_component_name",
          context.getPackageName() + "/" + getLaunchActivityName(context));
      localIntent.putExtra(
          "android.intent.extra.update_application_message_text",
          String.valueOf(count == 0 ? "" : count));
      context.sendBroadcast(localIntent);
    }
  }


  /**
   * 向索尼手机发送未读消息数广播<br/>
   * 据说：需添加权限：
   * <uses-permission android:name="com.sonyericsson.home.permission.BROADCAST_BADGE" /> [未验证]
   *
   * @param count
   */
  private static void sendToSony(Context context, int count) {
    String launcherClassName = getLaunchActivityName(context);
    if (launcherClassName == null) {
      return;
    }

    boolean isShow = true;
    if (count == 0) {
      isShow = false;
    }
    Intent localIntent = new Intent();
    localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);// 是否显示
    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",
        launcherClassName);// 启动页
    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));// 数字
    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME",
        context.getPackageName());// 包名
    context.sendBroadcast(localIntent);
  }

  /**
   * 向三星手机发送未读消息数广播
   *
   * @param count
   */
  private static void sendToSamsumg(Context context, int count) {
    String launcherClassName = getLaunchActivityName(context);
    if (launcherClassName == null) {
      return;
    }
    Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
    intent.putExtra("badge_count", count);
    intent.putExtra("badge_count_package_name", context.getPackageName());
    intent.putExtra("badge_count_class_name", launcherClassName);
    context.sendBroadcast(intent);
  }

  /**
   * 向华为手机发送未度广播
   * 需要添加权限:
   * <uses-permission android:name="com.huawei.android.launcher.permission.CHANGE_BADGE"/>
   * <uses-permission android:name="com.huawei.permission.sec.MDM"/>
   * 需要申请证书:
   * <a>http://developer.huawei.com/wiki/index.php?title=%E5%8D%8E%E4%B8%BA%E6%A1%8C%E9%9D%A2%E8%A7%
   * 92%E6%A0%87%E5%BC%80%E5%8F%91%E6%8C%87%E5%AF%BC%E4%B9%A6</a>
   * 
   * @param context
   * @param count
   */
  public static void sendToHuawei(Context context, int count) {
    boolean isSupportedBade = false;
    try {
      PackageManager manager = context.getPackageManager();
      PackageInfo info = manager.getPackageInfo("com.huawei.android.launcher",
          0);
      if (info.versionCode >= 63029) {
        isSupportedBade = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (isSupportedBade) {
      Bundle extra = new Bundle();
      extra.putString("package", context.getPackageName());
      extra.putString("class", getLaunchActivityName(context));
      extra.putInt("badgenumber", count);
      try {
        context.getContentResolver().call(
            Uri.parse("content://com.huawei.android.launcher.settings/badge/"),
            "change_launcher_badge", null, extra);
      } catch (SecurityException e) {// 未获得权限,详情见华为开发角标文档
      }

    }
  }


  /**
   * 重置、清除Badge未读显示数<br/>
   *
   * @param context
   */
  public static void resetBadgeCount(Context context) {
    setBadgeCount(context, 0);
  }


  /**
   * Retrieve launcher activity name of the application from the context
   *
   * @param context The context of the application package.
   * @return launcher activity name of this application. From the
   *         "android:name" attribute.
   */
  public static String getLaunchActivityName(Context context) {
    PackageManager localPackageManager = context.getPackageManager();
    Intent localIntent = new Intent(Intent.ACTION_MAIN);
    localIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    try {
      Iterator<ResolveInfo> localIterator =
          localPackageManager.queryIntentActivities(localIntent, 0).iterator();
      while (localIterator.hasNext()) {
        ResolveInfo localResolveInfo = localIterator.next();
        if (!localResolveInfo.activityInfo.applicationInfo.packageName
            .equalsIgnoreCase(context.getPackageName()))
          continue;
        String str = localResolveInfo.activityInfo.name;
        return str;
      }
    } catch (Exception localException) {
      return null;
    }
    return null;
  }
}
