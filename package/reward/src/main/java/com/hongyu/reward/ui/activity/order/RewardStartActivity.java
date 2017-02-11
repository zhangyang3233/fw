package com.hongyu.reward.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.ui.fragment.order.RewardStartFragment;

/**
 * 发起人看到的界面
 * Created by zhangyang131 on 16/9/19.
 */
public class RewardStartActivity extends BaseSingleFragmentActivity {
  public static final String SHOP_NAME = "shop_name";
  public static final String SHOP_IMG = "shop_img";
  public static final String ORDER_ID = "order_id";
  public static final String TABLE_NUM = "table_num";
  public static final String TABLE_WAIT = "table_wait";
  public static final String TABLE_PRE = "table_pre";

  public static void launch(Context context, String shop_name, String shop_img, String order_id,
      String table_num, String table_wait, String table_pre) {
    Intent intent = new Intent(context, RewardStartActivity.class);
    intent.putExtra(SHOP_NAME, shop_name);
    intent.putExtra(SHOP_IMG, shop_img);
    intent.putExtra(ORDER_ID, order_id);
    intent.putExtra(TABLE_NUM, table_num);
    intent.putExtra(TABLE_WAIT, table_wait);
    intent.putExtra(TABLE_PRE, table_pre);
    AccountManager.launchAfterLogin(context, intent);
  }

  public static void launch(Context context, String order_id, String shop_name, String shop_img) {
    Intent intent = new Intent(context, RewardStartActivity.class);
    intent.putExtra(ORDER_ID, order_id);
    intent.putExtra(SHOP_NAME, shop_name);
    intent.putExtra(SHOP_IMG, shop_img);
    AccountManager.launchAfterLogin(context, intent);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return RewardStartFragment.class;
  }

  @Override
  protected String getTitleText() {
    return getString(R.string.task_start);
  }

}
