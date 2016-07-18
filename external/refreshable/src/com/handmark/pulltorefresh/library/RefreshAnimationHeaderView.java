package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Description:下拉刷新动画
 *
 * @date 15/7/2 10:57
 */
public class RefreshAnimationHeaderView extends View {
  private final static int DEFAULT_CIRCLE_RADIU = 13;
  private final static int DEFAULT_MAX_CIRCLE_RADIU = 20;
  private final static int DEFAULT_MAX_HEIGHT = 300;
  private final static int DEFAULT_MAX_LOCATION = 17;
  private final static int DEFAULT_START_ANGLE = -90;

  // 两个圆球的最大半径和最小半径
  private int mCircleRadiu = DEFAULT_CIRCLE_RADIU;
  private int mMaxCircleRadiu = DEFAULT_MAX_CIRCLE_RADIU;
  // 最大下拉高度
  private int mMaxHeight = DEFAULT_MAX_HEIGHT;
  // 当前下拉的高度
  private float mCurrentPullingHeight = 0.0f;
  // 动画状态
  private AnimationState mAnimationState = AnimationState.NORMAL;

  private ValueAnimator valueRefreshAnimator = null;
  private ValueAnimator valueReleaseAnimator = null;

  private boolean isFirstDrap = true;
  private boolean isFirstRelease = true;
  private boolean isFirstReFresh = true;

  private Paint mPaint;

  private int mHeight;
  private int mWidth;

  // 移动动画
  private int mLocation = 0;
  private float mLoopLocation = 0;
  private int mMaxLocation = DEFAULT_MAX_LOCATION;
  private int mStartAngle = DEFAULT_START_ANGLE;

  // 中心点
  private float mCenterX;
  private float mCenterY;

  // 两个圆球的颜色
  private int mRightBallColor = Color.argb(255, 37, 119, 229);
  private int mLeftBallColor = Color.argb(255, 242, 70, 154);

  // 普通下拉，下拉释放，动画循环
  enum AnimationState {
    NORMAL, RELEASE, REFRESHING
  }

  public RefreshAnimationHeaderView(Context context) {
    this(context, null);
  }

  public RefreshAnimationHeaderView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
    initAnimation();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    mWidth = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    mHeight = MeasureSpec.getSize(heightMeasureSpec);

    mCenterX = mWidth / 2.0f;
    mCenterY = mMaxCircleRadiu;
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  private void initView() {
    // 初始化参数
    // 初始化画笔
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setAntiAlias(true);

  }

  private void initAnimation() {
    // 移动动画
    valueReleaseAnimator = ObjectAnimator.ofInt(0, mMaxLocation);
    valueReleaseAnimator.setDuration(800);
    valueReleaseAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    valueReleaseAnimator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationStart(Animator animation) {
        super.onAnimationStart(animation);
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        isFirstRelease = false;
        if (mAnimationState == AnimationState.RELEASE) {// check state
          valueReleaseAnimator.cancel();
          mAnimationState = AnimationState.REFRESHING;
          valueRefreshAnimator.start();
          invalidate();
        }
      }
    });
    valueReleaseAnimator.addUpdateListener(updateReleaseListener);


    // 旋转动画
    valueRefreshAnimator = ObjectAnimator.ofFloat(0, mMaxLocation * 2);
    valueRefreshAnimator.setDuration(1000);
    valueRefreshAnimator.setRepeatCount(Animation.INFINITE);
    valueRefreshAnimator.setRepeatMode(ValueAnimator.RESTART);
    valueRefreshAnimator.setInterpolator(new LinearInterpolator());
    valueRefreshAnimator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationStart(Animator animation) {
        super.onAnimationStart(animation);
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        if (mAnimationState == AnimationState.REFRESHING) {// check state
          valueRefreshAnimator.start();
        }
      }
    });
    valueRefreshAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        if (mAnimationState == AnimationState.REFRESHING) {
          mLoopLocation = (Float) animation.getAnimatedValue();
        }
        postInvalidate();
      }
    });
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    switch (mAnimationState) {
      case NORMAL:
        drawNormalView(canvas);
        break;
      case RELEASE:
        // 移动动画
        drawLocation(canvas, mLocation);
        break;
      case REFRESHING:
        // 旋转动画
        drawRotate(canvas, mLoopLocation);
        break;

    }
  }

  private void drawRotate(Canvas canvas, float loopLocation) {
    if (loopLocation > mMaxLocation) {
      mPaint.setStyle(Paint.Style.FILL);
      mPaint.setStrokeWidth(mMaxCircleRadiu);
      mPaint.setColor(mRightBallColor);

      // 缩放，右移动
      canvas.drawCircle(mCenterX - mMaxLocation + (loopLocation - mMaxLocation) * 2, mCenterY,
          mCircleRadiu, mPaint);

      mPaint.setStyle(Paint.Style.FILL);
      mPaint.setStrokeWidth(mCircleRadiu);
      mPaint.setColor(mLeftBallColor);
      canvas.drawCircle(mCenterX + mMaxLocation - (loopLocation - mMaxLocation) * 2, mCenterY,
          mCircleRadiu, mPaint);
    } else {
      mPaint.setStyle(Paint.Style.FILL);
      mPaint.setStrokeWidth(mCircleRadiu);
      mPaint.setColor(mLeftBallColor);
      canvas.drawCircle(mCenterX - mMaxLocation + loopLocation * 2, mCenterY, mCircleRadiu, mPaint);

      mPaint.setStyle(Paint.Style.FILL);
      mPaint.setStrokeWidth(mMaxCircleRadiu);
      mPaint.setColor(mRightBallColor);

      // 缩放，右移动
      canvas.drawCircle(mCenterX + mMaxLocation - loopLocation * 2, mCenterY, mCircleRadiu, mPaint);
    }
  }

  // 大球变小与小球一样大,同时中间的小球向左移动
  private void drawLocation(Canvas canvas, int location) {
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setStrokeWidth(mMaxCircleRadiu);
    mPaint.setColor(mRightBallColor);

    // 缩放，右移动
    canvas.drawCircle(mCenterX + location, mCenterY, mMaxCircleRadiu
        - (mMaxCircleRadiu - mCircleRadiu) * ((float) location / mMaxLocation), mPaint);

    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setStrokeWidth(mCircleRadiu);
    mPaint.setColor(mLeftBallColor);
    canvas.drawCircle(mCenterX - location, mCenterY, mCircleRadiu, mPaint);
  }

  private void drawNormalView(Canvas canvas) {
    canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
        | Paint.FILTER_BITMAP_FLAG));
    // View的中心点
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setStrokeWidth(mMaxCircleRadiu);
    mPaint.setColor(mRightBallColor);
    canvas.drawArc(new RectF(mCenterX - mMaxCircleRadiu, 0,
        mCenterX + mMaxCircleRadiu, mCenterY + mMaxCircleRadiu), mStartAngle,
        ((float) mCurrentPullingHeight / mMaxHeight) * 360, true, mPaint);

    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setStrokeWidth(mCircleRadiu);
    mPaint.setColor(mLeftBallColor);
    canvas.drawCircle(mCenterX, mCenterY, mCircleRadiu, mPaint);
  }

  // 释放后还有一段动画(大球变小与小球一样大,同时中间的小球向左移动)
  public void startReleaseAnimation() {
    if (isFirstRelease) {
      mAnimationState = AnimationState.RELEASE;
      valueReleaseAnimator.start();
    }
  }

  // 循环动画（两个小球沿着Z轴转圈）
  public void startRefreshingAnimation() {
    if (isFirstRelease) {
      mAnimationState = AnimationState.RELEASE;
      valueReleaseAnimator.start();
    } else {
      if (isFirstReFresh) {
        isFirstReFresh = false;
      } else {
        if (!valueRefreshAnimator.isRunning()) {
          mAnimationState = AnimationState.REFRESHING;
          valueRefreshAnimator.start();
        }
      }
    }
  }

  // 更新当前下拉高度
  public void setCurrentPullingHeight(float currentPullingHeight) {
    mCurrentPullingHeight = currentPullingHeight;
    if (mCurrentPullingHeight == mMaxHeight) {
      if (isFirstRelease) {
        mAnimationState = AnimationState.RELEASE;
        valueReleaseAnimator.start();
        isFirstRelease = false;
      }
    } else if (mCurrentPullingHeight > mMaxHeight) {
      startRefreshingAnimation();
    }
    invalidate();
  }

  // 动画监听器
  ValueAnimator.AnimatorUpdateListener updateReleaseListener =
      new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
          if (mAnimationState == AnimationState.RELEASE) {
            mLocation = (Integer) animation.getAnimatedValue();
          }
          postInvalidate();
        }
      };

  // 重置状态，重新开始
  private void reset() {
    initAnimation();
    mAnimationState = AnimationState.NORMAL;
    mCurrentPullingHeight = 0;
    isFirstDrap = true;
    isFirstRelease = true;
    invalidate();
  }

  public void reset(boolean isResetRefreshState) {
    isFirstReFresh = isResetRefreshState;
    reset();
  }

  // 设置状态，修改是否是首次下拉状态
  public void setOffState() {
    if (isFirstDrap) {
      isFirstDrap = false;
      mAnimationState = AnimationState.NORMAL;
    } else {
      mAnimationState = AnimationState.REFRESHING;
      startRefreshingAnimation();
    }
  }
}
