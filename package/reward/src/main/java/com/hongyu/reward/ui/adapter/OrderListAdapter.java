package com.hongyu.reward.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.ui.activity.order.PublishFinishedCommentActivity;
import com.hongyu.reward.ui.activity.order.ReceiveOrderFinishedActivity;
import com.hongyu.reward.ui.activity.order.ReceiveWaitActivity;
import com.hongyu.reward.ui.activity.order.RewardPublishWaitActivity;
import com.hongyu.reward.ui.activity.order.RewardStartActivity;
import com.hongyu.reward.ui.activity.order.SelectPersonActivity;
import com.hongyu.reward.utils.StatusUtil;

/**
 * 
 * adapter - order - 订单列表适配器
 *
 * =======================================
 * Copyright 2016-2017
 * =======================================
 *
 * @since 2016-7-9 上午11:48:04
 * @author centos
 *
 */
public class OrderListAdapter extends DataAdapter<OrderModel> {
  public OnOrderItemClickListener orderItemClickListener;
  private Context context;
  private int isme = 0; // 0我发出的 1我收到的

  public OrderListAdapter(Context context, int type) {
    this.context = context;
    this.isme = type;
    initOrderItemClickListener();
  }

  private void initOrderItemClickListener() {
    orderItemClickListener = new OrderClickListener(context);
  }

  public static void gelleryToPage(Context context, OrderModel model, int isme) {
    switch (model.getStatus()) {
      case OrderModel.STATUS_FINISHED: // 已完成
        if(isme == OrderModel.IS_ME){
          PublishFinishedCommentActivity.launch(context, model.getOrder_id());
        }else{
          ReceiveOrderFinishedActivity.launch(context, model.getOrder_id());
        }
        break;
      case OrderModel.STATUS_PENDING_RECEIVE: // 待接单
        RewardPublishWaitActivity.launch(context, model.getOrder_id(), model.getShop_name(),
            model.getImg());
        break;
      case OrderModel.STATUS_PENDING_PAY: // 待付款
        if (isme == OrderModel.NOT_ME) {
          ReceiveWaitActivity.launch(context, model.getOrder_id());
        }else{
          RewardStartActivity.launch(context, model.getOrder_id(),model.getShop_name(), model.getPrice());
        }
        break;
      case OrderModel.STATUS_PENDING_COMMENT: // 待评论
        if(isme == OrderModel.IS_ME){ // 0我发出的 1我收到的
          PublishFinishedCommentActivity.launch(context, model.getOrder_id());
        }else{
          ReceiveOrderFinishedActivity.launch(context, model.getOrder_id());
        }
        break;
      case OrderModel.STATUS_RECEIVED: // 已经领取
        SelectPersonActivity.launch(context, model.getOrder_id(), model.getShop_name(),
            model.getImg());
        break;
    }
  }


  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup arg2) {
    Holder holder;
    if (convertView == null) {
      holder = new Holder();
      convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_order, null);
      holder.status = (TextView) convertView.findViewById(R.id.status);
      holder.time = (TextView) convertView.findViewById(R.id.time);
      holder.price = (TextView) convertView.findViewById(R.id.price);
      holder.name = (TextView) convertView.findViewById(R.id.shop_name);
      holder.appointment = (TextView) convertView.findViewById(R.id.btn_appointment);
      convertView.setTag(holder);
    } else {
      holder = (Holder) convertView.getTag();
    }

    final OrderModel model = getItem(position);
    if (model == null) return null;

    holder.status.setText(StatusUtil.getMsgByStatus(model.getStatus()));
    holder.status.setTextColor(StatusUtil.getColorByStatus(model.getStatus()));

    holder.time.setText(model.getDate());
    holder.name.setText(model.getShop_name());
    holder.price.setText("￥" + model.getPrice());

    if (OrderModel.APPOINTMENT.equals(model.getType())) {
      holder.appointment.setText("预约");
    } else {
      holder.appointment.setText("即时");
    }

    convertView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (orderItemClickListener != null) {
          orderItemClickListener.onClick(model, isme);
        }
      }
    });
    return convertView;
  }

  public interface OnOrderItemClickListener {
    void onClick(OrderModel model, int type);
  }

  private class OrderClickListener implements OnOrderItemClickListener {
    private Context mContext;

    public OrderClickListener(Context context) {
      mContext = context;
    }

    @Override
    public void onClick(OrderModel model, int type) {
      OrderListAdapter.gelleryToPage(context, model, type);
    }

  }

  private class Holder {
    private TextView name;
    private TextView status;
    private TextView time;
    private TextView appointment;
    private TextView price;
  }

}
