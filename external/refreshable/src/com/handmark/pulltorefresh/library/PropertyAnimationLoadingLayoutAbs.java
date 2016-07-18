package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

/**
 * Description:
 *
 * @author lizhenhua9@wanda.cn (Lzh)
 * @date 15/7/8 16:32
 */
public class PropertyAnimationLoadingLayoutAbs extends PropertyLoadingLayout {

  public PropertyAnimationLoadingLayoutAbs(Context context, PullToRefreshBase.Mode mode,
      TypedArray attrs) {
    super(context, mode, attrs);
  }

  @Override
  public void loadingDrawableImpl(Drawable imageDrawable) {

  }

  @Override
  public void pullYImpl(float scaleOfHeight) {
    mAnimationHeaderView.setCurrentPullingHeight(Math.max((350 * scaleOfHeight - 50), 0.0F));
  }

  @Override
  public void pullToRefreshImpl() {
    mAnimationHeaderView.setOffState();
  }

  @Override
  public void refreshingImpl() {
    mAnimationHeaderView.startRefreshingAnimation();
  }

  @Override
  public void releaseToRefreshImpl() {
    mAnimationHeaderView.startReleaseAnimation();
  }

  @Override
  public void resetImpl(boolean isResetDropState) {
    mAnimationHeaderView.reset(isResetDropState);
  }
}
