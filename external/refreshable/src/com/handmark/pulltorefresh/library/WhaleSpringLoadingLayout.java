package com.handmark.pulltorefresh.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

@SuppressLint("ViewConstructor")
public class WhaleSpringLoadingLayout extends WhaleSpringLoadingLayoutAbs {

  public WhaleSpringLoadingLayout(Context context, PullToRefreshBase.Mode mode,
      TypedArray attrs) {
    super(context, mode, attrs);
  }

  @Override
  public void loadingDrawableImpl(Drawable imageDrawable) {

  }

  @Override
  public void pullYImpl(float scaleOfHeight) {
    mAnimationHeaderView.setPullY(Math.max((350 * scaleOfHeight - 50) / 2.0f, 0.0F));
  }

  @Override
  public void pullToRefreshImpl() {}

  @Override
  public void refreshingImpl() {
    mAnimationHeaderView.startRefreshingAnimation();
  }

  @Override
  public void releaseToRefreshImpl() {}

  @Override
  public void resetImpl(boolean isResetDropState) {
    mAnimationHeaderView.reset(isResetDropState);
  }

}
