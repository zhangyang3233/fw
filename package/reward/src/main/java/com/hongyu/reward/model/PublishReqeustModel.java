package com.hongyu.reward.model;

import java.io.Serializable;

/**
 * Created by zhangyang131 on 16/9/13.
 */
public class PublishReqeustModel extends BaseModel implements Serializable {

    /**
     * order_id : 100442
     */
    private OrderInfo data;

    public OrderInfo getData() {
        return data;
    }

    public void setData(OrderInfo data) {
        this.data = data;
    }

    public static class OrderInfo implements BaseDataModel, Serializable {
        private String order_id;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }
    }
}
