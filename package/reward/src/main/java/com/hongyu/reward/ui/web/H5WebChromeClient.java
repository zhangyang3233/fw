package com.hongyu.reward.ui.web;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * @author dengzhiren@wanda.cn (Dean)
 *         11/13/15
 */
public class H5WebChromeClient extends WebChromeClient {

  private TitleReceivedListener mTitleReceivedListener;

  public void setTitleReceivedListener(TitleReceivedListener titleReceivedListener) {
    mTitleReceivedListener = titleReceivedListener;
  }

  @Override
  public void onReceivedTitle(WebView view, String title) {
    if (mTitleReceivedListener != null) {
      mTitleReceivedListener.onReceivedTitle(view, title);
    }
  }

  public interface TitleReceivedListener {
    void onReceivedTitle(WebView view, String title);
  }
}
