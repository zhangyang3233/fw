package com.fw.zycoder.appbase.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fw.zycoder.appbase.R;
import com.fw.zycoder.utils.StringUtil;
import com.fw.zycoder.utils.ViewUtils;


/**
 * App统一的 LoadingView
 *
 * 可以有两种方式使用
 * 1.（建议）使用 newInstanceOnDecorView 传入 DecorView 动态构建。
 * feifan_loading_view ,注意给予 match_parent 的高宽。
 *
 * LoadingView 的 cancel 策略是：
 * 1.back 按键
 * 2.按 outside 的任意位置
 *
 * cancel 策略可以被设置为不可 cancel
 *
 * @author zhangyang
 */
public class AppLoadingView extends FrameLayout {

  private TextView mLoadingText;
  private LinearLayout mLoadingLayout;

  private String mText;
  private boolean mIsCancellable = true;

  private boolean mIsShown = false;

  public AppLoadingView(Context context) {
    super(context);
  }

  public AppLoadingView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }


  /**
   * 这里必须传入 DecorView
   *
   * @param decorView
   * @return
   */
  public static AppLoadingView newInstanceOnDecorView(ViewGroup decorView) {
    if (!ViewUtils.isViewAttachedToDecorView(decorView)) {
      throw new IllegalArgumentException("need a decorView for parent");
    }

    AppLoadingView loadingView =
        (AppLoadingView) ViewUtils.newInstance(decorView, R.layout.app_loading_view);

    decorView.addView(loadingView, new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));

    return loadingView;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    initView();
    initText();
    initListener();
  }

  private void initView() {
    mLoadingText = (TextView) findViewById(R.id.loading_text);
    mLoadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
  }

  private void initText() {
    mText = getContext().getString(R.string.base_default_loading);
  }

  private void initListener() {
    setOnKeyListener(new OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
          if (mIsCancellable) {
            if (getVisibility() == VISIBLE) {
              dismiss();
              return true;
            }
          } else {
            return true;
          }
        }
        return false;
      }
    });

    mLoadingLayout.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });

    setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {

        if (MotionEvent.ACTION_DOWN == event.getAction()
            || MotionEvent.ACTION_MOVE == event.getAction()) {
          return true;
        }
        if (MotionEvent.ACTION_UP == event.getAction()) {
          if (mIsCancellable && getVisibility() == VISIBLE) {
            dismiss();
          }
          return true;
        }
        return false;
      }
    });
  }

  public void setCancelable(boolean isCancellable) {
    this.mIsCancellable = isCancellable;
  }

  public void setLoadingText(String text) {
    this.mText = text;
    mLoadingText.setText(text);
  }

  /**
   * use default loading text, 努力加载中...;
   */
  public void show() {
    show(StringUtil.getString(R.string.base_default_loading));
  }

  /**
   * use user define loading text
   * 
   * @param title if title is empty or null, will hide loading text view
   */
  public void show(String title) {
    setLoadingText(title);
    if (TextUtils.isEmpty(mText)) {
      mLoadingText.setVisibility(GONE);
    } else {
      mLoadingText.setVisibility(VISIBLE);
    }
    mIsShown = true;
    setVisibility(View.VISIBLE);
    requestFocus();
  }


  public void dismiss() {
    mIsShown = false;
    setVisibility(View.GONE);
  }

  public boolean isShown() {
    return mIsShown;
  }

}
