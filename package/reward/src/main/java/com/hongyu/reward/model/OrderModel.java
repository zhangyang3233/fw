package com.hongyu.reward.model;

import com.hongyu.reward.manager.AccountManager;

/**
 * Created by zhangyang131 on 16/9/19.
 */
public class OrderModel implements BaseDataModel {
  public static final String IMMEDIATE = "0";
  public static final String APPOINTMENT = "1";
  // 订单状态
  public static final int STATUS_ALL = -1; // 待领取
  public static final int STATUS_PENDING_RECEIVE = 0; // 待领取
  public static final int STATUS_RECEIVED = 10; // 已经领取
  public static final int STATUS_PENDING_PAY = 20; // 待付款
  public static final int STATUS_FINISHED = 30; // 已经完成
  public static final int STATUS_INVALID = 31; // 失效
  public static final int STATUS_CANCEL = 32; // 取消
  public static final int STATUS_APPEND = 33; // 追加
  public static final int STATUS_PENDING_COMMENT = 40; // 待评价
  public static final int STATUS_PENDING_COMPLAINT = 50; // 投诉单
  // 订单状态(完)

  private String order_id;
  private String user_id;
  private String shop_id;
  private int type;
  private String price;
  private String usernum;
  private String begin_time;
  private String end_time;
  private int status;
  private String nickname;
  private String mobile;
  private String shop_name;
  private String shop_address;
  private String shop_telephone;
  private String head_img;
  private String img;
  private String date;
  private String good;
  private String gcr;
  private String order_num;
  private String isComment;
  private String mTvTime;

  public String getHead_img() {
    return head_img;
  }

  public void setHead_img(String head_img) {
    this.head_img = head_img;
  }

  public String getShop_telephone() {
    return shop_telephone;
  }

  public void setShop_telephone(String shop_telephone) {
    this.shop_telephone = shop_telephone;
  }

  public String getShop_address() {
    return shop_address;
  }

  public void setShop_address(String shop_address) {
    this.shop_address = shop_address;
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

  public String getShop_id() {
    return shop_id;
  }

  public void setShop_id(String shop_id) {
    this.shop_id = shop_id;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getUsernum() {
    return usernum;
  }

  public void setUsernum(String usernum) {
    this.usernum = usernum;
  }

  public String getBegin_time() {
    return begin_time;
  }

  public void setBegin_time(String begin_time) {
    this.begin_time = begin_time;
  }

  public String getEnd_time() {
    return end_time;
  }

  public void setEnd_time(String end_time) {
    this.end_time = end_time;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getShop_name() {
    return shop_name;
  }

  public void setShop_name(String shop_name) {
    this.shop_name = shop_name;
  }

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getGood() {
    return good;
  }

  public void setGood(String good) {
    this.good = good;
  }

  public String getGcr() {
    return gcr;
  }

  public void setGcr(String gcr) {
    this.gcr = gcr;
  }

  public String getOrder_num() {
    return order_num;
  }

  public void setOrder_num(String order_num) {
    this.order_num = order_num;
  }

  public String getIsComment() {
    return isComment;
  }

  public void setIsComment(String isComment) {
    this.isComment = isComment;
  }

  public String getmTvTime() {
    return mTvTime;
  }

  public void setmTvTime(String mTvTime) {
    this.mTvTime = mTvTime;
  }

  public boolean isMePublish(){
    return AccountManager.getInstance().getUser().getUser_id().equals(user_id);
  }
}
