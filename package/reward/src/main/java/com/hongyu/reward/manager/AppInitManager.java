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
import com.hongyu.reward.interfaces.LogoutListener;
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
public class AppInitManager implements LogoutListener {
  private static final String TAG = AppInitManager.class.getSimpleName();
  private static AppInitManager instance;
  ArrayList<AppInitFinishCallback> mAppInitFinishCallbacks = new ArrayList<>();
  boolean isInited;
  boolean isIniting;

  public static synchronized AppInitManager getInstance() {
    if (instance == null) {
      Log.e(TAG, "创建对象...");
      instance = new AppInitManager();
    }
    return instance;
  }

  public void init() {
    if (!isInited() && !isIniting) {
      isIniting = true;
      initToken();
    }
  }

  public void addInitListener(AppInitFinishCallback callback) {
    mAppInitFinishCallbacks.add(callback);
  }

  public boolean removeInitListener(AppInitFinishCallback callback) {
    return mAppInitFinishCallbacks.remove(callback);
  }

  public boolean isInited() {
    return isInited;
  }

  public boolean isIniting() {
    return isIniting;
  }

  private void initPush() {
    Log.w(TAG, "开始初始化 push");
    PushAgent mPushAgent = PushAgent.getInstance(GlobalConfig.getAppContext());
    registerPush();
    AccountManager.getInstance().addLogoutListener(this);
    mPushAgent.setDebugMode(BuildConfig.IS_SHOW_LOG);
    UmengMessageHandler umengMessageHandler = new CustomMessageHandler();
    UmengNotificationClickHandler notificationClickHandler = new CustomNotificationClickHandler();
    mPushAgent.setMessageHandler(umengMessageHandler);
    mPushAgent.setNotificationClickHandler(notificationClickHandler);
  }

  public void onLogout() { // 用户退出登录了,要重新刷pushCode
    registerPush();
  }


  private void registerPush() {
    Log.i(TAG, "开始注册pushCode");
    PushAgent mPushAgent = PushAgent.getInstance(GlobalConfig.getAppContext());
    mPushAgent.register(new IUmengRegisterCallback() {
      @Override
      public void onSuccess(String pushCode) {
        if (TextUtils.isEmpty(pushCode)) {
          if (TextUtils.isEmpty(GlobalConfig.getPushCode())) {
            Log.i(TAG, "请求pushCodef返回null,使用本地pushCode为null,继续请求pushCode");
            MainThreadPostUtils.postDelayed(new Runnable() {

              @Override
              public void run() {
                registerPush();
              }
            }, 200);
          } else {
            Log.i(TAG, "请求pushCodef返回null,使用本地pushCode:" + GlobalConfig.getPushCode());
            freshToken();
          }
        } else {
          GlobalConfig.setPushCode(pushCode);
          Log.i(TAG, "请求pushCode成功:" + pushCode);
          freshToken();
        }
      }

      @Override
      public void onFailure(String s, String s1) {
        Log.i(TAG, "请求pushCode失败:" + s + " & " + s1);
        MainThreadPostUtils.postDelayed(new Runnable() {
          @Override
          public void run() {
            registerPush();
          }
        }, 200);
      }
    });
  }

  private void freshToken() {
    GetTokenRequestBuilder builder = new GetTokenRequestBuilder();
    builder.setDataCallback(new DataCallback<TokenModel>() {
      @Override
      public void onDataCallback(TokenModel data) {
        if (ResponesUtil.checkModelCodeOK(data)) {
          Log.w(TAG, "刷pushCode成功: " + data.getToken());
        } else {
          Log.e(TAG, "刷pushCode失败:" + ResponesUtil.getErrorMsg(data) + "重新尝试中");
          MainThreadPostUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
              freshToken();
            }
          }, 200);
        }
      }
    });
    builder.build().submit();
  }

  private void initToken() {
    Log.w(TAG, "注册token");
    String token = getTokenLocation();
    if (TextUtils.isEmpty(token)) {
      Log.w(TAG, "本地token为空,请求token");
      GetTokenRequestBuilder builder = new GetTokenRequestBuilder();
      builder.setDataCallback(new DataCallback<TokenModel>() {
        @Override
        public void onDataCallback(TokenModel data) {
          if (ResponesUtil.checkModelCodeOK(data)) {
            Log.w(TAG, "网络获取token成功: " + data.getToken());
            saveToken(data.getToken());
            GlobalConfig.setToken(data.getToken());
            finishInit();
          } else {
            Log.e(TAG, "获取token失败:" + ResponesUtil.getErrorMsg(data) + "重新尝试中");
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
    } else {
      Log.w(TAG, "本地token不为空,使用本地token:" + token);
      GlobalConfig.setToken(token);
      finishInit();
    }
  }

  private void finishInit() {
    Log.w(TAG, "初始化完成!!!");
    AccountManager.getInstance().initUser();
    isInited = true;
    isIniting = false;
    if (!CollectionUtils.isEmpty(mAppInitFinishCallbacks)) {
      for (AppInitFinishCallback callback : mAppInitFinishCallbacks) {
        callback.initFinish();
      }
    }
    initPush();
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
      if (!TextUtils.isEmpty(token)) {
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
