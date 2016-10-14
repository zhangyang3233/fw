package com.fw.zycoder.utils;

import android.content.Context;
import android.text.TextUtils;


/**
 */
public class GlobalConfig {
  private static Context appContext;
  private static String rootDir = "rootDir";
  private static String pushCode;
  private static String token;
  private static boolean debug = true;

  public static Context getAppContext() {
    return appContext;
  }

  public static void setAppContext(Context context) {
    appContext = context;
  }

  public static String getAppRootDir() {
    return rootDir;
  }

  public static void setAppRootDir(String dir) {
    rootDir = dir;
  }

  public static boolean deviceToken() {
    return debug;
  }

  public static void setDebug(boolean debug) {
    GlobalConfig.debug = debug;
  }

  public static String getPushCode() {
    if(TextUtils.isEmpty(pushCode)){
      pushCode = getLocalPushCode();
    }
    return pushCode;
  }

  public static void setPushCode(String pushCode) {
    if(TextUtils.isEmpty(pushCode)){
      return;
    }
    GlobalConfig.pushCode = pushCode;
    savePushCode();
  }

  private static void savePushCode() {
    if(!TextUtils.isEmpty(GlobalConfig.pushCode)){
      SPUtil.putString("PushCode", GlobalConfig.pushCode);
    }
  }

  public static String getLocalPushCode(){
    return SPUtil.getString("PushCode", "");
  }

  public static String getToken() {
    return token;
  }

  public static void setToken(String token) {
    GlobalConfig.token = token;
  }
}
