package com.hongyu.reward.model;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class ReasonModel extends BaseModel {
  private ArrayList<Reason> data;

  public ArrayList<Reason> getData() {
    return data;
  }

  public void setData(ArrayList<Reason> data) {
    this.data = data;
  }

  public ReasonModel(ArrayList<Reason> data) {
    this.data = data;
  }

  public static class Reason {
    String id;
    String reason;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getReason() {
      return reason;
    }

    public void setReason(String reason) {
      this.reason = reason;
    }
  }
}
