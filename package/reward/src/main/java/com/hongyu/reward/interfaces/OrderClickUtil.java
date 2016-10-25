package com.hongyu.reward.interfaces;

import android.content.Context;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.manager.OrderDeal;
import com.hongyu.reward.model.OrderInfoModel;
import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.request.GetOrderInfoRequestBuilder;
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
        OrderDeal.jumpActivityByOrder(context, orderModel);
      }

      @Override
      public void onFailed(String msg) {
        appLoadingView.dismiss();
        T.show(msg);
      }
    });

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
