package com.hongyu.reward.ui.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.fw.zycoder.utils.NetworkUtil;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.ui.web.H5WebviewHelper;
import com.hongyu.reward.widget.AppEmptyView;

public class BaseBrowserFragment extends BaseLoadFragment {

  protected WebView mWebView;
  protected AppEmptyView mAppEmptyView;
  protected ProgressBar mH5progressBar;
  protected String mUrl;


  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {

  }

  protected void initListener() {
    mAppEmptyView.setOnRefreshListener(new AppEmptyView.OnEmptyRefreshListener() {
      @Override
      public void onRefresh() {
        if (mWebView != null) {
          mWebView.reload();
        }
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    if (mWebView != null) {
      mWebView.onResume();
      WebSettings settings = mWebView.getSettings();
      settings.setBuiltInZoomControls(false);
      mWebView.resumeTimers();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (mWebView != null) {
      mWebView.pauseTimers();
      mWebView.onPause();
    }
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.h5_fragment;
  }

  @Override
  protected void onStartLoading() {
    H5WebviewHelper.loadUrl(mWebView, mUrl);
  }

  public boolean onBackPressed() {
    if (!NetworkUtil.isNetworkConnected(getActivity())) {
      return false;
    }
    if (mWebView.canGoBack()) {
      mWebView.goBack();
      return true;
    }
    return false;
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      return onBackPressed();
    }
    return false;
  }

}
