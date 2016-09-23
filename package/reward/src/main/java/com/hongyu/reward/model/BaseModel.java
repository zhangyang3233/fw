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
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
