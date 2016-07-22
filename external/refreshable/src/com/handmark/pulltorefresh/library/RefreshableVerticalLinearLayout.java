package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class RefreshableVerticalLinearLayout extends PullToRefreshBase<LinearLayout> {

  public RefreshableVerticalLinearLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected LinearLayout createRefreshableView(Context context,
      AttributeSet attrs) {
    LinearLayout scrollView;
    if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
      scrollView = new InternalLinearLayoutSDK9(context, attrs);
    } else {
      scrollView = new LinearLayout(context, attrs);
    }
    scrollView.setOrientation(LinearLayout.VERTICAL);

    scrollView.setId(R.id.refreshable_widget_scrollview);
    return scrollView;
  }

  @Override
  protected boolean isReadyForPullDown() {
    return mRefreshableView.getScrollY() == 0;
  }

  @Override
  protected boolean isReadyForPullUp() {
    View scrollViewChild = mRefreshableView.getChildAt(0);
    if (null != scrollViewChild) {
      return mRefreshableView.getScrollY() >= (scrollViewChild
          .getHeight() - getHeight());
    }
    return false;
  }

  @TargetApi(9)
  final class InternalLinearLayoutSDK9 extends LinearLayout {

    public InternalLinearLayoutSDK9(Context context, AttributeSet attrs) {
      super(context, attrs);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
        int scrollY, int scrollRangeX, int scrollRangeY,
        int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

      final boolean returnValue = super.overScrollBy(deltaX, deltaY,
          scrollX, scrollY, scrollRangeX, scrollRangeY,
          maxOverScrollX, maxOverScrollY, isTouchEvent);

      // Does all of the hard work...
      OverscrollHelper.overScrollBy(RefreshableVerticalLinearLayout.this, deltaY,
          scrollY, getScrollRange(), isTouchEvent);

      return returnValue;
    }

    /**
     * Taken from the AOSP ScrollView source
     */
    private int getScrollRange() {
      int scrollRange = 0;
      if (getChildCount() > 0) {
        View child = getChildAt(0);
        scrollRange = Math.max(0, child.getHeight()
            - (getHeight() - getPaddingBottom() - getPaddingTop()));
      }
      return scrollRange;
    }
  }
}
