package com.fw.zycoder.utils;

import android.content.Context;


/**
 * @author liuxu34@wanda.cn (Liu Xu)
 */
public class GlobalConfig {
  private static Context appContext;
  private static String rootDir = "rootDir";
  private static boolean debug = true;

  public static void setAppContext(Context context) {
    appContext = context;
  }

  public static Context getAppContext() {
    return appContext;
  }

  public static void setAppRootDir(String dir) {
    rootDir = dir;
  }

  public static String getAppRootDir() {
    return rootDir;
  }


  public static void setDebug(boolean debug) {
    GlobalConfig.debug = debug;
  }


  public static boolean isDebug() {
    return debug;
  }
}
