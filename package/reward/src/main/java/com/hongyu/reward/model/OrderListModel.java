package com.hongyu.reward.model;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/9/20.
 */
public class OrderListModel extends BaseModel {
  private ArrayList<OrderModel> data;

  public ArrayList<OrderModel> getData() {
    return data;
  }

  public void setData(ArrayList<OrderModel> data) {
    this.data = data;
  }
}
