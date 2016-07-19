package com.fw.zycoder.appbase.activity.title;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fw.zycoder.appbase.R;
import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.StringUtil;
import com.fw.zycoder.utils.ViewUtils;
import com.zycoder.sliding.utils.ActivityUtils;


/**
 * 开发者请尽量复用此view 如需修改此view的属性 可以重写 {@link com.fw.zycoder.appbase.activity.BaseTitleActivity
 * getMyTitleContainer方法}
 */
public class CommonTitleView extends RelativeLayout implements TitleContainer {

  protected TextView mTitle;
  protected ImageView mBackImageView;
  private LinearLayout mLeftLayout;
  private LinearLayout mRightLayout;
  private RightViewChangedListener mRightViewChangedListener;


  public CommonTitleView(Context context) {
    super(context);
  }

  public CommonTitleView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public static CommonTitleView newInstance(ViewGroup parent) {
    return (CommonTitleView) ViewUtils.newInstance(parent, R.layout.common_title_view_layout);
  }

  public static CommonTitleView newInstance(Context context) {
    return (CommonTitleView) ViewUtils.newInstance(context, R.layout.common_title_view_layout);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    mTitle = (TextView) findViewById(R.id.common_title_view_layout_title);
    mBackImageView = (ImageView) findViewById(R.id.common_title_view_layout_left_arrow);
    mLeftLayout = (LinearLayout) findViewById(R.id.common_title_view_layout_left_container);
    mRightLayout = (LinearLayout) findViewById(R.id.common_title_view_layout_right_container);
    initListener();
    setTitleStyle();
  }

  private void initListener() {
    mLeftLayout.setOnClickListener(new OnClickListener() {
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
  }

  @Override
  public void setTitle(CharSequence title) {
    mTitle.setText(title);
  }

  @Override
  public void setTitle(int resId) {
    mTitle.setText(StringUtil.getString(resId));
  }


  @Override
  public void setLeftView(View v, ViewGroup.LayoutParams layoutParams) {
    if (v == null) {
      return;
    }
    mLeftLayout.removeAllViews();
    if (layoutParams == null) {
      mLeftLayout.addView(v);
    } else {
      mLeftLayout.addView(v, layoutParams);
    }
  }

  @Override
  public void setRightView(View v, ViewGroup.LayoutParams layoutParams) {
    if (v == null) {
      return;
    }
    mRightLayout.removeAllViews();
    if (layoutParams == null) {
      mRightLayout.addView(v);
    } else {
      mRightLayout.addView(v, layoutParams);
    }
    if (mRightViewChangedListener != null) {
      mRightViewChangedListener.onRightViewChangedListener(v);
    }
  }

  public TextView getTitle() {
    return mTitle;
  }

  public ImageView getBackImageView() {
    return mBackImageView;
  }

  protected void setTitleStyle() {
    setBackgroundColor(GlobalConfig.getAppContext().getResources()
        .getColor(R.color.appbase_common_blue_title_color));
    if (mTitle != null) {
      mTitle.setTextColor(Color.WHITE);
    }
    if (mBackImageView != null) {
      mBackImageView.setImageResource(R.mipmap.home_base_title_back);
    }
  }

  public interface RightViewChangedListener {
    void onRightViewChangedListener(View view);
  }

  public void setOnRightViewChangedListener(RightViewChangedListener listener) {
    mRightViewChangedListener = listener;
  }
}
