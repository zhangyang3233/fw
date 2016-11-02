package com.hongyu.reward.manager;

import android.text.TextUtils;

import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.Log;
import com.fw.zycoder.utils.MainThreadPostUtils;
import com.fw.zycoder.utils.SPUtil;
import com.hongyu.reward.BuildConfig;
import com.hongyu.reward.config.Constants;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;

/**
 * Created by zhangyang131 on 2016/10/28.
 */

public class InitPushManager {
  private static InitPushManager instance;
  public static final String TAG = InitPushManager.class.getSimpleName();
  PushAgent mPushAgent;

  private InitPushManager() {
    mPushAgent = getPushAgent();
  }

  public static synchronized InitPushManager getInstance() {
    if(instance == null){
        instance = new InitPushManager();
    }
    return instance;
  }

  public void register() {
    mPushAgent.register(new IUmengRegisterCallback() {
      @Override
      public void onSuccess(String pushCode) {

        switch (checkPushCode(pushCode)) {
          case 0: // 失败(本地和获取到的都为空)
            registerOnDelayed(); // 重新去注册
            break;
          case 1: // 改变（获取到的不为空， 但是和本地的不一样）
            // TODO 需要存pushCode
            PushTokenManager.getInstance().setPushCode(pushCode);
            break;
          case 2: // 没变（获取到的和本地是一样的）
            PushTokenManager.getInstance().setPushCode(pushCode);
            break;
        }
      }

      @Override
      public void onFailure(String s, String s1) {
        Log.i(TAG, "请求 pushCode 失败: " + s + " & " + s1);
      }
    });
  }

  /**
   *
   * @param pushCode
   * @return 0 失败(本地和获取到的都为空)， 1，改变（获取到的不为空， 但是和本地的不一样）， 2，没变（获取到的和本地是一样的）
   */
  private int checkPushCode(String pushCode) {
    Log.i(TAG, "请求api获得的pushCode: " + pushCode);
    String localPushCode = PushTokenManager.getInstance().getPushCode();
    if (TextUtils.isEmpty(localPushCode) && TextUtils.isEmpty(pushCode)) {
      return 0;
    }
    if (!TextUtils.isEmpty(pushCode) && !pushCode.equals(localPushCode)) {
      return 1;
    }

    return 2;
  }

  /**
   * 延时继续注册
   */
  private void registerOnDelayed() {
    MainThreadPostUtils.postDelayed(new Runnable() {
      @Override
      public void run() {
        register();
      }
    }, 500);
  }


  private PushAgent getPushAgent() {
    mPushAgent = PushAgent.getInstance(GlobalConfig.getAppContext());
    mPushAgent.setDebugMode(BuildConfig.IS_SHOW_LOG);
    UmengMessageHandler umengMessageHandler = new CustomMessageHandler();
    UmengNotificationClickHandler notificationClickHandler =
        new CustomNotificationClickHandler();
    mPushAgent.setMessageHandler(umengMessageHandler);
    mPushAgent.setNotificationClickHandler(notificationClickHandler);
    mPushAgent.setNotificaitonOnForeground(true);
    resetPushConfig();
    return mPushAgent;
  }

  /**
   * 改变 pushCode 配置
   */
  public void resetPushConfig() {
    if (mPushAgent == null) {
      return;
    }
    if (SPUtil.getBoolean(Constants.Pref.SHOCK_CUES, true)) {
      mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);// 振动
    } else {
      mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);// 振动
    }
    if (SPUtil.getBoolean(Constants.Pref.AUDIO_CUES, true)) {
      mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE); // 声音
    } else {
      mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE); // 声音
    }
  }

}
