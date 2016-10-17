package com.hongyu.reward.http;

import android.os.Looper;

import com.fw.zycoder.http.callback.DataFuture;
import com.hongyu.reward.model.BillDetailModel;
import com.hongyu.reward.model.MsgModel;
import com.hongyu.reward.model.OrderListModel;
import com.hongyu.reward.model.ReasonModel;
import com.hongyu.reward.model.RewardListModel;
import com.hongyu.reward.model.ShopListMode;
import com.hongyu.reward.request.GetBillRequestBuilder;
import com.hongyu.reward.request.GetMsgListRequestBuilder;
import com.hongyu.reward.request.GetOrderListRequestBuilder;
import com.hongyu.reward.request.GetReasonListRequestModel;
import com.hongyu.reward.request.GetReceiveShopListRequestBuilder;
import com.hongyu.reward.request.GetRewardListRequestBuilder;
import com.hongyu.reward.request.GetShopListRequestBuilder;

/**
 * Created by zhangyang131 on 16/9/12.
 */
public class HttpHelper {
  public static void checkNonUIThread() {
    if (Looper.getMainLooper() == Looper.myLooper()) {
      throw new IllegalStateException("Cannot call in UI thread.");
    }
  }

  /**
   * 获取悬赏商家列表
   * 
   * @param page
   * @param location
   * @param key
   * @return
   */
  public static ShopListMode getShopList(String page, String location, String city, String key) {
    checkNonUIThread();
    GetShopListRequestBuilder builder = new GetShopListRequestBuilder(page, location, city,key);
    DataFuture<ShopListMode> future = builder.build().submitSync();
    if (future == null) {
      return null;
    }
    return future.get();
  }

  /**
   * 获取可接单商家列表
   * 
   * @param page
   * @param location
   * @param key
   * @return
   */
  public static ShopListMode getReceiveShopList(String page, String location, String city,  String key) {
    checkNonUIThread();
    GetReceiveShopListRequestBuilder builder =
        new GetReceiveShopListRequestBuilder(page, location, city, key);
    DataFuture<ShopListMode> future = builder.build().submitSync();
    if (future == null) {
      return null;
    }
    return future.get();
  }

  /**
   * 获取商家悬赏信息列表
   * 
   * @param page
   * @param shopId
   * @return
   */
  public static RewardListModel getShopOrderList(String page, String shopId) {
    checkNonUIThread();
    GetRewardListRequestBuilder builder = new GetRewardListRequestBuilder(page, shopId);
    DataFuture<RewardListModel> future = builder.build().submitSync();
    if (future == null) {
      return null;
    }
    return future.get();
  }


  /**
   * 获取订单列表
   * 
   * @return
   */
  public static OrderListModel getOrderList(String status, String isme, String page) {
    checkNonUIThread();
    GetOrderListRequestBuilder builder = new GetOrderListRequestBuilder(status, isme, page);
    DataFuture<OrderListModel> future = builder.build().submitSync();
    if (future == null) {
      return null;
    }
    return future.get();
  }

  /**
   * 获取消息列表
   *
   * @return
   */
  public static MsgModel getMsgList(String page) {
    checkNonUIThread();
    GetMsgListRequestBuilder builder = new GetMsgListRequestBuilder(page);
    DataFuture<MsgModel> future = builder.build().submitSync();
    if (future == null) {
      return null;
    }
    return future.get();
  }

  /**
   * 获取投诉原因列表
   *
   * @return
   */
  public static ReasonModel getReasonList() {
    checkNonUIThread();
    GetReasonListRequestModel builder = new GetReasonListRequestModel();
    DataFuture<ReasonModel> future = builder.build().submitSync();
    if (future == null) {
      return null;
    }
    return future.get();
  }

  /**
   * @param type 账户类型 1:现金账户;2:积分账户;
   * @param page
   * @return
     */
  public static BillDetailModel getBillList(String type, String page){
    checkNonUIThread();
    GetBillRequestBuilder builder = new GetBillRequestBuilder(type, page);
    DataFuture<BillDetailModel> future = builder.build().submitSync();
    if (future == null) {
      return null;
    }
    return future.get();
  }

}
