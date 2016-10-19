package com.hongyu.reward.ui.fragment.tabhost;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.Spanny;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.interfaces.GetLocationListener;
import com.hongyu.reward.manager.LocationManager;
import com.hongyu.reward.manager.RefreshOrderManager;
import com.hongyu.reward.model.AdListModel;
import com.hongyu.reward.model.AppLocation;
import com.hongyu.reward.request.GetAdListRequestBuilder;
import com.hongyu.reward.ui.activity.TabHostActivity;
import com.hongyu.reward.ui.activity.personal.MessageListActivity;
import com.hongyu.reward.ui.city.CityPickerActivity;
import com.hongyu.reward.ui.dialog.CommonTwoBtnDialogFragment;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.BannerView;
import com.hongyu.reward.widget.NoticeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * fragment - home - 主页
 * <p>
 * =======================================
 * Copyright 2016-2017
 * =======================================
 *
 * @author centos
 * @since 2016-7-18 上午6:38:00
 */
public class FragmentMainTabHome extends BaseLoadFragment implements View.OnClickListener {
  private BannerView mBannerView;
  private View mRewardPublish;
  private NoticeView mNoticeView;
  private LinearLayout left_container;
  private LinearLayout right_container;
  private View mRewardget;
  private TextView title;
  private TextView leftBtn;
  private ImageView rightBtn;

  @Override
  protected void onStartLoading() {}

  @Override
  protected void loadingData() {
    initAd();
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initAd() {
    GetAdListRequestBuilder builder = new GetAdListRequestBuilder(String.valueOf(1));
    builder.setDataCallback(new DataCallback<AdListModel>() {
      @Override
      public void onDataCallback(AdListModel data) {
        if (!isAdded()) {
          return;
        }
        if (ResponesUtil.checkModelCodeOK(data)) {
          mBannerView.setData(data.getData());
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_home;
  }

  private void initView() {
    mBannerView = (BannerView) mContentView.findViewById(R.id.banner_layout);
    mRewardPublish = mContentView.findViewById(R.id.reward_publish);
    mRewardget = mContentView.findViewById(R.id.reward_get);
    left_container = (LinearLayout) mContentView.findViewById(R.id.left_container);
    right_container = (LinearLayout) mContentView.findViewById(R.id.right_container);
    title = (TextView) mContentView.findViewById(R.id.title);
    mRewardPublish.setOnClickListener(this);
    mRewardget.setOnClickListener(this);
    mNoticeView = (NoticeView) mContentView.findViewById(R.id.notice_view);
    title.setText(R.string.main_tag_text_home);
    RefreshOrderManager.getStatusOrder();
    initLeftBtn();
    initRightBtn();
  }

  private void initRightBtn(){
    rightBtn = new ImageView(getActivity());
    rightBtn.setImageResource(R.mipmap.xf);
    rightBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MessageListActivity.launch(getActivity());
      }
    });
    right_container.addView(rightBtn);
  }

  private void initLeftBtn() {
    leftBtn = new TextView(getActivity());
    leftBtn.setTextColor(getResources().getColor(R.color.white));
    leftBtn.setTextSize(18);
    leftBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        CityPickerActivity.launchForResult(FragmentMainTabHome.this,
            CityPickerActivity.REQUEST_CODE_PICK_CITY);
      }
    });
    String currentCity = LocationManager.getLocationCity();
    if (TextUtils.isEmpty(currentCity)) { // 没有定位过城市
      final AppLocation location = LocationManager.getInstance().getLocalLocationInfo();
      if (location != null && !TextUtils.isEmpty(location.getCity())) {
        leftBtn.setText(location.getCity());
        LocationManager.saveCity(location.getCity());
      } else {
        LocationManager.getInstance().addLocationListener(new GetLocationListener() {
          @Override
          public void onSuccess(AppLocation locationInfo) {
            LocationManager.getInstance().removeLocationListener(this);
            leftBtn.setText(location.getCity());
            LocationManager.saveCity(location.getCity());
          }

          @Override
          public void onFailed(String msg) {

          }
        });
        LocationManager.getInstance().start();
      }
    } else { // 以前定位过城市
      leftBtn.setText(currentCity);
      LocationManager.getInstance().addLocationListener(new GetLocationListener() {
        @Override
        public void onSuccess(final AppLocation locationInfo) {
          if (!isAdded()) {
            return;
          }
          LocationManager.getInstance().removeLocationListener(this);
          if (!locationInfo.getCity().equals(LocationManager.getLocationCity())) { // 获取的城市和当前城市不相同
            CommonTwoBtnDialogFragment dialog = new CommonTwoBtnDialogFragment();
            Spanny sp = new Spanny("定位到您所在城市:\n");
            sp.append(locationInfo.getCity(),
                new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)));
            dialog.setContent(sp);
            dialog.setLeft(getString(R.string.dialog_cancel),
                new CommonTwoBtnDialogFragment.OnClickListener() {
                  @Override
                  public void onClick(Dialog dialog) {
                    dialog.dismiss();
                  }
                });
            dialog.setRight(getString(R.string.city_changed),
                new CommonTwoBtnDialogFragment.OnClickListener() {
                  @Override
                  public void onClick(Dialog dialog) {
                    dialog.dismiss();
                    leftBtn.setText(locationInfo.getCity());
                    LocationManager.saveCity(locationInfo.getCity());
                  }
                });
            dialog.show(getChildFragmentManager(), FragmentMainTabHome.class.getSimpleName());
          }
        }

        @Override
        public void onFailed(String msg) {

        }
      });
      LocationManager.getInstance().start();
    }
    left_container.addView(leftBtn);
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.reward_publish:
        switchTabTo(1);
        break;
      case R.id.reward_get:
        switchTabTo(2);
        break;
    }
  }

  private void switchTabTo(int index) {
    TabHostActivity activity = (TabHostActivity) getActivity();
    activity.switchPage(index);
  }


  @Override
  public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
  }

  @Subscribe
  public void onEventMainThread(RefreshOrderManager.Prog prog) {
    if (!isAdded()) {
      return;
    }
    if (mNoticeView != null) {
      if (prog == null ||  TextUtils.isEmpty(prog.getOrderId())) {
        mNoticeView.hide();
      } else {
        mNoticeView.show(prog);
      }
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == CityPickerActivity.REQUEST_CODE_PICK_CITY
        && resultCode == Activity.RESULT_OK) {
      String selectCity = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
      if (!TextUtils.isEmpty(selectCity)) {
        leftBtn.setText(selectCity);
          LocationManager.saveCity(selectCity);
      }
    }
  }
}
