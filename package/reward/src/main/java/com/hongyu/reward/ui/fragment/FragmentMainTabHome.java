package com.hongyu.reward.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.AdListModel;
import com.hongyu.reward.request.GetAdListRequestBuilder;
import com.hongyu.reward.ui.activity.BrowserActivity;
import com.hongyu.reward.ui.activity.TabHostActivity;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.BannerView;
import com.hongyu.reward.widget.NetImageView;

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
  private View mRewardget;
  private TextView title;
  private NetImageView mMainImage;

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
        if(!isAdded()){
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

    GetAdListRequestBuilder builder2 = new GetAdListRequestBuilder(String.valueOf(2));
    builder2.setDataCallback(new DataCallback<AdListModel>() {
      @Override
      public void onDataCallback(final AdListModel data) {
        if(!isAdded()){
          return;
        }
        if (ResponesUtil.checkModelCodeOK(data) && data.getData() != null
            && data.getData().size() != 0
            && data.getData().get(0) != null
            && !TextUtils.isEmpty(data.getData().get(0).getPosition_url())) {
          mMainImage.loadNetworkImageByUrl(data.getData().get(0).getPosition_img());
          mMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              BrowserActivity.launch(getActivity(), data.getData().get(0).getPosition_url());
            }
          });
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder2.build().submit();
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_home;
  }

  private void initView() {
    mBannerView = (BannerView) mContentView.findViewById(R.id.banner_layout);
    mRewardPublish = mContentView.findViewById(R.id.reward_publish);
    mRewardget = mContentView.findViewById(R.id.reward_get);
    title = (TextView) mContentView.findViewById(R.id.title);
    mRewardPublish.setOnClickListener(this);
    mRewardget.setOnClickListener(this);
    mMainImage = (NetImageView) mContentView.findViewById(R.id.main_image);
    title.setText(R.string.main_tag_text_home);
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
}
