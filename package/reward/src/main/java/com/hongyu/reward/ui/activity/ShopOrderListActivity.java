package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.ShopOrderListFragment;

/**
 * 根据商家查询该商家可领取任务列表
 * Created by zhangyang131 on 16/9/19.
 */
public class ShopOrderListActivity extends BaseSingleFragmentActivity {
  public static final String SHOP_NAME = "shopName";
  public static final String SHOP_ID = "shop_id";

  public static void launch(Context context, String shopName, String shopId) {
    Intent i = new Intent(context, ShopOrderListActivity.class);
    i.putExtra(SHOP_NAME, shopName);
    i.putExtra(SHOP_ID, shopId);
    context.startActivity(i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return ShopOrderListFragment.class;
  }

  @Override
  protected String getTitleText() {
    return getIntent().getStringExtra(SHOP_NAME);
  }

  @Override
  public boolean getCanFlingBack() {
    return true;
  }
}
