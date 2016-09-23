package com.hongyu.reward.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongyu.reward.R;

/**
 * 
 * widget - comment - 自定义理由条目
 *
 * =======================================
 * Copyright 2016-2017
 * =======================================
 *
 * @since 2016-7-11 下午9:34:05
 * @author centos
 *
 */
public class ReasonView extends RelativeLayout {
  private Context mContext;
  private RelativeLayout mView;
  private TextView mTvContent;
  private ImageView mIvIcon;

  private String id;

  public ReasonView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    mContext = context;
    initView();
  }

  public ReasonView(Context context, AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
    initView();
  }

  public ReasonView(Context context) {
    super(context);
    mContext = context;
    initView();
  }

  private void initView() {
    mView = (RelativeLayout) View.inflate(mContext, R.layout.widget_reason, this);
    mTvContent = (TextView) mView.findViewById(R.id.content);
    mIvIcon = (ImageView) mView.findViewById(R.id.image);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    return super.dispatchTouchEvent(ev);

  }

  public void setContent(String id, String reason) {
    mTvContent.setText(reason);
    this.id = id;
  }

  public void setSelect(boolean isSelect) {
    if (isSelect) {
      mIvIcon.setImageResource(R.mipmap.btn_select);
    } else {
      mIvIcon.setImageResource(R.mipmap.btn_unselect);
    }
  }
}
