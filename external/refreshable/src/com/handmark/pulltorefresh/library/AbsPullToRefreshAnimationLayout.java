package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Description:下拉刷新动画抽象类
 *
 * @date 15/7/21 09:23
 */
public abstract class AbsPullToRefreshAnimationLayout extends LinearLayout {
  public AbsPullToRefreshAnimationLayout(Context context) {
    super(context);
  }

  public AbsPullToRefreshAnimationLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * 设置加载的图片资源 {@link #loadingDrawableImpl}
   * 
   * @param imageDrawable
   */
  public abstract void setLoadingDrawable(Drawable imageDrawable);

  /**
   * 下拉刷新 {@link #pullToRefreshImpl}
   */
  public abstract void pullToRefresh();

  /**
   * 开始加载动画 {@link #refreshingImpl}
   */
  public abstract void refreshing();

  /**
   * 下拉释放 {@link #releaseToRefreshImpl}
   */
  public abstract void releaseToRefresh();

  /**
   * 下拉刷新复位 {@link #resetImpl}
   */
  public abstract void reset(boolean isManualDrop);

  /**
   * 设置下拉距离 {@link #pullYImpl}
   */
  public abstract void pullY(float scaleOfHeight);


  // ImageLayout method
  public void setPullLabel(CharSequence pullLabel) {}

  public void setSubHeaderText(CharSequence label) {}

  public void setRefreshingLabel(CharSequence refreshingLabel) {}

  public void setReleaseLabel(CharSequence releaseLabel) {}


  // call back method
  protected abstract void loadingDrawableImpl(Drawable imageDrawable);

  protected abstract void pullYImpl(float scaleOfHeight);

  protected abstract void pullToRefreshImpl();

  protected abstract void refreshingImpl();

  protected abstract void releaseToRefreshImpl();

  protected abstract void resetImpl(boolean isManualDrop);
}
