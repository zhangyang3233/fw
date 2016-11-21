package com.zy.widgets.text;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class OverScrollView extends ScrollView {
  private static final String TAG = OverScrollView.class.getSimpleName();

  private static final float ELASTICITY_COEFFICIENT = 0.4f;

  private static final int NO_OVERSCROLL_STATE = 0;


  private static final int TOP_OVERSCROLL_STATE = 1;


  private static final int BOTTOM_OVERSCROLL_STATE = 2;


  private static final int OVERSCROLL_MAX_HEIGHT = 1200;


  private static final int INVALID_POINTER = -1;


  private static final int TRIGGER_HEIGHT = 120;


  private int overScrollSate;


  private boolean mIsUseOverScroll = true;


  private boolean isRecord;


  private OverScrollWarpLayout mContentLayout;


  private OverScrollListener mOverScrollListener;


  private OverScrollTinyListener mOverScrollTinyListener;


  private OnScrollListener mScrollListener;

  private float mLastMotionY;


  private int overScrollDistance;


  private int mActivePointerId = INVALID_POINTER;


  private boolean isOnTouch;


  private boolean isInertance;


  private boolean mIsUseInertance = true;


  private boolean mIsBanQuickScroll;

  private int inertanceY;


  private int mOverScrollTrigger = TRIGGER_HEIGHT;

  public OverScrollView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initScrollView();
  }

  public OverScrollView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public OverScrollView(Context context) {
    this(context, null);
  }

  private void initScrollView() {
    // 去掉过度滑动的阴影绘制
    if (Build.VERSION.SDK_INT >= 9) {
      setOverScrollMode(View.OVER_SCROLL_NEVER);
    } else {
      ViewCompat.setOverScrollMode(this, ViewCompat.OVER_SCROLL_NEVER);
    }
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {

    if (!mIsUseOverScroll) {
      return super.onInterceptTouchEvent(ev);
    }
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        if (isOverScrolled()) {
          isRecord = true;
          // Remember where the motion event started
          mLastMotionY = (int) ev.getY();

          mActivePointerId = ev.getPointerId(0);
        }
        break;
      case MotionEvent.ACTION_MOVE:
        if (isRecord && Math.abs(ev.getY() - mLastMotionY) > 20) {
          return true;
        }
        break;
      case MotionEvent.ACTION_CANCEL:
        if (isRecord) {
          isRecord = false;
        }
    }
    return super.onInterceptTouchEvent(ev);
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    isOnTouch = true;
    if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
      if (mOverScrollTinyListener != null) {
        mOverScrollTinyListener.scrollLoosen();
      }
      isOnTouch = false;
    }

    // ãκδ
    if (!mIsUseOverScroll) {
      return super.onTouchEvent(ev);
    }

    if (!isOverScrolled()) {
      mLastMotionY = (int) ev.getY();
      return super.onTouchEvent(ev);
    }

    switch (ev.getAction() & MotionEvent.ACTION_MASK) {
      case MotionEvent.ACTION_DOWN:
        mActivePointerId = ev.getPointerId(0);
        mLastMotionY = (int) ev.getY();
        break;
      case MotionEvent.ACTION_POINTER_DOWN:
        final int index = ev.getActionIndex();
        mLastMotionY = (int) ev.getY(index);
        mActivePointerId = ev.getPointerId(index);
        break;
      case MotionEvent.ACTION_POINTER_UP:
        onSecondaryPointerUp(ev);
        if (mActivePointerId != INVALID_POINTER) {
          mLastMotionY = (int) ev.getY(ev.findPointerIndex(mActivePointerId));
        }
        break;
      case MotionEvent.ACTION_MOVE:
        if (isRecord) {
          final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
          if (activePointerIndex == -1) {
            break;
          }

          final float y = ev.getY(activePointerIndex);
          // 算出移动的Y轴距离
          int deltaY = (int) (mLastMotionY - y);
          // 更新最后有效触控点位置
          mLastMotionY = y;

          // 如果过度滑动已经超出最大过度滑动距离 && deltaY 所指的方向还在使距离增大，则令 deltaY 为0
          if (Math.abs(overScrollDistance) >= OVERSCROLL_MAX_HEIGHT
              && overScrollDistance * deltaY > 0) {
            deltaY = 0;
          }
          Log.i(TAG, String.valueOf(deltaY));
          // 异常处理， 如果用户在顶部过度滑动小量值，然后迅速反方向恢复滑动，这时候可能让偏移量恢复过度，此时让布局自动调整正常状态
          if (overScrollDistance * (overScrollDistance + deltaY) < 0) {
            mContentLayout.smoothScrollToNormal();
            overScrollDistance = 0;
            break;
          }

          // 异常处理，同上，在底部的处理
          if ((!isOnBottom() && overScrollDistance > 0) || (!isOnTop() && overScrollDistance < 0)) {
            mContentLayout.smoothScrollToNormal();
            overScrollDistance = 0;
            break;
          }

          // 计算弹性系数
          if (overScrollDistance * deltaY > 0) {
            deltaY = (int) (deltaY * ELASTICITY_COEFFICIENT);
          }

          // // 在零界点减速 0.5
          // if (overScrollDistance == 0) {
          // deltaY = (int) (deltaY * ELASTICITY_COEFFICIENT * 0.5f);
          // }

          if (overScrollDistance == 0 && deltaY == 0) {
            break;
          }

          // 过度滑动速度控制
          // if (Math.abs(deltaY) > 20) {
          // deltaY = deltaY > 0 ? 20 : -20;
          // }

          overScrollDistance += deltaY;

          if (isOnTop() && overScrollDistance > 0 && !isOnBottom()) {
            overScrollDistance = 0;
            break;
          }

          if (isOnBottom() && overScrollDistance < 0 && !isOnTop()) {
            overScrollDistance = 0;
            break;
          }

          // ͼ
          mContentLayout.smoothScrollBy(0, deltaY);

          if (mOverScrollTinyListener != null) {
            mOverScrollTinyListener.scrollDistance(deltaY, overScrollDistance);
          }
          return true;
        }
        break;
      case MotionEvent.ACTION_CANCEL:
      case MotionEvent.ACTION_UP:
        mContentLayout.smoothScrollToNormal();
        overScrollTrigger();
        // ûܾ
        overScrollDistance = 0;
        // ñ
        isRecord = false;
        // ָid
        mActivePointerId = INVALID_POINTER;
        break;

      default:
        break;
    }
    return super.onTouchEvent(ev);
  }

  private void onSecondaryPointerUp(MotionEvent ev) {
    // 获取抬起手指的这个事件的 pointIndex： 将 pointIndex 信息从 Action 中分离出来， 同时右移 8 位即是该index值
    final int pointerIndex = (ev.getAction()
        & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    final int pointerId = ev.getPointerId(pointerIndex);
    // pointIndex：当前按下的手指按时间先后顺序进行排序的序号
    // 先后按下：A, B , C , D, E
    // 则index：0, 1 , 2 , 3, 4
    // 后AB松开：C, D，E
    // 则index： 0, 1, 2

    if (pointerId == mActivePointerId) {
      // This was our active pointer going up. Choose a new
      // active pointer and adjust accordingly.
      // TODO: Make this decision more intelligent.
      final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
      mLastMotionY = (int) ev.getY(newPointerIndex);
      mActivePointerId = ev.getPointerId(newPointerIndex);
    }

  }

  public boolean isOverScrolled() {
    return isOnTop() || isOnBottom();
  }

  private boolean isOnTop() {
    return getScrollY() == 0;
  }

  private boolean isOnBottom() {
    return getScrollY() + getHeight() == mContentLayout.getHeight();
  }


  private void initOverScrollLayout() {
    // 当子控件的高度没有达到ScrollView的高度， 让子控件的高度和ScrollView一样，需要在子控件 match_parent
    // 的基础上设置setFillViewport(true), 否则单纯的给子控件设置match_parent是没有效果的
    setFillViewport(true);
    if (mContentLayout == null) {
      int childCount = getChildCount();
      if (childCount > 1) {
        throw new IllegalStateException("ScrollView can host only one direct child");
      }
      mContentLayout = new OverScrollWarpLayout(getContext());
      if (childCount == 1) {
        View child = getChildAt(0);
        this.removeAllViews();
        mContentLayout.addView(child);
      }
      this.addView(mContentLayout,
          new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
    // mIsUseOverScroll = true;
  }


  public void setOverScroll(boolean isOverScroll) {
    mIsUseOverScroll = isOverScroll;
  }


  public void setUseInertance(boolean isInertance) {
    mIsUseInertance = isInertance;
  }

  @Override
  protected void onAttachedToWindow() {
    initOverScrollLayout();
    super.onAttachedToWindow();
  }

  public int getScrollState() {
    invalidateState();
    return overScrollSate;
  }


  private void invalidateState() {

    if (mContentLayout.getScrollerCurrY() == 0) {
      overScrollSate = NO_OVERSCROLL_STATE;
    }

    if (mContentLayout.getScrollerCurrY() < 0) {
      overScrollSate = TOP_OVERSCROLL_STATE;
    }

    if (mContentLayout.getScrollerCurrY() > 0) {
      overScrollSate = BOTTOM_OVERSCROLL_STATE;
    }
  }


  /**
   * @param l 变化后的X轴位置
   * @param t 变化后的Y轴的位置
   * @param oldl 原先的X轴的位置
   * @param oldt 原先的Y轴的位置
   */
  @Override
  protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    if (mScrollListener != null && overScrollDistance == 0) {
      mScrollListener.onScroll(l, t, oldl, oldt);
    }
    super.onScrollChanged(l, t, oldl, oldt);
  }

  /**
   * @param scrollX 距离原点的X轴的距离
   * @param scrollY 距离原点的Y轴的距离
   * @param clampedX 当ScrollView滑动到左右侧边界的时候值为true
   * @param clampedY 当ScrollView滑动到上下边界的时候值为true
   */
  @Override
  protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
      boolean clampedY) {
    if (mIsUseInertance && !isInertance && scrollY != 0) {
      isInertance = true;
    }
    if (clampedY && !isOnTouch && isInertance) {
      mContentLayout.smoothScrollBy(0, inertanceY);
      postDelayed(new Runnable() {
        @Override
        public void run() {
          mContentLayout.smoothScrollToNormal();
        }
      }, 100);
      inertanceY = 0;
    }
    super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
  }


  public void setOverScrollListener(OverScrollListener listener) {
    mOverScrollListener = listener;
  }


  public void setOverScrollTinyListener(OverScrollTinyListener listener) {
    mOverScrollTinyListener = listener;
  }


  public void setOnScrollListener(OnScrollListener listener) {
    mScrollListener = listener;
  }


  public void setOverScrollTrigger(int height) {
    if (height >= 30) {
      mOverScrollTrigger = height;
    }
  }

  private void overScrollTrigger() {
    if (mOverScrollListener == null) {
      return;
    }

    if (overScrollDistance > mOverScrollTrigger && isOnBottom()) {
      mOverScrollListener.footerScroll();
    }

    if (overScrollDistance < -mOverScrollTrigger && isOnTop()) {
      mOverScrollListener.headerScroll();
    }

  }

  public void setQuickScroll(boolean isEnable) {
    mIsBanQuickScroll = !isEnable;
  }

  @Override
  public void computeScroll() {
    if (!mIsBanQuickScroll) {
      super.computeScroll();
    }
  }


  public int getScrollHeight() {
    return mContentLayout.getHeight() - getHeight();
  }

  @Override
  public void fling(int velocityY) {
    inertanceY = velocityY / 100;
    super.fling(velocityY);
  }



  public interface OverScrollListener {


    void headerScroll();


    void footerScroll();

  }


  public interface OverScrollTinyListener {


    void scrollDistance(int tinyDistance, int totalDistance);


    void scrollLoosen();
  }


  public interface OnScrollListener {
    void onScroll(int l, int t, int oldl, int oldt);
  }
}
