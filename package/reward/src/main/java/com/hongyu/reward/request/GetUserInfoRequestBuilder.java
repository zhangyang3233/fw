package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.LoginModel;

/**
 * Created by zhangyang131 on 16/9/20.
 */
public class GetUserInfoRequestBuilder extends BaseHttpRequestBuilder<LoginModel> {


  @Override
  protected String getApiUrl() {
    return Constants.Server.API_USER_INFO;
  }

  @Override
  protected Class<LoginModel> getResponseClass() {
    return LoginModel.class;
  }
}
