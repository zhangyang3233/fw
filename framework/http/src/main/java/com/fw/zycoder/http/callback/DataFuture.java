package com.fw.zycoder.http.callback;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.fw.zycoder.http.R;
import com.fw.zycoder.utils.Log;
import com.fw.zycoder.utils.MainThreadPostUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class DataFuture<T> {
  private static final String TAG = "VOLLEY_REQUEST_TAG";

  private Request<T> mRequest;
  private RequestFuture<T> mRequestFuture;

  /**
   * 是否开启日志，由 RequestBuilder 设置。
   */
  private boolean isLogEnable = false;
  /**
   * 是否开启 Toast，由 RequestBuilder 设置。
   */
  private boolean needToastError = false;

  public DataFuture() {
    mRequestFuture = RequestFuture.newFuture();
  }

  public RequestFuture<T> getRequestFuture() {
    return mRequestFuture;
  }


  public void setRequest(Request<T> request) {
    mRequest = request;
    mRequestFuture.setRequest(request);
  }

  public synchronized boolean cancel(boolean mayInterruptIfRunning) {
    return mRequestFuture.cancel(mayInterruptIfRunning);
  }

  public T get() {
    T result;
    try {
      if (isLogEnable) {
        Log.d(TAG, "return is ok, url is " + mRequest.getUrl());
      }
      result = mRequestFuture.get();
    } catch (InterruptedException e) {
      if (isLogEnable) {
        Log.d(TAG, "InterruptedException , url is " + mRequest.getUrl());
      }
      return null;
    } catch (ExecutionException e) {
      if (isLogEnable) {
        if (e != null && (e.getCause() instanceof VolleyError)
            && ((VolleyError) e.getCause()).networkResponse != null) {
          VolleyError error = ((VolleyError) e.getCause());
          Log.d(TAG, "error status code is " + error.networkResponse.statusCode
              + ", url is " + mRequest.getUrl() + " , error message is " + error.getMessage());
        } else {
          Log.d(TAG, "error url is " + mRequest.getUrl());
        }
      }
      if (needToastError) {
        MainThreadPostUtils.toast(R.string.network_error);
      }
      return null;
    }
    return result;
  }

  public T get(long timeout) {
    T result;
    try {
      if (isLogEnable) {
        Log.d(TAG, "return is ok, url is " + mRequest.getUrl());
      }
      result = mRequestFuture.get(timeout, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      if (isLogEnable) {
        Log.d(TAG, "InterruptedException , url is " + mRequest.getUrl());
      }
      return null;
    } catch (TimeoutException e) {
      if (isLogEnable) {
        Log.d(TAG, "TimeoutException , url is " + mRequest.getUrl());
      }
      return null;
    } catch (ExecutionException e) {
      if (isLogEnable) {
        if (e != null && (e.getCause() instanceof VolleyError)
            && ((VolleyError) e.getCause()).networkResponse != null) {
          VolleyError error = ((VolleyError) e.getCause());
          Log.d(TAG, "error status code is " + error.networkResponse.statusCode
              + ", url is " + mRequest.getUrl() + " , error message is " + error.getMessage());
        } else {
          Log.d(TAG, "error url is " + mRequest.getUrl());
        }
      }
      if (needToastError) {
        MainThreadPostUtils.toast(R.string.network_error);
      }
      return null;
    }
    return result;

  }

  public boolean isCancelled() {
    return mRequestFuture.isCancelled();
  }

  public synchronized boolean isDone() {
    return mRequestFuture.isDone();
  }

  public void setIsNeedToastError(boolean isNeedToastError) {
    this.needToastError = isNeedToastError;
  }

  public void setLogEnable(boolean isLogEnable) {
    this.isLogEnable = isLogEnable;
  }

}
