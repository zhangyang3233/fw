package com.hongyu.reward.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import com.fw.zycoder.utils.CollectionUtils;
import com.hongyu.reward.R;
import com.hongyu.reward.ui.activity.BrowserActivity;
import com.hongyu.reward.ui.web.H5WebChromeClient;
import com.hongyu.reward.ui.web.H5WebViewClient;
import com.hongyu.reward.ui.web.H5WebviewHelper;
import com.hongyu.reward.widget.AppEmptyView;
import com.hongyu.reward.widget.CustomWebview;

import java.util.HashMap;
import java.util.Map;

public class BrowserFragment extends BaseBrowserFragment {


  private Map<String, String> mMap = new HashMap<>();// 存放标题 键是url 值是标题
  private String mTitle;

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    mWebView = (WebView) mContentView.findViewById(R.id.h5_webview);
    mAppEmptyView = (AppEmptyView) mContentView.findViewById(R.id.app_empty_view);
    Bundle bundle = getArguments();

    if (bundle != null) {
      mUrl = bundle.getString(BrowserActivity.URL, "");
      mTitle = bundle.getString(BrowserActivity.TITLE, "");
    }
    initWebView(mWebView);
    initListener();
    initWebViewScrollChangeListener();
  }

  private void initWebViewScrollChangeListener() {
    if (mWebView instanceof CustomWebview) {
      ((CustomWebview) mWebView)
          .setOnScrollChangeListener(new CustomWebview.ScrollChangeListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
              onWebViewScrollChanged(l, t, oldl, oldt);

            }
          });
    }
  }

  private void initWebView(final WebView webView) {
    showLoadingView();
    H5WebviewHelper.initWebView(webView, new H5WebChromeClient.TitleReceivedListener() {

      @Override
      public void onReceivedTitle(WebView view, String jsTitle) {
        dismissLoadingView();
        if (getActivity() == null) {
          return;
        }

        // 后端配置了标题mTitle，不再显示jsTitle;
        // 后配没有配置mTitle，显示jsTitle;
        if (mTitle != null && !mTitle.equals("")) {
          return;
        }

        String url = view.getUrl();
        if (mMap != null && !TextUtils.isEmpty(url) && !TextUtils.isEmpty(jsTitle)) {
          mMap.put(url, jsTitle);// 存放标题
        }
        if (!TextUtils.isEmpty(jsTitle)) {
          getActivity().setTitle(jsTitle);
        } else {
          if (TextUtils.isEmpty(url) || CollectionUtils.isEmpty(mMap)) {
            return;
          }
          String originTitle = mMap.get(url);
          if (TextUtils.isEmpty(originTitle)) {
            return;
          }
          getActivity().setTitle(originTitle);
        }
      }
    }, new H5WebViewClient.PageStatusChangeListener() {

      @Override
      public boolean onSchemeDetected(String originalUrl, String uri) {
        return false;
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        dismissLoadingView();
        // 解决手机兼容性问题
        if (getActivity() != null && !CollectionUtils.isEmpty(mMap)) {
          String originTitle = mMap.get(url);
          if (!TextUtils.isEmpty(originTitle)) {
            getActivity().setTitle(originTitle);
          }
        }
      }
    });
  }

  /**
   * webView滚动的监听
   * 
   * @param l
   * @param t
   * @param oldl
   * @param oldt
   */
  protected void onWebViewScrollChanged(int l, int t, int oldl, int oldt) {}

}
