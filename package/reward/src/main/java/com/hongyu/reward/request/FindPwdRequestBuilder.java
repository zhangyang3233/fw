package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BaseModel;

/**
 * Created by zhangyang131 on 16/9/10.
 */
public class FindPwdRequestBuilder extends BaseHttpRequestBuilder<BaseModel> {
  private static final String MOBILE = "mobile";
  private static final String CAPTCHA = "captcha";
  private String mobile;
  private String captcha;

  public FindPwdRequestBuilder(String mobile, String captcha) {
    this.mobile = mobile;
    this.captcha = captcha;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_FORPWD_VERIFY;
  }

  @Override
  protected Class<BaseModel> getResponseClass() {
    return BaseModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, MOBILE, mobile);
    checkNullAndSet(params, CAPTCHA, captcha);
  }

}
