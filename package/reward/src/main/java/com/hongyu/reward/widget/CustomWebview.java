package com.hongyu.reward.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 带有滚动监听的webview 增加扩展性
 * Created by mi on 16/6/20.
 */
public class CustomWebview extends WebView {
  public ScrollChangeListener mChangeListener;

  public CustomWebview(Context context) {
    super(context);
  }

  public CustomWebview(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CustomWebview(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CustomWebview(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    super.onScrollChanged(l, t, oldl, oldt);

    if (mChangeListener != null) {
      mChangeListener.onScrollChanged(l, t, oldl, oldt);
    }
  }

  public void setOnScrollChangeListener(ScrollChangeListener listener) {
    mChangeListener = listener;

  }

  public interface ScrollChangeListener {
    void onScrollChanged(int l, int t, int oldl, int oldt);
  }
}
