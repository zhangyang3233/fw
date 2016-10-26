package com.hongyu.reward.ui.fragment.tabhost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fw.zycoder.utils.CollectionUtils;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.AsyncLoadListFragment;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.appbase.fetcher.BaseFetcher;
import com.hongyu.reward.http.HttpHelper;
import com.hongyu.reward.interfaces.CityChangedListener;
import com.hongyu.reward.interfaces.GetLocationListener;
import com.hongyu.reward.manager.LocationManager;
import com.hongyu.reward.model.AppLocation;
import com.hongyu.reward.model.ShopListMode;
import com.hongyu.reward.ui.activity.SearchActivity;
import com.hongyu.reward.ui.activity.ShopOrderListActivity;
import com.hongyu.reward.ui.adapter.ShopListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 领取任务
 * aopayun - fragment - 领取任务商家列表 *
 * =======================================
 * Copyright 2015 yun.aopa.org.cn
 * =======================================
 *
 * @since 2016-7-18 下午12:12:25
 * @author centos
 * @version 1.1.0
 */
public class FragmentMainTabReceive extends AsyncLoadListFragment<ShopListMode.ShopInfo> {
  TextView mTitle;
  LinearLayout mRightContainer;
  LinearLayout mLeftContainer;
  ImageView mRightBtn;
  CityChangedListener cityChangedListener;


  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    super.onInflated(contentView, savedInstanceState);
    initView();
  }

  private void initView() {
    mTitle = (TextView) mContentView.findViewById(R.id.title);
    mRightContainer = (LinearLayout) mContentView.findViewById(R.id.right_container);
    mLeftContainer = (LinearLayout) mContentView.findViewById(R.id.left_container);
    initTitle();
  }

  private void initTitle() {
    mTitle.setText(R.string.title_get_award);
    mRightBtn = (ImageView) LayoutInflater.from(getActivity())
        .inflate(R.layout.search_right_button_layout, null, false);
    mRightContainer.addView(mRightBtn);
    mRightBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SearchActivity.launch(getActivity(), false);
      }
    });
  }

  @Override
  protected BaseFetcher newFetcher() {
    return new BaseFetcher() {
      @Override
      protected List<ShopListMode.ShopInfo> fetchHttpData(int limit, int page) {
        AppLocation location = LocationManager.getInstance().getLocation();
        if(location == null){
          LocationManager.getInstance().addLocationListener(new GetLocationListener() {
            @Override
            public void onSuccess(AppLocation locationInfo) {
              onPullDownToRefresh();
            }

            @Override
            public void onFailed(String msg) {

            }
          });
          return null;
        }
        String locationStr = location.toString();
        String city = LocationManager.getSavedCity();
        if(city.equals(location.getCity())){ // 当前城市一致,传坐标
          city = null;
        }
        ShopListMode listMode = HttpHelper.getReceiveShopList(String.valueOf(page),
                locationStr,city, null);
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
  protected DataAdapter newContentAdapter() {
    ShopListAdapter adapter = new ShopListAdapter();
    adapter.setmOnItemClickListener(new ShopListAdapter.OnItemClickListener() {
      @Override
      public void itemOnClick(ShopListMode.ShopInfo mode) {
        ShopOrderListActivity.launch(getActivity(), mode.getShop_name(), mode.getShop_id());
      }
    });
    return adapter;
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
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    LocationManager.getInstance().removeCityChangedListener(cityChangedListener);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.tab_title_load_list_fragment;
  }

  @Override
  protected int getPageSize() {
    return 10;
  }
}
