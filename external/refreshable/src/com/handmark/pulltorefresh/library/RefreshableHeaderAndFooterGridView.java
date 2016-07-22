package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;

public class RefreshableHeaderAndFooterGridView extends
    PullToRefreshAdapterViewBase<HeaderAndFooterGridView> {

  public RefreshableHeaderAndFooterGridView(Context context) {
    super(context);
    setDisableScrollingWhileRefreshing(false);
  }

  public RefreshableHeaderAndFooterGridView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setDisableScrollingWhileRefreshing(false);
  }

  public RefreshableHeaderAndFooterGridView(Context context, Mode mode) {
    super(context, mode);
    setDisableScrollingWhileRefreshing(false);
  }

  @Override
  public ContextMenuInfo getContextMenuInfo() {
    return ((InternalGridViewGridView) getRefreshableView()).getContextMenuInfo();
  }

  @Override
  public final HeaderAndFooterGridView createRefreshableView(Context context,
      AttributeSet attrs) {
    final HeaderAndFooterGridView gv;
    if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
      gv = new InternalGridViewSDK9GridView(context, attrs);
      gv.setNumColumns(2);
    } else {
      gv = new InternalGridViewGridView(context, attrs);
      gv.setNumColumns(2);
    }

    // Use Generated ID (from res/values/ids.xml)
    gv.setId(R.id.refreshable_widget_gridview);
    return gv;
  }

  class InternalGridViewGridView extends HeaderAndFooterGridView
      implements
        EmptyViewMethodAccessor {

    public InternalGridViewGridView(Context context, AttributeSet attrs) {
      super(context, attrs);
    }

    @Override
    public ContextMenuInfo getContextMenuInfo() {
      return super.getContextMenuInfo();
    }

    @Override
    public void setEmptyView(View emptyView) {
      RefreshableHeaderAndFooterGridView.this.setEmptyView(emptyView);
    }

    @Override
    public void setEmptyViewInternal(View emptyView) {
      super.setEmptyView(emptyView);
    }
  }

  @TargetApi(9)
  final class InternalGridViewSDK9GridView extends InternalGridViewGridView {

    public InternalGridViewSDK9GridView(Context context, AttributeSet attrs) {
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
      OverscrollHelper.overScrollBy(RefreshableHeaderAndFooterGridView.this, deltaY,
          scrollY, isTouchEvent);

      return returnValue;
    }
  }
}
