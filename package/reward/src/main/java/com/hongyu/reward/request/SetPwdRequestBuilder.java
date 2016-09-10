package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.utils.MD5;

/**
 * Created by zhangyang131 on 16/9/10.
 */
public class SetPwdRequestBuilder extends BaseHttpRequestBuilder<BaseModel> {
  public static final String MOBILE = "mobile";
  public static final String PASSWORD = "password";
  private String mobile;
  private String password;

  public SetPwdRequestBuilder(String mobile, String password) {
    this.mobile = mobile;
    this.password = password;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_FORPWD;
  }

  @Override
  protected Class<BaseModel> getResponseClass() {
    return BaseModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, MOBILE, mobile);
    checkNullAndSet(params, PASSWORD, MD5.getMD5(password));
  }
}
