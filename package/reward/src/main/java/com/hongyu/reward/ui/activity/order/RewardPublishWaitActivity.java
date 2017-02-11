package com.hongyu.reward.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.ui.fragment.order.RewardPublishWaitFragment;

/**
 * Created by zhangyang131 on 16/9/13.
 */
public class RewardPublishWaitActivity extends BaseSingleFragmentActivity {

  public static void launch(Context context, String order_id, String shopName, String shop_img) {
    Intent i = new Intent(context, RewardPublishWaitActivity.class);
    i.putExtra(RewardPublishWaitFragment.SHOP_NAME, shopName);
    i.putExtra(RewardPublishWaitFragment.ORDER_ID, order_id);
    i.putExtra(RewardPublishWaitFragment.SHOP_IMG, shop_img);
    AccountManager.launchAfterLogin(context, i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return RewardPublishWaitFragment.class;
  }

  @Override
  protected String getTitleText() {
    return getString(R.string.wait_for_reward);
  }

}
