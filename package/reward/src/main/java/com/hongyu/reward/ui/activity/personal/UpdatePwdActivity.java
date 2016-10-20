package com.hongyu.reward.ui.activity.personal;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.ui.fragment.personal.UpdatePwdFragment;

/**
 * Created by zhangyang131 on 16/9/22.
 */
public class UpdatePwdActivity extends BaseSingleFragmentActivity {

  public static void launch(Context context) {
    Intent i = new Intent(context, UpdatePwdActivity.class);
    AccountManager.launchAfterLogin(context, i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return UpdatePwdFragment.class;
  }

  @Override
  protected String getTitleText() {
    return getString(R.string.update_pwd_title);
  }
}
