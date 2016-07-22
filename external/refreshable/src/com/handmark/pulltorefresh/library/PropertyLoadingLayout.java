package com.handmark.pulltorefresh.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * 属性动画Layout
 */
@SuppressLint("ViewConstructor")
public abstract class PropertyLoadingLayout extends AbsPullToRefreshAnimationLayout {

  static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
  protected final RefreshAnimationHeaderView mAnimationHeaderView;
  private boolean mUseIntrinisicAnimation;

  public PropertyLoadingLayout(Context context, final PullToRefreshBase.Mode mode,
      TypedArray attrs) {
    super(context);

    setGravity(Gravity.CENTER_VERTICAL);

    final int tbPadding = getResources().getDimensionPixelSize(
        R.dimen.refreshable_widget_header_footer_top_bottom_padding);
    final int lrPadding = getResources().getDimensionPixelSize(
        R.dimen.refreshable_widget_header_footer_left_right_padding);
    setPadding(lrPadding, tbPadding, lrPadding, tbPadding);

    LayoutInflater.from(context).inflate(R.layout.refreshable_header_view, this);
    mAnimationHeaderView = (RefreshAnimationHeaderView) findViewById(R.id.refresh_header_view);


    if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
      Drawable background = attrs
          .getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
      if (null != background) {
        setBackgroundDrawable(background);
      }
    }

    // Try and get defined drawable from Attrs
    Drawable imageDrawable = null;
    if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawable)) {
      imageDrawable = attrs
          .getDrawable(R.styleable.PullToRefresh_ptrDrawable);
    }

    // Check Specific Drawable from Attrs, these overrite the generic
    // drawable attr above
    if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableTop)
        && mode == PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH) {
      imageDrawable = attrs
          .getDrawable(R.styleable.PullToRefresh_ptrDrawableTop);
    } else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableBottom)
        && mode == PullToRefreshBase.Mode.PULL_UP_TO_REFRESH) {
      imageDrawable = attrs
          .getDrawable(R.styleable.PullToRefresh_ptrDrawableTop);
    }

    // Set Drawable, and save width/height
    setLoadingDrawable(imageDrawable);

    reset(false);
  }

  public void setSubHeaderText(CharSequence label) {

  }

  public void setSubTextAppearance(int value) {}

  public void setSubTextColor(ColorStateList color) {}

  public void setSubTextColor(int color) {
    setSubTextColor(ColorStateList.valueOf(color));
  }

  public void setTextAppearance(int value) {}

  public void setTextColor(ColorStateList color) {}

  public void setTextColor(int color) {
    setTextColor(ColorStateList.valueOf(color));
  }

  public final void pullY(float scaleOfHeight) {
    if (!mUseIntrinisicAnimation) {
      pullYImpl(scaleOfHeight);
    }
  }

  public final void pullToRefresh() {
    // 从零开始下拉状态

    // Now call the callback
    pullToRefreshImpl();
  }

  public final void refreshing() {
    // 循环播放动画
    // mHeaderText.setText(mRefreshingLabel);

    if (mUseIntrinisicAnimation) {
      // ((AnimationDrawable) mHeaderImage.getDrawable()).start();
    } else {
      // Now call the callback
      refreshingImpl();
    }
  }

  public final void releaseToRefresh() {
    // 提示释放label
    // Now call the callback
    releaseToRefreshImpl();
  }

  /**
   * 是否手动点击展开动画
   * 
   * @param isManualDrop
   */
  public final void reset(boolean isManualDrop) {
    if (!mUseIntrinisicAnimation) {
      resetImpl(isManualDrop);
    }
  }

  public final void setLoadingDrawable(Drawable imageDrawable) {
    // Set Drawable
    // mHeaderImage.setImageDrawable(imageDrawable);
    mUseIntrinisicAnimation = (imageDrawable instanceof AnimationDrawable);

    // Now call the callback
    loadingDrawableImpl(imageDrawable);
  }
}
