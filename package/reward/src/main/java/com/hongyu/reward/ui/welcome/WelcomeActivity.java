package com.hongyu.reward.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseFragment;
import com.hongyu.reward.appbase.BaseSlideActivity;


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
    mFragment = (BaseFragment) Fragment.instantiate(this, WelcomeFragment.class.getName(),
        getIntent().getExtras());
    replaceFragment(mFragment);
  }
}
