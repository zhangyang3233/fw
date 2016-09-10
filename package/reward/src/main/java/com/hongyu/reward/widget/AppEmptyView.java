package com.hongyu.reward.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fw.zycoder.utils.StringUtil;
import com.fw.zycoder.utils.ViewUtils;
import com.hongyu.reward.R;

/**
 * App统一的 EmptyView
 *
 * 使用方法
 * 1.在 xml 中 include app_empty_view 并设置高宽。例子在 sample_network_fragment.xml
 *
 * 设置 OnEmptyRefreshListener 后，点击 EmptyView 会消失。OnEmptyRefreshListener 会接收到回调。
 * 不设置 OnEmptyRefreshListener 点击将不会消失。
 * 
 * 2.强烈建议配合 EmptyTipsUtil 使用
 *
 */
public class AppEmptyView extends LinearLayout {


  private TextView mEmptyText;
  private ImageView mEmptyImage;

  private String mText;

  private OnEmptyRefreshListener mRefreshListener;

  public AppEmptyView(Context context) {
    super(context);
  }

  public AppEmptyView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public static AppEmptyView newInstance(ViewGroup parent) {
    return (AppEmptyView) ViewUtils.newInstance(parent, R.layout.app_empty_view);
  }

  public static AppEmptyView newInstance(Context context) {
    return (AppEmptyView) ViewUtils.newInstance(context, R.layout.app_empty_view);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    initView();
    initText();
    initListener();
  }

  private void initView() {
    mEmptyText = (TextView) findViewById(R.id.empty_view_text);
    mEmptyImage = (ImageView) findViewById(R.id.empty_view_image);
  }

  private void initText() {
    mText = StringUtil.getString(R.string.base_default_empty_message);
  }

  private void initListener() {
    this.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mRefreshListener != null) {
          if (getVisibility() == VISIBLE) {
            dismiss();
            mRefreshListener.onRefresh();
          }
        }
      }
    });
  }

  public void setOnRefreshListener(OnEmptyRefreshListener refreshListener) {
    mRefreshListener = refreshListener;
  }

  public void setEmptyText(String text) {
    mText = text;
    mEmptyText.setText(mText);
  }

  public void setEmptyImage(int resId) {
    mEmptyImage.setImageResource(resId);
  }

  public void show() {
    if (TextUtils.isEmpty(mText)) {
      mEmptyText.setVisibility(GONE);
    }
    setVisibility(View.VISIBLE);
  }

  public void dismiss() {
    setVisibility(View.GONE);
  }


  public interface OnEmptyRefreshListener {
    void onRefresh();
  }

  public void setEmptyTextStatus(int visibility){
    mEmptyText.setVisibility(visibility);
  }


}
