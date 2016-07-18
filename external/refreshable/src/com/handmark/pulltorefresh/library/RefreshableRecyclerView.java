package com.handmark.pulltorefresh.library;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ScrollView;

/**
 * Description:
 *
 * @author lizhenhua9@wanda.cn (Lzh)
 * @date 15/8/20 11:27
 */

public class RefreshableRecyclerView extends PullToRefreshBase<RecyclerView> {

  private IndicatorLayout mIndicatorIvTop;
  private IndicatorLayout mIndicatorIvBottom;
  private boolean mShowIndicator;

  public RefreshableRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected RecyclerView createRefreshableView(Context context,
      AttributeSet attrs) {
    RecyclerView recyclerView;
    if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
      recyclerView = new InternalScrollViewSDK9(context, attrs);
    } else {
      recyclerView = new RecyclerView(context, attrs);
    }
    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setId(R.id.refreshable_widget_scrollview);
    return recyclerView;
  }

  @Override
  public ContextMenuInfo getContextMenuInfo() {
    return ((InternalScrollViewSDK9) getRefreshableView()).getContextMenuInfo();
  }

  @Override
  protected boolean isReadyForPullDown() {
      LinearLayoutManager linearLayoutManager =
              (LinearLayoutManager) mRefreshableView.getLayoutManager();
      if(linearLayoutManager != null){
          return linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;
      }else {
          return false;
      }
  }

  @Override
  protected void handleStyledAttributes(TypedArray a) {
    // Set Show Indicator to the XML value, or default value
    mShowIndicator = a.getBoolean(
        R.styleable.PullToRefresh_ptrShowIndicator,
        !isPullToRefreshOverScrollEnabled());
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

  public void setAdapter(RecyclerView.Adapter adapter) {
    mRefreshableView.setAdapter(adapter);
  }

  @TargetApi(9)
  final class InternalScrollViewSDK9 extends RecyclerView {

    public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
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
      OverscrollHelper.overScrollBy(RefreshableRecyclerView.this, deltaY,
          scrollY, getScrollRange(), isTouchEvent);

      return returnValue;
    }

    @Override
    protected ContextMenuInfo getContextMenuInfo() {
      return super.getContextMenuInfo();
    }

    /**
     * Taken from the AOSP RecyclerView source
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
