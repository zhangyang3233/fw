package com.hongyu.reward.model;

import com.fw.zycoder.utils.CollectionUtils;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/10/5.
 */
public class OrderIdRequestModel extends BaseModel {

    private ArrayList<OrderModel> data;

    public OrderModel getData() {
        if(CollectionUtils.isEmpty(data)){
            return null;
        }
        return data.get(0);
    }

}
