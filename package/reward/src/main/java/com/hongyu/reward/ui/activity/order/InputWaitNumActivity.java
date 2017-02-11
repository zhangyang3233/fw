package com.hongyu.reward.ui.activity.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.order.InputWaitNumFragment;

/**
 * Created by zhangyang131 on 2017/2/9.
 */

public class InputWaitNumActivity extends BaseSingleFragmentActivity {
  private static  Activity activity;
  public static final String ORDER_ID = "ORDER_ID";
  public static final String IMAGE = "IMAGE";
  public static final String SHOP_NAME = "SHOP_NAME";
  public static final String ADDRESS = "ADDRESS";

  public static void launch(Context context, String orderId, String shop_img, String shop_name,
      String address) {
    Intent i = new Intent(context, InputWaitNumActivity.class);
    i.putExtra(ORDER_ID, orderId);
    i.putExtra(IMAGE, shop_img);
    i.putExtra(SHOP_NAME, shop_name);
    i.putExtra(ADDRESS, address);
    context.startActivity(i);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = this;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    activity = null;
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return InputWaitNumFragment.class;
  }

  @Override
  protected String getTitleText() {
    return "填写排号单";
  }

  public static void finishIfNot() {
    if(activity != null && !activity.isFinishing()){
      activity.finish();
    }
  }
}
