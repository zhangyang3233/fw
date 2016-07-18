package com.fw.zycoder.appbase.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.fw.zycoder.appbase.R;
import com.fw.zycoder.appbase.view.AppEmptyView;
import com.fw.zycoder.appbase.view.CoverViewContainer;

/**
 * @author liuxu34@wanda.cn (Liu Xu)
 */
public class TipsViewUtil {

  /**
   * Show tips view to the target view.
   *
   * @param targetView the target to show the tips view.
   * @param tipsType {@link TipsType}
   * @return the tips view.
   */
  public static View showTipsView(View targetView, TipsType tipsType) {
    if (null == targetView || null == tipsType) {
      return null;
    }
    Tips tips = tipsType.createTips(targetView.getContext());
    if (tips == null) {
      return null;
    }
    return tips.applyTo(targetView, tipsType.ordinal());
  }

  /**
   * Batch hide tips view from the target view,
   * eg: {@code hideTipsView(target, TipsType.LOADING, TipsType.NO_FLOW)}.
   *
   * @param targetView the target to show the tips view.
   * @param tipsTypes the tips type you want to hid, {@link TipsType}.
   */
  public static void hideTipsView(View targetView, TipsType... tipsTypes) {
    if (targetView == null || tipsTypes == null || tipsTypes.length == 0) {
      return;
    }
    for (TipsType tipsType : tipsTypes) {
      hideTipsView(targetView, tipsType.ordinal());
    }
  }

  public static void hideTipsView(View targetView, int tipsId) {
    ViewGroup tipsContainerView = (ViewGroup) targetView.getParent();
    // TODO liuxu please handle StickyListHeadersListView case or other similar cases.
    // if tipsContainerView is StickyListHeadersListView, its parent is not CoverViewContainer.
    if (!(tipsContainerView instanceof CoverViewContainer)) {
      return;
    }
    View tipsView = findChildViewById(tipsContainerView, tipsId);
    if (tipsView == null) {
      return;
    }
    tipsContainerView.removeView(tipsView);
    boolean hideTarget = false;
    for (int i = 0; i < tipsContainerView.getChildCount(); ++i) {
      Tips tips = (Tips) tipsContainerView.getChildAt(i).getTag();
      if (tips == null) { // why not have tag?
        continue;
      }
      hideTarget = hideTarget || tips.mHideTarget;
      if (hideTarget) {
        break;
      }
    }
    targetView.setVisibility(hideTarget ? View.INVISIBLE : View.VISIBLE);
    if (tipsContainerView.getChildCount() == 1) {
      removeContainerView(tipsContainerView, targetView);
    }
  }

  public static void removeContainerView(ViewGroup tipsContainerView, View targetView) {
    ViewGroup parent = (ViewGroup) tipsContainerView.getParent();
    ViewGroup.LayoutParams targetParams = tipsContainerView.getLayoutParams();
    int index = parent.indexOfChild(tipsContainerView);
    parent.removeViewAt(index);
    if (targetView.getParent() != null) {
      ((ViewGroup) targetView.getParent()).removeView(targetView);
    }
    parent.addView(targetView, index, targetParams);
  }

  public static View findChildViewById(ViewGroup parent, int id) {
    final int count = parent.getChildCount();
    for (int i = 0; i < count; ++i) {
      View child = parent.getChildAt(i);
      if (child.getId() == id) {
        return child;
      }
    }
    return null;
  }


  public enum TipsType {

    LOADING(R.layout.progress_loading_view),
    LOADING_MORE(R.layout.search_loading_more_view),
    EMPTY {
      @Override
      public Tips createTips(Context context) {
        return new Tips(AppEmptyView.newInstance(context));
      }
    };

    private int layoutRes;

    private TipsType() {}

    private TipsType(int layoutRes) {
      this.layoutRes = layoutRes;
    }

    public Tips createTips(Context context) {
      return new Tips(context, layoutRes);
    }
  }
}
