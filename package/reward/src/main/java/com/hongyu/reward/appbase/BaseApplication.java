package com.hongyu.reward.appbase;

import android.app.Application;
import android.content.Intent;

import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.Log;
import com.hongyu.reward.BuildConfig;
import com.hongyu.reward.manager.CoreService;
import com.hongyu.reward.manager.InitPushManager;
import com.squareup.leakcanary.LeakCanary;

import org.litepal.LitePalApplication;

import debug.xly.com.debugkit.DebugKit;


/**
 * Created by zhangyang131 on 16/6/12.
 */
public class BaseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    initGlobalConfig();
    LitePalApplication.initialize(this);
    initLeakCanary();
    initErrorPage();
    initLog();
    startCoreService();
    InitPushManager.getInstance().register();
    DebugKit.init(this);
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
  }

  /**
   * 初始化崩溃页面
   */
  private void initErrorPage() {
    // PgyCrashManager.register(this);
    // CustomActivityOnCrash.install(this);
    // // 程序在后台崩溃是否显示错误页面
    // CustomActivityOnCrash.setLaunchErrorActivityWhenInBackground(false);
    // // 是否显示错误详细信息
    // CustomActivityOnCrash.setShowErrorDetails(BuildConfig.DEBUG);
    // // 是否打印log日志
    // CustomActivityOnCrash.setShowErrorLog(BuildConfig.DEBUG);
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
