package com.hongyu.reward.model;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/10/12.
 */
public class OrderCommentModel extends BaseModel {

  OrderCommentInfo data;

  public OrderCommentInfo getData() {
    return data;
  }

  public void setData(OrderCommentInfo data) {
    this.data = data;
  }

  public static class OrderCommentInfo implements BaseDataModel{
    String content;
    String score;
    ArrayList<String> tag;

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public String getScore() {
      return score;
    }

    public void setScore(String score) {
      this.score = score;
    }

    public ArrayList<String> getTag() {
      return tag;
    }

    public void setTag(ArrayList<String> tag) {
      this.tag = tag;
    }
  }
}
