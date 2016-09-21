package com.fw.zycoder.http.request;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fw.zycoder.http.callback.DataFuture;
import com.fw.zycoder.http.volley.ApiContext;
import com.fw.zycoder.http.volley.ApiRequest;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * 用于请求json类型的接口。
 * 
 *
 */
public class JSONObjectRequest extends ApiRequest<JSONObject> {

  public JSONObjectRequest(int method, String url, Map<String, String> params, ApiContext apiContext,
                           Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
    super(method, url, params, apiContext, listener, errorListener);
  }

  public JSONObjectRequest(int method, String url, Map<String, String> params, ApiContext apiContext,
                            DataFuture<JSONObject> future) {
    super(method, url, params, apiContext, future);
  }

  @Override
  protected JSONObject parseResponse(NetworkResponse response) throws IOException {
    String jsonString =
        new String(response.data, HttpHeaderParser.parseCharset(response.headers));
    Log.w("json", jsonString);

    JSONObject jsonObject = null;
    try {
      jsonObject = new JSONObject(jsonString);
    } catch (JSONException e) {
      e.printStackTrace();
      throw new JsonSyntaxException(e.getMessage());
    }
    return jsonObject;
  }
}
