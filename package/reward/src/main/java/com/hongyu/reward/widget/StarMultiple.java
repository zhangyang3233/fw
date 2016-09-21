package com.hongyu.reward.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongyu.reward.R;

/**
 * 
 * widget - star multiple - 多行星星
 *
 * =======================================
 * Copyright 2016-2017
 * =======================================
 *
 * @since 2016-7-11 上午8:46:58
 * @author centos
 *
 */
public class StarMultiple extends RelativeLayout {
  private Context mContext;
  private RelativeLayout mView;
  private View mLine1;
  private View mLine2;
  private View mLine3;
  private View mLine4;
  private View mLine5;
  private TextView mNum1;
  private TextView mNum5;
  private TextView mNum4;
  private TextView mNum3;
  private TextView mNum2;

  public StarMultiple(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    mContext = context;
    initView();
  }

  public StarMultiple(Context context, AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
    initView();
  }

  public StarMultiple(Context context) {
    super(context);
    mContext = context;
    initView();
  }

  /**
   * 初始化页面布局
   * 
   * @author Jfomt
   * @since 2014年10月1日 下午10:58:59
   * @version 1.0
   */
  private void initView() {
    // mView = (RelativeLayout) View.inflate(mContext, R.lauout.widget_edittext, null);
    // this.add(mView);
    mView = (RelativeLayout) View.inflate(mContext, R.layout.widget_star_multiple, this);
    mLine5 = mView.findViewById(R.id.line5);
    mLine4 = mView.findViewById(R.id.line4);
    mLine3 = mView.findViewById(R.id.line3);
    mLine2 = mView.findViewById(R.id.line2);
    mLine1 = mView.findViewById(R.id.line1);
    mNum1 = (TextView) mView.findViewById(R.id.num1);
    mNum2 = (TextView) mView.findViewById(R.id.num2);
    mNum3 = (TextView) mView.findViewById(R.id.num3);
    mNum4 = (TextView) mView.findViewById(R.id.num4);
    mNum5 = (TextView) mView.findViewById(R.id.num5);

  }

  public void setData(int[] data) {
    mNum1.setText("（" + data[0] + "）");
    mNum2.setText("（" + data[1] + "）");
    mNum3.setText("（" + data[2] + "）");
    mNum4.setText("（" + data[3] + "）");
    mNum5.setText("（" + data[4] + "）");
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    return super.dispatchTouchEvent(ev);

  }
}
