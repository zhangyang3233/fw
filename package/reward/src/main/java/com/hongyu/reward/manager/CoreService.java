package com.hongyu.reward.manager;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.fw.zycoder.utils.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * service - core - 核心服务
 *
 * =======================================
 * Copyright 2016-2017
 * =======================================
 *
 * @since 2016-6-18 上午4:11:37
 * @author centos
 *
 */
public class CoreService extends Service {

  private ScheduledExecutorService scheduledExecutor = null;
  private AccountManager userManager;
  // private ScreenManager screenManager;
  // private ReceiveBroadCast receiveBroadCast;

  private String order_id = "";
  private String shop_img = "";
  private String shop_name = "";

  private boolean isShow = false;

  private Handler handler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      isShow = true;
      // if (screenManager.currentActivity() instanceof RewardSelectPersonActivity) {
      // isShow = false;
      // } else {
      // DialogFactory.showCommDialog2(screenManager.currentActivity(), "提示", "您的订单有人接单了？", new
      // OnWhichListener(){
      //
      // @Override
      // public void onConfirmClick(int which) {
      // if (which == 99) {
      // isShow = false;
      // } else {
      // isShow = false;
      // toSelect();
      // }
      // }});
      // }
    }

  };

  private void toSelect() {
    // Intent intent = new Intent(screenManager.currentActivity(),
    // RewardSelectPersonActivity.class);
    // intent.putExtra("order_id", order_id);
    // intent.putExtra("shop_img", shop_img);
    // intent.putExtra("shop_name", shop_name);
    // screenManager.currentActivity().startActivity(intent);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    initSharedVal();
    onStartListen();
  }


  /**
   * 初始化类内部共享变量
   * 
   * @author Jfomt
   */
  private void initSharedVal() {
    userManager = AccountManager.getInstance(getApplicationContext());
    // screenManager = ScreenManager.getScreenManager();
    // this.appManager = new AppManager(this);
    userManager.getToken();
  }

  /**
   * 服务启动
   */
  @Override
  public int onStartCommand(final Intent intent, final int flags,
      final int startId) {
    return START_STICKY;
  }

  /**
   * 不允许bind模式启动服务
   */
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    onStopListen();
  }

  public void onStartListen() {
    if (scheduledExecutor != null && !scheduledExecutor.isShutdown()) return;
    scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    scheduledExecutor.scheduleAtFixedRate(new MyTask(), 5, 5, TimeUnit.SECONDS);
  }

  public void onStopListen() {
    if (scheduledExecutor == null || scheduledExecutor.isShutdown()) return;
    scheduledExecutor.shutdown();
  }

  private class MyTask implements Runnable {
    @Override
    public void run() {
      Log.i("MyTask", "MyTask执行中....");
      RefreshOrderManager.getStatusOrder();
    }
  }

}
