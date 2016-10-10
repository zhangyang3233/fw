package com.hongyu.reward.ui.fragment.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.LoginModel;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.RoundImageView;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class WithdrawFragment extends BaseLoadFragment implements View.OnClickListener {
  private RoundImageView mHeadImg;
  private TextView mTvName;
  private TextView mTvPrice;
  private EditText mWithdraw;
  private View mBtnAlipay;
  private View mIvAlipay;
  private View mBtnStartWithdraw;

  @Override
  protected void onStartLoading() {
    AccountManager.getInstance().requestUserInfo(new AccountManager.GetUserInfoCallback() {
      @Override
      public void getUserInfoSuccess(LoginModel.UserInfo userInfo) {
        if (!isAdded()) {
          return;
        }
        dismissLoadingView();
        mTvName.setText(userInfo.getNickname());
        mTvPrice.setText(String.valueOf(userInfo.getCash()));
        mWithdraw.setHint("本次可提现" + (userInfo.getCash() - userInfo.getLock_cash()) + "元");
      }

      @Override
      public void getUserInfoFailed(String msg) {
        if (!isAdded()) {
          return;
        }
        dismissLoadingView();
        T.show(msg);
      }
    });
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    mHeadImg = (RoundImageView) mContentView.findViewById(R.id.header_icon);
    mTvName = (TextView) mContentView.findViewById(R.id.name);
    mTvPrice = (TextView) mContentView.findViewById(R.id.price);

    mWithdraw = (EditText) mContentView.findViewById(R.id.withdraw_price);
    mBtnAlipay = mContentView.findViewById(R.id.btn_alipay);
    mIvAlipay = mContentView.findViewById(R.id.alipay_img);
    mBtnStartWithdraw = mContentView.findViewById(R.id.start);

    mBtnAlipay.setOnClickListener(this);
    mBtnStartWithdraw.setOnClickListener(this);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_withdraw_layout;
  }

  @Override
  public void onClick(View v) {

  }
}
