package com.hongyu.reward.ui.main.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hongyu.reward.appbase.BaseLoadFragment;



/**
 * @author zhangyuwen 2016-03-14
 */
public class EmptyFragment extends BaseLoadFragment {

  public static Fragment newInstance() {
    return new EmptyFragment();
  }

  @Override
  protected void onStartLoading() {

  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {

  }

  @Override
  protected int getLayoutResId() {
    return 0;
  }
}
