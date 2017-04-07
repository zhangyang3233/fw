package com.hongyu.reward.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.Log;
import com.fw.zycoder.utils.SPUtil;
import com.hongyu.reward.BuildConfig;
import com.hongyu.reward.appbase.BaseSlideActivity;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.location.LocationManager;
import com.hongyu.reward.manager.PushTokenManager;
import com.hongyu.reward.model.TokenModel;
import com.hongyu.reward.request.GetTokenRequestBuilder;
import com.hongyu.reward.ui.dialog.CommonTwoBtnDialogFragment;
import com.hongyu.reward.ui.fragment.startapp.WelcomeFragment;
import com.hongyu.reward.utils.PayEventUtil;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhangyang131 on 16/7/22.
 */
public class SplashActivity extends BaseSlideActivity {
  private static final int REQUEST_CODE = 127;
  private static final String IS_FIRST_LAUNCH = "is first launch";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.e(PayEventUtil.getDeviceInfo(this));
    MobclickAgent.setDebugMode(BuildConfig.IS_DEBUG);
    LocationManager.getInstance().init(getApplicationContext());
    LocationManager.getInstance().start();
    initWX();
    init();
  }

  private void init() {
    String localToken = PushTokenManager.getInstance().getToken();
    if (TextUtils.isEmpty(localToken)) {
      GetTokenRequestBuilder builder = new GetTokenRequestBuilder();
      builder.setDataCallback(new DataCallback<TokenModel>() {
        @Override
        public void onDataCallback(TokenModel data) {
          if (ResponesUtil.checkModelCodeOK(data) && !TextUtils.isEmpty(data.getToken())) {
            Log.i("init", "token: "+data.getToken());
            PushTokenManager.getInstance().setToken(data.getToken());
            jumpToNextActivity();
          }else{
            showErrDialog(ResponesUtil.getErrorMsg(data));
          }
        }
      });
      builder.build().submit();
      return;
    }
    Log.i("init", "token2: "+localToken);
    jumpToNextActivity();
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

}
