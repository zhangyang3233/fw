package com.hongyu.reward.model;

/**
 * Created by zhangyang131 on 16/9/8.
 */
public class BaseModel implements BaseDataModel {
  private int code;
  private String message;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    if(code == 3011){
      return "该订单不能重复取消";
    }
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
