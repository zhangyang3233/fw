package com.hongyu.reward.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.fw.zycoder.utils.GlobalConfig;
import com.hongyu.reward.config.Constants;

/**
 * Created by zhangyang131 on 16/9/8.
 */
public class LoginModel extends BaseModel {
  private UserInfo data;

  public UserInfo getData() {
    return data;
  }

  public void setData(UserInfo data) {
    this.data = data;
  }

  public static class UserInfo implements BaseDataModel {
    String username;
    String phone;
    String avatar;
    // score
    float score;
    float cash;
    float lock_cash;

    int gender; // 0男1女
    String head_img;
    String nickname;
    String user_id;
    int point;


    public int getPoint() {
      return point;
    }

    public void setPoint(int point) {
      this.point = point;
    }

    public int getGender() {
      return gender;
    }

    public void setGender(int gender) {
      this.gender = gender;
    }

    public String getHead_img() {
      return head_img;
    }

    public void setHead_img(String head_img) {
      this.head_img = head_img;
    }

    public String getNickname() {
      return nickname;
    }

    public void setNickname(String nickname) {
      this.nickname = nickname;
    }

    public String getUser_id() {
      return user_id;
    }

    public void setUser_id(String user_id) {
      this.user_id = user_id;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getAvatar() {
      return avatar;
    }

    public void setAvatar(String avatar) {
      this.avatar = avatar;
    }

    public float getScore() {
      return score;
    }

    public void setScore(float score) {
      this.score = score;
    }

    public float getCash() {
      return cash;
    }

    public void setCash(float cash) {
      this.cash = cash;
    }

    public float getLock_cash() {
      return lock_cash;
    }

    public void setLock_cash(float lock_cash) {
      this.lock_cash = lock_cash;
    }

    /**
     * 将个人信息持久化到本地
     */
    public void save() {
      SharedPreferences pref = GlobalConfig.getAppContext()
          .getSharedPreferences(Constants.Pref.USER_INFO, Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = pref.edit();
      editor.putString(Constants.App.APP_USERID, getUser_id());
      editor.putString("head_img", getHead_img());
      editor.putString("username", getUsername());
      editor.putString("nickname", getNickname());
      editor.putString("avatar", getAvatar());
      editor.putString("phone", getPhone());
      editor.putInt("gender", getGender());
      editor.putFloat("score", getScore());
      editor.putFloat("cash", getCash());
      editor.putFloat("lock_cash", getLock_cash());
      editor.commit();
    }
  }
}
