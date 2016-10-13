package com.hongyu.reward.interfaces;

import com.hongyu.reward.model.OrderModel;

/**
 * Created by zhangyang131 on 16/10/11.
 */
public interface OnOrderClickCallback {
    void onSuccess(OrderModel orderModel);
    void onFailed(String msg);
}
