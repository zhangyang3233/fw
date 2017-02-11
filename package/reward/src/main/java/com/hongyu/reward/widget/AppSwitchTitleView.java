package com.hongyu.reward.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.fw.zycoder.utils.ViewUtils;
import com.hongyu.reward.R;
import com.hongyu.reward.utils.ActivityUtils;

/**
 * Created by zhangyang131 on 16/9/20.
 */
public class AppSwitchTitleView extends RelativeLayout
    implements
      TitleContainer,
      RadioGroup.OnCheckedChangeListener {
  OnCheckedChangedListener mOnCheckedChangedListener;
  RadioGroup order_title;
  RadioButton send;
  RadioButton receive;
  LinearLayout mLeftContainer;
  LinearLayout mRightContainer;

  public AppSwitchTitleView(Context context) {
    super(context);
  }

  public AppSwitchTitleView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public static AppSwitchTitleView newInstance(ViewGroup parent) {
    return (AppSwitchTitleView) ViewUtils.newInstance(parent,
        R.layout.reward_switch_title_view_layout);
  }

  public static AppSwitchTitleView newInstance(Context context) {
    return (AppSwitchTitleView) ViewUtils.newInstance(context,
        R.layout.reward_switch_title_view_layout);
  }

  public OnCheckedChangedListener getOnCheckedChangedListener() {
    return mOnCheckedChangedListener;
  }

  public void setOnCheckedChangedListener(OnCheckedChangedListener mOnCheckedChangedListener) {
    this.mOnCheckedChangedListener = mOnCheckedChangedListener;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    initView();
    initListener();
  }

  private void initView() {
    order_title = (RadioGroup) findViewById(R.id.order_title);
    send = (RadioButton) findViewById(R.id.send);
    receive = (RadioButton) findViewById(R.id.receive);
    mLeftContainer =
        (LinearLayout) findViewById(R.id.common_title_view_layout_left_container);
    mRightContainer =
        (LinearLayout) findViewById(R.id.common_title_view_layout_right_container);
  }

  @Override
  public void setTitle(CharSequence title) {

  }

  @Override
  public void setTitle(int resId) {

  }

  @Override
  public void setLeftView(View v, ViewGroup.LayoutParams layoutParams) {
    if (v == null) {
      return;
    }
    mLeftContainer.removeAllViews();
    if (layoutParams == null) {
      mLeftContainer.addView(v);
    } else {
      mLeftContainer.addView(v, layoutParams);
    }
  }

  @Override
  public void setRightView(View v, ViewGroup.LayoutParams layoutParams) {
    if (v == null) {
      return;
    }
    mRightContainer.removeAllViews();
    if (layoutParams == null) {
      mRightContainer.addView(v);
    } else {
      mRightContainer.addView(v, layoutParams);
    }
  }

  private void initListener() {
    mLeftContainer.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Activity activity = ActivityUtils.findActivity(v);
        if (activity != null) {
          try {
            activity.onBackPressed();
          } catch (Exception e) {
            activity.finish();
          }
        }
      }
    });

    order_title.setOnCheckedChangeListener(this);
  }

  private void switchBtn(int id) {
    switch (id) {
      case R.id.send:
        if (mOnCheckedChangedListener != null) {
          mOnCheckedChangedListener.checkChanged(OnCheckedChangedListener.LEFT);
        }
        send.setTextColor(getResources().getColor(R.color.app_main_title_text_color));
        send.setBackgroundResource(R.drawable.cell_yellow_round_left);
        receive.setTextColor(getResources().getColor(R.color.text_gray_light));
        receive.setBackgroundResource(R.drawable.cell_tran_round_right);
        break;
      case R.id.receive:
        if (mOnCheckedChangedListener != null) {
          mOnCheckedChangedListener.checkChanged(OnCheckedChangedListener.RIGHT);
        }
        send.setTextColor(getResources().getColor(R.color.text_gray_light));
        send.setBackgroundResource(R.drawable.cell_tran_round_left);
        receive.setTextColor(getResources().getColor(R.color.app_main_title_text_color));
        receive.setBackgroundResource(R.drawable.cell_yellow_round_right);
        break;

    }
  }

  @Override
  public void onCheckedChanged(RadioGroup group, int checkedId) {
    int id = group.getCheckedRadioButtonId();
    switchBtn(id);
  }

  public interface OnCheckedChangedListener {
    int LEFT = 1;
    int RIGHT = 2;

    void checkChanged(int witch);
  }
}
