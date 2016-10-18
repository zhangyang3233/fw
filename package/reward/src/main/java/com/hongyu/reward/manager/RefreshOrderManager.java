package com.hongyu.reward.manager;

import android.text.TextUtils;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.OrderIdRequestModel;
import com.hongyu.reward.request.GetPublishOrderRequestBuilder;
import com.hongyu.reward.request.GetReceiveOrderRequestBuilder;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhangyang131 on 16/10/7.
 */
public class RefreshOrderManager {
    public static final int NONE = 0;

    public static class Prog{
        int step;
        boolean isPublish;
        String orderId;

        public Prog() {
        }

        public boolean isPublish() {
            return isPublish;
        }

        public void setPublish(boolean publish) {
            isPublish = publish;
        }

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public void stepOver(){
            synchronized (this){
                if(step == 1){ // 表示已经是最后一步
                    EventBus.getDefault().post(this);
                }else{
                    step++;
                }
            }
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

    }

    public static void getStatusOrder(){
        if(!AccountManager.getInstance().isLogin()){
            return;
        }
        final Prog prog = new Prog();
        GetPublishOrderRequestBuilder builder = new GetPublishOrderRequestBuilder();
        builder.setDataCallback(new DataCallback<OrderIdRequestModel>() {
            @Override
            public void onDataCallback(OrderIdRequestModel data) {
                if (ResponesUtil.checkModelCodeOK(data)) {
                    if (data.getData() != null && !TextUtils.isEmpty(data.getData().getOrder_id())) {
                        prog.setOrderId(data.getData().getOrder_id());
                        prog.setPublish(true);
                        EventBus.getDefault().post(prog);
                        return;
                    }
                }
                checkReceive(prog);
            }
        });
        builder.build().submit();


    }

    private static void checkReceive(final Prog prog) {
        GetReceiveOrderRequestBuilder builder2 = new GetReceiveOrderRequestBuilder();
        builder2.setDataCallback(new DataCallback<OrderIdRequestModel>() {
            @Override
            public void onDataCallback(OrderIdRequestModel data) {
                if (ResponesUtil.checkModelCodeOK(data)) {
                    if (data.getData() != null && !TextUtils.isEmpty(data.getData().getOrder_id())) { // 有接受订单
                        prog.setOrderId(data.getData().getOrder_id());
                        prog.setPublish(false);
                    }
                }
                EventBus.getDefault().post(prog);
            }
        });
        builder2.build().submit();
    }
}
