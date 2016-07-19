package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Description:
 *
 * @date 15/8/7 16:55
 */
public class WhaleSpringAnimationView extends View {
  private Paint mPaint;

  private int mHeight;
  private int mWidth;

  private int defaultBottomOffset = 4;
  private int mBottomOffset;

  private int defaultBottomHeight = 7;
  private int mBottomHeight;

  private int defaultSpringWidth = 4;
  private int mSpringWidth;

  private float mPullY = 0;

  private Bitmap bitmapWhale;
  private Bitmap bitmapSpring;
  private Bitmap bitmapWave;
  private float marginRight;
  private float marginTop;
  private float width;
  private AnimationState mAnimationState = AnimationState.NORMAL;

  private float mLoopLocation = 0;

  private ValueAnimator valueRefreshAnimator = null;


  enum AnimationState {
    NORMAL, RELEASE, REFRESHING
  }

  public WhaleSpringAnimationView(Context context) {
    this(context, null);
  }

  public WhaleSpringAnimationView(Context context, AttributeSet attrs) {
    super(context, attrs);
    mBottomOffset =
        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultBottomOffset, context
            .getResources().getDisplayMetrics());
    mBottomHeight =
        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultBottomHeight, context
            .getResources().getDisplayMetrics());
    mSpringWidth =
        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultSpringWidth, context
            .getResources().getDisplayMetrics());
    initView();
    initBitmap();
    initAnimation();
  }

  private void initBitmap() {
    bitmapWhale = ((BitmapDrawable) getResources().getDrawable(R.drawable.humpback)).getBitmap();
    bitmapSpring = ((BitmapDrawable) getResources().getDrawable(R.drawable.humptop)).getBitmap();
    bitmapWave = ((BitmapDrawable) getResources().getDrawable(R.drawable.humpwave)).getBitmap();

    marginRight = (float) (bitmapWhale.getWidth() * 0.395);
    marginTop = (float) (bitmapWhale.getWidth() * 0.284);
    width = (float) (mSpringWidth * 0.84);
  }

  private void initView() {
    // 初始化画笔
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setColor(Color.rgb(194, 194, 194));
  }

  private void initAnimation() {
    valueRefreshAnimator = ObjectAnimator.ofFloat(0.0f, 1.0f);
    valueRefreshAnimator.setDuration(500);
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
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    mWidth = MeasureSpec.getSize(widthMeasureSpec);
    mHeight = MeasureSpec.getSize(heightMeasureSpec);
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    mPullY = Math.max(mPullY - 120, 0);
    switch (mAnimationState) {
      case NORMAL:
        drawPullView(canvas);
        break;
      case RELEASE:
        drawRelease(canvas);
        break;
      case REFRESHING:
        drawRefresh(canvas, mLoopLocation);
        break;
    }


  }

  private void drawPullView(Canvas canvas) {
    float offset = (float) (mBottomOffset * 0.09);
    canvas.drawBitmap(bitmapSpring,
        mWidth / 2 + bitmapWhale.getWidth() / 2 - marginRight + width / 2 - bitmapSpring.getWidth()
            / 2 - offset,
        0,
        mPaint);

    canvas.drawRect(mWidth / 2 + bitmapWhale.getWidth() / 2 - marginRight,
        bitmapSpring.getHeight(),
        mWidth / 2 + bitmapWhale.getWidth() / 2 - marginRight + width,
        mPullY + bitmapSpring.getHeight(), mPaint);

    canvas.drawBitmap(bitmapWhale, mWidth / 2 - bitmapWhale.getWidth() / 2, mPullY - marginTop,
        mPaint);

  }


  private void drawRelease(Canvas canvas) {}

  private void drawRefresh(Canvas canvas, float loopLocation) {
    float offsetX = loopLocation * bitmapWave.getWidth();
    int size = getWidth() / bitmapWave.getWidth() + 2;

    for (int i = 0; i < size; i++) {
      canvas.drawBitmap(bitmapWave,
          -offsetX + bitmapWave.getWidth() * i,
          mHeight / 2 - bitmapWave.getHeight() / 2,
          mPaint);
    }
  }

  public void startRefreshingAnimation() {
    mAnimationState = AnimationState.REFRESHING;
    valueRefreshAnimator.start();
  }

  public void reset(boolean isResetDropState) {
    mAnimationState = AnimationState.NORMAL;
    valueRefreshAnimator.cancel();
    initAnimation();
  }

  public void setPullY(float pullY) {
    mPullY = Math.min(pullY, 150);
    invalidate();
  }
}
