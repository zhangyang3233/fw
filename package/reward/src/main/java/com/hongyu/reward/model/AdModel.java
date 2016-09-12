package com.hongyu.reward.model;

/**
 * 
 * model - ad - 广告数据模型
 *
 * =======================================
 * Copyright 2016-2017
 * =======================================
 *
 * @since 2016-6-17 下午10:54:29
 * @author centos
 *
 */
public class AdModel extends BaseModel {
  public int position_id;
  public String position_img;
  public String position_text;
  public String position_url;

  public int getPosition_id() {
    return position_id;
  }

  public void setPosition_id(int position_id) {
    this.position_id = position_id;
  }

  public String getPosition_img() {
    return position_img;
  }

  public void setPosition_img(String position_img) {
    this.position_img = position_img;
  }

  public String getPosition_text() {
    return position_text;
  }

  public void setPosition_text(String position_text) {
    this.position_text = position_text;
  }

  public String getPosition_url() {
    return position_url;
  }

  public void setPosition_url(String position_url) {
    this.position_url = position_url;
  }
}
