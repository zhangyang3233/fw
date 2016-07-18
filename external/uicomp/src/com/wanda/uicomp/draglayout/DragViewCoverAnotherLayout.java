package com.wanda.uicomp.draglayout;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.fw.zycoder.appbase.R;

import java.lang.ref.WeakReference;

/**
 * @author zhangyuwen 2015-12-11
 */
public class DragViewCoverAnotherLayout extends DragLayout {
  private static final int DURATION = 800;

  private ViewDragHelper mDragHelper;
  private View mDragView;
  private View mHeaderView;

  private float mInitialMotionX;
  private float mInitialMotionY;

  private boolean mDragged = false;

  private DraggedStatusListener mDraggedStatusListener;

  public DragViewCoverAnotherLayout(Context context) {
    super(context, null);
  }

  public DragViewCoverAnotherLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DragViewCoverAnotherLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    mDragView = findViewById(R.id.drag_view);
    mHeaderView = findViewById(R.id.header_view);
  }

  /**
   * 何时需要拦截手势?
   * 1.左右滑动的事件，不进行拦截
   * 2.上下滑动的事件，分为pull up，pull down
   * pull up 时，child.getTop() > getPaddingTop()
   * pull down时，child.getTop() < getPaddingTop() + headerView.getHeight();
   */
  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    final int action = MotionEventCompat.getActionMasked(ev);
    final int actionIndex = MotionEventCompat.getActionIndex(ev);
    final int pointerId = MotionEventCompat.getPointerId(ev, actionIndex);
    if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
      mDragHelper.cancel();
      return false;
    }

    final float x = ev.getX();
    final float y = ev.getY();
    boolean intercepted = false;
    switch (action) {
      case MotionEvent.ACTION_DOWN: {
        mInitialMotionX = x;
        mInitialMotionY = y;
        break;
      }
      case MotionEvent.ACTION_MOVE: {
        final float xDiff = Math.abs(x - mInitialMotionX);
        final float yDiff = Math.abs(y - mInitialMotionY);
        final int slop = mDragHelper.getTouchSlop();
        // 左右滑动不拦截
        if (xDiff > yDiff && xDiff > slop) {
          mInitialMotionX = x;
          mInitialMotionY = y;
          return false;
        }

        // 上下滑动
        if (yDiff > xDiff && yDiff > slop) {
          final float dy = y - mInitialMotionY;
          // pull down
          if (dy > 0 && mDragged) {
            if (needPullDownChild()) {
              return false;
            }
            if (mDragView.getTop() >= getPaddingTop() + mHeaderView.getHeight()) {
              return false;
            }
            intercepted = true;
            mDragHelper.captureChildView(mDragView, pointerId);
          }
          // pull up
          if (dy < 0 && !mDragged) {
            if (mDragView.getTop() <= getPaddingTop()) {
              return false;
            }
            intercepted = true;
            mDragHelper.captureChildView(mDragView, pointerId);
          }
        }
      }
    }
    return mDragHelper.shouldInterceptTouchEvent(ev) || intercepted;
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    mDragHelper.processTouchEvent(ev);
    final int action = MotionEventCompat.getActionMasked(ev);
    switch (action & MotionEventCompat.ACTION_MASK) {
      case MotionEvent.ACTION_DOWN: {
        final float x = ev.getX();
        final float y = ev.getY();
        mInitialMotionX = x;
        mInitialMotionY = y;
        break;
      }

      case MotionEvent.ACTION_UP: {
        break;
      }
    }

    return true;
  }

  @Override
  public void computeScroll() {
    if (mDragHelper.continueSettling(true)) {
      ViewCompat.postInvalidateOnAnimation(this);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthSize = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
    int heightSize = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

    int maxLayoutHeight = heightSize - getPaddingTop() - getPaddingBottom();
    int widthAvailable = widthSize - getPaddingLeft() - getPaddingRight();

    for (int i = 0; i < getChildCount(); ++i) {
      View child = getChildAt(i);
      final LayoutParams lp = child.getLayoutParams();

      int childWidthSpec;
      final int horizontalMargin = 0;
      if (lp.width == LayoutParams.WRAP_CONTENT) {
        childWidthSpec =
            MeasureSpec.makeMeasureSpec(widthAvailable - horizontalMargin, MeasureSpec.AT_MOST);
      } else if (lp.width == LayoutParams.MATCH_PARENT) {
        childWidthSpec =
            MeasureSpec.makeMeasureSpec(widthAvailable - horizontalMargin, MeasureSpec.EXACTLY);
      } else {
        childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
      }

      int childHeightSpec;
      if (lp.height == LayoutParams.WRAP_CONTENT) {
        childHeightSpec = MeasureSpec.makeMeasureSpec(maxLayoutHeight, MeasureSpec.AT_MOST);
      } else if (lp.height == LayoutParams.FILL_PARENT) {
        childHeightSpec = MeasureSpec.makeMeasureSpec(maxLayoutHeight, MeasureSpec.EXACTLY);
      } else {
        childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
      }

      child.measure(childWidthSpec, childHeightSpec);
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    if (mDragged) {
      mHeaderView.layout(l, t, r, t + mHeaderView.getMeasuredHeight());
      mDragView.layout(l, t, r, b);
    } else {
      mHeaderView.layout(l, t, r, t + mHeaderView.getMeasuredHeight());
      mDragView.layout(l, t + mHeaderView.getMeasuredHeight(), r,
          b + mHeaderView.getMeasuredHeight());
    }
  }

  public void registerTouchInterceptor(TouchInterceptor interceptor) {
    if (interceptor != null) {
      mInterceptorRef = new WeakReference<>(interceptor);
    } else {
      mInterceptorRef = null;
    }
  }

  public boolean needPullDownChild() {
    if (mInterceptorRef == null) {
      return false;
    }
    TouchInterceptor interceptor = mInterceptorRef.get();
    if (interceptor == null) {
      return false;
    }
    return interceptor.willHandlePullDown();
  }

  public boolean needPullUpChild() {
    if (mInterceptorRef == null) {
      return false;
    }
    TouchInterceptor interceptor = mInterceptorRef.get();
    if (interceptor == null) {
      return false;
    }
    return interceptor.willHandlePullUp();
  }

  public boolean isDragged() {
    return mDragged;
  }

  public void resetDraggedStatus() {
    mDragged = false;
    invalidate();
  }

  public void smoothResetDraggedStatus() {
    if (!isDragged()) {
      return;
    }
    mDragHelper.setCapturedView(mDragView);
    mDragged = false;
    smoothScrollTo(mDragView.getLeft(), getPaddingTop() + mHeaderView.getHeight());
  }

  private boolean smoothScrollTo(int finalLeft, int finalTop) {
    final int startLeft = mDragView.getLeft();
    final int startTop = mDragView.getTop();
    final int dx = finalLeft - startLeft;
    final int dy = finalTop - startTop;

    if (dx == 0 && dy == 0) {
      // Nothing to do. Send callbacks, be done.
      mDragHelper.mScroller.abortAnimation();
      mDragHelper.setDragState(mDragHelper.STATE_IDLE);
      return false;
    }

    mDragHelper.mScroller.startScroll(startLeft, startTop, dx, dy, DURATION);
    mDragHelper.setDragState(mDragHelper.STATE_SETTLING);
    return true;
  }

  private class DragHelperCallback extends ViewDragHelper.Callback {
    @Override
    public void onViewDragStateChanged(int state) {
      super.onViewDragStateChanged(state);
      notifyDraggedChanged(state);
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
      super.onViewPositionChanged(changedView, left, top, dx, dy);
    }

    @Override
    public void onViewCaptured(View capturedChild, int activePointerId) {
      super.onViewCaptured(capturedChild, activePointerId);
    }

    @Override
    public void onViewReleased(View releasedChild, float xvel, float yvel) {
      int left = releasedChild.getLeft();
      int top = releasedChild.getTop();
      int height = mHeaderView.getHeight();
      int newTop;

      if (mDragged) {
        if ((yvel > 0 && Math.abs(yvel) > ViewConfiguration.getMinimumFlingVelocity())
            || top > getPaddingTop() + 0.5 * height) {
          mDragged = !mDragged;
        }
      } else {
        if ((yvel < 0 && Math.abs(yvel) > ViewConfiguration.getMinimumFlingVelocity())
            || top < getPaddingTop() + 0.5 * height) {
          mDragged = !mDragged;
        }
      }

      if (mDragged) {
        newTop = getPaddingTop();
      } else {
        newTop = getPaddingTop() + height;
      }

      mDragHelper.settleCapturedViewAt(left, newTop);
      invalidate();
    }

    @Override
    public void onEdgeTouched(int edgeFlags, int pointerId) {
      super.onEdgeTouched(edgeFlags, pointerId);
    }

    @Override
    public boolean onEdgeLock(int edgeFlags) {
      return super.onEdgeLock(edgeFlags);
    }

    @Override
    public void onEdgeDragStarted(int edgeFlags, int pointerId) {
      super.onEdgeDragStarted(edgeFlags, pointerId);
    }

    @Override
    public int getOrderedChildIndex(int index) {
      return super.getOrderedChildIndex(index);
    }

    @Override
    public int getViewHorizontalDragRange(View child) {
      return super.getViewHorizontalDragRange(child);
    }

    @Override
    public int getViewVerticalDragRange(View child) {
      return child.getTop() - getPaddingTop();
    }

    @Override
    public int clampViewPositionHorizontal(View child, int left, int dx) {
      return super.clampViewPositionHorizontal(child, left, dx);
    }

    @Override
    public int clampViewPositionVertical(View child, int top, int dy) {
      final int topBound = getPaddingTop();
      final int bottomBound = getPaddingTop() + mHeaderView.getHeight();
      final int newTop = Math.min(Math.max(top, topBound), bottomBound);
      return newTop;
    }

    @Override
    public boolean tryCaptureView(View child, int pointerId) {
      return child == mDragView;
    }
  }

  private void notifyDraggedChanged(int state) {
    if (mDraggedStatusListener != null) {
      mDraggedStatusListener.onDraggedStatusChanged(state);
    }
  }

  public void setDraggedStatusListener(DraggedStatusListener draggedStatusListener) {
    if (draggedStatusListener == null) {
      return;
    }
    mDraggedStatusListener = draggedStatusListener;
  }

  public interface DraggedStatusListener {
    void onDraggedStatusChanged(int state);
  }
}
