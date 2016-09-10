package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.LoginModel;
import com.hongyu.reward.utils.MD5;

/**
 * Created by zhangyang131 on 16/9/8.
 */

public class LoginRequestBuilder extends BaseHttpRequestBuilder<LoginModel> {
  private static final String MOBILE = "mobile";
  private static final String PASSWORD = "password";
  private String mobile;
  private String password;

  public LoginRequestBuilder(String mobile, String password) {
    this.mobile = mobile;
    this.password = password;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  protected Class<LoginModel> getResponseClass() {
    return LoginModel.class;
  }

  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, MOBILE, mobile);
    checkNullAndSet(params, PASSWORD, MD5.getMD5(password));
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_LOGIN;
  }

  public interface LoginCallback {
    void loginSuccess(LoginModel.UserInfo userInfo);

    void loginFailed(String errorMsg);
  }
}
