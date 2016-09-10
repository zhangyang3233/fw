package com.hongyu.reward.appbase.fetcher;

import java.util.List;

/**
 * Usage: Helps ui to show fetch data.
 */
public class FetchHelper<T> {
  private static final int DEFAULT_PAGE_SIZE = 20;
  private final BaseFetcher<T> fetcher;
  private final BaseFetcher.Callback<T> callback;
  private final BaseFetcher.Callback<T> callbackProxy;
  protected int currentPage = 0;
  private int pageSize;
  private boolean hasMore;

  public FetchHelper(BaseFetcher<T> fetcher, BaseFetcher.Callback<T> callback) {
    this(fetcher, callback, DEFAULT_PAGE_SIZE);
  }

  public void fetch() {
    fetcher.fetch(pageSize, currentPage, callbackProxy);
  }

  public void fetchMore() {
    fetcher.fetch(pageSize, currentPage + 1, callbackProxy);

  }

  public FetchHelper(BaseFetcher<T> fetcher, BaseFetcher.Callback<T> callback, int pageSize) {
    this.fetcher = fetcher;
    this.callback = callback;
    this.pageSize = pageSize;
    this.callbackProxy = new CustomCallback();
    this.hasMore = true;
  }

  /**
   * Move current index to a new page.
   * 
   * @param page new position.
   */
  public void moveToPosition(int page) {
    this.currentPage = page;
  }

  public BaseFetcher<T> getFetcher() {
    return fetcher;
  }

  public boolean hasMore() {
    return hasMore;
  }

  /**
   * some demand need to change pageSize dynamic
   */
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  private class CustomCallback implements BaseFetcher.Callback<T> {
    @Override
    public void onFetched(int limit, int page, List<T> result) {
      hasMore = !(result != null && result.isEmpty());
      currentPage = page;
      callback.onFetched(limit, page, result);
    }

    @Override
    public void onFailed(int limit, int page) {
      callback.onFailed(limit, page);
    }
  }
}
