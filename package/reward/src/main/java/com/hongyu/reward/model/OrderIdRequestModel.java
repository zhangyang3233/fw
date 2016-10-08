package com.hongyu.reward.model;

/**
 * Created by zhangyang131 on 16/10/5.
 */
public class OrderIdRequestModel extends BaseModel {

    private OrderModel data;

    public OrderModel getData() {
        return data;
    }

    public void setData(OrderModel data) {
        this.data = data;
    }
}
