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
import com.fw.zycoder.utils.CollectionUtils;
import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.Log;
import com.fw.zycoder.utils.MainThreadPostUtils;
import com.fw.zycoder.utils.NetworkUtil;
import com.fw.zycoder.utils.Spanny;
import com.fw.zycoder.utils.StringUtil;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.AsyncLoadListFragment;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.appbase.fetcher.BaseFetcher;
import com.hongyu.reward.http.HttpHelper;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.interfaces.CityChangedListener;
import com.hongyu.reward.location.GetLocationListener;
import com.hongyu.reward.location.LocationManager;
import com.hongyu.reward.manager.RefreshOrderManager;
import com.hongyu.reward.model.AdListModel;
import com.hongyu.reward.model.AppLocation;
import com.hongyu.reward.model.ShopListMode;
import com.hongyu.reward.request.GetAdListRequestBuilder;
import com.hongyu.reward.ui.activity.RewardPublishInfoActivity;
import com.hongyu.reward.ui.activity.TabHostActivity;
import com.hongyu.reward.ui.activity.personal.MessageListActivity;
import com.hongyu.reward.ui.adapter.ShopListAdapter;
import com.hongyu.reward.ui.city.CityPickerActivity;
import com.hongyu.reward.ui.dialog.CommonTwoBtnDialogFragment;
import com.hongyu.reward.utils.EmptyTipsUtil;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.AppEmptyView;
import com.hongyu.reward.widget.BannerView2;
import com.hongyu.reward.widget.NoticeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

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
public class FragmentMainTabHome extends AsyncLoadListFragment<ShopListMode.ShopInfo>
    implements
      View.OnClickListener {
  private BannerView2 mBannerView;
  private View mRewardPublish;
  private NoticeView mNoticeView;
  private LinearLayout left_container;
  private LinearLayout right_container;
  private View mRewardget;
  private View list_top;
  private TextView title;
  private TextView leftBtn;
  private ImageView rightBtn;
  private CityChangedListener cityChangedListener;

  @Override
  protected int getPageSize() {
    return 3;
  }

  @Override
  protected boolean needLoadMore() {
    return false;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    cityChangedListener = new CityChangedListener() {
      @Override
      public void onCityChanged(String oldCity, String newCity) {
        onPullDownToRefresh();
      }
    };
    LocationManager.getInstance().addCitiChangedListener(cityChangedListener);
    EventBus.getDefault().register(this);
  }


  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
    LocationManager.getInstance().removeCityChangedListener(cityChangedListener);
  }



  protected void onNoFetchResult() {
    EmptyTipsUtil.showEmptyTips(mContentListView,
        StringUtil.getString(getEmptyTipsString()), R.mipmap.empty_icon_small,
        new AppEmptyView.OnEmptyRefreshListener() {
          @Override
          public void onRefresh() {
            if (!NetworkUtil.isNetworkConnected(GlobalConfig.getAppContext())) {
              MainThreadPostUtils.toast(R.string.app_wifi_isopen);
            }
            requestLoad();
          }
        });
  }

  /**
   * called when init data failed
   */
  protected void onLoadingFailed() {
    EmptyTipsUtil.showEmptyTips(mContentListView,
        StringUtil.getString(getEmptyTipsString()), R.mipmap.empty_icon_small,
        new AppEmptyView.OnEmptyRefreshListener() {
          @Override
          public void onRefresh() {
            if (!NetworkUtil.isNetworkConnected(GlobalConfig.getAppContext())) {
              MainThreadPostUtils.toast(R.string.app_wifi_isopen);
            }
            requestLoad();
          }
        });
  }


  @Override
  protected BaseFetcher<ShopListMode.ShopInfo> newFetcher() {
    return new BaseFetcher<ShopListMode.ShopInfo>() {
      @Override
      protected List<ShopListMode.ShopInfo> fetchHttpData(int limit, int page) {
        AppLocation location = LocationManager.getInstance().getLocation();

        if (location == null) {
          Log.e("asdf", "location == null");
          LocationManager.getInstance().addLocationListener(new GetLocationListener() {
            @Override
            public void onSuccess(AppLocation locationInfo) {
              Log.e("asdf", "onSuccess:" + locationInfo.toString());
              onPullDownToRefresh();
            }

            @Override
            public void onFailed(String msg) {
              Log.e("asdf", "onFailed:");
            }
          });
          return null;
        } else {
          Log.e("asdf", "location: " + location.toString());
        }
        String locationStr = location.toString();
        String city = LocationManager.getSavedCity();
        if (city.equals(location.getCity())) { // 当前城市一致,传坐标
          city = null;
        }
        ShopListMode listMode = HttpHelper.getShopList(String.valueOf(page),
            locationStr, city, null);
        if (listMode == null) {
          return null;
        } else if (CollectionUtils.isEmpty(listMode.getData())) {
          return new ArrayList();
        } else {
          return listMode.getData();
        }
      }
    };
  }

  @Override
  protected DataAdapter<ShopListMode.ShopInfo> newContentAdapter() {
    ShopListAdapter adapter = new ShopListAdapter();
    adapter.setmOnItemClickListener(new ShopListAdapter.OnItemClickListener() {
      @Override
      public void itemOnClick(ShopListMode.ShopInfo mode) {
        RewardPublishInfoActivity.launch(getActivity(), mode);
      }
    });
    return adapter;
  }

  @Override
  protected void loadingData() {
    super.loadingData();
    initAd();
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    super.onInflated(contentView, savedInstanceState);
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
          EmptyTipsUtil.hideEmptyTips(mBannerView);
          mBannerView.setData(data.getData());
        } else {
          EmptyTipsUtil.showEmptyTips(mBannerView, "点击重新加载", 0,
              new AppEmptyView.OnEmptyRefreshListener() {
                @Override
                public void onRefresh() {
                  initAd();
                }
              });
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
    mBannerView = (BannerView2) mContentView.findViewById(R.id.convenientBanner);
    mRewardPublish = mContentView.findViewById(R.id.reward_publish);
    mRewardget = mContentView.findViewById(R.id.reward_get);
    left_container = (LinearLayout) mContentView.findViewById(R.id.left_container);
    right_container = (LinearLayout) mContentView.findViewById(R.id.right_container);
    title = (TextView) mContentView.findViewById(R.id.title);
    list_top = mContentView.findViewById(R.id.list_top);
    mRewardPublish.setOnClickListener(this);
    mRewardget.setOnClickListener(this);
    list_top.setOnClickListener(this);
    mNoticeView = (NoticeView) mContentView.findViewById(R.id.notice_view);
    title.setText(R.string.main_tag_text_home);
    RefreshOrderManager.getStatusOrder();
    initLeftBtn();
    initRightBtn();
  }

  private void initRightBtn() {
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

    final AppLocation location = LocationManager.getInstance().getLocation();
    final String savedCity = LocationManager.getSavedCity();
    if (TextUtils.isEmpty(savedCity)) { // 没有定位过城市
      if (location != null && !TextUtils.isEmpty(location.getCity())) {
        leftBtn.setText(location.getCity());
        LocationManager.saveCity(location.getCity());
      } else {
        leftBtn.setText("定位中");
        LocationManager.getInstance().addLocationListener(new GetLocationListener() {
          @Override
          public void onSuccess(AppLocation locationInfo) {
            if (location == null) {
              return;
            }
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
      leftBtn.setText(savedCity);
      if (location == null) {
        LocationManager.getInstance().addLocationListener(new GetLocationListener() {
          @Override
          public void onSuccess(final AppLocation newLocation) {
            if (!isAdded()) {
              return;
            }
            if (!newLocation.getCity().equals(savedCity)) { // 获取的城市和当前城市不相同
              showCityChangedDialog(newLocation);
            }
          }

          @Override
          public void onFailed(String msg) {}
        });
        LocationManager.getInstance().start();
      } else {
        if (!location.getCity().equals(savedCity)) { // 获取的城市和当前城市不相同
          showCityChangedDialog(location);
        }
      }

    }
    left_container.addView(leftBtn);
    ImageView leftImg = new ImageView(getActivity());
    leftImg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT));
    leftImg.setImageResource(R.mipmap.arrow_down);
    left_container.addView(leftImg);
  }

  private void showCityChangedDialog(final AppLocation locationInfo) {
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


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.reward_publish:
      case R.id.list_top:
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

  @Subscribe
  public void onEventMainThread(RefreshOrderManager.Prog prog) {
    if (!isAdded()) {
      return;
    }
    if (mNoticeView != null) {
        mNoticeView.show(prog);
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

  @Override
  public void onResume() {
    super.onResume();
    mBannerView.startTurning(5000);
  }

  @Override
  public void onPause() {
    super.onPause();
    mBannerView.stopTurning();
  }
}
