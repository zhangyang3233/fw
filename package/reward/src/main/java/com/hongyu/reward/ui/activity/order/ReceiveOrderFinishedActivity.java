package com.hongyu.reward.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.order.ReceiveOrderFinishedFragment;

/**
 *
 * 领取的订单被发起人付款完成了, 此应该是评论页面
 * Created by zhangyang131 on 16/10/12.
 */
public class ReceiveOrderFinishedActivity extends BaseSingleFragmentActivity {
  public static final String ORDER_ID = "order_id";

  public static void launch(Context context, String order_id) {
    Intent i = new Intent(context, ReceiveOrderFinishedActivity.class);
    i.putExtra(ORDER_ID, order_id);
    context.startActivity(i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return ReceiveOrderFinishedFragment.class;
  }

  @Override
  protected String getTitleText() {
    return "给ta评价";
  }
}
