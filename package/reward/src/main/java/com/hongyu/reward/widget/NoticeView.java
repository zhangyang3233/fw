package com.hongyu.reward.widget;

import android.content.Context;
import android.text.TextUtils;
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
  private View layout1;
  private View layout2;
  private TextView notice_tip1;
  private TextView notice_tip2;
  String publicOrderId;
  String receiveOrderId;
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
    layout1 =  view.findViewById(R.id.layout1);
    layout2 =  view.findViewById(R.id.layout2);
    notice_tip1 = (TextView) view.findViewById(R.id.notice_tip1);
    notice_tip2 = (TextView) view.findViewById(R.id.notice_tip2);
    notice_tip1.setText(R.string.notice_publish);
    notice_tip2.setText(R.string.notice_receive);
    layout1.setOnClickListener(this);
    layout2.setOnClickListener(this);
    RelativeLayout.LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    addView(view,params);
    AccountManager.getInstance().addLogoutListener(new LogoutListener() {
      @Override
      public void onLogout() {
        hide();
      }
    });
  }


  public void show(RefreshOrderManager.Prog prog) {
    try{
      setVisibility(View.VISIBLE);
      this.isPublish = prog.isPublish();
      if(isPublish){
        publicOrderId = prog.getOrderId();
        if(TextUtils.isEmpty(publicOrderId)){
          layout1.setVisibility(View.GONE);
        }else{
          layout1.setVisibility(View.VISIBLE);
        }
      }else{
        receiveOrderId = prog.getOrderId();
        if(TextUtils.isEmpty(receiveOrderId)){
          layout2.setVisibility(View.GONE);
        }else{
          layout2.setVisibility(View.VISIBLE);
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void hide() {
    this.publicOrderId = null;
    this.receiveOrderId = null;
    layout1.setVisibility(View.GONE);
    layout2.setVisibility(View.GONE);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.layout1:
        OrderClickUtil.orderOnClick(mContext, publicOrderId);
        break;
      case R.id.layout2:
        OrderClickUtil.orderOnClick(mContext, receiveOrderId);
        break;
    }

  }
}
