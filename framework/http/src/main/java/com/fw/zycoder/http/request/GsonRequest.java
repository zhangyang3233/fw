package com.fw.zycoder.http.request;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fw.zycoder.http.callback.DataFuture;
import com.fw.zycoder.http.volley.ApiContext;
import com.fw.zycoder.http.volley.ApiRequest;
import com.fw.zycoder.utils.GsonUtils;

import java.io.IOException;
import java.util.Map;

/**
 * 用于请求json类型的接口。
 * 
 *
 */
public class GsonRequest<T> extends ApiRequest<T> {

  private final Class<T> responseClass;

  public GsonRequest(int method, String url, Map<String, String> params, ApiContext apiContext,
      Class<T> responseClass, Response.Listener<T> listener, Response.ErrorListener errorListener) {
    super(method, url, params, apiContext, listener, errorListener);
    this.responseClass = responseClass;
  }

  public GsonRequest(int method, String url, Map<String, String> params, ApiContext apiContext,
      Class<T> responseClass, DataFuture<T> future) {
    super(method, url, params, apiContext, future);
    this.responseClass = responseClass;
  }

  @Override
  protected T parseResponse(NetworkResponse response) throws IOException {
    String jsonString =
        new String(response.data, HttpHeaderParser.parseCharset(response.headers));
    Log.w("json", jsonString);
    return GsonUtils.getGson().fromJson(jsonString, responseClass);
  }
}
