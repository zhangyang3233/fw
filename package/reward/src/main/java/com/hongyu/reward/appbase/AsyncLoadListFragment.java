package com.hongyu.reward.appbase;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fw.zycoder.utils.CollectionUtils;
import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.MainThreadPostUtils;
import com.fw.zycoder.utils.NetworkUtil;
import com.fw.zycoder.utils.StringUtil;
import com.fw.zycoder.utils.ViewUtils;
import com.handmark.pulltorefresh.library.HeaderAndFooterGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.appbase.fetcher.BaseFetcher;
import com.hongyu.reward.appbase.fetcher.FetchHelper;
import com.hongyu.reward.utils.EmptyTipsUtil;
import com.hongyu.reward.utils.TipsViewUtil;
import com.hongyu.reward.utils.TipsViewUtil.TipsType;
import com.hongyu.reward.widget.AppEmptyView;
import com.hongyu.reward.widget.CommonBottomView;
import com.zycoder.uicomp.multicolumn.InternalAbsListView;
import com.zycoder.uicomp.multicolumn.InternalListView;
import com.zycoder.uicomp.multicolumn.MultiColumnListView;

import java.util.List;


public abstract class AsyncLoadListFragment<M extends Object> extends BaseLoadFragment {

  private static final int DEFAULT_PAGE_SIZE = 20;
  private static final int DEFAULT_START_PAGE = 1;
  private static final int DEFAULT_PRE_LOAD_GAP_SIZE = 15;
  private static final boolean DEFAULT_HAS_FILTER = false;
  private static final boolean DEFAULT_NEED_RESTORE_SCROLL_STATE = false;
  private static final String EXTRA_LIST_STATE = "zycoder.fw.intent.extra.LIST_STATE";
  protected DataAdapter<M> mContentAdapter;
  protected PullToRefreshBase<? extends View> mContentListView;
  protected CommonBottomView mBottomView;
  private List<M> mNewData;
  private FetchHelper<M> mFetchHelper;
  private int mLastTryFetch;
  private boolean mLoadedGapData;
  private boolean mIsPullToRefreshing;
  private boolean mIsScrolling;
  private boolean mFetchFail;
  private boolean mIsMatchPageSize = true;
  protected InternalAbsListView.OnScrollListener mOnScrollListener =
      new InternalAbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(InternalAbsListView view, int scrollState) {
          scrollStateChanged(SCROLL_STATE_IDLE == scrollState,
              SCROLL_STATE_TOUCH_SCROLL == scrollState);
        }

        @Override
        public void onScroll(InternalAbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
          scroll(firstVisibleItem, visibleItemCount, totalItemCount);
        }
      };
  private Parcelable mListState;
  private BaseFetcher.Callback<M> mFetchCallback = new BaseFetcher.Callback<M>() {
    @Override
    public void onFetched(int limit, int page, List<M> result) {
      AsyncLoadListFragment.this.onFetched(limit, page, result);
    }

    @Override
    public void onFailed(int limit, int page) {
      AsyncLoadListFragment.this.onFailed(limit, page);
    }
  };
  private AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
      scrollStateChanged(SCROLL_STATE_IDLE == scrollState,
          SCROLL_STATE_TOUCH_SCROLL == scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
        int totalItemCount) {
      scroll(firstVisibleItem, visibleItemCount, totalItemCount);
    }
  };

  protected void scrollStateChanged(boolean isStateIde, boolean isStateTouchScroll) {
    if (isStateIde) {
      if (needPreLoad()) {
        if (mNewData != null) {
          mContentAdapter.setData(mNewData);
          mNewData = null;
        }
        mIsScrolling = false;
      }
      onScrollIdle();
    } else {
      if (needPreLoad()) {
        mIsScrolling = true;
      }
      // 手还在ListView上
      if (isStateTouchScroll) {
        onScrollTouch();
      }
    }
  }

  protected void scroll(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    judgePosition(visibleItemCount + firstVisibleItem, totalItemCount);
    if (!needLoadMore()) {
      return;
    }
    if (needPreLoad()) {
      if (visibleItemCount + firstVisibleItem == totalItemCount
          && mContentAdapter.getCount() > 0) {
        // if user scrolls the listView too fast, fetcher has fetched the data and hasn't
        // notify the data has changed, need update the listView when scroll to the end.
        if (mNewData != null) {
          mContentAdapter.setData(mNewData);
          mNewData = null;
        }
        // if the first fetched total size is smaller than the visible item,
        // need load more data to show.
        if (!mLoadedGapData) {
          onLoadingMore();
        }
      } else if (visibleItemCount + firstVisibleItem >= totalItemCount - getPreLoadGap()
          && mContentAdapter.getCount() > 0) {

        if (totalItemCount <= mLastTryFetch) {
          return;
        }
        // user has scroll the list view and reach the gap item, first loadedGap set true.
        // doesn't need load more data when reach the end next time.
        mLoadedGapData = true;
        mLastTryFetch = totalItemCount;
        onLoadingMore();
      }
    } else {
      if (visibleItemCount + firstVisibleItem == totalItemCount
          && mContentAdapter.getCount() > 0) {
        if (totalItemCount <= mLastTryFetch) {
          return;
        }
        mLastTryFetch = totalItemCount;
        onLoadingMore();
      }
    }
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.base_async_load_list_fragment;
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    mContentListView =
        (PullToRefreshBase) contentView.findViewById(R.id.common_fragment_listview);
    mContentListView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
    mContentListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
      @Override
      public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        if (!mIsPullToRefreshing) {
          mIsPullToRefreshing = true;
          AsyncLoadListFragment.this.onPullDownToRefresh();
        }
      }

      @Override
      public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        // do nothing
      }
    });
    mBottomView = CommonBottomView.newInstance(mContentView.getContext());
    initBottomView(mBottomView.getBottomView());

    mContentAdapter = newContentAdapter();

    if (mContentListView.getRefreshableView() == null) {
      return;
    }
    setContentAdapter();
    mIsScrolling = false;
    mLoadedGapData = false;
  }

  protected void setContentAdapter() {
    if (mContentListView.getRefreshableView() instanceof AbsListView) {
      AbsListView listView = (AbsListView) mContentListView.getRefreshableView();
      // add footer
      LinearLayout linearLayout = new LinearLayout(getActivity());
      linearLayout.addView(mBottomView);
      // 解决mBottomView.setVisibale(Gone)失效问题
      initFooter(listView, linearLayout);
      listView.setAdapter(mContentAdapter);
      // doesn't show scrollbar in listview.
      listView.setVerticalScrollBarEnabled(false);
      listView.setOnScrollListener(mScrollListener);
    } else if (mContentListView.getRefreshableView() instanceof MultiColumnListView) {
      MultiColumnListView listView = (MultiColumnListView) mContentListView.getRefreshableView();
      // add footer
      initFooter(listView, mBottomView);
      listView.setAdapter(mContentAdapter);
      // doesn't show scrollbar in listview.
      listView.setVerticalScrollBarEnabled(false);
      listView.setOnScrollListener(mOnScrollListener);
    }
  }

  private void onLoadingMore() {
    if (needToLoadMore()) {
      getFetchHelper().fetchMore();
    }
  }

  /**
   * the impl of AbsListView maybe not has addFooterView().
   * such as {@link android.widget.GridView}
   */
  protected void initFooter(View listView, View footerView) {
    if (listView instanceof ListView) {
      ((ListView) listView).addFooterView(footerView);
    } else if (listView instanceof MultiColumnListView) {
      ((MultiColumnListView) listView).addFooterView(footerView);
    } else if (listView instanceof HeaderAndFooterGridView) {
      ((HeaderAndFooterGridView) listView).addFooterView(footerView);
    }
  }

  protected boolean needToLoadMore() {
    return true;
  }

  protected boolean needPreLoad() {
    return false;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null && getNeedReStoreScrollState()) {
      mListState = savedInstanceState.getParcelable(EXTRA_LIST_STATE);
    } else {
      mListState = null;
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    if (outState != null && mContentListView != null && getListView() != null) {
      outState.putParcelable(EXTRA_LIST_STATE, ((AbsListView) getRefreshableView())
          .onSaveInstanceState());
    }
    super.onSaveInstanceState(outState);
  }


  @Override
  protected void onPrepareLoading() {
    TipsViewUtil.showTipsView(mContentListView, TipsViewUtil.TipsType.LOADING);
    EmptyTipsUtil.hideEmptyTips(mContentListView);
  }

  @Override
  protected void onStartLoading() {
    getFetchHelper().fetch();
  }


  protected FetchHelper<M> getFetchHelper() {
    if (mFetchHelper == null) {
      mFetchHelper = newFetchHelper();
    }
    return mFetchHelper;
  }

  protected FetchHelper<M> newFetchHelper() {
    FetchHelper<M> fetchHelper;
    if (getPageSize() != 0) {
      fetchHelper = new FetchHelper<>(newFetcher(), mFetchCallback, getPageSize());
    } else {
      fetchHelper = new FetchHelper<>(newFetcher(), mFetchCallback);
    }
    fetchHelper.moveToPosition(getInitPage());
    return fetchHelper;
  }

  protected abstract BaseFetcher<M> newFetcher();

  protected abstract DataAdapter<M> newContentAdapter();

  /**
   * called when network callback is success but have no data
   */
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

  protected int getEmptyTipsString() {
    return R.string.base_default_empty_data_message;
  }

  /**
   * called when init data failed
   */
  protected void onLoadingFailed() {
    EmptyTipsUtil.showEmptyTips(mContentListView, new AppEmptyView.OnEmptyRefreshListener() {
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
   * called when load more failed
   */
  protected void onLoadingMoreFailed() {
    mBottomView.setVisibility(View.GONE);
    // Snackbar
    // .make(mContentListView, R.string.loading_more_error, Snackbar.LENGTH_LONG)
    // .setAction(R.string.retry, new View.OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // mFetchHelper.fetchMore();
    // showLoadingMoreView();
    // }
    // }).show();
  }

  /**
   * need restore scroll position
   *
   * @return
   */
  protected boolean getNeedReStoreScrollState() {
    return DEFAULT_NEED_RESTORE_SCROLL_STATE;
  }

  /**
   * @return if server have filter strategy
   */
  protected boolean getHasFilter() {
    return DEFAULT_HAS_FILTER;
  }

  protected int getInitPage() {
    return DEFAULT_START_PAGE;
  }

  protected int getPageSize() {
    return DEFAULT_PAGE_SIZE;
  }

  /**
   * when item is coming to the gap , it will be trigger pre-load
   *
   * @return
   */
  protected int getPreLoadGap() {
    return DEFAULT_PRE_LOAD_GAP_SIZE;
  }

  /**
   * init loading view
   *
   * @param targetView which the loading attach
   */
  protected void initBottomView(View targetView) {
    TipsViewUtil.showTipsView(targetView, TipsType.LOADING_MORE);
  }

  /**
   * show when this is no data any more
   */
  protected void showNoMoreView() {
    mBottomView.setVisibility(View.GONE);
  }

  /**
   * show when data is coming back or retry fetch
   */
  protected void showLoadingMoreView() {
    mBottomView.setVisibility(View.VISIBLE);
  }

  protected void onFetched(int limit, int page, List<M> result) {
    TipsViewUtil.hideTipsView(mContentListView, TipsType.LOADING);
    if (mIsPullToRefreshing) {
      mIsPullToRefreshing = false;
      mContentListView.onRefreshComplete();
      onRefreshComplete();
    }


    if (!CollectionUtils.isEmpty(result)) {
      EmptyTipsUtil.hideEmptyTips(mContentListView);
      mNewData =
          CollectionUtils.replaceFromPosition(mContentAdapter.getData(), result,
              limit * (page - getInitPage()));
      if (needPreLoad()) {
        // need notification data has changed as following scenarios
        // 1. The first time fetched data need to notification the data has changed right now.
        // 2. fetched data when scroll is idle.
        if (page == getInitPage() || !mIsScrolling) {
          mContentAdapter.setData(mNewData);
          mNewData = null;
        }
      } else {
        mContentAdapter.setData(mNewData);
        mNewData = null;
      }
      judgeResultListMatchSize(result.size(), limit);
      if (needLoadMore()) {
        showLoadingMoreView();
      } else {
        showNoMoreView();
      }

    } else {
      // First page, there is no data in the page.
      if (page == getInitPage()) {
        onNoFetchResult();
      } else {
        showNoMoreView();
      }
    }

    if (mListState != null) {
      getListView().onRestoreInstanceState(mListState);
      mListState = null;
    }

  }

  protected void onFailed(int limit, int page) {
    if (page == getInitPage()) {
      onLoadingFailed();
      TipsViewUtil.hideTipsView(mContentListView, TipsType.LOADING);
    } else {
      onLoadingMoreFailed();
      mFetchFail = true;
    }

  }

  /**
   * judge need load more again when load more failed
   */
  private void judgePosition(int lastVisibleItem, int totalItem) {
    if (mFetchFail && lastVisibleItem < totalItem - 2) {
      mFetchFail = false;
      mLastTryFetch = mLastTryFetch - getPageSize();
      showLoadingMoreView();
    }
  }

  /**
   * judge if result size is full page
   *
   * @param resultSize
   * @param pageSize
   */
  private void judgeResultListMatchSize(int resultSize, int pageSize) {
    if (resultSize < pageSize) {
      mIsMatchPageSize = false;
    } else {
      mIsMatchPageSize = true;
    }
  }

  /**
   * if has filter in server , we should load more whatever last page is full or not
   *
   * @return
   */
  protected boolean needLoadMore() {
    return mIsMatchPageSize || getHasFilter();
  }

  /**
   * refreshable view maybe not listview . maybe {@link android.widget.GridView}
   */
  protected ListView getListView() {
    if (mContentListView.getRefreshableView() instanceof ListView) {
      return (ListView) mContentListView.getRefreshableView();
    }
    return null;
  }

  protected View getRefreshableView() {
    return mContentListView.getRefreshableView();
  }

  /**
   * when pull to refresh called
   */
  protected void onPullDownToRefresh() {
    resetToFirstPosition();
    getFetchHelper().fetch();
  }

  /**
   * load first page data
   */
  protected void resetToFirstPosition() {
    getFetchHelper().moveToPosition(getInitPage());
    mLastTryFetch = 0;
  }

  /**
   * move to list top
   */
  protected void moveToFirstPage() {
    if (mContentListView.getRefreshableView() instanceof AbsListView) {
      ViewUtils.scrollToTop((AbsListView) mContentListView.getRefreshableView());
    } else if (mContentListView.getRefreshableView() instanceof InternalListView) {
      ViewUtils.scrollToTop((InternalListView) mContentListView.getRefreshableView());
    }
  }

  /**
   *
   * 在用{@link com.zycoder.uicomp.draglayout.DragFullViewLayout},检测listview是否能下拉
   * 
   * @return
   */
  public boolean needPullDown() {
    if (getListView() != null) {
      return com.zycoder.uicomp.draglayout.TouchHelper.isListViewNeedPullDown(getListView());
    }
    return false;
  }

  protected M getItemModel(int position) {
    return mContentAdapter.getItem(position);
  }

  /**
   * when refresh complete
   */
  protected void onRefreshComplete() {}

  /**
   * Stop sliding and have left the scrollview
   */
  protected void onScrollIdle() {}

  /**
   * Touch the scrollview
   */
  protected void onScrollTouch() {}

}


