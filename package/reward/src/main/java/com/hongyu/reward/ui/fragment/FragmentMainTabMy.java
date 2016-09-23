package com.hongyu.reward.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.LoginModel;
import com.hongyu.reward.ui.activity.personal.ContactActivity;
import com.hongyu.reward.ui.activity.personal.MessageListActivity;
import com.hongyu.reward.ui.activity.personal.MyEvaluateActivity;
import com.hongyu.reward.ui.activity.personal.MyOrderActivity;
import com.hongyu.reward.ui.activity.personal.ScoreActivity;
import com.hongyu.reward.ui.activity.personal.SettingActivity;
import com.hongyu.reward.ui.activity.personal.WalletActivity;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.CommonTextView;
import com.hongyu.reward.widget.RoundImageView;

/**
 * aopayun - fragment - 我的 *
 * =======================================
 * Copyright 2015
 * =======================================
 *
 * @author centos
 * @version 1.1.0
 * @since 2016-7-18 下午12:12:25
 */
public class FragmentMainTabMy extends BaseLoadFragment implements View.OnClickListener {
  TextView mTitle;
  LinearLayout mRightContainer;
  LinearLayout mLeftContainer;
  ImageView mRightBtn;
  private CommonTextView mEval;
  private CommonTextView mOrder;
  private CommonTextView mWallet;
  private CommonTextView mScore;
  private CommonTextView mMsg;
  private CommonTextView mContact;
  private CommonTextView mSetting;
  private View mNextInfo;
  private RoundImageView mHeadImag;
  private TextView mTvName;
  private TextView mTvPrice;
  private TextView mTvScore;

  protected void checkLazyLoad() {
    if (isVisible && isPrepared) {
      loadingData();
    }
  }

  @Override
  protected void loadingData() {
    showLoadingView();
    AccountManager.getInstance().getUserInfo(new AccountManager.GetUserInfoCallback() {
      @Override
      public void getUserInfoSuccess(LoginModel.UserInfo userInfo) {
        if (!isAdded()) {
          return;
        }
        dismissLoadingView();
        refreshUI(userInfo);
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

  private void refreshUI(LoginModel.UserInfo userInfo) {
    mTvName.setText(userInfo.getNickname());
    mTvPrice.setText(userInfo.getCash() + "元");
    mTvScore.setText(userInfo.getScore() + "积分");
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    mNextInfo = mContentView.findViewById(R.id.next_info);
    mOrder = (CommonTextView) mContentView.findViewById(R.id.my_order);
    mEval = (CommonTextView) mContentView.findViewById(R.id.my_evaluation);
    mWallet = (CommonTextView) mContentView.findViewById(R.id.my_wallet);
    mScore = (CommonTextView) mContentView.findViewById(R.id.my_score);
    mMsg = (CommonTextView) mContentView.findViewById(R.id.my_msg);
    mContact = (CommonTextView) mContentView.findViewById(R.id.my_contact);
    mSetting = (CommonTextView) mContentView.findViewById(R.id.my_setting);

    mHeadImag = (RoundImageView) mContentView.findViewById(R.id.header_icon);
    mTvName = (TextView) mContentView.findViewById(R.id.nickname);
    mTvPrice = (TextView) mContentView.findViewById(R.id.price);
    mTvScore = (TextView) mContentView.findViewById(R.id.score);


    mOrder.setOnClickListener(this);
    mEval.setOnClickListener(this);
    mWallet.setOnClickListener(this);
    mScore.setOnClickListener(this);
    mMsg.setOnClickListener(this);
    mContact.setOnClickListener(this);
    mSetting.setOnClickListener(this);
    mNextInfo.setOnClickListener(this);
    initTitle();
  }

  private void initTitle() {
    mTitle = (TextView) mContentView.findViewById(R.id.title);
    mRightContainer = (LinearLayout) mContentView.findViewById(R.id.right_container);
    mLeftContainer = (LinearLayout) mContentView.findViewById(R.id.left_container);
    mTitle.setText(R.string.personal_center);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_my;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.my_order: // 我的订单
        MyOrderActivity.launch(getActivity());
        break;
      case R.id.my_evaluation: // 我的评价
        MyEvaluateActivity.launch(getActivity());
        break;
      case R.id.my_wallet: // 我的钱包
        WalletActivity.launch(getActivity());
        break;
      case R.id.my_score: // 积分中心
        ScoreActivity.launch(getActivity());
        break;
      case R.id.my_msg:// 消息中心
        MessageListActivity.launch(getActivity());
        break;
      case R.id.my_contact: // 联系客服
        ContactActivity.launch(getActivity());
        break;
      case R.id.my_setting: // 设置
        SettingActivity.launch(getActivity());
        break;
      // case R.id.next_info: // 我的详情页面
      // intent = new Intent(getActivity(), MyInfoActivity.class);
      // goToActivity(intent);
      // break;
      default:
    }
  }

  private void goToActivity(Intent intent) {
    startActivity(intent);
  }

  @Override
  protected void onStartLoading() {

  }
}

