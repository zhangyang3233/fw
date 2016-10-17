package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BaseModel;

/**
 * 提现
 * Created by zhangyang131 on 16/10/17.
 */
public class WithdrawalsRequestBuilder extends BaseHttpRequestBuilder<BaseModel> {
  private static final String ACCOUNT = "account";
  private static final String REAL_NAME = "real_name";
  private static final String PRICE = "price";
  String account;
  String real_name;
  float price;

  public WithdrawalsRequestBuilder(String account, String real_name, float price) {
    this.account = account;
    this.real_name = real_name;
    this.price = price;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_ACCOUNT_GET;
  }

  @Override
  protected Class<BaseModel> getResponseClass() {
    return BaseModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, ACCOUNT, account);
    checkNullAndSet(params, REAL_NAME, real_name);
    checkNullAndSet(params, PRICE, price);
  }
}
