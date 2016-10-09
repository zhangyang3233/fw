package com.hongyu.reward.manager;

import android.content.Context;

import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by zhangyang131 on 16/10/9.
 */
public class CustomMessageHandler extends UmengMessageHandler {

    @Override
    public void handleMessage(Context context, UMessage uMessage) {
        super.handleMessage(context, uMessage);
        // 收到通知
    }
}
