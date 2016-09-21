package com.hongyu.reward.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class MyEvaluateReceiveModel extends BaseModel {
  private String gcr;
  private String nickname;
  private String order_num;
  private float score;
  private int[] score_list;
  private ArrayList<TagModel> tag_list;

  public String getGcr() {
    return gcr;
  }

  public void setGcr(String gcr) {
    this.gcr = gcr;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getOrder_num() {
    return order_num;
  }

  public void setOrder_num(String order_num) {
    this.order_num = order_num;
  }

  public float getScore() {
    return score;
  }

  public void setScore(float score) {
    this.score = score;
  }

  public int[] getScore_list() {
    return score_list;
  }

  public void setScore_list(int[] score_list) {
    this.score_list = score_list;
  }

  public ArrayList<TagModel> getTag_list() {
    return tag_list;
  }

  public void setTag_list(ArrayList<TagModel> tag_list) {
    this.tag_list = tag_list;
  }

  public static MyEvaluateReceiveModel decode(JSONObject evaluate) {
    MyEvaluateReceiveModel model = new MyEvaluateReceiveModel();
    try {
      model.setGcr(evaluate.optString("gcr", "%0"));
      model.setScore((float) evaluate.optDouble("score", 0f));
      model.setOrder_num(evaluate.optString("order_num", "0"));
      model.setNickname(evaluate.optString("nickname", ""));
      int[] scoreIndex = new int[5];
      JSONObject scoreIndexJson = evaluate.optJSONObject("score_list");
      scoreIndex[0] = scoreIndexJson.optInt("1", 0);
      scoreIndex[1] = scoreIndexJson.optInt("2", 0);
      scoreIndex[2] = scoreIndexJson.optInt("3", 0);
      scoreIndex[3] = scoreIndexJson.optInt("4", 0);
      scoreIndex[4] = scoreIndexJson.optInt("5", 0);
      model.setScore_list(scoreIndex);
      JSONArray list = evaluate.optJSONArray("tag_list");
      ArrayList<TagModel> tags = new ArrayList<>();
      for (int i = 0; i < list.length(); i++) {
        JSONObject obj = list.optJSONObject(i);
        TagModel tag = TagModel.decode(obj);
        tags.add(tag);
      }
      model.setTag_list(tags);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return model;
  }


  public static class TagModel implements BaseDataModel {
    private String tag;
    private int num;

    public TagModel() {
    }

    public TagModel(String tag, int num) {
      this.tag = tag;
      this.num = num;
    }

    public String getTag() {
      return tag;
    }

    public void setTag(String tag) {
      this.tag = tag;
    }

    public int getNum() {
      return num;
    }

    public void setNum(int num) {
      this.num = num;
    }

    public static TagModel decode(JSONObject obj) {
      TagModel tag = new TagModel();
      tag.setTag(obj.optString("tag",""));
      tag.setNum(obj.optInt("num",0));
      return tag;
    }
  }
}
