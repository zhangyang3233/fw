package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BaseModel;

/**
 * Created by zhangyang131 on 16/9/10.
 */
public class GetAuthCodeRequestBuilder extends BaseHttpRequestBuilder<BaseModel> {
  public static final String MOBILE = "mobile";
  public String mobile;

  public GetAuthCodeRequestBuilder(String mobile) {
    this.mobile = mobile;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_GET_CODE;
  }

  @Override
  protected Class<BaseModel> getResponseClass() {
    return BaseModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, MOBILE, mobile);
  }

  public interface CallBack {
    void success();

    void failed(String msg);
  }
}
