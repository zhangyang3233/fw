package com.hongyu.reward.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSlideActivity;
import com.hongyu.reward.ui.fragment.BrowserFragment;

/**
 * Created by zhangyang131 on 16/9/11.
 */
public class BrowserActivity extends BaseSlideActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFragment = (BrowserFragment) Fragment.instantiate(this, BrowserFragment.class.getName(),
        getIntent().getExtras());
    replaceFragment(mFragment);
  }

  @Override
  public boolean getCanFlingBack() {
    return true;
  }
}
