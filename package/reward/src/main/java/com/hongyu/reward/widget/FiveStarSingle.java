package com.hongyu.reward.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongyu.reward.R;

/**
 * 
 * widget - star multiple - 单行星星
 *
 * =======================================
 * Copyright 2016-2017
 * =======================================
 *
 * @since 2016-7-11 上午8:46:58
 * @author centos
 *
 */
public class FiveStarSingle extends LinearLayout {
  private Context mContext;
  private LinearLayout mView;
  private TextView mNum;
  private ImageView mStar5;
  private ImageView mStar4;
  private ImageView mStar3;
  private ImageView mStar2;
  private ImageView mStar1;

  @SuppressLint("NewApi")
  public FiveStarSingle(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    mContext = context;
    initView();
  }

  public FiveStarSingle(Context context, AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
    initView();
  }

  public FiveStarSingle(Context context) {
    super(context);
    mContext = context;
    initView();
  }

  private void initView() {
    mView = (LinearLayout) View.inflate(mContext, R.layout.widget_star_single, this);
    mStar1 = (ImageView) mView.findViewById(R.id.star1);
    mStar2 = (ImageView) mView.findViewById(R.id.star2);
    mStar3 = (ImageView) mView.findViewById(R.id.star3);
    mStar4 = (ImageView) mView.findViewById(R.id.star4);
    mStar5 = (ImageView) mView.findViewById(R.id.star5);
    mNum = (TextView) mView.findViewById(R.id.num);

  }

  public void setData(float score, boolean showNumText) {
    mStar1.setImageResource(R.mipmap.icon_star_orange_m);
    mStar2.setImageResource(R.mipmap.icon_star_orange_m);
    mStar3.setImageResource(R.mipmap.icon_star_orange_m);
    mStar4.setImageResource(R.mipmap.icon_star_orange_m);
    mStar5.setImageResource(R.mipmap.icon_star_orange_m);
    int tag = (int) score;
    switch (tag) {
      case 0:
        mStar1.setImageResource(R.mipmap.icon_star_gary_m);
      case 1:
        mStar2.setImageResource(R.mipmap.icon_star_gary_m);
      case 2:
        mStar3.setImageResource(R.mipmap.icon_star_gary_m);
      case 3:
        mStar4.setImageResource(R.mipmap.icon_star_gary_m);
      case 4:
        mStar5.setImageResource(R.mipmap.icon_star_gary_m);
      case 5:
        break;
    }
    mNum.setText(String.valueOf(score));
    if (showNumText) {
      mNum.setVisibility(View.VISIBLE);
    } else {
      mNum.setVisibility(View.GONE);
    }
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    return super.dispatchTouchEvent(ev);

  }
}
