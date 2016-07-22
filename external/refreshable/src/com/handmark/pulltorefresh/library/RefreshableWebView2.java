package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An advanced version of {@link RefreshableWebView} which delegates the
 * triggering of the PullToRefresh gesture to the Javascript running within the
 * WebView. This means that you should only use this class if:
 * 
 * <ul>
 * <li>{@link RefreshableWebView} doesn't work correctly because you're using
 * <code>overflow:scroll</code> or something else which means {@link WebView#getScrollY()} doesn't
 * return correct values.</li>
 * <li>You control the web content being displayed, as you need to write some Javascript callbacks.
 * </li>
 * </ul>
 * <p />
 * 
 * The way this call works is that when a PullToRefresh gesture is in action, the following
 * Javascript methods will be called: <code>isReadyForPullDown()</code> and
 * <code>isReadyForPullUp()</code>, it is your job to calculate whether the view is in a state where
 * a PullToRefresh can happen, and return the result via the callback mechanism. An example can be
 * seen below:
 * 
 * <pre>
 * function isReadyForPullDown() {
 *   var result = ...  // Probably using the .scrollTop DOM attribute
 *   ptr.isReadyForPullDownResponse(result);
 * }
 * 
 * function isReadyForPullUp() {
 *   var result = ...  // Probably using the .scrollBottom DOM attribute
 *   ptr.isReadyForPullUpResponse(result);
 * }
 * </pre>
 * 
 */
public class RefreshableWebView2 extends RefreshableWebView {

  static final String JS_INTERFACE_PKG = "ptr";
  static final String DEF_JS_READY_PULL_DOWN_CALL = "javascript:isReadyForPullDown();";
  static final String DEF_JS_READY_PULL_UP_CALL = "javascript:isReadyForPullUp();";
  private final AtomicBoolean mIsReadyForPullDown = new AtomicBoolean(false);
  private final AtomicBoolean mIsReadyForPullUp = new AtomicBoolean(false);
  private JsValueCallback mJsCallback;

  public RefreshableWebView2(Context context) {
    super(context);
  }
  public RefreshableWebView2(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  public RefreshableWebView2(Context context, Mode mode) {
    super(context, mode);
  }

  @Override
  protected WebView createRefreshableView(Context context, AttributeSet attrs) {
    WebView webView = super.createRefreshableView(context, attrs);

    // Need to add JS Interface so we can get the response back
    mJsCallback = new JsValueCallback();
    webView.addJavascriptInterface(mJsCallback, JS_INTERFACE_PKG);

    return webView;
  }

  @Override
  protected boolean isReadyForPullDown() {
    // Call Javascript...
    getRefreshableView().loadUrl(DEF_JS_READY_PULL_DOWN_CALL);

    // Response will be given to JsValueCallback, which will update
    // mIsReadyForPullDown

    return mIsReadyForPullDown.get();
  }

  @Override
  protected boolean isReadyForPullUp() {
    // Call Javascript...
    getRefreshableView().loadUrl(DEF_JS_READY_PULL_UP_CALL);

    // Response will be given to JsValueCallback, which will update
    // mIsReadyForPullUp

    return mIsReadyForPullUp.get();
  }

  /**
   * Used for response from Javascript
   */
  final class JsValueCallback {

    public void isReadyForPullUpResponse(boolean response) {
      mIsReadyForPullUp.set(response);
    }

    public void isReadyForPullDownResponse(boolean response) {
      mIsReadyForPullDown.set(response);
    }
  }
}
