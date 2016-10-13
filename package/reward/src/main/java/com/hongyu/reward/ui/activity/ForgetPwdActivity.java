package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.startapp.ForgetPwdFragment;

/**
 * Created by zhangyang131 on 16/9/10.
 */
public class ForgetPwdActivity extends BaseSingleFragmentActivity {
  public static void launch(Context context) {
    Intent i = new Intent(context, ForgetPwdActivity.class);
    context.startActivity(i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return ForgetPwdFragment.class;
  }

  @Override
  protected String getTitleText() {
    return getString(R.string.forget_pwd);
  }
}
