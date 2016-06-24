package com.wanda.sliding.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.wanda.sliding.SlidingLayout;
import com.wanda.sliding.utils.SlidingUtils;

/**
 *
 */
public class SlidingHorizontalScrollView extends HorizontalScrollView
    implements
      SlidingLayout.RightFlingInterceptor {

  private boolean mCanSlide = true;

  public SlidingHorizontalScrollView(Context context) {
    super(context);
  }

  public SlidingHorizontalScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SlidingHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public SlidingHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    SlidingUtils.findAndRegisterRightFlingInterceptor(this, this);
  }

  @Override
  public boolean isAllowedRightFlingBack(MotionEvent ev) {
    return getScrollX() <= 0 && mCanSlide;
  }


  public void setSlideable(boolean slideable) {
    mCanSlide = slideable;
  }
}
