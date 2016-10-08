package com.hongyu.reward.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fw.zycoder.utils.ViewUtils;
import com.hongyu.reward.R;
import com.hongyu.reward.ui.activity.RewardPublishWaitActivity;
import com.hongyu.reward.ui.activity.RewardStartActivity;

/**
 * Created by zhangyang131 on 16/10/5.
 */
public class NoticeView extends RelativeLayout implements View.OnClickListener {
  private Context mContext;
  private TextView notice_tip;
  String publishedOrderId;
  boolean isPublish;

  public NoticeView(Context context) {
    super(context);
    mContext = context;
    initView();
  }

  public NoticeView(Context context, AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
    initView();
  }

  private void initView() {
    View view = ViewUtils.newInstance(mContext, R.layout.notice_layout);
    notice_tip = (TextView) view.findViewById(R.id.notice_tip);
    view.setOnClickListener(this);
    addView(view);
  }


  public void show(String publishedOrderId, boolean isPublish) {
    setVisibility(View.VISIBLE);
    this.publishedOrderId = publishedOrderId;
    this.isPublish = isPublish;
    if (isPublish) {
      notice_tip.setText(R.string.notice_publish);
    } else {
      notice_tip.setText(R.string.notice_receive);
    }
  }

  public void hide() {
    setVisibility(View.GONE);
    this.publishedOrderId = null;
  }

  @Override
  public void onClick(View v) {
    if (isPublish) { // 发布的任务
      RewardPublishWaitActivity.launch(mContext, publishedOrderId, null, null);
    } else {// 接受任务
      RewardStartActivity.launch(mContext, publishedOrderId, null, null);
    }
  }
}
