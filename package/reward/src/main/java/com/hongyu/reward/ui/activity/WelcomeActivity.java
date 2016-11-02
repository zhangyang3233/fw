package com.hongyu.reward.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseFragment;
import com.hongyu.reward.appbase.BaseSlideActivity;
import com.hongyu.reward.ui.fragment.startapp.WelcomeFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * Created by zhangyang131 on 16/9/8.
 */
public class WelcomeActivity extends BaseSlideActivity {

  public static void launch(Context context) {
    Intent i = new Intent(context, WelcomeActivity.class);
    context.startActivity(i);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initWindow();
    mFragment = (BaseFragment) Fragment.instantiate(this, WelcomeFragment.class.getName(),
        getIntent().getExtras());
    replaceFragment(mFragment);
  }

  @TargetApi(19)
  private void initWindow() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      SystemBarTintManager tintManager = new SystemBarTintManager(this);
      tintManager.setStatusBarTintEnabled(true);
      tintManager.setNavigationBarTintEnabled(true);
      tintManager.setTintColor(getResources().getColor(R.color.colorPrimary));
    }
  }
}
