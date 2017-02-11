package com.hongyu.reward.ui.activity.personal;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.ui.fragment.personal.WithdrawFragment;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class WithdrawActivity extends BaseSingleFragmentActivity {

  public static void launch(Context context) {
    Intent i = new Intent(context, WithdrawActivity.class);
    AccountManager.launchAfterLogin(context, i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return WithdrawFragment.class;
  }

  @Override
  protected String getTitleText() {
    return getString(R.string.apply_get_money);
  }

}
