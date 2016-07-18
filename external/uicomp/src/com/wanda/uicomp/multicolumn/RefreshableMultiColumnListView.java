package com.wanda.uicomp.multicolumn;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

import com.handmark.pulltorefresh.library.AbsPullToRefreshAnimationLayout;
import com.handmark.pulltorefresh.library.EmptyViewMethodAccessor;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.wanda.uicomp.R;

public class RefreshableMultiColumnListView extends
    PullToRefreshMultiColumnAdapterViewBase<MultiColumnListView> {

  private AbsPullToRefreshAnimationLayout mHeaderLoadingView;
  private AbsPullToRefreshAnimationLayout mFooterLoadingView;

  private FrameLayout mLvFooterLoadingFrame;

  public RefreshableMultiColumnListView(Context context) {
    super(context);
    setDisableScrollingWhileRefreshing(false);
  }

  public RefreshableMultiColumnListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setDisableScrollingWhileRefreshing(false);
  }

  public RefreshableMultiColumnListView(Context context, PullToRefreshBase.Mode mode) {
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
  public void setLoadingDrawable(Drawable drawable, PullToRefreshBase.Mode mode) {
    super.setLoadingDrawable(drawable, mode);

    if (null != mHeaderLoadingView && mode.canPullDown()) {
      mHeaderLoadingView.setLoadingDrawable(drawable);
    }
    if (null != mFooterLoadingView && mode.canPullUp()) {
      mFooterLoadingView.setLoadingDrawable(drawable);
    }
  }

  public void setPullLabel(CharSequence pullLabel, PullToRefreshBase.Mode mode) {
    super.setPullLabel(pullLabel, mode);

    if (null != mHeaderLoadingView && mode.canPullDown()) {
      mHeaderLoadingView.setPullLabel(pullLabel);
    }
    if (null != mFooterLoadingView && mode.canPullUp()) {
      mFooterLoadingView.setPullLabel(pullLabel);
    }
  }

  public void setRefreshingLabel(CharSequence refreshingLabel, PullToRefreshBase.Mode mode) {
    super.setRefreshingLabel(refreshingLabel, mode);

    if (null != mHeaderLoadingView && mode.canPullDown()) {
      mHeaderLoadingView.setRefreshingLabel(refreshingLabel);
    }
    if (null != mFooterLoadingView && mode.canPullUp()) {
      mFooterLoadingView.setRefreshingLabel(refreshingLabel);
    }
  }

  public void setReleaseLabel(CharSequence releaseLabel, PullToRefreshBase.Mode mode) {
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
    if (!getShowViewWhileRefreshing() || null == adapter || adapter.isEmpty()) {
      super.onRefreshing(doScroll);
      return;
    }

    super.onRefreshing(false);

    final AbsPullToRefreshAnimationLayout originalLoadingLayout, listViewLoadingLayout;
    final int selection, scrollToY;

    switch (getCurrentMode()) {
      case PULL_UP_TO_REFRESH:
        originalLoadingLayout = getFooterLayout();
        listViewLoadingLayout = mFooterLoadingView;
        selection = mRefreshableView.getCount() - 1;
        scrollToY = getScrollY() - getFooterHeight();
        break;
      case PULL_DOWN_TO_REFRESH:
      default:
        originalLoadingLayout = getHeaderLayout();
        listViewLoadingLayout = mHeaderLoadingView;
        selection = 0;
        scrollToY = getScrollY() + getHeaderHeight();
        break;
    }

    // Hide our original Loading View
    originalLoadingLayout.setVisibility(View.INVISIBLE);

    // Show the ListView Loading View and set it to refresh
    listViewLoadingLayout.setVisibility(View.VISIBLE);
    listViewLoadingLayout.refreshing();

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
    if (!getShowViewWhileRefreshing() || null == adapter || adapter.isEmpty()) {
      super.onReset();
      return;
    }

    AbsPullToRefreshAnimationLayout originalLoadingLayout, listViewLoadingLayout;
    int scrollToHeight, selection;
    boolean scrollLvToEdge;

    switch (getCurrentMode()) {
      case PULL_UP_TO_REFRESH:
        originalLoadingLayout = getFooterLayout();
        listViewLoadingLayout = mFooterLoadingView;
        selection = mRefreshableView.getCount() - 1;
        scrollToHeight = getFooterHeight();
        scrollLvToEdge = Math.abs(mRefreshableView.getLastVisiblePosition() -
            selection) <= 1;
        break;
      case PULL_DOWN_TO_REFRESH:
      default:
        originalLoadingLayout = getHeaderLayout();
        listViewLoadingLayout = mHeaderLoadingView;
        scrollToHeight = -getHeaderHeight();
        selection = 0;
        scrollLvToEdge = Math.abs(mRefreshableView.getFirstVisiblePosition() -
            selection) <= 1;
        break;
    }

    // Set our Original View to Visible
    originalLoadingLayout.setVisibility(View.VISIBLE);

    /**
     * Scroll so the View is at the same Y as the ListView header/footer,
     * but only scroll if: we've pulled to refresh, it's positioned
     * correctly, and we're currently showing the ListViewLoadingLayout
     */
    if (scrollLvToEdge && getState() != PullToRefreshBase.State.MANUAL_REFRESHING &&
        listViewLoadingLayout.getVisibility() == View.VISIBLE) {
      mRefreshableView.setSelection(selection);
      setHeaderScroll(scrollToHeight);
    }

    // Hide the ListView Header/Footer
    listViewLoadingLayout.setVisibility(View.GONE);

    super.onReset();
  }

  protected MultiColumnListView createListView(Context context, AttributeSet attrs) {
    final MultiColumnListView lv;
    lv = new InternalListView(context, attrs);
    return lv;
  }

  @Override
  protected final MultiColumnListView createRefreshableView(Context context,
                                                            AttributeSet attrs) {
    MultiColumnListView lv = createListView(context, attrs);

    // Get Styles from attrs
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefresh);

    // Create Loading Views ready for use later
    FrameLayout frame = new FrameLayout(context);
    mHeaderLoadingView = createLoadingLayout(context, PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH, a);
    frame.addView(mHeaderLoadingView, FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.WRAP_CONTENT);
    mHeaderLoadingView.setVisibility(View.GONE);
    lv.addHeaderView(frame, null, false);

    mLvFooterLoadingFrame = new FrameLayout(context);
    mFooterLoadingView = createLoadingLayout(context, PullToRefreshBase.Mode.PULL_UP_TO_REFRESH, a);
    mLvFooterLoadingFrame.addView(mFooterLoadingView, FrameLayout.LayoutParams
        .MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    mFooterLoadingView.setVisibility(View.GONE);

    a.recycle();

    // Set it to this so it can be used in ListActivity/ListFragment
    lv.setId(android.R.id.list);
    return lv;
  }

  protected class InternalListView extends MultiColumnListView implements
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
      RefreshableMultiColumnListView.this.setEmptyView(emptyView);
    }

    @Override
    public void setEmptyViewInternal(View emptyView) {
      super.setEmptyView(emptyView);
    }

  }
}
