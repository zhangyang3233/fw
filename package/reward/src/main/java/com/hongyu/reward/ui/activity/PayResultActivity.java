package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.PayResultFragment;

/**
 * Created by zhangyang131 on 2016/10/24.
 */
public class PayResultActivity extends BaseSingleFragmentActivity {
  public static final String PAY_SUCCESS = "paySuccess";
  public static final String ORDER_ID = "orderId";
  private String title;

  public static void launch(Context context, boolean paySuccess, String orderId) {
    Intent i = new Intent(context, PayResultActivity.class);
    i.putExtra(PAY_SUCCESS, paySuccess);
    i.putExtra(ORDER_ID, orderId);
    context.startActivity(i);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    title = getIntent().getBooleanExtra(PAY_SUCCESS, true) ? "支付成功" : "支付失败";
    super.onCreate(savedInstanceState);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return PayResultFragment.class;
  }

  @Override
  protected String getTitleText() {
    return title;
  }
}
