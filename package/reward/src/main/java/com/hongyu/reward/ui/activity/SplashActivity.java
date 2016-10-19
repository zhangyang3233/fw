package com.hongyu.reward.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.fw.zycoder.utils.MainThreadPostUtils;
import com.fw.zycoder.utils.SPUtil;
import com.hongyu.reward.appbase.BaseSlideActivity;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.interfaces.AppInitFinishCallback;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.manager.AppInitManager;
import com.hongyu.reward.ui.fragment.startapp.WelcomeFragment;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.utils.WXUtil;

/**
 * Created by zhangyang131 on 16/7/22.
 */
public class SplashActivity extends BaseSlideActivity {
  private static final int REQUEST_CODE = 127;
  private static final String IS_FIRST_LAUNCH = "is first launch";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    delayedLaunch();
    AppInitManager.getInstance().init(null);
    initWX();
  }

  private void initWX() {
    WXUtil.getInstance().registWX(getApplicationContext());
  }


  private void delayedLaunch() {
    MainThreadPostUtils.postDelayed(new Runnable() {
      @Override
      public void run() {
        if (isFinishing()) {
          return;
        }

        if (isDestroyed()) {
          return;
        }

        AppInitManager.getInstance().init(new AppInitFinishCallback() {
          @Override
          public void initFinish() {
            jumpToNextActivity();
          }
        });
      }
    }, getDelayedTime());
  }

  private void jumpToNextActivity() {
    if (needToShowWelcome()) {
      WelcomeActivity.launch(this);
    } else if (AccountManager.getInstance().isLogin()) {
      TabHostActivity.launch(this);
    } else {
      LoginActivity.launch(this);
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
