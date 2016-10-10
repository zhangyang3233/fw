package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BaseModel;

/**
 * Created by zhangyang131 on 16/10/10.
 */
public class EditUserInfoRequestBuilder extends BaseHttpRequestBuilder<BaseModel> {
  private static final String NICKNAME = "nickname";
  private static final String GENDER = "gender";
  private static final String HEADER_IMG = "header_img";
  private String nickname; // String 昵称
  private String gender; // int 0男1女
  private String header_img; // file 头像图片

  public EditUserInfoRequestBuilder() {}

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getHeader_img() {
    return header_img;
  }

  public void setHeader_img(String header_img) {
    this.header_img = header_img;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_EDIT_INFO;
  }

  @Override
  protected Class<BaseModel> getResponseClass() {
    return BaseModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, NICKNAME, nickname);
    checkNullAndSet(params, GENDER, gender);
    checkNullAndSet(params, HEADER_IMG, header_img);
  }
}
