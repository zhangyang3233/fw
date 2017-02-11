package com.hongyu.reward.ui.fragment.personal;

import android.os.Bundle;

import com.fw.zycoder.utils.CollectionUtils;
import com.fw.zycoder.utils.Log;
import com.fw.zycoder.utils.StringUtil;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.AsyncLoadListFragment;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.appbase.fetcher.BaseFetcher;
import com.hongyu.reward.http.HttpHelper;
import com.hongyu.reward.model.OrderListModel;
import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.ui.adapter.OrderListAdapter;
import com.hongyu.reward.utils.EmptyTipsUtil;
import com.hongyu.reward.widget.AppEmptyView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyang131 on 16/9/20.
 */
public class OrderListFragment extends AsyncLoadListFragment<OrderModel> {
  public static final String TYPE = "type";
  public static final String ISME = "isme";
  int type;
  int isme; // 0我发出的 1我收到的

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    type = getArguments().getInt(TYPE);
    isme = getArguments().getInt(ISME);
  }

  @Override
  protected int getPageSize() {
    return 10;
  }

  @Override
  protected BaseFetcher<OrderModel> newFetcher() {
    return new BaseFetcher<OrderModel>() {
      @Override
      protected List<OrderModel> fetchHttpData(int limit, int page) {
        Log.e("type:" + type);
        Log.e("isme:" + isme);
        OrderListModel listMode = HttpHelper.getOrderList(String.valueOf(type),
            String.valueOf(isme), String.valueOf(page));
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
  protected DataAdapter<OrderModel> newContentAdapter() {
    return new OrderListAdapter(getActivity(), isme, type);
  }


  @Override
  protected void onNoFetchResult() {
    EmptyTipsUtil.showEmptyTips(mContentListView,
        StringUtil.getString(getEmptyTipsString()),
        new AppEmptyView.OnEmptyRefreshListener() {
          @Override
          public void onRefresh() {
            requestLoad();
          }
        });
  }

  @Override
  protected int getEmptyTipsString() {
    if(type == 20){
      return R.string.order_empty_20;
    }else if(type == 30){
      return R.string.order_empty_30;
    }else if(type == 40){
      return R.string.order_empty_40;
    }
    return R.string.order_empty;
  }

  @Override
  protected void onLoadingFailed() {
    super.onLoadingFailed();
  }
}
