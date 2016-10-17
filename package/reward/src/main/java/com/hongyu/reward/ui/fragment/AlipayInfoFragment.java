package com.hongyu.reward.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.request.WithdrawalsRequestBuilder;
import com.hongyu.reward.ui.activity.AlipayInfoActivity;
import com.hongyu.reward.ui.activity.WithdrawalFinishActivity;
import com.hongyu.reward.utils.T;

/**
 * Created by zhangyang131 on 16/10/17.
 */
public class AlipayInfoFragment extends BaseLoadFragment implements View.OnClickListener {
  private float price;
  TextView pricetv;
  EditText account;
  EditText name;
  Button next;

  @Override
  protected void onStartLoading() {
    price = getArguments().getFloat(AlipayInfoActivity.PRICE);
    pricetv.setText(getPriceStr());
  }

  private String getPriceStr() {
    return String.valueOf(price);
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    account = (EditText) mContentView.findViewById(R.id.account);
    pricetv = (TextView) mContentView.findViewById(R.id.price);
    name = (EditText) mContentView.findViewById(R.id.name);
    next = (Button) mContentView.findViewById(R.id.next);
    next.setOnClickListener(this);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_alipay_info_layout;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.next:
        if(isRightInput()){
          submit();
        }
        break;
    }
  }

  private void submit() {
    showLoadingView();
    String name = this.name.getText().toString();
    String account = this.account.getText().toString();
    WithdrawalsRequestBuilder builder = new WithdrawalsRequestBuilder(account, name, price);
    builder.setDataCallback(new DataCallback<BaseModel>() {
      @Override
      public void onDataCallback(BaseModel data) {
        if(!isAdded()){
          return;
        }
        dismissLoadingView();
        if(ResponesUtil.checkModelCodeOK(data)){
          WithdrawalFinishActivity.launch(getActivity());
          getActivity().finish();
        }else{
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }

  private boolean isRightInput() {
    String name = this.name.getText().toString();
    String account = this.account.getText().toString();
    if(TextUtils.isEmpty(account)){
      this.account.setError("请输入支付宝账户信息");
      return false;
    }

    if(TextUtils.isEmpty(name)){
      this.name.setError("请输入账户对应的真实姓名");
      return false;
    }
    return true;
  }
}
