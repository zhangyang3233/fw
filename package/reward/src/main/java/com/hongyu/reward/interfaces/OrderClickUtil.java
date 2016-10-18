package com.hongyu.reward.interfaces;

import android.content.Context;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.OrderInfoModel;
import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.request.GetOrderInfoRequestBuilder;
import com.hongyu.reward.ui.activity.order.PublishFinishedCommentActivity;
import com.hongyu.reward.ui.activity.order.ReceiveOrderFinishedActivity;
import com.hongyu.reward.ui.activity.order.ReceiveWaitActivity;
import com.hongyu.reward.ui.activity.order.RewardPublishWaitActivity;
import com.hongyu.reward.ui.activity.order.RewardStartActivity;
import com.hongyu.reward.ui.activity.order.SelectPersonActivity;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.AppLoadingView;

/**
 * Created by zhangyang131 on 16/10/11.
 */
public class OrderClickUtil {
  public static void orderOnClick(final Context context, String orderId) {
    final AppLoadingView appLoadingView = new AppLoadingView(context);
    appLoadingView.show();
    getOrderInfoById(orderId, new OnOrderClickCallback() {
      @Override
      public void onSuccess(OrderModel orderModel) {
        appLoadingView.dismiss();
        jumpActivityByOrder(context, orderModel);
      }

      @Override
      public void onFailed(String msg) {
        appLoadingView.dismiss();
        T.show(msg);
      }
    });

  }

  private static void jumpActivityByOrder(Context context, OrderModel orderModel) {
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
          T.show("等待发起人确认中...");
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
        }else{
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

  public static void getOrderInfoById(String orderId, final OnOrderClickCallback callback) {
    if (callback == null) {
      return;
    }
    GetOrderInfoRequestBuilder builder = new GetOrderInfoRequestBuilder(orderId);
    builder.setDataCallback(new DataCallback<OrderInfoModel>() {
      @Override
      public void onDataCallback(OrderInfoModel data) {
        if (ResponesUtil.checkModelCodeOK(data)) {
          callback.onSuccess(data.getData().getOrder());
        } else {
          callback.onFailed(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }

  // private static void showLoading(Context context) {
  // if (context instanceof AppAsyncActivity) {
  // ((AppAsyncActivity) context).showLoadingView();
  // }
  // }
  //
  // private static void dismissLoading(Context context) {
  // if (context instanceof AppAsyncActivity) {
  // ((AppAsyncActivity) context).dissmissLoadingView();
  // }
  // }
}
