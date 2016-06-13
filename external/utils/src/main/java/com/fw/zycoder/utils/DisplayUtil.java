package com.fw.zycoder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 */
public class DisplayUtil {

  public static int getScreenWidth(final Context context) {
    final DisplayMetrics metrics = getDisplayMetrics(context);
    return metrics.widthPixels;
  }

  public static int getScreenHeight(final Context context) {
    final DisplayMetrics metrics = getDisplayMetrics(context);
    return metrics.heightPixels;
  }

  public static boolean isPortrait(Context context) {
    return isPortrait(context.getResources().getConfiguration());
  }

  public static boolean isPortrait(Configuration newConfig) {
    if (newConfig != null && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      return false;
    }
    return true;
  }

  public static DisplayMetrics getDisplayMetrics(final Context context) {
    return context.getResources().getDisplayMetrics();
  }

  public static int getStatusBarHeight() {
    int result = 0;
    int resourceId = GlobalConfig.getAppContext().getResources().getIdentifier("status_bar_height",
        "dimen", "android");
    if (resourceId > 0) {
      result = GlobalConfig.getAppContext().getResources().getDimensionPixelSize(resourceId);
    }
    return result;
  }

  public static int getActionbarHeight(Activity activity) {
    if (activity == null) {
      return -1;
    }
    TypedValue tv = new TypedValue();
    if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
      int actionBarHeight =
          TypedValue.complexToDimensionPixelSize(tv.data, GlobalConfig.getAppContext()
              .getResources().getDisplayMetrics());
      return actionBarHeight;
    }
    return 0;
  }
}
