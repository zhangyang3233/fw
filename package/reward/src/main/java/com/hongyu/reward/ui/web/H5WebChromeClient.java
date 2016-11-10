package com.hongyu.reward.ui.web;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * @author dengzhiren@wanda.cn (Dean)
 *         11/13/15
 */
public class H5WebChromeClient extends WebChromeClient {

  private TitleReceivedListener mTitleReceivedListener;
  private ProgressReceivedListener mProgressReceivedListener;

  public void setTitleReceivedListener(TitleReceivedListener titleReceivedListener) {
    mTitleReceivedListener = titleReceivedListener;
  }

  public void setProgressReceivedListener(ProgressReceivedListener mProgressReceivedListener) {
    this.mProgressReceivedListener = mProgressReceivedListener;
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

  public interface ProgressReceivedListener {
    void onProgressChanged(WebView view, int newProgress);
  }

  @Override
  public void onProgressChanged(WebView view, int newProgress) {
    super.onProgressChanged(view, newProgress);
    if(mProgressReceivedListener != null){
      mProgressReceivedListener.onProgressChanged(view, newProgress);
    }
  }
}
