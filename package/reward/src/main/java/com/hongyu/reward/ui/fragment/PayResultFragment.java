package com.hongyu.reward.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.ui.activity.PayResultActivity;
import com.hongyu.reward.ui.activity.order.PublishFinishedCommentActivity;

/**
 * Created by zhangyang131 on 2016/10/24.
 */
public class PayResultFragment extends BaseLoadFragment implements View.OnClickListener {
  boolean isPaySuccess;
  String orderId;
  TextView title;
  TextView desc;
  ImageView icon;
  Button cancel;
  Button ok;

  @Override
  protected void onStartLoading() {
    isPaySuccess = getArguments().getBoolean(PayResultActivity.PAY_SUCCESS);
    orderId = getArguments().getString(PayResultActivity.ORDER_ID);
    resetView();
  }

  private void resetView() {
    cancel.setOnClickListener(this);
    ok.setOnClickListener(this);
    if (isPaySuccess) {
      title.setText("赏金支付成功");
      icon.setImageResource(R.mipmap.icon_pay_finish);
      desc.setText(R.string.pay_success);
    } else {
      title.setText("赏金支付失败");
      icon.setImageResource(R.mipmap.comm_icon_fail);
      desc.setText(R.string.pay_failed);
      cancel.setVisibility(View.GONE);
      ok.setText("重新支付");
    }
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    title = (TextView) mContentView.findViewById(R.id.title);
    desc = (TextView) mContentView.findViewById(R.id.desc);
    icon = (ImageView) mContentView.findViewById(R.id.icon);
    cancel = (Button) mContentView.findViewById(R.id.cancel);
    ok = (Button) mContentView.findViewById(R.id.to_comment);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_pay_result_layout;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.cancel:
        getActivity().finish();
        break;
      case R.id.to_comment:
        if (isPaySuccess) {
          PublishFinishedCommentActivity.launch(getActivity(), orderId);
        }
        getActivity().finish();
        break;
    }
  }
}
