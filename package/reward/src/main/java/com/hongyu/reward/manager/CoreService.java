package com.hongyu.reward.manager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.fw.zycoder.utils.Log;
import com.hongyu.reward.model.NoticeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

  @Override
  public void onCreate() {
    super.onCreate();
    onStartListen();
    EventBus.getDefault().register(this);
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
    EventBus.getDefault().unregister(this);
    onStopListen();

  }

  public void onStartListen() {
    if (scheduledExecutor != null && !scheduledExecutor.isShutdown()) return;
    scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    scheduledExecutor.scheduleAtFixedRate(new MyTask(), 50, 50, TimeUnit.SECONDS);
  }

  public void onStopListen() {
    if (scheduledExecutor == null || scheduledExecutor.isShutdown()) return;
    scheduledExecutor.shutdown();
  }

  private class MyTask implements Runnable {
    @Override
    public void run() {
      checkOrder();
    }
  }

  private void checkOrder() {
    if(AccountManager.getInstance().isLogin()){
      Log.i("MyTask", "MyTask执行中....");
      RefreshOrderManager.getStatusOrder();
    }else{
      Log.i("MyTask", "未登录....");
    }
  }


  @Subscribe
  public void onEventMainThread(NoticeEvent event) {
    if(event.getType() == NoticeEvent.ORDER_STATUS_CHANGED){
      checkOrder();
    }
  }
}
