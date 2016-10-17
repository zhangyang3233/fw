package com.hongyu.reward.manager;

import android.content.Context;

import com.fw.zycoder.utils.AppUtils;
import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.Log;
import com.fw.zycoder.utils.MainThreadPostUtils;
import com.hongyu.reward.model.PushModel;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by zhangyang131 on 16/10/9.
 */
public class CustomMessageHandler extends UmengMessageHandler implements Runnable{
  private static final String TAG = "appmsg";
  private static CustomMessageHandler instance;
  UMessage uMessage;
  Thread thread;
  // public static final int STATUS_PENDING_RECEIVE = 0; // 待领取
  // public static final int STATUS_RECEIVED = 10; // 已经领取
  // public static final int STATUS_PENDING_PAY = 20; // 待付款
  // public static final int STATUS_FINISHED = 30; // 已经完成
  // public static final int STATUS_INVALID = 31; // 失效
  // public static final int STATUS_CANCEL = 32; // 取消
  // public static final int STATUS_APPEND = 33; // 追加
  // public static final int STATUS_PENDING_COMMENT = 40; // 待评价
  // public static final int STATUS_PENDING_COMPLAINT = 50; // 投诉单

  @Override
  public void handleMessage(Context context, UMessage uMessage) {
    Log.i(TAG, "收到通知:" + uMessage.title);
    this.uMessage = uMessage;
    initThread();
    super.handleMessage(context, uMessage);
  }

  @Override
  public void run() {
    while (true){
      if(AppUtils.isBackground(GlobalConfig.getAppContext()) || uMessage == null){
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        continue;
      }else{
        final UMessage msg = uMessage;
        uMessage = null;
        MainThreadPostUtils.postDelayed(new Runnable() {
          @Override
          public void run() {
            dealWithUMessage(msg);
          }
        },500);
      }
    }
  }

  private void initThread() {
    if(thread == null){
      thread = new Thread(this);
      thread.start();
    }
  }

  private void dealWithUMessage(UMessage uMessage) {
    PushModel pm = PushModel.parse(uMessage);
    if(pm == null || pm.getPush() == null){
      return;
    }
    PushModel.PushInfo pi = pm.getPush();
    if (pi.getType().equals("1")) { // 订单状态
      if (pi.getStatus().equals("0")) { // 领取的任务被拒绝
        PushDeal.orderIsRefuse(pi);
      } else if (pi.getStatus().equals("10")) { // 悬赏已经被人领取
        PushDeal.orderIsReceived(pi);
      } else if (pi.getStatus().equals("20")) { // 订单已经被悬赏人选择
        PushDeal.orderReceiveSuccess(pi);
      } else if (pi.getStatus().equals("30")) { // 订单已经完成
        PushDeal.orderFinished(pi);
      } else if (pi.getStatus().equals("32")) { // 订单已经被取消
        PushDeal.orderCanceled(pi);
      }
    }else if(pi.getType().equals("2")){ // 领取的任务被拒绝
      PushDeal.orderIsRefuse(pi);
    }
  }


}
