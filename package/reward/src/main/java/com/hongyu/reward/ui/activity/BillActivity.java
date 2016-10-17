package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.BillFragment;

/**
 * Created by zhangyang131 on 16/10/17.
 */
public class BillActivity extends BaseSingleFragmentActivity {

  public static void launch(Context context) {
    Intent i = new Intent(context, BillActivity.class);
    context.startActivity(i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return BillFragment.class;
  }

  @Override
  protected String getTitleText() {
    return "账单";
  }
}
