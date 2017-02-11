package com.hongyu.reward.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.fw.zycoder.utils.CollectionUtils;
import com.hongyu.reward.appbase.AsyncLoadListFragment;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.appbase.fetcher.BaseFetcher;
import com.hongyu.reward.http.HttpHelper;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.RewardListModel;
import com.hongyu.reward.model.RewardModel;
import com.hongyu.reward.ui.activity.OrderDetailActivity;
import com.hongyu.reward.ui.activity.ShopOrderListActivity;
import com.hongyu.reward.ui.adapter.ShopOrderListAdapter;
import com.hongyu.reward.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据商家查询该商家可领取任务列表
 * Created by zhangyang131 on 16/9/19.
 */
public class ShopOrderListFragment extends AsyncLoadListFragment<RewardModel> {
  String shop_id;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    shop_id = getArguments().getString(ShopOrderListActivity.SHOP_ID);
    if (TextUtils.isEmpty(shop_id)) {
      T.show("商户id为空");
      getActivity().finish();
      return;
    }
  }

  @Override
  protected BaseFetcher<RewardModel> newFetcher() {
    return new BaseFetcher<RewardModel>() {
      @Override
      protected List<RewardModel> fetchHttpData(int limit, int page) {
        RewardListModel listMode = HttpHelper.getShopOrderList(String.valueOf(page), shop_id);
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
  protected DataAdapter<RewardModel> newContentAdapter() {
    ShopOrderListAdapter adapter = new ShopOrderListAdapter();
    adapter.setmOnItemClickListener(new ShopOrderListAdapter.OnItemClickListener() {
      @Override
      public void itemOnClick(RewardModel mode) {
        if(mode.getUser_id().equals(AccountManager.getInstance().getUser().getUser_id())){
          Toast.makeText(getActivity(), "您不可以领取自己发布的悬赏", Toast.LENGTH_LONG).show();
          return;
        }
        OrderDetailActivity.launch(getActivity(), mode.getShop_name(), mode.getImg(),
            mode.getOrder_id(), mode.getNickname(), mode.getPrice(), mode.getShop_id(),
            mode.getUser_id());
        getActivity().finish();
      }
    });
    return adapter;
  }
}
