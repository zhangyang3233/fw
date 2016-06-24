package com.fw.zycoder.http.volley;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fw.zycoder.http.callback.DataFuture;
import com.fw.zycoder.utils.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public abstract class ApiRequest<T> extends Request<T> {

  private final ApiContext mApiContext;
  private final Response.Listener<T> mListener;

  private boolean mResponseDelivered;
  private boolean mAllowMultipleResponses;

  private Map<String, String> mParams;
  private Map<String, String> mExtraHeaders;
  private List<Interceptor> mInterceptors;
  private DataFuture<T> mDataFuture;

  /**
   * 是否使用 FakeHttpHeader 代替原有的 header
   * 解决服务器返回缓存 header 不准的情况
   */
  private boolean mIsNeedFakeHeader;

  /**
   * 配合 FakeHttpHeaderParser 解决服务端返回 header 不规范的问题
   */
  private long fakeCacheTime = 0;

  public ApiRequest(int method, String url, Map<String, String> params, ApiContext apiContext,
      Response.Listener<T> listener, Response.ErrorListener errorListener) {
    super(method, buildUrl(url, params, method), errorListener);
    this.mParams = params;
    this.mApiContext = apiContext;
    this.mListener = listener;

    setShouldCache(!ApiConfig.skipAllCaches());
    setRetryPolicy(new ApiRetryPolicy());
    fakeCacheTime = apiContext.getCacheTime();
    mIsNeedFakeHeader = apiContext.getNeedFixCacheControl();
  }

  public ApiRequest(int method, String url, Map<String, String> params, ApiContext apiContext,
      DataFuture<T> dataFuture) {
    this(method, url, params, apiContext, dataFuture.getRequestFuture(), dataFuture
        .getRequestFuture());
    this.mDataFuture = dataFuture;
  }

  private static String buildUrl(String url, Map<String, String> params, int method) {
    if (params == null || params.isEmpty() || (method != Method.GET && method != Method.DELETE)) {
      return url;
    }
    Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
    }
    return uriBuilder.toString();
  }

  public final void submit() {
    mApiContext.getRequestQueue().add(this);
  }

  public final DataFuture<T> submitSync() {
    mApiContext.getRequestQueue().add(this);
    if (mDataFuture != null) {
      mDataFuture.setRequest(this);
      return mDataFuture;
    }
    return null;
  }

  /**
   * 设置是否使用 FakeHttpHeader 代替原有的 header ，解决服务端返回 header 不规范的问题
   */
  public void setIsNeedFakeHeader(boolean isNeedFakeHeader) {
    mIsNeedFakeHeader = isNeedFakeHeader;
  }

  /**
   * 设置 fake 缓存时间 ，配合 FakeHttpHeaderParser 解决服务端返回 header 不规范的问题
   * 注意：只有使用了 FakeHeader 才会有效
   */
  public void setCacheTime(long cacheTime) {
    fakeCacheTime = cacheTime;
  }

  /**
   * 清理当前的 url 对应的 cache
   */
  public final void invalidateCache() {
    if (mApiContext.getRequestQueue().getCache() != null) {
      mApiContext.getRequestQueue().getCache().remove(getCacheKey());
    }
  }

  @Override
  protected final Response<T> parseNetworkResponse(NetworkResponse response) {
    try {
      // saveCookie(response);
      T result = parseResponse(response);
      if (result == null) {
        return null;
      }
      Log.d("request url:" + getUrl() + " ;respone string:" + result.toString());
      interceptResult(result);
      return Response.success(result, parseCache(response));
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.error(new ParseError(response));
    }
  }

  /**
   * Subclasses must implement this to parse the raw network response
   * and return an appropriate response type. This method will be
   * called from a worker thread. The response will not be delivered
   * if you return null.
   *
   * @param response Response from the network
   * @return The parsed result, or null in the case of an error
   */
  protected abstract T parseResponse(NetworkResponse response) throws IOException;

  @Override
  protected void deliverResponse(T response) {
    if (mListener == null) {
      return;
    }
    if (mAllowMultipleResponses || !mResponseDelivered) {
      mListener.onResponse(response);
      mResponseDelivered = true;
    }
  }

  @Override
  public Map<String, String> getHeaders() throws AuthFailureError {
    Map<String, String> headers = new HashMap<>();
    Map<String, String> contextHeaders = mApiContext.getHeaders();
    if (contextHeaders != null) {
      headers.putAll(contextHeaders);
    }
    if (mExtraHeaders != null) {
      headers.putAll(mExtraHeaders);
    }
    // putCookie(headers);
    return headers;
  }

  @Override
  public Map<String, String> getParams() {
    return mParams;
  }

  @Override
  public String getCacheKey() {
    String postfix = mApiContext.getCacheKeyPostfix();
    return super.getCacheKey() + (postfix == null ? "" : postfix);
  }

  /**
   * Parse cache from response.
   * The default strategy use volley's <b>HttpHeaderParser</b>, sub-class enable override it.
   *
   * @param response
   * @return
   */
  protected Cache.Entry parseCache(NetworkResponse response) {
    if (mIsNeedFakeHeader) {
      /**
       * FakeHttpHeaderParser 解决服务端返回 header 不规范的问题
       */
      // return FakeHttpHeaderParser.parseCacheHeaders(response, fakeCacheTime);
      return HttpHeaderParser.parseCacheHeaders(response);
    } else {
      /**
       * 使用原生的 parser 处理并设置 cache 信息
       */
      return HttpHeaderParser.parseCacheHeaders(response);
    }

  }

  /**
   * 添加Interceptor.
   * Interceptor是串行调用的，如果前一个抛出了异常后续的则不再调用。
   *
   * @param interceptor the interceptor.
   */
  public void addInterceptor(Interceptor<T> interceptor) {
    if (interceptor == null) {
      return;
    }
    if (mInterceptors == null) {
      mInterceptors = new ArrayList<Interceptor>();
    }
    mInterceptors.add(interceptor);
  }

  public void addExtraHeader(String key, String value) {
    if (mExtraHeaders == null) {
      mExtraHeaders = new HashMap();
    }
    mExtraHeaders.put(key, value);
  }

  /**
   * Only work for POST and PUT request
   *
   * @param key
   * @param value
   */
  public void addParam(String key, String value) {
    if (mParams == null) {
      mParams = new HashMap<String, String>();
    }
    mParams.put(key, value);
  }

  /**
   * Only work for POST and PUT request
   *
   * @param params
   */
  public void addParams(Map<String, String> params) {
    if (params == null) {
      return;
    }
    if (this.mParams == null) {
      this.mParams = new HashMap<String, String>();
    }
    this.mParams.putAll(params);
  }

  public void setAllowMultipleResponses(boolean allowMultipleResponses) {
    this.mAllowMultipleResponses = allowMultipleResponses;
  }

  private void interceptResult(T result) {
    if (mInterceptors == null) {
      return;
    }
    for (Interceptor<T> interceptor : mInterceptors) {
      interceptor.intercept(result);
    }
  }


}
