package com.fw.zycoder.http.request;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fw.zycoder.http.R;
import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.http.callback.DataFuture;
import com.fw.zycoder.http.volley.ApiContext;
import com.fw.zycoder.http.volley.ApiRequest;
import com.fw.zycoder.http.volley.Interceptor;
import com.fw.zycoder.utils.CollectionUtils;
import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.Log;
import com.fw.zycoder.utils.MainThreadPostUtils;
import com.fw.zycoder.utils.StringUtil;
import com.fw.zycoder.utils.URIUtils;

import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public abstract class JSONObjectRequestBuilder implements VolleyRequestBuilder {

  private static final String TAG = "VOLLEY_REQUEST_TAG";
  private int mMethod = Request.Method.POST;
  private Interceptor<JSONObject> mInterceptor;
  private DataCallback<JSONObject> mCallback;
  private Map<String, String> extraHeaders;

  private boolean needCache = false;
  private boolean isNeedFakeHeader = true;
  private long cacheTime = 0;
  private boolean needInvalidateCache = false;
  private boolean needToastError = false;
  private String requestUrl = null;

  /**
   * 是否开启日志，默认为跟随应用的 debug 状态，即 debug 开启，release 关闭。
   */
  private boolean isLogEnable = GlobalConfig.isDebug();

  private Response.ErrorListener errorListener = new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
      if (isLogEnable) {
        if (error != null && error.networkResponse != null) {
          Log.d(TAG, "error status code is " + error.networkResponse.statusCode
              + ", url is " + requestUrl + " , error message is " + error.getMessage());
        } else {
          Log.d(TAG, "error url is " + requestUrl);
        }
      }
      if (needToastError) {
        MainThreadPostUtils.toast(R.string.network_error);
      }
      if (mCallback != null) {
        MainThreadPostUtils.post(new Runnable() {
          @Override
          public void run() {
            mCallback.onDataCallback(null);
          }
        });
      }
    }
  };
  private Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
    @Override
    public void onResponse(final JSONObject response) {
      if (isLogEnable) {
        Log.d(TAG, "return is ok, url is " + requestUrl);
      }
      if (mCallback != null) {
        MainThreadPostUtils.post(new Runnable() {
          @Override
          public void run() {
            mCallback.onDataCallback(response);
          }
        });
      }
    }
  };

  protected JSONObjectRequestBuilder() {}


  protected JSONObjectRequestBuilder setMethod(int method) {
    this.mMethod = method;
    return this;
  }

  public JSONObjectRequestBuilder setInterceptor(Interceptor<JSONObject> interceptor) {
    this.mInterceptor = interceptor;
    return this;
  }

  public JSONObjectRequestBuilder setDataCallback(DataCallback<JSONObject> callback) {
    this.mCallback = callback;
    return this;
  }

  public JSONObjectRequestBuilder setNeedCache(boolean needCache) {
    this.needCache = needCache;
    return this;
  }

  public JSONObjectRequestBuilder setIsNeedFakeHeader(boolean isNeedFakeHeader) {
    this.isNeedFakeHeader = isNeedFakeHeader;
    return this;
  }

  public JSONObjectRequestBuilder setIsNeedToastError(boolean isNeedToastError) {
    this.needToastError = isNeedToastError;
    return this;
  }

  public JSONObjectRequestBuilder setCacheTime(long cacheTime) {
    this.cacheTime = cacheTime;
    return this;
  }

  public JSONObjectRequestBuilder setInvalidateCache(boolean needInvalidateCache) {
    this.needInvalidateCache = needInvalidateCache;
    return this;
  }

  public JSONObjectRequestBuilder setLogEnable(boolean isLogEnable) {
    this.isLogEnable = isLogEnable;
    return this;
  }


  /**
   * Gets the url of request.
   *
   * @return url, e.x a request like: http://www.company.com/download?key1=value1&key2=value2
   *         the url is http://www.company.com/download. Can be null, which means error happens
   */
  protected abstract String getUrl();


  /**
   * Get ApiContext with some needed config
   */
  protected abstract ApiContext getApiContext();

  protected void addExtraHeaders(String key, String value) {
    if (extraHeaders == null) {
      extraHeaders = new HashMap<>();
    }
    extraHeaders.put(key, value);
  }

  /**
   * 设置普通参数，这里普通指的是正常意义上的参数，
   * 即 GET 请求参数全显示在 url 上，POST 请求参数写在 body 里。
   *
   * @param params
   */
  protected void setParams(Params params) {}

  /**
   * 获取暴露的参数，无论如何都会显示在 url 上的参数。
   * 由于这种参数比较稀少，所以使用 get 方法
   */
  protected Map<String, Object> getExposedParams() {
    return null;
  }


  @Override
  public ApiRequest<JSONObject> build() {
    Params params = new Params();
    setParams(params);
    JSONObjectRequest request;

    requestUrl = addExposedParamsToUrl(getUrl());

    /**
     * 没设置 callback 则默认为使用 future 方式自动返回
     */
    if (mCallback != null) {
      JSONObjectRequest rr = new JSONObjectRequest(mMethod, requestUrl, params.getParamMap(), getApiContext(),
              responseListener, errorListener);
      request = rr;
    } else {
      DataFuture<JSONObject> dataFuture = new DataFuture<>();
      dataFuture.setLogEnable(isLogEnable);
      dataFuture.setIsNeedToastError(needToastError);
      request = new JSONObjectRequest(mMethod, requestUrl, params.getParamMap(), getApiContext(),
           dataFuture);
    }

    /**
     * 得到最终的 url 供打印日志使用
     */
    requestUrl = request.getUrl();

    /**
     * 设置是否需要缓存。默认为不使用缓存
     */
    request.setShouldCache(needCache);

    /**
     * 设置是否使用 FakeHttpHeader 代替原有的 header ，解决服务端返回 header 不规范的问题
     */
    request.setIsNeedFakeHeader(isNeedFakeHeader);

    /**
     * 当设置了自定义 cacheTime 时设置，否则会使用 apiContext 中配置的默认缓存时间。
     * 注意：只有使用了 FakeHeader 才会有效
     */
    if (cacheTime != 0) {
      request.setCacheTime(cacheTime);
    }

    /**
     * 设置是否需要清理 当前 url 的缓存.
     * 一般用于强制刷新使用
     */
    if (needInvalidateCache) {
      request.invalidateCache();
    }

    request.addInterceptor(mInterceptor);

    /**
     * 遍历设置 headers
     */
    if (extraHeaders != null && !CollectionUtils.isEmpty(extraHeaders.keySet())) {
      for (String key : extraHeaders.keySet()) {
        request.addExtraHeader(key, extraHeaders.get(key));
      }
    }

    return request;
  }

  /**
   * 如果有需要暴露的参数，增加到 url 中
   */
  private String addExposedParamsToUrl(String requestUrl) {
    return URIUtils.putParamsToURLWithNoReplace(requestUrl, getExposedParams());
  }


  /**
   * Request params.
   */
  public static final class Params implements Serializable {
    private static final long serialVersionUID = 1677274487904495553L;
    private final Map<String, Object> params = new HashMap<>();

    public void put(String key, Object value) {
      params.put(key, value);
    }

    public void putAll(Map<String, Object> mapParams) {
      for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
        params.put(entry.getKey(), entry.getValue());
      }
    }

    public void putAll(Params params) {
      this.params.putAll(params.params);
    }

    public Object get(String key) {
      return params.get(key);
    }

    public Map<String, String> getParamMap() {
      Map<String, String> result = new HashMap<>();
      for (Map.Entry<String, Object> entry : params.entrySet()) {
        result.put(entry.getKey(), String.valueOf(entry.getValue()));
      }
      return result;
    }

    public void clear() {
      params.clear();
    }

    public Set<Map.Entry<String, Object>> entrySet() {
      return params.entrySet();
    }

    public boolean isEmpty() {
      return params.isEmpty();
    }

    public String toString() {
      List<String> paramStrings = new ArrayList<>();
      for (Map.Entry<String, Object> entry : params.entrySet()) {
        if (entry.getValue() != null && !TextUtils.isEmpty(String.valueOf(entry.getValue()))
            && !TextUtils.isEmpty(entry.getKey())) {
          try {
            String key = URLEncoder.encode(entry.getKey(), "UTF-8");
            String value = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
            paramStrings.add(String.format("%s=%s", key, value));
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          }
        }
      }
      return StringUtil.join(paramStrings, "&");
    }
  }


}
