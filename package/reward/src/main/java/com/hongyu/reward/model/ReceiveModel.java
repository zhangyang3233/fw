package com.hongyu.reward.model;

/**
 * Created by zhangyang131 on 16/9/19.
 */
public class ReceiveModel extends BaseModel {
  // 领赏id 2
  public String receive_id;
  // 领赏人id 321
  public String receive_uid;
  public String head_img;
  // 订单号 100011
  public String order_id;
  // 排号 10
  public String rank_num;
  // 桌人数 8
  public String table_num;
  // 等待人数 6
  public String wait_num;
  // 悬赏人 测试悬赏
  public String nickname;
  // 好评率 100%
  public String gcr;
  // 成单数 1
  public String order_num;
  public String mobile;
  public String good;
  public float score = 0;

  public String getHead_img() {
    return head_img;
  }

  public void setHead_img(String head_img) {
    this.head_img = head_img;
  }

  public String getReceive_id() {
    return receive_id;
  }

  public void setReceive_id(String receive_id) {
    this.receive_id = receive_id;
  }

  public String getReceive_uid() {
    return receive_uid;
  }

  public void setReceive_uid(String receive_uid) {
    this.receive_uid = receive_uid;
  }

  public String getOrder_id() {
    return order_id;
  }

  public void setOrder_id(String order_id) {
    this.order_id = order_id;
  }

  public String getRank_num() {
    return rank_num;
  }

  public void setRank_num(String rank_num) {
    this.rank_num = rank_num;
  }

  public String getTable_num() {
    return table_num;
  }

  public void setTable_num(String table_num) {
    this.table_num = table_num;
  }

  public String getWait_num() {
    return wait_num;
  }

  public void setWait_num(String wait_num) {
    this.wait_num = wait_num;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
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

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getGood() {
    return good;
  }

  public void setGood(String good) {
    this.good = good;
  }

  public float getScore() {
    return score;
  }

  public void setScore(float score) {
    this.score = score;
  }
}
