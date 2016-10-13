package com.hongyu.reward.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSlideActivity;
import com.hongyu.reward.ui.fragment.SearchFragment;

/**
 * Created by zhangyang131 on 16/10/13.
 */
public class SearchActivity extends BaseSlideActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFragment = (SearchFragment) Fragment.instantiate(this, SearchFragment.class.getName(),
        getIntent().getExtras());
    if (mFragment != null) {
      replaceFragment(mFragment);
    }
  }
}
