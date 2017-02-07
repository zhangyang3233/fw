package com.hongyu.reward.ui.fragment.personal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.LoginModel;
import com.hongyu.reward.ui.activity.AlipayInfoActivity;
import com.hongyu.reward.ui.activity.BrowserActivity;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.RoundImageView;

import java.math.BigDecimal;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class WithdrawFragment extends BaseLoadFragment
    implements
      View.OnClickListener,
      TextWatcher {
  private RoundImageView mHeadImg;
  private TextView mTvName;
  private TextView mTvPrice;
  private TextView mGuizeTv;
  private TextView mCalcTv;
  private EditText mWithdraw;
  private View mBtnAlipay;
  private View mIvAlipay;
  private View mBtnStartWithdraw;
  private float priceMax;

  @Override
  protected void onStartLoading() {
    LoginModel.UserInfo userInfo = AccountManager.getInstance().getUser();
    priceMax = userInfo.getCash();
    mTvName.setText(userInfo.getNickname());
    mTvPrice.setText(String.valueOf(userInfo.getCash()));
    if (priceMax < 1000) {
      mWithdraw.setHint("本次可提现" + priceMax + "元");
    } else {
      mWithdraw.setHint("本次可提现" + 1000 + "元");
    }
    mHeadImg.loadNetworkImageByUrl(userInfo.getHead_img());
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    mHeadImg = (RoundImageView) mContentView.findViewById(R.id.header_icon);
    mTvName = (TextView) mContentView.findViewById(R.id.name);
    mTvPrice = (TextView) mContentView.findViewById(R.id.price);
    mGuizeTv = (TextView) mContentView.findViewById(R.id.guize);
    mCalcTv = (TextView) mContentView.findViewById(R.id.calc_tv);

    mWithdraw = (EditText) mContentView.findViewById(R.id.withdraw_price);
    mBtnAlipay = mContentView.findViewById(R.id.btn_alipay);
    mIvAlipay = mContentView.findViewById(R.id.alipay_img);
    mBtnStartWithdraw = mContentView.findViewById(R.id.start);

    mBtnAlipay.setOnClickListener(this);
    mBtnStartWithdraw.setOnClickListener(this);
    mGuizeTv.setOnClickListener(this);
    mWithdraw.addTextChangedListener(this);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_withdraw_layout;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.start:
        if (checkInput()) {
          AlipayInfoActivity.launch(getActivity(), getInputPrice());
          getActivity().finish();
        }
        break;
      case R.id.guize:
        BrowserActivity.launch(getActivity(), Constants.Server.API_PREFIX + "/page/html?id=4",
            "提现规则");
        break;
    }
  }

  private float getInputPrice() {
    String p = mWithdraw.getText().toString();
    float price = 0;
    try {
      price = Float.parseFloat(p);
    } catch (NumberFormatException e) {
      T.show("请检查输入");
    }
    return price;
  }

  private boolean checkInput() {
    String p = mWithdraw.getText().toString();
    if (TextUtils.isEmpty(p)) {
      mWithdraw.setError("请输入提现金额");
      return false;
    }
    if (getInputPrice() > priceMax) {
      T.show("您最多可提现:" + priceMax + ", 请重新输入");
      return false;
    }

    if (getInputPrice() <= 0) {
      mWithdraw.setError("提现金额必须大于0");
      return false;
    }
    return true;
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(Editable s) {
    float money = 0;
    try {
      money = Float.valueOf(s.toString());
      if(money > 1000){
        money = 1000;
      }
    } catch (Exception e) {

    }
    mCalcTv.setText("实际到账金额：" + getCalcMoney(money));
  }

  /**
   * 提现时收取 平台服务费的标准是
   * 0-200（含200）收10%，
   * 201-500（含500）收5%，
   * 501-1000（含1000）不收；
   * 单次1000元封顶；再提需待下个提现周期
   * @param m
   * @return
   */
  private String getCalcMoney(float m) {
    BigDecimal result = new BigDecimal(String.valueOf(m));
    if (m <= 0) {
      result = new BigDecimal(0);
    } else if(0<m && m<=200){
      result = result.multiply(new BigDecimal(String.valueOf(0.9)));
    } else if(200<m && m<=500){
      result = result.multiply(new BigDecimal(String.valueOf(0.95)));
    } else if(500<m && m<=1000){
      result = new BigDecimal(m);
    }
    result = result.setScale(2, BigDecimal.ROUND_DOWN);
    return result.toString();
  }
}
