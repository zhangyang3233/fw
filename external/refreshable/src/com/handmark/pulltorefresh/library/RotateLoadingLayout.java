package com.handmark.pulltorefresh.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView.ScaleType;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.R;

@SuppressLint("ViewConstructor")
public class RotateLoadingLayout extends ImageLoadingLayout {

  static final int ROTATION_ANIMATION_DURATION = 1200;

  private final Animation mRotateAnimation;
  private final Matrix mHeaderImageMatrix;

  private float mRotationPivotX, mRotationPivotY;

  public RotateLoadingLayout(Context context, Mode mode, TypedArray attrs) {
    super(context, mode, attrs);

    mHeaderImage.setScaleType(ScaleType.MATRIX);
    mHeaderImageMatrix = new Matrix();
    mHeaderImage.setImageMatrix(mHeaderImageMatrix);

    mRotateAnimation = new RotateAnimation(0, 720,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
        0.5f);
    mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
    mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
    mRotateAnimation.setRepeatCount(Animation.INFINITE);
    mRotateAnimation.setRepeatMode(Animation.RESTART);
  }

  public void loadingDrawableImpl(Drawable imageDrawable) {
    if (null != imageDrawable) {
      mRotationPivotX = imageDrawable.getIntrinsicWidth() / 2f;
      mRotationPivotY = imageDrawable.getIntrinsicHeight() / 2f;
    }
  }

  protected void pullYImpl(float scaleOfHeight) {
    mHeaderImageMatrix.setRotate(scaleOfHeight * 90, mRotationPivotX,
        mRotationPivotY);
    mHeaderImage.setImageMatrix(mHeaderImageMatrix);
  }

  @Override
  protected void refreshingImpl() {
    mHeaderImage.startAnimation(mRotateAnimation);
  }

  @Override
  protected void resetImpl(boolean isResetDropState) {
    mHeaderImage.clearAnimation();
    resetImageRotation();
  }

  private void resetImageRotation() {
    if (null != mHeaderImageMatrix) {
      mHeaderImageMatrix.reset();
      mHeaderImage.setImageMatrix(mHeaderImageMatrix);
    }
  }

  @Override
  protected void pullToRefreshImpl() {
    // NO-OP
  }

  @Override
  protected void releaseToRefreshImpl() {
    // NO-OP
  }

  @Override
  protected int getDefaultTopDrawableResId() {
    return R.drawable.refreshable_widget_default_rotate;
  }

  @Override
  protected int getDefaultBottomDrawableResId() {
    return R.drawable.refreshable_widget_default_rotate;
  }

}
