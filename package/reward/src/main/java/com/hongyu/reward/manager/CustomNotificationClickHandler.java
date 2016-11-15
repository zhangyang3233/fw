package com.hongyu.reward.manager;

import android.content.Context;

import com.hongyu.reward.model.NoticeEvent;
import com.hongyu.reward.model.PushModel;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhangyang131 on 16/10/9.
 */
public class CustomNotificationClickHandler extends UmengNotificationClickHandler {
  private static CustomNotificationClickHandler instance;



  @Override
  public void handleMessage(Context context, final UMessage uMessage) {
    super.handleMessage(context, uMessage);
    PushModel pm = PushModel.parse(uMessage);
    if(pm == null || pm.getPush() == null || pm.getPush().getType() == null || !pm.getPush().getType().equals("3")){
      return;
    }
    EventBus.getDefault().post(new NoticeEvent(NoticeEvent.NEW_ORDER_CREATE));
  }
}
