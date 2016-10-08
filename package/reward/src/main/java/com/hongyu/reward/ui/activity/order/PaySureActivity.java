package com.hongyu.reward.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.order.PaySureFragment;

/**
 * Created by zhangyang131 on 16/10/3.
 */
public class PaySureActivity extends BaseSingleFragmentActivity {
  public static final String ORDER_ID = "order_id";
  public static final String PRICE = "price";


  public static void launch(Context context, String orderId, String price) {
    Intent i = new Intent(context, PaySureActivity.class);
    i.putExtra(ORDER_ID, orderId);
    i.putExtra(PRICE, price);
    context.startActivity(i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return PaySureFragment.class;
  }

  @Override
  protected String getTitleText() {
    return "确认支付";
  }
}
