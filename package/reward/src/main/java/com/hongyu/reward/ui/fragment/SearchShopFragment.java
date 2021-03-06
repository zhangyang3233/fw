package com.hongyu.reward.ui.fragment;

import android.os.Bundle;

import com.fw.zycoder.utils.CollectionUtils;
import com.hongyu.reward.appbase.AsyncLoadListFragment;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.appbase.fetcher.BaseFetcher;
import com.hongyu.reward.http.HttpHelper;
import com.hongyu.reward.location.LocationManager;
import com.hongyu.reward.model.AppLocation;
import com.hongyu.reward.model.ShopListMode;
import com.hongyu.reward.ui.activity.RewardPublishInfoActivity;
import com.hongyu.reward.ui.activity.SearchActivity;
import com.hongyu.reward.ui.activity.SearchShopActivity;
import com.hongyu.reward.ui.activity.ShopOrderListActivity;
import com.hongyu.reward.ui.adapter.ShopListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyang131 on 16/10/13.
 */
public class SearchShopFragment extends AsyncLoadListFragment<ShopListMode.ShopInfo> {
  boolean isPublish;
  String shop_name;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    shop_name = getArguments().getString(SearchShopActivity.SHOP_NAME);
    isPublish = getArguments().getBoolean(SearchActivity.PUBLISH);
  }

  @Override
  protected BaseFetcher<ShopListMode.ShopInfo> newFetcher() {
    return new BaseFetcher<ShopListMode.ShopInfo>() {
      @Override
      protected List<ShopListMode.ShopInfo> fetchHttpData(int limit, int page) {
        ShopListMode listMode;
        AppLocation location = LocationManager.getInstance().getLocation();
        if(location == null){
          return null;
        }
        String locationStr = location.toString();
        String city = LocationManager.getSavedCity();
        if (isPublish) {
          listMode = HttpHelper.getShopList(String.valueOf(page),
                  locationStr, city, shop_name);

        } else {
          listMode = HttpHelper.getReceiveShopList(String.valueOf(page),
                  locationStr, city, shop_name);
        }
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
        if (isPublish) {
          RewardPublishInfoActivity.launch(getActivity(), mode);
        } else {
          ShopOrderListActivity.launch(getActivity(), mode.getShop_name(), mode.getShop_id());
        }
      }
    });
    return adapter;
  }
}
