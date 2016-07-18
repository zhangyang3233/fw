package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.ExpandableListView;

public class RefreshableExpandableListView extends
    PullToRefreshAdapterViewBase<ExpandableListView> {

  public RefreshableExpandableListView(Context context) {
    super(context);
    setDisableScrollingWhileRefreshing(false);
  }

  public RefreshableExpandableListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setDisableScrollingWhileRefreshing(false);
  }

  public RefreshableExpandableListView(Context context, Mode mode) {
    super(context, mode);
    setDisableScrollingWhileRefreshing(false);
  }

  @Override
  public ContextMenuInfo getContextMenuInfo() {
    return ((InternalExpandableListView) getRefreshableView())
        .getContextMenuInfo();
  }

  @Override
  protected final ExpandableListView createRefreshableView(Context context,
      AttributeSet attrs) {
    final ExpandableListView lv;
    if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
      lv = new InternalExpandableListViewSDK9(context, attrs);
    } else {
      lv = new InternalExpandableListView(context, attrs);
    }

    // Set it to this so it can be used in ListActivity/ListFragment
    lv.setId(android.R.id.list);
    return lv;
  }

  class InternalExpandableListView extends ExpandableListView implements
      EmptyViewMethodAccessor {

    public InternalExpandableListView(Context context, AttributeSet attrs) {
      super(context, attrs);
    }

    public ContextMenuInfo getContextMenuInfo() {
      return super.getContextMenuInfo();
    }

    @Override
    public void setEmptyView(View emptyView) {
      RefreshableExpandableListView.this.setEmptyView(emptyView);
    }

    @Override
    public void setEmptyViewInternal(View emptyView) {
      super.setEmptyView(emptyView);
    }
  }

  @TargetApi(9)
  final class InternalExpandableListViewSDK9 extends
      InternalExpandableListView {

    public InternalExpandableListViewSDK9(Context context,
        AttributeSet attrs) {
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
      OverscrollHelper.overScrollBy(RefreshableExpandableListView.this,
          deltaY, scrollY, isTouchEvent);

      return returnValue;
    }
  }
}
