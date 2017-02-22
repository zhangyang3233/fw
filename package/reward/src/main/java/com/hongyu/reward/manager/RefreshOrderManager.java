package com.hongyu.reward.manager;

import android.text.TextUtils;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.Log;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.OrderIdRequestModel;
import com.hongyu.reward.request.GetPublishOrderRequestBuilder;
import com.hongyu.reward.request.GetReceiveOrderRequestBuilder;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhangyang131 on 16/10/7.
 */
public class RefreshOrderManager {

  public static class Prog {
    boolean isPublish;
    String orderId;

    public Prog(boolean isPublish) {
      this.isPublish = isPublish;
    }

    public boolean isPublish() {
      return isPublish;
    }

    public void setPublish(boolean publish) {
      isPublish = publish;
    }

    public String getOrderId() {
      return orderId;
    }

    public void setOrderId(String orderId) {
      this.orderId = orderId;
    }

  }

  public static void getStatusOrder() {
    if (!AccountManager.getInstance().isLogin()) {
      return;
    }

    GetPublishOrderRequestBuilder builder = new GetPublishOrderRequestBuilder();
    builder.setDataCallback(new DataCallback<OrderIdRequestModel>() {
      @Override
      public void onDataCallback(OrderIdRequestModel data) {
        final Prog prog = new Prog(true);
        if (ResponesUtil.checkModelCodeOK(data)) {
          if (data.getData() != null && !TextUtils.isEmpty(data.getData().getOrder_id())) {
            prog.setOrderId(data.getData().getOrder_id());
            Log.i("notice", "有发布：orderID：" + data.getData().getOrder_id());
          } else {
            Log.i("notice", "无发布");
          }

        } else {
          if(data == null){
            Log.i("notice", "检查发布异常：data == null" );
          }else if(data.getMessage() == null){
            Log.i("notice", "检查发布异常：data.getMessage() == null" );
          }else{
            Log.i("notice", "检查发布异常：" +data.getMessage());
          }
        }
        EventBus.getDefault().post(prog);
      }
    });
    Log.i("notice", "检查发布");
    builder.build().submit();
    checkReceive();
  }

  private static void checkReceive() {
    GetReceiveOrderRequestBuilder builder2 = new GetReceiveOrderRequestBuilder();
    builder2.setDataCallback(new DataCallback<OrderIdRequestModel>() {
      @Override
      public void onDataCallback(OrderIdRequestModel data) {
        final Prog prog = new Prog(false);
        if (ResponesUtil.checkModelCodeOK(data)) {
          if (data.getData() != null && !TextUtils.isEmpty(data.getData().getOrder_id())) { // 有接受订单
            prog.setOrderId(data.getData().getOrder_id());
            Log.i("notice", "有接受：orderID：" + data.getData().getOrder_id());
          } else {
            Log.i("notice", "无接受");
          }
        } else {
          Log.i("notice", "检查接受异常：" + (data != null ? data.getMessage() : "data==null!"));
        }
        EventBus.getDefault().post(prog);
      }
    });
    Log.i("notice", "检查接受");
    builder2.build().submit();
  }
}
