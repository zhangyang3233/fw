package com.fw.zycoder.utils;


/**
 * Log统一管理类
 */
public class Log {

  private Log() {
    /* cannot be instantiated */
    throw new UnsupportedOperationException("cannot be instantiated");
  }

  private static boolean isPrintLog = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
  private static final String TAG = "log";

  public static void setIsPrintLog(boolean isPrintLog) {
    Log.isPrintLog = isPrintLog;
  }

  // 下面四个是默认tag的函数
  public static void i(String msg) {
    if (isPrintLog)
      android.util.Log.i(TAG, msg);
  }

  public static void d(String msg) {
    if (isPrintLog)
      android.util.Log.d(TAG, msg);
  }

  public static void e(String msg) {
    if (isPrintLog)
      android.util.Log.e(TAG, msg);
  }

  public static void v(String msg) {
    if (isPrintLog)
      android.util.Log.v(TAG, msg);
  }

  // 下面是传入自定义tag的函数
  public static void i(String tag, String msg) {
    if (isPrintLog)
      android.util.Log.i(tag, msg);
  }

  public static void d(String tag, String msg) {
    if (isPrintLog)
      android.util.Log.i(tag, msg);
  }

  public static void e(String tag, String msg) {
    if (isPrintLog)
      android.util.Log.i(tag, msg);
  }

  public static void v(String tag, String msg) {
    if (isPrintLog)
      android.util.Log.i(tag, msg);
  }
}
