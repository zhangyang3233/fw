package com.hongyu.reward.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fw.zycoder.utils.ViewUtils;
import com.hongyu.reward.R;
import com.hongyu.reward.interfaces.LogoutListener;
import com.hongyu.reward.interfaces.OrderClickUtil;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.manager.RefreshOrderManager;

/**
 * Created by zhangyang131 on 16/10/5.
 */
public class NoticeView extends RelativeLayout implements View.OnClickListener {
  private Context mContext;
  private TextView notice_tip;
  String orderId;
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
    AccountManager.getInstance().addLogoutListener(new LogoutListener() {
      @Override
      public void onLogout() {
        hide();
      }
    });
  }


  public void show(RefreshOrderManager.Prog prog) {
    setVisibility(View.VISIBLE);
    this.orderId = prog.getOrderId();
    this.isPublish = prog.isPublish();
    if (isPublish) {
      notice_tip.setText(R.string.notice_publish);
    } else {
      notice_tip.setText(R.string.notice_receive);
    }
  }

  public void hide() {
    setVisibility(View.GONE);
    this.orderId = null;
  }

  @Override
  public void onClick(View v) {
    OrderClickUtil.orderOnClick(mContext, orderId);
  }
}
