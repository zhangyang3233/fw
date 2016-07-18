package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class RefreshableListView extends PullToRefreshAdapterViewBase<ListView> {

  private AbsPullToRefreshAnimationLayout mHeaderLoadingView;
  private AbsPullToRefreshAnimationLayout mFooterLoadingView;

  private FrameLayout mLvFooterLoadingFrame;
  private ListView mListView;

  public RefreshableListView(Context context) {
    super(context);
    setDisableScrollingWhileRefreshing(false);
  }

  public RefreshableListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setDisableScrollingWhileRefreshing(false);
  }

  public RefreshableListView(Context context, Mode mode) {
    super(context, mode);
    setDisableScrollingWhileRefreshing(false);
  }

  @Override
  public ContextMenuInfo getContextMenuInfo() {
    return ((InternalListView) getRefreshableView()).getContextMenuInfo();
  }

  @Override
  public void setLastUpdatedLabel(CharSequence label) {
    super.setLastUpdatedLabel(label);

    if (null != mHeaderLoadingView) {
      mHeaderLoadingView.setSubHeaderText(label);
    }
    if (null != mFooterLoadingView) {
      mFooterLoadingView.setSubHeaderText(label);
    }
  }

  @Override
  public void setLoadingDrawable(Drawable drawable, Mode mode) {
    super.setLoadingDrawable(drawable, mode);

    if (null != mHeaderLoadingView && mode.canPullDown()) {
      mHeaderLoadingView.setLoadingDrawable(drawable);
    }
    if (null != mFooterLoadingView && mode.canPullUp()) {
      mFooterLoadingView.setLoadingDrawable(drawable);
    }
  }

  public void setPullLabel(CharSequence pullLabel, Mode mode) {
    super.setPullLabel(pullLabel, mode);

    if (null != mHeaderLoadingView && mode.canPullDown()) {
      mHeaderLoadingView.setPullLabel(pullLabel);
    }
    if (null != mFooterLoadingView && mode.canPullUp()) {
      mFooterLoadingView.setPullLabel(pullLabel);
    }
  }

  public void setRefreshingLabel(CharSequence refreshingLabel, Mode mode) {
    super.setRefreshingLabel(refreshingLabel, mode);

    if (null != mHeaderLoadingView && mode.canPullDown()) {
      mHeaderLoadingView.setRefreshingLabel(refreshingLabel);
    }
    if (null != mFooterLoadingView && mode.canPullUp()) {
      mFooterLoadingView.setRefreshingLabel(refreshingLabel);
    }
  }

  public void setReleaseLabel(CharSequence releaseLabel, Mode mode) {
    super.setReleaseLabel(releaseLabel, mode);

    if (null != mHeaderLoadingView && mode.canPullDown()) {
      mHeaderLoadingView.setReleaseLabel(releaseLabel);
    }
    if (null != mFooterLoadingView && mode.canPullUp()) {
      mFooterLoadingView.setReleaseLabel(releaseLabel);
    }
  }

  @Override
  protected void onRefreshing(final boolean doScroll) {

    // If we're not showing the Refreshing view, or the list is empty, then
    // the header/footer views won't show so we use the
    // normal method
    ListAdapter adapter = mRefreshableView.getAdapter();
    if (!getShowViewWhileRefreshing() || null == adapter
        || adapter.isEmpty()) {
      super.onRefreshing(doScroll);
      return;
    }

    super.onRefreshing(false);

    final AbsPullToRefreshAnimationLayout originalImageLoadingLayout, listViewImageLoadingLayout;
    final int selection, scrollToY;

    switch (getCurrentMode()) {
      case PULL_UP_TO_REFRESH:
        originalImageLoadingLayout = getFooterLayout();
        listViewImageLoadingLayout = mFooterLoadingView;
        selection = mRefreshableView.getCount() - 1;
        scrollToY = getScrollY() == 0 ? 0 : getScrollY() - getFooterHeight();
        break;
      case PULL_DOWN_TO_REFRESH:
      default:
        originalImageLoadingLayout = getHeaderLayout();
        listViewImageLoadingLayout = mHeaderLoadingView;
        selection = 0;
        scrollToY = getScrollY() == 0 ? 0 : getScrollY() + getHeaderHeight();
        break;
    }

    // Hide our original Loading View
    originalImageLoadingLayout.setVisibility(View.INVISIBLE);

    // Show the ListView Loading View and set it to refresh
    listViewImageLoadingLayout.setVisibility(View.VISIBLE);
    listViewImageLoadingLayout.refreshing();

    if (doScroll) {
      // We scroll slightly so that the ListView's header/footer is at the
      // same Y position as our normal header/footer
      setHeaderScroll(scrollToY);

      // Make sure the ListView is scrolled to show the loading
      // header/footer
      mRefreshableView.setSelection(selection);

      // Smooth scroll as normal
      smoothScrollTo(0);
    }
  }

  @Override
  protected void onReset() {

    // If we're not showing the Refreshing view, or the list is empty, then
    // the header/footer views won't show so we use the
    // normal method
    ListAdapter adapter = mRefreshableView.getAdapter();
    if (!getShowViewWhileRefreshing() || null == adapter
        || adapter.isEmpty()) {
      super.onReset();
      return;
    }

    AbsPullToRefreshAnimationLayout originalImageLoadingLayout, listViewImageLoadingLayout;
    int scrollToHeight, selection;
    boolean scrollLvToEdge;

    switch (getCurrentMode()) {
      case PULL_UP_TO_REFRESH:
        originalImageLoadingLayout = getFooterLayout();
        listViewImageLoadingLayout = mFooterLoadingView;
        selection = mRefreshableView.getCount() - 1;
        scrollToHeight = getFooterHeight();
        scrollLvToEdge = Math.abs(mRefreshableView.getLastVisiblePosition()
            - selection) <= 1;
        break;
      case PULL_DOWN_TO_REFRESH:
      default:
        originalImageLoadingLayout = getHeaderLayout();
        listViewImageLoadingLayout = mHeaderLoadingView;
        scrollToHeight = -getHeaderHeight();
        selection = 0;
        scrollLvToEdge = Math.abs(mRefreshableView
            .getFirstVisiblePosition() - selection) <= 1;
        break;
    }

    // Set our Original View to Visible
    originalImageLoadingLayout.setVisibility(View.VISIBLE);

    /**
     * Scroll so the View is at the same Y as the ListView header/footer,
     * but only scroll if: we've pulled to refresh, it's positioned
     * correctly, and we're currently showing the ListViewLoadingLayout
     */
    if (scrollLvToEdge && getState() != State.MANUAL_REFRESHING
        && listViewImageLoadingLayout.getVisibility() == View.VISIBLE) {
      mRefreshableView.setSelection(selection);
      setHeaderScroll(scrollToHeight);
    }

    // Hide the ListView Header/Footer
    listViewImageLoadingLayout.setVisibility(View.GONE);

    super.onReset();
  }

  protected ListView createListView(Context context, AttributeSet attrs) {
    final ListView lv;
    if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
      lv = new InternalListViewSDK9(context, attrs);
      lv.setOverScrollMode(OVER_SCROLL_NEVER);
    } else {
      lv = new InternalListView(context, attrs);
    }
    return lv;
  }

  @Override
  protected final ListView createRefreshableView(Context context,
      AttributeSet attrs) {
    mListView = createListView(context, attrs);

    // Get Styles from attrs
    TypedArray a = context.obtainStyledAttributes(attrs,
        R.styleable.PullToRefresh);

    // Create Loading Views ready for use later
    FrameLayout frame = new FrameLayout(context);
    mHeaderLoadingView = createLoadingLayout(context,
        Mode.PULL_DOWN_TO_REFRESH, a);
    frame.addView(mHeaderLoadingView,
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.WRAP_CONTENT);
    mHeaderLoadingView.setVisibility(View.GONE);
    mListView.addHeaderView(frame, null, false);

    mLvFooterLoadingFrame = new FrameLayout(context);
    mFooterLoadingView = createLoadingLayout(context,
        Mode.PULL_UP_TO_REFRESH, a);
    mLvFooterLoadingFrame.addView(mFooterLoadingView,
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.WRAP_CONTENT);
    mFooterLoadingView.setVisibility(View.GONE);

    a.recycle();

    // Set it to this so it can be used in ListActivity/ListFragment
    mListView.setId(android.R.id.list);
    return mListView;
  }

  public int getHeaderViewsCount() {
    if (mListView == null) {
      return 0;
    }
    return mListView.getHeaderViewsCount();
  }

  public int getFooterViewsCount() {
    if (mListView == null) {
      return 0;
    }
    return mListView.getFooterViewsCount();
  }

  @TargetApi(9)
  final class InternalListViewSDK9 extends InternalListView {

    public InternalListViewSDK9(Context context, AttributeSet attrs) {
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
      OverscrollHelper.overScrollBy(RefreshableListView.this, deltaY,
          scrollY, isTouchEvent);

      return returnValue;
    }
  }

  protected class InternalListView extends ListView
      implements
        EmptyViewMethodAccessor {

    private boolean mAddedLvFooter = false;

    public InternalListView(Context context, AttributeSet attrs) {
      super(context, attrs);
    }

    @Override
    public void draw(Canvas canvas) {
      /**
       * This is a bit hacky, but ListView has got a bug in it when using
       * Header/Footer Views and the list is empty. This masks the issue
       * so that it doesn't cause an FC. See Issue #66.
       */
      try {
        super.draw(canvas);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public ContextMenuInfo getContextMenuInfo() {
      return super.getContextMenuInfo();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
      // Add the Footer View at the last possible moment
      if (!mAddedLvFooter) {
        addFooterView(mLvFooterLoadingFrame, null, false);
        mAddedLvFooter = true;
      }

      super.setAdapter(adapter);
    }

    @Override
    public void setEmptyView(View emptyView) {
      RefreshableListView.this.setEmptyView(emptyView);
    }

    @Override
    public void setEmptyViewInternal(View emptyView) {
      super.setEmptyView(emptyView);
    }

  }

}
