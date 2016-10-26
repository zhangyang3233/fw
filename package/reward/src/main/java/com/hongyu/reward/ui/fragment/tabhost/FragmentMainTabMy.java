package com.hongyu.reward.ui.fragment.tabhost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.interfaces.LogoutListener;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.LoginModel;
import com.hongyu.reward.model.NoticeEvent;
import com.hongyu.reward.request.GetUserInfoRequestBuilder;
import com.hongyu.reward.ui.activity.LoginActivity;
import com.hongyu.reward.ui.activity.PersonInfoSettingActivity;
import com.hongyu.reward.ui.activity.personal.ContactActivity;
import com.hongyu.reward.ui.activity.personal.MessageListActivity;
import com.hongyu.reward.ui.activity.personal.MyEvaluateActivity;
import com.hongyu.reward.ui.activity.personal.MyOrderActivity;
import com.hongyu.reward.ui.activity.personal.PointActivity;
import com.hongyu.reward.ui.activity.personal.SettingActivity;
import com.hongyu.reward.ui.activity.personal.WalletActivity;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.CommonTextView;
import com.hongyu.reward.widget.RoundImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
public class FragmentMainTabMy extends BaseLoadFragment implements View.OnClickListener, LogoutListener {
  boolean needRequest;
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
  private View head_layout;
  private RoundImageView mHeadImag;
  private TextView mTvName;
  private TextView mTvPrice;
  private TextView mTvScore;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    AccountManager.getInstance().addLogoutListener(this);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    AccountManager.getInstance().removeLogoutListener(this);
  }

  protected void checkLazyLoad() {
    if (isVisible && isPrepared) {
      loadingData();
    }
  }

  @Override
  protected void loadingData() {
    showLoadingView();
    if(AccountManager.getInstance().isLogin()){
      GetUserInfoRequestBuilder builder = new GetUserInfoRequestBuilder();
      builder.setDataCallback(new DataCallback<LoginModel>() {
        @Override
        public void onDataCallback(LoginModel data) {
          if (!isAdded()) {
            return;
          }
          dismissLoadingView();
          if (ResponesUtil.checkModelCodeOK(data)) {
            needRequest = false;
            AccountManager.getInstance().saveUser(data.getData());
          }else{
            T.show(ResponesUtil.getErrorMsg(data));
          }
          refreshUI();
        }
      });
      builder.build().submit();
    }else{
      needRequest = true;
      refreshUI();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if(needRequest){
      loadingData();
    }else{
      refreshUI();
    }
  }

  private void refreshUI() {
    LoginModel.UserInfo userInfo = AccountManager.getInstance().getUser();
    if(userInfo != null){
      mTvName.setText(userInfo.getNickname());
      mTvPrice.setText(userInfo.getCash() + "元");
      mHeadImag.loadNetworkImageByUrl(userInfo.getHead_img());
      mTvScore.setText(userInfo.getPoint() + "积分");
    }else{
      mTvName.setText("未登录");
      mTvPrice.setText(null);
      mHeadImag.setImageResource(R.mipmap.defalut_head_img);
      mTvScore.setText(null);
    }
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    mNextInfo = mContentView.findViewById(R.id.next_info);
    head_layout = mContentView.findViewById(R.id.head_layout);
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


    head_layout.setOnClickListener(this);
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
    if(!AccountManager.getInstance().isLogin()){
      LoginActivity.launch(getActivity());
      return;
    }
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
        PointActivity.launch(getActivity());
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
      case R.id.head_layout: // 我的详情页面
        PersonInfoSettingActivity.launch(getActivity());
        break;
      default:
    }
  }

  private void goToActivity(Intent intent) {
    startActivity(intent);
  }

  @Override
  protected void onStartLoading() {

  }

  @Subscribe
  public void onEventMainThread(NoticeEvent noticeEvent) {
    if (noticeEvent.getType() == NoticeEvent.USER_IMG_CHANGED
        || noticeEvent.getType() == NoticeEvent.USER_NICKNAME_CHANGED
        || noticeEvent.getType() == NoticeEvent.USER_GENDER_CHANGED) {
      loadingData();
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @Override
  public void onLogout() {
    refreshUI();
  }
}

