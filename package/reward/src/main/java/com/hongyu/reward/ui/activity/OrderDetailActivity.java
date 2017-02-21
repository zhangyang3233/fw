package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.ui.fragment.OrderDetailFragment;

/**
 * 可领取任务详情页
 * Created by zhangyang131 on 16/9/19.
 */
public class OrderDetailActivity extends BaseSingleFragmentActivity {
  public static final String SHOP_NAME = "shopName";
  public static final String ORDER_ID = "order_id";
  public static final String IS_MY_RECEIVE = "isMyReceive";

  public static void launch(Context context,String order_id, String shopName) {
    Intent i = new Intent(context, OrderDetailActivity.class);
    i.putExtra(SHOP_NAME, shopName);
    i.putExtra(ORDER_ID, order_id);
    AccountManager.launchAfterLogin(context, i);
  }

  public static void launch(Context context,String order_id, String shopName,boolean isMyReceive) {
    Intent i = new Intent(context, OrderDetailActivity.class);
    i.putExtra(SHOP_NAME, shopName);
    i.putExtra(ORDER_ID, order_id);
    i.putExtra(IS_MY_RECEIVE, isMyReceive);
    AccountManager.launchAfterLogin(context, i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return OrderDetailFragment.class;
  }

  @Override
  protected String getTitleText() {
    return getIntent().getStringExtra(SHOP_NAME);
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    mFragment.onActivityResult(requestCode, resultCode, data);
  }
}
