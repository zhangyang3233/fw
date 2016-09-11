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
  public int id;
  public String image;
  public String brief;
  public String url;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getBrief() {
    return brief;
  }

  public void setBrief(String brief) {
    this.brief = brief;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
