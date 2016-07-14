package com.zycoder.sliding.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.zycoder.sliding.SlidingLayout;

/**
 *
 */
public class SlidingUtils {

  /**
   * find parent and register interceptor
   *
   * @param view
   * @param interceptor
   */
  public static void findAndRegisterRightFlingInterceptor(View view,
      SlidingLayout.RightFlingInterceptor interceptor) {
    if (view == null) {
      return;
    }

    ViewParent viewParent = view.getParent();

    while (true) {
      if (viewParent == null) {
        return;
      }
      if (viewParent instanceof SlidingLayout) {
        ((SlidingLayout) viewParent).addInterceptor(interceptor);
        return;
      }
      viewParent = viewParent.getParent();
    }

  }

  /**
   * get coordinate in parent's coordinate system
   *
   * @param view target view
   * @param location an array of two integers in which to hold the coordinates
   */
  public static void getCoordinateInParent(View view, MotionEvent event, int[] location) {
    if (view == null || event == null || view.getParent() == null || location.length < 2) {
      return;
    }
    int parentLocation[] = new int[2];
    ((View) view.getParent()).getLocationOnScreen(parentLocation);
    if (parentLocation != null && parentLocation.length > 1) {
      location[0] = (int) event.getX() - parentLocation[0];
      location[1] = (int) event.getY() - parentLocation[1];
    }
  }


  public static ViewGroup getSlidingRoot(View view) {
    ViewGroup rootView = (ViewGroup) ((ViewGroup) (ActivityUtils.findActivity(view).getWindow()
        .getDecorView().findViewById(android.R.id.content))).getChildAt(0);
    if (rootView instanceof SlidingLayout) {
      rootView = (ViewGroup) rootView.getChildAt(0);
    }
    return rootView;
  }

  public static void getLocationInSlidingRoot(int[] position, View v) {
    if (position == null || position.length < 2) {
      throw new IllegalArgumentException("location must be an array of two integers");
    }
    if (v == null) {
      return;
    }
    ViewParent viewParent = v.getParent();
    if (viewParent == null) {
      return;
    }
    position[0] = position[1] = 0;
    position[0] += v.getLeft();
    position[1] += v.getTop();
    View rootView = getSlidingRoot(v);
    while (rootView != viewParent && viewParent instanceof View) {
      final View view = (View) viewParent;
      position[0] += view.getLeft();
      position[1] += view.getTop();
      viewParent = view.getParent();
      if (viewParent instanceof SlidingLayout) {
        break;
      }
    }
  }


}
