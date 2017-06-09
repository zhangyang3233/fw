package debug.xly.com.debugkit.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import debug.xly.com.debugkit.BuildConfig;
import debug.xly.com.debugkit.R;

/**
 * Created by zhangyang131 on 2017/5/26.
 */

public class L {
  private static final String TAG = "log";
  private static boolean isPrintLog;// 是否需 要打印log

  public static void init(Context context) {
    isPrintLog = PreferenceManager.getDefaultSharedPreferences(context)
        .getBoolean(context.getString(R.string.print_log), BuildConfig.DEBUG);
  }

  public static void setPrintLog(Context context, boolean value) {
    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    editor.putBoolean(context.getString(R.string.print_log), value);
    editor.commit();
    isPrintLog = value;
  }

  private L() {
    /* cannot be instantiated */
    throw new UnsupportedOperationException("cannot be instantiated");
  }

  public static boolean isPrintLog() {
    return isPrintLog;
  }

  // 下面四个是默认tag的函数
  public static void w(String msg) {
    if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(msg.trim())) {
      return;
    }
    if (isPrintLog)
      android.util.Log.w(TAG, msg);
  }

  // 下面四个是默认tag的函数
  public static void i(String msg) {
    if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(msg.trim())) {
      return;
    }
    if (isPrintLog)
      android.util.Log.i(TAG, msg);
  }

  public static void d(String msg) {
    if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(msg.trim())) {
      return;
    }
    if (isPrintLog)
      android.util.Log.d(TAG, msg);
  }

  public static void e(String msg) {
    if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(msg.trim())) {
      return;
    }
    if (isPrintLog)
      android.util.Log.e(TAG, msg);
  }

  public static void v(String msg) {
    if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(msg.trim())) {
      return;
    }
    if (isPrintLog)
      android.util.Log.v(TAG, msg);
  }

  // 下面是传入自定义tag的函数
  public static void w(String tag, String msg) {
    if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(msg.trim())) {
      return;
    }
    if (isPrintLog)
      android.util.Log.w(tag, msg);
  }

  public static void i(String tag, String msg) {
    if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(msg.trim())) {
      return;
    }
    if (isPrintLog)
      android.util.Log.i(tag, msg);
  }

  public static void d(String tag, String msg) {
    if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(msg.trim())) {
      return;
    }
    if (isPrintLog)
      android.util.Log.d(tag, msg);
  }

  public static void e(String tag, String msg) {
    if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(msg.trim())) {
      return;
    }
    if (isPrintLog)
      android.util.Log.e(tag, msg);
  }

  public static void v(String tag, String msg) {
    if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(msg.trim())) {
      return;
    }
    if (isPrintLog)
      android.util.Log.v(tag, msg);
  }
}
