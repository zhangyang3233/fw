package debug.xly.com.debugkit.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyang131 on 2017/5/25.
 */

public class KitActivityManager {
  private static final String TAG = KitActivityManager.class.getSimpleName();
  private static Activity currentActivity;
  private static Application application;
  private static int mOpenActivityCount;
  private static ArrayList<OnAppBackgroundListener> observer = new ArrayList<>();

  public static Activity getCurrentActivity() {
    if (isAppOnForeground()) {
      return currentActivity;
    }
    return null;
  }

  public static void registerAppbackgroundListener(OnAppBackgroundListener l){
    if(!observer.contains(l)){
      observer.add(l);
    }
  }

  public static void unRegisterAppbackgroundListener(OnAppBackgroundListener l){
      observer.remove(l);
  }


  public static void init(Application app) {
    application = app;
    application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
      @Override
      public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.d("YWK", activity + "onActivityCreated");
      }

      @Override
      public void onActivityStarted(Activity activity) {
        Log.d("YWK", activity + "onActivityStarted");
        currentActivity = activity;
        mOpenActivityCount++;
        // 如果mFinalCount ==1，说明是从后台到前台
        Log.e("onActivityStarted", mOpenActivityCount + "");
        if (mOpenActivityCount == 1) {
          // 说明从后台回到了前台
          notifyOnForeground();
        }
      }

      @Override
      public void onActivityResumed(Activity activity) {

      }

      @Override
      public void onActivityPaused(Activity activity) {

      }

      @Override
      public void onActivityStopped(Activity activity) {
        mOpenActivityCount--;
        // 如果mFinalCount ==0，说明是前台到后台
        Log.i("onActivityStopped", mOpenActivityCount + "");
        if (mOpenActivityCount == 0) {
          // 说明从前台回到了后台
          notifyOnBackground();
        }
      }

      @Override
      public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

      }

      @Override
      public void onActivityDestroyed(Activity activity) {

      }
    });
  }

  private static void notifyOnBackground() {
    L.v(TAG, "app进入后台");
    if (observer.isEmpty()) {
      return;
    }
    for (OnAppBackgroundListener l : observer) {
      l.onBackground();
    }
  }

  private static void notifyOnForeground() {
    L.v(TAG, "app进入前台");
    if (observer.isEmpty()) {
      return;
    }
    for (OnAppBackgroundListener l : observer) {
      l.onForeground();
    }
  }


  public interface OnAppBackgroundListener {
    void onBackground();

    void onForeground();
  }


  /**
   * APP是否处于前台唤醒状态
   *
   * @return
   */
  public static boolean isAppOnForeground() {
    ActivityManager activityManager = (ActivityManager) (application
        .getSystemService(Context.ACTIVITY_SERVICE));
    String packageName = application.getPackageName();
    List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
        .getRunningAppProcesses();
    if (appProcesses == null)
      return false;
    for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
      if (appProcess.processName.equals(packageName)
          && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
        return true;
      }
    }
    return false;
  }
}
