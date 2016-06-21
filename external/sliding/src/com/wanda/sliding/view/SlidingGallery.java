package com.wanda.sliding.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

import com.wanda.sliding.SlidingLayout;
import com.wanda.sliding.utils.SlidingUtils;

/**
 *
 */
public class SlidingGallery extends Gallery implements SlidingLayout.RightFlingInterceptor {

  private boolean mCanSlide = true;

  public SlidingGallery(Context context) {
    super(context);
  }

  public SlidingGallery(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SlidingGallery(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public SlidingGallery(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    SlidingUtils.findAndRegisterRightFlingInterceptor(this, this);
  }


  @Override
  public boolean isAllowedRightFlingBack(MotionEvent ev) {
    return getSelectedItemPosition() == 0 && mCanSlide;
  }

  public void setSlideable(boolean slideable) {
    mCanSlide = slideable;
  }
}
