package com.hongyu.reward.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fw.zycoder.utils.ViewUtils;
import com.hongyu.reward.R;


public class TopImageView extends FrameLayout {
  private static final int DEFAULT_DRAWABLE_WIDTH = 0;
  private static final int DEFAULT_DRAWABLE_HEIGHT = 0;
  private LinearLayout mContent;
  private NetImageView mImageView;
  private TextView mTextView;
  private int mDrawable;
  private int mDrawableWidth;
  private int mDrawableHeight;
  private String mTitle;
  private int mPaddingBetween;


  public TopImageView(Context context) {
    this(context, null);
  }

  public TopImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TopImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    LayoutInflater.from(context).inflate(R.layout.widget_top_image_view_internal, this);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextImageView);
    mDrawable = a.getResourceId(R.styleable.TextImageView_src, 0);
    mTitle = a.getString(R.styleable.TextImageView_text);
    int defaultPadding =
        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0F, context.getResources()
            .getDisplayMetrics());
    mPaddingBetween =
        a.getDimensionPixelOffset(R.styleable.TextImageView_paddingBetween, defaultPadding);

    mDrawableWidth =
        a.getDimensionPixelOffset(R.styleable.TextImageView_drawable_width, DEFAULT_DRAWABLE_WIDTH);
    mDrawableHeight =
        a.getDimensionPixelOffset(R.styleable.TextImageView_drawable_height,
            DEFAULT_DRAWABLE_HEIGHT);

    a.recycle();
  }

  public static TopImageView newInstance(Context context) {
    return (TopImageView) ViewUtils.newInstance(context, R.layout.widget_top_image_view);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    mContent = (LinearLayout) findViewById(R.id.ll_content);
    mImageView = (NetImageView) findViewById(R.id.iv_image);
    mTextView = (TextView) findViewById(R.id.tv_text);

    mImageView.setBackgroundResource(mDrawable);
    ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
    if (mDrawableWidth > 0) {
      layoutParams.width = mDrawableWidth;
    }
    if (mDrawableHeight > 0) {
      layoutParams.height = mDrawableHeight;
    }

    mTextView.setText(mTitle);

    mContent.removeView(mTextView);

    LinearLayout.LayoutParams lp =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    lp.topMargin = mPaddingBetween;
    lp.gravity = Gravity.CENTER_HORIZONTAL;
    mContent.addView(mTextView, lp);
  }

  @Override
  public void setPressed(boolean pressed) {
    super.setPressed(pressed);
    mImageView.setPressed(pressed);
    mTextView.setPressed(pressed);
  }

  @Override
  public void setSelected(boolean selected) {
    super.setSelected(selected);
    mImageView.setSelected(selected);
    mTextView.setSelected(selected);
  }

  public TopImageView setIcon(int resId) {
    mImageView.setImageResource(resId);
    return this;
  }

  public TopImageView setText(int resId) {
    mTextView.setText(resId);
    return this;
  }

  public TopImageView setText(CharSequence text) {
    mTextView.setText(text);
    return this;
  }

  public NetImageView getImageView() {
    return mImageView;
  }

  public TextView getTextView() {
    return mTextView;
  }
}
