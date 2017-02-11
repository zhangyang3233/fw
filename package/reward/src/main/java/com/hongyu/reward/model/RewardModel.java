package com.hongyu.reward.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/9/19.
 */
public class RewardModel extends BaseModel implements Serializable {
  // 商家图片
  public String img;
  // 商家id
  public String shop_id;
  // 商家名称
  public String shop_name;
  // 悬赏人
  public String nickname;
  // 悬赏金额
  public String price;
  // 订单号
  public String order_id;
  public String user_id;
  public String mobile;
  public ArrayList<String> taglist = new ArrayList<String>();
  public float score;


  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public String getShop_id() {
    return shop_id;
  }

  public void setShop_id(String shop_id) {
    this.shop_id = shop_id;
  }

  public String getShop_name() {
    return shop_name;
  }

  public void setShop_name(String shop_name) {
    this.shop_name = shop_name;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getOrder_id() {
    return order_id;
  }

  public void setOrder_id(String order_id) {
    this.order_id = order_id;
  }

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public ArrayList<String> getTaglist() {
    return taglist;
  }

  public void setTaglist(ArrayList<String> taglist) {
    this.taglist = taglist;
  }

  public float getScore() {
    return score;
  }

  public void setScore(float score) {
    this.score = score;
  }
}
