package com.hongyu.reward.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.MainThreadPostUtils;
import com.fw.zycoder.utils.SPUtil;
import com.hongyu.reward.BuildConfig;
import com.hongyu.reward.appbase.BaseSlideActivity;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.manager.CustomMessageHandler;
import com.hongyu.reward.manager.CustomNotificationClickHandler;
import com.hongyu.reward.manager.LocationManager;
import com.hongyu.reward.model.TokenModel;
import com.hongyu.reward.request.GetTokenRequestBuilder;
import com.hongyu.reward.ui.dialog.CommonTwoBtnDialogFragment;
import com.hongyu.reward.ui.fragment.startapp.WelcomeFragment;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;

/**
 * Created by zhangyang131 on 16/7/22.
 */
public class SplashActivity extends BaseSlideActivity {
  private static final int REQUEST_CODE = 127;
  private static final String IS_FIRST_LAUNCH = "is first launch";
  PushAgent mPushAgent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    delayedLaunch();
//    AppInitManager.getInstance().init(null);
    LocationManager.getInstance().init(getApplicationContext());
    LocationManager.getInstance().start();
    initWX();
    init();
  }

  private void init() {
    String localToken = AccountManager.getInstance().getToken();
    if (TextUtils.isEmpty(localToken)) {
      GetTokenRequestBuilder builder = new GetTokenRequestBuilder();
      builder.setDataCallback(new DataCallback<TokenModel>() {
        @Override
        public void onDataCallback(TokenModel data) {
          if (ResponesUtil.checkModelCodeOK(data) && !TextUtils.isEmpty(data.getToken())) {
            AccountManager.getInstance().saveToken(data.getToken());
            jumpToNextActivity();
          }else{
            showErrDialog(ResponesUtil.getErrorMsg(data));
          }
        }
      });
      builder.build().submit();
      return;
    }
    jumpToNextActivity();
    initPush();
  }


  private void refreshToken() {
    GetTokenRequestBuilder builder = new GetTokenRequestBuilder();
    builder.setDataCallback(new DataCallback<TokenModel>() {
      @Override
      public void onDataCallback(TokenModel data) {
        if (!ResponesUtil.checkModelCodeOK(data)) {
          MainThreadPostUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
              refreshToken();
            }
          }, 500);
        }
      }
    });
    builder.build().submit();
  }


  private void initPush() {
    MainThreadPostUtils.postDelayed(new Runnable() {
      @Override
      public void run() {
        // 初始化 pushCode
        getPushAgent().register(new IUmengRegisterCallback() {
          @Override
          public void onSuccess(String pushCode) {
            String localPushCode = AccountManager.getInstance().getPushCode();
            if (TextUtils.isEmpty(pushCode) && TextUtils.isEmpty(localPushCode)) {// 本地空, 网空
              initPush();
            } else if (!TextUtils.isEmpty(pushCode)) { // 网不空
              AccountManager.getInstance().savePushCode(pushCode);
              refreshToken();
            } else if (!TextUtils.isEmpty(localPushCode) && TextUtils.isEmpty(pushCode)) { // 本地不空,网空
              // TODO nothing
              refreshToken();
            }
          }

          @Override
          public void onFailure(String s, String s1) {
            initPush();
          }
        });
      }
    }, 500);

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
    }
    return mPushAgent;
  }

  private void showErrDialog(String error) {
    CommonTwoBtnDialogFragment dialog = new CommonTwoBtnDialogFragment();
    dialog.setContent(error);
    dialog.setLeft("取消", new CommonTwoBtnDialogFragment.OnClickListener() {
      @Override
      public void onClick(Dialog dialog) {
        dialog.dismiss();
        finish();
      }
    });
    dialog.setRight("重试", new CommonTwoBtnDialogFragment.OnClickListener() {
      @Override
      public void onClick(Dialog dialog) {
        dialog.dismiss();
        init();
      }
    });
    dialog.show(getSupportFragmentManager(), getClass().getSimpleName());
  }

  private void initWX() {
    IWXAPI api = WXEntryActivity.registWX(getApplicationContext());
    api.registerApp(Constants.WX.AppID);
  }

  private void jumpToNextActivity() {
    if (needToShowWelcome()) {
      WelcomeActivity.launch(this);
    } else {
      TabHostActivity.launch(this);
    }
    finish();
  }

  public boolean needToShowWelcome() {
    int v = SPUtil.getInt(Constants.Pref.GUIDE_KEY, 0);
    if (WelcomeFragment.GUIDE_VERSION > v) { // 如果有新的guide页面就应该看看新的
      SPUtil.putInt(Constants.Pref.GUIDE_KEY, WelcomeFragment.GUIDE_VERSION);
      return true;
    }
    return false;
  }

  @Override
  public boolean getCanFlingBack() {
    return false;
  }

  public long getDelayedTime() {
    SharedPreferences sp = getPreferences(MODE_PRIVATE);
    boolean isFirstLuanch = sp.getBoolean(IS_FIRST_LAUNCH, true);
    if (isFirstLuanch) {
      SharedPreferences.Editor ed = sp.edit();
      ed.putBoolean(IS_FIRST_LAUNCH, false);
      ed.commit();
      return 0;
    }
    return 2000;
  }



  @TargetApi(23)
  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_CODE) {
      int i;
      for (i = 0; i < permissions.length; i++) {
        if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[i])
            && grantResults[i] == PackageManager.PERMISSION_DENIED) { // 用户拒绝
          T.show("请在设置页面允许获取位置权限,以便我们能够为您提供服务.");
          return;
        }
      }
    }
  }


  // private void showPermissionDeniedDialog() {
  // if (showDialogCount > 0) {
  // CommonTwoBtnDialogFragment dialog = new CommonTwoBtnDialogFragment();
  // dialog.setContent("没有获取到系统位置信息授权,无法定位您当前位置,是否重新获取?");
  // dialog.setLeft("取消", new CommonTwoBtnDialogFragment.OnClickListener() {
  // @Override
  // public void onClick(Dialog dialog) {
  // System.exit(0);
  // }
  // });
  // dialog.setRight("重新获取", new CommonTwoBtnDialogFragment.OnClickListener() {
  // @Override
  // public void onClick(Dialog dialog) {
  // dialog.dismiss();
  // getPersimmions();
  // }
  // });
  // dialog.show(getSupportFragmentManager(), getClass().getSimpleName());
  // } else {
  // CommonTwoBtnDialogFragment dialog = new CommonTwoBtnDialogFragment();
  // dialog.setContent("是否已禁用获取位置权限?\n请到设置页面允许");
  // dialog.setLeft("取消", new CommonTwoBtnDialogFragment.OnClickListener() {
  // @Override
  // public void onClick(Dialog dialog) {
  // System.exit(0);
  // }
  // });
  // dialog.setRight("重新获取", new CommonTwoBtnDialogFragment.OnClickListener() {
  // @Override
  // public void onClick(Dialog dialog) {
  // dialog.dismiss();
  // getPersimmions();
  // }
  // });
  // dialog.show(getSupportFragmentManager(), getClass().getSimpleName());
  // }
  // showDialogCount++;
  //
  // }

}
