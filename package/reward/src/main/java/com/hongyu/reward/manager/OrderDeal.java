package com.hongyu.reward.manager;

import android.content.Context;

import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.ui.activity.OrderDetailActivity;
import com.hongyu.reward.ui.activity.order.PublishFinishedCommentActivity;
import com.hongyu.reward.ui.activity.order.ReceiveOrderFinishedActivity;
import com.hongyu.reward.ui.activity.order.ReceiveWaitActivity;
import com.hongyu.reward.ui.activity.order.RewardPublishWaitActivity;
import com.hongyu.reward.ui.activity.order.RewardStartActivity;
import com.hongyu.reward.ui.activity.order.SelectPersonActivity;

/**
 * Created by zhangyang131 on 2016/10/25.
 */
public class OrderDeal {

  public static void jumpActivityByOrder(Context context, OrderModel orderModel) {
    if (orderModel == null) {
      return;
    }
    switch (orderModel.getStatus()) {
      case OrderModel.STATUS_PENDING_RECEIVE:// 待领取
        if (orderModel.isMePublish()) {
          RewardPublishWaitActivity.launch(context, orderModel.getOrder_id(),
              orderModel.getShop_name(), orderModel.getImg());
        }
        break;
      case OrderModel.STATUS_RECEIVED:// 已经领取
        if (orderModel.isMePublish()) { // 我自己发起的, 跳转到选择领赏人界面
          SelectPersonActivity.launch(context, orderModel.getOrder_id(), orderModel.getShop_name(),
              orderModel.getImg());
        } else {// 别人发起的
          OrderDetailActivity.launch(context, orderModel.getOrder_id(), orderModel.getShop_name(),true);
        }
        break;
      case OrderModel.STATUS_PENDING_PAY: // 待付款
        if (orderModel.isMePublish()) { // 我自己发起的, 跳转到任务开始界面,点击完成付款
          RewardStartActivity.launch(context, orderModel.getOrder_id(), orderModel.getShop_name(),
              orderModel.getImg());
        } else { // 如果是别人发起的,跳转到 任务开始界面
          ReceiveWaitActivity.launch(context, orderModel.getOrder_id());
        }
        break;
      case OrderModel.STATUS_FINISHED: // 已经完成
        if (orderModel.isMePublish()) {
          PublishFinishedCommentActivity.launch(context, orderModel.getOrder_id());
        } else {
          ReceiveOrderFinishedActivity.launch(context, orderModel.getOrder_id());
        }
        break;
      case OrderModel.STATUS_INVALID:// 失效
        break;
      case OrderModel.STATUS_CANCEL: // 取消
        break;
      case OrderModel.STATUS_APPEND:// 追加
        break;
      case OrderModel.STATUS_PENDING_COMMENT: // 待评价
        break;
    }
  }
}
