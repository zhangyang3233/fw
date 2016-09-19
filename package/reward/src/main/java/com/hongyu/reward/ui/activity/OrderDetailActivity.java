package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.OrderDetailFragment;

/**
 * 可领取任务详情页
 * Created by zhangyang131 on 16/9/19.
 */
public class OrderDetailActivity extends BaseSingleFragmentActivity {
  public static final String SHOP_NAME = "shopName";
  public static final String SHOP_IMAGE = "shop_image";
  public static final String ORDER_ID = "order_id";
  public static final String NICKNAME = "nickname";
  public static final String PRICE = "price";
  public static final String SHOP_ID = "shop_id";
  public static final String USER_ID = "user_id";

  public static void launch(Context context, String shopName, String shop_image, String order_id,
      String nickname, String price, String shop_id, String user_id) {
    Intent i = new Intent(context, OrderDetailActivity.class);
    i.putExtra(SHOP_NAME, shopName);
    i.putExtra(SHOP_IMAGE, shop_image);
    i.putExtra(ORDER_ID, order_id);
    i.putExtra(NICKNAME, nickname);
    i.putExtra(PRICE, price);
    i.putExtra(SHOP_ID, shop_id);
    i.putExtra(USER_ID, user_id);
    context.startActivity(i);
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
  public boolean getCanFlingBack() {
    return true;
  }
}
