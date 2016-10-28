package com.hongyu.reward.http;


import android.text.TextUtils;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.http.request.JSONObjectRequestBuilder;
import com.fw.zycoder.http.volley.ApiContext;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.manager.PushTokenManager;

import org.json.JSONObject;

/**
 * Created by zhangyang131 on 16/9/8.
 */
public abstract class JSONObjectHttpRequestBuilder extends JSONObjectRequestBuilder {
  private DataCallback<JSONObject> mNetWorkCallback = null;
  DataCallback<JSONObject> dataCallback = new DataCallback<JSONObject>() {
    @Override
    public void onDataCallback(JSONObject data) {
      if (null != JSONObjectHttpRequestBuilder.this.mNetWorkCallback) {
        JSONObjectHttpRequestBuilder.this.mNetWorkCallback.onDataCallback(data);
      }
    }
  };

  @Override
  protected JSONObjectRequestBuilder setMethod(int method) {
    return super.setMethod(method);
  }

  @Override
  protected ApiContext getApiContext() {
    return ApiContextManager.getInstance().getApiContext();
  }

  protected void setParams(Params params) {
    super.setParams(params);
    // 除了获取token的接口之外所有的请求都加上token参数
    if (!(Constants.Server.API_PREFIX + Constants.Server.API_GET_TOKEN).equals(getUrl())) {
      String token = PushTokenManager.getInstance().getToken();
      params.put(Constants.Pref.TOKEN, token);
    }
  }

  protected void checkNullAndSet(Params params, String key, Object value) {
    if (value != null && !TextUtils.isEmpty(String.valueOf(value))) {
      params.put(key, value);
    }
  }

  public JSONObjectRequestBuilder setDataCallback(DataCallback<JSONObject> callback) {
    this.mNetWorkCallback = callback;
    return super.setDataCallback(this.dataCallback);
  }

  @Override
  protected String getUrl() {
    return Constants.Server.API_PREFIX + getApiUrl();
  }

  protected abstract String getApiUrl();
}
