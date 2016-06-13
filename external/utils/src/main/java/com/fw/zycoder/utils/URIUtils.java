package com.fw.zycoder.utils;

import android.net.Uri;
import android.text.TextUtils;

import java.util.Map;

/**
 */
public class URIUtils {

  public static String putParamsToURL(String url, Map<String, Object> params) {
    if (TextUtils.isEmpty(url)) {
      return null;
    }
    if (params == null || params.isEmpty()) {
      return url;
    }

    Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      if (entry == null || entry.getValue() == null) {
        continue;
      }
      uriBuilder.appendQueryParameter(entry.getKey(), String.valueOf(entry.getValue()));
    }
    return uriBuilder.toString();
  }

  /**
   * 不会覆盖原有的 key value
   */
  public static String putParamsToURLWithNoReplace(String url, Map<String, Object> params) {
    if (TextUtils.isEmpty(url)) {
      return null;
    }
    if (params == null || params.isEmpty()) {
      return url;
    }

    Uri uri = Uri.parse(url);
    Uri.Builder uriBuilder = uri.buildUpon();
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      if (entry == null || entry.getValue() == null) {
        continue;
      }
      // 只在uri 没有的时候添加
      if (TextUtils.isEmpty(uri.getQueryParameter(entry.getKey()))) {
        uriBuilder.appendQueryParameter(entry.getKey(), String.valueOf(entry.getValue()));
      }
    }
    return uriBuilder.toString();
  }

}
