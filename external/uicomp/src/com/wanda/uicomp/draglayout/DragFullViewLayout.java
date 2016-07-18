package com.wanda.uicomp.draglayout;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wanda.uicomp.R;

import java.lang.ref.WeakReference;

/**
 * @author heqinglong@wanda.cn (He QingLong)
 */
public class DragFullViewLayout extends DragLayout {

  private ViewDragHelper mDragHelper;
  private View mDragView;

  private float mInitialMotionX;
  private float mInitialMotionY;
  private boolean mIsDragged;
  private int mTop;
  private int mFullScreenHeight;
  private View mHeaderView;
  private View mContentView;
  private OnViewPositionChangeListener mPositionChangeListener;
  private boolean mIsDispatchEvent;
  private boolean mIsDispatchInterceptEvent;

  public DragFullViewLayout(Context context) {
    this(context, null);
  }

  public DragFullViewLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DragFullViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mDragHelper = ViewDragHelper.create(this, 1.0f, new DragCallBack());
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    mDragView = getChildAt(0);
    mHeaderView = findViewById(R.id.header_view);
    mContentView = findViewById(R.id.content_view);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    if (mIsDispatchEvent) {
      ev.setAction(MotionEvent.ACTION_DOWN);
      mIsDispatchEvent = false;
    }
    final int action = MotionEventCompat.getActionMasked(ev);
    final float x = ev.getX();
    final float y = ev.getY();
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        mInitialMotionX = ev.getX();
        mInitialMotionY = ev.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        final float adx = Math.abs(x - mInitialMotionX);
        final float ady = Math.abs(y - mInitialMotionY);
        final int slop = mDragHelper.getTouchSlop();
        if (ady > slop) { // 上下滑动
          final float dy = y - mInitialMotionY;
          // pull down
          if (dy > 0 && (mIsDragged || mTop < 0)) {
            if (!needPullDownChild() && mIsDispatchInterceptEvent) {
              requestDisallowInterceptTouchEvent(false);
              ev.setAction(MotionEvent.ACTION_DOWN);
              mIsDispatchInterceptEvent = false;
            }
          }
        }
    }
    return super.dispatchTouchEvent(ev);
  }

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
    boolean isIntercept = false;
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        mInitialMotionX = ev.getX();
        mInitialMotionY = ev.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        final float adx = Math.abs(x - mInitialMotionX);
        final float ady = Math.abs(y - mInitialMotionY);
        final int slop = mDragHelper.getTouchSlop();
        // 左右滑动不拦截，
        if (ady > slop && adx > ady && adx - ady > slop * 1.5) {
          mInitialMotionX = x;
          mInitialMotionY = y;
          return false;
        } else if (ady > slop) { // 上下滑动
          final float dy = y - mInitialMotionY;
          // pull down
          if (dy > 0 && (mIsDragged || mTop < 0)) {
            if (needPullDownChild()) {
              return false;
            }
            isIntercept = true;
            mDragHelper.captureChildView(mDragView, pointerId);
          }
          // pull up
          if (dy < 0 && !mIsDragged) {
            isIntercept = true;
            mDragHelper.captureChildView(mDragView, pointerId);
          }
        }
        break;
    }
    mDragHelper.shouldInterceptTouchEvent(ev);
    return isIntercept;
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    mDragHelper.processTouchEvent(ev);
    int action = ev.getAction();
    if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
      mDragHelper.cancel();
      return true;
    }
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        mInitialMotionX = ev.getX();
        mInitialMotionY = ev.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        break;
    }
    return true;
  }

  @Override
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

  @Override
  public boolean isDragged() {
    return mIsDragged;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    measureChildren(widthMeasureSpec, heightMeasureSpec);
    int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
    mFullScreenHeight = MeasureSpec.getSize(heightMeasureSpec);
    setMeasuredDimension(maxWidth, mFullScreenHeight + getHeaderHeight());
    invalidate();
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    setContentViewHeight();
    mDragView.layout(l, t + mTop, r, b);
  }

  @Override
  public void computeScroll() {
    if (mDragHelper.continueSettling(true)) {
      ViewCompat.postInvalidateOnAnimation(this);
    }
  }

  private void setContentViewHeight() {
    if (mContentView != null && mContentView.getHeight() != 0) {
      LayoutParams layoutParams = mContentView.getLayoutParams();
      layoutParams.height = mFullScreenHeight - mHeaderView.getTop();
      mContentView.setLayoutParams(layoutParams);
    }
  }

  class DragCallBack extends ViewDragHelper.Callback {

    @Override
    public void onViewDragStateChanged(int state) {
      super.onViewDragStateChanged(state);
      if (state == ViewDragHelper.STATE_IDLE) {
        mTop = mDragView.getTop();
        if (mTop == -getHeaderHeight()) {
          mIsDragged = true;
        }
      }
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
      if (mPositionChangeListener != null) {
        mPositionChangeListener.onViewPositionChanged(changedView, left, top, dx, dy);
      }
      super.onViewPositionChanged(changedView, left, top, dx, dy);
    }

    @Override
    public int clampViewPositionVertical(View child, int top, int dy) {
      mTop = top;
      if (top >= 0) {
        mIsDragged = false;
        mTop = 0;
        return 0;
      }
      if (top > -getHeaderHeight()) {
        mIsDragged = false;
        return top;
      } else {
        mIsDragged = true;
        mIsDispatchEvent = true;
        mIsDispatchInterceptEvent = true;
        mTop = -getHeaderHeight();
        requestDisallowInterceptTouchEvent(true);
        return -getHeaderHeight();
      }
    }

    @Override
    public boolean tryCaptureView(View child, int pointerId) {
      return true;
    }

    @Override
    public void onViewReleased(View releasedChild, float xvel, float yvel) {
      super.onViewReleased(releasedChild, xvel, yvel);
      mDragHelper.flingCapturedView(mDragView.getLeft(), -getHeaderHeight(), mDragView.getLeft(),
          0);
      postInvalidate();
    }
  }

  public void registerTouchInterceptor(TouchInterceptor interceptor) {
    if (interceptor != null) {
      mInterceptorRef = new WeakReference<>(interceptor);
    } else {
      mInterceptorRef = null;
    }
  }

  public boolean isDragToTop() {
    return mTop >= 0;
  }

  public int getHeaderHeight() {
    if (mHeaderView != null) {
      return mHeaderView.getMeasuredHeight();
    }
    return 0;
  }

  public void setOnViewPositionChangeListener(OnViewPositionChangeListener listener) {
    mPositionChangeListener = listener;
  }

  public interface OnViewPositionChangeListener {
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy);
  }

}
