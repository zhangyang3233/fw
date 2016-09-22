package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.utils.MD5;

/**
 * Created by zhangyang131 on 16/9/22.
 */
public class ResetPwdWithOldPwdRequestBuilder extends BaseHttpRequestBuilder<BaseModel> {
  private static final String PASSWORD = "password";
  private static final String OLD_PASSWORD = "old_password";
  private String password;
  private String old_password;

  public ResetPwdWithOldPwdRequestBuilder(String old_password, String password) {
    this.password = MD5.getMD5(password);
    this.old_password = MD5.getMD5(old_password);
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_RESET_PWD;
  }

  @Override
  protected Class<BaseModel> getResponseClass() {
    return BaseModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, PASSWORD, password);
    checkNullAndSet(params, OLD_PASSWORD, old_password);
  }
}
