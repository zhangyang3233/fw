package com.hongyu.reward.ui.fragment.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.LoginModel;
import com.hongyu.reward.ui.activity.personal.WithdrawActivity;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.CommonTextView;
import com.hongyu.reward.widget.RoundImageView;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class WalletFragment extends BaseLoadFragment implements View.OnClickListener {
  private RoundImageView mHeadImg;
  private TextView mTvName;
  private TextView mTvPrice;
  private CommonTextView mBtnWithdraw;
  private CommonTextView mBtnSetting;
  private CommonTextView mBtnDetail;

  @Override
  protected void onStartLoading() {
    showLoadingView();
    AccountManager.getInstance().requestUserInfo(new AccountManager.GetUserInfoCallback() {
      @Override
      public void getUserInfoSuccess(LoginModel.UserInfo userInfo) {
        if (!isAdded()) {
          return;
        }
        dismissLoadingView();
        mTvName.setText(userInfo.getNickname());
        mTvPrice.setText(String.valueOf(userInfo.getCash()));
        mHeadImg.loadNetworkImageByUrl(userInfo.getHead_img());
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
    mBtnWithdraw = (CommonTextView) mContentView.findViewById(R.id.withdraw);
    mBtnSetting = (CommonTextView) mContentView.findViewById(R.id.setting);
    mBtnDetail = (CommonTextView) mContentView.findViewById(R.id.detail);
    mBtnWithdraw.setOnClickListener(this);
    mBtnSetting.setOnClickListener(this);
    mBtnDetail.setOnClickListener(this);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_wallet_layout;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.withdraw:
        WithdrawActivity.launch(getActivity());
        break;
      case R.id.setting:
        break;
      case R.id.detail:
        break;
    }
  }
}
