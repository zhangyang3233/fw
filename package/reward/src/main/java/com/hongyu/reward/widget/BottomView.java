package com.hongyu.reward.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fw.zycoder.utils.ViewUtils;
import com.hongyu.reward.R;


public class BottomView extends RelativeLayout {

  private TextView title;
  private ImageView dotImg;
  private ImageView imageView;

  private int mDrawable;
  private String mTitle;
  private int mPaddingBetween;

  public BottomView(Context context) {
    this(context, null);
  }

  public BottomView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BottomView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    LayoutInflater.from(context).inflate(R.layout.bottom_view, this);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomView);
    mDrawable = a.getResourceId(R.styleable.BottomView_bottom_src, 0);
    mTitle = a.getString(R.styleable.BottomView_bottom_text);
    int defaultPadding =
        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0F, context.getResources()
            .getDisplayMetrics());
    mPaddingBetween =
        a.getDimensionPixelOffset(R.styleable.BottomView_bottom_paddingBetween, defaultPadding);
    a.recycle();

  }

  public static TopImageView newInstance(Context context) {
    return (TopImageView) ViewUtils.newInstance(context, R.layout.bottom_view);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    title = (TextView) findViewById(R.id.bottom_text);
    dotImg = (ImageView) findViewById(R.id.bottom_img);
    imageView = (ImageView) findViewById(R.id.top_img);
    imageView.setImageResource(mDrawable);
    title.setText(mTitle);

  }

  @Override
  public void setPressed(boolean pressed) {
    super.setPressed(pressed);
    title.setPressed(pressed);
    imageView.setPressed(pressed);
  }

  @Override
  public void setSelected(boolean selected) {
    super.setSelected(selected);
    title.setSelected(selected);
    imageView.setSelected(selected);
  }

  public TextView getTitle() {
    return title;
  }

  public ImageView getImageView() {
    return imageView;
  }

  public ImageView getDotImg() {
    return dotImg;
  }

}
