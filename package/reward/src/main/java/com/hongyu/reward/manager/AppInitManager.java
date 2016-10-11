package com.hongyu.reward.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.CollectionUtils;
import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.Log;
import com.fw.zycoder.utils.MainThreadPostUtils;
import com.hongyu.reward.BuildConfig;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.interfaces.AppInitFinishCallback;
import com.hongyu.reward.model.TokenModel;
import com.hongyu.reward.request.GetTokenRequestBuilder;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/10/10.
 */
public class AppInitManager {
  private static AppInitManager instance;
  ArrayList<AppInitFinishCallback> mAppInitFinishCallbacks = new ArrayList<>();
  boolean isInited;
  boolean isIniting;

  public static AppInitManager getInstance() {
    if (instance == null) {
      instance = new AppInitManager();
    }
    return instance;
  }

  public void init() {
      if(!isInited() && !isIniting){
          isIniting = true;
          initPush();
      }
  }

  public void addInitListener(AppInitFinishCallback callback){
    mAppInitFinishCallbacks.add(callback);
  }

  public boolean removeInitListener(AppInitFinishCallback callback){
    return mAppInitFinishCallbacks.remove(callback);
  }

  public boolean isInited() {
    return isInited;
  }

  public boolean isIniting() {
    return isIniting;
  }

  private void initPush() {
    PushAgent mPushAgent = PushAgent.getInstance(GlobalConfig.getAppContext());
    registerPush();
    mPushAgent.setDebugMode(BuildConfig.IS_SHOW_LOG);
    UmengMessageHandler umengMessageHandler = new CustomMessageHandler();
    UmengNotificationClickHandler notificationClickHandler = new CustomNotificationClickHandler();
    mPushAgent.setMessageHandler(umengMessageHandler);
    mPushAgent.setNotificationClickHandler(notificationClickHandler);
  }

  private void registerPush() {
    // 注册推送服务，每次调用register方法都会回调该接口
    PushAgent mPushAgent = PushAgent.getInstance(GlobalConfig.getAppContext());
    mPushAgent.register(new IUmengRegisterCallback() {

      @Override
      public void onSuccess(String deviceToken) {
        // 注册成功会返回device token
        if(!TextUtils.isEmpty(deviceToken)){
          GlobalConfig.setDeviceToken(deviceToken);
          Log.e("push deviceToken: ", String.valueOf(deviceToken));
          initToken();
        }else if(!TextUtils.isEmpty(GlobalConfig.getLocalPushCode())){
          GlobalConfig.setDeviceToken(GlobalConfig.getLocalPushCode());
          initToken();
        }else{
          MainThreadPostUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
              registerPush();
            }
          }, 200);
        }
      }

      @Override
      public void onFailure(String s, String s1) {
        Log.e("register push failed: ", s + " & " + s1);
        MainThreadPostUtils.postDelayed(new Runnable() {
          @Override
          public void run() {
            registerPush();
          }
        }, 200);
      }
    });
  }


  private void initToken() {
    String token = getTokenLocation();
    if (TextUtils.isEmpty(token)) {
      GetTokenRequestBuilder builder = new GetTokenRequestBuilder();
      builder.setDataCallback(new DataCallback<TokenModel>() {
        @Override
        public void onDataCallback(TokenModel data) {
          if (ResponesUtil.checkModelCodeOK(data)) {
            saveToken(data.getToken());
            GlobalConfig.setToken(data.getToken());
            finishInit();
          } else {
            Log.e("获取token失败:" + ResponesUtil.getErrorMsg(data));
            MainThreadPostUtils.postDelayed(new Runnable() {
              @Override
              public void run() {
                initToken();
              }
            }, 200);
          }
        }
      });
      builder.build().submit();
    }else{
      GlobalConfig.setToken(token);
      finishInit();
    }
  }

  private void finishInit() {
    AccountManager.getInstance().initUser();
    isInited = true;
    isIniting = false;
    if(!CollectionUtils.isEmpty(mAppInitFinishCallbacks)){
      for (AppInitFinishCallback callback : mAppInitFinishCallbacks) {
        callback.initFinish();
      }
    }
  }


  /**
   * 获取本地token
   *
   * @auther jiawenze
   * @tags @return
   * @since 2016-7-9 上午7:38:49
   */
  private String getTokenLocation() {
    if (TextUtils.isEmpty(GlobalConfig.getToken())) {
      SharedPreferences pref = GlobalConfig.getAppContext()
          .getSharedPreferences(Constants.Pref.USER_INFO, Context.MODE_PRIVATE);
      String token = pref.getString(Constants.Pref.TOKEN, "");
      if(!TextUtils.isEmpty(token)){
        GlobalConfig.setToken(token);
      }

    }
    return GlobalConfig.getToken();
  }


  private void saveToken(String token) {
    // JPushInterface.init(mContext);
    // JPushInterface.setAlias(mContext, token, null);
    if (TextUtils.isEmpty(token)) {
      throw new RuntimeException("token is empty");
    }
    SharedPreferences pref = GlobalConfig.getAppContext()
        .getSharedPreferences(Constants.Pref.USER_INFO, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();
    editor.putString(Constants.Pref.TOKEN, token);
    editor.commit();
  }
}
