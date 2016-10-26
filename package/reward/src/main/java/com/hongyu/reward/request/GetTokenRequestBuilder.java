package com.hongyu.reward.request;

import android.text.TextUtils;

import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.SystemUtil;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.TokenModel;

/**
 * Created by zhangyang131 on 16/9/9.
 */
public class GetTokenRequestBuilder extends BaseHttpRequestBuilder<TokenModel> {
  private static final String DEVICE_TYPE = "device_type";
  private static final String DEVICE_CODE = "device_code";
  private static final String PUSH_CODE = "push_code";
  private String devive_type;
  private String device_code; // token
  private String push_code; // pushCode

  public GetTokenRequestBuilder() {
    this.devive_type = "2";
    this.device_code = getDeviceCode();
    this.push_code = getPushCode();
  }

  private String getDeviceCode(){
    return SystemUtil.getDeviceId(GlobalConfig.getAppContext().getApplicationContext());
  }

  private String getPushCode(){
    String pushCode = AccountManager.getInstance().getPushCode();
    if(TextUtils.isEmpty(pushCode)){
      return getDeviceCode();
    }
    return pushCode;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_GET_TOKEN;
  }

  @Override
  protected Class<TokenModel> getResponseClass() {
    return TokenModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, DEVICE_CODE, device_code);
    checkNullAndSet(params, DEVICE_TYPE, devive_type);
    checkNullAndSet(params, PUSH_CODE, push_code);
  }
}
