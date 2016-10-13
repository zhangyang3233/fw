package com.hongyu.reward.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.HotSearchModel;
import com.hongyu.reward.model.dbmodel.SearchModel;
import com.hongyu.reward.request.GetHotSearchRequestBuilder;
import com.hongyu.reward.ui.activity.SearchActivity;
import com.hongyu.reward.ui.activity.SearchShopActivity;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.HistorySearchView;
import com.hongyu.reward.widget.HotSearchView;

/**
 * Created by zhangyang131 on 16/10/13.
 */
public class SearchFragment extends BaseLoadFragment
    implements
      HistorySearchView.HistorySearchClicked,
      HotSearchView.TagClickedListener {
  HotSearchView mHotSearchView;
  HistorySearchView mHistorySearchView;
  boolean isPublish;

  @Override
  protected void onStartLoading() {
    GetHotSearchRequestBuilder builder = new GetHotSearchRequestBuilder();
    builder.setDataCallback(new DataCallback<HotSearchModel>() {
      @Override
      public void onDataCallback(HotSearchModel data) {
        if (!isAdded()) {
          return;
        }
        if (ResponesUtil.checkModelCodeOK(data)) {
          mHotSearchView.setHots(data.getData().getTag());
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    mHotSearchView = (HotSearchView) mContentView.findViewById(R.id.hot_search_view);
    mHistorySearchView = (HistorySearchView) mContentView.findViewById(R.id.history_search_view);
    mHistorySearchView.setHistorySearchClicked(this);
    mHotSearchView.setmTagClickedListener(this);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.search_start_layout;
  }

  @Override
  public void historySearchClicked(String historyStr) {
    SearchShopActivity.launch(getActivity(), historyStr, isPublish);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    isPublish = getArguments().getBoolean(SearchActivity.PUBLISH);
  }

  @Override
  public void onResume() {
    super.onResume();
    mHistorySearchView.refresh();
  }

  @Override
  public void tagClicked(String str) {
    SearchModel searchModel = new SearchModel(str);
    searchModel.saveIfTagNotExist();
    SearchShopActivity.launch(getActivity(), str, isPublish);
  }

}
