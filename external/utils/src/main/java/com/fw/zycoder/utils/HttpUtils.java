package com.fw.zycoder.utils;

import android.text.TextUtils;

/**
 * Created by zrdeng on 5/14/15.
 */
public class HttpUtils {
  /**
   * 在较早版本中返回的JSON状态码为0时表示成功请求。
   */
  public static final String HTTP_OK_0 = "0";

  public static final String HTTP_OK_200 = "200";

  public static final String URL_SEPARATOR = "/";

  public static final String HTTP_PHONE_RE = "1109";

  /**
   * check the response message status.
   *
   * @param statusCode
   * @return true : the response is ok,false : response has a error code
   */
  public static boolean checkStatusCode(String statusCode) {
    if (TextUtils.isEmpty(statusCode)) {
      return false;
    }
    if (statusCode.equals(HTTP_OK_0) || statusCode.equals(HTTP_OK_200)) {
      return true;
    }
    return false;
  }

  public static boolean checkStatusCode(int statusCode) {
    return checkStatusCode(String.valueOf(statusCode));
  }
}
