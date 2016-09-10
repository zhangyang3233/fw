package com.hongyu.reward.appbase.fetcher;

import com.fw.zycoder.utils.MainThreadPostUtils;
import com.hongyu.reward.utils.ThreadPool;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A helper class to make getting content from network more convenient.
 *
 */
public abstract class BaseFetcher<T> {

  private final Set<String> runningSet;
  private final Set<Runnable> runnables;

  protected BaseFetcher() {
    runningSet = new HashSet<>();
    runnables = new HashSet<>();
  }

  // generate an id which can represent the request
  private static String genRequestId(int limit, int page, Object obj) {
    return limit + "." + page + "." + String.valueOf(obj);
  }

  /**
   * Fetches data.
   */
  public synchronized void fetch(final int limit, final int page, final Callback<T> callback) {
    // avoid repeat call fetch
    // TODO(liuxu): It's a temporary implementation to avoid duplicated invoke. We should
    // prevent upper layer from calling this function beyond need.

    final String requestId = genRequestId(limit, page, callback);
    if (runningSet.contains(requestId)) {
      return;
    }

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        try {
          final List<T> httpResult = fetchHttpData(limit, page);
          if (httpResult != null) {
            if (callback != null) {
              MainThreadPostUtils.post(new Runnable() {
                @Override
                public void run() {
                  callback.onFetched(limit, page, httpResult);
                }
              });
            }
          } else {
            if (callback != null) {
              MainThreadPostUtils.post(new Runnable() {
                @Override
                public void run() {
                  callback.onFailed(limit, page);
                }
              });
            }
          }
        } finally {
          synchronized (BaseFetcher.this) {
            runningSet.remove(requestId);
            runnables.remove(this);
          }
        }
      }

    };

    runningSet.add(requestId);
    runnables.add(runnable);
    ThreadPool.execute(runnable);
  }

  public synchronized void close() {
    for (Runnable runnable : runnables) {
      ThreadPool.cancel(runnable);
    }
  }

  /**
   * Fetches data from network.
   * 
   * @param limit size of page
   * @param page page num
   * @return return result list, can be null
   */
  protected abstract List<T> fetchHttpData(int limit, int page);

  /**
   * Callback class to be invoked on result fetched.
   *
   * @param <T> type of result list element
   */
  public interface Callback<T> {

    /**
     * Gets called when result is fetched.
     *
     * @param limit size of page
     * @param page page num
     * @param result result list, can be null
     */
    void onFetched(int limit, int page, List<T> result);

    void onFailed(int limit, int page);
  }

}
