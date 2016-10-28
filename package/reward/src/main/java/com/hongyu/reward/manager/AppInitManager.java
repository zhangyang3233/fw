package com.hongyu.reward.manager;

import android.text.TextUtils;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.CollectionUtils;
import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.Log;
import com.fw.zycoder.utils.MainThreadPostUtils;
import com.fw.zycoder.utils.SPUtil;
import com.hongyu.reward.BuildConfig;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.interfaces.AppInitFinishCallback;
import com.hongyu.reward.model.TokenModel;
import com.hongyu.reward.request.GetTokenRequestBuilder;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/10/14.
 */
public class AppInitManager {
  private static final String TAG = AppInitManager.class.getSimpleName();
  public static final int STATUS_NONE = 0;
  public static final int STATUS_INITING = 1;
  public static final int STATUS_FINISHED = 2;
  private static AppInitManager instance;
  private int status = STATUS_NONE;
  PushAgent mPushAgent;
  private ArrayList<AppInitFinishCallback> observe = new ArrayList<>();

  public static synchronized AppInitManager getInstance() {
    if (instance == null) {
      instance = new AppInitManager();
    }
    return instance;
  }

  public void init(final AppInitFinishCallback call) {
    if (status == STATUS_FINISHED) {
      if (call != null) {
        call.initFinish();
      }
      return;
    }
    if (status == STATUS_INITING) {
      if (call != null) {
        observe.add(call);
      }
      return;
    }
    // 初始化 token
    status = STATUS_INITING;
    startInit();
  }

  private void startInit() {
    Log.i(TAG, "开始初始化...");
    String localToken = AccountManager.getInstance().getToken();
    if (TextUtils.isEmpty(localToken)) {
      Log.i(TAG, "本地token == null, 开始请求token...");
      GetTokenRequestBuilder builder = new GetTokenRequestBuilder();
      builder.setDataCallback(new DataCallback<TokenModel>() {
        @Override
        public void onDataCallback(TokenModel data) {
          if (ResponesUtil.checkModelCodeOK(data) && !TextUtils.isEmpty(data.getToken())) {
            AccountManager.getInstance().saveToken(data.getToken());
          }
          startInit();
        }
      });
      builder.build().submit();
      return;
    }
    Log.i(TAG, "token = " + localToken);
    status = STATUS_FINISHED;
    notifyInit();
    initPush();
  }

  private void notifyInit() {
    if (!CollectionUtils.isEmpty(observe)) {
      for (AppInitFinishCallback call : observe) {
        call.initFinish();
      }
    }
    observe = new ArrayList<>();
  }

  private PushAgent getPushAgent() {
    if(mPushAgent == null){
      mPushAgent = PushAgent.getInstance(GlobalConfig.getAppContext());
      mPushAgent.setDebugMode(BuildConfig.IS_SHOW_LOG);
      UmengMessageHandler umengMessageHandler = new CustomMessageHandler();
      UmengNotificationClickHandler notificationClickHandler =
              new CustomNotificationClickHandler();
      mPushAgent.setMessageHandler(umengMessageHandler);
      mPushAgent.setNotificationClickHandler(notificationClickHandler);
      mPushAgent.setNotificaitonOnForeground(false);
      resetPushConfig();
    }
    return mPushAgent;
  }

  public void resetPushConfig() {
    getPushAgent();
    if(SPUtil.getBoolean(Constants.Pref.SHOCK_CUES, true)){
      mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动
    }else{
      mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);//振动
    }
    if(SPUtil.getBoolean(Constants.Pref.AUDIO_CUES, true)){
      mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //声音
    }else{
      mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE); //声音
    }
  }


  private void initPush() {
    Log.i(TAG, "开始初始化通知...");
    MainThreadPostUtils.postDelayed(new Runnable() {
      @Override
      public void run() {
        // 初始化 pushCode
        getPushAgent().register(new IUmengRegisterCallback() {
          @Override
          public void onSuccess(String pushCode) {
            Log.i(TAG, "请求api获得的pushCode: " + pushCode);
            String localPushCode = AccountManager.getInstance().getPushCode();
            if (TextUtils.isEmpty(pushCode) && TextUtils.isEmpty(localPushCode)) {// 本地空, 网空
              initPush();
            } else if (!TextUtils.isEmpty(pushCode)) { // 网不空
              Log.i(TAG, "网不空, 刷!, pushCode: " + pushCode);
              AccountManager.getInstance().savePushCode(pushCode);
              refreshToken();
            } else if (!TextUtils.isEmpty(localPushCode) && TextUtils.isEmpty(pushCode)) { // 本地不空,网空
              // TODO nothing
              refreshToken();
              Log.i(TAG, "本地不空,网空, 刷!, localPushCode: " + localPushCode);
            }
          }

          @Override
          public void onFailure(String s, String s1) {
            Log.i(TAG, "请求 pushCode 失败: " + s + " & " + s1);
            initPush();
          }
        });
      }
    }, 500);

  }

  private void refreshToken() {
    final String pushCode = AccountManager.getInstance().getPushCode();
    GetTokenRequestBuilder builder = new GetTokenRequestBuilder();
    builder.setDataCallback(new DataCallback<TokenModel>() {
      @Override
      public void onDataCallback(TokenModel data) {
        if (!ResponesUtil.checkModelCodeOK(data)) {
          Log.i(TAG, "强刷 pushCode 失败, 重新刷");
          MainThreadPostUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
              refreshToken();
            }
          }, 500);
        } else {
          Log.i(TAG, "强刷 pushCode 成功, pushCode :  " + pushCode + ", token :  "
              + AccountManager.getInstance().getToken());
        }
      }
    });
    builder.build().submit();
  }

}
