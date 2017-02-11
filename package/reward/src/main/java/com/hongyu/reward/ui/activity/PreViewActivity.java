package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.PreViewFragment;

/**
 * Created by zhangyang131 on 2017/2/10.
 */

public class PreViewActivity extends BaseSingleFragmentActivity {
  public static final String ORDER_ID = "order_id";
  public static final String SHOP_NAME = "shop_name";
  public static final String SHOP_ADDRESS = "shop_address";
  public static final String SHOP_IMG = "shop_img";
  public static final String JCRS = "jcrs";
  public static final String DDRS = "ddrs";
  public static final String PWH = "pwh";
  public static final String PHOTOPATH = "photoPath";

  public static void launch(Context context, String orderId, String shop_name, String shop_address,
      String shop_img, String jcrs, String ddrs, String pwh, String photoPath) {
    Intent i = new Intent(context, PreViewActivity.class);
    i.putExtra(ORDER_ID, orderId);
    i.putExtra(SHOP_NAME, shop_name);
    i.putExtra(SHOP_ADDRESS, shop_address);
    i.putExtra(SHOP_IMG, shop_img);
    i.putExtra(JCRS, jcrs);
    i.putExtra(DDRS, ddrs);
    i.putExtra(PWH, pwh);
    i.putExtra(PHOTOPATH, photoPath);
    context.startActivity(i);
  }


  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return PreViewFragment.class;
  }

  @Override
  protected String getTitleText() {
    return "预览排号单";
  }
}
