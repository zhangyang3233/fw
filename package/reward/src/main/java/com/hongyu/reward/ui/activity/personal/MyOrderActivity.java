package com.hongyu.reward.ui.activity.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.AppBaseActivity;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.ui.fragment.personal.MyCreateOrderListFragment;
import com.hongyu.reward.ui.fragment.personal.MyReceiveOrderListFragment;
import com.hongyu.reward.widget.AppSwitchTitleView;
import com.hongyu.reward.widget.TitleContainer;

/**
 * 我发起的订单页面
 * Created by zhangyang131 on 16/9/20.
 */
public class MyOrderActivity extends AppBaseActivity
    implements
      AppSwitchTitleView.OnCheckedChangedListener {
  AppSwitchTitleView mAppSwitchTitleView;
  Fragment leftFragment;
  Fragment rightFragment;

  public static void launch(Context context) {
    Intent i = new Intent(context, MyOrderActivity.class);
    AccountManager.launchAfterLogin(context, i);
  }

  @Override
  protected String getTitleText() {
    return null;
  }

  @Override
  protected void onCreate(Bundle onSaveInstanceState) {
    super.onCreate(onSaveInstanceState);
    showMyCreateOrder();
  }

  @Override
  protected TitleContainer getMyTitleContainer() {
    return AppSwitchTitleView.newInstance(this);
  }

  @Override
  protected <V extends TitleContainer> void setCustomTitleView(V view) {
    super.setCustomTitleView(view);
    mAppSwitchTitleView = (AppSwitchTitleView) view;
    mAppSwitchTitleView.setOnCheckedChangedListener(this);
  }

  @Override
  public void checkChanged(int witch) {
    switch (witch) {
      case AppSwitchTitleView.OnCheckedChangedListener.LEFT:
        showMyCreateOrder();
        break;
      case AppSwitchTitleView.OnCheckedChangedListener.RIGHT:
        showMyReceiveOrder();
        break;
    }
  }

  /**
   * 显示我的订单的页面
   */
  private void showMyCreateOrder() {
    leftFragment = Fragment.instantiate(this, MyCreateOrderListFragment.class.getName(),
        getIntent().getExtras());
    replaceFragment(leftFragment);
  }

  /**
   * 显示我接收到的定点页面
   */
  private void showMyReceiveOrder() {
    rightFragment = Fragment.instantiate(this, MyReceiveOrderListFragment.class.getName(),
        getIntent().getExtras());
    replaceFragment(rightFragment);
  }

}
