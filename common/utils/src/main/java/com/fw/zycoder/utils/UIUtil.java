
package com.fw.zycoder.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

public class UIUtil {
  private static Handler sHandler;

  public static interface Method<T> {
    T call();
  }

  /**
   * Retrieve the sHandler.
   *
   * @return the sHandler
   */
  public static Handler getHandler() {
    if (sHandler == null) {
      sHandler = new Handler(Looper.getMainLooper());
    }
    return sHandler;
  }

  @TargetApi(Build.VERSION_CODES.CUPCAKE)
  public static Thread getUIThread() {
    return Looper.getMainLooper().getThread();
  }

  public static boolean isOnUIThread() {
    return Thread.currentThread() == getUIThread();
  }

  public static void runOnUIThread(Runnable action) {
    if (!isOnUIThread()) {
      getHandler().post(action);
    } else {
      action.run();
    }
  }

  public static void postOnUIThread(Runnable action, long delay) {
    getHandler().postDelayed(action, delay);
  }

  public static boolean isMainProcess(Context context) {
    try {
      return context.getPackageName().equals(getProcessName(context));
    } catch (Exception e) {
      return false;
    }
  }

  @TargetApi(Build.VERSION_CODES.CUPCAKE)
  private static String getProcessName(Context context) {
    if (context != null) {
      int pid = android.os.Process.myPid();
      ActivityManager am = (ActivityManager) context
          .getSystemService(Context.ACTIVITY_SERVICE);
      List<ActivityManager.RunningAppProcessInfo> infoList = null;
      try {
        infoList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo apps : infoList) {
          if (apps.pid == pid) {
            return apps.processName;
          }
        }
      } catch (SecurityException e) {
        // isolatedProcess 进程可能不具备获取 getRunningAppProcesses 权限
      }
    }
    return "";
  }

}
