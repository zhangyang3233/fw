package com.hongyu.reward.model;

/**
 * Created by zhangyang131 on 16/10/9.
 */
public class ReceiveOrderInfo extends BaseModel {
  String orderId;
  String shopName;
  String orderImg;
  String indexNum;
  String waitNum;
  String pNum;

  public ReceiveOrderInfo(String orderId, String shopName, String orderImg, String indexNum,
      String waitNum, String pNum) {
    this.orderId = orderId;
    this.shopName = shopName;
    this.orderImg = orderImg;
    this.indexNum = indexNum;
    this.waitNum = waitNum;
    this.pNum = pNum;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getShopName() {
    return shopName;
  }

  public void setShopName(String shopName) {
    this.shopName = shopName;
  }

  public String getOrderImg() {
    return orderImg;
  }

  public void setOrderImg(String orderImg) {
    this.orderImg = orderImg;
  }

  public String getIndexNum() {
    return indexNum;
  }

  public void setIndexNum(String indexNum) {
    this.indexNum = indexNum;
  }

  public String getWaitNum() {
    return waitNum;
  }

  public void setWaitNum(String waitNum) {
    this.waitNum = waitNum;
  }

  public String getpNum() {
    return pNum;
  }

  public void setpNum(String pNum) {
    this.pNum = pNum;
  }
}
