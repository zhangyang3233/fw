package com.hongyu.reward.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.order.SelectPersonFragment;

/**
 * Created by zhangyang131 on 16/10/4.
 */
public class SelectPersonActivity extends BaseSingleFragmentActivity {
  public static final String ORDER_ID = "order_id";
  public static final String SHOP_NAME = "shop_name";
  public static final String SHOP_IMG = "shop_img";

  public static void launch(Context context, String order_id, String shop_name, String shop_img) {
    Intent i = new Intent(context, SelectPersonActivity.class);
    i.putExtra(ORDER_ID, order_id);
    i.putExtra(SHOP_NAME, shop_name);
    i.putExtra(SHOP_IMG, shop_img);
    context.startActivity(i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return SelectPersonFragment.class;
  }

  @Override
  protected String getTitleText() {
    return "选择领赏人";
  }
}
