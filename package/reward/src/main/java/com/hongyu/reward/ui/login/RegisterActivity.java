package com.hongyu.reward.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;

/**
 * Created by zhangyang131 on 16/9/10.
 */
public class RegisterActivity extends BaseSingleFragmentActivity {

  public static void launch(Context context) {
    Intent i = new Intent(context, RegisterActivity.class);
    context.startActivity(i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return RegisterFragment.class;
  }

  @Override
  protected String getTitleText() {
    return getString(R.string.register);
  }
}
