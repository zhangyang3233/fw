package com.hongyu.reward.model;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/9/19.
 */
public class RewardListModel extends BaseModel {
  private ArrayList<RewardModel> data;

  public ArrayList<RewardModel> getData() {
    return data;
  }

  public void setData(ArrayList<RewardModel> data) {
    this.data = data;
  }
}
