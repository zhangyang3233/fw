package com.hongyu.reward.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/10/12.
 */
public class CommentTagModel extends BaseModel {

  private ArrayList<CommentTag> c1 = new ArrayList<>();
  private ArrayList<CommentTag> c2 = new ArrayList<>();
  private ArrayList<CommentTag> c3 = new ArrayList<>();
  private ArrayList<CommentTag> c4 = new ArrayList<>();
  private ArrayList<CommentTag> c5 = new ArrayList<>();

  public ArrayList<CommentTag> getC1() {
    return c1;
  }

  public void setC1(ArrayList<CommentTag> c1) {
    this.c1 = c1;
  }

  public ArrayList<CommentTag> getC2() {
    return c2;
  }

  public void setC2(ArrayList<CommentTag> c2) {
    this.c2 = c2;
  }

  public ArrayList<CommentTag> getC3() {
    return c3;
  }

  public void setC3(ArrayList<CommentTag> c3) {
    this.c3 = c3;
  }

  public ArrayList<CommentTag> getC4() {
    return c4;
  }

  public void setC4(ArrayList<CommentTag> c4) {
    this.c4 = c4;
  }

  public ArrayList<CommentTag> getC5() {
    return c5;
  }

  public void setC5(ArrayList<CommentTag> c5) {
    this.c5 = c5;
  }

  public static CommentTagModel parse(JSONObject jsonObject) {
    CommentTagModel ct = new CommentTagModel();
    try {
      JSONArray jsonArray1 = jsonObject.getJSONArray("1");
      ct.c1 = parseTag(jsonArray1);
      JSONArray jsonArray2 = jsonObject.getJSONArray("2");
      ct.c2 = parseTag(jsonArray2);
      JSONArray jsonArray3 = jsonObject.getJSONArray("3");
      ct.c3 = parseTag(jsonArray3);
      JSONArray jsonArray4 = jsonObject.getJSONArray("4");
      ct.c4 = parseTag(jsonArray4);
      JSONArray jsonArray5 = jsonObject.getJSONArray("5");
      ct.c5 = parseTag(jsonArray5);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return ct;
  }


  private static ArrayList<CommentTag> parseTag(JSONArray jsonArray) {
    ArrayList<CommentTag> tags = new ArrayList<>();

    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject obj = jsonArray.optJSONObject(i);
      CommentTag tag = new CommentTag();
      tag.id = obj.optInt("id");
      tag.score = obj.optInt("score");
      tag.tag = obj.optString("tag");
      tags.add(tag);
    }
    return tags;
  }

  public static class CommentTag {
    int id;
    int score;
    String tag;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public int getScore() {
      return score;
    }

    public void setScore(int score) {
      this.score = score;
    }

    public String getTag() {
      return tag;
    }

    public void setTag(String tag) {
      this.tag = tag;
    }
  }
}
