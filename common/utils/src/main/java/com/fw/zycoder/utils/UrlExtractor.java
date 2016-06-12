package com.fw.zycoder.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class UrlExtractor {
  private URL url;

  public UrlExtractor(String urlStr) {
    try {
      this.url = new URL(urlStr);
    } catch (MalformedURLException e) {
      url = null;
      e.printStackTrace();
    }
  }

  public UrlExtractor(URL url) {
    this.url = url;
  }

  /**
   * if URL is construct fail return false;
   */
  private boolean isContainHost(String host) {
    if (url != null) {
      return url.getHost().contains(host);
    } else {
      return false;
    }
  }

  private URL extractUrl() {
    if (url != null) {
      String query = url.getQuery();
      Map<String, String> queryMap = splitUrlQuery(query);
      if (queryMap.containsKey("url")) {
        String newUrl = queryMap.get("url");
        URL url;
        try {
          url = new URL(URLDecoder.decode(newUrl, "UTF-8"));
        } catch (MalformedURLException e) {
          return null;
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
          return null;
        }
        return url;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * if url's host is same with Para host ,return the nested url's host else
   * return url's host otherwise return null
   *
   * for example: URL:
   * http://app.wandou.in/redirect?url=http%3A%2F%2Ft.androidgame
   * -store.com%2Fandroid
   * %2Fnew%2Fgame1%2F40%2F107940%2Fhtllqhdzgb_1.apk&pos=recommendItem input:
   * app.wandou.in output: t.androidgame-store.com
   *
   * URL: http://wdj.appchina.com/market/download.jsp?id=136&type=package
   * input: app.wandou.in output:wdj.appchina.com
   *
   */
  public String extractHost(String host) {
    if (isContainHost(host)) {
      URL secondUrl = extractUrl();
      if (secondUrl != null) {
        return secondUrl.getHost();
      } else {
        return null;
      }
    } else if (url != null) {
      return url.getHost();
    } else {
      return null;
    }
  }

  private static Map<String, String> splitUrlQuery(String query) {
    Map<String, String> result = new HashMap<String, String>();

    String[] pairs = query.split("&");
    if (pairs != null && pairs.length > 0) {
      for (String pair : pairs) {
        String[] param = pair.split("=", 2);
        if (param != null && param.length == 2) {
          result.put(param[0], param[1]);
        }
      }
    }

    return result;
  }

}
