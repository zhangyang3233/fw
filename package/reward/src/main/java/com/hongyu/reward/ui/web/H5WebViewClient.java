package com.hongyu.reward.ui.web;

import android.text.TextUtils;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashSet;
import java.util.Set;

/**
 * @author dengzhiren@wanda.cn (Dean)
 *         11/13/15
 */
public class H5WebViewClient extends WebViewClient {

  private Set<String> mOverrideUrlLoadingList = new HashSet<>();
  private PageStatusChangeListener mPageStatusChangeListener;

  public void setPageStatusChangeListener(PageStatusChangeListener pageStatusChangeListener) {
    mPageStatusChangeListener = pageStatusChangeListener;
  }

  public void addInterceptScheme(String scheme) {
    if (TextUtils.isEmpty(scheme)) {
      return;
    }
    mOverrideUrlLoadingList.add(scheme);
  }

  @Override
  public void onPageFinished(WebView view, String url) {
    CookieSyncManager.getInstance().sync();
    if (mPageStatusChangeListener != null) {
      mPageStatusChangeListener.onPageFinished(view, url);
    }
  }

  @Override
  public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    view.loadUrl("file:///android_asset/error.html");
  }


  public interface PageStatusChangeListener {
    boolean onSchemeDetected(String originalUrl, String uri);

    void onPageFinished(WebView view, String url);
  }
}
