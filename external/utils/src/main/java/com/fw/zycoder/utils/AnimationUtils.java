package com.fw.zycoder.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * @author zhangyang
 */
public class AnimationUtils {

  /**
   * View渐显动画效果
   * 
   * @param view
   * @param duration
   */
  public static void showGradientAnimation(View view, int duration) {
    if (null == view || duration < 0) {
      return;
    }
    showHideOrShowAnimation(view, duration, false);
  }

  /**
   * View渐隐动画效果
   * 
   * @param view
   * @param duration
   */
  public static void hideGradientAnimation(View view, int duration) {
    if (null == view || duration < 0) {
      return;
    }
    showHideOrShowAnimation(view, duration, true);
  }


  /**
   * View渐隐动画效果
   */
  private static void showHideOrShowAnimation(View view, int duration, boolean isHide) {
    if (null == view || duration < 0) {
      return;
    }
    AlphaAnimation alphaAnimation = new AlphaAnimation(isHide ? 1.0f : 0f, isHide ? 0.0f : 1.0f);
    alphaAnimation.setDuration(duration);
    alphaAnimation.setFillAfter(true);
    view.startAnimation(alphaAnimation);

  }
}
