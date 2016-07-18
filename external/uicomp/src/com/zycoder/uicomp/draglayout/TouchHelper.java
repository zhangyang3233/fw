package com.zycoder.uicomp.draglayout;

import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * @author zhangyuwen 2015-12-03
 */
public class TouchHelper {
  public static final int GESTURE_INVALID = -1;
  public static final int GESTURE_PULL_LEFT = 0;
  public static final int GESTURE_PULL_UP = 1;
  public static final int GESTURE_PULL_RIGHT = 2;
  public static final int GESTURE_PULL_DOWN = 3;

  private VelocityTracker mVelocityTracker;

  public TouchHelper() {
    mVelocityTracker = VelocityTracker.obtain();
  }

  public static void registerInterceptor(View view, com.zycoder.uicomp.draglayout.TouchInterceptor interceptor) {
    DragLayout dragParent = findDragLinearLayout(view);
    if (dragParent != null) {
      dragParent.registerTouchInterceptor(interceptor);
    }
  }

  private static DragLayout findDragLinearLayout(View child) {
    if (child == null) {
      return null;
    }
    ViewParent viewParent = child.getParent();
    while (true) {
      if (viewParent == null) {
        return null;
      }
      if (viewParent instanceof DragLayout) {
        return (DragLayout) viewParent;
      }
      viewParent = viewParent.getParent();
    }
  }

  /**
   * A
   *
   * B
   * A ---> B mVelocityTracker.getXVelocity() > 0
   * A ---> B mVelocityTracker.getYVelocity() > 0
   * B ---> A mVelocityTracker.getXVelocity() < 0
   * B ---> A mVelocityTracker.getXVelocity() < 0
   * 此函数默认是上下滑动手势
   *
   * @param ev
   * @return
   */
  public int getGestureUpOrDown(MotionEvent ev) {
    mVelocityTracker.addMovement(ev);
    mVelocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
    float Vy = mVelocityTracker.getYVelocity();
    if (Vy > 0) {
      return GESTURE_PULL_DOWN;
    } else {
      return GESTURE_PULL_UP;
    }
  }

  public static boolean isListViewNeedPullDown(AbsListView listView) {
    if (listView == null) {
      return false;
    }
    int position = listView.getFirstVisiblePosition();
    if (position > 0) {
      return true;
    }
    View itemView = listView.getChildAt(0);
    if (itemView != null) {
      return itemView.getTop() < listView.getTop();
    }
    return false;
  }

  public static boolean isScrollViewNeedPullDown(ScrollView scrollView) {
    if (scrollView == null || scrollView.getScrollY() == 0) {
      return false;
    }
    return true;
  }
}
