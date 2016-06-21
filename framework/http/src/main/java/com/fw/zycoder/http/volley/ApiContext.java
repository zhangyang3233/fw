package com.fw.zycoder.http.volley;

import android.content.Context;

import com.android.volley.RequestQueue;

import java.util.Map;

/**
 * http config
 *
 *
 */
public interface ApiContext {

  Context getContext();

  RequestQueue getRequestQueue();

  Map<String, String> getHeaders();

  String getCacheKeyPostfix();

  /**
   * 是否无视服务端 response header 中的 Cache-Control 和 Date 字段，直接使用客户端的缓存时间与策略。
   * 对于服务端 Cache-Control 和 Date 时间不准确时，十分奏效。
   */
  boolean getNeedFixCacheControl();

  /**
   * 使用 fakekey 时的缓存时间
   */
  long getCacheTime();
}
