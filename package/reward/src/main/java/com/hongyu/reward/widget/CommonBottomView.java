package com.hongyu.reward.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fw.zycoder.utils.ViewUtils;
import com.hongyu.reward.R;

public class CommonBottomView extends CoverViewContainer {

  private FrameLayout mBottomView;

  public CommonBottomView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public CommonBottomView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CommonBottomView(Context context) {
    super(context);
  }

  public static CommonBottomView newInstance(ViewGroup parent) {
    return (CommonBottomView) ViewUtils.newInstance(parent, R.layout.common_bottom_view);
  }

  public static CommonBottomView newInstance(Context context) {
    return (CommonBottomView) ViewUtils.newInstance(context, R.layout.common_bottom_view);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    mBottomView = (FrameLayout) findViewById(R.id.bottom_view);
  }

  public FrameLayout getBottomView() {
    return mBottomView;
  }
}
