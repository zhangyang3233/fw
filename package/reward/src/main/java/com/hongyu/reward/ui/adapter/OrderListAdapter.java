package com.hongyu.reward.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.model.OrderModel;

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
  }

  public static void gelleryToPage(Context context, OrderModel model, int isme) {
    Intent intent = null;
    // switch (model.getStatus()) {
    // case OrderModel.STATUS_FINISHED: // 已完成
    // intent = new Intent(context, OrderFinishActivity.class);
    // intent.putExtra("order_id", model.getOrder_id());
    // context.startActivity(intent);
    // break;
    // case OrderModel.STATUS_PENDING_RECEIVE: // 待接单
    // intent = new Intent(context, RewardPublishWaitActivity.class);
    // intent.putExtra("order_id", model.getOrder_id());
    // context.startActivity(intent);
    // break;
    // case OrderModel.STATUS_PENDING_PAY: // 待付款
    // if (isme == 1) return;
    // intent = new Intent(context, PaySureActivity.class);
    // intent.putExtra("order_id", model.getOrder_id());
    // intent.putExtra("price", String.valueOf(model.getPrice()));
    // context.startActivity(intent);
    // break;
    // case OrderModel.STATUS_PENDING_COMMENT: // 待评论
    // intent = new Intent(context, RewardFinishActivity.class);
    // intent.putExtra("order_id", model.getOrder_id());
    // intent.putExtra("price", model.getPrice());
    // context.startActivity(intent);
    // break;
    // case OrderModel.STATUS_PENDING_RECEIVED:
    // if (isme == 1) {
    // intent = new Intent(context, RewardStartActivity.class);
    // } else {
    // intent = new Intent(context, OrderStartActivity.class);
    // }
    // intent.putExtra("order_id", model.getOrder_id());
    // intent.putExtra("shop_name", model.getShop_name());
    // intent.putExtra("shop_image", model.getImg());
    //
    // context.startActivity(intent);
    // break;
    // }
  }

  public void setOnItemClickListener(OnOrderItemClickListener orderItemClickListener) {
    this.orderItemClickListener = orderItemClickListener;
  }

  public OnOrderItemClickListener getListener() {
    return new OrderClickListener(context);
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

    if (model.getStatus() == OrderModel.STATUS_PENDING_PAY) {
      holder.status.setText("待付款");
      holder.status.setTextColor(context.getResources().getColor(R.color.text_orange));
    } else if (model.getStatus() == OrderModel.STATUS_FINISHED) {
      holder.status.setText("已完成");
      holder.status.setTextColor(context.getResources().getColor(R.color.text_green));
    } else if (model.getStatus() == OrderModel.STATUS_PENDING_COMMENT) {
      holder.status.setText("待评论");
      holder.status.setTextColor(context.getResources().getColor(R.color.text_orange));
    } else if (model.getStatus() == OrderModel.STATUS_PENDING_COMPLAINT) {
      holder.status.setText("客诉单");
      holder.status.setTextColor(context.getResources().getColor(R.color.text_red));
    } else if (model.getStatus() == OrderModel.STATUS_PENDING_RECEIVE) {
      holder.status.setText("待领取");
      holder.status.setTextColor(context.getResources().getColor(R.color.text_orange));
    } else if (model.getStatus() == OrderModel.STATUS_PENDING_RECEIVED) {
      holder.status.setText("已领取");
      holder.status.setTextColor(context.getResources().getColor(R.color.text_green));
    }
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
        orderItemClickListener.onClick(model, isme);
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
