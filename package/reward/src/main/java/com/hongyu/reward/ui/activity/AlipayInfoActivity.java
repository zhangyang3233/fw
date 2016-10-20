package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.ui.fragment.AlipayInfoFragment;

/**
 * 提现详情
 * Created by zhangyang131 on 16/10/17.
 */
public class AlipayInfoActivity extends BaseSingleFragmentActivity {

  public static final String PRICE = "price";

  public static void launch(Context context, float price) {
    Intent i = new Intent(context, AlipayInfoActivity.class);
    i.putExtra(PRICE, price);
    AccountManager.launchAfterLogin(context, i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return AlipayInfoFragment.class;
  }

  @Override
  protected String getTitleText() {
    return "申请提现";
  }
}
