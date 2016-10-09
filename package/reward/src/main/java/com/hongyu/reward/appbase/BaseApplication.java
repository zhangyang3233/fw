package com.hongyu.reward.appbase;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.fw.zycoder.errorpage.CustomActivityOnCrash;
import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.Log;
import com.hongyu.reward.BuildConfig;
import com.hongyu.reward.manager.CoreService;
import com.hongyu.reward.manager.CustomMessageHandler;
import com.hongyu.reward.manager.CustomNotificationClickHandler;
import com.hongyu.reward.manager.LocationManager;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;


/**
 * Created by zhangyang131 on 16/6/12.
 */
public class BaseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    long t1 = System.currentTimeMillis();
    initLeakCanary();
    // initErrorPage();
    initGlobalConfig();
    initLog();
    LocationManager.getInstance().init(this);
    LocationManager.getInstance().start();
    startCoreService();
    initPush();
  }

  private void initPush() {
    PushAgent mPushAgent = PushAgent.getInstance(this);
    // 注册推送服务，每次调用register方法都会回调该接口
    mPushAgent.register(new IUmengRegisterCallback() {

      @Override
      public void onSuccess(String deviceToken) {
        // 注册成功会返回device token
        GlobalConfig.setDeviceToken(deviceToken);
        Log.e("push", deviceToken);
      }

      @Override
      public void onFailure(String s, String s1) {
        Log.e("push", s + " & " + s1);
      }
    });
    mPushAgent.setDebugMode(BuildConfig.IS_SHOW_LOG);
    UmengMessageHandler umengMessageHandler = new CustomMessageHandler();
    UmengNotificationClickHandler notificationClickHandler = new CustomNotificationClickHandler();
    mPushAgent.setMessageHandler(umengMessageHandler);
    mPushAgent.setNotificationClickHandler(notificationClickHandler);
  }


  private void initLeakCanary() {
    LeakCanary.install(this);
  }


  /**
   * 初始化log类
   */
  private void initLog() {
    Log.setIsPrintLog(BuildConfig.IS_SHOW_LOG);
  }

  /**
   * 初始化全局变量
   */
  private void initGlobalConfig() {
    GlobalConfig.setAppContext(this);
    GlobalConfig.setDebug(BuildConfig.DEBUG);
  }

  /**
   * 初始化崩溃页面
   */
  private void initErrorPage() {
    CustomActivityOnCrash.install(this);
    // 程序在后台崩溃是否显示错误页面
    CustomActivityOnCrash.setLaunchErrorActivityWhenInBackground(false);
    // 是否显示错误详细信息
    CustomActivityOnCrash.setShowErrorDetails(BuildConfig.DEBUG);
    // 是否打印log日志
    CustomActivityOnCrash.setShowErrorLog(BuildConfig.DEBUG);
  }

  /**
   * 启动核心服务
   *
   * @auther centos
   * @tags
   */
  public void startCoreService() {
    Intent intent = new Intent(this, CoreService.class);
    startService(intent);
  }
}
