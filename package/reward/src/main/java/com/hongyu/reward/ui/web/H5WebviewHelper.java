package com.hongyu.reward.ui.web;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.fw.zycoder.utils.URIUtils;

public class H5WebviewHelper {

  public static void initWebView(final WebView webView,
      H5WebChromeClient.TitleReceivedListener listener,
      H5WebViewClient.PageStatusChangeListener pageStatusChangeListener) {
    if (webView == null) {
      return;
    }
    H5WebViewClient webViewClient = new H5WebViewClient();
    H5WebChromeClient webChromeClient = new H5WebChromeClient();
    webChromeClient.setTitleReceivedListener(listener);
    webViewClient.setPageStatusChangeListener(pageStatusChangeListener);
    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDatabaseEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setAllowFileAccess(true);
    settings.setAppCacheEnabled(true);
    settings.setAppCacheMaxSize(1024 * 1024 * 8);
    settings.setCacheMode(WebSettings.LOAD_DEFAULT);
    settings.setPluginState(WebSettings.PluginState.ON);
    settings.setBuiltInZoomControls(true);
    settings.setLoadWithOverviewMode(true);
    settings.setUseWideViewPort(true);

    webView.requestFocus();
    webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
    webView.setMapTrackballToArrowKeys(false);
    webView.setWebChromeClient(webChromeClient);
    webView.setWebViewClient(webViewClient);
    webView.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
        return false;
      }
    });
    webView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        return true;
      }
    });
    webView.setVerticalScrollbarOverlay(true);
  }


  public static void destroyWebView(WebView webView) {
    if (webView == null) {
      return;
    }
    webView.stopLoading();
    webView.loadData("<a></a>", "text/html", "utf-8");
    webView.clearCache(false);
    webView.clearHistory();
    webView.destroyDrawingCache();
    webView.removeAllViews();
    webView.clearView();
    webView.clearDisappearingChildren();
    webView.freeMemory();
    webView.clearFocus();
    webView.clearMatches();
    webView.clearSslPreferences();
    webView.destroy();
  }

  public static void loadUrl(WebView webView, String url) {
    if (TextUtils.isEmpty(url)) {
      return;
    }
    if (webView == null) {
      return;
    }
    String loadUrl = URIUtils.putParamsToURLWithNoReplace(url, null);
    webView.loadUrl(loadUrl);
  }
}
