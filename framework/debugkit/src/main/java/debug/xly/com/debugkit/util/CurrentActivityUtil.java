package debug.xly.com.debugkit.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by zhangyang131 on 2017/5/25.
 */

public class CurrentActivityUtil {
  private static Activity currentActivity;

  public static Activity getCurrentActivity() {
    if(currentActivity != null && currentActivity.hasWindowFocus()){
      return currentActivity;
    }
    return null;
  }

  public static void init(Application app) {
    app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
      @Override
      public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.d("YWK", activity + "onActivityCreated");
      }

      @Override
      public void onActivityStarted(Activity activity) {
        Log.d("YWK", activity + "onActivityStarted");
        currentActivity = activity;

      }

      @Override
      public void onActivityResumed(Activity activity) {

      }

      @Override
      public void onActivityPaused(Activity activity) {

      }

      @Override
      public void onActivityStopped(Activity activity) {}

      @Override
      public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

      }

      @Override
      public void onActivityDestroyed(Activity activity) {

      }
    });
  }
}
